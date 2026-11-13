package com.example.Proyecto_Final_Progra3.api;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDate;

import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** EmpresaController, RecruiterController, VacanteController, PostulacionVacanteController, EntrevistaController, PortfolioController. */
class EmpresaApiTest extends ApiTestBase {

    private static final String EMPRESA_JSON = """
            {"nombre":"Empresa Test","descripcion":"Descripción test","sitio_web":"https://test.com","ubicacion":"Córdoba"}""";

    // ---------- Empresa ----------
    @Test void miEmpresa() throws Exception { get(EMPRESA, "/api/v1/empresas/my").andExpect(status().isOk()); }
    @Test void mostrarEmpresa() throws Exception { get(EMPRESA, "/api/v1/empresas/148").andExpect(status().isOk()); }
    @Test void mostrarEmpresaPorUuid() throws Exception {
        String uuid = jdbc.queryForObject("select uuid from empresa where empresa_id=148", String.class);
        get(EMPRESA, "/api/v1/empresas/uuid/{u}", uuid).andExpect(status().isOk());
    }
    @Test void dashboard() throws Exception { get(EMPRESA, "/api/v1/empresas/dashboard").andExpect(status().isOk()); }

    @Test void crearEmpresa_usuarioNuevo() throws Exception {
        postJson(null, "/api/v1/auth/register", """
                {"usuario":"test_emp_new","email":"test_emp_new@test.com","password":"Password123","rol":"EMPRESA"}""")
                .andExpect(status().isCreated());
        int st = postJson("test_emp_new", "/api/v1/empresas", EMPRESA_JSON).andReturn().getResponse().getStatus();
        // El registro de EMPRESA ya crea la empresa: crear otra es regla de negocio (422)
        org.junit.jupiter.api.Assertions.assertTrue(st == 201 || st == 422, "fue " + st);
    }

    @Test void editarEmpresa() throws Exception {
        putJson(EMPRESA, "/api/v1/empresas/148", EMPRESA_JSON).andExpect(status().isOk());
    }

    @Test void editarEmpresa_sinSitioNiUbicacion() throws Exception {
        // sitio_web y ubicacion son opcionales en el DTO: no debería romper con 409 por NOT NULL en la base
        putJson(EMPRESA, "/api/v1/empresas/148", """
                {"nombre":"Empresa Test","descripcion":"Sin sitio"}""").andExpect(status().isOk());
    }

    @Test void editarEmpresa_ajena_403() throws Exception {
        Integer otra = id("select min(empresa_id) from empresa where empresa_id<>148");
        putJson(EMPRESA, "/api/v1/empresas/{id}", EMPRESA_JSON, otra).andExpect(status().isForbidden());
    }

