# 📑 PLAN DE IMPLEMENTACIÓN: ARQUITECTURA MODULAR, MERCADO PAGO Y GOOGLE OAUTH2

**Proyecto:** WorkLink Backend (UTN - Programación III)  
**Tecnologías:** Java 21, Spring Boot 4 / 3.x, Spring Security (Stateless JWT), Spring Data JPA, MySQL  
**Estado:** Planificado (Listo para revisión y aprobación)

---

## 1. Visión y Objetivos Arquitectónicos

### 1.1 Contexto y Decisión de Diseño
WorkLink es una plataforma monolítica orientada a tres modelos de negocio: formación (Bootcamps), empleo tradicional (Vacantes) y servicios independientes (Freelance).
* **Decisión:** Mantener una arquitectura de **Monolito Modular (Modular Monolith)**.
* **Justificación técnica vs Microservicios:** Se descarta la arquitectura de microservicios y API Gateway para evitar complejidad accidental (transacciones distribuidas tipo Saga, pérdida de integridad referencial ACID en MySQL, latencia de red innecesaria y sobrecarga de mantenimiento para un equipo de 3 desarrolladores).
* **Preparación para el futuro:** Los módulos quedan desacoplados lógicamente, listos para extraerse como microservicios si la demanda futura lo justificara.

### 1.2 Nuevas Capacidades a Incorporar
1. **Autenticación con Google OAuth2:** Validación criptográfica de `idToken` en backend y emisión del token JWT nativo de WorkLink para clientes SPA.
2. **Pasarela de Pagos Mercado Pago:** Creación de preferencias de pago (Checkout Pro) y procesamiento asíncrono seguro mediante Webhooks.
3. **Optimización de Rendimiento:** Garantizar cero bloqueo de conexiones de base de datos durante comunicaciones con APIs externas.

---

## 2. Especificación Técnica: Google OAuth2

### 2.1 Flujo de Autenticación Stateless para SPAs
```
[Frontend (Angular/React)]
       │
       │ 1. Usuario inicia sesión con Google Identity Services
       ▼
[Google OAuth Servers] ─── (Devuelve idToken firmado criptográficamente) ───► [Frontend]
                                                                                   │
                                                               2. POST /api/v1/auth/google
                                                                  { idToken: "..." }
                                                                                   ▼
                                                                        [Spring Boot Backend]
                                                                                   │
                                                     3. Valida firma local con GoogleIdTokenVerifier
                                                     4. Busca o crea PerfilEntity en MySQL
                                                     5. Genera JWT nativo de WorkLink
                                                                                   ▼
                                                                    [Frontend con JWT de sesión]
```

### 2.2 Dependencias requeridas en `pom.xml`
```xml
<dependency>
    <groupId>com.google.api-client</groupId>
    <artifactId>google-api-client</artifactId>
    <version>2.7.0</version>
</dependency>
```

### 2.3 Variables de Entorno (`.env` / `application.properties`)
```properties
GOOGLE_CLIENT_ID=tu-client-id-de-google.apps.googleusercontent.com
```

### 2.4 Componentes a Crear / Modificar
* `[NEW]` `dto/requestdto/GoogleLoginRequestDTO.java`: DTO record que recibe el `idToken`.
* `[NEW]` `service/perfil/perfilInterfaces/IGoogleAuthService.java`: Contrato del servicio de validación.
* `[NEW]` `service/perfil/perfilImpl/GoogleAuthServiceImpl.java`: Validación del token con `GoogleIdTokenVerifier`, sincronización en base de datos y llamado a `JwtServiceImpl`.
* `[MODIFY]` `controller/AuthController.java`: Exponer `POST /api/v1/auth/google`.
* `config/security/SecurityConfig.java`: Ya cubre `/api/v1/auth/**` como ruta pública (`permitAll()`).

---

## 3. Especificación Técnica: Pasarela Mercado Pago

### 3.1 Flujo de Checkout Pro y Webhook Asíncrono
```
[Cliente Frontend] ─── (1. Solicita pagar hito) ───► [PagosController]
                                                            │
                                        (2. Crea Preferencia sin @Transactional)
                                                            ▼
                                                   [MercadoPagoAdapter] ───► [API Mercado Pago]
                                                            │                        │
                                            (3. Retorna init_point URL) ◄────────────┘
                                                            ▼
[Cliente Frontend] ◄─── (4. Redirige a init_point) ─────────┘
        │
        │ 5. Usuario abona en la pasarela de Mercado Pago
        ▼
[Mercado Pago] ─── (6. Dispara Webhook POST /api/v1/pagos/webhook?type=payment&data.id=123)
                          │
                          ▼
              [MercadoPagoWebhookController]
                          │
         (7. Consulta estado del pago a Mercado Pago)
         (8. Abre @Transactional corta para actualizar estado en MySQL)
         (9. Publica PagoConfirmadoEvent)
```

