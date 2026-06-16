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
    is_active BOOLEAN DEFAULT TRUE,
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
    duracion VARCHAR(50),
    tipo_pago VARCHAR(50),
	cliente_id INT NOT NULL,
	FOREIGN KEY(cliente_id)REFERENCES Cliente_Particular(cliente_particular_id)
); -- Ya definida en una java.class por JoaquinDev22

CREATE TABLE proyecto_habilidades(
    proyecto_id INT NOT NULL,
    habilidad VARCHAR(255) NOT NULL,
    FOREIGN KEY(proyecto_id) REFERENCES Proyecto(proyecto_id)
);

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
    logo VARCHAR(255),
    ubicacion VARCHAR(100),
    tipo_ubicacion VARCHAR(50),
    tipo_vacante VARCHAR(50),
    salario VARCHAR(50),
	empresa_id INT NOT NULL,
	FOREIGN KEY(empresa_id)REFERENCES Empresa(empresa_id)
); -- Ya definida en una java.class por JoaquinDev22

CREATE TABLE vacante_etiquetas(
    vacante_id INT NOT NULL,
    etiqueta VARCHAR(255) NOT NULL,
    FOREIGN KEY(vacante_id) REFERENCES Vacante(vacante_id)
);

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
); -- Ya definida en una java.class por JoaquinDev22

CREATE TABLE Metodos_Cobro_Freelancer(
	metodos_cobro_freelancer_id INT AUTO_INCREMENT PRIMARY KEY,
	titular VARCHAR(100) NOT NULL,
	dni VARCHAR(50) UNIQUE NOT NULL,
	entidad_bancaria ENUM('BANCO', 'BILLETERA_VIRTUAL'),
	numero_cuenta VARCHAR(100) UNIQUE NOT NULL,
	employee_id INT NOT NULL,
	FOREIGN KEY(employee_id)REFERENCES Employee(employee_id)
); -- Ya definida en una java.class por JoaquinDev22

CREATE TABLE Metodos_Pago_Cliente(
	metodos_pago_cliente_id INT AUTO_INCREMENT PRIMARY KEY,
	titular VARCHAR(50) NOT NULL,
	dni VARCHAR(50) UNIQUE NOT NULL,
	entidad_bancaria ENUM('BANCO', 'BILLETERA_VIRTUAL'),
	cvu VARCHAR(50) UNIQUE NOT NULL,
	tipo_tarjeta ENUM('DEBITO','CREDITO'),
	empresa_tarjeta ENUM('VISA','MASTERCARD','CABAL'),
	numero_tarjeta VARCHAR(50) UNIQUE NOT NULL,
	cliente_id INT NOT NULL,
	FOREIGN KEY(cliente_id)REFERENCES Cliente_Particular(cliente_particular_id)
); -- Ya definida en una java.class por JoaquinDev22

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
    apellido VARCHAR(50) NOT NULL,
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
    logo VARCHAR(255),
    precio VARCHAR(50),
    rating DECIMAL(3,1),
	instructor_id INT NOT NULL,
	empresa_id INT,
    FOREIGN KEY(instructor_id) REFERENCES Instructor(instructor_id),
	FOREIGN KEY(empresa_id) REFERENCES Empresa(empresa_id)
); -- Ya definida en una java.class por Yamils06

CREATE TABLE bootcamp_etiquetas(
    bootcamp_id INT NOT NULL,
    etiqueta VARCHAR(255) NOT NULL,
    FOREIGN KEY(bootcamp_id) REFERENCES Bootcamp(bootcamp_id)
);

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

CREATE TABLE Calificacion_Usuario(
    calificacion_usuario_id INT AUTO_INCREMENT PRIMARY KEY,
    calificacion_id INT,
    calificacion_bootcamp_id INT,
    perfil_id INT NOT NULL,
    FOREIGN KEY (calificacion_id) REFERENCES Calificacion_Proyecto(calificacion_id),
    FOREIGN KEY (calificacion_bootcamp_id) REFERENCES Calificacion_Bootcamp(calificacion_id),
    FOREIGN KEY (perfil_id) REFERENCES Perfil(perfil_id)
); -- Ya definida en una java.class por JoaquinDev22
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

-- ========================================= --
--                 Test Data                 --
-- ========================================= --

INSERT INTO Rol (nombre_rol) VALUES ('EMPRESA'), ('CLIENTE'), ('INSTRUCTOR');