    @Test void subirLogo() throws Exception {
        upload(EMPRESA, "/api/v1/empresas/logo", png("file"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.logoUrl", startsWith("data:image/png;base64,")));
    }

    @Test void subirBanner() throws Exception {
        upload(EMPRESA, "/api/v1/empresas/banner", png("file"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.bannerUrl", startsWith("data:image/png")));
    }

    @Test void subirLogo_tipoInvalido_400() throws Exception {
        upload(EMPRESA, "/api/v1/empresas/logo", new MockMultipartFile("file", "x.txt", "text/plain", "hola".getBytes()))
                .andExpect(status().isBadRequest());
    }

    // ---------- Recruiters ----------
    private static final String RECRUITER_JSON = """
            {"nombre":"Rita","apellido":"Test","email":"rita.test@test.com","fechaNacimiento":"1995-05-10","area":"IT"}""";

    @Test void recruiters_listar() throws Exception { get(EMPRESA, "/api/v1/empresas/recruiters").andExpect(status().isOk()); }
    @Test void recruiters_buscarPorId() throws Exception { get(EMPRESA, "/api/v1/empresas/recruiters/148").andExpect(status().isOk()); }

    @Test void recruiters_crear_actualizar_eliminar() throws Exception {
        // El recruiter tiene que estar registrado antes con ese email
        postJson(null, "/api/v1/auth/register", """
                {"usuario":"rita_test","email":"rita.test@test.com","password":"Password123","rol":"RECRUITER"}""")
                .andExpect(status().isCreated());
        postJson(EMPRESA, "/api/v1/empresas/recruiters", RECRUITER_JSON).andExpect(status().isCreated());
        Integer id = id("select max(recruiter_id) from recruiter where empresa_id=148");
        putJson(EMPRESA, "/api/v1/empresas/recruiters/{id}", RECRUITER_JSON.replace("Rita", "Rita M."), id).andExpect(status().isOk());
        int st = delete(EMPRESA, "/api/v1/empresas/recruiters/{id}", id).andReturn().getResponse().getStatus();
        org.junit.jupiter.api.Assertions.assertTrue(st == 200 || st == 204, "fue " + st);
    }

    // ---------- Vacantes ----------
    private String vacanteJson(String puesto) {
        return """
                {"puesto":"%s","descripcion":"Vacante test","finalizacion":"%s","ubicacion":"Remoto",
                 "tipoUbicacion":"REMOTO","tipoVacante":"FULL_TIME","salario":"1500000","etiquetas":["java"]}"""
                .formatted(puesto, LocalDate.now().plusDays(30));
    }

    @Test void vacantes_listar_publico() throws Exception { get(null, "/api/v1/vacantes").andExpect(status().isOk()); }
    @Test void vacantes_porId_publico() throws Exception { get(null, "/api/v1/vacantes/461").andExpect(status().isOk()); }
    @Test void vacantes_porUuid_publico() throws Exception {
        String uuid = jdbc.queryForObject("select uuid from vacante where vacante_id=461", String.class);
        get(null, "/api/v1/vacantes/uuid/{u}", uuid).andExpect(status().isOk());
    }
    @Test void vacantes_mias() throws Exception { get(EMPRESA, "/api/v1/vacantes/me").andExpect(status().isOk()); }
    @Test void vacantes_postulacionesDeMiEmpresa() throws Exception { get(EMPRESA, "/api/v1/vacantes/postulaciones").andExpect(status().isOk()); }
    @Test void vacantes_postulacionesFiltradas() throws Exception { get(EMPRESA, "/api/v1/vacantes/postulaciones?vacanteId=461").andExpect(status().isOk()); }

    @Test void vacantes_crear() throws Exception {
        postJson(EMPRESA, "/api/v1/vacantes/me", vacanteJson("Backend Dev Test")).andExpect(status().isOk());
    }

    @Test void vacantes_crear_puestoLargo_400() throws Exception {
        // La columna vacante.puesto es VARCHAR(50): debería validarse con 400, no explotar en la base
        postJson(EMPRESA, "/api/v1/vacantes/me", vacanteJson("P".repeat(80))).andExpect(status().isBadRequest());
    }

    @Test void vacantes_actualizar() throws Exception {
        putJson(EMPRESA, "/api/v1/vacantes/461", vacanteJson("Puesto editado")).andExpect(status().isOk());
    }

    @Test void vacantes_eliminar() throws Exception {
        int st = delete(EMPRESA, "/api/v1/vacantes/463").andReturn().getResponse().getStatus();
        org.junit.jupiter.api.Assertions.assertTrue(st == 200 || st == 204, "fue " + st);
    }

    @Test void vacantes_actualizarAjena_403() throws Exception {
        Integer ajena = id("select min(vacante_id) from vacante where empresa_id<>148");
        putJson(EMPRESA, "/api/v1/vacantes/{id}", vacanteJson("Hack"), ajena).andExpect(status().isForbidden());
    }

    // ---------- Postulaciones a vacantes ----------
    @Test void postularse() throws Exception {
        Integer vac = id("""
                select min(vacante_id) from vacante where activa=1 and fecha_fin>=curdate()
                and vacante_id not in (select vacante_id from postulacion_vacante where postulante_id=296)""");
        call(POSTULANTE, multipart("/api/v1/vacantes/{v}/postulaciones/me", vac).file(pdf("cv")).param("mensaje", "Me interesa"))
                .andExpect(status().isOk());
    }

    @Test void postularse_duplicado_rechazado() throws Exception {
        int st = call(POSTULANTE, multipart("/api/v1/vacantes/461/postulaciones/me").file(pdf("cv")).param("mensaje", "Otra vez"))
                .andReturn().getResponse().getStatus();
        org.junit.jupiter.api.Assertions.assertTrue(st == 409 || st == 422, "fue " + st);
    }

    @Test void postularse_sinCV() throws Exception {
        // El parámetro cv es opcional (required=false) en el controller
        Integer vac = id("""
                select min(vacante_id) from vacante where activa=1 and fecha_fin>=curdate()
                and vacante_id not in (select vacante_id from postulacion_vacante where postulante_id=296)""");
        // Sin adjunto se usa el CV del perfil
        upload(POSTULANTE, "/api/v1/perfiles/uploadMyCV", pdf("file")).andExpect(status().isOk());
        call(POSTULANTE, multipart("/api/v1/vacantes/{v}/postulaciones/me", vac).param("mensaje", "Sin CV"))
                .andExpect(status().isOk());
    }

    @Test void postularse_sinCVNiEnPerfil_400() throws Exception {
        jdbc.update("update portfolio set cv=null where employee_id=296");
        Integer vac = id("""
                select min(vacante_id) from vacante where activa=1 and fecha_fin>=curdate()
                and vacante_id not in (select vacante_id from postulacion_vacante where postulante_id=296)""");
        call(POSTULANTE, multipart("/api/v1/vacantes/{v}/postulaciones/me", vac).param("mensaje", "Sin CV"))
                .andExpect(status().isBadRequest());
    }

    @Test void verPostulantes() throws Exception { get(EMPRESA, "/api/v1/vacantes/461/postulaciones").andExpect(status().isOk()); }

    @Test void cambiarEstadoPostulacion() throws Exception {
        call(EMPRESA, put("/api/v1/vacantes/461/postulaciones/758/estado").param("nuevoEstado", "RECHAZADO"))
                .andExpect(status().isOk());
    }

    // ---------- Entrevistas ----------
    @Test void entrevistas_recruiter() throws Exception { get(RECRUITER, "/api/v1/recruiter/entrevistas").andExpect(status().isOk()); }
    @Test void entrevistas_postulante() throws Exception { get(POSTULANTE, "/api/v1/recruiter/entrevistas").andExpect(status().isOk()); }

    @Test void entrevistas_agendar() throws Exception {
        postJson(RECRUITER, "/api/v1/recruiter/entrevistas", """
                {"postulacionId":758,"fecha":"%s","hora":"14:30:00","link":"https://meet.google.com/abc"}"""
                .formatted(LocalDate.now().plusDays(5))).andExpect(status().isCreated());
    }

    // ---------- Portfolio ----------
    @Test void portfolio_subirCV() throws Exception {
        upload(POSTULANTE, "/api/v1/portfolio/296/cv", pdf("file")).andExpect(status().isOk());
    }

    @Test void portfolio_subirCV_deOtro_403() throws Exception {
        upload(POSTULANTE, "/api/v1/portfolio/316/cv", pdf("file")).andExpect(status().isForbidden());
    }
}
