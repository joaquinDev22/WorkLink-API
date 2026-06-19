CREATE TABLE rol(
    rol_id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_rol VARCHAR(50) UNIQUE NOT NULL
); -- Ya definida en una java.class por JoaquinDev22

CREATE TABLE perfil(
	perfil_id INT AUTO_INCREMENT PRIMARY KEY,
    foto_perfil LONGBLOB,
    banner_perfil LONGBLOB,
    ubicacion VARCHAR(150),
    descripcion VARCHAR(500),
	usuario VARCHAR(50) UNIQUE NOT NULL,
	email VARCHAR(50) UNIQUE NOT NULL,
	password VARCHAR(255) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
	rol_id INT NOT NULL,
	FOREIGN KEY(rol_id) REFERENCES rol(rol_id)
); -- Ya definida en una java.class por Gabrielramirez2003

CREATE TABLE empresa(
	empresa_id INT AUTO_INCREMENT PRIMARY KEY,
	nombre VARCHAR(100) NOT NULL,
	descripcion VARCHAR(255) NOT NULL,
	sitio_web VARCHAR(100) NOT NULL,
	ubicacion VARCHAR(100) NOT NULL,
	tamano VARCHAR(50),
	sector VARCHAR(100),
	perfil_id INT NOT NULL,
	FOREIGN KEY(perfil_id) REFERENCES perfil(perfil_id)
); -- Ya definida en una java.class por Gabrielramirez2003

CREATE TABLE recruiter(
	recruiter_id INT AUTO_INCREMENT PRIMARY KEY,
	nombre VARCHAR(50) NOT NULL,
	apellido VARCHAR(50) NOT NULL,
	email VARCHAR(50) NOT NULL,
	fecha_nacimiento DATE NOT NULL,
	area VARCHAR(50) NOT NULL,
	empresa_id INT NOT NULL,
	perfil_id INT NOT NULL,
	FOREIGN KEY(perfil_id) REFERENCES perfil(perfil_id),
	FOREIGN KEY(empresa_id) REFERENCES empresa(empresa_id)
); -- Ya definida en una java.class por JoaquinDev22

CREATE TABLE employee(
	employee_id INT AUTO_INCREMENT PRIMARY KEY,
	nombre VARCHAR(50) NOT NULL, 
	apellido VARCHAR(50) NOT NULL,
	email VARCHAR(50) NOT NULL,
	fecha_nacimiento DATE NOT NULL,
    freelance BOOLEAN,
    enterprise BOOLEAN,
	perfil_id INT NOT NULL,
	FOREIGN KEY(perfil_id) REFERENCES perfil(perfil_id)
); -- Ya definida en una java.class por JoaquinDev22

CREATE TABLE cliente_particular(
	cliente_particular_id INT AUTO_INCREMENT PRIMARY KEY,
	nombre VARCHAR(50) NOT NULL,
	apellido VARCHAR(50) NOT NULL,
	email VARCHAR(50) NOT NULL,
	fecha_nacimiento DATE NOT NULL,
	telefono VARCHAR(50) NOT NULL,
	pais VARCHAR(100) NOT NULL,
	perfil_id INT NOT NULL,
	FOREIGN KEY(perfil_id) REFERENCES perfil(perfil_id)
); -- Ya definida en una java.class por JoaquinDev22

CREATE TABLE experiencia(
	experiencia_id INT AUTO_INCREMENT PRIMARY KEY,
	titulo VARCHAR(100) NOT NULL,
	descripcion VARCHAR(255) NOT NULL,
	fecha_inicio DATE NOT NULL,
	fecha_fin DATE,
	employee_id INT NOT NULL,
	FOREIGN KEY(employee_id) REFERENCES employee(employee_id)
); -- Ya definida en una java.class por JoaquinDev22

CREATE TABLE proyecto(
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
	FOREIGN KEY(cliente_id) REFERENCES cliente_particular(cliente_particular_id),
	FOREIGN KEY(freelancer_asignado_id) REFERENCES employee(employee_id)
); -- Ya definida en una java.class por JoaquinDev22

