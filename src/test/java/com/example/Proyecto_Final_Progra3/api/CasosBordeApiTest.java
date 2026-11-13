package com.example.Proyecto_Final_Progra3.api;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.AbstractMockHttpServletRequestBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Casos borde de todos los endpoints: ids/UUID inexistentes o mal formados, bodies inválidos,
 * parámetros faltantes o con valores fuera del enum, sin token, rol equivocado y cuentas sin datos.
 * Cada tabla junta los fallos y los informa todos juntos, con la URL que falló.
 */
class CasosBordeApiTest extends ApiTestBase {

    private static final String NO_EXISTE = "999999";
    private static final String UUID_NO_EXISTE = "00000000-0000-0000-0000-000000000000";

    private record Caso(String usuario, AbstractMockHttpServletRequestBuilder<?> req, int esperado, String desc) {}

    private static Caso c(String u, AbstractMockHttpServletRequestBuilder<?> r, int esperado) {
        return new Caso(u, r, esperado, "");
    }

    private static AbstractMockHttpServletRequestBuilder<?> json(AbstractMockHttpServletRequestBuilder<?> r, String body) {
        return r.contentType(MediaType.APPLICATION_JSON).content(body);
    }

    private void verificar(List<Caso> casos) throws Exception {
        List<String> fallos = new ArrayList<>();
        for (Caso caso : casos) {
            int st = call(caso.usuario(), caso.req()).andReturn().getResponse().getStatus();
            var r = caso.req().buildRequest(new org.springframework.mock.web.MockServletContext());
            if (st != caso.esperado())
                fallos.add(r.getMethod() + " " + r.getRequestURI() + (r.getQueryString() != null ? "?" + r.getQueryString() : "")
                        + " [" + (caso.usuario() == null ? "anónimo" : caso.usuario()) + "] esperado " + caso.esperado() + ", fue " + st);
        }
        if (!fallos.isEmpty()) throw new AssertionError(fallos.size() + " casos fallaron:\n  " + String.join("\n  ", fallos));
    }