### 3.2 Dependencias requeridas en `pom.xml`
```xml
<dependency>
    <groupId>com.mercadopago</groupId>
    <artifactId>sdk-java</artifactId>
    <version>2.1.28</version>
</dependency>
```

### 3.3 Variables de Entorno (`.env`)
```properties
MERCADOPAGO_ACCESS_TOKEN=APP_USR-tu-access-token-de-prueba
MERCADOPAGO_WEBHOOK_URL=https://tu-dominio.com/api/v1/pagos/webhook
```

### 3.4 Componentes a Crear / Modificar
* `[NEW]` `config/mercadopago/MercadoPagoConfigBean.java`: Inicializa el SDK con `@PostConstruct`.
* `[NEW]` `service/freelance/freelanceImpl/MercadoPagoAdapter.java`: Generación de preferencias con items, precio y URLs de retorno.
* `[NEW]` `controller/freelance/MercadoPagoWebhookController.java`: Endpoint público para recibir eventos de Mercado Pago.
* `[MODIFY]` `service/freelance/freelanceInterfaces/IPagosService.java`: Métodos para crear preferencia y procesar notificación de pago.
* `[MODIFY]` `service/freelance/freelanceImpl/PagosServiceImpl.java`: Lógica transaccional de actualización de pago.
* `[MODIFY]` `config/security/SecurityConfig.java`: Agregar `/api/v1/pagos/webhook` a la lista de `permitAll()`.

---

## 4. Principios de Rendimiento y Eficiencia

1. **Evitar agotamiento del Connection Pool (HikariCP):**
   * Las llamadas HTTP salientes a Mercado Pago o Google tardan entre 200 ms y 1500 ms.
   * **REGLA:** Jamás envolver la llamada al SDK de Mercado Pago dentro de un método anotado con `@Transactional`. Primero se obtiene la respuesta del servicio externo y luego se abre la transacción SQL para escribir en base de datos.
2. **Consultas de solo lectura:**
   * Toda consulta en repositorios JPA que no requiera modificar entidades debe ejecutarse con `@Transactional(readOnly = true)` para deshabilitar el dirty checking de Hibernate.
3. **Caché en Validación de Tokens:**
   * La biblioteca `GoogleIdTokenVerifier` descarga y mantiene en caché las claves públicas de firma de Google, ejecutando la validación en memoria sin latencia de red.

---

## 5. Plan de Tareas por Fases

### Fase 1: Configuración de Dependencias y Credenciales
- [ ] Incorporar dependencias de Google API Client y Mercado Pago SDK en `pom.xml`.
- [ ] Declarar variables de entorno en `.env.example` y documentar obtención de credenciales.

### Fase 2: Implementación de Google OAuth2
- [ ] Implementar `GoogleLoginRequestDTO` y `IGoogleAuthService`.
- [ ] Crear `GoogleAuthServiceImpl` con validación de firma y vinculación de roles.
- [ ] Exponer endpoint `POST /api/v1/auth/google` en `AuthController`.
- [ ] Verificar login con token simulado o token real de Google Playground.

### Fase 3: Integración de Mercado Pago Checkout Pro
- [ ] Crear bean de configuración `MercadoPagoConfigBean`.
- [ ] Implementar `MercadoPagoAdapter` para generar `PreferenceRequest`.
- [ ] Exponer endpoint `POST /api/v1/freelance/proyectos/{id}/pagos/checkout` que retorne la URL de pago.
- [ ] Crear `MercadoPagoWebhookController` público en `SecurityConfig`.
- [ ] Implementar verificación y actualización de estado en `PagosServiceImpl`.

### Fase 4: Pruebas y Verificación
- [ ] Probar flujo completo de pago con tarjetas de prueba de Mercado Pago (Sandbox).
- [ ] Probar autenticación con Google y verificación de generación de JWT.
- [ ] Verificar auditoría automática en `AuditoriaListener`.

