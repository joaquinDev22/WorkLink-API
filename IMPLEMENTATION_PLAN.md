# 📐 MASTER IMPLEMENTATION PLAN: ARQUITECTURA FRONTEND ANGULAR CLI v22

**Proyecto:** WorkLink Platform (Cliente Web)  
**Tecnología Base:** Angular CLI v22 (Standalone Components, Signals-first, Control Flow, `@defer`, Strict TypeScript)  
**Backend Vinculado:** WorkLink REST API (Java 21, Spring Boot 3.x, MySQL, JWT Stateless)  
**Estado:** Documento Rector y Especificación Arquitectónica Definitiva  

---

## 1. Visión y Decisiones Arquitectónicas Fundamentales

Para construir un frontend escalable, mantenible y alineado con los estándares modernos de **Angular v22**, resolvemos a continuación las preguntas estructurales clave:

### 1.1 ¿Se debe separar UI (Presentational/Dumb) de Pages (Smart/Containers)?
**SÍ, rotunda y obligatoriamente.** Es el patrón fundamental **Smart vs. Dumb Components**:
* **Pages / Containers (`features/.../pages/`):**
  * Tienen acceso a los servicios e inyectan el estado (`inject(StateService)`).
  * Coordinan flujos de negocio, escuchan eventos, realizan llamadas HTTP y manejan navegación (`Router`).
  * No definen estilos visuales complejos ni HTML repetitivo; delegan la presentación a componentes de UI.
* **Componentes de UI / Presentational (`shared/components/` o `features/.../components/`):**
  * **Cero acoplamiento al backend o a servicios de datos.**
  * Se comunican con el exterior únicamente mediante **`input()`** (para recibir datos) y **`output()`** (para emitir eventos del usuario).
  * Son 100% testeables en aislamiento, reutilizables y sin efectos secundarios.

### 1.2 ¿Debería existir una carpeta especializada para cada servicio?
**SÍ, siguiendo una regla de dos niveles (Colocación por Alcance):**
1. **Servicios Globales / Singleton (`core/`):**
   * Servicios que viven durante todo el ciclo de vida de la aplicación (`AuthService`, `TokenStorageService`, `ThemeService`, `NotificationService`).
2. **Servicios de Característica / Estado (`features/<modulo>/services/`):**
   * Cada subdominio o feature tiene su propia carpeta `services/` dentro de su módulo.
   * Contiene el **Signal State Service** (Signal Store) específico de esa funcionalidad (ejemplo: `vacante-state.service.ts`, `proyecto-state.service.ts`, `recruiter.service.ts`).
   * **Ventaja:** Si un módulo se elimina o se refactoriza, sus servicios se mueven con él sin dejar código huérfano.

### 1.3 ¿Deberían crearse Modelos e Interfaces?
**SÍ, SIEMPRE.** En TypeScript la falta de modelos tipados degenera en código con `any`, pérdida de autocompletado y errores en tiempo de ejecución:
* **Modelos Compartidos (`shared/models/` o `core/models/`):** Interfaces genéricas como `ApiResponse<T>`, `Pagination<T>`, enumeraciones globales (`Role`, `EstadoProyecto`).
* **Modelos Específicos (`features/<modulo>/models/`):** Contratos tipados que mapean con exactitud quirúrgica los **Request DTOs** y **Response DTOs** de la API de Spring Boot.

### 1.4 Paradigma Reactivo: Signals-first vs. RxJS
* **Signals (`signal()`, `computed()`, `effect()`):** Es la fuente de la verdad para el **estado síncrono y la UI**. Reemplaza por completo a `BehaviorSubject` en componentes y servicios de estado.
* **RxJS (`HttpClient`, `Observable`, `pipe`):** Se reserva **exclusivamente** para operaciones asíncronas de red (`HttpClient`), cancelación de peticiones, *debouncing* de búsquedas y *web sockets*. Se conecta a Signals mediante `toSignal()` o suscripciones controladas.

---

## 2. Esquema Completo del Árbol de Directorios (Folder Structure)