    // ---------- 404: ids inexistentes ----------
    @Test void idsInexistentes_404() throws Exception {
        verificar(List.of(
                c(null, MockMvcRequestBuilders.get("/api/v1/bootcamps/" + NO_EXISTE), 404),
                c(INSTRUCTOR, json(put("/api/v1/bootcamps/" + NO_EXISTE), BOOTCAMP), 404),
                c(INSTRUCTOR, MockMvcRequestBuilders.delete("/api/v1/bootcamps/" + NO_EXISTE), 404),
                c(INSTRUCTOR, json(put("/api/v1/instructores/" + NO_EXISTE), INSTRUCTOR_JSON), 404),
                c(INSTRUCTOR, MockMvcRequestBuilders.delete("/api/v1/instructores/" + NO_EXISTE), 404),
                c(POSTULANTE, post("/api/v1/inscripciones/me/" + NO_EXISTE), 404),
                c(POSTULANTE, put("/api/v1/inscripciones/" + NO_EXISTE + "/cancelar"), 404),
                c(INSTRUCTOR, json(post("/api/v1/calificaciones/bootcamps/" + NO_EXISTE), "{\"employeeId\":1,\"puntaje\":5,\"comentario\":\"x\"}"), 404),
                c(INSTRUCTOR, post("/api/v1/certificados/generar/" + NO_EXISTE), 404),
                c(EMPRESA, MockMvcRequestBuilders.get("/api/v1/empresas/" + NO_EXISTE), 404),
                c(EMPRESA, json(put("/api/v1/empresas/" + NO_EXISTE), EMPRESA_JSON), 404),
                c(EMPRESA, MockMvcRequestBuilders.get("/api/v1/empresas/recruiters/" + NO_EXISTE), 404),
                c(EMPRESA, json(put("/api/v1/empresas/recruiters/" + NO_EXISTE), RECRUITER_JSON), 404),
                c(EMPRESA, MockMvcRequestBuilders.delete("/api/v1/empresas/recruiters/" + NO_EXISTE), 404),
                c(null, MockMvcRequestBuilders.get("/api/v1/vacantes/" + NO_EXISTE), 404),
                c(EMPRESA, json(put("/api/v1/vacantes/" + NO_EXISTE), VACANTE), 404),
                c(EMPRESA, MockMvcRequestBuilders.delete("/api/v1/vacantes/" + NO_EXISTE), 404),
                c(POSTULANTE, multipart("/api/v1/vacantes/" + NO_EXISTE + "/postulaciones/me").file(pdf("cv")).param("mensaje", "x"), 404),
                c(EMPRESA, MockMvcRequestBuilders.get("/api/v1/vacantes/" + NO_EXISTE + "/postulaciones"), 404),
                c(EMPRESA, put("/api/v1/vacantes/461/postulaciones/" + NO_EXISTE + "/estado").param("nuevoEstado", "RECHAZADO"), 404),
                c(RECRUITER, json(post("/api/v1/recruiter/entrevistas"), ENTREVISTA.formatted(NO_EXISTE)), 404),
                c(null, MockMvcRequestBuilders.get("/api/v1/freelance/proyectos/" + NO_EXISTE), 404),
                c(CLIENTE, json(put("/api/v1/freelance/proyectos/" + NO_EXISTE), PROYECTO), 404),
                c(CLIENTE, put("/api/v1/freelance/proyectos/" + NO_EXISTE + "/cancelar"), 404),
                c(CLIENTE, put("/api/v1/freelance/proyectos/" + NO_EXISTE + "/asignar").param("employeeId", "316"), 404),
                c(FREELANCER, json(post("/api/v1/freelance/proyectos/" + NO_EXISTE + "/postulaciones/me"), PROPUESTA), 404),
                c(CLIENTE, MockMvcRequestBuilders.get("/api/v1/freelance/proyectos/" + NO_EXISTE + "/postulaciones"), 404),
                c(CLIENTE, json(post("/api/v1/freelance/proyectos/" + NO_EXISTE + "/hitos"), HITO), 404),
                c(CLIENTE, MockMvcRequestBuilders.get("/api/v1/freelance/proyectos/" + NO_EXISTE + "/hitos"), 404),
                c(FREELANCER, put("/api/v1/freelance/proyectos/392/hitos/" + NO_EXISTE + "/estado").param("nuevoEstado", "COMPLETADO"), 404),
                c(CLIENTE, MockMvcRequestBuilders.get("/api/v1/freelance/proyectos/" + NO_EXISTE + "/pagos"), 404),
                c(CLIENTE, post("/api/v1/freelance/proyectos/" + NO_EXISTE + "/pagos/checkout").param("hitoId", "548"), 422), // valida el hito antes que el proyecto
                c(CLIENTE, post("/api/v1/freelance/proyectos/392/pagos/checkout").param("hitoId", NO_EXISTE), 404),
                c(CLIENTE, json(post("/api/v1/freelance/proyectos/" + NO_EXISTE + "/calificar"), CALIFICACION), 404),
                c(CLIENTE, MockMvcRequestBuilders.get("/api/v1/freelance/proyectos/" + NO_EXISTE + "/calificaciones"), 200), // lista vacía
                c(null, MockMvcRequestBuilders.get("/api/v1/perfiles/" + NO_EXISTE + "/calificaciones"), 404),
                c(FREELANCER, MockMvcRequestBuilders.delete("/api/v1/perfiles/removeMySkill/" + NO_EXISTE), 200), // borrado idempotente
                c(FREELANCER, MockMvcRequestBuilders.delete("/api/v1/perfiles/removeMyProject/" + NO_EXISTE), 404),
                c(EMPRESA, MockMvcRequestBuilders.get("/api/v1/vacantes/postulaciones").param("vacanteId", NO_EXISTE), 404)
        ));
    }

