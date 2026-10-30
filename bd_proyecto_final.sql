-- ===================================================================================
-- DATABASE SCHEMA: bd_proyecto_final
-- ENGINE: MySQL 8.0+
-- FEATURES: Clean DDL, Foreign Keys, B-Tree Indexes, Views, Functions, Procedures, Triggers & Security GRANTs
-- ===================================================================================

DROP DATABASE IF EXISTS bd_proyecto_final;
CREATE DATABASE bd_proyecto_final CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE bd_proyecto_final;

-- ===================================================================================
-- 1. TABLAS Y ENTIDADES PRINCIPALES
-- ===================================================================================

CREATE TABLE rol (
    rol_id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_rol VARCHAR(50) UNIQUE NOT NULL
) ENGINE=InnoDB;

CREATE TABLE perfil (
    perfil_id INT AUTO_INCREMENT PRIMARY KEY,
    uuid CHAR(36) NOT NULL UNIQUE DEFAULT (UUID()),
    foto_perfil LONGBLOB,
    banner_perfil LONGBLOB,
    ubicacion VARCHAR(150),
    descripcion VARCHAR(500),
    usuario VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    rol_id INT NOT NULL,
    FOREIGN KEY (rol_id) REFERENCES rol(rol_id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE empresa (
    empresa_id INT AUTO_INCREMENT PRIMARY KEY,
    uuid CHAR(36) NOT NULL UNIQUE DEFAULT (UUID()),
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    sitio_web VARCHAR(100) NOT NULL,
    ubicacion VARCHAR(100) NOT NULL,
    tamano VARCHAR(50),
    sector VARCHAR(100),
    perfil_id INT NOT NULL UNIQUE,
    FOREIGN KEY (perfil_id) REFERENCES perfil(perfil_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE recruiter (
    recruiter_id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    area VARCHAR(50) NOT NULL,
    empresa_id INT NOT NULL,
    perfil_id INT NOT NULL UNIQUE,
    FOREIGN KEY (perfil_id) REFERENCES perfil(perfil_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (empresa_id) REFERENCES empresa(empresa_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE employee (
    employee_id INT AUTO_INCREMENT PRIMARY KEY,
    uuid CHAR(36) NOT NULL UNIQUE DEFAULT (UUID()),
    nombre VARCHAR(50) NOT NULL, 
    apellido VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    freelance BOOLEAN DEFAULT FALSE,
    enterprise BOOLEAN DEFAULT FALSE,
    perfil_id INT NOT NULL UNIQUE,
    FOREIGN KEY (perfil_id) REFERENCES perfil(perfil_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE cliente_particular (
    cliente_particular_id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    telefono VARCHAR(50) NOT NULL,
    pais VARCHAR(100) NOT NULL,
    perfil_id INT NOT NULL UNIQUE,
    FOREIGN KEY (perfil_id) REFERENCES perfil(perfil_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE experiencia (
    experiencia_id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE,
    employee_id INT NOT NULL,
    FOREIGN KEY (employee_id) REFERENCES employee(employee_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT chk_experiencia_fechas CHECK (fecha_fin IS NULL OR fecha_fin >= fecha_inicio)
) ENGINE=InnoDB;

CREATE TABLE proyecto (
    proyecto_id INT AUTO_INCREMENT PRIMARY KEY,
    uuid CHAR(36) NOT NULL UNIQUE DEFAULT (UUID()),
    nombre_proyecto VARCHAR(30) NOT NULL,
    area_proyecto VARCHAR(10) NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    fecha_creacion DATE NOT NULL DEFAULT (CURRENT_DATE),
    presupuesto_max DECIMAL(10,2) NOT NULL,
    presupuesto_min DECIMAL(10,2) NOT NULL,
    duracion VARCHAR(50),
    tipo_pago VARCHAR(50),
    estado ENUM('BUSCANDO_TALENTO', 'EN_PROGRESO', 'FINALIZADO') DEFAULT 'BUSCANDO_TALENTO',
    freelancer_asignado_id INT,
    cliente_id INT NOT NULL,
    FOREIGN KEY (cliente_id) REFERENCES cliente_particular(cliente_particular_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (freelancer_asignado_id) REFERENCES employee(employee_id) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT chk_proyecto_presupuesto CHECK (presupuesto_max >= presupuesto_min)
) ENGINE=InnoDB;

CREATE TABLE hito_proyecto (
    hito_id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    monto DECIMAL(10,2) NOT NULL,
    estado ENUM('PENDIENTE', 'COMPLETADO', 'PAGADO') DEFAULT 'PENDIENTE',
    fecha_limite DATE,
    proyecto_id INT NOT NULL,
    FOREIGN KEY (proyecto_id) REFERENCES proyecto(proyecto_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE proyecto_habilidades (
    proyecto_id INT NOT NULL,
    habilidad VARCHAR(255) NOT NULL,
    FOREIGN KEY (proyecto_id) REFERENCES proyecto(proyecto_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE postulacion_proyecto (
    post_proy_id INT AUTO_INCREMENT PRIMARY KEY,
    descripcion VARCHAR(255) NOT NULL,
    valor_propuesto DECIMAL(10,2) NOT NULL,
    fecha_postulacion DATE NOT NULL DEFAULT (CURRENT_DATE),
    estado_postulacion ENUM('PENDIENTE', 'ACEPTADA', 'RECHAZADA') DEFAULT 'PENDIENTE',
    employee_id INT NOT NULL,
    proyecto_id INT NOT NULL,
    FOREIGN KEY (employee_id) REFERENCES employee(employee_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (proyecto_id) REFERENCES proyecto(proyecto_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT uq_postulacion_proyecto UNIQUE (proyecto_id, employee_id)
) ENGINE=InnoDB;

CREATE TABLE calificacion_proyecto (
    calificacion_id INT AUTO_INCREMENT PRIMARY KEY,
    puntaje INT NOT NULL,
    titulo_resena VARCHAR(100),
    comentario VARCHAR(255) NOT NULL,
    fecha_comentario DATE NOT NULL DEFAULT (CURRENT_DATE),
    proyecto_id INT NOT NULL,
    FOREIGN KEY (proyecto_id) REFERENCES proyecto(proyecto_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT chk_puntaje_proyecto CHECK (puntaje BETWEEN 1 AND 5)
) ENGINE=InnoDB;

CREATE TABLE habilidad (
    habilidad_id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_habilidad VARCHAR(50) UNIQUE NOT NULL
) ENGINE=InnoDB;

CREATE TABLE employee_habilidad (
    employee_id INT NOT NULL,
    habilidad_id INT NOT NULL,
    nivel INT NOT NULL DEFAULT 1,
    PRIMARY KEY (employee_id, habilidad_id),
    FOREIGN KEY (employee_id) REFERENCES employee(employee_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (habilidad_id) REFERENCES habilidad(habilidad_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT chk_habilidad_nivel CHECK (nivel BETWEEN 1 AND 5)
) ENGINE=InnoDB;

CREATE TABLE vacante (
    vacante_id INT AUTO_INCREMENT PRIMARY KEY,
    uuid CHAR(36) NOT NULL UNIQUE DEFAULT (UUID()),
    puesto VARCHAR(50) NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    fecha_publicacion DATE NOT NULL DEFAULT (CURRENT_DATE),
    fecha_fin DATE NOT NULL,
    logo VARCHAR(255),
    ubicacion VARCHAR(100),
    tipo_ubicacion VARCHAR(50),
    tipo_vacante VARCHAR(50),
    salario VARCHAR(50),
    empresa_id INT NOT NULL,
    FOREIGN KEY (empresa_id) REFERENCES empresa(empresa_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT chk_vacante_fechas CHECK (fecha_fin >= fecha_publicacion)
) ENGINE=InnoDB;

CREATE TABLE vacante_etiquetas (
    vacante_id INT NOT NULL,
    etiqueta VARCHAR(255) NOT NULL,
    FOREIGN KEY (vacante_id) REFERENCES vacante(vacante_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE postulacion_vacante (
    postulacion_id INT AUTO_INCREMENT PRIMARY KEY,
    fecha_postulacion DATE NOT NULL DEFAULT (CURRENT_DATE),
    estado_postulacion ENUM('PENDIENTE','VISUALIZADO','RECHAZADO') DEFAULT 'PENDIENTE',
    cv LONGBLOB NOT NULL,
    mensaje VARCHAR(255),
    vacante_id INT NOT NULL,
    postulante_id INT NOT NULL,
    FOREIGN KEY (vacante_id) REFERENCES vacante(vacante_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (postulante_id) REFERENCES employee(employee_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT uq_postulacion_vacante UNIQUE (vacante_id, postulante_id)
) ENGINE=InnoDB;

CREATE TABLE vacante_habilidad (
    vacante_id INT NOT NULL,
    habilidad_id INT NOT NULL,
    PRIMARY KEY (vacante_id, habilidad_id),
    FOREIGN KEY (vacante_id) REFERENCES vacante(vacante_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (habilidad_id) REFERENCES habilidad(habilidad_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE portfolio (
    portfolio_id INT AUTO_INCREMENT PRIMARY KEY,
    github VARCHAR(150),
    linkedin VARCHAR(150),
    cv LONGBLOB,
    descripcion VARCHAR(255) NOT NULL,
    employee_id INT NOT NULL UNIQUE,
    FOREIGN KEY (employee_id) REFERENCES employee(employee_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ===================================================================================
-- 2. TABLAS DE PAGOS
-- ===================================================================================

CREATE TABLE pagos (
    pago_id INT AUTO_INCREMENT PRIMARY KEY,
    moneda ENUM('USD','PESOS') DEFAULT 'USD',
    monto DECIMAL(10,2) NOT NULL,
    medio_de_pago ENUM('TARJETA', 'BILLETERA_VIRTUAL') NOT NULL,
    proyecto_id INT NOT NULL,
    FOREIGN KEY (proyecto_id) REFERENCES proyecto(proyecto_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE metodos_cobro_freelancer (
    metodos_cobro_freelancer_id INT AUTO_INCREMENT PRIMARY KEY,
    titular VARCHAR(100) NOT NULL,
    dni VARCHAR(50) UNIQUE NOT NULL,
    entidad_bancaria ENUM('BANCO', 'BILLETERA_VIRTUAL') NOT NULL,
    numero_cuenta VARCHAR(100) UNIQUE NOT NULL,
    employee_id INT NOT NULL,
    FOREIGN KEY (employee_id) REFERENCES employee(employee_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE metodos_pago_cliente (
    metodos_pago_cliente_id INT AUTO_INCREMENT PRIMARY KEY,
    titular VARCHAR(50) NOT NULL,
    dni VARCHAR(50) UNIQUE NOT NULL,
    entidad_bancaria ENUM('BANCO', 'BILLETERA_VIRTUAL') NOT NULL,
    cvu VARCHAR(50) UNIQUE NOT NULL,
    tipo_tarjeta ENUM('DEBITO','CREDITO'),
    empresa_tarjeta ENUM('VISA','MASTERCARD','CABAL'),
    numero_tarjeta VARCHAR(50) UNIQUE NOT NULL,
    cliente_id INT NOT NULL,
    FOREIGN KEY (cliente_id) REFERENCES cliente_particular(cliente_particular_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE pagos_cliente_freelancer (
    pagos_cliente_freelancer_id INT AUTO_INCREMENT PRIMARY KEY,
    pago_id INT NOT NULL,
    metodos_cobro_freelancer_id INT NOT NULL,
    metodos_pago_cliente_id INT NOT NULL,
    FOREIGN KEY (pago_id) REFERENCES pagos(pago_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (metodos_cobro_freelancer_id) REFERENCES metodos_cobro_freelancer(metodos_cobro_freelancer_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (metodos_pago_cliente_id) REFERENCES metodos_pago_cliente(metodos_pago_cliente_id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ===================================================================================
-- 3. TABLAS DE BOOTCAMPS
-- ===================================================================================

CREATE TABLE instructor (
    instructor_id INT AUTO_INCREMENT PRIMARY KEY,
    uuid CHAR(36) NOT NULL UNIQUE DEFAULT (UUID()),
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    titulo VARCHAR(100) NOT NULL,
    presentacion VARCHAR(255) NOT NULL,
    tipo ENUM('PARTICULAR', 'EMPRESA') DEFAULT 'PARTICULAR',
    perfil_id INT NOT NULL UNIQUE,
    empresa_id INT,
    FOREIGN KEY (perfil_id) REFERENCES perfil(perfil_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (empresa_id) REFERENCES empresa(empresa_id) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE bootcamp (
    bootcamp_id INT AUTO_INCREMENT PRIMARY KEY,
    uuid CHAR(36) NOT NULL UNIQUE DEFAULT (UUID()),
    titulo VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    modalidad ENUM('VIRTUAL', 'PRESENCIAL') DEFAULT 'VIRTUAL',
    tipo ENUM('PARTICULAR', 'EMPRESA') DEFAULT 'PARTICULAR',
    cupos INT NOT NULL,
    logo VARCHAR(255),
    precio DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    rating DECIMAL(3,1) DEFAULT 0.0,
    instructor_id INT NOT NULL,
    empresa_id INT,
    FOREIGN KEY (instructor_id) REFERENCES instructor(instructor_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (empresa_id) REFERENCES empresa(empresa_id) ON DELETE SET NULL ON UPDATE CASCADE,
    CONSTRAINT chk_bootcamp_fechas CHECK (fecha_fin >= fecha_inicio),
    CONSTRAINT chk_bootcamp_cupos CHECK (cupos >= 0)
) ENGINE=InnoDB;

CREATE TABLE modulo_bootcamp (
    modulo_id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    descripcion TEXT,
    link_clase VARCHAR(255),
    horario_clase VARCHAR(100),
    bootcamp_id INT NOT NULL,
    FOREIGN KEY (bootcamp_id) REFERENCES bootcamp(bootcamp_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE bootcamp_etiquetas (
    bootcamp_id INT NOT NULL,
    etiqueta VARCHAR(255) NOT NULL,
    FOREIGN KEY (bootcamp_id) REFERENCES bootcamp(bootcamp_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

CREATE TABLE inscripcion_bootcamp (
    inscripcion_id INT AUTO_INCREMENT PRIMARY KEY,
    fecha_inscripcion DATE NOT NULL DEFAULT (CURRENT_DATE),
    estado ENUM('INSCRIPTO','PENDIENTE','CANCELADO', 'COMPLETADO') DEFAULT 'INSCRIPTO',
    bootcamp_id INT NOT NULL,
    employee_id INT NOT NULL,
    FOREIGN KEY (bootcamp_id) REFERENCES bootcamp(bootcamp_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (employee_id) REFERENCES employee(employee_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT uq_inscripcion_bootcamp UNIQUE (bootcamp_id, employee_id)
) ENGINE=InnoDB;

CREATE TABLE calificacion_bootcamp (
    calificacion_id INT AUTO_INCREMENT PRIMARY KEY,
    puntaje INT NOT NULL,
    comentario VARCHAR(255) NOT NULL,
    fecha_comentario DATE NOT NULL DEFAULT (CURRENT_DATE),
    bootcamp_id INT NOT NULL,
    employee_id INT NOT NULL,
    FOREIGN KEY (bootcamp_id) REFERENCES bootcamp(bootcamp_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (employee_id) REFERENCES employee(employee_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT chk_puntaje_bootcamp CHECK (puntaje BETWEEN 1 AND 5),
    CONSTRAINT uq_calificacion_bootcamp UNIQUE (bootcamp_id, employee_id)
) ENGINE=InnoDB;

CREATE TABLE certificado_bootcamp (
    certificado_id INT AUTO_INCREMENT PRIMARY KEY,
    fecha_emision DATE NOT NULL DEFAULT (CURRENT_DATE),
    codigo_certificado VARCHAR(100) NOT NULL UNIQUE,
    bootcamp_id INT NOT NULL,
    employee_id INT NOT NULL,
    FOREIGN KEY (bootcamp_id) REFERENCES bootcamp(bootcamp_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (employee_id) REFERENCES employee(employee_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT uq_certificado_bootcamp UNIQUE (bootcamp_id, employee_id)
) ENGINE=InnoDB;

CREATE TABLE calificacion_usuario (
    calificacion_usuario_id INT AUTO_INCREMENT PRIMARY KEY,
    calificacion_id INT,
    calificacion_bootcamp_id INT,
    perfil_id INT NOT NULL,
    FOREIGN KEY (calificacion_id) REFERENCES calificacion_proyecto(calificacion_id) ON DELETE SET NULL ON UPDATE CASCADE,
    FOREIGN KEY (calificacion_bootcamp_id) REFERENCES calificacion_bootcamp(calificacion_id) ON DELETE SET NULL ON UPDATE CASCADE,
    FOREIGN KEY (perfil_id) REFERENCES perfil(perfil_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ===================================================================================
-- 4. TABLAS DE AUDITORIA Y CONTROL DE ERRORES
-- ===================================================================================

CREATE TABLE auditoria_operaciones (
    auditoria_id INT AUTO_INCREMENT PRIMARY KEY,
    tabla_afectada VARCHAR(100) NOT NULL,
    operacion VARCHAR(50) NOT NULL,
    id_registro_afectado INT NOT NULL,
    usuario VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    fecha_operacion DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE auditoria_errores (
    auditoria_error_id INT AUTO_INCREMENT PRIMARY KEY,
    procedimiento VARCHAR(100) NOT NULL,
    operacion VARCHAR(100) NOT NULL,
    mensaje_error VARCHAR(255) NOT NULL,
    fecha_error DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ===================================================================================
-- 5. INDICES B-TREE DE ALTO RENDIMIENTO
-- ===================================================================================

CREATE INDEX idx_perfil_usuario ON perfil(usuario);
CREATE INDEX idx_perfil_email ON perfil(email);
CREATE INDEX idx_perfil_uuid ON perfil(uuid);

CREATE INDEX idx_empresa_perfil ON empresa(perfil_id);
CREATE INDEX idx_empresa_uuid ON empresa(uuid);

CREATE INDEX idx_employee_perfil ON employee(perfil_id);
CREATE INDEX idx_employee_uuid ON employee(uuid);

CREATE INDEX idx_vacante_empresa ON vacante(empresa_id);
CREATE INDEX idx_vacante_fechas ON vacante(fecha_publicacion, fecha_fin);

CREATE INDEX idx_proyecto_cliente ON proyecto(cliente_id);
CREATE INDEX idx_proyecto_freelancer ON proyecto(freelancer_asignado_id);
CREATE INDEX idx_proyecto_estado ON proyecto(estado);

CREATE INDEX idx_bootcamp_instructor ON bootcamp(instructor_id);
CREATE INDEX idx_bootcamp_cupos_fechas ON bootcamp(cupos, fecha_inicio);

CREATE INDEX idx_auditoria_fecha_tabla ON auditoria_operaciones(fecha_operacion, tabla_afectada);

-- ===================================================================================
-- 6. VISTAS RESUMIDAS (VIEWS)
-- ===================================================================================

-- Vista de Resumen de Metricas por Empresa
CREATE OR REPLACE VIEW vw_dashboard_empresa AS
SELECT 
    e.empresa_id,
    e.nombre AS empresa_nombre,
    COUNT(DISTINCT v.vacante_id) AS total_vacantes_publicadas,
    COUNT(DISTINCT pv.postulacion_id) AS total_postulaciones_recibidas
FROM empresa e
LEFT JOIN vacante v ON e.empresa_id = v.empresa_id
LEFT JOIN postulacion_vacante pv ON v.vacante_id = pv.vacante_id
GROUP BY e.empresa_id, e.nombre;

-- Vista de Resumen de Bootcamps
CREATE OR REPLACE VIEW vw_resumen_bootcamps AS
SELECT 
    b.bootcamp_id,
    b.titulo AS bootcamp_titulo,
    CONCAT(i.nombre, ' ', i.apellido) AS instructor_nombre,
    b.modalidad,
    b.cupos AS cupos_disponibles,
    COUNT(DISTINCT ib.inscripcion_id) AS total_inscriptos,
    b.rating
FROM bootcamp b
INNER JOIN instructor i ON b.instructor_id = i.instructor_id
LEFT JOIN inscripcion_bootcamp ib ON b.bootcamp_id = ib.bootcamp_id AND ib.estado = 'INSCRIPTO'
GROUP BY b.bootcamp_id, b.titulo, i.nombre, i.apellido, b.modalidad, b.cupos, b.rating;

-- Vista de Proyectos Freelance Activos
CREATE OR REPLACE VIEW vw_proyectos_freelance_activos AS
SELECT 
    p.proyecto_id,
    p.area_proyecto,
    p.descripcion,
    p.presupuesto_min,
    p.presupuesto_max,
    p.estado,
    CONCAT(c.nombre, ' ', c.apellido) AS cliente_nombre,
    COUNT(DISTINCT pp.post_proy_id) AS postulaciones_recibidas
FROM proyecto p
INNER JOIN cliente_particular c ON p.cliente_id = c.cliente_particular_id
LEFT JOIN postulacion_proyecto pp ON p.proyecto_id = pp.proyecto_id
WHERE p.estado IN ('BUSCANDO_TALENTO', 'EN_PROGRESO')
GROUP BY p.proyecto_id, p.area_proyecto, p.descripcion, p.presupuesto_min, p.presupuesto_max, p.estado, c.nombre, c.apellido;

-- ===================================================================================
-- 7. FUNCIONES ALMACENADAS (STORED FUNCTIONS)
-- ===================================================================================

DELIMITER //

-- Función para calcular el rating promedio de un bootcamp
CREATE FUNCTION fn_calcular_rating_bootcamp(p_bootcamp_id INT) 
RETURNS DECIMAL(3,1)
DETERMINISTIC
READS SQL DATA
BEGIN
    DECLARE v_rating DECIMAL(3,1);
    SELECT COALESCE(AVG(puntaje), 0.0) INTO v_rating
    FROM calificacion_bootcamp
    WHERE bootcamp_id = p_bootcamp_id;
    RETURN v_rating;
END //

-- Función para verificar cupos disponibles en un bootcamp
CREATE FUNCTION fn_verificar_cupos_disponibles(p_bootcamp_id INT) 
RETURNS BOOLEAN
DETERMINISTIC
READS SQL DATA
BEGIN
    DECLARE v_cupos INT;
    DECLARE v_fecha_inicio DATE;
    
    SELECT cupos, fecha_inicio INTO v_cupos, v_fecha_inicio
    FROM bootcamp
    WHERE bootcamp_id = p_bootcamp_id;
    
    IF v_cupos > 0 AND v_fecha_inicio > CURRENT_DATE() THEN
        RETURN TRUE;
    ELSE
        RETURN FALSE;
    END IF;
END //

DELIMITER ;

-- ===================================================================================
-- 8. TRIGGERS AUTOMATICOS
-- ===================================================================================

DELIMITER //

-- Trigger: Recalcular rating de bootcamp tras insertar una calificación
CREATE TRIGGER trg_bootcamp_rating_after_insert
AFTER INSERT ON calificacion_bootcamp
FOR EACH ROW
BEGIN
    UPDATE bootcamp
    SET rating = fn_calcular_rating_bootcamp(NEW.bootcamp_id)
    WHERE bootcamp_id = NEW.bootcamp_id;
END //

-- Trigger: Recalcular rating de bootcamp tras actualizar una calificación
CREATE TRIGGER trg_bootcamp_rating_after_update
AFTER UPDATE ON calificacion_bootcamp
FOR EACH ROW
BEGIN
    UPDATE bootcamp
    SET rating = fn_calcular_rating_bootcamp(NEW.bootcamp_id)
    WHERE bootcamp_id = NEW.bootcamp_id;
END //

-- Trigger: Registrar auditoría si se deshabilita un perfil
CREATE TRIGGER trg_auditoria_perfil_update
AFTER UPDATE ON perfil
FOR EACH ROW
BEGIN
    IF OLD.is_active = TRUE AND NEW.is_active = FALSE THEN
        INSERT INTO auditoria_operaciones (tabla_afectada, operacion, id_registro_afectado, usuario, descripcion)
        VALUES ('perfil', 'DESACTIVAR', NEW.perfil_id, NEW.usuario, 'El usuario deshabilitó su cuenta.');
    END IF;
END //

DELIMITER ;

-- ===================================================================================
-- 9. PROCEDIMIENTOS ALMACENADOS TRANSACCIONALES (STORED PROCEDURES)
-- ===================================================================================

DELIMITER //

-- Procedimiento Atómico para Inscribirse a un Bootcamp
CREATE PROCEDURE sp_inscribir_bootcamp(
    IN p_bootcamp_id INT,
    IN p_employee_id INT,
    OUT p_resultado VARCHAR(100)
)
BEGIN
    DECLARE v_disponible BOOLEAN;
    DECLARE v_ya_inscripto INT;
    
    -- Handler de errores para rollback automático
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET p_resultado = 'ERROR: Fallo transaccional al realizar la inscripción.';
        INSERT INTO auditoria_errores (procedimiento, operacion, mensaje_error)
        VALUES ('sp_inscribir_bootcamp', 'INSERT', 'Error durante la ejecución del procedimiento.');
    END;

    START TRANSACTION;
    
    -- 1. Validar disponibilidad de cupos
    SET v_disponible = fn_verificar_cupos_disponibles(p_bootcamp_id);
    IF NOT v_disponible THEN
        SET p_resultado = 'RECHAZADO: No hay cupos disponibles o el bootcamp ya inició.';
        ROLLBACK;
    ELSE
        -- 2. Verificar que no esté previamente inscripto
        SELECT COUNT(*) INTO v_ya_inscripto 
        FROM inscripcion_bootcamp 
        WHERE bootcamp_id = p_bootcamp_id AND employee_id = p_employee_id;
        
        IF v_ya_inscripto > 0 THEN
            SET p_resultado = 'RECHAZADO: El alumno ya se encuentra inscripto.';
            ROLLBACK;
        ELSE
            -- 3. Descontar cupo
            UPDATE bootcamp SET cupos = cupos - 1 WHERE bootcamp_id = p_bootcamp_id;
            
            -- 4. Registrar inscripción
            INSERT INTO inscripcion_bootcamp (fecha_inscripcion, estado, bootcamp_id, employee_id)
            VALUES (CURRENT_DATE(), 'INSCRIPTO', p_bootcamp_id, p_employee_id);
            
            -- 5. Registrar auditoría
            INSERT INTO auditoria_operaciones (tabla_afectada, operacion, id_registro_afectado, usuario, descripcion)
            VALUES ('inscripcion_bootcamp', 'INSERT', LAST_INSERT_ID(), 'Sistema', CONCAT('Inscripción exitosa al bootcamp ID: ', p_bootcamp_id));
            
            COMMIT;
            SET p_resultado = 'EXITO: Inscripción completada correctamente.';
        END IF;
    END IF;
END //

-- Procedimiento Atómico para Postular a una Vacante Laboral
CREATE PROCEDURE sp_postular_vacante(
    IN p_vacante_id INT,
    IN p_postulante_id INT,
    IN p_cv LONGBLOB,
    IN p_mensaje VARCHAR(255),
    OUT p_resultado VARCHAR(100)
)
BEGIN
    DECLARE v_ya_postulado INT;
    
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET p_resultado = 'ERROR: Fallo transaccional al realizar la postulación.';
        INSERT INTO auditoria_errores (procedimiento, operacion, mensaje_error)
        VALUES ('sp_postular_vacante', 'INSERT', 'Error durante la ejecución del procedimiento.');
    END;

    START TRANSACTION;
    
    SELECT COUNT(*) INTO v_ya_postulado 
    FROM postulacion_vacante 
    WHERE vacante_id = p_vacante_id AND postulante_id = p_postulante_id;
    
    IF v_ya_postulado > 0 THEN
        SET p_resultado = 'RECHAZADO: El postulante ya aplicó a esta vacante.';
        ROLLBACK;
    ELSE
        INSERT INTO postulacion_vacante (fecha_postulacion, estado_postulacion, cv, mensaje, vacante_id, postulante_id)
        VALUES (CURRENT_DATE(), 'PENDIENTE', p_cv, p_mensaje, p_vacante_id, p_postulante_id);
        
        INSERT INTO auditoria_operaciones (tabla_afectada, operacion, id_registro_afectado, usuario, descripcion)
        VALUES ('postulacion_vacante', 'INSERT', LAST_INSERT_ID(), 'Sistema', CONCAT('Postulación exitosa a la vacante ID: ', p_vacante_id));
        
        COMMIT;
        SET p_resultado = 'EXITO: Postulación registrada correctamente.';
    END IF;
END //

DELIMITER ;

-- ===================================================================================
-- 10. SEGURIDAD Y PERMISOS DE USUARIOS (GRANTs & LEAST PRIVILEGE)
-- ===================================================================================

-- Crear usuario exclusivo para la aplicación Spring Boot
CREATE USER IF NOT EXISTS 'app_user'@'%' IDENTIFIED BY 'AppSecurePass2026!';

-- Otorgar solo permisos DML y de Ejecución (Sin permisos DDL de alteración/borrado de esquema en prod)
GRANT SELECT, INSERT, UPDATE, DELETE, EXECUTE ON bd_proyecto_final.* TO 'app_user'@'%';

FLUSH PRIVILEGES;

-- ===================================================================================
-- 11. DATOS INICIALES (SEED DATA)
-- ===================================================================================

INSERT INTO rol (nombre_rol) VALUES 
('EMPRESA'), ('CLIENTE'), ('INSTRUCTOR'), ('FREELANCER'), ('POSTULANTE'), ('RECRUITER');

-- 1. perfiles iniciales
INSERT INTO perfil (usuario, email, password, is_active, rol_id) VALUES
('AnaRecruiter', 'ana.recruiter@ejemplo.com', '$2a$10$e8Za3...', TRUE, 6),
('DevFreelancer', 'freelancer@ejemplo.com', '$2a$10$e8Za3...', TRUE, 4),
('ClienteGlobal', 'cliente@global.com', '$2a$10$e8Za3...', TRUE, 2),
('TechCorp', 'empresa@techcorp.com', '$2a$10$e8Za3...', TRUE, 1),
('CandidatoIT', 'candidato@it.com', '$2a$10$e8Za3...', TRUE, 5),
('ProfeBootcamp', 'profe@bootcamp.com', '$2a$10$e8Za3...', TRUE, 3);

-- 2. Entidades vinculadas
INSERT INTO empresa (nombre, descripcion, sitio_web, ubicacion, tamano, sector, perfil_id) VALUES 
('TechCorp', 'Soluciones de software corporativo', 'https://techcorp.com', 'Madrid, España', '50-200 empleados', 'Tecnología', 4);

INSERT INTO recruiter (nombre, apellido, email, fecha_nacimiento, area, empresa_id, perfil_id) VALUES 
('Ana', 'Recruiter', 'ana.recruiter@ejemplo.com', '1985-05-12', 'Recursos Humanos', 1, 1);

INSERT INTO cliente_particular (nombre, apellido, email, fecha_nacimiento, telefono, pais, perfil_id) VALUES 
('Cliente', 'Global', 'cliente@global.com', '1980-08-20', '+34600123456', 'España', 3);

INSERT INTO instructor (nombre, apellido, titulo, presentacion, tipo, perfil_id) VALUES 
('Profe', 'Bootcamp', 'Senior Frontend Developer', 'Apasionada por la enseñanza.', 'PARTICULAR', 6);

INSERT INTO employee (nombre, apellido, email, fecha_nacimiento, freelance, enterprise, perfil_id) VALUES 
('Dev', 'Freelancer', 'freelancer@ejemplo.com', '1995-02-15', TRUE, FALSE, 2),
('Candidato', 'IT', 'candidato@it.com', '1998-07-22', FALSE, FALSE, 5);

-- 3. Habilidades y experiencias
INSERT INTO habilidad (nombre_habilidad) VALUES 
('React'), ('Node.js'), ('Figma'), ('TypeScript'), ('Python');

INSERT INTO employee_habilidad (employee_id, habilidad_id, nivel) VALUES 
(1, 1, 5), (1, 4, 4),
(2, 2, 5), (2, 5, 4);

INSERT INTO experiencia (titulo, descripcion, fecha_inicio, fecha_fin, employee_id) VALUES 
('Frontend Developer', 'Desarrollo de interfaces en React para E-commerce.', '2020-01-01', '2023-01-01', 1),
('Backend Engineer', 'Creación de APIs REST con Node.js.', '2019-05-01', '2024-02-01', 2);

INSERT INTO portfolio (github, linkedin, descripcion, employee_id) VALUES 
('https://github.com/devfreelancer', 'https://linkedin.com/in/devfreelancer', 'Portafolio de proyectos React', 1);

-- 4. Vacantes y postulaciones
INSERT INTO vacante (puesto, descripcion, fecha_publicacion, fecha_fin, logo, ubicacion, tipo_ubicacion, tipo_vacante, salario, empresa_id) VALUES 
('Frontend React SSR', 'Buscamos desarrollador para migrar app a Next.js', '2026-06-01', '2026-07-01', '🏢', 'Remoto', 'Remoto', 'Tiempo Completo', '$3000 USD/mes', 1);

INSERT INTO vacante_etiquetas (vacante_id, etiqueta) VALUES 
(1, 'React'), (1, 'Next.js');

INSERT INTO postulacion_vacante (fecha_postulacion, estado_postulacion, cv, mensaje, vacante_id, postulante_id) VALUES 
('2026-06-15', 'PENDIENTE', '', 'Me interesa mucho la vacante.', 1, 2);

-- 5. Proyectos freelance
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

-- 6. Bootcamps, calificaciones e inscripciones
INSERT INTO bootcamp (titulo, descripcion, fecha_inicio, fecha_fin, modalidad, tipo, cupos, logo, precio, rating, instructor_id, empresa_id) VALUES 
('Master en React', 'Domina React, Hooks, Context y Next.js desde cero.', '2026-08-01', '2026-12-01', 'VIRTUAL', 'PARTICULAR', 100, '⚛️', 150.00, 5.0, 1, NULL);

INSERT INTO bootcamp_etiquetas (bootcamp_id, etiqueta) VALUES 
(1, 'React'), (1, 'Frontend');

INSERT INTO modulo_bootcamp (titulo, descripcion, link_clase, horario_clase, bootcamp_id) VALUES 
('Introducción a React', 'Conceptos básicos, JSX y componentes.', 'https://zoom.us/clase1', 'Lunes 18:00', 1),
('Hooks y Estado', 'useState, useEffect y custom hooks.', 'https://zoom.us/clase2', 'Miércoles 18:00', 1);

INSERT INTO inscripcion_bootcamp (fecha_inscripcion, estado, bootcamp_id, employee_id) VALUES 
('2026-06-16', 'INSCRIPTO', 1, 1);

INSERT INTO calificacion_bootcamp (puntaje, comentario, fecha_comentario, bootcamp_id, employee_id) VALUES 
(5, 'Excelente bootcamp, muy bien explicado.', '2026-06-20', 1, 1);
