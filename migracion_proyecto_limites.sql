-- Correr UNA vez en producción antes de desplegar el back.
-- Alinea los largos de proyecto con el formulario de "Publicar Proyecto":
-- título hasta 120, área hasta 50 ("Desarrollo Web" no entraba en 10) y descripción hasta 3000.
use bd_proyecto_final;
ALTER TABLE proyecto
    MODIFY nombre_proyecto VARCHAR(120) NOT NULL,
    MODIFY area_proyecto VARCHAR(50) NOT NULL,
    MODIFY descripcion VARCHAR(3000) NOT NULL;
