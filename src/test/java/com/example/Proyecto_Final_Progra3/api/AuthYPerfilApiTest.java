package com.example.Proyecto_Final_Progra3.api;

import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** AuthController, PerfilController, HabilidadController + reglas de seguridad generales. */
class AuthYPerfilApiTest extends ApiTestBase {

    // ---------- Auth ----------
    @Test void register_creaUsuario() throws Exception {
        postJson(null, "/api/v1/auth/register", """
                {"usuario":"test_reg_01","email":"test_reg_01@test.com","password":"Password123","rol":"FREELANCER"}""")
                .andExpect(status().isCreated());
    }

    @Test void register_usuarioDuplicado_422() throws Exception {
        postJson(null, "/api/v1/auth/register", """
                {"usuario":"demo_cliente","email":"otro@test.com","password":"Password123","rol":"CLIENTE"}""")
                .andExpect(status().isUnprocessableContent());
    }

    @Test void register_datosInvalidos_400() throws Exception {
        postJson(null, "/api/v1/auth/register", """
                {"usuario":"x","email":"no-es-mail","password":"123","rol":"ADMIN"}""")
                .andExpect(status().isBadRequest());
    }

    @Test void login_ok_devuelveToken() throws Exception {
        postJson(null, "/api/v1/auth/login", """
                {"usuario":"demo_cliente","password":"Demo1234"}""")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", notNullValue()))
                .andExpect(jsonPath("$.rol").value("CLIENTE"));
    }

    @Test void login_passwordIncorrecta_401() throws Exception {
        postJson(null, "/api/v1/auth/login", """
                {"usuario":"demo_cliente","password":"incorrecta"}""")
                .andExpect(status().isUnauthorized());
    }

    @Test void loginGoogle_tokenInvalido_401() throws Exception {
        postJson(null, "/api/v1/auth/google", """
                {"idToken":"token-invalido","rol":"FREELANCER"}""")
                .andExpect(status().isUnauthorized());
    }

    // ---------- Seguridad general ----------
    @Test void endpointProtegido_sinToken_401() throws Exception {
        get(null, "/api/v1/perfiles/getProfile").andExpect(status().isUnauthorized());
    }

    @Test void endpointProtegido_tokenBasura_401() throws Exception {
        mvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/api/v1/perfiles/getProfile")
                        .header("Authorization", "Bearer basura"))
                .andExpect(status().isUnauthorized());
    }

    @Test void rolIncorrecto_403() throws Exception {
        get(FREELANCER, "/api/v1/client/dashboard").andExpect(status().isForbidden());
    }

    // ---------- Habilidades ----------
    @Test void habilidades_publico() throws Exception {
        get(null, "/api/v1/habilidades").andExpect(status().isOk());
    }

    // ---------- Perfil ----------
    @Test void getProfile() throws Exception {
        get(FREELANCER, "/api/v1/perfiles/getProfile").andExpect(status().isOk())
                .andExpect(jsonPath("$.email", notNullValue()));
    }

    @Test void getProfile_todosLosRoles() throws Exception {
        for (String u : new String[]{POSTULANTE, FREELANCER, EMPRESA, RECRUITER, CLIENTE, INSTRUCTOR})
            get(u, "/api/v1/perfiles/getProfile").andExpect(status().isOk());
    }

    @Test void updateProfile() throws Exception {
        putJson(FREELANCER, "/api/v1/perfiles/updateProfile", """
                {"nombre":"Demo","apellido":"Freelancer","descripcion":"Perfil actualizado por test","ubicacion":"Córdoba"}""")
                .andExpect(status().isOk());
    }

    @Test void uploadMyCV_y_downloadMyCV() throws Exception {
        upload(POSTULANTE, "/api/v1/perfiles/uploadMyCV", pdf("file")).andExpect(status().isOk());
        get(POSTULANTE, "/api/v1/perfiles/downloadMyCV").andExpect(status().isOk());
    }

    @Test void uploadMyPhoto() throws Exception {
        upload(FREELANCER, "/api/v1/perfiles/uploadMyPhoto", png("file")).andExpect(status().isOk());
    }

    @Test void uploadMyBanner() throws Exception {
        upload(FREELANCER, "/api/v1/perfiles/uploadMyBanner", png("file")).andExpect(status().isOk());
    }

    @Test void addMySkill_y_removeMySkill() throws Exception {
        call(FREELANCER, post("/api/v1/perfiles/addMySkill").param("nombre", "Kotlin").param("nivel", "3"))
                .andExpect(status().isOk());
        Integer habId = id("""
                select eh.habilidad_id from employee_habilidad eh join habilidad h on h.habilidad_id=eh.habilidad_id
                where eh.employee_id=316 and h.nombre_habilidad='Kotlin'""");
        delete(FREELANCER, "/api/v1/perfiles/removeMySkill/{id}", habId).andExpect(status().isOk());
    }

    @Test void addMyProject_y_removeMyProject() throws Exception {
        call(FREELANCER, multipart("/api/v1/perfiles/addMyProject").file(png("imagen"))
                .param("nombre", "Proyecto test").param("descripcion", "Desc test").param("link", "https://test.com"))
                .andExpect(status().isOk());
        Integer ppId = id("select max(proyecto_perfil_id) from proyecto_perfil where perfil_id=762");
        delete(FREELANCER, "/api/v1/perfiles/removeMyProject/{id}", ppId).andExpect(status().isOk());
    }

    @Test void removeMyProject_ajeno_403o404() throws Exception {
        Integer ajeno = id("select min(proyecto_perfil_id) from proyecto_perfil where perfil_id<>762");
        int st = delete(FREELANCER, "/api/v1/perfiles/removeMyProject/{id}", ajeno).andReturn().getResponse().getStatus();
        org.junit.jupiter.api.Assertions.assertTrue(st == 403 || st == 404, "esperado 403/404, fue " + st);
    }

    @Test void desactivarMiPerfil() throws Exception {
        delete(POSTULANTE, "/api/v1/perfiles/me").andExpect(status().isOk());
    }
}
