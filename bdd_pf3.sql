DROP DATABASE IF EXISTS Bd_Proyecto_Final;
CREATE DATABASE Bd_Proyecto_Final CHARSET utf8mb4;
USE Bd_Proyecto_Final;

-- ========================================= --
--                 Entidades                 --
-- ========================================= --
CREATE TABLE Rol(
    rol_id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_rol VARCHAR(50) UNIQUE NOT NULL
); -- Ya definida en una java.class por JoaquinDev22

CREATE TABLE Perfil(
	perfil_id INT AUTO_INCREMENT PRIMARY KEY,
    foto_perfil VARCHAR(150),
	usuario VARCHAR(50) UNIQUE NOT NULL,
	email VARCHAR(50) UNIQUE NOT NULL,
	password VARCHAR(255) NOT NULL,
	rol_id INT NOT NULL,
	FOREIGN KEY(rol_id) REFERENCES Rol(rol_id)
); -- Ya definida en una java.class por Gabrielramirez2003

CREATE TABLE Empresa(
	empresa_id INT AUTO_INCREMENT PRIMARY KEY,
	nombre VARCHAR(100) NOT NULL,
	descripcion VARCHAR(255) NOT NULL,
	sitio_web VARCHAR(100) NOT NULL,
	ubicacion VARCHAR(100) NOT NULL,
	perfil_id INT NOT NULL,
	FOREIGN KEY(perfil_id)REFERENCES Perfil(perfil_id)
); -- Ya definida en una java.class por Gabrielramirez2003

CREATE TABLE Recruiter(
	recruiter_id INT AUTO_INCREMENT PRIMARY KEY,
	nombre VARCHAR(50) NOT NULL,
	apellido VARCHAR(50) NOT NULL,
	email VARCHAR(50) NOT NULL,
	fecha_nacimiento DATE NOT NULL,
	area VARCHAR(50) NOT NULL,
	empresa_id INT NOT NULL,
	perfil_id INT NOT NULL,
	FOREIGN KEY(perfil_id)REFERENCES Perfil(perfil_id),
	FOREIGN KEY(empresa_id)REFERENCES Empresa(empresa_id)
); -- Ya definida en una java.class por JoaquinDev22

CREATE TABLE Employee(
	employee_id INT AUTO_INCREMENT PRIMARY KEY,
	nombre VARCHAR(50) NOT NULL, 
	apellido VARCHAR(50) NOT NULL,
	email VARCHAR(50) NOT NULL,
	fecha_nacimiento DATE NOT NULL,
    freelance BOOLEAN,
    enterprise BOOLEAN,
	perfil_id INT NOT NULL,
	FOREIGN KEY(perfil_id)REFERENCES Perfil(perfil_id)
); -- Ya definida en una java.class por JoaquinDev22

CREATE TABLE Cliente_Particular(
	cliente_particular_id INT AUTO_INCREMENT PRIMARY KEY,
	nombre VARCHAR(50) NOT NULL,
	apellido VARCHAR(50) NOT NULL,
	email VARCHAR(50) NOT NULL,
	fecha_nacimiento DATE NOT NULL,
	telefono VARCHAR(50) NOT NULL,
	pais VARCHAR(100) NOT NULL,
	perfil_id INT NOT NULL,
	FOREIGN KEY(perfil_id)REFERENCES Perfil(perfil_id)
); -- Ya definida en una java.class por JoaquinDev22

CREATE TABLE Experiencia(
	experiencia_id INT AUTO_INCREMENT PRIMARY KEY,
	titulo VARCHAR(100) NOT NULL,
	descripcion VARCHAR(255) NOT NULL,
	fecha_inicio DATE NOT NULL,
	fecha_fin DATE,
	employee_id INT NOT NULL,
	FOREIGN KEY(employee_id) REFERENCES Employee(employee_id)
); -- Ya definida en una java.class por JoaquinDev22

