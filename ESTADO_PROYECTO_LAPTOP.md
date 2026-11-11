# Estado Técnico y Bitácora del Proyecto (Versión Laptop)
## Documento de Traspaso / Handover para la PC de Escritorio

**Proyecto:** WorkLink Platform Backend (`Proyecto_Final_Progra3`)  
**Fecha de actualización:** 5 de Noviembre de 2026  
**Rama Git:** `main` (limpia, compilando con éxito)  
**Estado de Build:** `BUILD SUCCESS` (JDK 21 - Maven Wrapper)  

---

## 1. Resumen de Commits Recientes en esta Máquina

1. **`e3564e0`** — *feat: crear campo 'nombre_proyecto' y ajustar longitudes en la entidad Proyecto*
   - Modificación en `bd_proyecto_final.sql`.
   - Incorporación de `nombreProyecto` en `ProyectoEntity`, `ProyectoRequestDTO` y `ProyectoResponseDTO`.
   - Sincronización en `ProyectoServiceImpl` (`crearProyecto`, `crearMiProyecto` y `actualizarMiProyecto`).
2. **`33b28ba`** — *feat: mejorar gestión de postulaciones y auditoría, agregar validaciones y enums para estados*
   - Correcciones de compilación, seguridad BOLA/IDOR, transaccionalidad, baja lógica y normalización financiera a `BigDecimal`.

---

## 2. Mejoras y Correcciones ya Aplicadas en la Laptop

### A. Correcciones de Compilación e Imports
* **OpenAPI `@Tag`:** Corregido el paquete incorrecto en `EntrevistaController.java` y `ClientController.java` (`io.swagger.v3.oas.annotations.tags.Tag`).
* **Firma de Portfolio:** Se corrigió en `PerfilUsuarioServiceImpl` la llamada a `portfolioService.guardarCV(emp.getEmployeeId(), username, file)` enviando el `username` obligatorio.
* **Constructor de Proyecto:** Se actualizó `ProyectoRequestDTO` en `ProyectoServiceImpl.crearMiProyecto` para contemplar el nuevo campo `nombreProyecto`.

### B. Seguridad y Control de Acceso (Anti-BOLA / Anti-IDOR)
* **Postulaciones a Vacantes (`PostulacionVacanteServiceImpl`):**
  - **Antes:** Se autorizaba a cualquier usuario con rol `RECRUITER` o `EMPRESA` a cambiar estados de postulaciones de empresas ajenas.
  - **Ahora:** Valida que el recruiter pertenezca estrictamente a la misma empresa titular de la vacante antes de alterar el estado.
* **Agendamiento de Entrevistas (`EntrevistaServiceImpl` y `EntrevistaController`):**
  - El endpoint `POST /api/v1/recruiter/entrevistas` ahora recibe al usuario autenticado (`Authentication`).
  - El servicio valida que la postulación a entrevistar pertenezca a una vacante de la empresa del usuario en sesión.
* **Aserciones en Runtime (`AuthServiceImpl`):**
  - Se eliminó `assert perfil != null;` (ignorado en producción por la JVM) y se reemplazó por validación explícita con excepción `UnauthorizedAccessException`.

### C. Rendimiento y Persistencia (Spring & Hibernate)
* **Sobrecarga de Memoria Heap (`PerfilEntity`):**
  - Los campos binarios `fotoPerfil` y `bannerPerfil` (`LONGBLOB`) se marcaron con `@Basic(fetch = FetchType.LAZY)`.
  - Con esto, cada request HTTP entrante con JWT no descarga megabytes de imágenes en `loadUserByUsername()`.
* **Bypass Transaccional por Auto-Invocación (`PagosServiceImpl` y `GoogleAuthServiceImpl`):**
  - Se solucionó el problema de self-invocation de Spring AOP inyectando su referencia proxificada `@Lazy private ... self`.
  - `guardarPagoConfirmado(...)` y `findOrCreatePerfil(...)` ahora abren y cierran transacciones SQL reales.
* **Baja Lógica de Vacantes (`VacanteEntity` y `VacanteServiceImpl`):**
  - Se agregó el campo `activa = true`.
  - El método `eliminarVacante` ejecuta `vacante.setActiva(false)` en lugar de `repository.delete()`, evitando fallos de clave foránea (`DataIntegrityViolationException`) con postulaciones previas.
  - El listado público filtra mediante `vacanteRepository.findByActivaTrue()`.
* **Auditoría Resiliente (`AuditoriaListener`):**
  - El bloque de guardado de auditoría JPA se protegió con `catch (Throwable e)` para garantizar que un fallo en auditoría nunca aborte la transacción principal del usuario.

### D. Modelo de Dominio y Normalización Monetaria
* **Tipo de Datos Financiero:**
  - `monto` / `amount` se migró de `Integer` a `BigDecimal` en `PagosEntity`, `PagosRequestDTO`, `PagosResponseDTO` y `FreelancerTransactionResponseDTO`.
  - Se eliminó el truncamiento de centavos (`intValue()`) en el webhook de Mercado Pago.
* **Sincronización de Estados de Propuestas:**
  - Se creó el enum `EstadoPostulacionProyecto` (`PENDIENTE`, `ACEPTADA`, `RECHAZADA`).
  - Se mapeó en `PostulacionProyectoEntity`, `PostulacionProyectoRequestDTO`, `PostulacionProyectoResponseDTO` y `PostulacionProyectoMapper`.

