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
    @Test void miEmpresa() throws Exception {
        get(EMPRESA, "/api/v1/empresas/my").andExpect(status().isOk())
                .andExpect(jsonPath("$.descripcion").isNotEmpty())
                .andExpect(jsonPath("$.perfil_id").value(empresaPerfil))
                .andExpect(jsonPath("$.logo", startsWith("data:image")));
    }
    @Test void getProfileEmpresa_traeLogo() throws Exception {
        get(EMPRESA, "/api/v1/perfiles/getProfile").andExpect(status().isOk())
                .andExpect(jsonPath("$.logo", startsWith("data:image")));
    }
    @Test void mostrarEmpresa() throws Exception { get(EMPRESA, "/api/v1/empresas/" + empresaId).andExpect(status().isOk()); }
    @Test void mostrarEmpresaPorUuid() throws Exception {
        String uuid = jdbc.queryForObject("select uuid from empresa where empresa_id=" + empresaId, String.class);
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
        putJson(EMPRESA, "/api/v1/empresas/" + empresaId, EMPRESA_JSON).andExpect(status().isOk());
    }

    @Test void editarEmpresa_sinSitioNiUbicacion() throws Exception {
        // sitio_web y ubicacion son opcionales en el DTO: no debería romper con 409 por NOT NULL en la base
        putJson(EMPRESA, "/api/v1/empresas/" + empresaId, """
                {"nombre":"Empresa Test","descripcion":"Sin sitio"}""").andExpect(status().isOk());
    }

    @Test void editarEmpresa_ajena_403() throws Exception {
        Integer otra = id("select min(empresa_id) from empresa where empresa_id<>?", empresaId);
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
    // La empresa vincula al recruiter solo por email
    private static final String RECRUITER_JSON = """
            {"email":"rita.test@test.com"}""";

    @Test void recruiters_listar() throws Exception { get(EMPRESA, "/api/v1/empresas/recruiters").andExpect(status().isOk()); }
    @Test void recruiters_buscarPorId() throws Exception { get(EMPRESA, "/api/v1/empresas/recruiters/" + recruiterId).andExpect(status().isOk()); }

    // IDs buscados por usuario: no dependen de los IDs de una base en particular
    private Integer recruiterDemoId() {
        return id("select r.recruiter_id from recruiter r join perfil p on p.perfil_id=r.perfil_id where p.usuario=?", RECRUITER);
    }

    @Test void recruiter_solicitaBaja_yAlos15DiasSeVaSolo() throws Exception {
        Integer rid = recruiterDemoId();
        get(RECRUITER, "/api/v1/recruiter/vinculacion").andExpect(status().isOk());
        postJson(RECRUITER, "/api/v1/recruiter/vinculacion/baja", "{}").andExpect(status().isOk());
        // Antes de los 15 días no puede irse por su cuenta
        postJson(RECRUITER, "/api/v1/recruiter/vinculacion/baja/confirmar", "{}").andExpect(status().is4xxClientError());
        // Simulamos que pasaron 16 días sin respuesta de la empresa
        sincronizar();
        jdbc.update("update recruiter set baja_solicitada_en = date_sub(now(), interval 16 day) where recruiter_id=?", rid);
        postJson(RECRUITER, "/api/v1/recruiter/vinculacion/baja/confirmar", "{}").andExpect(status().isNoContent());
        sincronizar();
        org.junit.jupiter.api.Assertions.assertEquals(0, id("select count(*) from recruiter where recruiter_id=?", rid));
    }

    @Test void recruiter_empresaAceptaLaBaja() throws Exception {
        Integer rid = recruiterDemoId();
        postJson(RECRUITER, "/api/v1/recruiter/vinculacion/baja", "{}").andExpect(status().isOk());
        postJson(EMPRESA, "/api/v1/empresas/recruiters/{id}/baja/aceptar", "{}", rid).andExpect(status().isNoContent());
        sincronizar();
        org.junit.jupiter.api.Assertions.assertEquals(0, id("select count(*) from recruiter where recruiter_id=?", rid));
    }

    @Test void recruiters_crear_actualizar_eliminar() throws Exception {
        // El recruiter tiene que estar registrado antes con ese email
        postJson(null, "/api/v1/auth/register", """
                {"usuario":"rita_test","email":"rita.test@test.com","password":"Password123","rol":"RECRUITER"}""")
                .andExpect(status().isCreated());
        postJson(EMPRESA, "/api/v1/empresas/recruiters", RECRUITER_JSON).andExpect(status().isCreated());
        Integer id = id("select max(recruiter_id) from recruiter where empresa_id=" + empresaId);
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
    @Test void vacantes_porId_publico() throws Exception {
        // Sin logo propio usa el de la empresa
        get(null, "/api/v1/vacantes/" + vacante).andExpect(status().isOk())
                .andExpect(jsonPath("$.logo", startsWith("data:image")));
    }
    @Test void vacantes_porUuid_publico() throws Exception {
        String uuid = jdbc.queryForObject("select uuid from vacante where vacante_id=" + vacante, String.class);
        get(null, "/api/v1/vacantes/uuid/{u}", uuid).andExpect(status().isOk());
    }
    @Test void vacantes_mias() throws Exception { get(EMPRESA, "/api/v1/vacantes/me").andExpect(status().isOk()); }
    @Test void vacantes_postulacionesDeMiEmpresa() throws Exception { get(EMPRESA, "/api/v1/vacantes/postulaciones").andExpect(status().isOk()); }
    @Test void vacantes_postulacionesFiltradas() throws Exception { get(EMPRESA, "/api/v1/vacantes/postulaciones?vacanteId=" + vacante).andExpect(status().isOk()); }

    @Test void vacantes_crear() throws Exception {
        postJson(EMPRESA, "/api/v1/vacantes/me", vacanteJson("Backend Dev Test")).andExpect(status().isOk());
    }

    @Test void vacantes_crear_puestoLargo_400() throws Exception {
        // La columna vacante.puesto es VARCHAR(50): debería validarse con 400, no explotar en la base
        postJson(EMPRESA, "/api/v1/vacantes/me", vacanteJson("P".repeat(80))).andExpect(status().isBadRequest());
    }

    @Test void vacantes_actualizar() throws Exception {
        putJson(EMPRESA, "/api/v1/vacantes/" + vacante, vacanteJson("Puesto editado")).andExpect(status().isOk());
    }

    @Test void vacantes_eliminar() throws Exception {
        postJson(EMPRESA, "/api/v1/vacantes/me", vacanteJson("Para borrar")).andExpect(status().isOk());
        Integer nueva = id("select max(vacante_id) from vacante where empresa_id=?", empresaId);
        int st = delete(EMPRESA, "/api/v1/vacantes/{id}", nueva).andReturn().getResponse().getStatus();
        org.junit.jupiter.api.Assertions.assertTrue(st == 200 || st == 204, "fue " + st);
    }

    @Test void vacantes_actualizarAjena_403() throws Exception {
        Integer ajena = id("select min(vacante_id) from vacante where empresa_id<>?", empresaId);
        putJson(EMPRESA, "/api/v1/vacantes/{id}", vacanteJson("Hack"), ajena).andExpect(status().isForbidden());
    }

    // ---------- Postulaciones a vacantes ----------
    @Test void postularse() throws Exception {
        Integer vac = id("""
                select min(vacante_id) from vacante where activa=1 and fecha_fin>=curdate()
                and vacante_id not in (select vacante_id from postulacion_vacante where postulante_id=?)""", postulanteEmp);
        call(POSTULANTE, multipart("/api/v1/vacantes/{v}/postulaciones/me", vac).file(pdf("cv")).param("mensaje", "Me interesa"))
                .andExpect(status().isOk());
    }

    @Test void postularse_duplicado_rechazado() throws Exception {
        int st = call(POSTULANTE, multipart("/api/v1/vacantes/" + vacante + "/postulaciones/me").file(pdf("cv")).param("mensaje", "Otra vez"))
                .andReturn().getResponse().getStatus();
        org.junit.jupiter.api.Assertions.assertTrue(st == 409 || st == 422, "fue " + st);
    }

    @Test void postularse_sinCV() throws Exception {
        // El parámetro cv es opcional (required=false) en el controller
        Integer vac = id("""
                select min(vacante_id) from vacante where activa=1 and fecha_fin>=curdate()
                and vacante_id not in (select vacante_id from postulacion_vacante where postulante_id=?)""", postulanteEmp);
        // Sin adjunto se usa el CV del perfil
        upload(POSTULANTE, "/api/v1/perfiles/uploadMyCV", pdf("file")).andExpect(status().isOk());
        call(POSTULANTE, multipart("/api/v1/vacantes/{v}/postulaciones/me", vac).param("mensaje", "Sin CV"))
                .andExpect(status().isOk());
    }

    @Test void postularse_sinCVNiEnPerfil_400() throws Exception {
        jdbc.update("update portfolio set cv=null where employee_id=" + postulanteEmp);
        Integer vac = id("""
                select min(vacante_id) from vacante where activa=1 and fecha_fin>=curdate()
                and vacante_id not in (select vacante_id from postulacion_vacante where postulante_id=?)""", postulanteEmp);
        call(POSTULANTE, multipart("/api/v1/vacantes/{v}/postulaciones/me", vac).param("mensaje", "Sin CV"))
                .andExpect(status().isBadRequest());
    }

    @Test void verPostulantes() throws Exception { get(EMPRESA, "/api/v1/vacantes/" + vacante + "/postulaciones").andExpect(status().isOk()); }

    @Test void cambiarEstadoPostulacion() throws Exception {
        call(EMPRESA, put("/api/v1/vacantes/" + vacante + "/postulaciones/" + postulacion + "/estado").param("nuevoEstado", "RECHAZADO"))
                .andExpect(status().isOk());
    }

    // Una postulación a una vacante de demo_empresa, llevada a un estado inicial conocido
    private Integer[] postulacionDemo() {
        Integer pid = id("""
                select min(pv.postulacion_id) from postulacion_vacante pv join vacante v on v.vacante_id=pv.vacante_id
                join empresa e on e.empresa_id=v.empresa_id join perfil p on p.perfil_id=e.perfil_id where p.usuario=?""", EMPRESA);
        Integer vid = id("select vacante_id from postulacion_vacante where postulacion_id=?", pid);
        jdbc.update("delete from entrevista where postulacion_id=?", pid);
        jdbc.update("update postulacion_vacante set estado_postulacion='VISUALIZADO' where postulacion_id=?", pid);
        return new Integer[]{vid, pid};
    }

    @Test void estadoPostulacion_noPuedeRetroceder() throws Exception {
        Integer[] vp = postulacionDemo();
        call(EMPRESA, put("/api/v1/vacantes/{v}/postulaciones/{p}/estado", vp[0], vp[1]).param("nuevoEstado", "RECHAZADO"))
                .andExpect(status().isOk());
        call(EMPRESA, put("/api/v1/vacantes/{v}/postulaciones/{p}/estado", vp[0], vp[1]).param("nuevoEstado", "PENDIENTE"))
                .andExpect(status().is4xxClientError());
    }

    @Test void estadoPostulacion_bloqueadoConEntrevistaAgendada() throws Exception {
        Integer[] vp = postulacionDemo();
        postJson(RECRUITER, "/api/v1/recruiter/entrevistas", entrevistaJson(vp[1], diaHabil(5), "14:30:00"))
                .andExpect(status().isCreated());
        // Agendar la entrevista avanza la postulación
        sincronizar();
        org.junit.jupiter.api.Assertions.assertEquals("ENTREVISTA_AGENDADA",
                jdbc.queryForObject("select estado_postulacion from postulacion_vacante where postulacion_id=?", String.class, vp[1]));
        call(EMPRESA, put("/api/v1/vacantes/{v}/postulaciones/{p}/estado", vp[0], vp[1]).param("nuevoEstado", "RECHAZADO"))
                .andExpect(status().is4xxClientError());
    }

    // ---------- Entrevistas ----------
    @Test void entrevistas_recruiter() throws Exception { get(RECRUITER, "/api/v1/recruiter/entrevistas").andExpect(status().isOk()); }
    @Test void entrevistas_postulante() throws Exception { get(POSTULANTE, "/api/v1/recruiter/entrevistas").andExpect(status().isOk()); }

    @Test void entrevistas_agendar() throws Exception {
        postJson(RECRUITER, "/api/v1/recruiter/entrevistas", """
                {"postulacionId":%d,"fecha":"%s","hora":"14:30:00","link":"https://meet.google.com/abc"}"""
                .formatted(postulacion, diaHabil(5))).andExpect(status().isCreated());
    }

    private String entrevistaJson(int postulacion, LocalDate fecha, String hora) {
        return """
                {"postulacionId":%d,"fecha":"%s","hora":"%s","link":"https://meet.test/abc"}""".formatted(postulacion, fecha, hora);
    }

    @Test void entrevistas_noSePuedeAgendarParaHoy() throws Exception {
        Integer[] vp = postulacionDemo();
        postJson(RECRUITER, "/api/v1/recruiter/entrevistas", entrevistaJson(vp[1], LocalDate.now(), "23:59:00"))
                .andExpect(status().is4xxClientError());
    }

    @Test void entrevistas_soloEnHorarioDeOficina() throws Exception {
        LocalDate habil = diaHabil(3);
        LocalDate sabado = LocalDate.now().plusDays(1).with(java.time.temporal.TemporalAdjusters.next(java.time.DayOfWeek.SATURDAY));
        for (String[] caso : new String[][]{{habil.toString(), "08:59:00"}, {habil.toString(), "18:00:00"}, {sabado.toString(), "10:00:00"}})
            postJson(RECRUITER, "/api/v1/recruiter/entrevistas", entrevistaJson(postulacion, LocalDate.parse(caso[0]), caso[1]))
                    .andExpect(status().isUnprocessableContent());
        // Reprogramar tampoco puede sacarla del horario de oficina
        Integer id = agendar758(habil);
        putJson(RECRUITER, "/api/v1/recruiter/entrevistas/{id}", entrevistaJson(postulacion, habil, "19:30:00"), id)
                .andExpect(status().isUnprocessableContent());
    }

    @Test void entrevistas_noSePuedenSuperponer() throws Exception {
        LocalDate dia = diaHabil(4);
        // Otra postulación de la misma empresa ya tiene entrevista ese día a las 11
        Integer otra = id("select min(pv.postulacion_id) from postulacion_vacante pv join vacante v on v.vacante_id=pv.vacante_id "
                + "where v.empresa_id=? and pv.postulacion_id<>?", empresaId, postulacion);
        jdbc.update("insert into entrevista (postulacion_id, fecha, hora, estado) values (?, ?, '11:00:00', 'PROGRAMADA')", otra, dia);

        postJson(RECRUITER, "/api/v1/recruiter/entrevistas", entrevistaJson(postulacion, dia, "11:00:00"))
                .andExpect(status().isUnprocessableContent());
        Integer id = agendar758(dia);
        putJson(RECRUITER, "/api/v1/recruiter/entrevistas/{id}", entrevistaJson(postulacion, dia, "11:00:00"), id)
                .andExpect(status().isUnprocessableContent());
        // Reprogramar al mismo día y horario que ya tiene tampoco cambia nada: avisa
        putJson(RECRUITER, "/api/v1/recruiter/entrevistas/{id}", entrevistaJson(postulacion, dia, "14:30:00"), id)
                .andExpect(status().isUnprocessableContent());
    }

    @Test void entrevistas_noSePuedeAgendarDosVeces() throws Exception {
        postJson(RECRUITER, "/api/v1/recruiter/entrevistas", entrevistaJson(postulacion, diaHabil(5), "14:30:00"))
                .andExpect(status().isCreated());
        postJson(RECRUITER, "/api/v1/recruiter/entrevistas", entrevistaJson(postulacion, diaHabil(9), "10:00:00"))
                .andExpect(status().isUnprocessableContent());
    }

    @Test void entrevistas_reprogramar() throws Exception {
        postJson(RECRUITER, "/api/v1/recruiter/entrevistas", entrevistaJson(postulacion, diaHabil(5), "14:30:00"))
                .andExpect(status().isCreated());
        Integer id = id("select max(entrevista_id) from entrevista where postulacion_id=" + postulacion);
        LocalDate nueva = diaHabil(12);
        putJson(RECRUITER, "/api/v1/recruiter/entrevistas/{id}", entrevistaJson(postulacion, nueva, "09:15:00"), id)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.date").value(nueva.toString()))
                .andExpect(jsonPath("$.time").value("09:15"))
                .andExpect(jsonPath("$.status").value("PROGRAMADA"));
        // Sigue habiendo una sola entrevista para esa postulación
        org.junit.jupiter.api.Assertions.assertEquals(1, id("select count(*) from entrevista where postulacion_id=" + postulacion));
    }

    @Test void entrevistas_reprogramarPasada_422() throws Exception {
        Integer pasada = id("select min(entrevista_id) from entrevista e join postulacion_vacante pv on pv.postulacion_id=e.postulacion_id "
                + "join vacante v on v.vacante_id=pv.vacante_id where v.empresa_id=" + empresaId + " and e.fecha<curdate()");
        putJson(RECRUITER, "/api/v1/recruiter/entrevistas/{id}", entrevistaJson(postulacion, diaHabil(3), "10:00:00"), pasada)
                .andExpect(status().isUnprocessableContent());
    }

    @Test void entrevistas_reprogramarDeOtraEmpresa_403() throws Exception {
        postJson(RECRUITER, "/api/v1/recruiter/entrevistas", entrevistaJson(postulacion, diaHabil(5), "14:30:00"))
                .andExpect(status().isCreated());
        Integer id = id("select max(entrevista_id) from entrevista where postulacion_id=" + postulacion);
        putJson("AnaRecruiter", "/api/v1/recruiter/entrevistas/{id}", entrevistaJson(postulacion, diaHabil(6), "10:00:00"), id)
                .andExpect(status().isForbidden());
    }

    @Test void entrevistas_reprogramarInexistente_404() throws Exception {
        putJson(RECRUITER, "/api/v1/recruiter/entrevistas/999999", entrevistaJson(postulacion, diaHabil(3), "10:00:00"))
                .andExpect(status().isNotFound());
    }

    @Test void entrevistas_reprogramarFechaPasada_400() throws Exception {
        putJson(RECRUITER, "/api/v1/recruiter/entrevistas/999999", entrevistaJson(postulacion, LocalDate.now().minusDays(3), "10:00:00"))
                .andExpect(status().isBadRequest());
    }

    private Integer agendar758(LocalDate fecha) throws Exception {
        postJson(RECRUITER, "/api/v1/recruiter/entrevistas", entrevistaJson(postulacion, fecha, "14:30:00")).andExpect(status().isCreated());
        return id("select max(entrevista_id) from entrevista where postulacion_id=" + postulacion);
    }

    @Test void entrevistas_cancelar() throws Exception {
        Integer id = agendar758(diaHabil(4));
        delete(RECRUITER, "/api/v1/recruiter/entrevistas/{id}", id).andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELADA"));
        get(RECRUITER, "/api/v1/recruiter/entrevistas").andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == " + id + ")].status", org.hamcrest.Matchers.hasItem("CANCELADA")));
        org.junit.jupiter.api.Assertions.assertEquals("CANCELADA",
                jdbc.queryForObject("select estado from entrevista where entrevista_id=?", String.class, id));
    }

    @Test void entrevistas_cancelarDosVeces_422() throws Exception {
        Integer id = agendar758(diaHabil(4));
        delete(RECRUITER, "/api/v1/recruiter/entrevistas/{id}", id).andExpect(status().isOk());
        delete(RECRUITER, "/api/v1/recruiter/entrevistas/{id}", id).andExpect(status().isUnprocessableContent());
    }

    @Test void entrevistas_cancelarElMismoDia_422() throws Exception {
        Integer id = agendar758(diaHabil(4));
        sincronizar();
        jdbc.update("update entrevista set fecha=curdate(), hora='23:59:00' where entrevista_id=?", id);
        delete(RECRUITER, "/api/v1/recruiter/entrevistas/{id}", id).andExpect(status().isUnprocessableContent());
    }

    @Test void entrevistas_cancelarPasada_422() throws Exception {
        Integer pasada = id("select min(entrevista_id) from entrevista e join postulacion_vacante pv on pv.postulacion_id=e.postulacion_id "
                + "join vacante v on v.vacante_id=pv.vacante_id where v.empresa_id=" + empresaId + " and e.fecha<curdate()");
        delete(RECRUITER, "/api/v1/recruiter/entrevistas/{id}", pasada).andExpect(status().isUnprocessableContent());
    }

    @Test void entrevistas_cancelarDeOtraEmpresa_403() throws Exception {
        Integer id = agendar758(diaHabil(4));
        delete("AnaRecruiter", "/api/v1/recruiter/entrevistas/{id}", id).andExpect(status().isForbidden());
    }

    @Test void entrevistas_cancelarInexistente_404() throws Exception {
        delete(RECRUITER, "/api/v1/recruiter/entrevistas/999999").andExpect(status().isNotFound());
    }

    @Test void entrevistas_postulanteNoPuedeCancelar_403() throws Exception {
        Integer id = agendar758(diaHabil(4));
        delete(POSTULANTE, "/api/v1/recruiter/entrevistas/{id}", id).andExpect(status().isForbidden());
    }

    @Test void entrevistas_trasCancelarSePuedeAgendarOtra() throws Exception {
        Integer id = agendar758(diaHabil(4));
        delete(RECRUITER, "/api/v1/recruiter/entrevistas/{id}", id).andExpect(status().isOk());
        postJson(RECRUITER, "/api/v1/recruiter/entrevistas", entrevistaJson(postulacion, diaHabil(8), "11:00:00"))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.status").value("PROGRAMADA"));
    }

    @Test void entrevistas_noSeReprogramaUnaCancelada_422() throws Exception {
        Integer id = agendar758(diaHabil(4));
        delete(RECRUITER, "/api/v1/recruiter/entrevistas/{id}", id).andExpect(status().isOk());
        putJson(RECRUITER, "/api/v1/recruiter/entrevistas/{id}", entrevistaJson(postulacion, diaHabil(9), "10:00:00"), id)
                .andExpect(status().isUnprocessableContent());
    }

    @Test void entrevistas_dashboardNoCuentaCanceladas() throws Exception {
        Integer id = agendar758(diaHabil(4));
        int antes = com.jayway.jsonpath.JsonPath.read(get(EMPRESA, "/api/v1/empresas/dashboard")
                .andReturn().getResponse().getContentAsString(), "$.entrevistasPendientes");
        delete(RECRUITER, "/api/v1/recruiter/entrevistas/{id}", id).andExpect(status().isOk());
        get(EMPRESA, "/api/v1/empresas/dashboard").andExpect(jsonPath("$.entrevistasPendientes").value(antes - 1));
    }

    // ---------- Portfolio ----------
    @Test void portfolio_subirCV() throws Exception {
        upload(POSTULANTE, "/api/v1/portfolio/" + postulanteEmp + "/cv", pdf("file")).andExpect(status().isOk());
    }

    @Test void portfolio_subirCV_deOtro_403() throws Exception {
        upload(POSTULANTE, "/api/v1/portfolio/" + freelancerEmp + "/cv", pdf("file")).andExpect(status().isForbidden());
    }
}
