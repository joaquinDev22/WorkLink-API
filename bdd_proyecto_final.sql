DROP DATABASE IF EXISTS Bd_Proyecto_Final;
CREATE DATABASE Bd_Proyecto_Final CHARSET utf8mb4;
USE Bd_Proyecto_Final;

-- ========================================= --
--                 Entidades                 --
-- ========================================= --
CREATE TABLE Rol(
    rol_id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_rol VARCHAR(50) UNIQUE NOT NULL
);
CREATE TABLE Perfil(
	perfil_id INT AUTO_INCREMENT PRIMARY KEY,
	usuario VARCHAR(50) UNIQUE NOT NULL,
	email VARCHAR(50) UNIQUE NOT NULL,
	password VARCHAR(255) NOT NULL,
	rol_id INT NOT NULL,
	FOREIGN KEY(rol_id) REFERENCES Rol(rol_id)
);
CREATE TABLE Empresa(
	empresa_id INT AUTO_INCREMENT PRIMARY KEY,
	nombre VARCHAR(100) NOT NULL,
	descripcion VARCHAR(255) NOT NULL,
	sitio_web VARCHAR(100) NOT NULL,
	ubicacion VARCHAR(100) NOT NULL,
	perfil_id INT NOT NULL,
	FOREIGN KEY(perfil_id)REFERENCES Perfil(perfil_id)
);
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
);
CREATE TABLE Employee(
	employee_id INT AUTO_INCREMENT PRIMARY KEY,
	nombre VARCHAR(50) NOT NULL, 
	apellido VARCHAR(50) NOT NULL,
	email VARCHAR(50) NOT NULL,
	fecha_nacimiento DATE NOT NULL,
	freelance BOOLEAN DEFAULT FALSE,
	enterprise BOOLEAN DEFAULT TRUE,
	perfil_id INT NOT NULL,
	FOREIGN KEY(perfil_id)REFERENCES Perfil(perfil_id)
);
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
);
CREATE TABLE Experiencia(
	experiencia_id INT AUTO_INCREMENT PRIMARY KEY,
	titulo VARCHAR(100) NOT NULL,
	descripcion VARCHAR(255) NOT NULL,
	fecha_inicio DATE NOT NULL,
	fecha_fin DATE NOT NULL,
	employee_id INT NOT NULL,
	FOREIGN KEY(employee_id) REFERENCES Employee(employee_id)
);
CREATE TABLE Proyecto(
	proyecto_id INT AUTO_INCREMENT PRIMARY KEY,
	area_proyecto VARCHAR(50) NOT NULL,
	descripcion VARCHAR(255) NOT NULL,
	fecha_creacion DATE NOT NULL,
	cliente_id INT NOT NULL,
	FOREIGN KEY(cliente_id)REFERENCES Cliente_Particular(cliente_particular_id)
);
CREATE TABLE Habilidad(
	habilidad_id INT AUTO_INCREMENT PRIMARY KEY,
	nombre_habilidad VARCHAR(50) UNIQUE NOT NULL,
	nivel INT NOT NULL DEFAULT 0
);
CREATE TABLE Employee_Habilidad(
	employee_id INT NOT NULL,
	habilidad_id INT NOT NULL,
	PRIMARY KEY(employee_id, habilidad_id),
	FOREIGN KEY(employee_id) REFERENCES Employee(employee_id),
	FOREIGN KEY(habilidad_id) REFERENCES Habilidad(habilidad_id)
);
CREATE TABLE Postulacion(
	postulacion_id INT AUTO_INCREMENT PRIMARY KEY,
	fecha_postulacion DATE NOT NULL,
	estado VARCHAR(30) NOT NULL,
	mensaje VARCHAR(255) NOT NULL
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
CREATE TABLE Pagos(
	pago_id INT AUTO_INCREMENT PRIMARY KEY,
	moneda ENUM('USD','PESOS'),
	monto INT NOT NULL,
	medio_de_pago ENUM('TARJETA', 'BILLETERA_VIRTUAL')
);
CREATE TABLE Pagos_Freelancer(
	pagos_freelancer_id INT AUTO_INCREMENT PRIMARY KEY,
	titular VARCHAR(100) NOT NULL,
	dni VARCHAR(50) UNIQUE NOT NULL,
	entidad_bancaria ENUM('BANCO', 'BILLETERA_VIRTUAL'),
	numero_cuenta VARCHAR(100) NOT NULL,
	employee_id INT NOT NULL,
	FOREIGN KEY(employee_id)REFERENCES Employee(employee_id)
);
CREATE TABLE Pagos_Cliente_Freelancer(
	pagos_cliente_freelancer_id INT AUTO_INCREMENT PRIMARY KEY,
	titular VARCHAR(50) NOT NULL,
	dni VARCHAR(50) UNIQUE NOT NULL,
	entidad_bancaria ENUM('BANCO', 'BILLETERA_VIRTUAL'),
	cvu VARCHAR(50) UNIQUE NOT NULL,
	tipo_tarjeta ENUM('DEBITO','CREDITO'),
	empresa_tarjeta ENUM('VISA','MASTERCARD'),
	numero_tarjeta VARCHAR(50) UNIQUE NOT NULL,
	employee_id INT NOT NULL,
	cliente_id INT NOT NULL,
	FOREIGN KEY(employee_id)REFERENCES Employee(employee_id),
	FOREIGN KEY(cliente_id)REFERENCES Cliente_Particular(cliente_particular_id)
);
-- =============================== --
--       BOOTCAMP EMPRESA          --
-- =============================== --
CREATE TABLE Instructor_Empresa(
	instructor_id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    titulo VARCHAR(100) NOT NULL,
    empresa_id INT NOT NULL,
    perfil_id INT NOT NULL,
    FOREIGN KEY(perfil_id)REFERENCES Perfil(perfil_id),
    FOREIGN KEY(empresa_id)REFERENCES Empresa(empresa_id)
);
CREATE TABLE Bootcamp_Empresa(
    bootcamp_id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    modalidad ENUM('VIRTUAL', 'PRESENCIAL'),
    cupos INT NOT NULL,
	empresa_id INT NOT NULL,
    instructor_id INT NOT NULL,
    FOREIGN KEY(empresa_id) REFERENCES Empresa(empresa_id),
    FOREIGN KEY(instructor_id) REFERENCES Instructor_Empresa(instructor_id)
);
CREATE TABLE Inscripcion_Bootcamp_Empresa(
    inscripcion_id INT AUTO_INCREMENT PRIMARY KEY,
    fecha_inscripcion DATE NOT NULL,
    estado ENUM('INSCRIPTO','PENDIENTE','CANCELADO'),
    bootcamp_empresa INT NOT NULL,
    employee_id INT NOT NULL,
    FOREIGN KEY(bootcamp_empresa)REFERENCES Bootcamp_Empresa(bootcamp_id),
    FOREIGN KEY(employee_id) REFERENCES Employee(employee_id)
);
CREATE TABLE Calificacion_Bootcamp_Empresa(
    calificacion_id INT AUTO_INCREMENT PRIMARY KEY,
    puntaje INT NOT NULL,
    comentario VARCHAR(255) NOT NULL,
    fecha_comentario DATE NOT NULL,
    bootcamp_id INT NOT NULL,
    employee_id INT NOT NULL,
    FOREIGN KEY(bootcamp_id) REFERENCES Bootcamp_Empresa(bootcamp_id),
    FOREIGN KEY(employee_id) REFERENCES Employee(employee_id)
);
-- =============================== --
--       BOOTCAMP PARTICULAR       --
-- =============================== --
CREATE TABLE Instructor_Particular (
    instructor_id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    titulo VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    perfil_id INT NOT NULL,
    FOREIGN KEY(perfil_id)REFERENCES Perfil(perfil_id)
);
CREATE TABLE Bootcamp_Particular(
    bootcamp_id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    modalidad ENUM('VIRTUAL', 'PRESENCIAL'),
    cupos INT NOT NULL,
	instructor_id INT NOT NULL,
    FOREIGN KEY(instructor_id) REFERENCES Instructor_Particular(instructor_id)
);
CREATE TABLE Inscripcion_Bootcamp_Particular(
    inscripcion_id INT AUTO_INCREMENT PRIMARY KEY,
    fecha_inscripcion DATE  NOT NULL,
    estado ENUM('INSCRIPTO','PENDIENTE','CANCELADO'),
    bootcamp_particular INT  NOT NULL,
    employee_id INT NOT NULL,
    FOREIGN KEY(bootcamp_particular)REFERENCES Bootcamp_Particular(bootcamp_id),
	FOREIGN KEY(employee_id) REFERENCES Employee(employee_id)
);
CREATE TABLE Calificacion_Bootcamp_Particular(
    calificacion_id INT AUTO_INCREMENT PRIMARY KEY,
    puntaje INT NOT NULL,
    comentario VARCHAR(255) NOT NULL,
    fecha_comentario DATE NOT NULL,
    bootcamp_id INT NOT NULL,
    employee_id INT NOT NULL,
    FOREIGN KEY(bootcamp_id) REFERENCES Bootcamp_Particular(bootcamp_id),
    FOREIGN KEY(employee_id) REFERENCES Employee(employee_id)
);
CREATE TABLE Calificacion_Proyecto( 
    calificacion_id INT AUTO_INCREMENT PRIMARY KEY,
    puntaje INT NOT NULL,
    comentario VARCHAR(255) NOT NULL,
    fecha_comentario DATE NOT NULL,
    proyecto_id INT NOT NULL,
    freelancer_id INT NOT NULL,
    recruiter_id INT NOT NULL,
    FOREIGN KEY(proyecto_id) REFERENCES Proyecto(proyecto_id),
    FOREIGN KEY(freelancer_id) REFERENCES Employee(employee_id),
    FOREIGN KEY(recruiter_id) REFERENCES Recruiter(recruiter_id)
);
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
('empresaTech','empresa@tech.com','123456',5),
('recruiterJuan','juan@tech.com','123456',3),
('freelancerAna','ana@gmail.com','123456',2),
('freelancerPedro','pedro@gmail.com','123456',2),
('clienteCarlos','carlos@gmail.com','123456',4),
('instEmpresa','instempresa@gmail.com','123456',6),
('instParticular','instparticular@gmail.com','123456',6);

-- =====================
-- EMPRESA
-- =====================
INSERT INTO Empresa (nombre,descripcion,sitio_web,ubicacion,perfil_id) VALUES 
('Tech Solutions', 'Empresa dedicada al desarrollo de software', 'https://techsolutions.com', 'Buenos Aires', 1);

-- =====================
-- RECRUITER
-- =====================
INSERT INTO Recruiter (nombre,apellido,email,fecha_nacimiento,area,empresa_id,perfil_id) VALUES 
('Juan','Perez','juan@tech.com','1990-05-15','RRHH',1,2);

-- =====================
-- EMPLOYEES
-- =====================
INSERT INTO Employee (nombre,apellido,email,fecha_nacimiento,freelance,enterprise,perfil_id) VALUES
('Ana','Gomez','ana@gmail.com','1998-08-20',TRUE,FALSE,3),
('Pedro','Lopez','pedro@gmail.com','1996-04-10',TRUE,TRUE,4);

-- =====================
-- CLIENTE PARTICULAR
-- =====================
INSERT INTO Cliente_Particular (nombre,apellido,email,fecha_nacimiento,telefono,pais,perfil_id) VALUES
('Carlos','Fernandez','carlos@gmail.com','1985-07-12','2235123456','Argentina',5);

-- =====================
-- EXPERIENCIA
-- =====================
INSERT INTO Experiencia (titulo,descripcion,fecha_inicio,fecha_fin,employee_id) VALUES
('Java Developer','Desarrollo backend con Spring Boot','2022-01-01','2024-01-01',1),
('Frontend Developer','Desarrollo React','2021-06-01','2023-12-01',2);

-- =====================
-- HABILIDADES
-- =====================
INSERT INTO Habilidad (nombre_habilidad,nivel) VALUES
('Java',8),
('Spring Boot',7),
('React',9),
('MySQL',8);

-- =====================
-- EMPLOYEE_HABILIDAD
-- =====================
INSERT INTO Employee_Habilidad (employee_id,habilidad_id) VALUES
(1,1),
(1,2),
(1,4),
(2,3),
(2,4);

-- =====================
-- PORTFOLIOS
-- =====================
INSERT INTO Portfolio (github,linkedin,cv_url,descripcion,employee_id) VALUES
('https://github.com/anagomez','https://linkedin.com/in/anagomez','https://cv.com/ana.pdf','Backend Java Developer',1),
('https://github.com/pedrolopez','https://linkedin.com/in/pedrolopez','https://cv.com/pedro.pdf','Frontend React Developer',2);

-- =====================
-- PROYECTO
-- =====================
INSERT INTO Proyecto (area_proyecto,descripcion,fecha_creacion,cliente_id) VALUES
('Desarrollo Web','Sistema de gestión para PyME',CURDATE(),1);

-- =====================
-- POSTULACION
-- =====================
INSERT INTO Postulacion (fecha_postulacion,estado,mensaje) VALUES
(CURDATE(),'PENDIENTE','Me interesa participar en este proyecto');

INSERT INTO Postulacion_Postulante (employee_id,postulacion_id) VALUES (1,1);

-- =====================
-- PAGOS
-- =====================
INSERT INTO Pagos (moneda,monto,medio_de_pago) VALUES 
('USD',500,'TARJETA'), 
('PESOS',250000,'BILLETERA_VIRTUAL');

-- =====================
-- DATOS COBRO FREELANCER
-- =====================
INSERT INTO Pagos_Freelancer (titular,dni,entidad_bancaria,numero_cuenta,employee_id) VALUES
('Ana Gomez','40111222','BANCO','123456789',1);

-- =====================
-- PAGO CLIENTE-FREELANCER
-- =====================
INSERT INTO Pagos_Cliente_Freelancer (titular,dni,entidad_bancaria,cvu,tipo_tarjeta,empresa_tarjeta,numero_tarjeta,employee_id,cliente_id) VALUES
('Carlos Fernandez','30111222','BILLETERA_VIRTUAL','0000003100000000000011','CREDITO','VISA','4111111111111111',1,1);

-- =====================
-- INSTRUCTOR EMPRESA
-- =====================
INSERT INTO Instructor_Empresa (nombre,titulo,empresa_id,perfil_id) VALUES
('Martin Rodriguez','Senior Java Architect',1,6);

-- =====================
-- BOOTCAMP EMPRESA
-- =====================
INSERT INTO Bootcamp_Empresa (titulo,descripcion,fecha_inicio,fecha_fin,modalidad,cupos,empresa_id,instructor_id) VALUES
('Java Spring Boot','Bootcamp intensivo de backend','2026-07-01','2026-08-30','VIRTUAL',30,1,1);

-- =====================
-- INSCRIPCION BOOTCAMP EMPRESA
-- =====================
INSERT INTO Inscripcion_Bootcamp_Empresa (fecha_inscripcion,estado,bootcamp_empresa,employee_id) VALUES 
( CURDATE(), 'INSCRIPTO', 1, 1 );

-- =====================
-- CALIFICACION BOOTCAMP EMPRESA
-- =====================
INSERT INTO Calificacion_Bootcamp_Empresa (puntaje,comentario,fecha_comentario,bootcamp_id,employee_id) VALUES
(9,'Excelente contenido',CURDATE(),1,1);

-- =====================
-- INSTRUCTOR PARTICULAR
-- =====================
INSERT INTO Instructor_Particular (nombre,titulo,descripcion,perfil_id) VALUES 
( 'Lucia Torres', 'Full Stack Developer', 'Especialista en React y Node', 7 );

-- =====================
-- BOOTCAMP PARTICULAR
-- =====================
INSERT INTO Bootcamp_Particular ( titulo, descripcion, fecha_inicio, fecha_fin, modalidad, cupos, instructor_id ) VALUES 
( 'React desde Cero', 'Curso intensivo React', '2026-09-01', '2026-10-01', 'VIRTUAL', 50, 1 );

-- =====================
-- INSCRIPCION BOOTCAMP PARTICULAR
-- =====================
INSERT INTO Inscripcion_Bootcamp_Particular (fecha_inscripcion,estado,bootcamp_particular,employee_id) VALUES 
( CURDATE(), 'INSCRIPTO', 1, 2 );

-- =====================
-- CALIFICACION BOOTCAMP PARTICULAR
-- =====================
INSERT INTO Calificacion_Bootcamp_Particular (puntaje, comentario, fecha_comentario, bootcamp_id, employee_id ) VALUES 
( 10, 'Muy recomendable', CURDATE(), 1, 2);

-- =====================
-- CALIFICACION PROYECTO
-- =====================
INSERT INTO Calificacion_Proyecto (puntaje, comentario, fecha_comentario, proyecto_id, freelancer_id, recruiter_id)
VALUES (10, 'Excelente trabajo realizado', CURDATE(), 1, 1, 1);