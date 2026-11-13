package com.example.Proyecto_Final_Progra3.api;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Verifica el CONTENIDO de las respuestas GET: cada valor se compara contra lo que hay en MySQL,
 * no solo que el campo exista. Si un dashboard cuenta mal o un mapper pierde un campo, falla.
 */
class ContenidoApiTest extends ApiTestBase {

    private int count(String sql, Object... args) { return jdbc.queryForObject(sql, Integer.class, args); }
    private double sum(String sql, Object... args) {
        BigDecimal v = jdbc.queryForObject(sql, BigDecimal.class, args);
        return v == null ? 0 : v.doubleValue();
    }
    private String str(String sql, Object... args) { return jdbc.queryForObject(sql, String.class, args); }

    // ---------- Catálogos públicos ----------
    @Test void habilidades() throws Exception {
        get(null, "/api/v1/habilidades").andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(count("select count(*) from habilidad")))
                .andExpect(jsonPath("$[0].id").isNumber())
                .andExpect(jsonPath("$[0].name").isNotEmpty());
    }

    @Test void vacantes_listaSoloActivas() throws Exception {
        get(null, "/api/v1/vacantes").andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(count("select count(*) from vacante where activa=1")))
                .andExpect(jsonPath("$[*].vacanteId", everyItem(notNullValue())))
                .andExpect(jsonPath("$[*].nombreEmpresa", everyItem(not(emptyOrNullString()))))
                .andExpect(jsonPath("$[*].etiquetas", everyItem(notNullValue())));
    }

    @Test void vacante_detalleCoincideConBase() throws Exception {
        get(null, "/api/v1/vacantes/461").andExpect(status().isOk())
                .andExpect(jsonPath("$.vacanteId").value(461))
                .andExpect(jsonPath("$.puesto").value(str("select puesto from vacante where vacante_id=461")))
                .andExpect(jsonPath("$.uuid").value(str("select uuid from vacante where vacante_id=461")))
                .andExpect(jsonPath("$.empresaId").value(148))
                .andExpect(jsonPath("$.nombreEmpresa").value(str("select nombre from empresa where empresa_id=148")))
                .andExpect(jsonPath("$.finalizacion").value(str("select fecha_fin from vacante where vacante_id=461")))
                .andExpect(jsonPath("$.etiquetas.length()").value(count("select count(*) from vacante_etiquetas where vacante_id=461")));
    }

    @Test void vacante_porUuidEsLaMisma() throws Exception {
        get(null, "/api/v1/vacantes/uuid/{u}", str("select uuid from vacante where vacante_id=461"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.vacanteId").value(461));
    }

    @Test void proyectos_catalogo() throws Exception {
        get(null, "/api/v1/freelance/proyectos").andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(count("select count(*) from proyecto")))
                .andExpect(jsonPath("$[*].estado", everyItem(notNullValue())))
                .andExpect(jsonPath("$[*].nombreCliente", everyItem(not(emptyOrNullString()))));
    }

    @Test void proyecto_detalleCoincideConBase() throws Exception {
        get(null, "/api/v1/freelance/proyectos/392").andExpect(status().isOk())
                .andExpect(jsonPath("$.proyectoId").value(392))
                .andExpect(jsonPath("$.nombreProyecto").value(str("select nombre_proyecto from proyecto where proyecto_id=392")))
                .andExpect(jsonPath("$.area").value(str("select area_proyecto from proyecto where proyecto_id=392")))
                .andExpect(jsonPath("$.presupuestoMax").value(sum("select presupuesto_max from proyecto where proyecto_id=392")))
                .andExpect(jsonPath("$.presupuestoMin").value(sum("select presupuesto_min from proyecto where proyecto_id=392")))
                .andExpect(jsonPath("$.estado").value("EN_PROGRESO"))
                .andExpect(jsonPath("$.clienteId").value(150))
                .andExpect(jsonPath("$.nombreFreelancer").isNotEmpty())
                .andExpect(jsonPath("$.habilidades.length()").value(count("select count(*) from proyecto_habilidades where proyecto_id=392")));
    }

    @Test void proyecto_porUuidEsElMismo() throws Exception {
        get(null, "/api/v1/freelance/proyectos/uuid/{u}", str("select uuid from proyecto where proyecto_id=392"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.proyectoId").value(392));
    }

    @Test void bootcamp_detalle() throws Exception {
        get(null, "/api/v1/bootcamps/1").andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value(str("select titulo from bootcamp where bootcamp_id=1")))
                .andExpect(jsonPath("$.instructor_id").value(1))
                .andExpect(jsonPath("$.cupos").value(count("select cupos from bootcamp where bootcamp_id=1")))
                .andExpect(jsonPath("$.modalidad", in(new String[]{"VIRTUAL", "PRESENCIAL"})));
    }

    @Test void bootcamps_lista() throws Exception {
        get(null, "/api/v1/bootcamps").andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(count("select count(*) from bootcamp")));
    }

    @Test void bootcamps_porModalidadFiltra() throws Exception {
        get(null, "/api/v1/bootcamps/modalidad/VIRTUAL").andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(count("select count(*) from bootcamp where modalidad='VIRTUAL'")))
                .andExpect(jsonPath("$[*].modalidad", everyItem(is("VIRTUAL"))));
    }

    @Test void bootcamps_disponibles_tienenCuposYNoEmpezaron() throws Exception {
        get(null, "/api/v1/bootcamps/disponibles").andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(count("select count(*) from bootcamp where cupos>0 and fecha_inicio>=curdate()")));
    }

    // ---------- Perfil ----------
    @Test void perfil_freelancer() throws Exception {
        get(FREELANCER, "/api/v1/perfiles/getProfile").andExpect(status().isOk())
                .andExpect(jsonPath("$.rol").value("FREELANCER"))
                .andExpect(jsonPath("$.email").value(str("select email from perfil where usuario=?", FREELANCER)))
                .andExpect(jsonPath("$.nombre").value(str("select nombre from employee where employee_id=316")))
                .andExpect(jsonPath("$.habilidades.length()").value(count("select count(*) from employee_habilidad where employee_id=316")))
                .andExpect(jsonPath("$.tieneCv").isBoolean());
    }

    @Test void perfil_cliente() throws Exception {
        get(CLIENTE, "/api/v1/perfiles/getProfile").andExpect(status().isOk())
                .andExpect(jsonPath("$.rol").value("CLIENTE"))
                .andExpect(jsonPath("$.nombre").value(str("select nombre from cliente_particular where cliente_particular_id=150")));
    }

    @Test void perfil_rolCoincideParaTodos() throws Exception {
        String[][] casos = {{POSTULANTE, "POSTULANTE"}, {FREELANCER, "FREELANCER"}, {EMPRESA, "EMPRESA"},
                {RECRUITER, "RECRUITER"}, {CLIENTE, "CLIENTE"}, {INSTRUCTOR, "INSTRUCTOR"}};
        for (String[] c : casos)
            get(c[0], "/api/v1/perfiles/getProfile").andExpect(jsonPath("$.rol").value(c[1]))
                    .andExpect(jsonPath("$.email").isNotEmpty());
    }

    @Test void reputacion_publica() throws Exception {
        get(null, "/api/v1/perfiles/762/calificaciones").andExpect(status().isOk())
                .andExpect(jsonPath("$.perfilId").value(762))
                .andExpect(jsonPath("$.totalResenas").value(count("select count(*) from calificacion_usuario where perfil_id=762")))
                .andExpect(jsonPath("$.resenas.length()").value(count("select count(*) from calificacion_usuario where perfil_id=762")))
                .andExpect(jsonPath("$.promedio", allOf(greaterThanOrEqualTo(0.0), lessThanOrEqualTo(5.0))));
    }

    @Test void miReputacion_igualALaPublica() throws Exception {
        int total = count("select count(*) from calificacion_usuario where perfil_id=762");
        for (String url : new String[]{"/api/v1/perfiles/me/calificaciones", "/api/v1/freelancer/opiniones"})
            get(FREELANCER, url).andExpect(jsonPath("$.perfilId").value(762)).andExpect(jsonPath("$.totalResenas").value(total));
    }

    // ---------- Empresa ----------
    @Test void empresa_detalle() throws Exception {
        get(EMPRESA, "/api/v1/empresas/148").andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(148))
                .andExpect(jsonPath("$.nombre").value(str("select nombre from empresa where empresa_id=148")))
                .andExpect(jsonPath("$.descripcion").value(str("select descripcion from empresa where empresa_id=148")))
                .andExpect(jsonPath("$.sitio_web").value(str("select sitio_web from empresa where empresa_id=148")))
                .andExpect(jsonPath("$.ubicacion").value(str("select ubicacion from empresa where empresa_id=148")))
                .andExpect(jsonPath("$.perfil_id").value(802));
    }

    @Test void empresa_porUuidEsLaMisma() throws Exception {
        get(EMPRESA, "/api/v1/empresas/uuid/{u}", str("select uuid from empresa where empresa_id=148"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(148));
    }

    @Test void empresa_dashboardCoincideConBase() throws Exception {
        get(EMPRESA, "/api/v1/empresas/dashboard").andExpect(status().isOk())
                .andExpect(jsonPath("$.totalVacantes").value(count("select count(*) from vacante where empresa_id=148")))
                .andExpect(jsonPath("$.totalPostulaciones").value(count(
                        "select count(*) from postulacion_vacante pv join vacante v on v.vacante_id=pv.vacante_id where v.empresa_id=148")))
                .andExpect(jsonPath("$.entrevistasPendientes").isNumber())
                .andExpect(jsonPath("$.contratacionesEfectuadas").isNumber());
    }

    @Test void empresa_recruiters() throws Exception {
        get(EMPRESA, "/api/v1/empresas/recruiters").andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(count("select count(*) from recruiter where empresa_id=148")))
                .andExpect(jsonPath("$[*].empresaId", everyItem(is(148))))
                .andExpect(jsonPath("$[*].email", everyItem(not(emptyOrNullString()))));
    }

    @Test void empresa_recruiterPorId() throws Exception {
        get(EMPRESA, "/api/v1/empresas/recruiters/148").andExpect(status().isOk())
                .andExpect(jsonPath("$.recruiterId").value(148)).andExpect(jsonPath("$.empresaId").value(148));
    }

    @Test void empresa_misVacantes() throws Exception {
        get(EMPRESA, "/api/v1/vacantes/me").andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(count("select count(*) from vacante where empresa_id=148")))
                .andExpect(jsonPath("$[*].empresaId", everyItem(is(148))));
    }

    @Test void empresa_postulacionesDeTodasSusVacantes() throws Exception {
        get(EMPRESA, "/api/v1/vacantes/postulaciones").andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(count(
                        "select count(*) from postulacion_vacante pv join vacante v on v.vacante_id=pv.vacante_id where v.empresa_id=148")))
                .andExpect(jsonPath("$[*].nombreCandidato", everyItem(not(emptyOrNullString()))))
                .andExpect(jsonPath("$[*].tieneCvSubido", everyItem(isA(Boolean.class))));
    }

    @Test void empresa_postulacionesFiltradasPorVacante() throws Exception {
        get(EMPRESA, "/api/v1/vacantes/postulaciones?vacanteId=461").andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(count("select count(*) from postulacion_vacante where vacante_id=461")))
                .andExpect(jsonPath("$[*].vacanteId", everyItem(is(461))));
    }

    @Test void empresa_postulantesDeUnaVacante() throws Exception {
        get(EMPRESA, "/api/v1/vacantes/461/postulaciones").andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(count("select count(*) from postulacion_vacante where vacante_id=461")))
                .andExpect(jsonPath("$[*].puestoVacante", everyItem(is(str("select puesto from vacante where vacante_id=461")))));
    }

    @Test void entrevistas_delPostulanteSonSuyas() throws Exception {
        get(POSTULANTE, "/api/v1/recruiter/entrevistas").andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(count(
                        "select count(*) from entrevista e join postulacion_vacante pv on pv.postulacion_id=e.postulacion_id where pv.postulante_id=296")))
                .andExpect(jsonPath("$[*].date", everyItem(notNullValue())))
                .andExpect(jsonPath("$[*].vacancyTitle", everyItem(not(emptyOrNullString()))));
    }

    @Test void entrevistas_deLaEmpresaDelRecruiter() throws Exception {
        get(RECRUITER, "/api/v1/recruiter/entrevistas").andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(count(
                        "select count(*) from entrevista e join postulacion_vacante pv on pv.postulacion_id=e.postulacion_id "
                                + "join vacante v on v.vacante_id=pv.vacante_id where v.empresa_id=148")));
    }

    @Test void entrevistas_traenEmpresaYEstado() throws Exception {
        String empresa = str("select nombre from empresa where empresa_id=148");
        get(RECRUITER, "/api/v1/recruiter/entrevistas").andExpect(status().isOk())
                .andExpect(jsonPath("$[*].companyName", everyItem(is(empresa))))
                .andExpect(jsonPath("$[*].status", everyItem(in(new String[]{"PROGRAMADA", "COMPLETADA"}))))
                .andExpect(jsonPath("$[*].postulacionId", everyItem(notNullValue())))
                .andExpect(jsonPath("$[*].candidateEmail", everyItem(not(emptyOrNullString()))));
    }

    @Test void entrevistas_estadoSegunFecha() throws Exception {
        // Una futura queda PROGRAMADA; si se mueve al pasado pasa a COMPLETADA
        postJson(RECRUITER, "/api/v1/recruiter/entrevistas", """
                {"postulacionId":758,"fecha":"%s","hora":"10:00:00","link":"https://meet.test/x"}"""
                .formatted(java.time.LocalDate.now().plusDays(3)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("PROGRAMADA"))
                .andExpect(jsonPath("$.companyName").value(str("select nombre from empresa where empresa_id=148")));
        Integer id = jdbc.queryForObject("select max(entrevista_id) from entrevista", Integer.class);
        jdbc.update("update entrevista set fecha=? where entrevista_id=?", java.time.LocalDate.now().minusDays(1), id);
        get(RECRUITER, "/api/v1/recruiter/entrevistas").andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == " + id + ")].status", hasItem("COMPLETADA")));
    }

    // ---------- Cliente ----------
    @Test void cliente_dashboardCoincideConBase() throws Exception {
        get(CLIENTE, "/api/v1/client/dashboard").andExpect(status().isOk())
                .andExpect(jsonPath("$.completedProjects").value(count("select count(*) from proyecto where cliente_id=150 and estado='FINALIZADO'")))
                .andExpect(jsonPath("$.totalSpent").value(sum(
                        "select sum(monto) from pagos where proyecto_id in (select proyecto_id from proyecto where cliente_id=150)")))
                .andExpect(jsonPath("$.activeProjects").isNumber())
                .andExpect(jsonPath("$.pendingProposals").value(count(
                        "select count(*) from postulacion_proyecto pp join proyecto p on p.proyecto_id=pp.proyecto_id "
                                + "where p.cliente_id=150 and pp.estado_postulacion='PENDIENTE'")));
    }

    @Test void cliente_me() throws Exception {
        get(CLIENTE, "/api/v1/client/me").andExpect(status().isOk())
                .andExpect(jsonPath("$.clienteId").value(150))
                .andExpect(jsonPath("$.username").value(CLIENTE))
                .andExpect(jsonPath("$.firstName").value(str("select nombre from cliente_particular where cliente_particular_id=150")))
                .andExpect(jsonPath("$.perfilId").value(782));
    }

    @Test void cliente_proyectosSonSuyos() throws Exception {
        int total = count("select count(*) from proyecto where cliente_id=150");
        for (String url : new String[]{"/api/v1/client/proyectos", "/api/v1/freelance/proyectos/me/publicados"})
            get(CLIENTE, url).andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(total))
                    .andExpect(jsonPath("$[*].clienteId", everyItem(is(150))));
    }

    @Test void propuestasDeUnProyecto() throws Exception {
        get(CLIENTE, "/api/v1/freelance/proyectos/382/postulaciones").andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(count("select count(*) from postulacion_proyecto where proyecto_id=382")))
                .andExpect(jsonPath("$[*].proyectoId", everyItem(is(382))))
                .andExpect(jsonPath("$[*].costoPropuesto", everyItem(greaterThan(0.0))))
                .andExpect(jsonPath("$[*].nombreEmpleado", everyItem(not(emptyOrNullString()))));
    }

    @Test void calificacionesDeUnProyecto() throws Exception {
        get(CLIENTE, "/api/v1/freelance/proyectos/400/calificaciones").andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(count("select count(*) from calificacion_proyecto where proyecto_id=400")))
                .andExpect(jsonPath("$[*].puntaje", everyItem(allOf(greaterThanOrEqualTo(1), lessThanOrEqualTo(5)))));
    }

    // ---------- Hitos y pagos ----------
    @Test void hitos_enOrdenYConMontos() throws Exception {
        get(CLIENTE, "/api/v1/freelance/proyectos/392/hitos").andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(count("select count(*) from hito_proyecto where proyecto_id=392")))
                .andExpect(jsonPath("$[0].hitoId").value(548))
                .andExpect(jsonPath("$[*].proyectoId", everyItem(is(392))))
                .andExpect(jsonPath("$[*].monto", everyItem(greaterThan(0.0))))
                .andExpect(jsonPath("$[*].estado", everyItem(in(new String[]{"PENDIENTE", "EN_PROGRESO", "COMPLETADO"}))));
    }

    @Test void pagos_igualesParaClienteYFreelancer() throws Exception {
        int total = count("select count(*) from pagos where proyecto_id=394");
        for (String u : new String[]{CLIENTE, FREELANCER})
            get(u, "/api/v1/freelance/proyectos/394/pagos").andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(total))
                    .andExpect(jsonPath("$[*].moneda", everyItem(is("PESOS"))))
                    .andExpect(jsonPath("$[*].fecha", everyItem(notNullValue())))
                    .andExpect(jsonPath("$[*].proyectoId", everyItem(is(394))));
    }

    // ---------- Freelancer ----------
    @Test void freelancer_finanzasCoincidenConBase() throws Exception {
        get(FREELANCER, "/api/v1/freelancer/finances").andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(sum(
                        "select sum(monto) from pagos where proyecto_id in (select proyecto_id from proyecto where freelancer_asignado_id=316)")))
                .andExpect(jsonPath("$.pending").value(sum(
                        "select sum(presupuesto_max) from proyecto where freelancer_asignado_id=316 and estado='EN_PROGRESO'")));
    }

    @Test void freelancer_transaccionesCoincidenConPagos() throws Exception {
        get(FREELANCER, "/api/v1/freelancer/transactions").andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(count(
                        "select count(*) from pagos where proyecto_id in (select proyecto_id from proyecto where freelancer_asignado_id=316)")))
                .andExpect(jsonPath("$[*].currency", everyItem(is("PESOS"))))
                .andExpect(jsonPath("$[*].amount", everyItem(greaterThan(0.0))));
    }

    @Test void freelancer_proyectosAsignadosSonSuyos() throws Exception {
        int total = count("select count(*) from proyecto where freelancer_asignado_id=316");
        for (String url : new String[]{"/api/v1/freelancer/proyectos", "/api/v1/freelance/proyectos/me/asignados"})
            get(FREELANCER, url).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(total));
    }

    @Test void freelancer_medioCobro() throws Exception {
        get(FREELANCER, "/api/v1/freelancer/medios-cobro").andExpect(status().isOk())
                .andExpect(jsonPath("$.titular").value(str("select titular from freelancer_medios_cobro where freelancer_id=762")))
                .andExpect(jsonPath("$.tipo", in(new String[]{"TRANSFERENCIA", "MERCADOPAGO"})));
    }

    // ---------- Bootcamps (instructor / postulante) ----------
    @Test void instructores_lista() throws Exception {
        get(INSTRUCTOR, "/api/v1/instructores").andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(count("select count(*) from instructor")));
    }

    @Test void misBootcamps_delInstructor() throws Exception {
        get(INSTRUCTOR, "/api/v1/bootcamps/me").andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(count("select count(*) from bootcamp where instructor_id=1")))
                .andExpect(jsonPath("$[*].instructor_id", everyItem(is(1))));
    }

    @Test void misInscripciones() throws Exception {
        get(POSTULANTE, "/api/v1/inscripciones/me").andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(count("select count(*) from inscripcion_bootcamp where employee_id=296")));
    }
}
