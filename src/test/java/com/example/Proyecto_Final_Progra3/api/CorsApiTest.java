package com.example.Proyecto_Final_Progra3.api;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Preflight CORS (OPTIONS) y headers en respuestas reales, según SecurityConfig.corsConfigurationSource(). */
class CorsApiTest extends ApiTestBase {

    private static final String FRONT = "http://localhost:4200";

    private org.springframework.test.web.servlet.ResultActions preflight(String url, String origin, String metodo, String headers) throws Exception {
        return mvc.perform(options(url)
                .header(HttpHeaders.ORIGIN, origin)
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, metodo)
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, headers));
    }

    @Test void preflight_frontPermitido_endpointProtegido() throws Exception {
        // El preflight no lleva token: tiene que pasar igual aunque el endpoint requiera auth
        preflight("/api/v1/client/dashboard", FRONT, "GET", "authorization,content-type")
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, FRONT))
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true"))
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, containsString("GET")))
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, containsString("authorization")))
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "3600"));
    }

    @Test void preflight_todosLosMetodosUsados() throws Exception {
        for (String m : new String[]{"GET", "POST", "PUT", "DELETE", "PATCH"})
            preflight("/api/v1/freelance/proyectos/1", FRONT, m, "authorization,content-type")
                    .andExpect(status().isOk())
                    .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, containsString(m)));
    }

    @Test void preflight_login() throws Exception {
        preflight("/api/v1/auth/login", FRONT, "POST", "content-type")
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, FRONT));
    }

    @Test void preflight_multipart() throws Exception {
        preflight("/api/v1/perfiles/uploadMyCV", FRONT, "POST", "authorization")
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, FRONT));
    }

    @Test void preflight_otroPuertoLocalhost() throws Exception {
        preflight("/api/v1/vacantes", "http://localhost:5173", "GET", "content-type")
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:5173"));
    }

    @Test void preflight_subdominioWorklink() throws Exception {
        preflight("/api/v1/vacantes", "https://app.worklink.com", "GET", "content-type")
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "https://app.worklink.com"));
    }

    @Test void preflight_origenNoPermitido_403() throws Exception {
        preflight("/api/v1/vacantes", "https://sitio-malicioso.com", "GET", "content-type")
                .andExpect(status().isForbidden())
                .andExpect(header().doesNotExist(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN));
    }

    @Test void preflight_headerNoPermitido_403() throws Exception {
        preflight("/api/v1/vacantes", FRONT, "GET", "x-header-raro")
                .andExpect(status().isForbidden());
    }

    @Test void preflight_metodoNoPermitido_403() throws Exception {
        preflight("/api/v1/vacantes", FRONT, "TRACE", "content-type")
                .andExpect(status().isForbidden());
    }

    @Test void respuestaReal_traeHeadersCors() throws Exception {
        mvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/api/v1/vacantes").header(HttpHeaders.ORIGIN, FRONT))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, FRONT))
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true"));
    }

    @Test void respuestaReal_exponeAuthorizationYContentDisposition() throws Exception {
        mvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/api/v1/perfiles/getProfile").header(HttpHeaders.ORIGIN, FRONT)
                        .header(HttpHeaders.AUTHORIZATION, bearer(FREELANCER)))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, containsString("Authorization")))
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, containsString("Content-Disposition")));
    }

    @Test void respuestaDeError_tambienTraeCors() throws Exception {
        // Si un 401 no trae CORS, el navegador lo muestra como "CORS error" y el front no puede leer el mensaje
        mvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/api/v1/client/dashboard").header(HttpHeaders.ORIGIN, FRONT))
                .andExpect(status().isUnauthorized())
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, FRONT));
    }

    @Test void respuestaReal_origenNoPermitido_403() throws Exception {
        mvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/api/v1/vacantes").header(HttpHeaders.ORIGIN, "https://sitio-malicioso.com"))
                .andExpect(status().isForbidden());
    }
}
