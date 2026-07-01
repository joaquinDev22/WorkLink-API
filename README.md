# WorkLink
Plataforma de gestion de bootcamps, ofertas de trabajo y proyectos freelance

## Descripción

WorkLink es una plataforma diseñada para centralizar servicios de empleo, educación y contratación freelance en un único lugar.
Su objetivo es optimizar la información publicada, ofreciendo un entorno enfocado exclusivamente en oportunidades laborales y formativas,
evitando la sobrecarga de contenido irrelevante presente en otras plataformas.

Además, WorkLink busca impulsar la visibilidad de nuevos talentos, permitiéndoles crear y exhibir su portafolio profesional.
De esta manera, los usuarios pueden demostrar sus habilidades, obtener experiencia en proyectos freelance y
ser contratados tanto por empresas como por clientes particulares.


## Objetivo

Facilitar la conexión entre empresas, reclutadores, instructores, postulantes,
freelancers y clientes particulares mediante una plataforma moderna, intuitiva y especializada.


## Funcionalidades Principales
-- Autenticacion y Seguridad --

.Registro de usuarios
.Inicio de sesion mediante JWT
.Gestion de roles y permisos
.Contraseñas encriptadas con spring Security

-- Gestion de Usuarios --

.Postulantes
.Empresas
.Reclutadores
.Instructores
.Clientes particulares
.Freelancers

-- Gestion de Bootcamps --

.Creación y administración de bootcamps
.Asociación con empresas o clientes particulares
.Gestión de instructores participantes

--Gestion de Vacantes --

.Publicación de ofertas laborales
.Postulación de candidatos
.Seguimiento de vacantes

-- Gestion de Proyectos Freelance --

Publicación de proyectos
Contratación de freelancers
Seguimiento de propuestas


## Arquitectura Del Proyecto
-- BACKEND --

.Java 21
.Spring Boot
.Spring Security
.Spring Data JPA
.JWT
.MySQL
.Maven

-- FRONTEND --
.React
.Vite
.Netlify
.TypeScript
.Tailwind CSS
.Visual Studio Code


## Base de Datos
-- Entidades Principales --
.Perfil
.Empresa
.Reclutador
.Instructor
.Postulante
.Freelancer
.Cliente Particular
.Bootcamp
.Vacante
.Proyecto
.Auditoría


## Endpoints Principales

-- Autenticación --

POST /api/auth/register
POST /api/auth/login

-- Bootcamps --

GET /api/v1/bootcamps
POST /api/v1/bootcamps
PUT /api/v1/bootcamps/{id}
DELETE /api/v1/bootcamps/{id}

-- Vacantes --

GET /api/v1/vacantes
POST /api/v1/vacantes

-- Proyectos --

GET /api/v1/proyectos
POST /api/v1/proyectos


## Integrantes 
. NIEVA JOAQUIN (J0aking21/ JoaquinDev22)
. RAMIREZ GABRIEL (Gabrielramirez2003)
. SAFI YAMIL (Yamils06)

## Objetivo Academico

Proyecto desarrollado como trabajo final de la materia Programación III de la Universidad Tecnológica Nacional (UTN).

## Repositorio

-- FRONTEND --
https://github.com/joaquinDev22/Proyecto-Final-Front-End