CREATE TABLE hito_proyecto(
    hito_id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    monto DECIMAL(10,2),
    estado ENUM('PENDIENTE', 'COMPLETADO', 'PAGADO') DEFAULT 'PENDIENTE',
    fecha_limite DATE,
    proyecto_id INT NOT NULL,
    FOREIGN KEY (proyecto_id) REFERENCES proyecto(proyecto_id) ON DELETE CASCADE
);  -- Ya definida en una java.class por JoaquinDev22

CREATE TABLE proyecto_habilidades(
    proyecto_id INT NOT NULL,
    habilidad VARCHAR(255) NOT NULL,
    FOREIGN KEY(proyecto_id) REFERENCES proyecto(proyecto_id)
);  -- Ya definida en una java.class.List por JoaquinDev22

CREATE TABLE postulacion_proyecto(
    post_proy_id INT AUTO_INCREMENT PRIMARY KEY,
    descripcion VARCHAR(255) NOT NULL,
    valor_propuesto DECIMAL(10,2),
    fecha_postulacion DATE NOT NULL,
    estado_postulacion ENUM('PENDIENTE', 'ACEPTADA', 'RECHAZADA') DEFAULT 'PENDIENTE',
    employee_id INT NOT NULL,
    proyecto_id INT NOT NULL,
    FOREIGN KEY (employee_id) REFERENCES employee(employee_id),
    FOREIGN KEY (proyecto_id) REFERENCES proyecto(proyecto_id)
); -- Ya definida en una java.class por JoaquinDev22

CREATE TABLE calificacion_proyecto(
    calificacion_id INT AUTO_INCREMENT PRIMARY KEY,
    puntaje INT NOT NULL,
    titulo_resena VARCHAR(100),
    comentario VARCHAR(255) NOT NULL,
    fecha_comentario DATE NOT NULL,
    proyecto_id INT NOT NULL,
    FOREIGN KEY (proyecto_id) REFERENCES proyecto(proyecto_id)
); -- Ya definida en una java.class por JoaquinDev22

CREATE TABLE habilidad(
	habilidad_id INT AUTO_INCREMENT PRIMARY KEY,
	nombre_habilidad VARCHAR(50) UNIQUE NOT NULL
); -- Ya definida en una java.class por JoaquinDev22

CREATE TABLE employee_habilidad(
	employee_id INT NOT NULL,
	habilidad_id INT NOT NULL,
	nivel INT NOT NULL DEFAULT 0,
	PRIMARY KEY(employee_id, habilidad_id),
	FOREIGN KEY(employee_id) REFERENCES employee(employee_id),
	FOREIGN KEY(habilidad_id) REFERENCES habilidad(habilidad_id)
); -- Ya definida en una java.class por JoaquinDev22

CREATE TABLE vacante(
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
	FOREIGN KEY(empresa_id) REFERENCES empresa(empresa_id)
); -- Ya definida en una java.class por JoaquinDev22

CREATE TABLE vacante_etiquetas(
    vacante_id INT NOT NULL,
    etiqueta VARCHAR(255) NOT NULL,
    FOREIGN KEY(vacante_id) REFERENCES vacante(vacante_id)
); -- Ya definida en una java.class.List por JoaquinDev22

CREATE TABLE postulacion_vacante(
	postulacion_id INT AUTO_INCREMENT PRIMARY KEY,
	fecha_postulacion DATE NOT NULL,
	estado_postulacion ENUM('PENDIENTE','VISUALIZADO','RECHAZADO') DEFAULT 'PENDIENTE',
    cv LONGBLOB NOT NULL,
	mensaje VARCHAR(255),
	vacante_id INT NOT NULL,
    postulante_id INT NOT NULL,
    FOREIGN KEY(vacante_id) REFERENCES vacante(vacante_id),
    FOREIGN KEY (postulante_id) REFERENCES employee(employee_id)
); -- Ya definida en una java.class por JoaquinDev22

