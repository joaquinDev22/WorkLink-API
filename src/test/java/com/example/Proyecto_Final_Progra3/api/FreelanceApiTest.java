package com.example.Proyecto_Final_Progra3.api;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * ProyectoController, PostulacionProyectoController, HitoController, PagosController, MercadoPagoWebhookController,
 * CalificacionProyectoController, ClientController, FreelancerController.
 * Datos demo: cliente 150 (demo_cliente); 382 BUSCANDO_TALENTO con propuesta de 316; 392 EN_PROGRESO (freelancer 316,
 * hitos 548..551 PENDIENTE); 394 EN_PROGRESO con hitos pagados; 400 FINALIZADO (316).
 */
class FreelanceApiTest extends ApiTestBase {

    private String proyectoJson(String nombre) {
        return """
                {"nombreProyecto":"%s","area":"IT","descripcion":"Proyecto de prueba","presupuestoMax":300000,
                 "presupuestoMin":100000,"duracion":"2 meses","tipoPago":"POR_HITO","habilidades":["Java"]}""".formatted(nombre);
    }

    // ---------- Proyectos ----------
    @Test void proyectos_listar_publico() throws Exception { get(null, "/api/v1/freelance/proyectos").andExpect(status().isOk()); }
    @Test void proyectos_porId_publico() throws Exception { get(null, "/api/v1/freelance/proyectos/382").andExpect(status().isOk()); }
    @Test void proyectos_porId_inexistente_404() throws Exception { get(null, "/api/v1/freelance/proyectos/999999").andExpect(status().isNotFound()); }
    @Test void proyectos_porUuid_publico() throws Exception {
        String uuid = jdbc.queryForObject("select uuid from proyecto where proyecto_id=382", String.class);
        get(null, "/api/v1/freelance/proyectos/uuid/{u}", uuid).andExpect(status().isOk());
    }
    @Test void proyectos_misPublicados() throws Exception { get(CLIENTE, "/api/v1/freelance/proyectos/me/publicados").andExpect(status().isOk()); }
    @Test void proyectos_misAsignados() throws Exception {
        for (String url : new String[]{"/api/v1/freelance/proyectos/me/asignados", "/api/v1/freelance/proyectos/me/historico", "/api/v1/freelance/proyectos/historico"})
            get(FREELANCER, url).andExpect(status().isOk());
    }

