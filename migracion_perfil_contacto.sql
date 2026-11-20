-- Migración para una base existente (ddl-auto: none).
-- Ejecutar una sola vez antes de desplegar esta versión del backend.

-- Teléfono y país del perfil: el formulario los pedía para todos los roles,
-- pero solo el cliente tenía dónde guardarlos.
ALTER TABLE perfil
    ADD COLUMN telefono VARCHAR(30) NULL,
    ADD COLUMN pais VARCHAR(60) NULL;

-- Los clientes ya tenían teléfono y país en cliente_particular: se copian al perfil
UPDATE perfil p JOIN cliente_particular c ON c.perfil_id = p.perfil_id
SET p.telefono = NULLIF(c.telefono, ''), p.pais = NULLIF(c.pais, '');
