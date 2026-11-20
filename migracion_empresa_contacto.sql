-- Migración para una base existente (ddl-auto: none).
-- Ejecutar una sola vez antes de desplegar esta versión del backend.

-- Teléfono y dirección de la empresa (el rubro ya existía como "sector").
-- La descripción pasa de 255 a 500 caracteres, igual que la entidad.
-- Sitio web y ubicación pasan a ser opcionales (el formulario los permite vacíos y la base los rechazaba).
ALTER TABLE empresa
    ADD COLUMN telefono VARCHAR(30) NULL,
    ADD COLUMN direccion VARCHAR(150) NULL,
    MODIFY descripcion VARCHAR(500) NOT NULL,
    MODIFY sitio_web VARCHAR(150) NULL,
    MODIFY ubicacion VARCHAR(150) NULL;
