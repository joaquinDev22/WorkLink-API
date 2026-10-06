# 📑 PLAN DE IMPLEMENTACIÓN Y GUÍA DE API: WORKLINK BACKEND

**Proyecto:** WorkLink Backend (UTN - Programación III)  
**Tecnologías:** Java 21, Spring Boot 3.x, Spring Security (Stateless JWT), Spring Data JPA, MySQL, MapStruct, Swagger/OpenAPI  
**Estado:** ✅ **100% Implementado, Compilado y Verificado** (BUILD SUCCESS, Tests OK)  

---

## 1. Visión y Objetivos Arquitectónicos

WorkLink es una plataforma basada en una arquitectura de **Monolito Modular (Modular Monolith)** dividida en subdominios:
* **Perfil & Seguridad:** Gestión de usuarios, autenticación nativa y federada (Google OAuth2), currículum y portafolio multimedia.
* **Empresas & Vacantes (Enterprise):** Gestión de empresas, recruiters, vacantes laborales, postulaciones y entrevistas.
* **Freelance:** Publicación de proyectos, propuestas, hitos, finanzas, métricas de dashboard y pasarela de cobros con Mercado Pago.
* **Bootcamps:** Formación, instructores, inscripciones, calificaciones y certificados.

---

## 2. Resumen de las 10 Decisiones e Implementaciones Resueltas

| # | Módulo / Requerimiento | Decisión y Estado | Detalle Técnico en Backend |
|:---:|:---|:---:|:---|
| **1** | **Mercado Pago Checkout & Webhook** | ✅ Implementado | SDK `sdk-java:2.1.28`, `MercadoPagoAdapter` aislado fuera de transacción SQL (pool seguro HikariCP), endpoint `/checkout` y webhook `/api/v1/pagos/webhook` público. |
| **2** | **Google OAuth2** | ✅ Implementado | `google-api-client:2.7.0`, `GoogleAuthServiceImpl` con validación criptográfica de `idToken` y endpoint público `POST /api/v1/auth/google` emitiendo JWT nativo. |
| **3** | **Gestión de Recruiters (RF22-24)** | ✅ Implementado | CRUD completo en `RecruiterController` (`/api/v1/empresas/recruiters`) vinculado a la empresa autenticada con validaciones de unicidad de email y mapper MapStruct. |
| **4** | **Modificación y Baja de Vacantes (RF05)** | ✅ Implementado | `PUT /api/v1/vacantes/{id}` (modificación) y `DELETE /api/v1/vacantes/{id}` (baja física) con validación de propiedad de empresa/recruiter. |
| **5** | **Modificación de Proyectos (RF26)** | ✅ Implementado | `PUT /api/v1/freelance/proyectos/{id}` permitiendo edición si el proyecto está en estado `BUSCANDO_TALENTO` y pertenece al cliente. |
| **6** | **Agendar Entrevistas (RF58)** | ✅ Implementado | `POST /api/v1/recruiter/entrevistas` registrando fecha, hora, enlace y postulación asociada. |
| **7** | **Baja de Perfil Propio (RF01)** | ✅ Implementado | `DELETE /api/v1/perfiles/me` aplicando baja lógica (`is_active = false`) en `PerfilEntity`. |
| **8** | **Filtrado de Vacantes y Proyectos** | ✅ Sincerado | Backend entrega listados completos de forma desacoplada; los filtros por facetas se procesan en memoria en el frontend. |
| **9** | **Cero Mocks / Persistencia JPA Real** | ✅ Implementado | `ClientController` y `FreelancerController` conectados a consultas reales de conteo y sumatorias en repositorios JPA MySQL. |
| **10** | **Features Avanzadas Conservadas** | ✅ Preservado | Subida de archivos (CV, foto, banner), habilidades y proyectos de portafolio mantenidos al 100% en `PerfilController`. |

---

## 3. Visualización y Pruebas en Swagger UI

