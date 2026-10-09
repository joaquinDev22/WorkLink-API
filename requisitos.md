# Especificación de Requisitos de Software: WorkLink Platform

**Proyecto:** WorkLink Platform  
**Documento:** Catálogo de Requisitos Funcionales (RF) y Requisitos No Funcionales (RNF)  
**Materia:** Programación III - Universidad Tecnológica Nacional (UTN)  
**Ámbito de Aplicación:** Plataforma de Empleo Corporativo, Mercado Freelance, Identidad Profesional y Pagos  
**Nota de Alcance:** El módulo de Formación y Bootcamps ha sido excluido explícitamente del presente documento conforme a los requerimientos de la entrega.

---

## 1. Introducción y Definición del Alcance

El presente documento formaliza los requisitos funcionales y no funcionales que rigen el comportamiento técnico, operativo y de seguridad del sistema WorkLink. 

### 1.1 Objetivo del Sistema
Centralizar y optimizar la intermediación laboral tecnológica a través de dos ejes principales:
1. **Adquisición de Talento Corporativo (Enterprise):** Publicación y administración de ofertas laborales en relación de dependencia, gestión delegada de recruiters por parte de empresas y coordinación de entrevistas.
2. **Mercado de Servicios Freelance (Gig Economy):** Publicación de proyectos de desarrollo, recepción y selección de propuestas técnicas, liquidación transaccional con custodia de fondos mediante Mercado Pago y analítica financiera.

Ambos ejes se sustentan en una capa transversal de identidad profesional, autenticación segura (nativa y federada) y gestión de portafolio multimedia verificable.

---

## 2. Actores y Roles del Sistema

| Identificador de Rol | Nombre del Actor | Descripción |
| :--- | :--- | :--- |
| `ROLE_EMPRESA` | Empresa Corporativa | Organización empleadora que publica vacantes, administra a sus reclutadores asociados y consulta analíticas corporativas. |
| `ROLE_RECRUITER` | Reclutador | Profesional técnico vinculado a una empresa responsable de publicar vacantes, evaluar postulantes y agendar entrevistas. |
| `ROLE_EMPLOYEE` | Postulante / Empleado | Profesional en búsqueda activa de empleo formal que carga su CV, define habilidades y se postula a vacantes. |
| `ROLE_CLIENTE` | Cliente Particular | Contratante individual que publica proyectos freelance, selecciona propuestas y realiza los pagos de custodia. |
| `ROLE_FREELANCER` | Profesional Freelance | Trabajador independiente que envía propuestas a proyectos, entrega hitos de trabajo y administra su balance financiero. |
| `PUBLIC` | Usuario Anónimo | Visitante que consulta los catálogos públicos de ofertas laborales y proyectos sin requerir inicio de sesión. |

---

## 3. Requisitos Funcionales (RF)

### 3.1 Módulo de Autenticación y Control de Acceso

#### RF-01: Registro de Usuarios
* **Actores:** `PUBLIC`
* **Descripción:** El sistema debe permitir a nuevos usuarios registrarse en la plataforma ingresando nombre de usuario, correo electrónico, contraseña en texto plano (que debe ser cifrada previo almacenamiento) y el rol principal deseado (`EMPRESA`, `CLIENTE`, `FREELANCER`, `EMPLOYEE`).
* **Reglas de Negocio:**
  - El correo electrónico debe tener un formato sintáctico válido y ser único en el sistema.
  - La contraseña debe contener un mínimo de caracteres exigido por la política de seguridad.
  - Se genera automáticamente la entidad de perfil base asociada al usuario.
* **Endpoint API:** `POST /api/v1/auth/register`

#### RF-02: Inicio de Sesión Tradicional (JWT)
* **Actores:** `PUBLIC`
* **Descripción:** El sistema debe autenticar a un usuario registrado mediante sus credenciales (correo electrónico y contraseña), retornando un token de acceso JSON Web Token (JWT) firmado digitalmente que identifique al usuario y sus privilegios.
* **Reglas de Negocio:**
  - Si las credenciales no coinciden o el usuario no existe, se retorna código de error `401 Unauthorized`.
  - Si el usuario se encuentra inactivo (`is_active = false`), se debe denegar el acceso.
* **Endpoint API:** `POST /api/v1/auth/login`

