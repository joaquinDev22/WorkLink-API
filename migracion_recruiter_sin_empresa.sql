-- Migración para una base existente (ddl-auto: none). Correr una sola vez.
-- Un recruiter puede completar su perfil antes de que una empresa lo sume:
-- su ficha existe con empresa_id en NULL hasta que lo vinculan.
ALTER TABLE recruiter MODIFY empresa_id INT NULL;