### 3.1 Cómo Levantar el Servidor
En la raíz del proyecto, ejecutar:
```powershell
.\mvnw.cmd spring-boot:run
```

### 3.2 Acceso a la Documentación Interactiva
* **Swagger UI:** `http://localhost:8080/swagger-ui/index.html` (o `http://localhost:8080/swagger-ui.html`)
* **OpenAPI JSON Spec:** `http://localhost:8080/v3/api-docs`

### 3.3 Cómo Probar Rutas Protegidas en Swagger
1. Ir al bloque **Autenticación** y ejecutar `POST /api/v1/auth/login` o `POST /api/v1/auth/google`.
2. Copiar el valor del atributo `token` de la respuesta JSON.
3. Hacer clic en el botón verde superior **Authorize** (con icono de candado).
4. Escribir:
   ```text
   Bearer <PEGA_AQUI_TU_TOKEN>
   ```
5. Presionar **Authorize** y cerrar el modal. Ahora todas las peticiones enviarán la cabecera `Authorization`.

---

## 4. Catálogo Completo de Endpoints

### 4.1 Autenticación (`/api/v1/auth`)
| Método | Endpoint | Rol / Acceso | Descripción |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/auth/register` | `permitAll()` | Registra un nuevo usuario con credenciales y perfil base. |
| `POST` | `/api/v1/auth/login` | `permitAll()` | Inicia sesión tradicional y retorna token JWT. |
| `POST` | `/api/v1/auth/google` | `permitAll()` | Autentica mediante `idToken` de Google y devuelve token JWT. |

### 4.2 Perfiles y Portafolio (`/api/v1/perfiles`)
| Método | Endpoint | Rol / Acceso | Descripción |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/v1/perfiles/getProfile` | Autenticado | Devuelve los datos del perfil del usuario autenticado. |
| `PUT` | `/api/v1/perfiles/updateProfile` | Autenticado | Actualiza la información personal del perfil. |
| `DELETE` | `/api/v1/perfiles/me` | Autenticado | Baja lógica (`is_active = false`) del usuario actual. |
| `POST` | `/api/v1/perfiles/uploadMyCV` | `EMPLOYEE`, `FREELANCER` | Sube el CV en formato binario. |
| `POST` | `/api/v1/perfiles/uploadMyPhoto` | Autenticado | Sube la foto de perfil en formato binario. |
| `POST` | `/api/v1/perfiles/uploadMyBanner` | Autenticado | Sube el banner de portada en formato binario. |
| `POST` | `/api/v1/perfiles/addMySkill` | Autenticado | Agrega una habilidad indicando nombre y nivel. |
| `DELETE` | `/api/v1/perfiles/removeMySkill/{id}` | Autenticado | Elimina una habilidad del perfil. |
| `POST` | `/api/v1/perfiles/addMyProject` | Autenticado | Agrega un proyecto al portafolio personal. |
| `DELETE` | `/api/v1/perfiles/removeMyProject/{id}` | Autenticado | Elimina un proyecto del portafolio personal. |

