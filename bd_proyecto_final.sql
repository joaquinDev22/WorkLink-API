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
    estado ENUM('BUSCANDO_TALENTO', 'EN_PROGRESO', 'FINALIZADO', 'CANCELADO') DEFAULT 'BUSCANDO_TALENTO',
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
    estado ENUM('PENDIENTE', 'EN_PROGRESO', 'COMPLETADO') DEFAULT 'PENDIENTE',
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
    activa BOOLEAN NOT NULL DEFAULT TRUE,
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
    estado_postulacion ENUM('PENDIENTE','VISUALIZADO','ENTREVISTA_AGENDADA','RECHAZADO') DEFAULT 'PENDIENTE',
    cv LONGBLOB NOT NULL,
    mensaje VARCHAR(255),
    vacante_id INT NOT NULL,
    postulante_id INT NOT NULL,
    FOREIGN KEY (vacante_id) REFERENCES vacante(vacante_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (postulante_id) REFERENCES employee(employee_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT uq_postulacion_vacante UNIQUE (vacante_id, postulante_id)
) ENGINE=InnoDB;

CREATE TABLE entrevista (
    entrevista_id INT AUTO_INCREMENT PRIMARY KEY,
    postulacion_id INT NOT NULL,
    fecha DATE NOT NULL,
    hora TIME NOT NULL,
    link VARCHAR(255),
    FOREIGN KEY (postulacion_id) REFERENCES postulacion_vacante(postulacion_id) ON DELETE CASCADE ON UPDATE CASCADE
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

CREATE TABLE proyecto_perfil (
    proyecto_perfil_id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    descripcion VARCHAR(1000),
    link VARCHAR(250),
    imagen LONGBLOB,
    perfil_id INT NOT NULL,
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (perfil_id) REFERENCES perfil(perfil_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ===================================================================================
-- 2. TABLAS DE PAGOS
-- ===================================================================================

CREATE TABLE pagos (
    pago_id INT AUTO_INCREMENT PRIMARY KEY,
    moneda ENUM('USD','PESOS') DEFAULT 'PESOS',
    monto DECIMAL(10,2) NOT NULL,
    medio_de_pago ENUM('TARJETA', 'BILLETERA_VIRTUAL') NOT NULL,
    proyecto_id INT NOT NULL,
    hito_id INT NULL,                       -- hito que habilita este pago (D2)
    mp_payment_id VARCHAR(30) NULL UNIQUE,  -- id del pago en Mercado Pago: idempotencia del webhook (RNF09)
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,  -- WL-39: fecha de acreditación del pago
    FOREIGN KEY (proyecto_id) REFERENCES proyecto(proyecto_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (hito_id) REFERENCES hito_proyecto(hito_id) ON DELETE SET NULL ON UPDATE CASCADE
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

CREATE TABLE freelancer_medios_cobro (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    freelancer_id INT NOT NULL UNIQUE,
    tipo ENUM('TRANSFERENCIA', 'MERCADOPAGO') NOT NULL,
    titular VARCHAR(120) NOT NULL,
    documento_titular VARCHAR(20),
    cbu_cvu VARCHAR(22),
    alias VARCHAR(40),
    mercado_pago_email VARCHAR(150),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (freelancer_id) REFERENCES perfil(perfil_id) ON DELETE CASCADE ON UPDATE CASCADE
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
-- 11. DATOS INICIALES (SEED DATA COMPLETO)
-- ===================================================================================

INSERT INTO rol (nombre_rol) VALUES 
('EMPRESA'), ('CLIENTE'), ('INSTRUCTOR'), ('FREELANCER'), ('POSTULANTE'), ('RECRUITER');

-- 1. Perfiles iniciales 100% completos
INSERT INTO perfil (perfil_id, usuario, email, password, ubicacion, descripcion, is_active, rol_id) VALUES
(1, 'AnaRecruiter', 'ana.recruiter@ejemplo.com', '$2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu', 'Madrid, España', 'Senior Tech Recruiter con más de 8 años conectando el mejor talento IT con empresas de primer nivel tecnológico en Europa y Latinoamérica.', TRUE, 6),
(2, 'DevFreelancer', 'freelancer@ejemplo.com', '$2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu', 'Buenos Aires, Argentina', 'Full Stack Engineer especializado en arquitecturas reactivas, TypeScript, React, Node.js y microservicios escalables de alta concurrencia.', TRUE, 4),
(3, 'ClienteGlobal', 'cliente@global.com', '$2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu', 'Barcelona, España', 'Empresario e inversor enfocado en modernización digital, e-commerce transfronterizo y desarrollo de soluciones SaaS ágiles.', TRUE, 2),
(4, 'TechCorp', 'empresa@techcorp.com', '$2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu', 'Madrid, España', 'Compañía multinacional líder en soluciones corporativas, transformación digital de alto impacto y consultoría de arquitectura de software.', TRUE, 1),
(5, 'CandidatoIT', 'candidato@it.com', '$2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu', 'Córdoba, Argentina', 'Desarrollador Backend apasionado por Java, Spring Boot, Clean Architecture y sistemas distribuidos resilientes.', TRUE, 5),
(6, 'ProfeBootcamp', 'profe@bootcamp.com', '$2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu', 'Santiago, Chile', 'Líder técnica y formadora de talento en bootcamps intensivos con foco en ecosistemas JavaScript, Next.js y metodologías ágiles.', TRUE, 3),
(8, 'joaking102003', 'joaking102003@gmail.com', '$2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu', 'Tucumán, Argentina', 'Desarrollador Full Stack apasionado por Java, Spring Boot, Angular y arquitecturas escalables.', TRUE, 5),
(10, 'MercadoLibre', 'careers@mercadolibre.com', '$2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu', 'Buenos Aires, Argentina', 'El ecosistema de comercio electrónico y servicios financieros líder de América Latina.', TRUE, 1),
(11, 'Globant', 'hiring@globant.com', '$2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu', 'Buenos Aires, Argentina', 'Compañía nativa digital enfocada en reinventar los negocios mediante soluciones tecnológicas innovadoras.', TRUE, 1),
(12, 'Auth0', 'talent@auth0.com', '$2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu', 'Bellevue, Estados Unidos', 'Plataforma líder en gestión de identidades y autenticación segura para desarrolladores y empresas globales.', TRUE, 1),
(13, 'Despegar', 'careers@despegar.com', '$2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu', 'Buenos Aires, Argentina', 'La empresa de viajes y turismo digital más grande de Latinoamérica con tecnología de punta.', TRUE, 1),
(14, 'UalaFintech', 'talento@uala.com.ar', '$2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu', 'Buenos Aires, Argentina', 'Ecosistema financiero digital que revoluciona el acceso al dinero en América Latina.', TRUE, 1),
(15, 'SofiaRecruiterMeLi', 'sofia.recruiter@mercadolibre.com', '$2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu', 'Buenos Aires, Argentina', 'Lead Tech Recruiter en Mercado Libre para los equipos de Shipping y Mercado Pago.', TRUE, 6),
(16, 'LucasRecruiterGlobant', 'lucas.recruiter@globant.com', '$2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu', 'Córdoba, Argentina', 'Talent Acquisition Specialist en Globant buscando perfiles Cloud, Mobile y Frontend.', TRUE, 6),
(17, 'MartinaRecruiterAuth0', 'martina.recruiter@auth0.com', '$2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu', 'Remoto, Argentina', 'Global IT Sourcer en Auth0 reclutando ingenieros de software e infraestructura.', TRUE, 6),
(18, 'GonzaloRecruiterDespegar', 'gonzalo.recruiter@despegar.com', '$2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu', 'Buenos Aires, Argentina', 'Tech Recruiter en Despegar para áreas de Search, Booking y Machine Learning.', TRUE, 6),
(19, 'FlorRecruiterUala', 'florencia.recruiter@uala.com.ar', '$2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu', 'Buenos Aires, Argentina', 'Talent Partner en Ualá enfocado en desarrolladores Java, Kotlin y Mobile.', TRUE, 6),
(20, 'ValeriaStartup', 'valeria.rossi@fintechstartup.io', '$2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu', 'Montevideo, Uruguay', 'Fundadora de startup Fintech orientada a microcréditos para emprendedores.', TRUE, 2),
(21, 'SantiagoPixels', 'santiago@agenciapixels.com', '$2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu', 'Buenos Aires, Argentina', 'Director de agencia de marketing y diseño digital con clientes internacionales.', TRUE, 2),
(22, 'ElenaMediConnect', 'elena.vega@mediconnect.org', '$2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu', 'Santiago, Chile', 'Coordinadora de tecnología en plataforma de telemedicina y turnos médicos online.', TRUE, 2),
(23, 'LucasLogistica', 'lucas@logisticapro.net', '$2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu', 'Rosario, Argentina', 'Gerente de operaciones en empresa logística de última milla y geolocalización.', TRUE, 2),
(24, 'MarianaEdTech', 'mariana.lopez@aprendeonline.edu', '$2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu', 'Bogotá, Colombia', 'Directora de producto en plataforma educativa de cursos interactivos.', TRUE, 2),
(25, 'MartinAgroTech', 'martin@agrotechdigital.com', '$2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu', 'Córdoba, Argentina', 'Ingeniero agrónomo e impulsor de tecnología IoT y monitoreo de cultivos.', TRUE, 2),
(26, 'SofiaFullstack', 'sofia.martinez@dev.io', '$2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu', 'Buenos Aires, Argentina', 'Senior Fullstack Engineer con 6 años de experiencia en Angular, React, Node.js y Spring Boot.', TRUE, 4),
(27, 'MateoCloudOps', 'mateo.rodriguez@cloudops.io', '$2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu', 'Mendoza, Argentina', 'DevOps & Cloud Engineer especializado en AWS, Kubernetes, Terraform y pipelines CI/CD.', TRUE, 4),
(28, 'CamilaDesigner', 'camila.fernandez@design.io', '$2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu', 'Montevideo, Uruguay', 'Product Designer y Frontend UI Developer apasionada por Design Systems y accesibilidad.', TRUE, 5),
(29, 'LucasMobile', 'lucas.gomez@mobileapps.dev', '$2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu', 'Rosario, Argentina', 'Desarrollador Mobile especializado en Flutter, Android nativo (Kotlin) y React Native.', TRUE, 4),
(30, 'ValentinaData', 'valentina.perez@datainsights.ai', '$2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu', 'Buenos Aires, Argentina', 'Data Scientist e Ingeniera de Datos con foco en Python, Machine Learning y análisis predictivo.', TRUE, 5),
(31, 'NicolasQA', 'nicolas.diaz@qasolutions.io', '$2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu', 'La Plata, Argentina', 'QA Automation Engineer experto en Cypress, Playwright, Selenium y testing de APIs REST.', TRUE, 5),
(32, 'LucianaJava', 'luciana.alvarez@cloudbackend.io', '$2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu', 'Córdoba, Argentina', 'Backend Developer especializada en Java 21, Spring Boot 3, Hibernate y microservicios reactivos.', TRUE, 5);

-- 2. Entidades vinculadas (Empresas, Recruiters, Clientes, Instructores, Employees)
INSERT INTO empresa (empresa_id, nombre, descripcion, sitio_web, ubicacion, tamano, sector, perfil_id) VALUES 
(1, 'TechCorp', 'Soluciones de software corporativo y consultoría arquitectónica', 'https://techcorp.com', 'Madrid, España', '50-200 empleados', 'Tecnología', 4),
(2, 'Mercado Libre', 'Líder en e-commerce y fintech en América Latina', 'https://mercadolibre.com', 'Buenos Aires, Argentina', '10.000+ empleados', 'Comercio Electrónico y Fintech', 10),
(3, 'Globant', 'Transformación digital y servicios de ingeniería de software a escala mundial', 'https://globant.com', 'Buenos Aires, Argentina', '5.000-10.000 empleados', 'Servicios de TI y Consultoría', 11),
(4, 'Auth0 by Okta', 'Seguridad, SSO y autenticación moderna para la nube', 'https://auth0.com', 'Remoto Global', '1.000-5.000 empleados', 'Ciberseguridad y SaaS', 12),
(5, 'Despegar.com', 'Plataforma de viajes, vuelos y experiencias turísticas', 'https://despegar.com', 'Buenos Aires, Argentina', '1.000-5.000 empleados', 'Turismo y Tecnología', 13),
(6, 'Ualá', 'Inclusión financiera mediante app y tarjeta Mastercard internacional', 'https://uala.com.ar', 'Buenos Aires, Argentina', '1.000-5.000 empleados', 'Fintech', 14);

INSERT INTO recruiter (recruiter_id, nombre, apellido, email, fecha_nacimiento, area, empresa_id, perfil_id) VALUES 
(1, 'Ana', 'Recruiter', 'ana.recruiter@ejemplo.com', '1985-05-12', 'Recursos Humanos', 1, 1),
(2, 'Sofía', 'Benítez', 'sofia.recruiter@mercadolibre.com', '1990-03-14', 'Tech Recruiting', 2, 15),
(3, 'Lucas', 'Páez', 'lucas.recruiter@globant.com', '1988-11-20', 'Talent Acquisition', 3, 16),
(4, 'Martina', 'Soria', 'martina.recruiter@auth0.com', '1992-07-09', 'People & Culture', 4, 17),
(5, 'Gonzalo', 'Méndez', 'gonzalo.recruiter@despegar.com', '1987-01-30', 'IT Recruiting', 5, 18),
(6, 'Florencia', 'Carrizo', 'florencia.recruiter@uala.com.ar', '1994-09-18', 'Fintech Talent', 6, 19);

INSERT INTO cliente_particular (cliente_particular_id, nombre, apellido, email, fecha_nacimiento, telefono, pais, perfil_id) VALUES 
(1, 'Cliente', 'Global', 'cliente@global.com', '1980-08-20', '+34600123456', 'España', 3),
(2, 'Valeria', 'Rossi', 'valeria.rossi@fintechstartup.io', '1986-04-12', '+59899123456', 'Uruguay', 20),
(3, 'Santiago', 'Morales', 'santiago@agenciapixels.com', '1984-12-05', '+5491134567890', 'Argentina', 21),
(4, 'Elena', 'Vega', 'elena.vega@mediconnect.org', '1989-08-22', '+56987654321', 'Chile', 22),
(5, 'Lucas', 'Benítez', 'lucas@logisticapro.net', '1982-06-15', '+5493415678901', 'Argentina', 23),
(6, 'Mariana', 'López', 'mariana.lopez@aprendeonline.edu', '1991-03-29', '+573109876543', 'Colombia', 24),
(7, 'Martín', 'Fierro', 'martin@agrotechdigital.com', '1980-10-18', '+5493514567892', 'Argentina', 25);

INSERT INTO instructor (instructor_id, nombre, apellido, titulo, presentacion, tipo, perfil_id) VALUES 
(1, 'Profe', 'Bootcamp', 'Senior Frontend Developer', 'Apasionada por la enseñanza y mentora de software.', 'PARTICULAR', 6);

INSERT INTO employee (employee_id, nombre, apellido, email, fecha_nacimiento, freelance, enterprise, perfil_id) VALUES 
(1, 'Dev', 'Freelancer', 'freelancer@ejemplo.com', '1995-02-15', TRUE, FALSE, 2),
(2, 'Candidato', 'IT', 'candidato@it.com', '1998-07-22', FALSE, TRUE, 5),
(3, 'Sofía', 'Martínez', 'sofia.martinez@dev.io', '1994-05-18', TRUE, TRUE, 26),
(4, 'Joaquín', 'Nieva', 'joaking102003@gmail.com', '2003-10-20', TRUE, TRUE, 8),
(5, 'Mateo', 'Rodríguez', 'mateo.rodriguez@cloudops.io', '1991-09-23', TRUE, TRUE, 27),
(6, 'Camila', 'Fernández', 'camila.fernandez@design.io', '1996-01-11', TRUE, FALSE, 28),
(7, 'Lucas', 'Gómez', 'lucas.gomez@mobileapps.dev', '1993-11-04', TRUE, TRUE, 29),
(8, 'Valentina', 'Pérez', 'valentina.perez@datainsights.ai', '1995-07-30', FALSE, TRUE, 30),
(9, 'Nicolás', 'Díaz', 'nicolas.diaz@qasolutions.io', '1992-04-19', TRUE, TRUE, 31),
(10, 'Luciana', 'Álvarez', 'luciana.alvarez@cloudbackend.io', '1997-12-08', FALSE, TRUE, 32);

-- 3. Habilidades y experiencias
INSERT INTO habilidad (habilidad_id, nombre_habilidad) VALUES 
(1, 'React'), (2, 'Node.js'), (3, 'Figma'), (4, 'TypeScript'), (5, 'Python'),
(6, 'Java'), (7, 'Spring Boot'), (8, 'Angular'), (9, 'Tailwind CSS'), (10, 'Docker'),
(11, 'Kubernetes'), (12, 'AWS'), (13, 'MySQL'), (14, 'PostgreSQL'), (15, 'Next.js'),
(16, 'Flutter'), (17, 'Golang'), (18, 'GraphQL'), (19, 'C#'), (20, 'Kotlin'),
(21, 'Swift'), (22, 'Git'), (23, 'Redis'), (24, 'Microservicios'), (25, 'CI/CD');

INSERT INTO employee_habilidad (employee_id, habilidad_id, nivel) VALUES 
(1, 1, 5), (1, 4, 4), (1, 2, 4),
(2, 6, 5), (2, 7, 5), (2, 13, 4),
(3, 1, 5), (3, 4, 5), (3, 2, 4), (3, 15, 5), (3, 9, 5),
(4, 6, 5), (4, 7, 5), (4, 8, 5), (4, 4, 4), (4, 13, 4),
(5, 12, 5), (5, 11, 5), (5, 10, 5), (5, 25, 5), (5, 17, 4),
(6, 3, 5), (6, 9, 5), (6, 1, 4), (6, 4, 4),
(7, 16, 5), (7, 20, 5), (7, 21, 4), (7, 1, 3),
(8, 5, 5), (8, 14, 5), (8, 13, 4), (8, 10, 3),
(9, 4, 5), (9, 2, 4), (9, 22, 5), (9, 25, 4),
(10, 6, 5), (10, 7, 5), (10, 24, 5), (10, 14, 4), (10, 23, 4);

INSERT INTO experiencia (titulo, descripcion, fecha_inicio, fecha_fin, employee_id) VALUES 
('Frontend Developer Senior', 'Desarrollo de interfaces en React para E-commerce.', '2020-01-01', '2023-01-01', 1),
('Backend Engineer', 'Creación de APIs REST con Node.js y microservicios.', '2019-05-01', '2024-02-01', 2),
('Lead Frontend Engineer', 'Dirección de arquitectura UI con React, Next.js y Tailwind CSS.', '2022-06-01', NULL, 3),
('Fullstack Developer & Arquitecto de Software', 'Desarrollo de aplicaciones completas con Spring Boot, Angular y bases de datos relacionales.', '2022-01-01', NULL, 4),
('DevOps Engineer Senior', 'Automatización de despliegues multi-región en AWS EKS.', '2021-04-01', NULL, 5),
('Senior Product Designer', 'Diseño de sistemas de diseño y flujos de usuario complejos.', '2020-08-01', NULL, 6),
('Mobile Tech Lead', 'Desarrollo de apps Fintech multiplataforma con Flutter.', '2021-02-01', NULL, 7),
('Data Scientist', 'Modelos predictivos de churn y scoring crediticio en Python.', '2022-09-01', NULL, 8),
('QA Automation Lead', 'Frameworks de automatización E2E con Playwright y Cypress.', '2021-10-01', NULL, 9),
('Java Backend Specialist', 'Microservicios de alta concurrencia y mensajería reactiva.', '2023-01-01', NULL, 10);

INSERT INTO portfolio (github, linkedin, descripcion, employee_id) VALUES 
('https://github.com/devfreelancer', 'https://linkedin.com/in/devfreelancer', 'Portafolio de proyectos React y Web.', 1),
('https://github.com/sofiamartinez-dev', 'https://linkedin.com/in/sofiamartinez', 'Portafolio con aplicaciones React, Next.js y componentes Tailwind.', 3),
('https://github.com/joaking102003', 'https://linkedin.com/in/joaquin-nieva', 'Portafolio de proyectos backend, microservicios y desarrollo web full stack.', 4),
('https://github.com/mateo-cloudops', 'https://linkedin.com/in/mateorodriguez', 'Infraestructura como código en Terraform, configuraciones Helm y K8s.', 5),
('https://github.com/camila-uxdesign', 'https://linkedin.com/in/camilafernandez', 'Casos de estudio UX/UI y librerías de diseño de componentes.', 6),
('https://github.com/lucasgomez-mob', 'https://linkedin.com/in/lucasgomez', 'Aplicaciones móviles publicadas en App Store y Google Play Store.', 7),
('https://github.com/valentinaperez-ai', 'https://linkedin.com/in/valentinaperez', 'Notebooks de machine learning, pipelines de datos y visualizaciones.', 8),
('https://github.com/nicolasdiaz-qa', 'https://linkedin.com/in/nicolasdiaz', 'Suites automatizadas de testing de integración y rendimiento.', 9),
('https://github.com/lucianaalvarez-code', 'https://linkedin.com/in/lucianaalvarez', 'Repositorios con patrones de diseño, Spring Security y Kafka.', 10);

-- 4. Vacantes laborales (25 Vacantes)
INSERT INTO vacante (vacante_id, puesto, descripcion, fecha_publicacion, fecha_fin, logo, ubicacion, tipo_ubicacion, tipo_vacante, salario, empresa_id, activa) VALUES 
(1, 'Frontend React SSR', 'Buscamos desarrollador para migrar app a Next.js', '2026-06-01', '2026-07-01', '🏢', 'Remoto', 'Remoto', 'Tiempo Completo', '$3.000.000 / mes', 1, TRUE),
(2, 'Senior Java Spring Boot Engineer', 'Desarrollo de microservicios transaccionales con Spring Boot 3, Kafka y MySQL.', '2026-10-01', '2026-12-15', '🚀', 'Buenos Aires, Argentina', 'Remoto', 'Tiempo Completo', '$4.500.000 / mes', 1, TRUE),
(3, 'Fullstack Angular & Node.js Developer', 'Creación de portales de autogestión corporativos con Angular 19 y Node.', '2026-10-05', '2026-12-01', '⚡', 'Madrid, España', 'Híbrido', 'Tiempo Completo', '$4.000.000 / mes', 1, TRUE),
(4, 'Database Administrator DBA MySQL', 'Optimización de consultas, particionado de tablas y replicación MySQL 8.', '2026-09-15', '2026-11-30', '💾', 'Buenos Aires, Argentina', 'Remoto', 'Tiempo Completo', '$3.800.000 / mes', 1, TRUE),
(5, 'Lead Cloud DevOps AWS & Kubernetes', 'Liderar la infraestructura cloud y migración de contenedores en Mercado Pago.', '2026-10-10', '2026-12-20', '💛', 'Buenos Aires, Argentina', 'Híbrido', 'Tiempo Completo', '$5.500.000 / mes', 2, TRUE),
(6, 'Data Engineer Big Data & Spark', 'Construcción de data pipelines para el procesamiento de millones de compras diarias.', '2026-10-12', '2026-12-31', '📊', 'São Paulo, Brasil', 'Remoto', 'Tiempo Completo', '$4.800.000 / mes', 2, TRUE),
(7, 'Senior iOS Developer Swift', 'Mejora continua del checkout nativo en la app de Mercado Libre.', '2026-10-15', '2026-12-10', '📱', 'Buenos Aires, Argentina', 'Híbrido', 'Tiempo Completo', '$4.600.000 / mes', 2, TRUE),
(8, 'Tech Lead Microservicios Golang', 'Diseño de arquitectura distribuida para pagos internacionales de baja latencia.', '2026-10-18', '2027-01-15', '⚙️', 'Montevideo, Uruguay', 'Remoto', 'Tiempo Completo', '$6.000.000 / mes', 2, TRUE),
(9, 'UI/UX Senior Product Designer', 'Diseño de interfaces complejas para clientes Fortune 500.', '2026-10-01', '2026-12-05', '🎨', 'Remoto Global', 'Remoto', 'Tiempo Completo', '$4.000.000 / mes', 3, TRUE),
(10, 'QA Automation Lead Playwright', 'Definición de estándares de testing automatizado E2E y rendimiento.', '2026-10-08', '2026-11-28', '🧪', 'Córdoba, Argentina', 'Híbrido', 'Tiempo Completo', '$3.500.000 / mes', 3, TRUE),
(11, 'React Native Mobile Engineer', 'Desarrollo de soluciones omnicanal para banca y telecomunicaciones.', '2026-09-20', '2026-11-25', '📲', 'Santiago, Chile', 'Remoto', 'Tiempo Completo', '$4.200.000 / mes', 3, TRUE),
(12, 'Scrum Master & Agile Coach', 'Acompañar squads de ingeniería en prácticas Scrum, Kanban y delivery continuo.', '2026-10-05', '2026-12-15', '🎯', 'Buenos Aires, Argentina', 'Híbrido', 'Tiempo Completo', '$3.200.000 / mes', 3, TRUE),
(13, 'Identity & Security Engineer', 'Desarrollo de protocolos OAuth 2.1, OIDC y passkeys en la nube Auth0.', '2026-10-02', '2026-12-15', '🔒', 'Remoto Global', 'Remoto', 'Tiempo Completo', '$6.500.000 / mes', 4, TRUE),
(14, 'Backend Golang Distributed Systems', 'Desarrollo de APIs críticas de autenticación con disponibilidad 99.999%.', '2026-10-14', '2027-01-10', '🌐', 'Remoto Global', 'Remoto', 'Tiempo Completo', '$6.000.000 / mes', 4, TRUE),
(15, 'Principal Software Architect', 'Definición de roadmap tecnológico para servicios multi-tenant SaaS.', '2026-09-01', '2026-11-30', '🏛️', 'Bellevue, EE.UU.', 'Remoto', 'Tiempo Completo', '$8.000.000 / mes', 4, TRUE),
(16, 'Cyber Security Pentester', 'Auditoría de código, modelado de amenazas y ethical hacking continuo.', '2026-10-20', '2027-01-30', '🛡️', 'Remoto Global', 'Remoto', 'Tiempo Completo', '$5.000.000 / mes', 4, TRUE),
(17, 'Senior Python Backend Developer', 'Motor de búsqueda y cotización de vuelos en tiempo real.', '2026-10-05', '2026-12-20', '✈️', 'Buenos Aires, Argentina', 'Híbrido', 'Tiempo Completo', '$4.500.000 / mes', 5, TRUE),
(18, 'Machine Learning Scientist', 'Modelos predictivos de precios de pasajes dinámicos y recomendaciones.', '2026-10-10', '2026-12-18', '🤖', 'Buenos Aires, Argentina', 'Remoto', 'Tiempo Completo', '$5.200.000 / mes', 5, TRUE),
(19, 'Frontend React Next.js Specialist', 'Optimización de Core Web Vitals y renderizado híbrido para web móvil.', '2026-10-15', '2026-12-22', '🌐', 'Remoto, Argentina', 'Remoto', 'Tiempo Completo', '$4.000.000 / mes', 5, TRUE),
(20, 'Backend Kotlin / Java Developer', 'Core bancario digital, tarjetas prepagas y transferencias inmediatas.', '2026-10-03', '2026-12-10', '💳', 'Buenos Aires, Argentina', 'Híbrido', 'Tiempo Completo', '$4.200.000 / mes', 6, TRUE),
(21, 'Mobile Flutter FinTech Developer', 'Nuevas funcionalidades de inversiones y plazos fijos en la app Ualá.', '2026-10-12', '2026-12-15', '📈', 'Buenos Aires, Argentina', 'Remoto', 'Tiempo Completo', '$4.000.000 / mes', 6, TRUE),
(22, 'Site Reliability Engineer SRE', 'Monitoreo Prometheus/Grafana y resiliencia para microservicios financieros.', '2026-10-16', '2027-01-05', '🩺', 'Buenos Aires, Argentina', 'Remoto', 'Tiempo Completo', '$4.800.000 / mes', 6, TRUE),
(23, 'Junior Frontend Vue.js (Cerrada)', 'Puesto junior de maquetación ya cubierto exitosamente.', '2026-08-01', '2026-09-01', '🔒', 'Buenos Aires, Argentina', 'Presencial', 'Tiempo Completo', '$1.500.000 / mes', 5, FALSE),
(24, 'Junior Data Analyst SQL (Cerrada)', 'Procesamiento de reportes comerciales ya finalizado.', '2026-08-10', '2026-09-15', '🔒', 'Buenos Aires, Argentina', 'Híbrido', 'Medio Tiempo', '$1.200.000 / mes', 6, FALSE),
(25, 'Intern Soporte IT (Cerrada)', 'Pasantía técnica de soporte interno completada.', '2026-07-01', '2026-08-30', '🔒', 'Córdoba, Argentina', 'Presencial', 'Medio Tiempo', '$800.000 / mes', 3, FALSE);

INSERT INTO vacante_etiquetas (vacante_id, etiqueta) VALUES 
(1, 'React'), (1, 'Next.js'),
(2, 'Java'), (2, 'Spring Boot'), (2, 'Kafka'),
(3, 'Angular'), (3, 'TypeScript'), (3, 'Node.js'),
(4, 'MySQL'), (4, 'DBA'), (4, 'Performance'),
(5, 'AWS'), (5, 'Kubernetes'), (5, 'Docker'),
(6, 'Python'), (6, 'Big Data'), (6, 'Spark'),
(7, 'iOS'), (7, 'Swift'), (7, 'Mobile'),
(8, 'Golang'), (8, 'Microservicios'), (8, 'Architecture'),
(9, 'Figma'), (9, 'UI/UX'), (9, 'Design System'),
(10, 'QA'), (10, 'Playwright'), (10, 'Cypress'),
(11, 'React Native'), (11, 'Mobile'), (11, 'TypeScript'),
(12, 'Agile'), (12, 'Scrum'), (12, 'Kanban'),
(13, 'Security'), (13, 'OAuth2'), (13, 'OIDC'),
(14, 'Golang'), (14, 'APIs'), (14, 'High Availability'),
(15, 'Architecture'), (15, 'Cloud'), (15, 'SaaS'),
(16, 'Pentesting'), (16, 'Security'), (16, 'Ethical Hacking'),
(17, 'Python'), (17, 'FastAPI'), (17, 'Microservicios'),
(18, 'Machine Learning'), (18, 'Python'), (18, 'Data Science'),
(19, 'React'), (19, 'Next.js'), (19, 'SSR'),
(20, 'Kotlin'), (20, 'Java'), (20, 'Fintech'),
(21, 'Flutter'), (21, 'Dart'), (21, 'Mobile'),
(22, 'SRE'), (22, 'Kubernetes'), (22, 'Grafana');

-- 5. Postulaciones a vacantes y entrevistas (32 postulaciones, 13 entrevistas)
INSERT INTO postulacion_vacante (postulacion_id, fecha_postulacion, estado_postulacion, cv, mensaje, vacante_id, postulante_id) VALUES 
(1, '2026-06-15', 'RECHAZADO', '', 'Me interesa mucho la vacante.', 1, 2),
(2, '2026-10-15', 'ENTREVISTA_AGENDADA', '', 'Cuento con amplia experiencia en Spring Boot, JPA y arquitecturas limpias.', 2, 4),
(3, '2026-10-16', 'ENTREVISTA_AGENDADA', '', 'Me motiva mucho el desarrollo frontend en Angular y microservicios.', 3, 4),
(4, '2026-10-20', 'VISUALIZADO', '', 'Postulación para el rol de Backend Kotlin / Java.', 20, 4),
(5, '2026-10-22', 'PENDIENTE', '', 'Experiencia sólida en diseño y optimización de bases de datos relacionales.', 4, 4),
(6, '2026-10-10', 'ENTREVISTA_AGENDADA', '', 'He liderado equipos frontend migrando arquitecturas a Next.js y React.', 19, 3),
(7, '2026-10-12', 'VISUALIZADO', '', 'Interesada en la vacante de Fullstack Angular y Node.js.', 3, 3),
(8, '2026-10-18', 'PENDIENTE', '', 'Aplicación al rol de React Native Mobile Engineer.', 11, 3),
(9, '2026-10-08', 'ENTREVISTA_AGENDADA', '', 'Experiencia comprobable construyendo apps React de alto tráfico.', 19, 1),
(10, '2026-10-14', 'RECHAZADO', '', 'Postulando al rol de Lead DevOps.', 5, 1),
(11, '2026-10-19', 'VISUALIZADO', '', 'Especialista en desarrollo frontend y diseño en Figma.', 9, 1),
(12, '2026-10-12', 'ENTREVISTA_AGENDADA', '', 'Cuento con certificaciones AWS Solutions Architect y CKA de Kubernetes.', 5, 5),
(13, '2026-10-17', 'ENTREVISTA_AGENDADA', '', 'Interesado en la posición de Site Reliability Engineer en Ualá.', 22, 5),
(14, '2026-10-21', 'PENDIENTE', '', 'Postulación al rol de Principal Software Architect.', 15, 5),
(15, '2026-10-05', 'ENTREVISTA_AGENDADA', '', 'Especialista en Design Systems y diseño de flujos de checkout para e-commerce.', 9, 6),
(16, '2026-10-11', 'VISUALIZADO', '', 'Postulación para diseño UI/UX en Despegar.', 19, 6),
(17, '2026-10-25', 'PENDIENTE', '', 'Interés en participar en el equipo de frontend.', 3, 6),
(18, '2026-10-16', 'ENTREVISTA_AGENDADA', '', 'Más de 4 años desarrollando aplicaciones en Flutter y Kotlin.', 21, 7),
(19, '2026-10-18', 'ENTREVISTA_AGENDADA', '', 'Postulación para iOS nativo con Swift en Mercado Libre.', 7, 7),
(20, '2026-10-22', 'RECHAZADO', '', 'Aplicación para rol de Java Backend.', 2, 7),
(21, '2026-10-14', 'ENTREVISTA_AGENDADA', '', 'Experiencia en pipelines ETL de datos en streaming y modelos ML.', 6, 8),
(22, '2026-10-18', 'VISUALIZADO', '', 'Postulación para Machine Learning Scientist en Despegar.', 18, 8),
(23, '2026-10-24', 'PENDIENTE', '', 'Interés en análisis de datos comerciales.', 4, 8),
(24, '2026-10-10', 'ENTREVISTA_AGENDADA', '', 'Frameworks de automatización continua integrados en GitHub Actions.', 10, 9),
(25, '2026-10-15', 'VISUALIZADO', '', 'Postulando al rol de SRE en Ualá.', 22, 9),
(26, '2026-10-26', 'PENDIENTE', '', 'Aplicación para testing de APIs en Auth0.', 13, 9),
(27, '2026-10-08', 'ENTREVISTA_AGENDADA', '', 'Experiencia profunda en Java 21, Spring Boot y microservicios distribuidos.', 2, 10),
(28, '2026-10-12', 'VISUALIZADO', '', 'Postulando para Backend Kotlin en Ualá.', 20, 10),
(29, '2026-10-20', 'RECHAZADO', '', 'Aplicación para rol de DevOps AWS.', 5, 10),
(30, '2026-10-06', 'ENTREVISTA_AGENDADA', '', 'Desarrollador enfocado en buenas prácticas y Spring Boot.', 2, 2),
(31, '2026-10-15', 'PENDIENTE', '', 'Postulando al rol de DBA MySQL.', 4, 2),
(32, '2026-10-23', 'RECHAZADO', '', 'Postulación para Frontend Angular.', 3, 2);

INSERT INTO entrevista (entrevista_id, postulacion_id, fecha, hora, link) VALUES 
(1, 2, '2026-11-16', '10:00:00', 'https://meet.google.com/qwe-rtyu-iop'),
(2, 3, '2026-11-18', '15:30:00', 'https://meet.google.com/asd-fghj-klz'),
(3, 6, '2026-11-17', '11:00:00', 'https://meet.google.com/zxc-vbnm-qwe'),
(4, 9, '2026-11-19', '14:00:00', 'https://meet.google.com/poi-uytr-ewq'),
(5, 12, '2026-11-16', '16:00:00', 'https://meet.google.com/lkj-hgfd-sa1'),
(6, 13, '2026-11-20', '09:30:00', 'https://meet.google.com/mnb-vcxz-lk2'),
(7, 15, '2026-11-17', '14:30:00', 'https://meet.google.com/tyu-iopa-sdf3'),
(8, 18, '2026-11-21', '10:30:00', 'https://meet.google.com/ghj-klzx-cvb4'),
(9, 19, '2026-11-22', '16:30:00', 'https://meet.google.com/bnm-qwer-tyu5'),
(10, 21, '2026-11-23', '11:30:00', 'https://meet.google.com/iop-asdf-ghj6'),
(11, 24, '2026-11-24', '15:00:00', 'https://meet.google.com/klz-xcvb-nmq7'),
(12, 27, '2026-11-25', '10:00:00', 'https://meet.google.com/wer-tyui-opa8'),
(13, 30, '2026-11-25', '14:00:00', 'https://meet.google.com/sdf-ghjk-lzx9');

-- 6. Proyectos freelance (23 Proyectos, estados: BUSCANDO_TALENTO, EN_PROGRESO, FINALIZADO)
INSERT INTO proyecto (proyecto_id, nombre_proyecto, area_proyecto, descripcion, fecha_creacion, presupuesto_max, presupuesto_min, duracion, tipo_pago, estado, freelancer_asignado_id, cliente_id) VALUES 
(1, 'Portal Bancario B2B', 'Backend', 'Desarrollo de API REST en Spring Boot 3 con seguridad OAuth2 y liquidaciones automáticas.', '2026-10-01', 3500000.00, 2500000.00, '2 meses', 'Precio Fijo', 'EN_PROGRESO', 4, 2),
(2, 'Dashboard Analytics', 'Frontend', 'Dashboard interactivo en Angular con gráficos en tiempo real y exportación PDF/Excel.', '2026-10-15', 2200000.00, 1600000.00, '1 mes', 'Precio Fijo', 'EN_PROGRESO', 4, 3),
(3, 'Microservicio Facturacion', 'Backend', 'Módulo de generación de comprobantes electrónicos con firma digital AFIP.', '2026-08-10', 1800000.00, 1200000.00, '3 semanas', 'Precio Fijo', 'FINALIZADO', 4, 5),
(4, 'App Delivery React Native', 'Mobile', 'Desarrollo de app móvil de tracking en vivo de pedidos para cadena gastronómica.', '2026-10-05', 4000000.00, 3000000.00, '2 meses', 'Precio Fijo', 'EN_PROGRESO', 7, 5),
(5, 'Infraestructura Terraform AWS', 'DevOps', 'Automatización de infraestructura como código con VPC, ECS Fargate y RDS.', '2026-10-12', 3000000.00, 2200000.00, '1 mes', 'Por Hora', 'EN_PROGRESO', 5, 2),
(6, 'Rediseño E-commerce Moda', 'Diseño', 'Diseño UX/UI completo en Figma con prototipo navegable y kit de componentes.', '2026-10-18', 2500000.00, 1800000.00, '3 semanas', 'Precio Fijo', 'EN_PROGRESO', 6, 3),
(7, 'Plataforma Telemedicina', 'FullStack', 'Videoconsultas médicas en WebRTC, agenda de turnos y recetas digitales.', '2026-09-25', 5000000.00, 3800000.00, '3 meses', 'Precio Fijo', 'EN_PROGRESO', 3, 4),
(8, 'Suite Testing Automatizado', 'QA', 'Implementación de pruebas automatizadas E2E en Playwright para pasarela de pagos.', '2026-10-08', 2000000.00, 1400000.00, '1 mes', 'Precio Fijo', 'EN_PROGRESO', 9, 2),
(9, 'Landing Page Corporativa', 'Frontend', 'Landing page de alta conversión optimizada para SEO con Next.js y Tailwind.', '2026-07-01', 1200000.00, 900000.00, '2 semanas', 'Precio Fijo', 'FINALIZADO', 1, 3),
(10, 'App Monitoreo de Cultivos', 'Mobile', 'App offline-first para registro de fertilización y humedad con geolocalización.', '2026-07-15', 3600000.00, 2800000.00, '2 meses', 'Precio Fijo', 'FINALIZADO', 7, 7),
(11, 'Pipeline ETL Datos Ventas', 'Data', 'Extracción y transformación de datos de Shopify a almacén PostgreSQL en Python.', '2026-08-01', 2500000.00, 1800000.00, '3 semanas', 'Precio Fijo', 'FINALIZADO', 8, 3),
(12, 'Auditoría Seguridad Web', 'DevOps', 'Penetration testing y hardening de servidores Linux y bases de datos.', '2026-08-20', 2000000.00, 1500000.00, '2 semanas', 'Precio Fijo', 'FINALIZADO', 5, 6),
(13, 'Plataforma Cursos Online', 'FullStack', 'LMS interactivo con reproducción de videos, exámenes y emisión de certificados.', '2026-06-10', 4500000.00, 3500000.00, '3 meses', 'Precio Fijo', 'FINALIZADO', 3, 6),
(14, 'Integración Pasarela Pagos', 'Backend', 'Integración de webhooks de Mercado Pago y Stripe con reintentos exponenciales.', '2026-11-01', 1800000.00, 1300000.00, '3 semanas', 'Precio Fijo', 'BUSCANDO_TALENTO', NULL, 2),
(15, 'App Control de Stock IoT', 'Mobile', 'Lectura de códigos QR y chips RFID desde teléfonos Android en almacén.', '2026-11-02', 3200000.00, 2400000.00, '6 semanas', 'Precio Fijo', 'BUSCANDO_TALENTO', NULL, 5),
(16, 'Migración Cloud Kubernetes', 'DevOps', 'Migración de arquitectura monolítica a clúster Kubernetes en Google Cloud.', '2026-11-03', 4500000.00, 3200000.00, '2 meses', 'Por Hora', 'BUSCANDO_TALENTO', NULL, 6),
(17, 'Sistema Turnos Consultorios', 'FullStack', 'Sistema web para reserva de citas médicas con recordatorios automáticos por WhatsApp.', '2026-11-04', 2800000.00, 2000000.00, '1 mes', 'Precio Fijo', 'BUSCANDO_TALENTO', NULL, 4),
(18, 'Design System Componentes', 'Diseño', 'Creación de biblioteca de componentes reutilizables en Figma y Storybook.', '2026-11-05', 2000000.00, 1500000.00, '3 semanas', 'Precio Fijo', 'BUSCANDO_TALENTO', NULL, 3),
(19, 'Modelo Scoring Predictivo', 'Data', 'Algoritmo de clasificación en Scikit-Learn para predecir morosidad crediticia.', '2026-11-06', 3000000.00, 2200000.00, '1 mes', 'Precio Fijo', 'BUSCANDO_TALENTO', NULL, 2),
(20, 'Automatización Pruebas Web', 'QA', 'Creación de pipeline de pruebas funcionales automatizadas con Cypress.', '2026-11-07', 1500000.00, 1000000.00, '3 semanas', 'Precio Fijo', 'BUSCANDO_TALENTO', NULL, 6),
(21, 'Plataforma Subastas Ganado', 'FullStack', 'Página web para remates ganaderos en vivo con streaming de baja latencia.', '2026-11-08', 5500000.00, 4000000.00, '3 meses', 'Precio Fijo', 'BUSCANDO_TALENTO', NULL, 7),
(22, 'Optimización Motor MySQL', 'Backend', 'Tuning de base de datos MySQL con más de 20 millones de registros transaccionales.', '2026-11-09', 2400000.00, 1800000.00, '2 semanas', 'Precio Fijo', 'BUSCANDO_TALENTO', NULL, 5),
(23, 'App Billetera Virtual Crypto', 'Mobile', 'Frontend móvil para consulta de saldos y transacciones en stablecoins.', '2026-11-10', 4200000.00, 3000000.00, '2 meses', 'Precio Fijo', 'BUSCANDO_TALENTO', NULL, 2);

INSERT INTO proyecto_habilidades (proyecto_id, habilidad) VALUES 
(1, 'Java'), (1, 'Spring Boot'), (1, 'MySQL'),
(2, 'Angular'), (2, 'TypeScript'), (2, 'Tailwind CSS'),
(3, 'Java'), (3, 'Spring Boot'), (3, 'APIs'),
(4, 'React Native'), (4, 'TypeScript'), (4, 'Mobile'),
(5, 'AWS'), (5, 'Docker'), (5, 'Kubernetes'),
(6, 'Figma'), (6, 'UI/UX'), (6, 'Tailwind CSS'),
(7, 'React'), (7, 'Node.js'), (7, 'WebRTC'),
(8, 'QA'), (8, 'Playwright'), (8, 'CI/CD'),
(9, 'Next.js'), (9, 'React'), (9, 'Tailwind CSS'),
(10, 'Flutter'), (10, 'Dart'), (10, 'Mobile'),
(11, 'Python'), (11, 'PostgreSQL'), (11, 'ETL'),
(12, 'AWS'), (12, 'Linux'), (12, 'Security'),
(13, 'React'), (13, 'Node.js'), (13, 'PostgreSQL'),
(14, 'Node.js'), (14, 'APIs'), (14, 'Backend'),
(15, 'Flutter'), (15, 'Mobile'), (15, 'IoT'),
(16, 'Kubernetes'), (16, 'Docker'), (16, 'Cloud'),
(17, 'Angular'), (17, 'Spring Boot'), (17, 'MySQL'),
(18, 'Figma'), (18, 'Design System'), (18, 'UI/UX'),
(19, 'Python'), (19, 'Machine Learning'), (19, 'Data'),
(20, 'QA'), (20, 'Cypress'), (20, 'Testing'),
(21, 'FullStack'), (21, 'WebSockets'), (21, 'Streaming'),
(22, 'MySQL'), (22, 'Database'), (22, 'Backend'),
(23, 'Flutter'), (23, 'Crypto'), (23, 'Mobile');

INSERT INTO hito_proyecto (hito_id, titulo, descripcion, monto, estado, fecha_limite, proyecto_id) VALUES 
(1, 'Diseño de Modelo Relacional y Auth', 'Estructura de base de datos y autenticación JWT.', 1066666.66, 'COMPLETADO', '2026-10-25', 1),
(2, 'Endpoints de Liquidación y Webhooks', 'Lógica transaccional de pagos B2B.', 1066666.66, 'PENDIENTE', '2026-11-20', 1),
(3, 'Pruebas de Carga y Entrega Final', 'Auditoría de rendimiento y documentación Swagger.', 1066666.68, 'PENDIENTE', '2026-12-05', 1),
(4, 'Arquitectura de Componentes y Estado', 'Configuración de Angular Signals y routing.', 1000000.00, 'COMPLETADO', '2026-10-30', 2),
(5, 'Integración Gráficos y Exportación', 'Visualización con Chart.js y exportación a PDF.', 1000000.00, 'PENDIENTE', '2026-11-25', 2),
(6, 'Conexión Web Services AFIP', 'Comunicación con certificados y claves fiscales.', 900000.00, 'COMPLETADO', '2026-08-25', 3),
(7, 'Generación de PDFs y Envíos Mail', 'Generación de facturas A y B con código QR.', 900000.00, 'COMPLETADO', '2026-09-02', 3),
(8, 'Maquetación responsive', 'Diseño de todas las secciones en Next.js.', 600000.00, 'COMPLETADO', '2026-07-08', 9),
(9, 'Optimización SEO y Deploy Vercel', 'Core Web Vitals verdes y analytics.', 600000.00, 'COMPLETADO', '2026-07-15', 9),
(10, 'Módulo GPS y Base SQLite Local', 'Sincronización bidireccional offline.', 1800000.00, 'COMPLETADO', '2026-08-10', 10),
(11, 'Entrega en Play Store', 'Publicación en canal privado de la empresa.', 1800000.00, 'COMPLETADO', '2026-09-15', 10),
(12, 'Conectores API Shopify a Postgres', 'Pipeline de carga incremental en Python.', 1250000.00, 'COMPLETADO', '2026-08-12', 11),
(13, 'Validación de Consistencia y Dashboards', 'Reporte automático de discrepancias.', 1250000.00, 'COMPLETADO', '2026-08-22', 11),
(14, 'Reporte de Vulnerabilidades Encontradas', 'Informe técnico de puntos críticos.', 1000000.00, 'COMPLETADO', '2026-08-28', 12),
(15, 'Hardening y Re-testing', 'Verificación de correcciones de seguridad.', 1000000.00, 'COMPLETADO', '2026-09-05', 12),
(16, 'Arquitectura de Vídeo y Streaming', 'Integración CDN y reproductor seguro.', 2250000.00, 'COMPLETADO', '2026-07-20', 13),
(17, 'Sistema de Exámenes y Certificados', 'Generación de PDFs dinámicos con firma hash.', 2250000.00, 'COMPLETADO', '2026-09-10', 13);

INSERT INTO calificacion_proyecto (calificacion_id, puntaje, titulo_resena, comentario, fecha_comentario, proyecto_id) VALUES 
(1, 5, 'Excelente dominio técnico de Spring Boot', 'Joaquín entregó la integración fiscal sin fisuras y en tiempo récord. Código impecable.', '2026-09-05', 3),
(2, 5, 'Landing veloz y diseño moderno', 'Gran trabajo con Next.js y Tailwind. Mejoró notablemente la conversión de la agencia.', '2026-07-16', 9),
(3, 5, 'App robusta para el campo', 'Funciona a la perfección sin conexión a internet en medio de los lotes agrícolas.', '2026-09-18', 10),
(4, 5, 'Pipeline veloz y automatizado', 'Valentina resolvió los cuellos de botella de datos y ahora el reporte es diario.', '2026-08-25', 11),
(5, 5, 'Auditoría rigurosa y profesional', 'Mateo encontró brechas que ningún scanner detectaba y nos ayudó a mitigarlas.', '2026-09-08', 12),
(6, 5, 'LMS de clase mundial', 'Sofía desarrolló una plataforma muy fluida que ya usan más de 2000 estudiantes.', '2026-09-12', 13);

INSERT INTO calificacion_usuario (calificacion_usuario_id, calificacion_id, calificacion_bootcamp_id, perfil_id) VALUES 
(1, 1, NULL, 8),
(2, 2, NULL, 2),
(3, 3, NULL, 29),
(4, 4, NULL, 30),
(5, 5, NULL, 27),
(6, 6, NULL, 26);

INSERT INTO postulacion_proyecto (post_proy_id, descripcion, valor_propuesto, fecha_postulacion, estado_postulacion, employee_id, proyecto_id) VALUES 
(1, 'Cuento con amplia experiencia en APIs financieras de Spring Boot. Me encantaría desarrollar este portal.', 3200000.00, '2026-10-02', 'ACEPTADA', 4, 1),
(2, 'Especialista en Angular 19 y arquitectura reactiva. Puedo completar el dashboard en 4 semanas.', 2000000.00, '2026-10-16', 'ACEPTADA', 4, 2),
(3, 'Tengo módulos fiscales AFIP ya desarrollados listos para adaptar en Java.', 1800000.00, '2026-08-11', 'ACEPTADA', 4, 3),
(4, 'Desarrollador Flutter con experiencia en geolocalización en tiempo real.', 3800000.00, '2026-10-06', 'ACEPTADA', 7, 4),
(5, 'Arquitecto cloud certificado AWS. Infraestructura lista en Terraform en 3 semanas.', 2800000.00, '2026-10-13', 'ACEPTADA', 5, 5),
(6, 'Diseño interfaces centradas en usuario con sistemas de diseño atómicos.', 2300000.00, '2026-10-19', 'ACEPTADA', 6, 6),
(7, 'Desarrollo fullstack con WebRTC y APIs seguras en Node y React.', 4800000.00, '2026-09-26', 'ACEPTADA', 3, 7),
(8, 'Ingeniero QA con amplia experiencia en automatización de checkout y pasarelas.', 1800000.00, '2026-10-09', 'ACEPTADA', 9, 8),
(9, 'Puedo integrar Mercado Pago y Stripe con tolerancia a fallos y reintentos.', 1600000.00, '2026-11-02', 'PENDIENTE', 4, 14),
(10, 'Experiencia integrando lectores de código QR y RFID en Android nativo.', 3000000.00, '2026-11-03', 'PENDIENTE', 7, 15),
(11, 'Especialista en migraciones Kubernetes sin downtime en GCP.', 4000000.00, '2026-11-04', 'PENDIENTE', 5, 16),
(12, 'Desarrollé sistemas de agendamiento médico similares en Angular y Spring.', 2600000.00, '2026-11-05', 'PENDIENTE', 4, 17),
(13, 'Puedo crear el Design System completo en Figma y documentarlo en Storybook.', 1800000.00, '2026-11-06', 'PENDIENTE', 6, 18),
(14, 'Data scientist con experiencia en modelos de riesgo y scoring crediticio.', 2800000.00, '2026-11-07', 'PENDIENTE', 8, 19),
(15, 'Automatización de tests de extremo a extremo con Cypress y reportes Allure.', 1300000.00, '2026-11-08', 'PENDIENTE', 9, 20);

-- 7. Bootcamps, calificaciones e inscripciones
INSERT INTO bootcamp (bootcamp_id, titulo, descripcion, fecha_inicio, fecha_fin, modalidad, tipo, cupos, logo, precio, rating, instructor_id, empresa_id) VALUES 
(1, 'Master en React', 'Domina React, Hooks, Context y Next.js desde cero.', '2026-08-01', '2026-12-01', 'VIRTUAL', 'PARTICULAR', 100, '⚛️', 150.00, 5.0, 1, NULL);

INSERT INTO bootcamp_etiquetas (bootcamp_id, etiqueta) VALUES 
(1, 'React'), (1, 'Frontend');

INSERT INTO modulo_bootcamp (modulo_id, titulo, descripcion, link_clase, horario_clase, bootcamp_id) VALUES 
(1, 'Introducción a React', 'Conceptos básicos, JSX y componentes.', 'https://zoom.us/clase1', 'Lunes 18:00', 1),
(2, 'Hooks y Estado', 'useState, useEffect y custom hooks.', 'https://zoom.us/clase2', 'Miércoles 18:00', 1);

INSERT INTO inscripcion_bootcamp (fecha_inscripcion, estado, bootcamp_id, employee_id) VALUES 
('2026-06-16', 'INSCRIPTO', 1, 1);

INSERT INTO calificacion_bootcamp (puntaje, comentario, fecha_comentario, bootcamp_id, employee_id) VALUES 
(5, 'Excelente bootcamp, muy bien explicado.', '2026-06-20', 1, 1);