#### RF-03: Inicio de Sesión Federado con Google OAuth2
* **Actores:** `PUBLIC`
* **Descripción:** El sistema debe permitir el inicio de sesión o registro automático utilizando credenciales de Google Identity Services mediante la recepción de un token criptográfico `idToken`.
* **Reglas de Negocio:**
  - El backend debe validar criptográficamente la firma del `idToken` y contrastar el `aud` (Audience) contra el Google Client ID configurado.
  - Si el correo electrónico extraído del token no existe en el sistema, se crea automáticamente un nuevo usuario y perfil con rol predeterminado.
  - Tras la validación, el backend emite un token JWT nativo de WorkLink para mantener la sesión uniforme.
* **Endpoint API:** `POST /api/v1/auth/google`

---

### 3.2 Módulo de Perfil, Habilidades y Portafolio Multimedia

#### RF-04: Consulta del Perfil Propio
* **Actores:** `ROLE_EMPRESA`, `ROLE_RECRUITER`, `ROLE_CLIENTE`, `ROLE_FREELANCER`, `ROLE_EMPLOYEE`
* **Descripción:** El sistema debe permitir al usuario autenticado consultar la totalidad de su información de perfil personal, información de contacto, biografía, enlaces a redes y estado de cuenta.
* **Endpoint API:** `GET /api/v1/perfiles/getProfile`

#### RF-05: Modificación de Datos del Perfil
* **Actores:** `ROLE_EMPRESA`, `ROLE_RECRUITER`, `ROLE_CLIENTE`, `ROLE_FREELANCER`, `ROLE_EMPLOYEE`
* **Descripción:** El sistema debe permitir al usuario en sesión actualizar su información biográfica, número telefónico, ubicación y preferencias de contacto.
* **Endpoint API:** `PUT /api/v1/perfiles/updateProfile`

#### RF-06: Desactivación Lógica del Perfil Propio (RF01 Original)
* **Actores:** `ROLE_EMPRESA`, `ROLE_RECRUITER`, `ROLE_CLIENTE`, `ROLE_FREELANCER`, `ROLE_EMPLOYEE`
* **Descripción:** El sistema debe permitir al usuario dar de baja su cuenta de manera lógica, cambiando la propiedad `is_active` a `false`.
* **Reglas de Negocio:**
  - No se realiza borrado físico en base de datos para preservar la integridad referencial histórica de proyectos, contratos y pagos.
  - Un usuario desactivado queda inhabilitado para autenticarse o interactuar en la plataforma.
* **Endpoint API:** `DELETE /api/v1/perfiles/me`

#### RF-07: Almacenamiento de Currículum Vitae (CV)
* **Actores:** `ROLE_EMPLOYEE`, `ROLE_FREELANCER`
* **Descripción:** El sistema debe permitir la subida de un archivo digital (formato PDF) correspondiente al CV del postulante.
* **Reglas de Negocio:**
  - Se debe validar el tipo MIME del archivo (`application/pdf`) y un tamaño máximo de carga.
  - El archivo se almacena en el registro del perfil del usuario para su posterior descarga por reclutadores.
* **Endpoint API:** `POST /api/v1/perfiles/uploadMyCV`

#### RF-08: Gestión de Recursos Gráficos del Perfil (Foto y Portada)
* **Actores:** `ROLE_EMPRESA`, `ROLE_RECRUITER`, `ROLE_CLIENTE`, `ROLE_FREELANCER`, `ROLE_EMPLOYEE`
* **Descripción:** El sistema debe permitir la carga independiente de la fotografía de perfil y de la imagen de portada/banner del usuario.
* **Reglas de Negocio:**
  - Validación de extensiones permitidas (JPEG, PNG, WEBP).
* **Endpoints API:** `POST /api/v1/perfiles/uploadMyPhoto`, `POST /api/v1/perfiles/uploadMyBanner`

#### RF-09: Gestión de Habilidades Técnicas (Skills)
* **Actores:** `ROLE_EMPLOYEE`, `ROLE_FREELANCER`
* **Descripción:** El sistema debe permitir al usuario incorporar habilidades técnicas a su perfil indicando denominación y nivel de dominio, así como removerlas.
* **Endpoints API:** `POST /api/v1/perfiles/addMySkill`, `DELETE /api/v1/perfiles/removeMySkill/{id}`

#### RF-10: Gestión de Proyectos de Portafolio Personal
* **Actores:** `ROLE_EMPLOYEE`, `ROLE_FREELANCER`
* **Descripción:** El sistema debe permitir al usuario publicar proyectos representativos en su vitrina personal con título, descripción y enlace externo al repositorio o demostración en vivo.
* **Endpoints API:** `POST /api/v1/perfiles/addMyProject`, `DELETE /api/v1/perfiles/removeMyProject/{id}`

