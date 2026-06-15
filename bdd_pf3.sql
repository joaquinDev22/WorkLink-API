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
); -- Ya definida en una java.class por JoaquinDev22

CREATE TABLE Calificacion_Usuario(
    calificacion_usuario_id INT AUTO_INCREMENT PRIMARY KEY,
    calificacion_id INT,
    calificacion_bootcamp_id INT,
    perfil_id INT NOT NULL,
    FOREIGN KEY (calificacion_id) REFERENCES Calificacion_Proyecto(calificacion_id),
    FOREIGN KEY (calificacion_bootcamp_id) REFERENCES Calificacion_Bootcamp(calificacion_id),
    FOREIGN KEY (perfil_id) REFERENCES Perfil(perfil_id)
); -- Ya definida en una java.class por JoaquinDev22

CREATE TABLE Habilidad(
	habilidad_id INT AUTO_INCREMENT PRIMARY KEY,
	nombre_habilidad VARCHAR(50) UNIQUE NOT NULL
); -- Ya definida en una java.class por JoaquinDev22

CREATE TABLE Employee_Habilidad(
	employee_id INT NOT NULL,
	habilidad_id INT NOT NULL,
	nivel INT NOT NULL DEFAULT 0,
	PRIMARY KEY(employee_id, habilidad_id),
	FOREIGN KEY(employee_id) REFERENCES Employee(employee_id),
	FOREIGN KEY(habilidad_id) REFERENCES Habilidad(habilidad_id)
); -- Ya definida en una java.class por JoaquinDev22

CREATE TABLE Vacante(
	vacante_id INT AUTO_INCREMENT PRIMARY KEY,
	puesto VARCHAR(50) NOT NULL,
	descripcion VARCHAR(255) NOT NULL,
	fecha_publicacion DATE NOT NULL,
	fecha_fin DATE NOT NULL,
	empresa_id INT NOT NULL,
	FOREIGN KEY(empresa_id)REFERENCES Empresa(empresa_id)
); -- Ya definida en una java.class por JoaquinDev22

CREATE TABLE Postulacion_Vacante(
	postulacion_id INT AUTO_INCREMENT PRIMARY KEY,
	fecha_postulacion DATE NOT NULL,
	estado_postulacion ENUM('PENDIENTE','VISUALIZADO','RECHAZADO') DEFAULT 'PENDIENTE',
    cv LONGBLOB NOT NULL,
	mensaje VARCHAR(255),
	vacante_id INT NOT NULL,
    postulante_id INT NOT NULL,
    FOREIGN KEY(vacante_id)REFERENCES Vacante(vacante_id),
    FOREIGN KEY (postulante_id) REFERENCES Employee(employee_id)
); -- Ya definida en una java.class por JoaquinDev22

CREATE TABLE Vacante_Habilidad(
	vacante_id INT NOT NULL,
	habilidad_id INT NOT NULL,
	PRIMARY KEY(vacante_id, habilidad_id),
	FOREIGN KEY(vacante_id) REFERENCES Vacante(vacante_id),
	FOREIGN KEY(habilidad_id) REFERENCES Habilidad(habilidad_id)
); -- Ya definida en una java.class por JoaquinDev22

CREATE TABLE Portfolio(
	portfolio_id INT AUTO_INCREMENT PRIMARY KEY,
	github VARCHAR(150),
	linkedin VARCHAR(150),
	cv LONGBLOB,
	descripcion VARCHAR(255) NOT NULL,
	employee_id INT NOT NULL,
	FOREIGN KEY(employee_id) REFERENCES Employee(employee_id)
); -- Ya definida en una java.class por JoaquinDev22
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
    FOREIGN KEY(bootcamp_id) REFERENCES Bootcamp(bootcamp_id)
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