### 4.3 Empresas y Recruiters (`/api/v1/empresas`)
| Método | Endpoint | Rol / Acceso | Descripción |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/empresas` | `EMPRESA` | Crea el registro de una empresa. |
| `PUT` | `/api/v1/empresas/{id}` | `EMPRESA` | Modifica los datos de una empresa existente. |
| `GET` | `/api/v1/empresas/{id}` | `EMPRESA` | Consulta detalles de una empresa por ID. |
| `GET` | `/api/v1/empresas/dashboard` | `EMPRESA` | Métricas estadísticas para el panel de la empresa. |
| `POST` | `/api/v1/empresas/recruiters` | `EMPRESA` | Da de alta un nuevo recruiter asociado a la empresa. |
| `GET` | `/api/v1/empresas/recruiters` | `EMPRESA` | Lista todos los recruiters pertenecientes a la empresa. |
| `GET` | `/api/v1/empresas/recruiters/{id}` | `EMPRESA` | Consulta los datos de un recruiter por ID. |
| `PUT` | `/api/v1/empresas/recruiters/{id}` | `EMPRESA` | Actualiza la información de un recruiter propio. |
| `DELETE` | `/api/v1/empresas/recruiters/{id}` | `EMPRESA` | Elimina y desvincula a un recruiter de la empresa. |

### 4.4 Vacantes Laborales y Postulaciones
| Método | Endpoint | Rol / Acceso | Descripción |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/v1/vacantes` | `permitAll()` | Consulta el catálogo público completo de vacantes. |
| `GET` | `/api/v1/vacantes/{id}` | `permitAll()` | Consulta una vacante laboral por ID. |
| `GET` | `/api/v1/vacantes/me` | `EMPRESA`, `RECRUITER` | Lista las vacantes publicadas por la empresa autenticada. |
| `POST` | `/api/v1/vacantes` | `EMPRESA`, `RECRUITER` | Publica una vacante indicando `empresaId`. |
| `POST` | `/api/v1/vacantes/me` | `EMPRESA`, `RECRUITER` | Publica una vacante asociada automáticamente a la empresa actual. |
| `PUT` | `/api/v1/vacantes/{id}` | `EMPRESA`, `RECRUITER` | Modifica los campos de una vacante propia. |
| `DELETE` | `/api/v1/vacantes/{id}` | `EMPRESA`, `RECRUITER` | Da de baja/elimina una vacante laboral propia. |
| `POST` | `/api/v1/vacantes/{id}/postulaciones` | Autenticado | Postula a un empleado con `employeeId`, mensaje y CV. |
| `POST` | `/api/v1/vacantes/{id}/postulaciones/me` | Autenticado | Postula automáticamente al candidato en sesión. |
| `GET` | `/api/v1/vacantes/{id}/postulaciones` | Autenticado | Consulta la lista de postulantes a una vacante. |

### 4.5 Entrevistas Laborales (`/api/v1/recruiter/entrevistas`)
| Método | Endpoint | Rol / Acceso | Descripción |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/v1/recruiter/entrevistas` | `EMPRESA`, `RECRUITER` | Lista las entrevistas programadas con datos del candidato y vacante. |
| `POST` | `/api/v1/recruiter/entrevistas` | `EMPRESA`, `RECRUITER` | Agenda una nueva entrevista asociada a una postulación. |

### 4.6 Proyectos Freelance (`/api/v1/freelance/proyectos`)
| Método | Endpoint | Rol / Acceso | Descripción |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/v1/freelance/proyectos` | `permitAll()` | Lista todos los proyectos freelance activos en la plataforma. |
| `GET` | `/api/v1/freelance/proyectos/{id}` | `permitAll()` | Consulta el detalle de un proyecto por ID. |
| `POST` | `/api/v1/freelance/proyectos` | `CLIENTE` | Publica un proyecto especificando `clienteId`. |
| `POST` | `/api/v1/freelance/proyectos/me` | `CLIENTE` | Publica un proyecto asociado al cliente autenticado. |
| `GET` | `/api/v1/freelance/proyectos/me/publicados` | `CLIENTE` | Lista proyectos publicados por el cliente en sesión. |
| `GET` | `/api/v1/freelance/proyectos/me/asignados` | `FREELANCER` | Lista proyectos asignados al freelancer en sesión. |
| `PUT` | `/api/v1/freelance/proyectos/{id}` | `CLIENTE` | Modifica datos del proyecto (si está en `BUSCANDO_TALENTO`). |
| `PUT` | `/api/v1/freelance/proyectos/{id}/cancelar`| `CLIENTE` | Cancela un proyecto freelance publicado. |
| `POST` | `/api/v1/freelance/proyectos/{id}/postulaciones` | `FREELANCER` | Envía una propuesta para trabajar en el proyecto. |
| `POST` | `/api/v1/freelance/proyectos/{id}/postulaciones/me` | `FREELANCER` | Envía propuesta vinculada al freelancer autenticado. |
| `GET` | `/api/v1/freelance/proyectos/{id}/postulaciones` | Autenticado | Lista las propuestas recibidas para el proyecto. |