---

## 3. Puntos Pendientes a Completar en la PC de Escritorio

Si en tu PC de escritorio vas a avanzar con los puntos de diseño y patrones que faltaban, aquí tienes el detalle de lo que resta:

### 1. Desacoplamiento del Registro con Observer (Spring Events)
* **Archivo:** `src/main/java/.../service/perfil/perfilImpl/AuthServiceImpl.java` (método `register`).
* **Situación actual:** `AuthServiceImpl` tiene inyectados `EmployeeRepository`, `ClientRepository`, `EmpresaRepository` e `InstructorRepository` y usa `if-else` para instanciar cada uno con datos dummy.
* **Solución pendiente:**
  1. Crear la clase de evento: `UserRegisteredEvent(PerfilEntity perfil, String rol)`.
  2. En `AuthServiceImpl.register`:
     ```java
     eventPublisher.publishEvent(new UserRegisteredEvent(perfil, rol.getNombre()));
     ```
  3. En cada módulo (`EmployeeService`, `EmpresaService`, `ClientService`), crear métodos con `@EventListener` que capturen el evento y creen su propia entidad de perfil.

### 2. Desmantelar la "God Class" de Perfiles con Strategy Pattern
* **Archivo:** `src/main/java/.../service/perfil/perfilImpl/PerfilUsuarioServiceImpl.java`.
* **Situación actual:** Contiene 10 repositorios inyectados y cadenas de `if ("CLIENTE".equals(rol)) ... else if ("EMPRESA"...)` tanto para leer como para actualizar perfiles.
* **Solución pendiente:**
  1. Crear la interfaz:
     ```java
     public interface ProfileHandlerStrategy {
         boolean supports(String rol);
         void populateProfile(PerfilEntity perfil, PerfilUsuarioDTO dto);
         void updateProfile(PerfilEntity perfil, PerfilUsuarioDTO dto);
     }
     ```
  2. Implementar `FreelancerProfileHandler`, `ClientProfileHandler`, `EmpresaProfileHandler` y `RecruiterProfileHandler`.
  3. Inyectar `List<ProfileHandlerStrategy>` en `PerfilUsuarioServiceImpl` para delegar sin un solo `if-else`.

### 3. Pasarela de Pagos desacoplada con GoF Adapter
* **Archivo:** `src/main/java/.../service/freelance/freelanceImpl/PagosServiceImpl.java`.
* **Situación actual:** Depende directamente de la clase concreta `MercadoPagoAdapter` y maneja clases del SDK externo (`com.mercadopago.resources.payment.Payment`).
* **Solución pendiente:**
  1. Definir una interfaz de dominio: `IPaymentGateway` (con `crearPreferencia(...)` y `verificarPago(...)` retornando un DTO propio agnóstico).
  2. Hacer que `MercadoPagoAdapter implements IPaymentGateway`.
  3. Inyectar `IPaymentGateway` en `PagosServiceImpl` (cumpliendo **DIP**).

### 4. Modelo Rico e Information Expert en Hitos y Proyectos
* **Archivos:** `HitoProyectoEntity.java`, `HitoServiceImpl.java`, `ProyectoServiceImpl.java`.
* **Situación actual:** 
  - `HitoServiceImpl` muta el estado con `hito.setEstado(nuevoEstado)` permitiendo saltar de `PENDIENTE` a `PAGADO` sin validación.
  - `ProyectoServiceImpl.asignarFreelancer` permite adjudicar el proyecto a un `employeeId` que nunca se postuló al proyecto.
* **Solución pendiente:**
  - Encapsular métodos en `HitoProyectoEntity`: `marcarCompletado()`, `marcarPagado()`, que arrojen `BusinessRuleException` si la transición es inválida.
  - En `ProyectoServiceImpl.asignarFreelancer`, verificar que exista una postulación previa de ese freelancer para ese proyecto, y actualizar su estado a `ACEPTADA` y las demás a `RECHAZADA`.

### 5. Limpieza de Scope y Código Muerto (YAGNI)
* **Bootcamps:** El archivo `requisitos.md` excluye formalmente el módulo. Evaluar si se depreca o se aísla por completo.
* **Entidades Huérfanas:** `MetodosCobroFreelancer.java` y `MetodosPagoCliente.java` no poseen lógica ni repositorios en el backend.
* **Habilidades:** Renombrar `EmployeeServiceImpl` a `HabilidadServiceImpl`, crear `HabilidadMapper` con MapStruct y retirar la lógica de Stream manual en `HabilidadController`.

---

## 4. Configuración del Entorno de Ejecución

* **Java:** 21 (OpenJDK / Eclipse Temurin).
* **Base de Datos:** MySQL 8.0+ en `localhost:3306` (Base: `bd_proyecto_final`).
* **Script SQL:** [`bd_proyecto_final.sql`](bd_proyecto_final.sql) actualizado con `nombre_proyecto`.
* **Comandos de Verificación:**
  ```powershell
  # Compilar sin pruebas
  .\mvnw.cmd compile -DskipTests

  # Ejecutar aplicación
  .\mvnw.cmd spring-boot:run
  ```
* **Variables requeridas en `.env`:**
  - `GOOGLE_CLIENT_ID`
  - `MERCADOPAGO_ACCESS_TOKEN`
  - `JWT_SECRET`, `JWT_EXPIRATION`
  - `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`
