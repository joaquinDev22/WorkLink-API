package com.example.Proyecto_Final_Progra3.api;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** BootcampController, InstructorController, InscripcionController, CalificacionBootcampController, CertificadoController. */
class BootcampApiTest extends ApiTestBase {

    private String bootcampJson(String titulo) {
        return """
                {"titulo":"%s","descripcion":"Bootcamp de prueba","inicio":"%s","fin":"%s",
                 "modalidad":"VIRTUAL","tipo":"PARTICULAR","cupos":20,"instructor_id":1,"empresa_id":148,
                 "precio":"50000","etiquetas":["java","spring"]}"""
                .formatted(titulo, LocalDate.now().plusDays(10), LocalDate.now().plusDays(60));
    }

    private Integer crearBootcamp() throws Exception {
        postJson(INSTRUCTOR, "/api/v1/bootcamps/me", bootcampJson("Bootcamp Test")).andExpect(status().isCreated());
        return id("select max(bootcamp_id) from bootcamp");
    }

    // ---------- Bootcamps ----------
    @Test void listar_publico() throws Exception { get(null, "/api/v1/bootcamps").andExpect(status().isOk()); }
    @Test void buscarPorId_publico() throws Exception { get(null, "/api/v1/bootcamps/1").andExpect(status().isOk()); }
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
        // Un FREELANCER tiene acceso a /bootcamps/** por SecurityConfig, pero no es dueño del bootcamp 1
        putJson(FREELANCER, "/api/v1/bootcamps/1", bootcampJson("Hackeado")).andExpect(status().isForbidden());
    }

    // ---------- Instructores ----------
    private static final String INSTRUCTOR_JSON = """
            {"nombre":"Ana","apellido":"Test","titulo":"Ing. Sistemas","presentacion":"Instructora de prueba","tipo":"EMPRESA","empresa_id":148}""";

    @Test void instructores_listar() throws Exception { get(INSTRUCTOR, "/api/v1/instructores").andExpect(status().isOk()); }
    @Test void instructores_bootcamps() throws Exception { get(INSTRUCTOR, "/api/v1/instructores/bootcamps").andExpect(status().isOk()); }

    @Test void instructores_crear_modificar_eliminar() throws Exception {
        Integer perfil = id("select perfil_id from perfil where usuario='freelancer07'");
        String json = INSTRUCTOR_JSON.replace("}", ",\"perfil_id\":" + perfil + "}");
        postJson(INSTRUCTOR, "/api/v1/instructores", json).andExpect(status().isCreated());
        Integer id = id("select max(instructor_id) from instructor");
        putJson(INSTRUCTOR, "/api/v1/instructores/{id}", json.replace("Ana", "Ana María"), id).andExpect(status().isOk());
        int st = delete(INSTRUCTOR, "/api/v1/instructores/{id}", id).andReturn().getResponse().getStatus();
        org.junit.jupiter.api.Assertions.assertTrue(st == 200 || st == 204, "fue " + st);
    }

    // ---------- Inscripciones ----------
    @Test void inscribirse_y_listar_y_cancelar() throws Exception {
        Integer bootcampId = crearBootcamp();
        call(POSTULANTE, post("/api/v1/inscripciones/me/{b}", bootcampId)).andExpect(status().isCreated());
        get(POSTULANTE, "/api/v1/inscripciones/me").andExpect(status().isOk());
        Integer insc = id("select max(inscripcion_id) from inscripcion_bootcamp where bootcamp_id=?", bootcampId);
        call(POSTULANTE, put("/api/v1/inscripciones/{i}/cancelar", insc)).andExpect(status().isOk());
    }

    // ---------- Calificación y certificado ----------
    @Test void calificarBootcamp() throws Exception {
        jdbc.update("delete from calificacion_bootcamp where bootcamp_id=1 and employee_id=1");
        postJson(INSTRUCTOR, "/api/v1/calificaciones/bootcamps/1", """
                {"employeeId":1,"puntaje":5,"comentario":"Excelente"}""").andExpect(status().isCreated());
    }

    @Test void generarCertificado() throws Exception {
        call(INSTRUCTOR, post("/api/v1/certificados/generar/1")).andExpect(status().isCreated());
    }
}