---

### 3.3 Módulo de Empresas y Gestión de Recruiters

#### RF-11: Registro y Modificación de Empresa
* **Actores:** `ROLE_EMPRESA`
* **Descripción:** Permite dar de alta y actualizar los datos corporativos de la empresa empleadora (razón social, CUIT, rubro, tamaño de planta, sitio web y descripción institucional).
* **Endpoints API:** `POST /api/v1/empresas`, `PUT /api/v1/empresas/{id}`, `GET /api/v1/empresas/{id}`

#### RF-12: Alta de Recruiter Asociado a Empresa (RF22)
* **Actores:** `ROLE_EMPRESA`
* **Descripción:** Permite a una empresa autenticada dar de alta un reclutador subordinado, asignándole nombre, apellido, correo institucional, fecha de nacimiento y área técnica de cobertura.
* **Reglas de Negocio:**
  - El recruiter queda automáticamente vinculado a la empresa que ejecutó la solicitud.
  - El correo electrónico del recruiter no debe estar registrado previamente.
* **Endpoint API:** `POST /api/v1/empresas/recruiters`

#### RF-13: Consulta y Listado de Recruiters de la Empresa (RF23, RF24)
* **Actores:** `ROLE_EMPRESA`
* **Descripción:** Permite a la empresa obtener el listado completo de todos los reclutadores dependientes de su cuenta, así como consultar la ficha de detalle de un reclutador por su identificador único.
* **Reglas de Negocio:**
  - Una empresa únicamente puede acceder a los datos de sus propios reclutadores (aislamiento entre empresas).
* **Endpoints API:** `GET /api/v1/empresas/recruiters`, `GET /api/v1/empresas/recruiters/{id}`

#### RF-14: Modificación y Baja de Recruiter por Empresa
* **Actores:** `ROLE_EMPRESA`
* **Descripción:** Permite a la empresa editar los datos de un reclutador propio o darlo de baja definitiva desvinculándolo de la organización.
* **Endpoints API:** `PUT /api/v1/empresas/recruiters/{id}`, `DELETE /api/v1/empresas/recruiters/{id}`

---

### 3.4 Módulo de Vacantes Laborales y Postulaciones

#### RF-15: Publicación de Ofertas Laborales
* **Actores:** `ROLE_EMPRESA`, `ROLE_RECRUITER`
* **Descripción:** Permite publicar una vacante de empleo especificando cargo/puesto, descripción funcional, modalidad de trabajo (`REMOTO`, `HIBRIDO`, `PRESENCIAL`), tipo de jornada laboral y rango salarial mínimo y máximo.
* **Reglas de Negocio:**
  - Si es publicada por un recruiter, la vacante se asocia a la empresa a la que pertenece dicho recruiter.
* **Endpoints API:** `POST /api/v1/vacantes`, `POST /api/v1/vacantes/me`

#### RF-16: Consulta Pública y Catálogo de Vacantes (RF07)
* **Actores:** `PUBLIC`, `ROLE_EMPLOYEE`
* **Descripción:** Permite consultar el catálogo completo de vacantes de empleo activas en el sistema y visualizar el detalle completo de una vacante por ID.
* **Reglas de Negocio:**
  - Los filtros facetados por texto y modalidad de trabajo se aplican en la capa de interfaz de usuario en memoria sobre el listado provisto por la API.
* **Endpoints API:** `GET /api/v1/vacantes`, `GET /api/v1/vacantes/{id}`

#### RF-17: Modificación y Baja de Vacantes Laborales (RF05)
* **Actores:** `ROLE_EMPRESA`, `ROLE_RECRUITER`
* **Descripción:** Permite modificar los datos de una vacante activa (puesto, condiciones, salario) o eliminarla/cerrarla definitivamente del catálogo de postulaciones.
* **Reglas de Negocio:**
  - Sólo el reclutador o empresa propietaria de la vacante puede modificarla o darla de baja.
* **Endpoints API:** `PUT /api/v1/vacantes/{id}`, `DELETE /api/v1/vacantes/{id}`

#### RF-18: Postulación de Candidatos a Vacantes
* **Actores:** `ROLE_EMPLOYEE`
* **Descripción:** Permite a un candidato postularse a una vacante laboral enviando un mensaje de presentación y adjuntando su perfil y CV registrado.
* **Reglas de Negocio:**
  - Un postulante no puede postularse más de una vez a la misma vacante activa.