```text
src/
├── app/
│   ├── app.config.ts                         # Configuración central: Router, HttpClient, Interceptors
│   ├── app.routes.ts                         # Enrutamiento raíz con lazy loading por subdominios
│   ├── app.component.ts                      # Shell raíz de la aplicación (<router-outlet />)
│   │
│   ├── core/                                 # SERVICIOS SINGLETON Y SEGURIDAD (Se carga 1 sola vez)
│   │   ├── auth/                             # Dominio de Autenticación
│   │   │   ├── guards/                       # auth.guard.ts, role.guard.ts (CanActivateFn)
│   │   │   ├── interceptors/                 # auth.interceptor.ts, error.interceptor.ts (HttpInterceptorFn)
│   │   │   └── services/                     # auth.service.ts (Signal-based), token-storage.service.ts
│   │   ├── layout/                           # Componentes estructurales de la ventana principal
│   │   │   ├── navbar/                       # Barra superior con datos de usuario y logout
│   │   │   ├── sidebar/                      # Menú lateral dinámico según rol (Empresa, Cliente, etc.)
│   │   │   └── main-layout.component.ts      # Contenedor con Sidebar + Navbar + Content
│   │   └── models/                           # session.model.ts, user-role.enum.ts
│   │
│   ├── shared/                               # RECURSOS REUTILIZABLES (UI Kit, Pipes, Directivas)
│   │   ├── components/                       # DUMB COMPONENTS (Pura presentación: inputs y outputs)
│   │   │   ├── button/                       # Botones con estados de carga
│   │   │   ├── modal/                        # Modal genérico accesible
│   │   │   ├── data-table/                   # Tabla genérica con ordenamiento y paginación
│   │   │   ├── file-uploader/                # Carga de archivos (PDF/Imágenes) con Drag & Drop
│   │   │   ├── status-badge/                 # Etiquetas de estado (Activo, En Proceso, etc.)
│   │   │   └── spinner/                      # Indicadores visuales de carga
│   │   ├── directives/                       # Directivas estructurales y de atributos
│   │   │   ├── has-role.directive.ts         # Muestra/oculta elementos del DOM según rol
│   │   │   └── click-outside.directive.ts    # Cierre de menús flotantes
│   │   ├── pipes/                            # currency-ars.pipe.ts, time-ago.pipe.ts
│   │   └── models/                           # api-response.model.ts, pagination.model.ts
│   │
│   └── features/                             # SUBDOMINIOS DE NEGOCIO (Smart Pages + Feature Components)
│       │
│       ├── auth/                             # Subdominio: Autenticación
│       │   ├── pages/login/                  # login.component.ts (Página)
│       │   ├── pages/register/               # register.component.ts (Página)
│       │   ├── components/google-btn/        # google-button.component.ts (GIS Button)
│       │   ├── models/auth.models.ts         # LoginRequest, RegisterRequest, GoogleLoginRequest
│       │   └── auth.routes.ts                # Sub-rutas de autenticación
│       │
│       ├── perfiles/                         # Subdominio: Perfil y Portafolio Multimedia
│       │   ├── pages/mi-perfil/              # mi-perfil.component.ts (Página principal)
│       │   ├── components/cv-uploader/       # cv-uploader.component.ts
│       │   ├── components/skill-selector/    # skill-selector.component.ts
│       │   ├── components/portfolio-grid/    # portfolio-grid.component.ts
│       │   ├── services/perfil.service.ts    # Conexión con /api/v1/perfiles (Carga binaria y datos)
│       │   └── models/perfil.models.ts       # PerfilResponse, SkillRequest, ProjectRequest
│       │
│       ├── enterprise/                       # Subdominio: Corporativo y Recruiting
│       │   ├── dashboard/                    # dashboard-empresa.component.ts (Métricas)
│       │   ├── recruiters/                   # GESTIÓN DE RECRUITERS (RF22-24)
│       │   │   ├── pages/recruiter-list/     # recruiter-list.component.ts (Página)
│       │   │   ├── components/recruiter-modal/# recruiter-form-modal.component.ts (UI)
│       │   │   ├── services/recruiter.service.ts
│       │   │   └── models/recruiter.models.ts# RecruiterRequest, RecruiterResponse
│       │   ├── vacantes/                     # CICLO COMPLETO DE VACANTES (RF05)
│       │   │   ├── pages/vacante-admin/      # vacante-admin-list.component.ts (PUT/DELETE)
│       │   │   ├── pages/vacante-form/       # vacante-create-edit.component.ts
│       │   │   ├── services/vacante.service.ts
│       │   │   └── models/vacante.models.ts  # VacanteRequest, VacanteResponse
│       │   └── entrevistas/                  # AGENDAR ENTREVISTAS (RF58)
│       │       ├── pages/entrevista-calendar/# entrevista-calendar.component.ts
│       │       ├── components/agenda-modal/  # agenda-modal.component.ts
│       │       ├── services/entrevista.service.ts
│       │       └── models/entrevista.models.ts# EntrevistaRequest, EntrevistaResponse
│       │
│       ├── freelance/                        # Subdominio: Mercado Freelance y Pagos
│       │   ├── proyectos-catalogo/           # Catálogo público con filtrado en memoria
│       │   │   ├── pages/proyecto-list/      # proyecto-list.component.ts
│       │   │   ├── components/proyecto-card/ # proyecto-card.component.ts (Dumb)
│       │   │   └── services/proyecto-catalog.service.ts (Signal State con computed)
│       │   ├── proyecto-gestion/             # Modificación (RF26) y propuestas
│       │   │   ├── pages/proyecto-edit/      # proyecto-edit.component.ts (PUT si BUSCANDO_TALENTO)
│       │   │   └── services/proyecto-admin.service.ts
│       │   ├── cliente-dashboard/            # Panel cliente con métricas JPA reales (RF51)
│       │   │   ├── pages/cliente-dash/       # cliente-dashboard.component.ts
│       │   │   └── services/cliente.service.ts
│       │   ├── freelancer-finances/          # Finanzas reales del freelancer (RF53)
│       │   │   ├── pages/finances-view/      # freelancer-finances.component.ts
│       │   │   └── services/finances.service.ts
│       │   └── checkout-mercadopago/         # PASARELA MERCADO PAGO (Checkout Pro)
│       │       ├── components/mp-button/     # mp-checkout-button.component.ts
│       │       ├── services/mercadopago.service.ts
│       │       └── models/mercadopago.models.ts# PreferenceResponse
│       │
│       └── bootcamps/                        # Subdominio: Formación y Educación
│           ├── pages/bootcamp-list/          # bootcamp-list.component.ts
│           ├── pages/bootcamp-detail/        # bootcamp-detail.component.ts
│           ├── services/bootcamp.service.ts
│           └── models/bootcamp.models.ts
│
└── environments/
    ├── environment.ts                        # Localhost API URL (http://localhost:8080)
    └── environment.prod.ts                   # Producción URL, Google Client ID, Mercado Pago Key
```