    @Test void proyectos_crear() throws Exception {
        postJson(CLIENTE, "/api/v1/freelance/proyectos/me", proyectoJson("Proyecto Test")).andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("BUSCANDO_TALENTO"));
    }

    @Test void proyectos_crear_nombreLargo_400() throws Exception {
        // nombre_proyecto es VARCHAR(30): debería ser 400 de validación, no un error de base
        postJson(CLIENTE, "/api/v1/freelance/proyectos/me", proyectoJson("N".repeat(45))).andExpect(status().isBadRequest());
    }

    @Test void proyectos_crear_descripcionLarga_400() throws Exception {
        String json = proyectoJson("Desc larga").replace("Proyecto de prueba", "D".repeat(400));
        postJson(CLIENTE, "/api/v1/freelance/proyectos/me", json).andExpect(status().isBadRequest());
    }

    @Test void proyectos_actualizar() throws Exception {
        putJson(CLIENTE, "/api/v1/freelance/proyectos/383", proyectoJson("Editado")).andExpect(status().isOk());
    }

    @Test void proyectos_actualizarAjeno_403() throws Exception {
        Integer ajeno = id("select min(proyecto_id) from proyecto where cliente_id<>150 and estado='BUSCANDO_TALENTO'");
        putJson(CLIENTE, "/api/v1/freelance/proyectos/{id}", proyectoJson("Hack"), ajeno).andExpect(status().isForbidden());
    }

    @Test void proyectos_cancelar() throws Exception {
        int st = call(CLIENTE, put("/api/v1/freelance/proyectos/384/cancelar")).andReturn().getResponse().getStatus();
        org.junit.jupiter.api.Assertions.assertTrue(st == 200 || st == 204, "fue " + st);
    }

    @Test void proyectos_asignarFreelancer() throws Exception {
        call(CLIENTE, put("/api/v1/freelance/proyectos/382/asignar").param("employeeId", "316"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.estado").value("EN_PROGRESO"));
    }

    // ---------- Propuestas ----------
    @Test void propuestas_enviar() throws Exception {
        Integer emp = id("select e.employee_id from employee e join perfil p on p.perfil_id=e.perfil_id where p.usuario='freelancer05'");
        jdbc.update("delete from postulacion_proyecto where proyecto_id=382 and employee_id=?", emp);
        postJson("freelancer05", "/api/v1/freelance/proyectos/382/postulaciones/me", """
                {"descripcion":"Puedo hacerlo","costo":250000,"employeeId":%d}""".formatted(emp)).andExpect(status().isOk());
    }

    @Test void propuestas_superaPresupuesto_rechazada() throws Exception {
        Integer emp = id("select e.employee_id from employee e join perfil p on p.perfil_id=e.perfil_id where p.usuario='freelancer06'");
        jdbc.update("delete from postulacion_proyecto where proyecto_id=382 and employee_id=?", emp);
        int st = postJson("freelancer06", "/api/v1/freelance/proyectos/382/postulaciones/me", """
                {"descripcion":"Caro","costo":99999999,"employeeId":%d}""".formatted(emp)).andReturn().getResponse().getStatus();
        org.junit.jupiter.api.Assertions.assertTrue(st == 400 || st == 409 || st == 422, "fue " + st);
    }

    @Test void propuestas_listar() throws Exception {
        get(CLIENTE, "/api/v1/freelance/proyectos/382/postulaciones").andExpect(status().isOk());
    }

    // ---------- Hitos ----------
    @Test void hitos_crear() throws Exception {
        // 392 tiene 4 hitos que ya suman el total: se libera el último para poder crear uno
        jdbc.update("delete from hito_proyecto where hito_id=551");
        postJson(CLIENTE, "/api/v1/freelance/proyectos/392/hitos", """
                {"titulo":"Hito test","descripcion":"desc","fechaLimite":"%s"}""".formatted(LocalDate.now().plusDays(20)))
                .andExpect(status().isCreated());
    }

    @Test void hitos_crear_enProyectoSinAdjudicar_rechazado() throws Exception {
        int st = postJson(CLIENTE, "/api/v1/freelance/proyectos/382/hitos", """
                {"titulo":"Hito test"}""").andReturn().getResponse().getStatus();
        org.junit.jupiter.api.Assertions.assertTrue(st == 400 || st == 409 || st == 422, "fue " + st);
    }

    @Test void hitos_listar() throws Exception { get(CLIENTE, "/api/v1/freelance/proyectos/392/hitos").andExpect(status().isOk()); }

    @Test void hitos_listar_proyectoAjeno_403() throws Exception {
        get("cliente05", "/api/v1/freelance/proyectos/392/hitos").andExpect(status().isForbidden());
    }

    @Test void hitos_completar() throws Exception {
        jdbc.update("update hito_proyecto set estado='EN_PROGRESO' where hito_id=548");
        call(FREELANCER, put("/api/v1/freelance/proyectos/392/hitos/548/estado").param("nuevoEstado", "COMPLETADO"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.estado").value("COMPLETADO"));
    }

    @Test void hitos_pasarAEnProgresoManual_rechazado() throws Exception {
        int st = call(CLIENTE, put("/api/v1/freelance/proyectos/392/hitos/548/estado").param("nuevoEstado", "EN_PROGRESO"))
                .andReturn().getResponse().getStatus();
        org.junit.jupiter.api.Assertions.assertTrue(st == 400 || st == 409 || st == 422, "fue " + st);
    }

    // ---------- Pagos ----------
    @Test void pagos_listar() throws Exception { get(CLIENTE, "/api/v1/freelance/proyectos/394/pagos").andExpect(status().isOk()); }
    @Test void pagos_listar_freelancer() throws Exception { get(FREELANCER, "/api/v1/freelance/proyectos/394/pagos").andExpect(status().isOk()); }

    @Test void pagos_checkout() throws Exception {
        call(CLIENTE, post("/api/v1/freelance/proyectos/392/pagos/checkout").param("hitoId", "548"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.initPoint", notNullValue()));
    }

    @Test void pagos_checkout_hitoFueraDeOrden_rechazado() throws Exception {
        int st = call(CLIENTE, post("/api/v1/freelance/proyectos/392/pagos/checkout").param("hitoId", "550"))
                .andReturn().getResponse().getStatus();
        org.junit.jupiter.api.Assertions.assertTrue(st == 400 || st == 409 || st == 422, "fue " + st);
    }

    @Test void webhook_sinDatos_200() throws Exception {
        call(null, post("/api/v1/pagos/webhook")).andExpect(status().isOk());
    }

    @Test void webhook_pagoDesconocido_noDa500() throws Exception {
        // El adapter real lanza BusinessRuleException si MP no encuentra el pago
        org.mockito.Mockito.when(mercadoPagoAdapter.consultarPago(123L))
                .thenThrow(new com.example.Proyecto_Final_Progra3.exceptions.domain.BusinessRuleException("no existe"));
        int st = call(null, post("/api/v1/pagos/webhook").param("type", "payment").param("data.id", "123"))
                .andReturn().getResponse().getStatus();
        org.junit.jupiter.api.Assertions.assertTrue(st < 500, "fue " + st);
    }

    // ---------- Calificaciones ----------
    @Test void calificar() throws Exception {
        jdbc.update("delete from calificacion_usuario where calificacion_id in (select calificacion_id from calificacion_proyecto where proyecto_id=400)");
        jdbc.update("delete from calificacion_proyecto where proyecto_id=400");
        postJson(CLIENTE, "/api/v1/freelance/proyectos/400/calificar", """
                {"puntaje":5,"comentario":"Excelente trabajo"}""").andExpect(status().isCreated());
    }

    @Test void calificar_proyectoNoFinalizado_rechazado() throws Exception {
        int st = postJson(CLIENTE, "/api/v1/freelance/proyectos/392/calificar", """
                {"puntaje":5,"comentario":"Antes de tiempo"}""").andReturn().getResponse().getStatus();
        org.junit.jupiter.api.Assertions.assertTrue(st == 400 || st == 409 || st == 422, "fue " + st);
    }

    @Test void calificaciones_deProyecto() throws Exception { get(CLIENTE, "/api/v1/freelance/proyectos/400/calificaciones").andExpect(status().isOk()); }
    @Test void reputacion_publica() throws Exception { get(null, "/api/v1/perfiles/762/calificaciones").andExpect(status().isOk()); }
    @Test void miReputacion_perfiles() throws Exception { get(FREELANCER, "/api/v1/perfiles/me/calificaciones").andExpect(status().isOk()); }
    @Test void miReputacion_freelance() throws Exception { get(FREELANCER, "/api/v1/freelance/calificaciones/me").andExpect(status().isOk()); }
    @Test void miReputacion_freelancerOpiniones() throws Exception { get(FREELANCER, "/api/v1/freelancer/opiniones").andExpect(status().isOk()); }
    @Test void miReputacion_freelancerCalificaciones() throws Exception { get(FREELANCER, "/api/v1/freelancer/calificaciones").andExpect(status().isOk()); }

    // ---------- Cliente ----------
    @Test void cliente_dashboard() throws Exception { get(CLIENTE, "/api/v1/client/dashboard").andExpect(status().isOk()); }
    @Test void cliente_proyectos() throws Exception { get(CLIENTE, "/api/v1/client/proyectos").andExpect(status().isOk()); }
    @Test void cliente_me() throws Exception { get(CLIENTE, "/api/v1/client/me").andExpect(status().isOk()); }
    @Test void cliente_actualizarMe() throws Exception {
        putJson(CLIENTE, "/api/v1/client/me", """
                {"firstName":"Demo","lastName":"Cliente","phoneNumber":"3510000000","country":"Argentina","fechaNacimiento":"1990-01-01"}""")
                .andExpect(status().isOk());
    }

    // ---------- Freelancer ----------
    @Test void freelancer_finanzas() throws Exception { get(FREELANCER, "/api/v1/freelancer/finances").andExpect(status().isOk()); }
    @Test void freelancer_transacciones() throws Exception { get(FREELANCER, "/api/v1/freelancer/transactions").andExpect(status().isOk()); }
    @Test void freelancer_proyectos() throws Exception {
        for (String url : new String[]{"/api/v1/freelancer/proyectos", "/api/v1/freelancer/proyectos/historico", "/api/v1/freelancer/proyectos/asignados"})
            get(FREELANCER, url).andExpect(status().isOk());
    }

    @Test void freelancer_medioCobro_crud() throws Exception {
        putJson(FREELANCER, "/api/v1/freelancer/medios-cobro", """
                {"tipo":"TRANSFERENCIA","titular":"Demo Freelancer","documentoTitular":"30123456",
                 "cbuCvu":"0000003100012345678901","alias":"demo.free.test"}""").andExpect(status().isOk());
        get(FREELANCER, "/api/v1/freelancer/medios-cobro").andExpect(status().isOk());
        int st = delete(FREELANCER, "/api/v1/freelancer/medios-cobro").andReturn().getResponse().getStatus();
        org.junit.jupiter.api.Assertions.assertTrue(st == 200 || st == 204, "fue " + st);
    }

    @Test void freelancer_medioCobro_cbuInvalido_400() throws Exception {
        putJson(FREELANCER, "/api/v1/freelancer/medios-cobro", """
                {"tipo":"TRANSFERENCIA","titular":"X","cbuCvu":"123"}""").andExpect(status().isBadRequest());
    }
}