* **Endpoints API:** `POST /api/v1/vacantes/{id}/postulaciones`, `POST /api/v1/vacantes/{id}/postulaciones/me`

#### RF-19: Consulta de Postulantes por la Empresa
* **Actores:** `ROLE_EMPRESA`, `ROLE_RECRUITER`
* **Descripción:** Permite al reclutador o empresa visualizar todos los candidatos que se han postulado a una vacante específica de su autoría, accediendo a sus perfiles y CVs.
* **Endpoint API:** `GET /api/v1/vacantes/{id}/postulaciones`

---

### 3.5 Módulo de Entrevistas Laborales

#### RF-20: Agendamiento de Entrevistas Técnicas (RF58)
* **Actores:** `ROLE_RECRUITER`, `ROLE_EMPRESA`
* **Descripción:** Permite al reclutador concertar una entrevista laboral con un candidato que posea una postulación activa a una vacante, registrando fecha, hora de inicio y enlace a la sala de videoconferencia (Google Meet, Zoom, Microsoft Teams).
* **Reglas de Negocio:**
  - La postulación debe encontrarse en estado válido previo a la entrevista.
  - La fecha y hora de la entrevista no pueden pertenecer al pasado.
* **Endpoint API:** `POST /api/v1/recruiter/entrevistas`

#### RF-21: Listado de Entrevistas del Reclutador
* **Actores:** `ROLE_RECRUITER`, `ROLE_EMPRESA`
* **Descripción:** Permite consultar la nómina de entrevistas agendadas por el reclutador o empresa, con los datos consolidados del candidato, puesto y fecha coordinada.
* **Endpoint API:** `GET /api/v1/recruiter/entrevistas`

---

### 3.6 Módulo de Mercado Freelance y Proyectos

#### RF-22: Publicación de Proyectos Freelance
* **Actores:** `ROLE_CLIENTE`
* **Descripción:** Permite a un cliente particular dar de alta una solicitud de servicio freelance con título, descripción detallada del alcance y presupuesto asignado.
* **Reglas de Negocio:**
  - El proyecto se inicia automáticamente en estado `BUSCANDO_TALENTO`.
* **Endpoints API:** `POST /api/v1/freelance/proyectos`, `POST /api/v1/freelance/proyectos/me`

#### RF-23: Modificación y Cancelación de Proyectos Freelance (RF26)
* **Actores:** `ROLE_CLIENTE`
* **Descripción:** Permite al cliente autor modificar los datos de un proyecto propio o cancelarlo definitivamente.
* **Reglas de Negocio:**
  - La edición de datos (`PUT`) únicamente está permitida mientras el proyecto permanezca en estado `BUSCANDO_TALENTO`. Una vez que el proyecto pasa a estar `EN_PROGRESO`, queda bloqueado para modificaciones.
  - La cancelación cambia el estado del proyecto a `CANCELADO`.
* **Endpoints API:** `PUT /api/v1/freelance/proyectos/{id}`, `PUT /api/v1/freelance/proyectos/{id}/cancelar`

#### RF-24: Catálogo Público y Consulta de Proyectos (RF28)
* **Actores:** `PUBLIC`, `ROLE_FREELANCER`
* **Descripción:** Permite listar todos los proyectos freelance vigentes y consultar el detalle técnico individual por identificador.
* **Reglas de Negocio:**
  - El filtrado por presupuesto o tecnología se ejecuta de forma desacoplada en memoria del cliente web sobre el catálogo devuelto.
* **Endpoints API:** `GET /api/v1/freelance/proyectos`, `GET /api/v1/freelance/proyectos/{id}`

#### RF-25: Envío y Evaluación de Propuestas de Trabajo
* **Actores:** `ROLE_FREELANCER`, `ROLE_CLIENTE`
* **Descripción:** Permite a un freelancer enviar una propuesta con plazo de entrega y honorarios a un proyecto en convocatoria, y al cliente listar todas las propuestas recibidas para su evaluación.
* **Endpoints API:** `POST /api/v1/freelance/proyectos/{id}/postulaciones`, `GET /api/v1/freelance/proyectos/{id}/postulaciones`

---

### 3.7 Módulo de Pasarela de Pagos (Mercado Pago)