-- Passwords hashed to '1234' via BCrypt
INSERT INTO Perfil (usuario, email, password, rol_id, is_active) VALUES 
('google_hr', 'hr@google.com', '$2a$10$DowrM1y4H.P1DInPZ.7j0.GstB93l7AHTQ/8nQkG72iT2XW.Bf.5.', 1, 1),
('tech_startup', 'contact@techstartup.com', '$2a$10$DowrM1y4H.P1DInPZ.7j0.GstB93l7AHTQ/8nQkG72iT2XW.Bf.5.', 2, 1),
('dr_smith', 'john.smith@data.com', '$2a$10$DowrM1y4H.P1DInPZ.7j0.GstB93l7AHTQ/8nQkG72iT2XW.Bf.5.', 3, 1);

INSERT INTO Empresa (nombre, descripcion, sitio_web, ubicacion, perfil_id) VALUES 
('Google', 'Empresa tecnológica multinacional', 'https://google.com', 'Mountain View, CA', 1);

INSERT INTO Cliente_Particular (nombre, apellido, email, fecha_nacimiento, telefono, pais, perfil_id) VALUES 
('Tech', 'Startup', 'contact@techstartup.com', '1990-01-01', '123456789', 'USA', 2);

INSERT INTO Instructor (nombre, apellido, titulo, presentacion, tipo, perfil_id, empresa_id) VALUES 
('John', 'Smith', 'Senior Data Scientist', 'Experto en IA con 10 años de experiencia.', 'PARTICULAR', 3, 1);

INSERT INTO Vacante (puesto, descripcion, fecha_publicacion, fecha_fin, logo, ubicacion, tipo_ubicacion, tipo_vacante, salario, empresa_id) VALUES 
('Ingeniero de Software Senior', 'Buscamos un ingeniero con experiencia en Spring Boot y React.', '2026-06-16', '2026-07-16', '🏢', 'Remoto', 'Remoto', 'Tiempo Completo', '$120k - $150k', 1),
('Diseñador UX/UI', 'Unete a nuestro equipo para diseñar el futuro de la web.', '2026-06-16', '2026-07-06', '🎨', 'Buenos Aires, AR', 'Híbrido', 'Tiempo Completo', '$80k - $100k', 1);

INSERT INTO vacante_etiquetas (vacante_id, etiqueta) VALUES 
(1, 'Java'), (1, 'React'), (1, 'AWS'),
(2, 'Figma'), (2, 'UI'), (2, 'UX');

INSERT INTO Proyecto (area_proyecto, descripcion, fecha_creacion, presupuesto_max, presupuesto_min, duracion, tipo_pago, cliente_id) VALUES 
('Desarrollo Web', 'Crear una landing page en React con animaciones 3D.', '2026-06-16', 3000.00, 1000.00, '1 mes', 'Precio Fijo', 1),
('Desarrollo Backend', 'Migrar base de datos de MySQL a PostgreSQL.', '2026-06-16', 1000.00, 500.00, '2 semanas', 'Precio Fijo', 1);

INSERT INTO proyecto_habilidades (proyecto_id, habilidad) VALUES 
(1, 'React'), (1, 'Three.js'), (1, 'CSS'),
(2, 'SQL'), (2, 'PostgreSQL'), (2, 'Bases de Datos');

INSERT INTO Bootcamp (titulo, descripcion, fecha_inicio, fecha_fin, modalidad, tipo, cupos, logo, precio, rating, instructor_id, empresa_id) VALUES 
('Bootcamp Full-Stack Web', 'Conviértete en desarrollador web Full-Stack en 16 semanas. Aprende HTML, CSS, JS, React, y Node.', '2026-06-26', '2026-09-24', 'VIRTUAL', 'PARTICULAR', 50, '💻', '$4,500', 4.8, 1, 1),
('Data Science & Machine Learning', 'Domina Python, Pandas, y Scikit-learn para construir modelos predictivos.', '2026-07-01', '2026-10-14', 'VIRTUAL', 'PARTICULAR', 30, '📊', '$5,000', 4.9, 1, 1);

INSERT INTO bootcamp_etiquetas (bootcamp_id, etiqueta) VALUES 
(1, 'React'), (1, 'Node'), (1, 'Full-Stack'),
(2, 'Python'), (2, 'IA'), (2, 'Data Science');