    // ---------- UUID: inexistente (404) y mal formado (400) ----------
    @Test void uuids() throws Exception {
        verificar(List.of(
                c(null, MockMvcRequestBuilders.get("/api/v1/vacantes/uuid/" + UUID_NO_EXISTE), 404),
                c(null, MockMvcRequestBuilders.get("/api/v1/vacantes/uuid/no-es-un-uuid"), 400),
                c(null, MockMvcRequestBuilders.get("/api/v1/freelance/proyectos/uuid/" + UUID_NO_EXISTE), 404),
                c(null, MockMvcRequestBuilders.get("/api/v1/freelance/proyectos/uuid/no-es-un-uuid"), 400),
                c(EMPRESA, MockMvcRequestBuilders.get("/api/v1/empresas/uuid/" + UUID_NO_EXISTE), 404),
                c(EMPRESA, MockMvcRequestBuilders.get("/api/v1/empresas/uuid/no-es-un-uuid"), 400)
        ));
    }

    // ---------- 400: ids que no son números ----------
    @Test void idsNoNumericos_400() throws Exception {
        verificar(List.of(
                c(INSTRUCTOR, json(put("/api/v1/bootcamps/abc"), BOOTCAMP), 400),
                c(INSTRUCTOR, MockMvcRequestBuilders.delete("/api/v1/instructores/abc"), 400),
                c(EMPRESA, MockMvcRequestBuilders.get("/api/v1/empresas/abc"), 400),
                c(EMPRESA, MockMvcRequestBuilders.get("/api/v1/empresas/recruiters/abc"), 400),
                c(EMPRESA, MockMvcRequestBuilders.get("/api/v1/vacantes/abc/postulaciones"), 400),
                c(CLIENTE, MockMvcRequestBuilders.get("/api/v1/freelance/proyectos/abc/hitos"), 400),
                c(CLIENTE, MockMvcRequestBuilders.get("/api/v1/freelance/proyectos/abc/pagos"), 400),
                c(CLIENTE, post("/api/v1/freelance/proyectos/392/pagos/checkout").param("hitoId", "abc"), 400),
                c(CLIENTE, put("/api/v1/freelance/proyectos/382/asignar").param("employeeId", "abc"), 400),
                c(FREELANCER, MockMvcRequestBuilders.delete("/api/v1/perfiles/removeMySkill/abc"), 400)
        ));
    }

    // ---------- 400: parámetros obligatorios faltantes o fuera del enum ----------
    @Test void parametros_400() throws Exception {
        verificar(List.of(
                c(CLIENTE, post("/api/v1/freelance/proyectos/392/pagos/checkout"), 400),                 // falta hitoId
                c(CLIENTE, put("/api/v1/freelance/proyectos/382/asignar"), 400),                          // falta employeeId
                c(FREELANCER, put("/api/v1/freelance/proyectos/392/hitos/548/estado"), 400),             // falta nuevoEstado
                c(FREELANCER, put("/api/v1/freelance/proyectos/392/hitos/548/estado").param("nuevoEstado", "PAGADO"), 400),
                c(EMPRESA, put("/api/v1/vacantes/461/postulaciones/758/estado"), 400),                   // falta nuevoEstado
                c(EMPRESA, put("/api/v1/vacantes/461/postulaciones/758/estado").param("nuevoEstado", "CONTRATADO"), 400),
                c(POSTULANTE, multipart("/api/v1/vacantes/461/postulaciones/me").file(pdf("cv")), 400),  // falta mensaje
                c(FREELANCER, post("/api/v1/perfiles/addMySkill").param("nombre", "Go"), 400),          // falta nivel
                c(FREELANCER, post("/api/v1/perfiles/addMySkill").param("nombre", "Go").param("nivel", "alto"), 400),
                c(FREELANCER, multipart("/api/v1/perfiles/addMyProject").param("nombre", "x"), 400),    // falta descripcion
                c(FREELANCER, multipart("/api/v1/perfiles/uploadMyCV"), 400),                           // falta file
                c(EMPRESA, multipart("/api/v1/empresas/logo"), 400),
                c(null, MockMvcRequestBuilders.get("/api/v1/bootcamps/modalidad/MARCIANA"), 400)
        ));
    }