#### RF-26: Generación de Preferencia Checkout Pro
* **Actores:** `ROLE_CLIENTE`
* **Descripción:** Permite al cliente generar una orden de cobro mediante la pasarela de pagos integrada de Mercado Pago para fondear un proyecto freelance contratado.
* **Reglas de Negocio:**
  - El backend interactúa con la API de Mercado Pago mediante el SDK oficial y devuelve un `initPoint` (URL de redirección segura) y `preferenceId`.
  - La llamada HTTP hacia la pasarela externa se ejecuta de forma desacoplada y fuera de transacciones de base de datos para no bloquear conexiones de base de datos.
* **Endpoint API:** `POST /api/v1/freelance/proyectos/{id}/pagos/checkout`

#### RF-27: Procesamiento Asíncrono de Webhooks de Pago
* **Actores:** `PUBLIC` (Notificación del Servidor de Mercado Pago)
* **Descripción:** Endpoint público expuesto para recibir las notificaciones push (`webhooks`) de Mercado Pago relativas a eventos de actualización de órdenes de pago (`payment.created`, `payment.updated`).
* **Reglas de Negocio:**
  - Debe ser accesible públicamente sin requerir cabecera Bearer JWT en `SecurityConfig`.
  - El sistema verifica el estado reportado por Mercado Pago (`approved`) y registra la transacción en la tabla `pagos` de MySQL con monto, moneda y medio de pago.
* **Endpoint API:** `POST /api/v1/pagos/webhook`

#### RF-28: Registro y Consulta Histórica de Pagos
* **Actores:** `ROLE_CLIENTE`, `ROLE_FREELANCER`
* **Descripción:** Permite registrar transacciones de pago en base de datos y listar el historial de comprobantes de pago asociados a un proyecto determinado.
* **Endpoints API:** `POST /api/v1/freelance/proyectos/{id}/pagos`, `GET /api/v1/freelance/proyectos/{id}/pagos`

---

### 3.8 Módulo de Analítica y Métricas Financieras (Persistencia Real)

#### RF-29: Dashboard Analítico del Cliente (RF51)
* **Actores:** `ROLE_CLIENTE`
* **Descripción:** Proporciona al cliente métricas consolidadas sobre su actividad: cantidad de proyectos activos, proyectos completados, total acumulado gastado e importe de propuestas pendientes.
* **Reglas de Negocio:**
  - Todos los valores se calculan dinámicamente mediante consultas agregadas (`COUNT`, `SUM`) en MySQL; se prohíbe el uso de datos estáticos o mocks.
* **Endpoint API:** `GET /api/v1/client/dashboard`

#### RF-30: Panel de Finanzas del Freelancer (RF53)
* **Actores:** `ROLE_FREELANCER`
* **Descripción:** Proporciona al profesional independiente un resumen financiero con su saldo disponible cobrado, montos en custodia pendientes de acreditación, fondos retirados e historial de transacciones recibidas.
* **Reglas de Negocio:**
  - Los balances y transacciones se obtienen en tiempo real desde la persistencia relacional (`PagosRepository`).
* **Endpoints API:** `GET /api/v1/freelancer/finances`, `GET /api/v1/freelancer/transactions`

#### RF-31: Dashboard Corporativo de la Empresa
* **Actores:** `ROLE_EMPRESA`
* **Descripción:** Proporciona a la empresa estadísticas consolidadas sobre vacantes abiertas, volumen total de postulaciones recibidas y reclutadores en nómina.
* **Endpoint API:** `GET /api/v1/empresas/dashboard`

---

## 4. Requisitos No Funcionales (RNF)

Los Requisitos No Funcionales definen los atributos de calidad, rendimiento, seguridad y arquitectura bajo el estándar de calidad de software ISO/IEC 25010.

### 4.1 Seguridad y Privacidad (RNF-SEG)

* **RNF-SEG-01: Autenticación Stateless con JWT**  
  Todas las solicitudes dirigidas a recursos protegidos de la API deben autenticarse mediante JSON Web Tokens (JJWT) transmitidos en la cabecera HTTP `Authorization: Bearer <token>`. El servidor no almacenará estado de sesión en memoria (*Stateless*).
* **RNF-SEG-02: Cifrado Criptográfico de Contraseñas**  
  Las contraseñas de los usuarios deben procesarse mediante el algoritmo de derivación de claves BCrypt con factor de costo configurable previo a su inserción en MySQL. Queda terminantemente prohibido almacenar contraseñas en texto legible.
* **RNF-SEG-03: Control de Acceso Basado en Roles (RBAC)**  
  La autorización debe validarse a nivel de endpoint en `SecurityConfig` garantizando que los roles (`ROLE_EMPRESA`, `ROLE_RECRUITER`, `ROLE_CLIENTE`, `ROLE_FREELANCER`, `ROLE_EMPLOYEE`) únicamente ejecuten las operaciones permitidas para su perfil.