### 4.7 Pasarela de Pagos y Mercado Pago
| Método | Endpoint | Rol / Acceso | Descripción |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/v1/freelance/proyectos/{id}/pagos/checkout` | `CLIENTE` | Genera preferencia Checkout Pro y devuelve `initPoint`. |
| `POST` | `/api/v1/freelance/proyectos/{id}/pagos` | `CLIENTE` | Registra un pago directo interno en base de datos. |
| `GET` | `/api/v1/freelance/proyectos/{id}/pagos` | Autenticado | Lista el historial de pagos de un proyecto. |
| `POST` | `/api/v1/pagos/webhook` | `permitAll()` | Webhook público receptor de notificaciones de pago de Mercado Pago. |

### 4.8 Dashboard de Cliente y Finanzas de Freelancer (Persistencia Real)
| Método | Endpoint | Rol / Acceso | Descripción |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/v1/client/dashboard` | `CLIENTE` | Métricas reales: proyectos activos, finalizados, total gastado y propuestas. |
| `GET` | `/api/v1/client/proyectos` | `CLIENTE` | Lista real de proyectos del cliente. |
| `GET` | `/api/v1/freelancer/finances` | `FREELANCER` | Resumen real: saldo disponible cobrado y montos pendientes. |
| `GET` | `/api/v1/freelancer/transactions`| `FREELANCER` | Historial de transacciones de pagos reales recibidos. |

### 4.9 Bootcamps y Formación
| Método | Endpoint | Rol / Acceso | Descripción |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/v1/bootcamps` | `permitAll()` | Lista todos los bootcamps. |
| `GET` | `/api/v1/bootcamps/disponibles` | `permitAll()` | Lista bootcamps con cupos disponibles. |
| `POST` | `/api/v1/bootcamps` | `INSTRUCTOR` | Publica un nuevo bootcamp. |
| `POST` | `/api/v1/inscripciones` | Autenticado | Inscribe a un alumno a un bootcamp. |
| `POST` | `/api/v1/certificados` | `INSTRUCTOR` | Emite un certificado digital. |

---

## 5. Guía de Integración para el Frontend (Angular / TypeScript)

### 5.1 Interceptor HTTP de Autenticación JWT
Permite inyectar de forma transparente la cabecera `Authorization: Bearer <token>` en todas las peticiones salientes:

```typescript
// src/app/core/interceptors/jwt.interceptor.ts
import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    const token = localStorage.getItem('token');
    if (token) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
    }
    return next.handle(request);
  }
}
```

---

### 5.2 Ejemplos de Consumo en Servicios del Frontend

#### 1. Login con Google Identity Services
```typescript
loginConGoogle(idTokenGoogle: string) {
  return this.http.post<{ token: string, nombreUsuario: string, rol: string }>(
    'http://localhost:8080/api/v1/auth/google',
    { idToken: idTokenGoogle }
  ).subscribe(res => {
    localStorage.setItem('token', res.token);
    localStorage.setItem('rol', res.rol);
    console.log('Sesión iniciada con éxito:', res.nombreUsuario);
  });
}
```

#### 2. Checkout Pro de Mercado Pago (Redirección Externa)
```typescript
iniciarPagoProyecto(proyectoId: number, monto: number) {
  const backUrl = window.location.origin; // http://localhost:4200
  return this.http.post<{ initPoint: string, sandboxInitPoint: string }>(
    `http://localhost:8080/api/v1/freelance/proyectos/${proyectoId}/pagos/checkout?monto=${monto}&backUrl=${backUrl}`,
    {}
  ).subscribe(res => {
    // Redirigir al usuario al entorno seguro de Mercado Pago
    window.location.href = res.initPoint;
  });
}
```

#### 3. Empresa: Crear, Listar y Eliminar Recruiters
```typescript
// Alta de recruiter
crearRecruiter(dto: { nombre: string; apellido: string; email: string; fechaNacimiento: string; area: string }) {
  return this.http.post('http://localhost:8080/api/v1/empresas/recruiters', dto);
}