CREATE TABLE Proyecto(
	proyecto_id INT AUTO_INCREMENT PRIMARY KEY,
	area_proyecto VARCHAR(50) NOT NULL,
	descripcion VARCHAR(255) NOT NULL,
	fecha_creacion DATE NOT NULL,
    presupuesto_max DECIMAL(10,2),
    presupuesto_min DECIMAL(10,2),
	cliente_id INT NOT NULL,
	FOREIGN KEY(cliente_id)REFERENCES Cliente_Particular(cliente_particular_id)
); -- Ya definida en una java.class por JoaquinDev22

CREATE TABLE Postulacion_Proyecto(
    post_proy_id INT AUTO_INCREMENT PRIMARY KEY,
    descripcion VARCHAR(255) NOT NULL,
    valor_propuesto DECIMAL(10,2),
    fecha_postulacion DATE NOT NULL,
    employee_id INT NOT NULL,
    proyecto_id INT NOT NULL,
    FOREIGN KEY (employee_id) REFERENCES Employee(employee_id),
    FOREIGN KEY (proyecto_id) REFERENCES Proyecto(proyecto_id)
); -- Ya definida en una java.class por JoaquinDev22

CREATE TABLE Calificacion_Proyecto(
    calificacion_id INT AUTO_INCREMENT PRIMARY KEY,
    puntaje INT NOT NULL,
    comentario VARCHAR(255) NOT NULL,
    fecha_comentario DATE NOT NULL,
    proyecto_id INT NOT NULL,
    FOREIGN KEY (proyecto_id) REFERENCES Proyecto(proyecto_id)
);-- Ya definida en una java.class por JoaquinDev22