* **RNF-SEG-04: Verificación Segura de Google OpenID Connect**  
  La autenticación con Google debe utilizar la biblioteca criptográfica oficial `GoogleIdTokenVerifier` para validar la firma digital RSA, la expiración del token y la coincidencia del `aud` (Audience) con el identificador del proyecto, protegiendo contra ataques de suplantación (*Token Substitution*).
* **RNF-SEG-05: Protección de Pasarela y Webhook Idempotente**  
  El endpoint receptor de notificaciones de Mercado Pago debe procesar los eventos de forma idempotente para evitar duplicación de acreditaciones ante reintentos automáticos del servidor de pagos.
* **RNF-SEG-06: Preservación de Integridad en Desactivación (Baja Lógica)**  
  La baja de cuentas de usuario debe ejecutarse de forma lógica (`is_active = false`) para garantizar la trazabilidad de auditoría de contratos y pagos previos, denegando el acceso a futuras autenticaciones.

### 4.2 Rendimiento, Concurrencia y Eficiencia (RNF-REN)

* **RNF-REN-01: Soporte de Virtual Threads (Java 21 Project Loom)**  
  El backend debe tener habilitado el soporte de Virtual Threads (`spring.threads.virtual.enabled: true`) para atender peticiones I/O concurrentes sin degradación por saturación de hilos a nivel de sistema operativo.
* **RNF-REN-02: Aislamiento Transaccional contra Agotamiento de Pool (HikariCP Safety)**  
  Queda prohibido realizar llamadas HTTP salientes a servicios de terceros (Mercado Pago, Google Identity) dentro de métodos anotados con `@Transactional`. Esto previene el bloqueo prolongado de conexiones físicas del pool de base de datos HikariCP (*Connection Pool Exhaustion*).
* **RNF-REN-03: Optimización de Consultas de Lectura**  
  Todas las operaciones de consulta en repositorios JPA deben ejecutarse bajo `@Transactional(readOnly = true)`, permitiendo a Hibernate desactivar el rastreo de cambios en memoria (*dirty checking*) y optimizar el rendimiento de la CPU.
* **RNF-REN-04: Configuración Calibrada de Conexiones a Base de Datos**  
  El pool de conexiones HikariCP debe estar calibrado con un tamaño máximo de 30 conexiones concurrentes, mínimo de 10 conexiones inactivas y tiempo de espera de conexión máximo de 20 segundos.
* **RNF-REN-05: Prevención de Consultas N+1 y Batch Fetching**  
  La configuración de persistencia JPA debe establecer un tamaño de lote de recuperación (`default_batch_fetch_size: 50`) y tamaño de inserción por lotes (`jdbc.batch_size: 50`) para optimizar el tráfico de red con el motor de base de datos.
* **RNF-REN-06: Compresión de Respuestas HTTP**  
  El servidor debe contar con compresión de respuestas habilitada (`server.compression.enabled: true`) para cargas JSON o XML superiores a 1024 bytes, minimizando el ancho de banda requerido por clientes móviles y web.

### 4.3 Arquitectura y Diseño de Software (RNF-ARQ)

* **RNF-ARQ-01: Arquitectura de Monolito Modular (Modular Monolith)**  
  El código backend debe estructurarse como un monolito desacoplado por subdominios funcionales (`auth`, `perfil`, `enterprise`, `freelance`), evitando la dispersión y sobrecarga operativa de microservicios distribuidos.
* **RNF-ARQ-02: Separación Estricta de Capas**  
  Se debe cumplir el flujo unidireccional por capas: `Controller` $\rightarrow$ `Service` $\rightarrow$ `Repository` $\rightarrow$ `Entity`, impidiendo el acceso a la base de datos desde los controladores web.
* **RNF-ARQ-03: Patrón Adapter para Pasarelas Externas**  
  Las comunicaciones con Mercado Pago y Google deben implementarse detrás de adaptadores dedicados (`MercadoPagoAdapter`, `GoogleAuthServiceImpl`), desacoplando las APIs de terceros de la lógica pura del negocio.
* **RNF-ARQ-04: Encapsulamiento con DTOs Inmutables y Mappers Tipados**  
  Las entidades de persistencia JPA nunca deben exponerse directamente en los controladores REST. La comunicación debe emplear Java Records inmutables para Request y Response DTOs, transformados en tiempo de compilación con **MapStruct** para evitar la sobrecarga de reflexión.