    // ---------- 400: bodies inválidos ----------
    @Test void bodiesInvalidos_400() throws Exception {
        verificar(List.of(
                c(null, json(post("/api/v1/auth/register"), "{}"), 400),
                c(null, json(post("/api/v1/auth/login"), "{\"usuario\":\"\",\"password\":\"\"}"), 400),
                c(null, json(post("/api/v1/auth/google"), "{}"), 400),
                c(null, json(post("/api/v1/auth/login"), "{esto no es json"), 400),
                c(INSTRUCTOR, json(post("/api/v1/bootcamps/me"), "{\"titulo\":\"x\"}"), 400),
                c(INSTRUCTOR, json(post("/api/v1/bootcamps/me"), BOOTCAMP.replace("\"cupos\":20", "\"cupos\":0")), 400),
                c(INSTRUCTOR, json(post("/api/v1/bootcamps/me"), BOOTCAMP.replace("\"VIRTUAL\"", "\"HOLOGRAMA\"")), 400),
                c(INSTRUCTOR, json(post("/api/v1/instructores"), "{\"nombre\":\"\"}"), 400),
                c(INSTRUCTOR, json(post("/api/v1/calificaciones/bootcamps/1"), "{\"employeeId\":1,\"puntaje\":9,\"comentario\":\"x\"}"), 400),
                c(EMPRESA, json(post("/api/v1/empresas"), "{\"nombre\":\"\"}"), 400),
                c(EMPRESA, json(put("/api/v1/empresas/148"), "{\"nombre\":\"\",\"descripcion\":\"\"}"), 400),
                c(EMPRESA, json(post("/api/v1/empresas/recruiters"), "{\"nombre\":\"R\",\"apellido\":\"T\",\"email\":\"no-mail\",\"fechaNacimiento\":\"1990-01-01\"}"), 400),
                c(EMPRESA, json(post("/api/v1/vacantes/me"), VACANTE.replace("2099-01-01", "2000-01-01")), 400),   // finalización pasada
                c(EMPRESA, json(put("/api/v1/vacantes/461"), "{\"puesto\":\"\"}"), 400),
                c(RECRUITER, json(post("/api/v1/recruiter/entrevistas"), "{\"postulacionId\":758,\"fecha\":\"2000-01-01\",\"hora\":\"10:00:00\"}"), 400),
                c(CLIENTE, json(post("/api/v1/freelance/proyectos/me"), PROYECTO.replace("300000", "-5")), 400),
                c(CLIENTE, json(put("/api/v1/freelance/proyectos/383"), "{\"nombreProyecto\":\"\"}"), 400),
                c(FREELANCER, json(post("/api/v1/freelance/proyectos/382/postulaciones/me"), "{\"descripcion\":\"x\",\"costo\":0,\"employeeId\":316}"), 400),
                c(CLIENTE, json(post("/api/v1/freelance/proyectos/392/hitos"), "{\"titulo\":\"\"}"), 400),
                c(CLIENTE, json(post("/api/v1/freelance/proyectos/400/calificar"), "{\"puntaje\":0,\"comentario\":\"x\"}"), 400),
                c(CLIENTE, json(post("/api/v1/freelance/proyectos/400/calificar"), "{\"puntaje\":5,\"comentario\":\"" + "x".repeat(300) + "\"}"), 400),
                c(FREELANCER, json(put("/api/v1/freelancer/medios-cobro"), "{\"tipo\":\"TRANSFERENCIA\",\"titular\":\"\"}"), 400),
                c(FREELANCER, json(put("/api/v1/freelancer/medios-cobro"), "{\"tipo\":\"BITCOIN\",\"titular\":\"X\"}"), 400),
                c(FREELANCER, json(put("/api/v1/freelancer/medios-cobro"), "{\"tipo\":\"MERCADOPAGO\",\"titular\":\"X\",\"mercadoPagoEmail\":\"no-mail\"}"), 400),
                c(CLIENTE, json(put("/api/v1/client/me"), "{\"fechaNacimiento\":\"no-es-fecha\"}"), 400),
                c(FREELANCER, json(put("/api/v1/perfiles/updateProfile"), "{\"fechaNacimiento\":\"no-es-fecha\"}"), 400)
        ));
    }

