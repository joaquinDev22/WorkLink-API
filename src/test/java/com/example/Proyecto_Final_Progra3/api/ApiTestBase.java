package com.example.Proyecto_Final_Progra3.api;

import com.example.Proyecto_Final_Progra3.dto.responsedto.MercadoPagoPreferenceResponseDTO;
import com.example.Proyecto_Final_Progra3.service.freelance.freelanceImpl.MercadoPagoAdapter;
import com.example.Proyecto_Final_Progra3.service.perfil.perfilInterfaces.IJwtService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.AbstractMockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * Tests de integración contra MySQL real (base bd_proyecto_final_test, copia de la de demo).
 * Cada test corre en una transacción que se revierte al final: la base no queda modificada.
 * Mercado Pago se mockea para no llamar a la API externa.
 */
@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:mysql://localhost:3306/bd_proyecto_final_test?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8"
})
@AutoConfigureMockMvc
@Transactional
public abstract class ApiTestBase {

    // Cuentas del seed_demo.sql
    protected static final String POSTULANTE = "demo_postulante";   // employee 296
    protected static final String FREELANCER = "demo_freelancer";   // employee 316, perfil 762
    protected static final String EMPRESA = "demo_empresa";         // empresa 148
    protected static final String RECRUITER = "demo_recruiter";     // recruiter 148 (empresa 148)
    protected static final String CLIENTE = "demo_cliente";         // cliente 150
    protected static final String INSTRUCTOR = "ProfeBootcamp";     // instructor 1

    @Autowired protected MockMvc mvc;
    @Autowired protected JdbcTemplate jdbc;
    @Autowired private IJwtService jwtService;
    @Autowired private UserDetailsService userDetailsService;

    @MockitoBean protected MercadoPagoAdapter mercadoPagoAdapter;

    @BeforeEach
    void mockMercadoPago() {
        when(mercadoPagoAdapter.crearPreferencia(any(), any(), any(), any()))
                .thenReturn(new MercadoPagoPreferenceResponseDTO("pref-test", "https://mp.test/checkout", "https://sandbox.mp.test/checkout"));
    }

    protected String bearer(String usuario) {
        return "Bearer " + jwtService.generateToken(userDetailsService.loadUserByUsername(usuario));
    }

    protected ResultActions call(String usuario, AbstractMockHttpServletRequestBuilder<?> req) throws Exception {
        if (usuario != null) req.header("Authorization", bearer(usuario));
        return mvc.perform(req).andDo(r -> {
            if (r.getResponse().getStatus() >= 400)
                System.out.println("[API " + r.getResponse().getStatus() + "] " + r.getRequest().getMethod() + " "
                        + r.getRequest().getRequestURI() + " -> " + r.getResponse().getContentAsString());
        });
    }

    protected ResultActions get(String usuario, String url, Object... vars) throws Exception {
        return call(usuario, org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get(url, vars));
    }

    protected ResultActions delete(String usuario, String url, Object... vars) throws Exception {
        return call(usuario, org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete(url, vars));
    }

    protected ResultActions postJson(String usuario, String url, String json, Object... vars) throws Exception {
        return call(usuario, post(url, vars).contentType(MediaType.APPLICATION_JSON).content(json));
    }

    protected ResultActions putJson(String usuario, String url, String json, Object... vars) throws Exception {
        return call(usuario, put(url, vars).contentType(MediaType.APPLICATION_JSON).content(json));
    }

    protected ResultActions upload(String usuario, String url, MockMultipartFile file) throws Exception {
        return call(usuario, multipart(url).file(file));
    }

    protected static MockMultipartFile png(String campo) {
        // PNG 1x1 válido
        byte[] bytes = java.util.Base64.getDecoder().decode(
                "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNk+M9QDwADhgGAWjR9awAAAABJRU5ErkJggg==");
        return new MockMultipartFile(campo, "img.png", "image/png", bytes);
    }

    protected static MockMultipartFile pdf(String campo) {
        return new MockMultipartFile(campo, "cv.pdf", "application/pdf", "%PDF-1.4\n%test\n".getBytes());
    }

    protected Integer id(String sql, Object... args) {
        return jdbc.queryForObject(sql, Integer.class, args);
    }
}