* **RNF-ARQ-05: Manejo Centralizado de Errores (Global Exception Handling)**  
  Todas las excepciones de dominio (`BadRequestException`, `ResourceNotFoundException`, `BusinessRuleException`) deben ser interceptadas por una clase `@RestControllerAdvice`, emitiendo respuestas de error consistentes con el estándar RFC 7807 (`ErrorResponseDTO`).

### 4.4 Persistencia e Integridad de Datos (RNF-CON)

* **RNF-CON-01: Persistencia Relacional Real (Cero Mocks)**  
  Todos los datos expuestos en los endpoints de analítica, balances y gestión deben provenir de tablas relacionales en MySQL 8.0, cumpliendo con las consignas académicas de prohibición de datos simulados en memoria.
* **RNF-CON-02: Integridad Transaccional ACID**  
  Las transacciones de modificación (alta de vacante, postulación, registro de pago) deben operar bajo semántica ACID administrada por Spring Framework (`@Transactional`).
* **RNF-CON-03: Codificación de Caracteres Multibyte**  
  La base de datos MySQL debe configurarse con el juego de caracteres `utf8mb4` y colación `utf8mb4_unicode_ci` para admitir acentos, caracteres internacionales y símbolos especiales sin corrupción de datos.

### 4.5 Interoperabilidad y Documentación de Interfaces (RNF-INT)

* **RNF-INT-01: Especificación Viva OpenAPI 3 / Swagger UI**  
  Todos los controladores y esquemas de la API REST deben documentarse automáticamente mediante SpringDoc OpenAPI 3, exponiendo la documentación interactiva en `/swagger-ui/index.html` y el esquema JSON en `/v3/api-docs`.
* **RNF-INT-02: Versionado Semántico de APIs**  
  Todos los endpoints expuestos deben encontrarse explícitamente versionados en la ruta mediante el prefijo `/api/v1/`.
* **RNF-INT-03: Compatibilidad de Carga de Archivos Multipart**  
  Los endpoints de carga de CV y fotografías deben procesar solicitudes HTTP multipart/form-data con límites máximos configurados para proteger el consumo de memoria del servidor.

### 4.6 Mantenibilidad y Calidad de Código (RNF-MAN)

* **RNF-MAN-01: Lenguaje Base Java 21 LTS**  
  El código debe utilizar características idiomáticas modernas de Java 21: *Records*, *Pattern Matching*, inferencia de tipos con `var` en métodos locales y *Switch Expressions*.
* **RNF-MAN-02: Principios SOLID y Limpieza de Código**  
  Las clases y métodos deben responder al principio de responsabilidad única (SRP), con métodos concisos y nombres orientados al dominio del negocio.
* **RNF-MAN-03: Independencia de Construcción con Maven Wrapper**  
  El proyecto debe incluir el wrapper oficial de Maven (`mvnw` y `mvnw.cmd`) para garantizar compilaciones reproducibles en cualquier entorno sin dependencia de la instalación local de Maven.

---

## 5. Matriz de Trazabilidad: Requisitos vs. Endpoints