    // ---------- 400: archivos con tipo o tamaño inválido ----------
    @Test void archivosInvalidos_400() throws Exception {
        var txt = new org.springframework.mock.web.MockMultipartFile("file", "a.txt", "text/plain", "hola".getBytes());
        var txtCv = new org.springframework.mock.web.MockMultipartFile("cv", "a.txt", "text/plain", "hola".getBytes());
        var pdfGrande = new org.springframework.mock.web.MockMultipartFile("file", "cv.pdf", "application/pdf", new byte[6 * 1024 * 1024]);
        verificar(List.of(
                c(POSTULANTE, multipart("/api/v1/perfiles/uploadMyCV").file(txt), 400),
                c(POSTULANTE, multipart("/api/v1/perfiles/uploadMyCV").file(pdfGrande), 400),
                c(POSTULANTE, multipart("/api/v1/portfolio/296/cv").file(txt), 400),
                c(FREELANCER, multipart("/api/v1/perfiles/uploadMyPhoto").file(txt), 400),
                c(FREELANCER, multipart("/api/v1/perfiles/uploadMyBanner").file(txt), 400),
                c(EMPRESA, multipart("/api/v1/empresas/logo").file(txt), 400),
                c(EMPRESA, multipart("/api/v1/empresas/banner").file(txt), 400),
                c(POSTULANTE, multipart("/api/v1/vacantes/1/postulaciones/me").file(txtCv).param("mensaje", "x"), 400)
        ));
    }

    // ---------- 401: sin token ----------
    @Test void sinToken_401() throws Exception {
        String[] protegidos = {
                "/api/v1/perfiles/getProfile", "/api/v1/perfiles/downloadMyCV", "/api/v1/bootcamps/me", "/api/v1/instructores",
                "/api/v1/instructores/bootcamps", "/api/v1/inscripciones/me", "/api/v1/empresas/my", "/api/v1/empresas/148",
                "/api/v1/empresas/dashboard", "/api/v1/empresas/recruiters", "/api/v1/vacantes/me", "/api/v1/vacantes/postulaciones",
                "/api/v1/vacantes/461/postulaciones", "/api/v1/recruiter/entrevistas", "/api/v1/freelance/proyectos/me/publicados",
                "/api/v1/freelance/proyectos/me/asignados", "/api/v1/freelance/proyectos/392/hitos", "/api/v1/freelance/proyectos/394/pagos",
                "/api/v1/freelance/proyectos/382/postulaciones", "/api/v1/freelance/proyectos/400/calificaciones",
                "/api/v1/perfiles/me/calificaciones", "/api/v1/client/dashboard", "/api/v1/client/proyectos", "/api/v1/client/me",
                "/api/v1/freelancer/finances", "/api/v1/freelancer/transactions", "/api/v1/freelancer/medios-cobro",
                "/api/v1/freelancer/proyectos", "/api/v1/freelancer/opiniones"};
        List<Caso> casos = new ArrayList<>();
        for (String u : protegidos) casos.add(c(null, MockMvcRequestBuilders.get(u), 401));
        casos.add(c(null, json(post("/api/v1/freelance/proyectos/me"), PROYECTO), 401));
        casos.add(c(null, json(post("/api/v1/vacantes/me"), VACANTE), 401));
        casos.add(c(null, json(post("/api/v1/bootcamps/me"), BOOTCAMP), 401));
        casos.add(c(null, MockMvcRequestBuilders.delete("/api/v1/perfiles/me"), 401));
        casos.add(c(null, json(put("/api/v1/freelancer/medios-cobro"), "{}"), 401));
        verificar(casos);
    }

