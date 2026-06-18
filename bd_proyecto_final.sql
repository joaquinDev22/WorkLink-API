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
	tamano VARCHAR(50),
	sector VARCHAR(100),
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
    estado ENUM('BUSCANDO_TALENTO', 'EN_PROGRESO', 'FINALIZADO') DEFAULT 'BUSCANDO_TALENTO',
    freelancer_asignado_id INT,
	cliente_id INT NOT NULL,
	FOREIGN KEY(cliente_id)REFERENCES Cliente_Particular(cliente_particular_id),
	FOREIGN KEY(freelancer_asignado_id) REFERENCES Employee(employee_id)
); -- Ya definida en una java.class por JoaquinDev22

CREATE TABLE Hito_Proyecto(
    hito_id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    monto DECIMAL(10,2),
    estado ENUM('PENDIENTE', 'COMPLETADO', 'PAGADO') DEFAULT 'PENDIENTE',
    fecha_limite DATE,
    proyecto_id INT NOT NULL,
    FOREIGN KEY (proyecto_id) REFERENCES Proyecto(proyecto_id) ON DELETE CASCADE
);  -- Ya definida en una java.class por JoaquinDev22

CREATE TABLE proyecto_habilidades(
    proyecto_id INT NOT NULL,
    habilidad VARCHAR(255) NOT NULL,
    FOREIGN KEY(proyecto_id) REFERENCES Proyecto(proyecto_id)
);  -- Ya definida en una java.class.List por JoaquinDev22

CREATE TABLE Postulacion_Proyecto(
    post_proy_id INT AUTO_INCREMENT PRIMARY KEY,
    descripcion VARCHAR(255) NOT NULL,
    valor_propuesto DECIMAL(10,2),
    fecha_postulacion DATE NOT NULL,
    estado_postulacion ENUM('PENDIENTE', 'ACEPTADA', 'RECHAZADA') DEFAULT 'PENDIENTE',
    employee_id INT NOT NULL,
    proyecto_id INT NOT NULL,
    FOREIGN KEY (employee_id) REFERENCES Employee(employee_id),
    FOREIGN KEY (proyecto_id) REFERENCES Proyecto(proyecto_id)
); -- Ya definida en una java.class por JoaquinDev22

CREATE TABLE Calificacion_Proyecto(
    calificacion_id INT AUTO_INCREMENT PRIMARY KEY,
    puntaje INT NOT NULL,
    titulo_resena VARCHAR(100),
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
); -- Ya definida en una java.class.List por JoaquinDev22

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
); -- Ya definida en una java.class por JoaquinDev22

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

CREATE TABLE Modulo_Bootcamp (
    modulo_id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    descripcion TEXT,
    link_clase VARCHAR(255),
    horario_clase VARCHAR(100),
    bootcamp_id INT NOT NULL,
    FOREIGN KEY (bootcamp_id) REFERENCES Bootcamp(bootcamp_id) ON DELETE CASCADE
); -- Ya definida en una java.class por JoaquinDev22

CREATE TABLE bootcamp_etiquetas(
    bootcamp_id INT NOT NULL,
    etiqueta VARCHAR(255) NOT NULL,
    FOREIGN KEY(bootcamp_id) REFERENCES Bootcamp(bootcamp_id)
); -- Ya definida en una java.class.List por JoaquinDev22

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

INSERT INTO Rol (nombre_rol) VALUES 
('EMPRESA'), ('CLIENTE'), ('INSTRUCTOR'), ('FREELANCER'), ('POSTULANTE'), ('RECRUITER');

-- =========================================================================
-- ATENCIÓN: Se asume que los perfiles del 1 al 6 ya fueron insertados desde
-- Postman con sus respectivas credenciales y rol_id, para que estas llaves
-- foráneas (perfil_id) funcionen correctamente.
-- =========================================================================

INSERT INTO Empresa (nombre, descripcion, sitio_web, ubicacion, tamano, sector, perfil_id) VALUES 
('TechCorp', 'Soluciones de software corporativo', 'https://techcorp.com', 'Madrid, España', '50-200 empleados', 'Tecnología', 1);

INSERT INTO Recruiter (nombre, apellido, email, fecha_nacimiento, area, empresa_id, perfil_id) VALUES 
('Laura', 'Gomez', 'laura@techcorp.com', '1985-05-12', 'Recursos Humanos', 1, 1);

INSERT INTO Cliente_Particular (nombre, apellido, email, fecha_nacimiento, telefono, pais, perfil_id) VALUES 
('Carlos', 'Mendoza', 'carlos.m@empresa.com', '1980-08-20', '+34600123456', 'España', 2);

INSERT INTO Instructor (nombre, apellido, titulo, presentacion, tipo, perfil_id, empresa_id) VALUES 
('Maria', 'Lopez', 'Senior Frontend Developer', 'Apasionada por la enseñanza y el código limpio.', 'PARTICULAR', 3, NULL);

INSERT INTO Employee (nombre, apellido, email, fecha_nacimiento, freelance, enterprise, perfil_id) VALUES 
('Juan', 'Perez', 'juan.freelance@gmail.com', '1995-02-15', TRUE, FALSE, 4),
('Ana', 'Martinez', 'ana.dev@gmail.com', '1992-11-30', TRUE, FALSE, 5),
('Luis', 'Torres', 'luis.postulante@gmail.com', '1998-07-22', FALSE, FALSE, 6);

