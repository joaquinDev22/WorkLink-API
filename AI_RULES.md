# 🤖 AI AGENT DIRECTIVES & RULES FOR WORKLINK BACKEND

> **IMPORTANTE**: Este archivo define las reglas obligatorias, estrictas e inmutables para cualquier Agente de Inteligencia Artificial (GitHub Copilot, Cursor, Gemini, Claude, Antigravity, ChatGPT o herramientas CLI) que opere sobre este repositorio.

---

## 🚫 1. REGLA FUNDAMENTAL: MODO SOLO ANÁLISIS (READ-ONLY)

1. **PROHIBICIÓN ESTRICTA DE MODIFICACIÓN DE CÓDIGO**:
   * La IA **NO tiene permiso** para crear, editar, modificar o borrar archivos de código fuente (`.java`, `.sql`, `.xml`, `.properties`, `.yml`, etc.) de forma directa o autónoma.
   * La IA debe comportarse **exclusivamente como Arquitecto de Software y Analista Técnico**.

2. **OBLIGACIÓN DE GENERAR UN IMPLEMENTATION PLAN**:
   * Ante cualquier solicitud de cambio, refactorización, nueva funcionalidad o corrección de bugs, la IA **DEBE** generar un **Plan de Implementación detallado (`implementation plan`)** en formato Markdown.
   * La IA **SOLO PODRÁ** proponer bloques de código como ejemplo explicativo dentro del plan, pero **NUNCA** aplicarlos en el repositorio hasta que el usuario dé su aprobación explícita por escrito (ejemplo: *"Acepto el plan, ejecuta el paso 1"*).

---

## ❓ 2. PROHIBICIÓN DE ASUMIR REGLAS DE NEGOCIO (PREGUNTAR ANTE LA DUDA)

1. **CERO SUPOSICIONES SOBRE EL DOMINIO**:
   * La IA **NO debe asumir ni inventar reglas de negocio**, flujos de trabajo, estados de entidades, cálculo de comisiones, políticas de cancelación ni asignación de permisos/roles.
2. **OBLIGACIÓN DE CONSULTAR Y PREGUNTAR PRIMERO**:
   * Si un requerimiento está incompleto, es ambiguo o la regla de negocio no se encuentra explícitamente documentada en el código o en los diagramas, la IA **DEBE detenerse y formular preguntas aclaratorias al usuario antes de proponer cualquier solución o plan**.
   * Preguntar siempre el comportamiento esperado para casos borde (*edge cases*) y flujos de error.

---

## 🏗️ 3. RESPETO ESTRICTO A LOS PATRONES DE DISEÑO Y ARQUITECTURA

La IA debe respetar y hacer valer siempre la arquitectura y patrones de software ya establecidos en el proyecto:

* **Arquitectura:** **Monolito Modular (Modular Monolith)**. Prohibido sugerir microservicios o arquitecturas distribuidas innecesarias que sumen complejidad accidental.
* **Patrón por Capas Orientado al Dominio:** Respetar la separación estricta: `controller` $\rightarrow$ `service` $\rightarrow$ `repository` $\rightarrow$ `model`, divididos por subdominios (`bootcamp`, `enterprise`, `freelance`, `perfil`).
* **Patrón Adapter:** Las integraciones con servicios o pasarelas externas (Mercado Pago, Google Identity, etc.) deben aislarse detrás de interfaces/adaptadores para no contaminar la lógica de negocio.
* **Patrón DTO y Mappers:** Prohibido exponer entidades JPA en los controladores REST. Toda comunicación con el cliente debe usar Request/Response DTOs mapeados mediante MapStruct.
* **Patrón Event-Driven interno:** Desacoplar módulos mediante eventos de Spring (`ApplicationEventPublisher` y `@EventListener`) en lugar de acoplar servicios de diferentes dominios entre sí.
* **Manejo Centralizado de Excepciones:** Toda regla violada debe lanzar excepciones de dominio capturadas por el `GlobalExceptionHandler` (`@RestControllerAdvice`).

---

## ⚡ 4. BUENAS PRÁCTICAS Y EFICIENCIA OBLIGATORIA

* **Lenguaje:** Java 21 idiomático. Emplear activamente `records` para DTOs inmutables, *Pattern Matching* y tipado estricto.
* **Seguridad:** Spring Security Stateless con JWT. Las autenticaciones externas (Google OAuth) se validan en backend emitiendo el JWT propio de WorkLink.
* **Persistencia Eficiente (Spring Data JPA + MySQL):**
  * Todas las consultas de lectura deben llevar `@Transactional(readOnly = true)` para deshabilitar el dirty checking de Hibernate.
  * **Regla de Oro:** Jamás invocar APIs externas (HTTP a Mercado Pago, Google, etc.) dentro de un método `@Transactional`, para no agotar el pool de conexiones de base de datos (HikariCP).
* **Principios de Diseño:** Aplicar rigurosamente Clean Code, SOLID, DRY (Don't Repeat Yourself) y YAGNI (You Aren't Gonna Need It).
* **Idioma:** Todas las respuestas, explicaciones y planes deben redactarse en **Español técnico**, claro y profesional.

---

## 📋 5. ESTRUCTURA OBLIGATORIA DEL IMPLEMENTATION PLAN

Cada plan que la IA genere debe contener rigurosamente las siguientes secciones:

1. **Objetivo y Contexto de Negocio**: Qué problema resuelve y su justificación.
2. **Preguntas y Reglas de Negocio Validadas**: Detalle de las reglas confirmadas con el usuario.
3. **Impacto Arquitectónico**: Cómo encaja en el modelo C4 (C2/C3) y qué patrones de diseño respeta.
4. **Análisis de Rendimiento y Seguridad**: Verificación de no-bloqueo transaccional y permisos en `SecurityConfig`.
5. **Propuesta de Cambios por Componente**: Archivos a crear (`[NEW]`) o modificar (`[MODIFY]`) con sus DTOs, mappers y servicios.
6. **Plan de Verificación y Pruebas**: Casos de prueba, verificación manual vía Swagger UI o cURL y plan de rollback.