    // ---------- 403: rol equivocado ----------
    @Test void rolEquivocado_403() throws Exception {
        verificar(List.of(
                c(FREELANCER, MockMvcRequestBuilders.get("/api/v1/client/dashboard"), 403),
                c(EMPRESA, MockMvcRequestBuilders.get("/api/v1/client/me"), 403),
                c(CLIENTE, MockMvcRequestBuilders.get("/api/v1/empresas/my"), 403),
                c(FREELANCER, MockMvcRequestBuilders.get("/api/v1/empresas/dashboard"), 403),
                c(POSTULANTE, MockMvcRequestBuilders.get("/api/v1/empresas/recruiters"), 403),
                c(CLIENTE, MockMvcRequestBuilders.get("/api/v1/vacantes/me"), 403),
                c(CLIENTE, MockMvcRequestBuilders.get("/api/v1/freelancer/finances"), 404),                 // SecurityConfig deja pasar CLIENTE; el service no lo encuentra como freelancer
                c(EMPRESA, MockMvcRequestBuilders.get("/api/v1/freelancer/transactions"), 403),
                c(CLIENTE, MockMvcRequestBuilders.get("/api/v1/freelancer/medios-cobro"), 403),
                c(EMPRESA, MockMvcRequestBuilders.get("/api/v1/instructores"), 403),
                c(CLIENTE, json(post("/api/v1/bootcamps/me"), BOOTCAMP), 403),
                c(FREELANCER, json(post("/api/v1/vacantes/me"), VACANTE), 403),
                c(EMPRESA, json(post("/api/v1/freelance/proyectos/me"), PROYECTO), 403),
                c(FREELANCER, json(post("/api/v1/freelance/proyectos/me"), PROYECTO), 403),
                c(CLIENTE, json(post("/api/v1/freelance/proyectos/382/postulaciones/me"), PROPUESTA), 403),
                c(FREELANCER, put("/api/v1/freelance/proyectos/382/asignar").param("employeeId", "316"), 403),
                c(CLIENTE, multipart("/api/v1/vacantes/461/postulaciones/me").file(pdf("cv")).param("mensaje", "x"), 403),
                c(POSTULANTE, put("/api/v1/vacantes/461/postulaciones/758/estado").param("nuevoEstado", "RECHAZADO"), 403),
                c(POSTULANTE, json(post("/api/v1/recruiter/entrevistas"), ENTREVISTA.formatted("758")), 403)
        ));
    }

    // ---------- 403: dueño equivocado (mismo rol, recurso ajeno) ----------
    @Test void recursoAjeno_403() throws Exception {
        verificar(List.of(
                c("cliente05", json(put("/api/v1/freelance/proyectos/383"), PROYECTO), 403),
                c("cliente05", put("/api/v1/freelance/proyectos/384/cancelar"), 403),
                c("cliente05", put("/api/v1/freelance/proyectos/382/asignar").param("employeeId", "316"), 403),
                c("cliente05", json(post("/api/v1/freelance/proyectos/392/hitos"), HITO), 403),
                c("cliente05", MockMvcRequestBuilders.get("/api/v1/freelance/proyectos/392/hitos"), 403),
                c("cliente05", MockMvcRequestBuilders.get("/api/v1/freelance/proyectos/394/pagos"), 403),
                c("cliente05", post("/api/v1/freelance/proyectos/392/pagos/checkout").param("hitoId", "548"), 403),
                c("cliente05", json(post("/api/v1/freelance/proyectos/400/calificar"), CALIFICACION), 403),
                c("freelancer05", put("/api/v1/freelance/proyectos/392/hitos/548/estado").param("nuevoEstado", "COMPLETADO"), 403),
                c("freelancer05", MockMvcRequestBuilders.get("/api/v1/freelance/proyectos/394/pagos"), 403),
                c("empresa05", json(put("/api/v1/vacantes/461"), VACANTE), 403),
                c("empresa05", MockMvcRequestBuilders.delete("/api/v1/vacantes/461"), 403),
                c("empresa05", MockMvcRequestBuilders.get("/api/v1/vacantes/461/postulaciones"), 403),
                c("empresa05", put("/api/v1/vacantes/461/postulaciones/758/estado").param("nuevoEstado", "RECHAZADO"), 403),
                c("empresa05", json(put("/api/v1/empresas/148"), EMPRESA_JSON), 403),
                c("empresa05", MockMvcRequestBuilders.get("/api/v1/empresas/recruiters/148"), 404),          // otra empresa: no revela que existe
                c("empresa05", MockMvcRequestBuilders.delete("/api/v1/empresas/recruiters/148"), 404),
                c("AnaRecruiter", json(post("/api/v1/recruiter/entrevistas"), ENTREVISTA.formatted("758")), 403),
                c(POSTULANTE, multipart("/api/v1/portfolio/316/cv").file(pdf("file")), 403),
                c(FREELANCER, json(put("/api/v1/bootcamps/1"), BOOTCAMP), 403),
                c(FREELANCER, MockMvcRequestBuilders.delete("/api/v1/bootcamps/1"), 403)
        ));
    }