// Listado de recruiters de la empresa
obtenerMisRecruiters() {
  return this.http.get<any[]>('http://localhost:8080/api/v1/empresas/recruiters');
}

// Baja de recruiter
eliminarRecruiter(id: number) {
  return this.http.delete(`http://localhost:8080/api/v1/empresas/recruiters/${id}`);
}
```

#### 4. Vacantes: Modificar y Dar de Baja
```typescript
// Modificar vacante (PUT)
modificarVacante(id: number, vacanteData: any) {
  return this.http.put(`http://localhost:8080/api/v1/vacantes/${id}`, vacanteData);
}

// Eliminar vacante (DELETE)
eliminarVacante(id: number) {
  return this.http.delete(`http://localhost:8080/api/v1/vacantes/${id}`);
}
```

#### 5. Proyectos Freelance: Modificar Datos
```typescript
modificarProyecto(id: number, proyectoData: any) {
  return this.http.put(`http://localhost:8080/api/v1/freelance/proyectos/${id}`, proyectoData);
}
```

#### 6. Agendar Entrevista
```typescript
agendarEntrevista(postulacionId: number, fecha: string, hora: string, link: string) {
  return this.http.post('http://localhost:8080/api/v1/recruiter/entrevistas', {
    postulacionId,
    fecha, // Formato "YYYY-MM-DD"
    hora,  // Formato "HH:mm:ss"
    link   // ej. "https://meet.google.com/xyz-abcd-jkl"
  });
}
```

#### 7. Baja Lógica de Perfil Propio
```typescript
desactivarMiCuenta() {
  return this.http.delete('http://localhost:8080/api/v1/perfiles/me').subscribe(() => {
    localStorage.clear();
    window.location.href = '/login';
  });
}
```

#### 8. Dashboard de Cliente y Finanzas de Freelancer
```typescript
// Métricas analíticas reales del cliente
obtenerDashboardCliente() {
  return this.http.get<{ activeProjects: number; completedProjects: number; totalSpent: number; pendingProposals: number }>(
    'http://localhost:8080/api/v1/client/dashboard'
  );
}

// Finanzas reales del freelancer
obtenerFinanzasFreelancer() {
  return this.http.get<{ balance: number; pending: number; withdrawn: number }>(
    'http://localhost:8080/api/v1/freelancer/finances'
  );
}
```

#### 9. Filtrado de Vacantes y Proyectos en Memoria (Frontend)
Para no saturar el backend con endpoints de filtrado parametrizado, el componente de Angular realiza la búsqueda y filtrado de forma reactiva:
```typescript
export class VacantesComponent implements OnInit {
  vacantes: any[] = [];
  vacantesFiltradas: any[] = [];

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.http.get<any[]>('http://localhost:8080/api/v1/vacantes').subscribe(data => {
      this.vacantes = data;
      this.vacantesFiltradas = data;
    });
  }

  filtrarPorModalidad(modalidad: string) {
    if (!modalidad) {
      this.vacantesFiltradas = this.vacantes;
    } else {
      this.vacantesFiltradas = this.vacantes.filter(v => v.tipoUbicacion === modalidad);
    }
  }

  filtrarPorPuesto(termino: string) {
    const q = termino.toLowerCase();
    this.vacantesFiltradas = this.vacantes.filter(v => 
      v.puesto.toLowerCase().includes(q) || v.descripcion.toLowerCase().includes(q)
    );
  }
}
```
