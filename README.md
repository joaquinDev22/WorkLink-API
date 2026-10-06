# WorkLink API - Plataforma Integral de Servicios Tecnológicos

Plataforma integral orientada al ecosistema tecnológico, diseñada para centralizar y optimizar la vinculación laboral, la contratación freelance y la formación continua mediante bootcamps.

[![Java 21](https://img.shields.io/badge/Java-21%20LTS-orange.svg?style=flat&logo=openjdk)](https://www.oracle.com/java/)
[![Spring Boot 3](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg?style=flat&logo=springboot)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/Spring%20Security-Stateless%20JWT-6DB33F.svg?style=flat&logo=springsecurity)](https://spring.io/projects/spring-security)
[![MySQL](https://img.shields.io/badge/MySQL-8.0%2B-blue.svg?style=flat&logo=mysql)](https://www.mysql.com/)
[![OpenAPI 3](https://img.shields.io/badge/OpenAPI-Swagger%203-85EA2D.svg?style=flat&logo=swagger)](https://swagger.io/)
[![Mercado Pago](https://img.shields.io/badge/Mercado%20Pago-Checkout%20Pro-009EE3.svg?style=flat&logo=mercadopago)](https://www.mercadopago.com.ar/developers)
[![Google OAuth2](https://img.shields.io/badge/Google-OAuth2%20Identity-4285F4.svg?style=flat&logo=google)](https://developers.google.com/identity)

---

## Descripción General

WorkLink es una solución integral desarrollada para dar respuesta a la fragmentación informativa en el mercado de contratación tecnológica y capacitación profesional. La plataforma unifica en un único ecosistema las dinámicas de empleo formal en relación de dependencia, la contratación ágil de servicios freelance y los programas de formación especializada.

### Objetivos y Propuesta de Valor
* **Centralización de Oportunidades:** Eliminación del ruido operativo de las redes sociales generalistas, concentrándose en perfiles técnicos y proyectos verificables.
* **Trazabilidad de Contratación:** Gestión integral del ciclo de selección y contratación, desde la publicación de requerimientos hasta la coordinación de entrevistas y liquidación de servicios.
* **Validación de Habilidades:** Espacio para la exhibición de portafolios verificables, proyectos personales y certificaciones de formación continua.

---

## Arquitectura y Principios de Diseño

El sistema está construido como un **Monolito Modular (Modular Monolith)**, priorizando la cohesión interna por dominio de negocio y manteniendo bajo acoplamiento entre módulos:

* **Concurrencia de Próxima Generación:** Soporte nativo de **Virtual Threads (Project Loom)** sobre Java 21, optimizando el rendimiento de operaciones de entrada/salida (I/O) sin sobrecargar los recursos del sistema operativo.
* **Seguridad Stateless y Federación de Identidad:**
  * Autenticación local mediante tokens web JSON (JJWT) con cifrado simétrico robusto.
  * Autenticación federada mediante **Google OAuth2 / OpenID Connect**, con validación criptográfica en el servidor del token de identidad (`idToken`).
  * Control de acceso basado en roles (RBAC) granular sobre cada recurso.
* **Patrón Adapter para Integraciones de Terceros:**
  * **Mercado Pago Adapter:** Desacoplamiento de la lógica de pagos y webhook handling del ciclo de vida transaccional de base de datos, resguardando la estabilidad del pool de conexiones HikariCP.
  * **Google Identity Adapter:** Aislamiento de la librería de verificación externa respecto de la capa de servicios de dominio.
* **Diseño Orientado al Dominio y Capas Limpias:**
  * Separación estricta de responsabilidades: Controladores REST, Servicios de Negocio, Repositorios de Persistencia y Modelos de Dominio.
  * Uso de **Data Transfer Objects (DTOs)** inmutables implementados con Java Records para proteger la exposición directa del modelo de datos relacional.
  * Mapeo de datos en tiempo de compilación con **MapStruct** para un rendimiento óptimo sin sobrecarga de reflexión.
* **Persistencia y Optimización de Consultas:** Spring Data JPA con Hibernate sobre motor relacional MySQL 8, utilizando políticas de sólo lectura explícitas (`@Transactional(readOnly = true)`) y procesamiento en lotes (*batch fetching*).

---

## Módulos y Dominio Funcional

### 1. Gestión de Identidad, Perfiles y Portafolio
* Control de acceso seguro, registro federado y nativo.
* Gestión de perfiles profesionales diferenciados (Empresas, Recruiters, Postulantes, Freelancers, Clientes).
* Repositorio de recursos multimedia (CV en formato digital, imagen de perfil y portada).
* Registro de habilidades técnicas y proyectos de portafolio personal.
* Mecanismo de baja lógica controlada para preservación de integridad referencial.

### 2. Ecosistema Corporativo y Adquisición de Talento
* Perfiles corporativos y paneles estadísticos de actividad.
* Delegación y gestión de múltiples reclutadores por empresa.
* Ciclo de vida completo de ofertas laborales (creación, edición, publicación y cierre).
* Recepción de postulaciones y agendamiento formal de entrevistas laborales con candidatos.

### 3. Mercado Freelance y Liquidación Transaccional
* Publicación, actualización de alcance y cancelación de proyectos freelance.
* Sistema de ofertas y evaluación de propuestas técnicas.
* Generación de preferencias de pago con Mercado Pago Checkout Pro.
* Notificación asíncrona mediante Webhooks de pagos con confirmación y auditoría transaccional.
* Paneles analíticos con cálculo de balance, gastos y proyectos activos mediante agregaciones directas en base de datos.

### 4. Formación y Bootcamps
* Catálogo de programas educativos especializados.
* Administración de programas por instructores técnicos y seguimiento de inscripciones.

---

## Contrato de Interfaces y Catálogo de la API

La API REST se expone bajo el estándar OpenAPI 3 / Swagger, versionada bajo el prefijo `/api/v1`.

### Autenticación y Seguridad
* `POST /api/v1/auth/register` - Registro de nuevos usuarios y asignación de perfil base.
* `POST /api/v1/auth/login` - Inicio de sesión tradicional y emisión de token JWT.
* `POST /api/v1/auth/google` - Verificación de token federado Google y emisión de token de sesión.

### Perfiles y Portafolio
* `GET /api/v1/perfiles/getProfile` - Obtención del perfil autenticado.
* `PUT /api/v1/perfiles/updateProfile` - Modificación de datos personales y profesionales.
* `DELETE /api/v1/perfiles/me` - Baja lógica del perfil de usuario en la plataforma.
* `POST /api/v1/perfiles/uploadMyCV` - Almacenamiento de currículum profesional.
* `POST /api/v1/perfiles/uploadMyPhoto` - Carga de fotografía de perfil.
* `POST /api/v1/perfiles/uploadMyBanner` - Carga de imagen de encabezado.
* `POST /api/v1/perfiles/addMySkill` - Vinculación de habilidad técnica al perfil.
* `DELETE /api/v1/perfiles/removeMySkill/{id}` - Desvinculación de habilidad técnica.
* `POST /api/v1/perfiles/addMyProject` - Incorporación de proyecto al portafolio.
* `DELETE /api/v1/perfiles/removeMyProject/{id}` - Eliminación de proyecto del portafolio.

### Empresas y Reclutamiento
* `POST /api/v1/empresas` - Alta de perfil de empresa.
* `PUT /api/v1/empresas/{id}` - Modificación de información institucional.
* `GET /api/v1/empresas/{id}` - Consulta de datos de empresa.
* `GET /api/v1/empresas/dashboard` - Métricas de rendimiento corporativo.
* `POST /api/v1/empresas/recruiters` - Alta y vinculación de reclutador a la empresa.
* `GET /api/v1/empresas/recruiters` - Listado de reclutadores pertenecientes a la empresa.
* `GET /api/v1/empresas/recruiters/{id}` - Consulta de reclutador específico.
* `PUT /api/v1/empresas/recruiters/{id}` - Actualización de datos de reclutador.
* `DELETE /api/v1/empresas/recruiters/{id}` - Desvinculación y baja de reclutador.

### Ofertas Laborales y Entrevistas
* `GET /api/v1/vacantes` - Consulta pública del catálogo de vacantes.
* `GET /api/v1/vacantes/{id}` - Detalle de vacante laboral.
* `POST /api/v1/vacantes` - Publicación de nueva vacante laboral.
* `PUT /api/v1/vacantes/{id}` - Modificación de vacante activa.
* `DELETE /api/v1/vacantes/{id}` - Baja o cierre definitivo de vacante.
* `POST /api/v1/recruiter/entrevistas` - Agendamiento de entrevista con candidato postulado.

### Mercado Freelance y Pagos
* `GET /api/v1/freelance/proyectos` - Catálogo de proyectos freelance disponibles.
* `POST /api/v1/freelance/proyectos` - Publicación de nuevo requerimiento freelance.
* `PUT /api/v1/freelance/proyectos/{id}` - Modificación de requerimiento freelance en convocatoria.
* `POST /api/v1/freelance/proyectos/{id}/pagos/checkout` - Generación de preferencia de cobro en Mercado Pago.
* `POST /api/v1/pagos/webhook` - Receptor público de eventos de notificación transaccional.
* `GET /api/v1/freelance/client/dashboard` - Métricas consolidadas del panel de cliente.
* `GET /api/v1/freelance/finances` - Balance y estado de liquidaciones del profesional independiente.

### Educación y Bootcamps
* `GET /api/v1/bootcamps` - Consulta de oferta educativa y programas de formación.
* `POST /api/v1/bootcamps` - Alta y programación de nuevos bootcamps.
* `PUT /api/v1/bootcamps/{id}` - Actualización de contenidos y fechas de cursado.
* `DELETE /api/v1/bootcamps/{id}` - Cierre o baja de programa formativo.

---

## Contexto Institucional y Autores

Proyecto desarrollado como trabajo final integrador para la cátedra **Programación III** de la **Universidad Tecnológica Nacional (UTN)**.

### Equipo de Desarrollo
* **Nieva, Joaquín** ([@J0aking21](https://github.com/J0aking21) / [@joaquinDev22](https://github.com/joaquinDev22))
* **Ramírez, Gabriel** ([@Gabrielramirez2003](https://github.com/Gabrielramirez2003))
* **Safi, Yamil** ([@Yamils06](https://github.com/Yamils06))

---