| Requisito | Acción Funcional | Método HTTP | Endpoint del Backend | Rol Requerido |
| :--- | :--- | :---: | :--- | :---: |
| **RF-01** | Registro de usuario | `POST` | `/api/v1/auth/register` | `PUBLIC` |
| **RF-02** | Login nativo con JWT | `POST` | `/api/v1/auth/login` | `PUBLIC` |
| **RF-03** | Login federado Google | `POST` | `/api/v1/auth/google` | `PUBLIC` |
| **RF-04** | Consulta de perfil | `GET` | `/api/v1/perfiles/getProfile` | Autenticado |
| **RF-05** | Edición de perfil | `PUT` | `/api/v1/perfiles/updateProfile` | Autenticado |
| **RF-06** | Baja lógica de cuenta | `DELETE` | `/api/v1/perfiles/me` | Autenticado |
| **RF-07** | Subida de CV digital | `POST` | `/api/v1/perfiles/uploadMyCV` | `EMPLOYEE`, `FREELANCER` |
| **RF-08** | Carga de foto de perfil | `POST` | `/api/v1/perfiles/uploadMyPhoto` | Autenticado |
| **RF-08** | Carga de banner portada | `POST` | `/api/v1/perfiles/uploadMyBanner` | Autenticado |
| **RF-09** | Añadir habilidad técnica | `POST` | `/api/v1/perfiles/addMySkill` | Autenticado |
| **RF-09** | Eliminar habilidad | `DELETE` | `/api/v1/perfiles/removeMySkill/{id}` | Autenticado |
| **RF-10** | Añadir proyecto portafolio | `POST` | `/api/v1/perfiles/addMyProject` | Autenticado |
| **RF-10** | Eliminar proyecto | `DELETE` | `/api/v1/perfiles/removeMyProject/{id}` | Autenticado |
| **RF-11** | Registro de empresa | `POST` | `/api/v1/empresas` | `EMPRESA` |
| **RF-11** | Edición de empresa | `PUT` | `/api/v1/empresas/{id}` | `EMPRESA` |
| **RF-12** | Alta de recruiter (RF22) | `POST` | `/api/v1/empresas/recruiters` | `EMPRESA` |
| **RF-13** | Listar recruiters (RF23) | `GET` | `/api/v1/empresas/recruiters` | `EMPRESA` |
| **RF-13** | Detalle recruiter (RF24) | `GET` | `/api/v1/empresas/recruiters/{id}` | `EMPRESA` |
| **RF-14** | Modificar recruiter | `PUT` | `/api/v1/empresas/recruiters/{id}` | `EMPRESA` |
| **RF-14** | Baja de recruiter | `DELETE` | `/api/v1/empresas/recruiters/{id}` | `EMPRESA` |
| **RF-15** | Publicar vacante | `POST` | `/api/v1/vacantes` | `EMPRESA`, `RECRUITER` |
| **RF-16** | Catálogo de vacantes (RF07) | `GET` | `/api/v1/vacantes` | `PUBLIC` |
| **RF-16** | Detalle de vacante | `GET` | `/api/v1/vacantes/{id}` | `PUBLIC` |
| **RF-17** | Modificar vacante (RF05) | `PUT` | `/api/v1/vacantes/{id}` | `EMPRESA`, `RECRUITER` |
| **RF-17** | Baja de vacante (RF05) | `DELETE` | `/api/v1/vacantes/{id}` | `EMPRESA`, `RECRUITER` |
| **RF-18** | Postularse a vacante | `POST` | `/api/v1/vacantes/{id}/postulaciones` | `EMPLOYEE` |
| **RF-19** | Ver postulantes vacante | `GET` | `/api/v1/vacantes/{id}/postulaciones` | `EMPRESA`, `RECRUITER` |
| **RF-20** | Agendar entrevista (RF58) | `POST` | `/api/v1/recruiter/entrevistas` | `RECRUITER`, `EMPRESA` |
| **RF-21** | Listar entrevistas | `GET` | `/api/v1/recruiter/entrevistas` | `RECRUITER`, `EMPRESA` |
| **RF-22** | Publicar proyecto | `POST` | `/api/v1/freelance/proyectos` | `CLIENTE` |
| **RF-23** | Modificar proyecto (RF26) | `PUT` | `/api/v1/freelance/proyectos/{id}` | `CLIENTE` |
| **RF-23** | Cancelar proyecto | `PUT` | `/api/v1/freelance/proyectos/{id}/cancelar` | `CLIENTE` |
| **RF-24** | Catálogo proyectos (RF28) | `GET` | `/api/v1/freelance/proyectos` | `PUBLIC` |
| **RF-25** | Enviar propuesta freelance | `POST` | `/api/v1/freelance/proyectos/{id}/postulaciones` | `FREELANCER` |
| **RF-25** | Ver propuestas recibidas | `GET` | `/api/v1/freelance/proyectos/{id}/postulaciones` | `CLIENTE` |
| **RF-26** | Checkout Mercado Pago | `POST` | `/api/v1/freelance/proyectos/{id}/pagos/checkout` | `CLIENTE` |
| **RF-27** | Webhook Mercado Pago | `POST` | `/api/v1/pagos/webhook` | `PUBLIC` |
| **RF-28** | Registro de pago | `POST` | `/api/v1/freelance/proyectos/{id}/pagos` | `CLIENTE` |
| **RF-29** | Dashboard cliente (RF51) | `GET` | `/api/v1/client/dashboard` | `CLIENTE` |
| **RF-30** | Finanzas freelance (RF53) | `GET` | `/api/v1/freelancer/finances` | `FREELANCER` |
| **RF-30** | Historial transacciones | `GET` | `/api/v1/freelancer/transactions` | `FREELANCER` |
| **RF-31** | Dashboard empresa | `GET` | `/api/v1/empresas/dashboard` | `EMPRESA` |