CREATE TABLE vacante_habilidad(
	vacante_id INT NOT NULL,
	habilidad_id INT NOT NULL,
	PRIMARY KEY(vacante_id, habilidad_id),
	FOREIGN KEY(vacante_id) REFERENCES vacante(vacante_id),
	FOREIGN KEY(habilidad_id) REFERENCES habilidad(habilidad_id)
); -- Ya definida en una java.class por JoaquinDev22

CREATE TABLE portfolio(
	portfolio_id INT AUTO_INCREMENT PRIMARY KEY,
	github VARCHAR(150),
	linkedin VARCHAR(150),
	cv LONGBLOB,
	descripcion VARCHAR(255) NOT NULL,
	employee_id INT NOT NULL,
	FOREIGN KEY(employee_id) REFERENCES employee(employee_id)
); -- Ya definida en una java.class por JoaquinDev22

-- ======================= --
--          PAGOS          --
-- ======================= --

CREATE TABLE pagos(
	pago_id INT AUTO_INCREMENT PRIMARY KEY,
	moneda ENUM('USD','PESOS'),
	monto INT NOT NULL,
	medio_de_pago ENUM('TARJETA', 'BILLETERA_VIRTUAL'),
	proyecto_id INT NOT NULL,
	FOREIGN KEY(proyecto_id) REFERENCES proyecto(proyecto_id)
); -- Ya definida en una java.class por JoaquinDev22

CREATE TABLE metodos_cobro_freelancer(
	metodos_cobro_freelancer_id INT AUTO_INCREMENT PRIMARY KEY,
	titular VARCHAR(100) NOT NULL,
	dni VARCHAR(50) UNIQUE NOT NULL,
	entidad_bancaria ENUM('BANCO', 'BILLETERA_VIRTUAL'),
	numero_cuenta VARCHAR(100) UNIQUE NOT NULL,
	employee_id INT NOT NULL,
	FOREIGN KEY(employee_id) REFERENCES employee(employee_id)
); -- Ya definida en una java.class por JoaquinDev22

CREATE TABLE metodos_pago_cliente(
	metodos_pago_cliente_id INT AUTO_INCREMENT PRIMARY KEY,
	titular VARCHAR(50) NOT NULL,
	dni VARCHAR(50) UNIQUE NOT NULL,
	entidad_bancaria ENUM('BANCO', 'BILLETERA_VIRTUAL'),
	cvu VARCHAR(50) UNIQUE NOT NULL,
	tipo_tarjeta ENUM('DEBITO','CREDITO'),
	empresa_tarjeta ENUM('VISA','MASTERCARD','CABAL'),
	numero_tarjeta VARCHAR(50) UNIQUE NOT NULL,
	cliente_id INT NOT NULL,
	FOREIGN KEY(cliente_id) REFERENCES cliente_particular(cliente_particular_id)
); -- Ya definida en una java.class por JoaquinDev22

CREATE TABLE pagos_cliente_freelancer(
	pagos_cliente_freelancer_id INT AUTO_INCREMENT PRIMARY KEY,
	pago_id INT NOT NULL,
	metodos_cobro_freelancer_id INT NOT NULL,
	metodos_pago_cliente_id INT NOT NULL,
	FOREIGN KEY(pago_id) REFERENCES pagos(pago_id),
	FOREIGN KEY(metodos_cobro_freelancer_id) REFERENCES metodos_cobro_freelancer(metodos_cobro_freelancer_id),
	FOREIGN KEY(metodos_pago_cliente_id) REFERENCES metodos_pago_cliente(metodos_pago_cliente_id)
); -- Ya definida en una java.class por JoaquinDev22

-- =============================== --
--       BOOTCAMP                  --
-- =============================== --