CREATE TABLE Calificacion_Usuario(
    calificacion_usuario_id INT AUTO_INCREMENT PRIMARY KEY,
    calificacion_id INT,
    calificacion_bootcamp_id INT,
    perfil_id INT NOT NULL,
    FOREIGN KEY (calificacion_id) REFERENCES Calificacion_Proyecto(calificacion_id),
    FOREIGN KEY (calificacion_bootcamp_id) REFERENCES Calificacion_Bootcamp(calificacion_id),
    FOREIGN KEY (perfil_id) REFERENCES Perfil(perfil_id)
);
CREATE TABLE Habilidad(
	habilidad_id INT AUTO_INCREMENT PRIMARY KEY,
	nombre_habilidad VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE Employee_Habilidad(
	employee_id INT NOT NULL,
	habilidad_id INT NOT NULL,
	nivel INT NOT NULL DEFAULT 0,
	PRIMARY KEY(employee_id, habilidad_id),
	FOREIGN KEY(employee_id) REFERENCES Employee(employee_id),
	FOREIGN KEY(habilidad_id) REFERENCES Habilidad(habilidad_id)
);

CREATE TABLE Vacante(
	vacante_id INT AUTO_INCREMENT PRIMARY KEY,
	puesto VARCHAR(50) NOT NULL,
	descripcion VARCHAR(255) NOT NULL,
	fecha_publicacion DATE NOT NULL,
	fecha_fin DATE NOT NULL,
	empresa_id INT NOT NULL,
	FOREIGN KEY(empresa_id)REFERENCES Empresa(empresa_id)
);

CREATE TABLE Postulacion(
	postulacion_id INT AUTO_INCREMENT PRIMARY KEY,
	fecha_postulacion DATE NOT NULL,
	estado VARCHAR(30) NOT NULL,
	mensaje VARCHAR(255) NOT NULL,
	proyecto_id INT,
	vacante_id INT,
	FOREIGN KEY(proyecto_id)REFERENCES Proyecto(proyecto_id),
	FOREIGN KEY(vacante_id)REFERENCES Vacante(vacante_id)
);

CREATE TABLE Vacante_Habilidad(
	vacante_id INT NOT NULL,
	habilidad_id INT NOT NULL,
	PRIMARY KEY(vacante_id, habilidad_id),
	FOREIGN KEY(vacante_id) REFERENCES Vacante(vacante_id),
	FOREIGN KEY(habilidad_id) REFERENCES Habilidad(habilidad_id)
);

CREATE TABLE Postulacion_Postulante(
	employee_id INT NOT NULL,
	postulacion_id INT NOT NULL,
	PRIMARY KEY(employee_id,postulacion_id),
	FOREIGN KEY(employee_id) REFERENCES Employee(employee_id),
	FOREIGN KEY(postulacion_id) REFERENCES Postulacion(postulacion_id)
);

CREATE TABLE Portfolio(
	portfolio_id INT AUTO_INCREMENT PRIMARY KEY,
	github VARCHAR(150) NOT NULL,
	linkedin VARCHAR(150) NOT NULL,
	cv_url VARCHAR(150) NOT NULL,
	descripcion VARCHAR(255) NOT NULL,
	employee_id INT NOT NULL,
	FOREIGN KEY(employee_id) REFERENCES Employee(employee_id)
);
-- ======================= --
--          PAGOS          --
-- ======================= --

CREATE TABLE Pagos(
	pago_id INT AUTO_INCREMENT PRIMARY KEY,
	moneda ENUM('USD','PESOS'),
	monto INT NOT NULL,
	medio_de_pago ENUM('TARJETA', 'BILLETERA_VIRTUAL'),
	proyecto_id INT NOT NULL,
	FOREIGN KEY(proyecto_id) REFERENCES Proyecto(proyecto_id)
);

CREATE TABLE Metodos_Cobro_Freelancer(
	metodos_cobro_freelancer_id INT AUTO_INCREMENT PRIMARY KEY,
	titular VARCHAR(100) NOT NULL,
	dni VARCHAR(50) UNIQUE NOT NULL,
	entidad_bancaria ENUM('BANCO', 'BILLETERA_VIRTUAL'),
	numero_cuenta VARCHAR(100) NOT NULL,
	employee_id INT NOT NULL,
	FOREIGN KEY(employee_id)REFERENCES Employee(employee_id)
);

CREATE TABLE Metodos_Pago_Cliente(
	metodos_pago_cliente_id INT AUTO_INCREMENT PRIMARY KEY,
	titular VARCHAR(50) NOT NULL,
	dni VARCHAR(50) UNIQUE NOT NULL,
	entidad_bancaria ENUM('BANCO', 'BILLETERA_VIRTUAL'),
	cvu VARCHAR(50) UNIQUE NOT NULL,
	tipo_tarjeta ENUM('DEBITO','CREDITO'),
	empresa_tarjeta ENUM('VISA','MASTERCARD'),
	numero_tarjeta VARCHAR(50) UNIQUE NOT NULL,
	cliente_id INT NOT NULL,
	FOREIGN KEY(cliente_id)REFERENCES Cliente_Particular(cliente_particular_id)
);

CREATE TABLE Pagos_Cliente_Freelancer(
	pagos_cliente_freelancer_id INT AUTO_INCREMENT PRIMARY KEY,
	pago_id INT NOT NULL,
	metodos_cobro_freelancer_id INT NOT NULL,
	metodos_pago_cliente_id INT NOT NULL,
	FOREIGN KEY(pago_id) REFERENCES Pagos(pago_id),
	FOREIGN KEY(metodos_cobro_freelancer_id) REFERENCES Metodos_Cobro_Freelancer(metodos_cobro_freelancer_id),
	FOREIGN KEY(metodos_pago_cliente_id) REFERENCES Metodos_Pago_Cliente(metodos_pago_cliente_id)
);

-- =============================== --
--       BOOTCAMP                  --
-- =============================== --

CREATE TABLE Instructor (
    instructor_id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    titulo VARCHAR(100) NOT NULL,
    presentacion VARCHAR(255) NOT NULL,
    tipo ENUM('PARTICULAR', 'EMPRESA'),
    perfil_id INT NOT NULL,
	empresa_id INT,
    FOREIGN KEY(perfil_id)REFERENCES Perfil(perfil_id),
	FOREIGN KEY(empresa_id) REFERENCES Empresa(empresa_id)
); -- Ya definida en una java.class por Yamils06

CREATE TABLE Bootcamp(
    bootcamp_id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    modalidad ENUM('VIRTUAL', 'PRESENCIAL'),
    tipo ENUM('PARTICULAR', 'EMPRESA'),
    cupos INT NOT NULL,
	instructor_id INT NOT NULL,
	empresa_id INT,
    FOREIGN KEY(instructor_id) REFERENCES Instructor(instructor_id),
	FOREIGN KEY(empresa_id) REFERENCES Empresa(empresa_id)
); -- Ya definida en una java.class por Yamils06

CREATE TABLE Inscripcion_Bootcamp(
    inscripcion_id INT AUTO_INCREMENT PRIMARY KEY,
    fecha_inscripcion DATE  NOT NULL,
    estado ENUM('INSCRIPTO','PENDIENTE','CANCELADO', 'COMPLETADO'),
    bootcamp_id INT NOT NULL,
    employee_id INT NOT NULL,
    FOREIGN KEY(bootcamp_id)REFERENCES Bootcamp(bootcamp_id),
	FOREIGN KEY(employee_id) REFERENCES Employee(employee_id)
); -- Ya definida en una java.class por Yamils06

CREATE TABLE Calificacion_Bootcamp(
    calificacion_id INT AUTO_INCREMENT PRIMARY KEY,
    puntaje INT NOT NULL,
    comentario VARCHAR(255) NOT NULL,
    fecha_comentario DATE NOT NULL,
    bootcamp_id INT NOT NULL,
    employee_id INT NOT NULL,
    FOREIGN KEY(bootcamp_id) REFERENCES Bootcamp(bootcamp_id),
    FOREIGN KEY(employee_id) REFERENCES Employee(employee_id)
); -- Ya definida en una java.class por Yamils06

CREATE TABLE Certificado_Bootcamp (
    certificado_id INT AUTO_INCREMENT PRIMARY KEY,
    fecha_emision DATE NOT NULL,
    codigo_certificado VARCHAR(100) NOT NULL UNIQUE,
    bootcamp_id INT NOT NULL,
    employee_id INT NOT NULL,
    FOREIGN KEY (bootcamp_id) REFERENCES Bootcamp(bootcamp_id),
    FOREIGN KEY (employee_id) REFERENCES Employee(employee_id)
); -- Ya definida en una java.class por Yamils06

-- ===================== --
--       AUDITORIA       --
-- ===================== --

CREATE TABLE Auditoria_Operaciones (
    auditoria_id INT AUTO_INCREMENT PRIMARY KEY,
    tabla_afectada VARCHAR(100) NOT NULL,
    operacion VARCHAR(50) NOT NULL,
    id_registro_afectado INT NOT NULL,
    usuario VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    fecha_operacion DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Auditoria_Errores (
    auditoria_error_id INT AUTO_INCREMENT PRIMARY KEY,
    procedimiento VARCHAR(100) NOT NULL,
    operacion VARCHAR(100) NOT NULL,
    mensaje_error VARCHAR(255) NOT NULL,
    fecha_error DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- =====================
-- ROLES
-- =====================
INSERT INTO Rol (nombre_rol) VALUES
('ADMIN'),
('EMPLOYEE'),
('RECRUITER'),
('CLIENTE'),
('EMPRESA'),
('INSTRUCTOR');

-- =====================
-- PERFILES
-- =====================
INSERT INTO Perfil (usuario,email,password,rol_id) VALUES
('empresaTech','empresa@tech.com','$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HCGKK2TfBhlLc59NgaEGC',5),
('recruiterJuan','juan@tech.com','$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HCGKK2TfBhlLc59NgaEGC',3),
('freelancerAna','ana@gmail.com','$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HCGKK2TfBhlLc59NgaEGC',2),
('freelancerPedro','pedro@gmail.com','$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HCGKK2TfBhlLc59NgaEGC',2),
('clienteCarlos','carlos@gmail.com','$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HCGKK2TfBhlLc59NgaEGC',4),
('instEmpresa','instempresa@gmail.com','$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HCGKK2TfBhlLc59NgaEGC',6),
('instParticular','instparticular@gmail.com','$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HCGKK2TfBhlLc59NgaEGC',6);

-- =====================
-- EMPRESA (perfil_id = 1)
-- =====================
INSERT INTO Empresa (nombre,descripcion,sitio_web,ubicacion,perfil_id) VALUES 
('Tech Solutions', 'Empresa de desarrollo de software', 'https://techsolutions.com', 'Buenos Aires', 1);

-- =====================
-- RECRUITER (empresa_id = 1, perfil_id = 2)
-- =====================
INSERT INTO Recruiter (nombre,apellido,email,fecha_nacimiento,area,empresa_id,perfil_id) VALUES 
('Juan','Perez','juan@tech.com','1990-05-15','RRHH',1,2);

-- =====================
-- EMPLOYEES (perfil_id = 3, 4)
-- =====================
INSERT INTO Employee (nombre,apellido,email,fecha_nacimiento,freelance,enterprise,perfil_id) VALUES
('Ana','Gomez','ana@gmail.com','1998-08-20',TRUE,FALSE,3),
('Pedro','Lopez','pedro@gmail.com','1996-04-10',TRUE,TRUE,4);

-- =====================
-- CLIENTE PARTICULAR (perfil_id = 5)
-- =====================
INSERT INTO Cliente_Particular (nombre,apellido,email,fecha_nacimiento,telefono,pais,perfil_id) VALUES
('Carlos','Fernandez','carlos@gmail.com','1985-07-12','2235123456','Argentina',5);

-- =====================
-- EXPERIENCIA
-- =====================
INSERT INTO Experiencia (titulo,descripcion,fecha_inicio,fecha_fin,employee_id) VALUES
('Java Developer','Backend en Spring','2022-01-01','2024-01-01',1),
('React Developer','Frontend en React','2021-06-01','2023-12-01',2);

-- =====================
-- PROYECTO (cliente_id = 1)
-- =====================
INSERT INTO Proyecto (area_proyecto,descripcion,fecha_creacion,cliente_id) VALUES
('Desarrollo Web','Sistema de gestión PyME',CURDATE(),1);

-- =====================
-- HABILIDADES
-- =====================
INSERT INTO Habilidad (nombre_habilidad) VALUES
('Java'),
('Spring Boot'),
('React'),
('MySQL');

-- =====================
-- EMPLOYEE_HABILIDAD (employee_id, habilidad_id, nivel)
-- =====================
INSERT INTO Employee_Habilidad (employee_id,habilidad_id,nivel) VALUES
(1,1,8),
(1,2,7),
(1,4,8),
(2,3,9),
(2,4,6);

-- =====================
-- VACANTE (empresa_id = 1)
-- =====================
INSERT INTO Vacante (puesto,descripcion,fecha_publicacion,fecha_fin,empresa_id) VALUES
('Senior Java Developer','Desarrollo backend para plataforma web',CURDATE(),DATE_ADD(CURDATE(), INTERVAL 30 DAY),1);

-- =====================
-- VACANTE_HABILIDAD (vacante_id = 1, habilidad_id)
-- =====================
INSERT INTO Vacante_Habilidad (vacante_id,habilidad_id) VALUES
(1,1),
(1,2),
(1,4);

-- =====================
-- POSTULACION 
-- (proyecto_id = 1, vacante_id = NULL) y (proyecto_id = NULL, vacante_id = 1)
-- =====================
INSERT INTO Postulacion (fecha_postulacion,estado,mensaje,proyecto_id,vacante_id) VALUES
(CURDATE(),'PENDIENTE','Interesada en el proyecto',1,NULL),
(CURDATE(),'PENDIENTE','Me postulo a la vacante',NULL,1);

-- =====================
-- POSTULACION_POSTULANTE
-- =====================
INSERT INTO Postulacion_Postulante (employee_id,postulacion_id) VALUES 
(1,1), -- Ana se postula al proyecto
(2,2); -- Pedro se postula a la vacante

-- =====================
-- PORTFOLIO
-- =====================
INSERT INTO Portfolio (github,linkedin,cv_url,descripcion,employee_id) VALUES
('github.com/ana','linkedin.com/ana','cv.com/ana','Backend Dev',1);

-- =====================
-- PAGOS (proyecto_id = 1)
-- =====================
INSERT INTO Pagos (moneda,monto,medio_de_pago,proyecto_id) VALUES 
('USD',500,'TARJETA',1);

-- =====================
-- METODOS COBRO FREELANCER (employee_id = 1)
-- =====================
INSERT INTO Metodos_Cobro_Freelancer (titular,dni,entidad_bancaria,numero_cuenta,employee_id) VALUES
('Ana Gomez','40111222','BANCO','123456789',1);

-- =====================
-- METODOS PAGO CLIENTE (cliente_id = 1)
-- =====================
INSERT INTO Metodos_Pago_Cliente (titular,dni,entidad_bancaria,cvu,tipo_tarjeta,empresa_tarjeta,numero_tarjeta,cliente_id) VALUES
('Carlos Fernandez','30111222','BILLETERA_VIRTUAL','00000031000','CREDITO','VISA','41111111',1);

-- =====================
-- PAGOS CLIENTE FREELANCER (pago_id=1, cobro=1, pago=1)
-- =====================
INSERT INTO Pagos_Cliente_Freelancer (pago_id,metodos_cobro_freelancer_id,metodos_pago_cliente_id) VALUES
(1,1,1);

-- =====================
-- INSTRUCTOR (perfil_id = 6, 7)
-- =====================
INSERT INTO Instructor (nombre,titulo,presentacion,tipo,perfil_id,empresa_id) VALUES
('Martin Rodriguez','Java Architect','Experto en backend','EMPRESA',6,1),
('Lucia Torres','Full Stack','Especialista en MERN','PARTICULAR',7,NULL);

-- =====================
-- BOOTCAMP (instructor_id = 1, 2)
-- =====================
INSERT INTO Bootcamp (titulo,descripcion,fecha_inicio,fecha_fin,modalidad,tipo,cupos,instructor_id,empresa_id) VALUES
('Java Spring Boot','Bootcamp empresa','2026-07-01','2026-08-30','VIRTUAL','EMPRESA',30,1,1),
('React Cero','Bootcamp particular','2026-09-01','2026-10-01','VIRTUAL','PARTICULAR',50,2,NULL);

-- =====================
-- INSCRIPCION BOOTCAMP (bootcamp_ID = 1, employee_id = 1)
-- =====================
INSERT INTO Inscripcion_Bootcamp (fecha_inscripcion,estado,bootcamp_ID,employee_id) VALUES 
(CURDATE(),'INSCRIPTO',1,1);

-- =====================
-- CALIFICACION BOOTCAMP
-- =====================
INSERT INTO Calificacion_Bootcamp (puntaje,comentario,fecha_comentario,bootcamp_id,employee_id) VALUES
(9,'Excelente material didactico',CURDATE(),1,1);

-- =====================
-- CALIFICACION PROYECTO (proyecto_id=1, freelancer=1, recruiter=1)
-- =====================
INSERT INTO Calificacion_Proyecto (puntaje,comentario,fecha_comentario,proyecto_id) VALUES
(10,'Gran trabajo',CURDATE(),1);