DROP DATABASE IF EXISTS Bd_Proyecto_Final;
CREATE DATABASE Bd_Proyecto_Final CHARSET utf8mb4;
USE Bd_Proyecto_Final;

-- ========================================= --
--                 Entidades                 --
-- ========================================= --
CREATE TABLE Empresa(
empresa_id INT AUTO_INCREMENT PRIMARY KEY,
nombre VARCHAR(100) NOT NULL,
descripcion VARCHAR(255) NOT NULL,
sitio_web VARCHAR(100) NOT NULL,
ubicacion VARCHAR(100) NOT NULL
);
CREATE TABLE Recruiter(
recruiter_id INT AUTO_INCREMENT PRIMARY KEY,
nombre VARCHAR(50) NOT NULL,
apellido VARCHAR(50) NOT NULL,
email VARCHAR(50) NOT NULL,
fecha_nacimiento DATE NOT NULL,
area VARCHAR(50) NOT NULL,
empresa_id INT NOT NULL,
FOREIGN KEY(empresa_id)REFERENCES Empresa(empresa_id)
);
CREATE TABLE Employee(
employee_id INT AUTO_INCREMENT PRIMARY KEY,
nombre VARCHAR(50) NOT NULL, 
apellido VARCHAR(50) NOT NULL,
email VARCHAR(50) NOT NULL,
fecha_nacimiento DATE NOT NULL,
freelance BOOLEAN DEFAULT FALSE,
enterprise BOOLEAN DEFAULT TRUE
);
CREATE TABLE Cliente_Particular(
cliente_particular_id INT AUTO_INCREMENT PRIMARY KEY,
nombre VARCHAR(50) NOT NULL,
apellido VARCHAR(50) NOT NULL,
email VARCHAR(50) NOT NULL,
fecha_nacimiento DATE NOT NULL,
telefono VARCHAR(50) NOT NULL,
pais VARCHAR(100) NOT NULL
);
CREATE TABLE Perfil(
perfil_id INT AUTO_INCREMENT PRIMARY KEY,
nombre_usuario VARCHAR(50) UNIQUE NOT NULL,
password_usuario VARCHAR(255) NOT NULL,
employee_id INT NOT NULL,
recruiter_id INT NOT NULL,
cliente_id INT NOT NULL,
empresa_id INT NOT NULL,
FOREIGN KEY(employee_id)REFERENCES Employee(employee_id),
FOREIGN KEY(recruiter_id)REFERENCES Recruiter(recruiter_id),
FOREIGN KEY(cliente_id)REFERENCES Cliente_Particular(cliente_particular_id),
FOREIGN KEY(empresa_id)REFERENCES Empresa(empresa_id)
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
    descripcion VARCHAR(255) NOT NULL
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