    @Test void propuestasDeProyectoAjeno_noSeFiltran() throws Exception {
        // Otro cliente recibe 200 pero sin ninguna propuesta (montos ajenos ocultos)
        get("cliente05", "/api/v1/freelance/proyectos/382/postulaciones").andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
        // Un freelancer solo ve la suya
        get(FREELANCER, "/api/v1/freelance/proyectos/382/postulaciones").andExpect(status().isOk())
                .andExpect(jsonPath("$[*].empleadoId", org.hamcrest.Matchers.everyItem(org.hamcrest.Matchers.is(316))));
    }

    // ---------- 422: reglas de negocio ----------
    @Test void reglasDeNegocio() throws Exception {
        verificar(List.of(
                c(CLIENTE, put("/api/v1/freelance/proyectos/392/asignar").param("employeeId", "316"), 422),        // ya en progreso
                c(CLIENTE, json(post("/api/v1/freelance/proyectos/382/hitos"), HITO), 422),                         // sin adjudicar
                c(CLIENTE, json(post("/api/v1/freelance/proyectos/392/calificar"), CALIFICACION), 422),            // no finalizado
                c(CLIENTE, post("/api/v1/freelance/proyectos/392/pagos/checkout").param("hitoId", "550"), 422),     // fuera de orden
                c(CLIENTE, post("/api/v1/freelance/proyectos/394/pagos/checkout").param("hitoId", "556"), 422),     // ya pagado
                c(FREELANCER, put("/api/v1/freelance/proyectos/392/hitos/548/estado").param("nuevoEstado", "COMPLETADO"), 422), // sin pagar
                c(CLIENTE, put("/api/v1/freelance/proyectos/392/hitos/548/estado").param("nuevoEstado", "EN_PROGRESO"), 422),
                c(POSTULANTE, multipart("/api/v1/vacantes/461/postulaciones/me").file(pdf("cv")).param("mensaje", "x"), 422), // duplicada
                c(EMPRESA, json(post("/api/v1/empresas"), EMPRESA_JSON), 422)                                        // ya tiene empresa
        ));
    }

