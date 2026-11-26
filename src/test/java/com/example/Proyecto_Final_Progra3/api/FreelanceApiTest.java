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
 * Escenario demo (IDs buscados en ApiTestBase): proyBuscando con propuesta de demo_freelancer; proyEnCurso con
 * todos los hitos PENDIENTE; proyConPagos con hitos pagados; proyFinalizado con calificaciones.
 */
class FreelanceApiTest extends ApiTestBase {

    private String proyectoJson(String nombre) {
        return """
                {"nombreProyecto":"%s","area":"IT","descripcion":"Proyecto de prueba","presupuestoMax":300000,
                 "presupuestoMin":100000,"duracion":"2 meses","tipoPago":"POR_HITO","habilidades":["Java"]}""".formatted(nombre);
    }

    // ---------- Proyectos ----------
    @Test void proyectos_listar_publico() throws Exception { get(null, "/api/v1/freelance/proyectos").andExpect(status().isOk()); }
    @Test void proyectos_porId_publico() throws Exception { get(null, "/api/v1/freelance/proyectos/" + proyBuscando).andExpect(status().isOk()); }
    @Test void proyectos_porId_inexistente_404() throws Exception { get(null, "/api/v1/freelance/proyectos/999999").andExpect(status().isNotFound()); }
    @Test void proyectos_porUuid_publico() throws Exception {
        String uuid = jdbc.queryForObject("select uuid from proyecto where proyecto_id=" + proyBuscando, String.class);
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

    @Test void proyectos_crear_conLosLargosDelFormulario() throws Exception {
        // El formulario manda áreas como "Desarrollo Web" y títulos de hasta 120 caracteres
        String json = proyectoJson("T".repeat(120)).replace("\"IT\"", "\"Desarrollo Web\"")
                .replace("Proyecto de prueba", "D".repeat(3000));
        postJson(CLIENTE, "/api/v1/freelance/proyectos/me", json).andExpect(status().isOk());
        sincronizar();
    }

    @Test void proyectos_crear_nombreLargo_400_mensajeSinNombreDeCampo() throws Exception {
        postJson(CLIENTE, "/api/v1/freelance/proyectos/me", proyectoJson("N".repeat(121))).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details[0]").value("El título del proyecto no puede superar los 120 caracteres"));
    }

    @Test void proyectos_crear_descripcionLarga_400() throws Exception {
        String json = proyectoJson("Desc larga").replace("Proyecto de prueba", "D".repeat(3001));
        postJson(CLIENTE, "/api/v1/freelance/proyectos/me", json).andExpect(status().isBadRequest());
    }

    @Test void proyectos_actualizar() throws Exception {
        putJson(CLIENTE, "/api/v1/freelance/proyectos/" + proyBuscandoOtro, proyectoJson("Editado")).andExpect(status().isOk());
    }

    @Test void proyectos_actualizarAjeno_403() throws Exception {
        Integer ajeno = id("select min(proyecto_id) from proyecto where cliente_id<>" + clienteId + " and estado='BUSCANDO_TALENTO'");
        putJson(CLIENTE, "/api/v1/freelance/proyectos/{id}", proyectoJson("Hack"), ajeno).andExpect(status().isForbidden());
    }

    @Test void proyectos_cancelar() throws Exception {
        int st = call(CLIENTE, put("/api/v1/freelance/proyectos/" + proyBuscandoOtro + "/cancelar")).andReturn().getResponse().getStatus();
        org.junit.jupiter.api.Assertions.assertTrue(st == 200 || st == 204, "fue " + st);
    }

    @Test void proyectos_eliminar_conPropuestas() throws Exception {
        // proyBuscando tiene una propuesta de demo_freelancer: se borra junto con el proyecto
        delete(CLIENTE, "/api/v1/freelance/proyectos/{id}", proyBuscando).andExpect(status().isNoContent());
        sincronizar();
        org.junit.jupiter.api.Assertions.assertEquals(0, id("select count(*) from proyecto where proyecto_id=?", proyBuscando));
        org.junit.jupiter.api.Assertions.assertEquals(0, id("select count(*) from postulacion_proyecto where proyecto_id=?", proyBuscando));
    }

    @Test void proyectos_eliminarEnCurso_422() throws Exception {
        delete(CLIENTE, "/api/v1/freelance/proyectos/{id}", proyEnCurso).andExpect(status().isUnprocessableEntity());
    }

    @Test void proyectos_eliminarAjeno_403() throws Exception {
        Integer ajeno = id("select min(proyecto_id) from proyecto where cliente_id<>" + clienteId + " and estado='BUSCANDO_TALENTO'");
        delete(CLIENTE, "/api/v1/freelance/proyectos/{id}", ajeno).andExpect(status().isForbidden());
    }

    @Test void proyectos_asignarFreelancer() throws Exception {
        call(CLIENTE, put("/api/v1/freelance/proyectos/" + proyBuscando + "/asignar").param("employeeId", String.valueOf(freelancerEmp)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.estado").value("EN_PROGRESO"));
    }

    // ---------- Propuestas ----------
    @Test void propuestas_enviar() throws Exception {
        Integer emp = id("select e.employee_id from employee e join perfil p on p.perfil_id=e.perfil_id where p.usuario='freelancer05'");
        jdbc.update("delete from postulacion_proyecto where proyecto_id=" + proyBuscando + " and employee_id=?", emp);
        postJson("freelancer05", "/api/v1/freelance/proyectos/" + proyBuscando + "/postulaciones/me", """
                {"descripcion":"Puedo hacerlo","costo":250000,"employeeId":%d}""".formatted(emp)).andExpect(status().isOk());
    }

    @Test void propuestas_superaPresupuesto_rechazada() throws Exception {
        Integer emp = id("select e.employee_id from employee e join perfil p on p.perfil_id=e.perfil_id where p.usuario='freelancer06'");
        jdbc.update("delete from postulacion_proyecto where proyecto_id=" + proyBuscando + " and employee_id=?", emp);
        int st = postJson("freelancer06", "/api/v1/freelance/proyectos/" + proyBuscando + "/postulaciones/me", """
                {"descripcion":"Caro","costo":99999999,"employeeId":%d}""".formatted(emp)).andReturn().getResponse().getStatus();
        org.junit.jupiter.api.Assertions.assertTrue(st == 400 || st == 409 || st == 422, "fue " + st);
    }

    @Test void propuestas_listar() throws Exception {
        get(CLIENTE, "/api/v1/freelance/proyectos/" + proyBuscando + "/postulaciones").andExpect(status().isOk());
    }

    // ---------- Hitos ----------
    @Test void hitos_crear() throws Exception {
        // proyEnCurso tiene 4 hitos que ya suman el total: se libera el último para poder crear uno
        jdbc.update("delete from hito_proyecto where hito_id=" + hitoUltimo);
        postJson(CLIENTE, "/api/v1/freelance/proyectos/" + proyEnCurso + "/hitos", """
                {"titulo":"Hito test","descripcion":"desc","fechaLimite":"%s"}""".formatted(LocalDate.now().plusDays(20)))
                .andExpect(status().isCreated());
    }

    @Test void hitos_crear_enProyectoSinAdjudicar_rechazado() throws Exception {
        int st = postJson(CLIENTE, "/api/v1/freelance/proyectos/" + proyBuscando + "/hitos", """
                {"titulo":"Hito test"}""").andReturn().getResponse().getStatus();
        org.junit.jupiter.api.Assertions.assertTrue(st == 400 || st == 409 || st == 422, "fue " + st);
    }

    @Test void hitos_listar() throws Exception { get(CLIENTE, "/api/v1/freelance/proyectos/" + proyEnCurso + "/hitos").andExpect(status().isOk()); }

    @Test void hitos_listar_proyectoAjeno_403() throws Exception {
        get("cliente05", "/api/v1/freelance/proyectos/" + proyEnCurso + "/hitos").andExpect(status().isForbidden());
    }

    @Test void hitos_completar() throws Exception {
        jdbc.update("update hito_proyecto set estado='EN_PROGRESO' where hito_id=" + hitoPrimero);
        call(FREELANCER, put("/api/v1/freelance/proyectos/" + proyEnCurso + "/hitos/" + hitoPrimero + "/estado").param("nuevoEstado", "COMPLETADO"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.estado").value("COMPLETADO"));
    }

    @Test void hitos_pasarAEnProgresoManual_rechazado() throws Exception {
        int st = call(CLIENTE, put("/api/v1/freelance/proyectos/" + proyEnCurso + "/hitos/" + hitoPrimero + "/estado").param("nuevoEstado", "EN_PROGRESO"))
                .andReturn().getResponse().getStatus();
        org.junit.jupiter.api.Assertions.assertTrue(st == 400 || st == 409 || st == 422, "fue " + st);
    }

    // ---------- Pagos ----------
    @Test void pagos_listar() throws Exception { get(CLIENTE, "/api/v1/freelance/proyectos/" + proyConPagos + "/pagos").andExpect(status().isOk()); }
    @Test void pagos_listar_freelancer() throws Exception { get(FREELANCER, "/api/v1/freelance/proyectos/" + proyConPagos + "/pagos").andExpect(status().isOk()); }

    @Test void pagos_checkout() throws Exception {
        call(CLIENTE, post("/api/v1/freelance/proyectos/" + proyEnCurso + "/pagos/checkout").param("hitoId", String.valueOf(hitoPrimero)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.initPoint", notNullValue()));
    }

    @Test void pagos_checkout_hitoFueraDeOrden_rechazado() throws Exception {
        int st = call(CLIENTE, post("/api/v1/freelance/proyectos/" + proyEnCurso + "/pagos/checkout").param("hitoId", String.valueOf(hitoUltimo)))
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

    // Corre fuera de la transacción del test (como en producción): así se detecta si el guardado
    // queda dentro de una transacción de solo lectura. Por eso limpia a mano lo que crea.
    @org.springframework.transaction.annotation.Transactional(propagation = org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED)
    @Test void webhook_merchantOrder_registraElPagoAprobadoYHabilitaElHito() throws Exception {
        // Mercado Pago a veces avisa solo la orden (topic=merchant_order): hay que buscar sus pagos
        Integer hito = id("""
                select min(hito_id) from hito_proyecto where proyecto_id=? and estado='PENDIENTE'
                and hito_id not in (select hito_id from pagos where hito_id is not null)""", proyEnCurso);
        java.math.BigDecimal monto = jdbc.queryForObject("select monto from hito_proyecto where hito_id=?", java.math.BigDecimal.class, hito);
        com.mercadopago.resources.payment.Payment pago = org.mockito.Mockito.mock(com.mercadopago.resources.payment.Payment.class);
        org.mockito.Mockito.when(pago.getId()).thenReturn(990001L);
        org.mockito.Mockito.when(pago.getStatus()).thenReturn("approved");
        org.mockito.Mockito.when(pago.getExternalReference()).thenReturn(proyEnCurso + "-" + hito);
        org.mockito.Mockito.when(pago.getTransactionAmount()).thenReturn(monto);
        org.mockito.Mockito.when(mercadoPagoAdapter.consultarPagosDeOrden(46461453778L)).thenReturn(java.util.List.of(990001L));
        org.mockito.Mockito.when(mercadoPagoAdapter.consultarPago(990001L)).thenReturn(pago);

        try {
            call(null, post("/api/v1/pagos/webhook").param("topic", "merchant_order").param("id", "46461453778"))
                    .andExpect(status().isOk());

            org.junit.jupiter.api.Assertions.assertEquals(1, id("select count(*) from pagos where mp_payment_id='990001' and hito_id=?", hito));
            org.junit.jupiter.api.Assertions.assertEquals("EN_PROGRESO",
                    jdbc.queryForObject("select estado from hito_proyecto where hito_id=?", String.class, hito));
        } finally {
            jdbc.update("delete from pagos where mp_payment_id='990001'");
            jdbc.update("update hito_proyecto set estado='PENDIENTE' where hito_id=?", hito);
        }
    }

    // ---------- Calificaciones ----------
    @Test void calificar() throws Exception {
        jdbc.update("delete from calificacion_usuario where calificacion_id in (select calificacion_id from calificacion_proyecto where proyecto_id=" + proyFinalizado + ")");
        jdbc.update("delete from calificacion_proyecto where proyecto_id=" + proyFinalizado);
        postJson(CLIENTE, "/api/v1/freelance/proyectos/" + proyFinalizado + "/calificar", """
                {"puntaje":5,"comentario":"Excelente trabajo"}""").andExpect(status().isCreated());
    }

    @Test void calificar_proyectoNoFinalizado_rechazado() throws Exception {
        int st = postJson(CLIENTE, "/api/v1/freelance/proyectos/" + proyEnCurso + "/calificar", """
                {"puntaje":5,"comentario":"Antes de tiempo"}""").andReturn().getResponse().getStatus();
        org.junit.jupiter.api.Assertions.assertTrue(st == 400 || st == 409 || st == 422, "fue " + st);
    }

    @Test void calificaciones_deProyecto() throws Exception { get(CLIENTE, "/api/v1/freelance/proyectos/" + proyFinalizado + "/calificaciones").andExpect(status().isOk()); }
    @Test void reputacion_publica() throws Exception { get(null, "/api/v1/perfiles/" + freelancerPerfil + "/calificaciones").andExpect(status().isOk()); }
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
    @Test void freelancer_transacciones() throws Exception {
        String nombre = jdbc.queryForObject("select nombre_proyecto from proyecto where proyecto_id=" + proyConPagos, String.class);
        get(FREELANCER, "/api/v1/freelancer/transactions").andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.projectId == " + proyConPagos + ")].projectTitle", org.hamcrest.Matchers.hasItem(nombre)));
    }
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
