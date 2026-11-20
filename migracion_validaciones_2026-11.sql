-- Migración para una base existente (ddl-auto: none).
-- Ejecutar una sola vez antes de desplegar esta versión del backend.

-- 1) La empresa vincula al recruiter solo por email: fecha de nacimiento y área dejan de ser obligatorias.
ALTER TABLE recruiter
    MODIFY fecha_nacimiento DATE NULL,
    MODIFY area VARCHAR(50) NULL;

-- 2) Aviso de contratación y baja del recruiter.
ALTER TABLE recruiter
    ADD COLUMN fecha_vinculacion DATETIME NULL,
    ADD COLUMN vinculacion_vista BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN baja_solicitada_en DATETIME NULL;

-- Los recruiters que ya estaban trabajando no necesitan el aviso de "te contrataron".
UPDATE recruiter SET vinculacion_vista = TRUE, fecha_vinculacion = NOW() WHERE fecha_vinculacion IS NULL;