    // ---------- Cuentas sin datos: los dashboards devuelven ceros, no errores ----------
    @Test void cuentasNuevas_dashboardsEnCero() throws Exception {
        postJson(null, "/api/v1/auth/register", """
                {"usuario":"borde_free","email":"borde_free@test.com","password":"Password123","rol":"FREELANCER"}""").andExpect(status().isCreated());
        postJson(null, "/api/v1/auth/register", """
                {"usuario":"borde_cli","email":"borde_cli@test.com","password":"Password123","rol":"CLIENTE"}""").andExpect(status().isCreated());
        postJson(null, "/api/v1/auth/register", """
                {"usuario":"borde_emp","email":"borde_emp@test.com","password":"Password123","rol":"EMPRESA"}""").andExpect(status().isCreated());

        get("borde_free", "/api/v1/freelancer/finances").andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(0.0)).andExpect(jsonPath("$.pending").value(0.0));
        get("borde_free", "/api/v1/freelancer/transactions").andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(0));
        get("borde_free", "/api/v1/freelancer/proyectos").andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(0));
        get("borde_free", "/api/v1/freelancer/opiniones").andExpect(status().isOk()).andExpect(jsonPath("$.totalResenas").value(0));
        int stMedio = get("borde_free", "/api/v1/freelancer/medios-cobro").andReturn().getResponse().getStatus();
        org.junit.jupiter.api.Assertions.assertTrue(stMedio == 204 || stMedio == 404, "medios-cobro sin configurar fue " + stMedio);
        int stCv = get("borde_free", "/api/v1/perfiles/downloadMyCV").andReturn().getResponse().getStatus();
        org.junit.jupiter.api.Assertions.assertTrue(stCv == 204 || stCv == 404, "downloadMyCV sin CV fue " + stCv);

        get("borde_cli", "/api/v1/client/dashboard").andExpect(status().isOk())
                .andExpect(jsonPath("$.activeProjects").value(0)).andExpect(jsonPath("$.completedProjects").value(0))
                .andExpect(jsonPath("$.totalSpent").value(0.0)).andExpect(jsonPath("$.pendingProposals").value(0));
        get("borde_cli", "/api/v1/client/proyectos").andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(0));

        get("borde_emp", "/api/v1/empresas/dashboard").andExpect(status().isOk())
                .andExpect(jsonPath("$.totalVacantes").value(0)).andExpect(jsonPath("$.totalPostulaciones").value(0));
        get("borde_emp", "/api/v1/vacantes/me").andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(0));
        get("borde_emp", "/api/v1/empresas/recruiters").andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(0));
    }

    // ---------- Errores con formato uniforme y sin filtrar detalles internos ----------
    @Test void erroresTienenFormatoUniforme() throws Exception {
        get(null, "/api/v1/freelance/proyectos/" + NO_EXISTE).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").isNotEmpty()).andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.date").isNotEmpty());
        postJson(CLIENTE, "/api/v1/freelance/proyectos/me", "{}").andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details").isArray());
    }

    @Test void rutaInexistente_404() throws Exception {
        get(CLIENTE, "/api/v1/esto/no/existe").andExpect(status().isNotFound());
    }

    @Test void metodoNoSoportado_405() throws Exception {
        call(CLIENTE, MockMvcRequestBuilders.delete("/api/v1/client/dashboard")).andExpect(status().isMethodNotAllowed());
    }

    // ---------- Bodies reutilizados ----------
    private static final String BOOTCAMP = """
            {"titulo":"Borde","descripcion":"d","inicio":"2099-01-01","fin":"2099-03-01","modalidad":"VIRTUAL","tipo":"PARTICULAR",
             "cupos":20,"instructor_id":1,"empresa_id":148}""";
    private static final String INSTRUCTOR_JSON = """
            {"nombre":"A","apellido":"B","titulo":"C","presentacion":"D","tipo":"PARTICULAR"}""";
    private static final String EMPRESA_JSON = """
            {"nombre":"E","descripcion":"D","sitio_web":"https://e.com","ubicacion":"U"}""";
    private static final String RECRUITER_JSON = """
            {"nombre":"R","apellido":"T","email":"r@t.com","fechaNacimiento":"1990-01-01","area":"IT"}""";
    private static final String VACANTE = """
            {"puesto":"Dev","descripcion":"d","finalizacion":"2099-01-01"}""";
    private static final String ENTREVISTA = """
            {"postulacionId":%s,"fecha":"2099-01-01","hora":"10:00:00","link":"https://meet.test"}""";
    private static final String PROYECTO = """
            {"nombreProyecto":"P","area":"IT","descripcion":"d","presupuestoMax":300000,"presupuestoMin":100000}""";
    private static final String PROPUESTA = """
            {"descripcion":"d","costo":1000,"employeeId":316}""";
    private static final String HITO = """
            {"titulo":"H"}""";
    private static final String CALIFICACION = """
            {"puntaje":5,"comentario":"ok"}""";
}