CREATE TABLE instructor (
    instructor_id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    titulo VARCHAR(100) NOT NULL,
    presentacion VARCHAR(255) NOT NULL,
    tipo ENUM('PARTICULAR', 'EMPRESA'),
    perfil_id INT NOT NULL,
	empresa_id INT,
    FOREIGN KEY(perfil_id) REFERENCES perfil(perfil_id),
	FOREIGN KEY(empresa_id) REFERENCES empresa(empresa_id)
); -- Ya definida en una java.class por Yamils06

CREATE TABLE bootcamp(
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
    FOREIGN KEY(instructor_id) REFERENCES instructor(instructor_id),
	FOREIGN KEY(empresa_id) REFERENCES empresa(empresa_id)
); -- Ya definida en una java.class por Yamils06

CREATE TABLE modulo_bootcamp (
    modulo_id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    descripcion TEXT,
    link_clase VARCHAR(255),
    horario_clase VARCHAR(100),
    bootcamp_id INT NOT NULL,
    FOREIGN KEY (bootcamp_id) REFERENCES bootcamp(bootcamp_id) ON DELETE CASCADE
); -- Ya definida en una java.class por JoaquinDev22

CREATE TABLE bootcamp_etiquetas(
    bootcamp_id INT NOT NULL,
    etiqueta VARCHAR(255) NOT NULL,
    FOREIGN KEY(bootcamp_id) REFERENCES bootcamp(bootcamp_id)
); -- Ya definida en una java.class.List por JoaquinDev22

CREATE TABLE inscripcion_bootcamp(
    inscripcion_id INT AUTO_INCREMENT PRIMARY KEY,
    fecha_inscripcion DATE  NOT NULL,
    estado ENUM('INSCRIPTO','PENDIENTE','CANCELADO', 'COMPLETADO'),
    bootcamp_id INT NOT NULL,
    employee_id INT NOT NULL,
    FOREIGN KEY(bootcamp_id) REFERENCES bootcamp(bootcamp_id),
	FOREIGN KEY(employee_id) REFERENCES employee(employee_id)
); -- Ya definida en una java.class por Yamils06

CREATE TABLE calificacion_bootcamp(
    calificacion_id INT AUTO_INCREMENT PRIMARY KEY,
    puntaje INT NOT NULL,
    comentario VARCHAR(255) NOT NULL,
    fecha_comentario DATE NOT NULL,
    bootcamp_id INT NOT NULL,
    FOREIGN KEY(bootcamp_id) REFERENCES bootcamp(bootcamp_id)
); -- Ya definida en una java.class por Yamils06

CREATE TABLE certificado_bootcamp (
    certificado_id INT AUTO_INCREMENT PRIMARY KEY,
    fecha_emision DATE NOT NULL,
    codigo_certificado VARCHAR(100) NOT NULL UNIQUE,
    bootcamp_id INT NOT NULL,
    employee_id INT NOT NULL,
    FOREIGN KEY (bootcamp_id) REFERENCES bootcamp(bootcamp_id),
    FOREIGN KEY (employee_id) REFERENCES employee(employee_id)
); -- Ya definida en una java.class por Yamils06

CREATE TABLE calificacion_usuario(
    calificacion_usuario_id INT AUTO_INCREMENT PRIMARY KEY,
    calificacion_id INT,
    calificacion_bootcamp_id INT,
    perfil_id INT NOT NULL,
    FOREIGN KEY (calificacion_id) REFERENCES calificacion_proyecto(calificacion_id),
    FOREIGN KEY (calificacion_bootcamp_id) REFERENCES calificacion_bootcamp(calificacion_id),
    FOREIGN KEY (perfil_id) REFERENCES perfil(perfil_id)
); -- Ya definida en una java.class por JoaquinDev22

-- ===================== --
--       AUDITORIA       --
-- ===================== --