INSERT INTO Habilidad (nombre_habilidad) VALUES 
('React'), ('Node.js'), ('Figma'), ('TypeScript'), ('Python');

INSERT INTO Employee_Habilidad (employee_id, habilidad_id, nivel) VALUES 
(1, 1, 5), (1, 4, 4), -- Juan: React y TS
(2, 2, 5), (2, 5, 4), -- Ana: Node y Python
(3, 1, 3); -- Luis: React junior

INSERT INTO Experiencia (titulo, descripcion, fecha_inicio, fecha_fin, employee_id) VALUES 
('Frontend Developer', 'Desarrollo de interfaces en React para E-commerce.', '2020-01-01', '2023-01-01', 1),
('Backend Engineer', 'Creación de APIs REST con Node.js.', '2019-05-01', '2024-02-01', 2);

INSERT INTO Portfolio (github, linkedin, descripcion, employee_id) VALUES 
('https://github.com/juanperez', 'https://linkedin.com/in/juanperez', 'Portafolio de proyectos React', 1);

INSERT INTO Vacante (puesto, descripcion, fecha_publicacion, fecha_fin, logo, ubicacion, tipo_ubicacion, tipo_vacante, salario, empresa_id) VALUES 
('Frontend React SSR', 'Buscamos desarrollador para migrar app a Next.js', '2026-06-01', '2026-07-01', '🏢', 'Remoto', 'Remoto', 'Tiempo Completo', '$3000 USD/mes', 1);

INSERT INTO vacante_etiquetas (vacante_id, etiqueta) VALUES 
(1, 'React'), (1, 'Next.js');

INSERT INTO Postulacion_Vacante (fecha_postulacion, estado_postulacion, cv, mensaje, vacante_id, postulante_id) VALUES 
('2026-06-15', 'PENDIENTE', '', 'Me interesa mucho la vacante, tengo experiencia en React.', 1, 3);

INSERT INTO Proyecto (area_proyecto, descripcion, fecha_creacion, presupuesto_max, presupuesto_min, duracion, tipo_pago, estado, freelancer_asignado_id, cliente_id) VALUES 
('Diseño Web', 'Necesito un rediseño completo de mi sitio corporativo en Figma y React.', '2026-05-10', 2500.00, 1500.00, '1 mes', 'Precio Fijo', 'EN_PROGRESO', 1, 1),
('API en Node.js', 'Crear una API de facturación.', '2026-06-10', 1000.00, 800.00, '2 semanas', 'Por Hora', 'BUSCANDO_TALENTO', NULL, 1),
('App Móvil', 'App en React Native finalizada.', '2026-01-10', 4000.00, 3000.00, '3 meses', 'Precio Fijo', 'FINALIZADO', 2, 1);

INSERT INTO proyecto_habilidades (proyecto_id, habilidad) VALUES 
(1, 'React'), (1, 'Figma'),
(2, 'Node.js'), (2, 'SQL'),
(3, 'React Native');

INSERT INTO Postulacion_Proyecto (descripcion, valor_propuesto, fecha_postulacion, estado_postulacion, employee_id, proyecto_id) VALUES 
('Hola, soy experto en React y Figma. Puedo hacerlo en 3 semanas.', 2000.00, '2026-05-12', 'ACEPTADA', 1, 1),
('Tengo experiencia en Node y APIs financieras.', 900.00, '2026-06-12', 'PENDIENTE', 2, 2);

INSERT INTO Hito_Proyecto (titulo, descripcion, monto, estado, fecha_limite, proyecto_id) VALUES 
('Wireframes y UX', 'Diseño de pantallas iniciales en Figma.', 500.00, 'COMPLETADO', '2026-05-20', 1),
('Desarrollo Frontend', 'Maquetación e integración de React.', 1500.00, 'PENDIENTE', '2026-06-25', 1);

INSERT INTO Calificacion_Proyecto (puntaje, titulo_resena, comentario, fecha_comentario, proyecto_id) VALUES 
(5, 'Trabajo excelente y rápido', 'Ana entregó la app móvil antes de tiempo con cero bugs. Muy recomendada.', '2026-04-15', 3);

INSERT INTO Bootcamp (titulo, descripcion, fecha_inicio, fecha_fin, modalidad, tipo, cupos, logo, precio, rating, instructor_id, empresa_id) VALUES 
('Master en React', 'Domina React, Hooks, Context y Next.js desde cero.', '2026-08-01', '2026-12-01', 'VIRTUAL', 'PARTICULAR', 100, '⚛️', '150.00', 4.9, 1, NULL);

INSERT INTO bootcamp_etiquetas (bootcamp_id, etiqueta) VALUES 
(1, 'React'), (1, 'Frontend');

INSERT INTO Modulo_Bootcamp (titulo, descripcion, link_clase, horario_clase, bootcamp_id) VALUES 
('Introducción a React', 'Conceptos básicos, JSX y componentes.', 'https://zoom.us/clase1', 'Lunes 18:00', 1),
('Hooks y Estado', 'useState, useEffect y custom hooks.', 'https://zoom.us/clase2', 'Miércoles 18:00', 1);

INSERT INTO Inscripcion_Bootcamp (fecha_inscripcion, estado, bootcamp_id, employee_id) VALUES 
('2026-06-16', 'INSCRIPTO', 1, 3);