---

## 3. Contratos Tipados TypeScript (Mapeo 1:1 con Backend DTOs)

```typescript
// src/app/core/models/api-contracts.models.ts

// 1. Recruiters (RF22-24)
export interface RecruiterRequestDTO {
  nombre: string;
  apellido: string;
  email: string;
  fechaNacimiento: string; // YYYY-MM-DD
  area: string;
}

export interface RecruiterResponseDTO {
  id: number;
  nombre: string;
  apellido: string;
  email: string;
  area: string;
  empresaId: number;
  nombreEmpresa: string;
}

// 2. Vacantes Laborales (RF05)
export interface VacanteRequestDTO {
  empresaId?: number;
  puesto: string;
  descripcion: string;
  tipoUbicacion: 'REMOTO' | 'HIBRIDO' | 'PRESENCIAL';
  tipoEmpleo: string;
  salarioMinimo: number;
  salarioMaximo: number;
}

export interface VacanteResponseDTO {
  id: number;
  puesto: string;
  descripcion: string;
  tipoUbicacion: string;
  tipoEmpleo: string;
  salarioMinimo: number;
  salarioMaximo: number;
  empresaId: number;
  empresaNombre: string;
}

// 3. Entrevistas (RF58)
export interface EntrevistaRequestDTO {
  postulacionId: number;
  fecha: string; // YYYY-MM-DD
  hora: string;  // HH:mm:ss
  link: string;
}

// 4. Proyectos Freelance (RF26)
export interface ProyectoRequestDTO {
  titulo: string;
  descripcion: string;
  presupuesto: number;
}

export interface ProyectoResponseDTO {
  id: number;
  titulo: string;
  descripcion: string;
  presupuesto: number;
  estado: 'BUSCANDO_TALENTO' | 'EN_PROGRESO' | 'FINALIZADO' | 'CANCELADO';
  clienteId: number;
}

// 5. Métricas Analíticas JPA Reales (RF51, RF53)
export interface ClientDashboardResponseDTO {
  activeProjects: number;
  completedProjects: number;
  totalSpent: number;
  pendingProposals: number;
}

export interface FreelancerFinancesResponseDTO {
  balance: number;
  pending: number;
  withdrawn: number;
}
```