CREATE TABLE auditoria_operaciones (
    auditoria_id INT AUTO_INCREMENT PRIMARY KEY,
    tabla_afectada VARCHAR(100) NOT NULL,
    operacion VARCHAR(50) NOT NULL,
    id_registro_afectado INT NOT NULL,
    usuario VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    fecha_operacion DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE auditoria_errores (
    auditoria_error_id INT AUTO_INCREMENT PRIMARY KEY,
    procedimiento VARCHAR(100) NOT NULL,
    operacion VARCHAR(100) NOT NULL,
    mensaje_error VARCHAR(255) NOT NULL,
    fecha_error DATETIME DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO rol (nombre_rol) VALUES 
('EMPRESA'), ('CLIENTE'), ('INSTRUCTOR'), ('FREELANCER'), ('POSTULANTE'), ('RECRUITER');

-- ========================================= --
--                 Test Data                 --
-- ========================================= --

-- IMPORTANTE: Para evitar duplicados, hacemos UPDATE en lugar de INSERT a las entidades ya creadas por la API.

-- 1. Actualizar empresa (TechCorp - perfil_id = 4)
UPDATE empresa SET nombre = 'TechCorp', descripcion = 'Soluciones de software corporativo', sitio_web = 'https://techcorp.com', ubicacion = 'Madrid, España', tamano = '50-200 empleados', sector = 'Tecnología' WHERE perfil_id = 4;

-- 2. Insertar recruiter (AnaRecruiter - perfil_id = 1). AuthService NO crea Recruiter automáticamente, así que lo insertamos.
INSERT INTO recruiter (nombre, apellido, email, fecha_nacimiento, area, empresa_id, perfil_id) VALUES 
('Ana', 'Recruiter', 'ana.recruiter@ejemplo.com', '1985-05-12', 'Recursos Humanos', 1, 1);

-- 3. Actualizar cliente_particular (ClienteGlobal - perfil_id = 3)
UPDATE cliente_particular SET nombre = 'Cliente', apellido = 'Global', email = 'cliente@global.com', fecha_nacimiento = '1980-08-20', telefono = '+34600123456', pais = 'España' WHERE perfil_id = 3;

-- 4. Actualizar instructor (ProfeBootcamp - perfil_id = 6)
UPDATE instructor SET nombre = 'Profe', apellido = 'Bootcamp', titulo = 'Senior Frontend Developer', presentacion = 'Apasionada por la enseñanza.', tipo = 'PARTICULAR' WHERE perfil_id = 6;

-- 5. Actualizar employee Freelancer (DevFreelancer - perfil_id = 2)
UPDATE employee SET nombre = 'Dev', apellido = 'Freelancer', email = 'freelancer@ejemplo.com', fecha_nacimiento = '1995-02-15', freelance = TRUE, enterprise = FALSE WHERE perfil_id = 2;

-- 6. Actualizar employee Postulante (CandidatoIT - perfil_id = 5)
UPDATE employee SET nombre = 'Candidato', apellido = 'IT', email = 'candidato@it.com', fecha_nacimiento = '1998-07-22', freelance = FALSE, enterprise = FALSE WHERE perfil_id = 5;

-- A partir de aquí todo son INSERTs normales de los datos secundarios (proyectos, habilidades, etc.)

INSERT INTO habilidad (nombre_habilidad) VALUES 
('React'), ('Node.js'), ('Figma'), ('TypeScript'), ('Python');

-- Habilidades para DevFreelancer (employee_id = 1) y CandidatoIT (employee_id = 2)
INSERT INTO employee_habilidad (employee_id, habilidad_id, nivel) VALUES 
(1, 1, 5), (1, 4, 4), -- DevFreelancer: React y TS
(2, 2, 5), (2, 5, 4); -- CandidatoIT: Node y Python

INSERT INTO experiencia (titulo, descripcion, fecha_inicio, fecha_fin, employee_id) VALUES 
('Frontend Developer', 'Desarrollo de interfaces en React para E-commerce.', '2020-01-01', '2023-01-01', 1),
('Backend Engineer', 'Creación de APIs REST con Node.js.', '2019-05-01', '2024-02-01', 2);

INSERT INTO portfolio (github, linkedin, descripcion, employee_id) VALUES 
('https://github.com/devfreelancer', 'https://linkedin.com/in/devfreelancer', 'Portafolio de proyectos React', 1);

INSERT INTO vacante (puesto, descripcion, fecha_publicacion, fecha_fin, logo, ubicacion, tipo_ubicacion, tipo_vacante, salario, empresa_id) VALUES 
('Frontend React SSR', 'Buscamos desarrollador para migrar app a Next.js', '2026-06-01', '2026-07-01', '🏢', 'Remoto', 'Remoto', 'Tiempo Completo', '$3000 USD/mes', 1);

INSERT INTO vacante_etiquetas (vacante_id, etiqueta) VALUES 
(1, 'React'), (1, 'Next.js');

INSERT INTO postulacion_vacante (fecha_postulacion, estado_postulacion, cv, mensaje, vacante_id, postulante_id) VALUES 
('2026-06-15', 'PENDIENTE', '', 'Me interesa mucho la vacante.', 1, 2);

INSERT INTO proyecto (area_proyecto, descripcion, fecha_creacion, presupuesto_max, presupuesto_min, duracion, tipo_pago, estado, freelancer_asignado_id, cliente_id) VALUES 
('Diseño Web', 'Rediseño completo de mi sitio corporativo en Figma y React.', '2026-05-10', 2500.00, 1500.00, '1 mes', 'Precio Fijo', 'EN_PROGRESO', 1, 1),
('API en Node.js', 'Crear una API de facturación.', '2026-06-10', 1000.00, 800.00, '2 semanas', 'Por Hora', 'BUSCANDO_TALENTO', NULL, 1);

INSERT INTO proyecto_habilidades (proyecto_id, habilidad) VALUES 
(1, 'React'), (1, 'Figma'),
(2, 'Node.js'), (2, 'SQL');

INSERT INTO postulacion_proyecto (descripcion, valor_propuesto, fecha_postulacion, estado_postulacion, employee_id, proyecto_id) VALUES 
('Hola, soy experto en React y Figma. Puedo hacerlo en 3 semanas.', 2000.00, '2026-05-12', 'ACEPTADA', 1, 1);

INSERT INTO hito_proyecto (titulo, descripcion, monto, estado, fecha_limite, proyecto_id) VALUES 
('Wireframes y UX', 'Diseño de pantallas iniciales en Figma.', 500.00, 'COMPLETADO', '2026-05-20', 1),
('Desarrollo Frontend', 'Maquetación e integración de React.', 1500.00, 'PENDIENTE', '2026-06-25', 1);

INSERT INTO calificacion_proyecto (puntaje, titulo_resena, comentario, fecha_comentario, proyecto_id) VALUES 
(5, 'Trabajo excelente y rápido', 'Entregó la app móvil antes de tiempo con cero bugs.', '2026-04-15', 1);

INSERT INTO bootcamp (titulo, descripcion, fecha_inicio, fecha_fin, modalidad, tipo, cupos, logo, precio, rating, instructor_id, empresa_id) VALUES 
('Master en React', 'Domina React, Hooks, Context y Next.js desde cero.', '2026-08-01', '2026-12-01', 'VIRTUAL', 'PARTICULAR', 100, '⚛️', '150.00', 4.9, 1, NULL);

INSERT INTO bootcamp_etiquetas (bootcamp_id, etiqueta) VALUES 
(1, 'React'), (1, 'Frontend');

INSERT INTO modulo_bootcamp (titulo, descripcion, link_clase, horario_clase, bootcamp_id) VALUES 
('Introducción a React', 'Conceptos básicos, JSX y componentes.', 'https://zoom.us/clase1', 'Lunes 18:00', 1),
('Hooks y Estado', 'useState, useEffect y custom hooks.', 'https://zoom.us/clase2', 'Miércoles 18:00', 1);

INSERT INTO inscripcion_bootcamp (fecha_inscripcion, estado, bootcamp_id, employee_id) VALUES 
('2026-06-16', 'INSCRIPTO', 1, 1);
