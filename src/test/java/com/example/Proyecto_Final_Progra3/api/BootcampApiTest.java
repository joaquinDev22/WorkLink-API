package com.example.Proyecto_Final_Progra3.api;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** BootcampController, InstructorController, InscripcionController, CalificacionBootcampController, CertificadoController. */
class BootcampApiTest extends ApiTestBase {

    // ---------- Bootcamps ----------
    @Test void listar_publico() throws Exception { get(null, "/api/v1/bootcamps").andExpect(status().isOk()); }
    @Test void buscarPorId_publico() throws Exception { get(null, "/api/v1/bootcamps/{id}", crearBootcamp()).andExpect(status().isOk()); }
    @Test void buscarPorId_inexistente_404() throws Exception { get(null, "/api/v1/bootcamps/999999").andExpect(status().isNotFound()); }
    @Test void disponibles_publico() throws Exception { get(null, "/api/v1/bootcamps/disponibles").andExpect(status().isOk()); }
    @Test void porModalidad_publico() throws Exception { get(null, "/api/v1/bootcamps/modalidad/VIRTUAL").andExpect(status().isOk()); }
    @Test void instructores_crear_sinPerfil_400() throws Exception {
        postJson(INSTRUCTOR, "/api/v1/instructores", INSTRUCTOR_JSON).andExpect(status().isBadRequest());
    }

    @Test void porModalidad_invalida_400() throws Exception { get(null, "/api/v1/bootcamps/modalidad/MARCIANA").andExpect(status().isBadRequest()); }
    @Test void misBootcamps() throws Exception { get(INSTRUCTOR, "/api/v1/bootcamps/me").andExpect(status().isOk()); }

    @Test void crear() throws Exception { crearBootcamp(); }

    @Test void crear_invalido_400() throws Exception {
        postJson(INSTRUCTOR, "/api/v1/bootcamps/me", """
                {"titulo":"","descripcion":"","cupos":0}""").andExpect(status().isBadRequest());
    }

    @Test void modificar() throws Exception {
        Integer id = crearBootcamp();
        putJson(INSTRUCTOR, "/api/v1/bootcamps/{id}", bootcampJson("Bootcamp Editado"), id).andExpect(status().isOk());
    }

    @Test void eliminar() throws Exception {
        Integer id = crearBootcamp();
        int st = delete(INSTRUCTOR, "/api/v1/bootcamps/{id}", id).andReturn().getResponse().getStatus();
        org.junit.jupiter.api.Assertions.assertTrue(st == 200 || st == 204, "fue " + st);
    }

    @Test void modificar_bootcampAjeno_403() throws Exception {
        // Un FREELANCER tiene acceso a /bootcamps/** por SecurityConfig, pero no es dueño del bootcamp
        putJson(FREELANCER, "/api/v1/bootcamps/{id}", bootcampJson("Hackeado"), crearBootcamp()).andExpect(status().isForbidden());
    }

    // ---------- Instructores ----------
    private static final String INSTRUCTOR_JSON = """
            {"nombre":"Ana","apellido":"Test","titulo":"Ing. Sistemas","presentacion":"Instructora de prueba","tipo":"EMPRESA","empresa_id":%d}""";

    @Test void instructores_listar() throws Exception { get(INSTRUCTOR, "/api/v1/instructores").andExpect(status().isOk()); }
    @Test void instructores_bootcamps() throws Exception { get(INSTRUCTOR, "/api/v1/instructores/bootcamps").andExpect(status().isOk()); }

    @Test void instructores_crear_modificar_eliminar() throws Exception {
        Integer perfil = id("select perfil_id from perfil where usuario='freelancer07'");
        String json = INSTRUCTOR_JSON.formatted(empresaId).replace("}", ",\"perfil_id\":" + perfil + "}");
        postJson(INSTRUCTOR, "/api/v1/instructores", json).andExpect(status().isCreated());
        Integer id = id("select max(instructor_id) from instructor");
        putJson(INSTRUCTOR, "/api/v1/instructores/{id}", json.replace("Ana", "Ana María"), id).andExpect(status().isOk());
        int st = delete(INSTRUCTOR, "/api/v1/instructores/{id}", id).andReturn().getResponse().getStatus();
        org.junit.jupiter.api.Assertions.assertTrue(st == 200 || st == 204, "fue " + st);
    }

    // ---------- Inscripciones ----------
    @Test void inscribirse_y_listar_y_cancelar() throws Exception {
        Integer insc = inscribirPostulante(crearBootcamp());
        get(POSTULANTE, "/api/v1/inscripciones/me").andExpect(status().isOk());
        call(POSTULANTE, put("/api/v1/inscripciones/{i}/cancelar", insc)).andExpect(status().isOk());
    }

    // ---------- Calificación y certificado ----------
    @Test void calificarBootcamp() throws Exception {
        Integer b = crearBootcamp();
        inscribirPostulante(b);
        postJson(INSTRUCTOR, "/api/v1/calificaciones/bootcamps/{b}", """
                {"employeeId":%d,"puntaje":5,"comentario":"Excelente"}""".formatted(postulanteEmp), b).andExpect(status().isCreated());
    }

    @Test void generarCertificado() throws Exception {
        Integer insc = inscribirPostulante(crearBootcamp());
        call(INSTRUCTOR, post("/api/v1/certificados/generar/{i}", insc)).andExpect(status().isCreated());
    }
}
