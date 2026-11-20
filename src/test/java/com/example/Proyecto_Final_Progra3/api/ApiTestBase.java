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
    protected static final String POSTULANTE = "demo_postulante";
    protected static final String FREELANCER = "demo_freelancer";
    protected static final String EMPRESA = "demo_empresa";
    protected static final String RECRUITER = "demo_recruiter";     // recruiter de demo_empresa
    protected static final String CLIENTE = "demo_cliente";
    protected static final String INSTRUCTOR = "ProfeBootcamp";

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

    @Autowired private jakarta.persistence.EntityManager em;

    /** El test comparte transacción con la app: al mezclar SQL directo y JPA hay que sincronizar Hibernate. */
    protected void sincronizar() {
        em.flush();
        em.clear();
    }

    protected Integer id(String sql, Object... args) {
        return jdbc.queryForObject(sql, Integer.class, args);
    }

    // ---------- IDs del escenario demo ----------
    // Se buscan por usuario y por estado en cada test: no dependen de los IDs de una base en particular.
    protected int postulanteEmp, freelancerEmp, freelancerPerfil, clienteId, clientePerfil,
            empresaId, empresaPerfil, recruiterId, instructorId;
    /** Proyecto de demo_cliente BUSCANDO_TALENTO con propuesta de demo_freelancer, y otro BUSCANDO_TALENTO distinto. */
    protected int proyBuscando, proyBuscandoOtro;
    /** Proyecto EN_PROGRESO de demo_cliente con demo_freelancer y todos los hitos PENDIENTE, sin pagos. */
    protected int proyEnCurso, hitoPrimero, hitoUltimo;
    /** Proyecto EN_PROGRESO de demo_cliente con demo_freelancer que ya tiene hitos pagados. */
    protected int proyConPagos, hitoPagado;
    /** Proyecto FINALIZADO de demo_cliente con demo_freelancer y calificaciones. */
    protected int proyFinalizado;
    /** Vacante vigente de demo_empresa con una postulación de demo_postulante sin entrevistas ni rechazo. */
    protected int vacante, postulacion;

    private int usuario(String sql, String usuario) {
        return id(sql + " join perfil p on p.perfil_id=x.perfil_id where p.usuario=?", usuario);
    }

    @BeforeEach
    void cargarIdsDemo() {
        postulanteEmp = usuario("select x.employee_id from employee x", POSTULANTE);
        freelancerEmp = usuario("select x.employee_id from employee x", FREELANCER);
        freelancerPerfil = usuario("select x.perfil_id from employee x", FREELANCER);
        clienteId = usuario("select x.cliente_particular_id from cliente_particular x", CLIENTE);
        clientePerfil = usuario("select x.perfil_id from cliente_particular x", CLIENTE);
        empresaId = usuario("select x.empresa_id from empresa x", EMPRESA);
        empresaPerfil = usuario("select x.perfil_id from empresa x", EMPRESA);
        recruiterId = usuario("select x.recruiter_id from recruiter x", RECRUITER);
        instructorId = usuario("select x.instructor_id from instructor x", INSTRUCTOR);

        proyBuscando = id("""
                select min(p.proyecto_id) from proyecto p where p.cliente_id=? and p.estado='BUSCANDO_TALENTO'
                and exists (select 1 from postulacion_proyecto pp where pp.proyecto_id=p.proyecto_id and pp.employee_id=?)""",
                clienteId, freelancerEmp);
        proyBuscandoOtro = id("select max(proyecto_id) from proyecto where cliente_id=? and estado='BUSCANDO_TALENTO' and proyecto_id<>?",
                clienteId, proyBuscando);
        proyEnCurso = id("""
                select min(p.proyecto_id) from proyecto p where p.cliente_id=? and p.freelancer_asignado_id=? and p.estado='EN_PROGRESO'
                and exists (select 1 from hito_proyecto h where h.proyecto_id=p.proyecto_id)
                and not exists (select 1 from hito_proyecto h where h.proyecto_id=p.proyecto_id and h.estado<>'PENDIENTE')
                and not exists (select 1 from pagos pg where pg.proyecto_id=p.proyecto_id)""", clienteId, freelancerEmp);
        hitoPrimero = id("select min(hito_id) from hito_proyecto where proyecto_id=?", proyEnCurso);
        hitoUltimo = id("select max(hito_id) from hito_proyecto where proyecto_id=?", proyEnCurso);
        proyConPagos = id("""
                select min(p.proyecto_id) from proyecto p where p.cliente_id=? and p.freelancer_asignado_id=? and p.estado='EN_PROGRESO'
                and exists (select 1 from pagos pg where pg.proyecto_id=p.proyecto_id and pg.hito_id is not null)""", clienteId, freelancerEmp);
        hitoPagado = id("select min(hito_id) from pagos where proyecto_id=? and hito_id is not null", proyConPagos);
        proyFinalizado = id("""
                select min(p.proyecto_id) from proyecto p where p.cliente_id=? and p.freelancer_asignado_id=? and p.estado='FINALIZADO'
                and exists (select 1 from calificacion_proyecto c where c.proyecto_id=p.proyecto_id)""", clienteId, freelancerEmp);

        postulacion = id("""
                select min(pv.postulacion_id) from postulacion_vacante pv join vacante v on v.vacante_id=pv.vacante_id
                where v.empresa_id=? and v.activa=1 and v.fecha_fin>curdate() and pv.postulante_id=?
                and pv.estado_postulacion<>'RECHAZADO'
                and not exists (select 1 from entrevista e where e.postulacion_id=pv.postulacion_id)""", empresaId, postulanteEmp);
        vacante = id("select vacante_id from postulacion_vacante where postulacion_id=?", postulacion);
    }

    // ---------- Bootcamps: el seed no trae ninguno, cada test crea el suyo ----------
    protected String bootcampJson(String titulo) {
        return """
                {"titulo":"%s","descripcion":"Bootcamp de prueba","inicio":"%s","fin":"%s",
                 "modalidad":"VIRTUAL","tipo":"PARTICULAR","cupos":20,"instructor_id":%d,"empresa_id":%d,
                 "precio":"50000","etiquetas":["java","spring"]}"""
                .formatted(titulo, java.time.LocalDate.now().plusDays(10), java.time.LocalDate.now().plusDays(60), instructorId, empresaId);
    }

    protected Integer crearBootcamp() throws Exception {
        postJson(INSTRUCTOR, "/api/v1/bootcamps/me", bootcampJson("Bootcamp Test"))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isCreated());
        return id("select max(bootcamp_id) from bootcamp where instructor_id=?", instructorId);
    }

    /** Inscribe a demo_postulante en el bootcamp y devuelve el id de la inscripción. */
    protected Integer inscribirPostulante(Integer bootcampId) throws Exception {
        call(POSTULANTE, post("/api/v1/inscripciones/me/{b}", bootcampId))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isCreated());
        return id("select max(inscripcion_id) from inscripcion_bootcamp where bootcamp_id=?", bootcampId);
    }
}
