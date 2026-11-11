-- ===================================================================================
-- SEED DATA ENRIQUECIDO PARA WORKLINK / PROYECTO FINAL PROGRA 3
-- Cumple con:
-- 1. Perfiles 100% completos (descripción, ubicación, experiencia, portfolio, habilidades)
-- 2. 22+ Vacantes laborales con diversos estados y tags
-- 3. Postulaciones a vacantes en estados: PENDIENTE, VISUALIZADO, ENTREVISTA_AGENDADA, RECHAZADO
-- 4. Entrevistas agendadas con fecha, hora y Google Meet link para las postulaciones seleccionadas
-- 5. 22+ Proyectos freelance en estados: BUSCANDO_TALENTO, EN_PROGRESO, FINALIZADO
-- 6. Hitos, habilidades de proyectos, propuestas y calificaciones de proyectos
-- ===================================================================================

START TRANSACTION;

-- -----------------------------------------------------------------------------------
-- 1. ACTUALIZAR PERFILES EXISTENTES PARA QUE ESTÉN 100% COMPLETOS
-- -----------------------------------------------------------------------------------
UPDATE perfil SET 
    ubicacion = 'Madrid, España',
    descripcion = 'Senior Tech Recruiter con más de 8 años conectando el mejor talento IT con empresas de primer nivel tecnológico en Europa y Latinoamérica.'
WHERE perfil_id = 1;

UPDATE perfil SET 
    ubicacion = 'Buenos Aires, Argentina',
    descripcion = 'Full Stack Engineer especializado en arquitecturas reactivas, TypeScript, React, Node.js y microservicios escalables de alta concurrencia.'
WHERE perfil_id = 2;

UPDATE perfil SET 
    ubicacion = 'Barcelona, España',
    descripcion = 'Empresario e inversor enfocado en modernización digital, e-commerce transfronterizo y desarrollo de soluciones SaaS ágiles.'
WHERE perfil_id = 3;

UPDATE perfil SET 
    ubicacion = 'Madrid, España',
    descripcion = 'Compañía multinacional líder en soluciones corporativas, transformación digital de alto impacto y consultoría de arquitectura de software.'
WHERE perfil_id = 4;

UPDATE perfil SET 
    ubicacion = 'Córdoba, Argentina',
    descripcion = 'Desarrollador Backend apasionado por Java, Spring Boot, Clean Architecture y sistemas distribuidos resilientes.'
WHERE perfil_id = 5;

UPDATE perfil SET 
    ubicacion = 'Santiago, Chile',
    descripcion = 'Líder técnica y formadora de talento en bootcamps intensivos con foco en ecosistemas JavaScript, Next.js y metodologías ágiles.'
WHERE perfil_id = 6;

-- Actualizar el perfil del usuario actual (perfil 8 / joaking102003) si existe
UPDATE perfil SET 
    ubicacion = 'Buenos Aires, Argentina',
    descripcion = 'Software Engineer Full Stack con sólida experiencia en Java Spring Boot, microservicios, seguridad OAuth2/JWT y desarrollo frontend con Angular.'
WHERE perfil_id = 8;

-- Asegurar que el employee correspondiente al usuario (employee 4 o perfil 8) tenga flags activas
UPDATE employee SET 
    freelance = TRUE,
    enterprise = TRUE
WHERE perfil_id = 8;

-- -----------------------------------------------------------------------------------
-- 2. HABILIDADES EXTENDIDAS
-- -----------------------------------------------------------------------------------
INSERT IGNORE INTO habilidad (nombre_habilidad) VALUES 
('Java'),
('Spring Boot'),
('Angular'),
('Tailwind CSS'),
('Docker'),
('Kubernetes'),
('AWS'),
('MySQL'),
('PostgreSQL'),
('Next.js'),
('Flutter'),
('Golang'),
('GraphQL'),
('C#'),
('Kotlin'),
('Swift'),
('Git'),
('Redis'),
('Microservicios'),
('CI/CD');

-- -----------------------------------------------------------------------------------
-- 3. NUEVOS PERFILES (EMPRESAS, RECRUITERS, CLIENTES Y EMPLOYEES/FREELANCERS)
-- Contraseña estándar bcrypt ('password123'): $2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu
-- -----------------------------------------------------------------------------------

-- Empresas nuevas (perfil_id 10 al 14)
INSERT IGNORE INTO perfil (perfil_id, usuario, email, password, ubicacion, descripcion, is_active, rol_id) VALUES
(10, 'MercadoLibre', 'careers@mercadolibre.com', '$2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu', 'Buenos Aires, Argentina', 'El ecosistema de comercio electrónico y servicios financieros líder de América Latina.', TRUE, 1),
(11, 'Globant', 'hiring@globant.com', '$2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu', 'Buenos Aires, Argentina', 'Compañía nativa digital enfocada en reinventar los negocios mediante soluciones tecnológicas innovadoras.', TRUE, 1),
(12, 'Auth0', 'talent@auth0.com', '$2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu', 'Bellevue, Estados Unidos', 'Plataforma líder en gestión de identidades y autenticación segura para desarrolladores y empresas globales.', TRUE, 1),
(13, 'Despegar', 'careers@despegar.com', '$2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu', 'Buenos Aires, Argentina', 'La empresa de viajes y turismo digital más grande de Latinoamérica con tecnología de punta.', TRUE, 1),
(14, 'UalaFintech', 'talento@uala.com.ar', '$2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu', 'Buenos Aires, Argentina', 'Ecosistema financiero digital que revoluciona el acceso al dinero en América Latina.', TRUE, 1);

INSERT IGNORE INTO empresa (empresa_id, nombre, descripcion, sitio_web, ubicacion, tamano, sector, perfil_id) VALUES
(2, 'Mercado Libre', 'Líder en e-commerce y fintech en América Latina', 'https://mercadolibre.com', 'Buenos Aires, Argentina', '10.000+ empleados', 'Comercio Electrónico y Fintech', 10),
(3, 'Globant', 'Transformación digital y servicios de ingeniería de software a escala mundial', 'https://globant.com', 'Buenos Aires, Argentina', '5.000-10.000 empleados', 'Servicios de TI y Consultoría', 11),
(4, 'Auth0 by Okta', 'Seguridad, SSO y autenticación moderna para la nube', 'https://auth0.com', 'Remoto Global', '1.000-5.000 empleados', 'Ciberseguridad y SaaS', 12),
(5, 'Despegar.com', 'Plataforma de viajes, vuelos y experiencias turísticas', 'https://despegar.com', 'Buenos Aires, Argentina', '1.000-5.000 empleados', 'Turismo y Tecnología', 13),
(6, 'Ualá', 'Inclusión financiera mediante app y tarjeta Mastercard internacional', 'https://uala.com.ar', 'Buenos Aires, Argentina', '1.000-5.000 empleados', 'Fintech', 14);

-- Recruiters asociados a las empresas (perfil_id 15 al 19)
INSERT IGNORE INTO perfil (perfil_id, usuario, email, password, ubicacion, descripcion, is_active, rol_id) VALUES
(15, 'SofiaRecruiterMeLi', 'sofia.recruiter@mercadolibre.com', '$2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu', 'Buenos Aires, Argentina', 'Lead Tech Recruiter en Mercado Libre para los equipos de Shipping y Mercado Pago.', TRUE, 6),
(16, 'LucasRecruiterGlobant', 'lucas.recruiter@globant.com', '$2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu', 'Córdoba, Argentina', 'Talent Acquisition Specialist en Globant buscando perfiles Cloud, Mobile y Frontend.', TRUE, 6),
(17, 'MartinaRecruiterAuth0', 'martina.recruiter@auth0.com', '$2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu', 'Remoto, Argentina', 'Global IT Sourcer en Auth0 reclutando ingenieros de software e infraestructura.', TRUE, 6),
(18, 'GonzaloRecruiterDespegar', 'gonzalo.recruiter@despegar.com', '$2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu', 'Buenos Aires, Argentina', 'Tech Recruiter en Despegar para áreas de Search, Booking y Machine Learning.', TRUE, 6),
(19, 'FlorRecruiterUala', 'florencia.recruiter@uala.com.ar', '$2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu', 'Buenos Aires, Argentina', 'Talent Partner en Ualá enfocado en desarrolladores Java, Kotlin y Mobile.', TRUE, 6);

INSERT IGNORE INTO recruiter (recruiter_id, nombre, apellido, email, fecha_nacimiento, area, empresa_id, perfil_id) VALUES
(2, 'Sofía', 'Benítez', 'sofia.recruiter@mercadolibre.com', '1990-03-14', 'Tech Recruiting', 2, 15),
(3, 'Lucas', 'Páez', 'lucas.recruiter@globant.com', '1988-11-20', 'Talent Acquisition', 3, 16),
(4, 'Martina', 'Soria', 'martina.recruiter@auth0.com', '1992-07-09', 'People & Culture', 4, 17),
(5, 'Gonzalo', 'Méndez', 'gonzalo.recruiter@despegar.com', '1987-01-30', 'IT Recruiting', 5, 18),
(6, 'Florencia', 'Carrizo', 'florencia.recruiter@uala.com.ar', '1994-09-18', 'Fintech Talent', 6, 19);

-- Clientes particulares que publican proyectos freelance (perfil_id 20 al 25)
INSERT IGNORE INTO perfil (perfil_id, usuario, email, password, ubicacion, descripcion, is_active, rol_id) VALUES
(20, 'ValeriaStartup', 'valeria.rossi@fintechstartup.io', '$2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu', 'Montevideo, Uruguay', 'Fundadora de startup Fintech orientada a microcréditos para emprendedores.', TRUE, 2),
(21, 'SantiagoPixels', 'santiago@agenciapixels.com', '$2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu', 'Buenos Aires, Argentina', 'Director de agencia de marketing y diseño digital con clientes internacionales.', TRUE, 2),
(22, 'ElenaMediConnect', 'elena.vega@mediconnect.org', '$2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu', 'Santiago, Chile', 'Coordinadora de tecnología en plataforma de telemedicina y turnos médicos online.', TRUE, 2),
(23, 'LucasLogistica', 'lucas@logisticapro.net', '$2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu', 'Rosario, Argentina', 'Gerente de operaciones en empresa logística de última milla y geolocalización.', TRUE, 2),
(24, 'MarianaEdTech', 'mariana.lopez@aprendeonline.edu', '$2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu', 'Bogotá, Colombia', 'Directora de producto en plataforma educativa de cursos interactivos.', TRUE, 2),
(25, 'MartinAgroTech', 'martin@agrotechdigital.com', '$2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu', 'Córdoba, Argentina', 'Ingeniero agrónomo e impulsor de tecnología IoT y monitoreo de cultivos.', TRUE, 2);

INSERT IGNORE INTO cliente_particular (cliente_particular_id, nombre, apellido, email, fecha_nacimiento, telefono, pais, perfil_id) VALUES
(2, 'Valeria', 'Rossi', 'valeria.rossi@fintechstartup.io', '1986-04-12', '+59899123456', 'Uruguay', 20),
(3, 'Santiago', 'Morales', 'santiago@agenciapixels.com', '1984-12-05', '+5491134567890', 'Argentina', 21),
(4, 'Elena', 'Vega', 'elena.vega@mediconnect.org', '1989-08-22', '+56987654321', 'Chile', 22),
(5, 'Lucas', 'Benítez', 'lucas@logisticapro.net', '1982-06-15', '+5493415678901', 'Argentina', 23),
(6, 'Mariana', 'López', 'mariana.lopez@aprendeonline.edu', '1991-03-29', '+573109876543', 'Colombia', 24),
(7, 'Martín', 'Fierro', 'martin@agrotechdigital.com', '1980-10-18', '+5493514567892', 'Argentina', 25);

-- Employees / Freelancers / Postulantes (perfil_id 26 al 32)
INSERT IGNORE INTO perfil (perfil_id, usuario, email, password, ubicacion, descripcion, is_active, rol_id) VALUES
(26, 'SofiaFullstack', 'sofia.martinez@dev.io', '$2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu', 'Buenos Aires, Argentina', 'Senior Fullstack Engineer con 6 años de experiencia en Angular, React, Node.js y Spring Boot.', TRUE, 4),
(27, 'MateoCloudOps', 'mateo.rodriguez@cloudops.io', '$2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu', 'Mendoza, Argentina', 'DevOps & Cloud Engineer especializado en AWS, Kubernetes, Terraform y pipelines CI/CD.', TRUE, 4),
(28, 'CamilaDesigner', 'camila.fernandez@design.io', '$2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu', 'Montevideo, Uruguay', 'Product Designer y Frontend UI Developer apasionada por Design Systems y accesibilidad.', TRUE, 5),
(29, 'LucasMobile', 'lucas.gomez@mobileapps.dev', '$2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu', 'Rosario, Argentina', 'Desarrollador Mobile especializado en Flutter, Android nativo (Kotlin) y React Native.', TRUE, 4),
(30, 'ValentinaData', 'valentina.perez@datainsights.ai', '$2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu', 'Buenos Aires, Argentina', 'Data Scientist e Ingeniera de Datos con foco en Python, Machine Learning y análisis predictivo.', TRUE, 5),
(31, 'NicolasQA', 'nicolas.diaz@qasolutions.io', '$2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu', 'La Plata, Argentina', 'QA Automation Engineer experto en Cypress, Playwright, Selenium y testing de APIs REST.', TRUE, 5),
(32, 'LucianaJava', 'luciana.alvarez@cloudbackend.io', '$2a$10$u6H6dHwPd/sxic3NosWUxeQIDmbO/ZZJSHkuGGUt8Ey1ihwXwfyTu', 'Córdoba, Argentina', 'Backend Developer especializada en Java 21, Spring Boot 3, Hibernate y microservicios reactivos.', TRUE, 5);

INSERT IGNORE INTO employee (employee_id, nombre, apellido, email, fecha_nacimiento, freelance, enterprise, perfil_id) VALUES
(3, 'Sofía', 'Martínez', 'sofia.martinez@dev.io', '1994-05-18', TRUE, TRUE, 26),
(5, 'Mateo', 'Rodríguez', 'mateo.rodriguez@cloudops.io', '1991-09-23', TRUE, TRUE, 27),
(6, 'Camila', 'Fernández', 'camila.fernandez@design.io', '1996-01-11', TRUE, FALSE, 28),
(7, 'Lucas', 'Gómez', 'lucas.gomez@mobileapps.dev', '1993-11-04', TRUE, TRUE, 29),
(8, 'Valentina', 'Pérez', 'valentina.perez@datainsights.ai', '1995-07-30', FALSE, TRUE, 30),
(9, 'Nicolás', 'Díaz', 'nicolas.diaz@qasolutions.io', '1992-04-19', TRUE, TRUE, 31),
(10, 'Luciana', 'Álvarez', 'luciana.alvarez@cloudbackend.io', '1997-12-08', FALSE, TRUE, 32);

-- -----------------------------------------------------------------------------------
-- 4. HABILIDADES DE EMPLEADOS (Nivel 1 a 5)
-- -----------------------------------------------------------------------------------
-- Joaquin Nieva (employee 4 / usuario actual)
INSERT IGNORE INTO employee_habilidad (employee_id, habilidad_id, nivel) VALUES
(4, (SELECT habilidad_id FROM habilidad WHERE nombre_habilidad='Java'), 5),
(4, (SELECT habilidad_id FROM habilidad WHERE nombre_habilidad='Spring Boot'), 5),
(4, (SELECT habilidad_id FROM habilidad WHERE nombre_habilidad='Angular'), 4),
(4, (SELECT habilidad_id FROM habilidad WHERE nombre_habilidad='MySQL'), 5),
(4, (SELECT habilidad_id FROM habilidad WHERE nombre_habilidad='Git'), 5),
(4, (SELECT habilidad_id FROM habilidad WHERE nombre_habilidad='Docker'), 4);

-- Sofía Martínez (employee 3)
INSERT IGNORE INTO employee_habilidad (employee_id, habilidad_id, nivel) VALUES
(3, (SELECT habilidad_id FROM habilidad WHERE nombre_habilidad='React'), 5),
(3, (SELECT habilidad_id FROM habilidad WHERE nombre_habilidad='TypeScript'), 5),
(3, (SELECT habilidad_id FROM habilidad WHERE nombre_habilidad='Node.js'), 4),
(3, (SELECT habilidad_id FROM habilidad WHERE nombre_habilidad='Next.js'), 5),
(3, (SELECT habilidad_id FROM habilidad WHERE nombre_habilidad='Tailwind CSS'), 5);

-- Mateo Rodríguez (employee 5)
INSERT IGNORE INTO employee_habilidad (employee_id, habilidad_id, nivel) VALUES
(5, (SELECT habilidad_id FROM habilidad WHERE nombre_habilidad='AWS'), 5),
(5, (SELECT habilidad_id FROM habilidad WHERE nombre_habilidad='Kubernetes'), 5),
(5, (SELECT habilidad_id FROM habilidad WHERE nombre_habilidad='Docker'), 5),
(5, (SELECT habilidad_id FROM habilidad WHERE nombre_habilidad='CI/CD'), 5),
(5, (SELECT habilidad_id FROM habilidad WHERE nombre_habilidad='Golang'), 4);

-- Camila Fernández (employee 6)
INSERT IGNORE INTO employee_habilidad (employee_id, habilidad_id, nivel) VALUES
(6, (SELECT habilidad_id FROM habilidad WHERE nombre_habilidad='Figma'), 5),
(6, (SELECT habilidad_id FROM habilidad WHERE nombre_habilidad='Tailwind CSS'), 5),
(6, (SELECT habilidad_id FROM habilidad WHERE nombre_habilidad='React'), 4),
(6, (SELECT habilidad_id FROM habilidad WHERE nombre_habilidad='TypeScript'), 4);

-- Lucas Gómez (employee 7)
INSERT IGNORE INTO employee_habilidad (employee_id, habilidad_id, nivel) VALUES
(7, (SELECT habilidad_id FROM habilidad WHERE nombre_habilidad='Flutter'), 5),
(7, (SELECT habilidad_id FROM habilidad WHERE nombre_habilidad='Kotlin'), 5),
(7, (SELECT habilidad_id FROM habilidad WHERE nombre_habilidad='Swift'), 4),
(7, (SELECT habilidad_id FROM habilidad WHERE nombre_habilidad='React'), 3);

-- Valentina Pérez (employee 8)
INSERT IGNORE INTO employee_habilidad (employee_id, habilidad_id, nivel) VALUES
(8, (SELECT habilidad_id FROM habilidad WHERE nombre_habilidad='Python'), 5),
(8, (SELECT habilidad_id FROM habilidad WHERE nombre_habilidad='PostgreSQL'), 5),
(8, (SELECT habilidad_id FROM habilidad WHERE nombre_habilidad='MySQL'), 4),
(8, (SELECT habilidad_id FROM habilidad WHERE nombre_habilidad='Docker'), 3);

-- Nicolás Díaz (employee 9)
INSERT IGNORE INTO employee_habilidad (employee_id, habilidad_id, nivel) VALUES
(9, (SELECT habilidad_id FROM habilidad WHERE nombre_habilidad='TypeScript'), 5),
(9, (SELECT habilidad_id FROM habilidad WHERE nombre_habilidad='Node.js'), 4),
(9, (SELECT habilidad_id FROM habilidad WHERE nombre_habilidad='Git'), 5),
(9, (SELECT habilidad_id FROM habilidad WHERE nombre_habilidad='CI/CD'), 4);

-- Luciana Álvarez (employee 10)
INSERT IGNORE INTO employee_habilidad (employee_id, habilidad_id, nivel) VALUES
(10, (SELECT habilidad_id FROM habilidad WHERE nombre_habilidad='Java'), 5),
(10, (SELECT habilidad_id FROM habilidad WHERE nombre_habilidad='Spring Boot'), 5),
(10, (SELECT habilidad_id FROM habilidad WHERE nombre_habilidad='Microservicios'), 5),
(10, (SELECT habilidad_id FROM habilidad WHERE nombre_habilidad='PostgreSQL'), 4),
(10, (SELECT habilidad_id FROM habilidad WHERE nombre_habilidad='Redis'), 4);

-- -----------------------------------------------------------------------------------
-- 5. EXPERIENCIA LABORAL
-- -----------------------------------------------------------------------------------
INSERT IGNORE INTO experiencia (titulo, descripcion, fecha_inicio, fecha_fin, employee_id) VALUES
('Full Stack Developer', 'Desarrollo de plataforma WorkLink con Spring Boot 3 y Angular 19.', '2024-03-01', NULL, 4),
('Backend Developer', 'Construcción de APIs REST y microservicios bancarios en Java.', '2022-01-15', '2024-02-28', 4),
('Lead Frontend Engineer', 'Dirección de arquitectura UI con React, Next.js y Tailwind CSS.', '2022-06-01', NULL, 3),
('DevOps Engineer Senior', 'Automatización de despliegues multi-región en AWS EKS.', '2021-04-01', NULL, 5),
('Senior Product Designer', 'Diseño de sistemas de diseño y flujos de usuario complejos.', '2020-08-01', NULL, 6),
('Mobile Tech Lead', 'Desarrollo de apps Fintech multiplataforma con Flutter.', '2021-02-01', NULL, 7),
('Data Scientist', 'Modelos predictivos de churn y scoring crediticio en Python.', '2022-09-01', NULL, 8),
('QA Automation Lead', 'Frameworks de automatización E2E con Playwright y Cypress.', '2021-10-01', NULL, 9),
('Java Backend Specialist', 'Microservicios de alta concurrencia y mensajería reactiva.', '2023-01-01', NULL, 10);

-- -----------------------------------------------------------------------------------
-- 6. PORTFOLIOS DE CANDIDATOS / FREELANCERS
-- -----------------------------------------------------------------------------------
INSERT IGNORE INTO portfolio (github, linkedin, descripcion, employee_id) VALUES
('https://github.com/joaking102003', 'https://linkedin.com/in/joaquin-nieva', 'Portafolio de proyectos backend, microservicios y desarrollo web full stack.', 4),
('https://github.com/sofiamartinez-dev', 'https://linkedin.com/in/sofiamartinez', 'Portafolio con aplicaciones React, Next.js y componentes Tailwind.', 3),
('https://github.com/mateo-cloudops', 'https://linkedin.com/in/mateorodriguez', 'Infraestructura como código en Terraform, configuraciones Helm y K8s.', 5),
('https://github.com/camila-uxdesign', 'https://linkedin.com/in/camilafernandez', 'Casos de estudio UX/UI y librerías de diseño de componentes.', 6),
('https://github.com/lucasgomez-mob', 'https://linkedin.com/in/lucasgomez', 'Aplicaciones móviles publicadas en App Store y Google Play Store.', 7),
('https://github.com/valentinaperez-ai', 'https://linkedin.com/in/valentinaperez', 'Notebooks de machine learning, pipelines de datos y visualizaciones.', 8),
('https://github.com/nicolasdiaz-qa', 'https://linkedin.com/in/nicolasdiaz', 'Suites automatizadas de testing de integración y rendimiento.', 9),
('https://github.com/lucianaalvarez-code', 'https://linkedin.com/in/lucianaalvarez', 'Repositorios con patrones de diseño, Spring Security y Kafka.', 10);

-- -----------------------------------------------------------------------------------
-- 7. VACANTES LABORALES (24 Vacantes en diferentes empresas y estados)
-- -----------------------------------------------------------------------------------
INSERT IGNORE INTO vacante (vacante_id, puesto, descripcion, fecha_publicacion, fecha_fin, logo, ubicacion, tipo_ubicacion, tipo_vacante, salario, empresa_id, activa) VALUES
(2, 'Senior Java Spring Boot Engineer', 'Desarrollo de microservicios transaccionales con Spring Boot 3, Kafka y MySQL.', '2026-10-01', '2026-12-15', '🚀', 'Buenos Aires, Argentina', 'Remoto', 'Tiempo Completo', '$4500 USD/mes', 1, TRUE),
(3, 'Fullstack Angular & Node.js Developer', 'Creación de portales de autogestión corporativos con Angular 19 y Node.', '2026-10-05', '2026-12-01', '⚡', 'Madrid, España', 'Híbrido', 'Tiempo Completo', '€48.000 /año', 1, TRUE),
(4, 'Database Administrator DBA MySQL', 'Optimización de consultas, particionado de tablas y replicación MySQL 8.', '2026-09-15', '2026-11-30', '💾', 'Buenos Aires, Argentina', 'Remoto', 'Tiempo Completo', '$3800 USD/mes', 1, TRUE),
(5, 'Lead Cloud DevOps AWS & Kubernetes', 'Liderar la infraestructura cloud y migración de contenedores en Mercado Pago.', '2026-10-10', '2026-12-20', '💛', 'Buenos Aires, Argentina', 'Híbrido', 'Tiempo Completo', '$5500 USD/mes', 2, TRUE),
(6, 'Data Engineer Big Data & Spark', 'Construcción de data pipelines para el procesamiento de millones de compras diarias.', '2026-10-12', '2026-12-31', '📊', 'São Paulo, Brasil', 'Remoto', 'Tiempo Completo', '$4800 USD/mes', 2, TRUE),
(7, 'Senior iOS Developer Swift', 'Mejora continua del checkout nativo en la app de Mercado Libre.', '2026-10-15', '2026-12-10', '📱', 'Buenos Aires, Argentina', 'Híbrido', 'Tiempo Completo', '$4600 USD/mes', 2, TRUE),
(8, 'Tech Lead Microservicios Golang', 'Diseño de arquitectura distribuida para pagos internacionales de baja latencia.', '2026-10-18', '2027-01-15', '⚙️', 'Montevideo, Uruguay', 'Remoto', 'Tiempo Completo', '$6000 USD/mes', 2, TRUE),
(9, 'UI/UX Senior Product Designer', 'Diseño de interfaces complejas para clientes Fortune 500.', '2026-10-01', '2026-12-05', '🎨', 'Remoto Global', 'Remoto', 'Tiempo Completo', '$4000 USD/mes', 3, TRUE),
(10, 'QA Automation Lead Playwright', 'Definición de estándares de testing automatizado E2E y rendimiento.', '2026-10-08', '2026-11-28', '🧪', 'Córdoba, Argentina', 'Híbrido', 'Tiempo Completo', '$3500 USD/mes', 3, TRUE),
(11, 'React Native Mobile Engineer', 'Desarrollo de soluciones omnicanal para banca y telecomunicaciones.', '2026-09-20', '2026-11-25', '📲', 'Santiago, Chile', 'Remoto', 'Tiempo Completo', '$4200 USD/mes', 3, TRUE),
(12, 'Scrum Master & Agile Coach', 'Acompañar squads de ingeniería en prácticas Scrum, Kanban y delivery continuo.', '2026-10-05', '2026-12-15', '🎯', 'Buenos Aires, Argentina', 'Híbrido', 'Tiempo Completo', '$3200 USD/mes', 3, TRUE),
(13, 'Identity & Security Engineer', 'Desarrollo de protocolos OAuth 2.1, OIDC y passkeys en la nube Auth0.', '2026-10-02', '2026-12-15', '🔒', 'Remoto Global', 'Remoto', 'Tiempo Completo', '$6500 USD/mes', 4, TRUE),
(14, 'Backend Golang Distributed Systems', 'Desarrollo de APIs críticas de autenticación con disponibilidad 99.999%.', '2026-10-14', '2027-01-10', '🌐', 'Remoto Global', 'Remoto', 'Tiempo Completo', '$6000 USD/mes', 4, TRUE),
(15, 'Principal Software Architect', 'Definición de roadmap tecnológico para servicios multi-tenant SaaS.', '2026-09-01', '2026-11-30', '🏛️', 'Bellevue, EE.UU.', 'Remoto', 'Tiempo Completo', '$8000 USD/mes', 4, TRUE),
(16, 'Cyber Security Pentester', 'Auditoría de código, modelado de amenazas y ethical hacking continuo.', '2026-10-20', '2027-01-30', '🛡️', 'Remoto Global', 'Remoto', 'Tiempo Completo', '$5000 USD/mes', 4, TRUE),
(17, 'Senior Python Backend Developer', 'Motor de búsqueda y cotización de vuelos en tiempo real.', '2026-10-05', '2026-12-20', '✈️', 'Buenos Aires, Argentina', 'Híbrido', 'Tiempo Completo', '$4500 USD/mes', 5, TRUE),
(18, 'Machine Learning Scientist', 'Modelos predictivos de precios de pasajes dinámicos y recomendaciones.', '2026-10-10', '2026-12-18', '🤖', 'Buenos Aires, Argentina', 'Remoto', 'Tiempo Completo', '$5200 USD/mes', 5, TRUE),
(19, 'Frontend React Next.js Specialist', 'Optimización de Core Web Vitals y renderizado híbrido para web móvil.', '2026-10-15', '2026-12-22', '🌐', 'Remoto, Argentina', 'Remoto', 'Tiempo Completo', '$4000 USD/mes', 5, TRUE),
(20, 'Backend Kotlin / Java Developer', 'Core bancario digital, tarjetas prepagas y transferencias inmediatas.', '2026-10-03', '2026-12-10', '💳', 'Buenos Aires, Argentina', 'Híbrido', 'Tiempo Completo', '$4200 USD/mes', 6, TRUE),
(21, 'Mobile Flutter FinTech Developer', 'Nuevas funcionalidades de inversiones y plazos fijos en la app Ualá.', '2026-10-12', '2026-12-15', '📈', 'Buenos Aires, Argentina', 'Remoto', 'Tiempo Completo', '$4000 USD/mes', 6, TRUE),
(22, 'Site Reliability Engineer SRE', 'Monitoreo Prometheus/Grafana y resiliencia para microservicios financieros.', '2026-10-16', '2027-01-05', '🩺', 'Buenos Aires, Argentina', 'Remoto', 'Tiempo Completo', '$4800 USD/mes', 6, TRUE),
(23, 'Junior Frontend Vue.js (Cerrada)', 'Puesto junior de maquetación ya cubierto exitosamente.', '2026-08-01', '2026-09-01', '🔒', 'Buenos Aires, Argentina', 'Presencial', 'Tiempo Completo', '$1500 USD/mes', 5, FALSE),
(24, 'Junior Data Analyst SQL (Cerrada)', 'Procesamiento de reportes comerciales ya finalizado.', '2026-08-10', '2026-09-15', '🔒', 'Buenos Aires, Argentina', 'Híbrido', 'Medio Tiempo', '$1200 USD/mes', 6, FALSE),
(25, 'Intern Soporte IT (Cerrada)', 'Pasantía técnica de soporte interno completada.', '2026-07-01', '2026-08-30', '🔒', 'Córdoba, Argentina', 'Presencial', 'Medio Tiempo', '$800 USD/mes', 3, FALSE);

-- Etiquetas de vacantes
INSERT IGNORE INTO vacante_etiquetas (vacante_id, etiqueta) VALUES
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

-- -----------------------------------------------------------------------------------
-- 8. POSTULACIONES A VACANTES (30 Postulaciones en diversos estados)
-- Estados: PENDIENTE, VISUALIZADO, ENTREVISTA_AGENDADA, RECHAZADO
-- -----------------------------------------------------------------------------------
INSERT IGNORE INTO postulacion_vacante (postulacion_id, fecha_postulacion, estado_postulacion, cv, mensaje, vacante_id, postulante_id) VALUES
-- Postulaciones de Joaquin Nieva (employee 4)
(2, '2026-10-15', 'ENTREVISTA_AGENDADA', '', 'Cuento con amplia experiencia en Spring Boot, JPA y arquitecturas limpias.', 2, 4),
(3, '2026-10-16', 'ENTREVISTA_AGENDADA', '', 'Me motiva mucho el desarrollo frontend en Angular y microservicios.', 3, 4),
(4, '2026-10-20', 'VISUALIZADO', '', 'Postulación para el rol de Backend Kotlin / Java.', 20, 4),
(5, '2026-10-22', 'PENDIENTE', '', 'Experiencia sólida en diseño y optimización de bases de datos relacionales.', 4, 4),

-- Postulaciones de Sofía Martínez (employee 3)
(6, '2026-10-10', 'ENTREVISTA_AGENDADA', '', 'He liderado equipos frontend migrando arquitecturas a Next.js y React.', 19, 3),
(7, '2026-10-12', 'VISUALIZADO', '', 'Interesada en la vacante de Fullstack Angular y Node.js.', 3, 3),
(8, '2026-10-18', 'PENDIENTE', '', 'Aplicación al rol de React Native Mobile Engineer.', 11, 3),

-- Postulaciones de Dev Freelancer (employee 1)
(9, '2026-10-08', 'ENTREVISTA_AGENDADA', '', 'Experiencia comprobable construyendo apps React de alto tráfico.', 19, 1),
(10, '2026-10-14', 'RECHAZADO', '', 'Postulando al rol de Lead DevOps.', 5, 1),
(11, '2026-10-19', 'VISUALIZADO', '', 'Especialista en desarrollo frontend y diseño en Figma.', 9, 1),

-- Postulaciones de Mateo Rodríguez (employee 5)
(12, '2026-10-12', 'ENTREVISTA_AGENDADA', '', 'Cuento con certificaciones AWS Solutions Architect y CKA de Kubernetes.', 5, 5),
(13, '2026-10-17', 'ENTREVISTA_AGENDADA', '', 'Interesado en la posición de Site Reliability Engineer en Ualá.', 22, 5),
(14, '2026-10-21', 'PENDIENTE', '', 'Postulación al rol de Principal Software Architect.', 15, 5),

-- Postulaciones de Camila Fernández (employee 6)
(15, '2026-10-05', 'ENTREVISTA_AGENDADA', '', 'Especialista en Design Systems y diseño de flujos de checkout para e-commerce.', 9, 6),
(16, '2026-10-11', 'VISUALIZADO', '', 'Postulación para diseño UI/UX en Despegar.', 19, 6),
(17, '2026-10-25', 'PENDIENTE', '', 'Interés en participar en el equipo de frontend.', 3, 6),

-- Postulaciones de Lucas Gómez (employee 7)
(18, '2026-10-16', 'ENTREVISTA_AGENDADA', '', 'Más de 4 años desarrollando aplicaciones en Flutter y Kotlin.', 21, 7),
(19, '2026-10-18', 'ENTREVISTA_AGENDADA', '', 'Postulación para iOS nativo con Swift en Mercado Libre.', 7, 7),
(20, '2026-10-22', 'RECHAZADO', '', 'Aplicación para rol de Java Backend.', 2, 7),

-- Postulaciones de Valentina Pérez (employee 8)
(21, '2026-10-14', 'ENTREVISTA_AGENDADA', '', 'Experiencia en pipelines ETL de datos en streaming y modelos ML.', 6, 8),
(22, '2026-10-18', 'VISUALIZADO', '', 'Postulación para Machine Learning Scientist en Despegar.', 18, 8),
(23, '2026-10-24', 'PENDIENTE', '', 'Interés en análisis de datos comerciales.', 4, 8),

-- Postulaciones de Nicolás Díaz (employee 9)
(24, '2026-10-10', 'ENTREVISTA_AGENDADA', '', 'Frameworks de automatización continua integrados en GitHub Actions.', 10, 9),
(25, '2026-10-15', 'VISUALIZADO', '', 'Postulando al rol de SRE en Ualá.', 22, 9),
(26, '2026-10-26', 'PENDIENTE', '', 'Aplicación para testing de APIs en Auth0.', 13, 9),

-- Postulaciones de Luciana Álvarez (employee 10)
(27, '2026-10-08', 'ENTREVISTA_AGENDADA', '', 'Experiencia profunda en Java 21, Spring Boot y microservicios distribuidos.', 2, 10),
(28, '2026-10-12', 'VISUALIZADO', '', 'Postulando para Backend Kotlin en Ualá.', 20, 10),
(29, '2026-10-20', 'RECHAZADO', '', 'Aplicación para rol de DevOps AWS.', 5, 10),

-- Postulaciones de Candidato IT (employee 2)
(30, '2026-10-06', 'ENTREVISTA_AGENDADA', '', 'Desarrollador enfocado en buenas prácticas y Spring Boot.', 2, 2),
(31, '2026-10-15', 'PENDIENTE', '', 'Postulando al rol de DBA MySQL.', 4, 2),
(32, '2026-10-23', 'RECHAZADO', '', 'Postulación para Frontend Angular.', 3, 2);

-- -----------------------------------------------------------------------------------
-- 9. ENTREVISTAS AGENDADAS (Citas con Google Meet para postulaciones aprobadas)
-- -----------------------------------------------------------------------------------
INSERT IGNORE INTO entrevista (entrevista_id, postulacion_id, fecha, hora, link) VALUES
-- Entrevistas para Joaquin Nieva (postulacion 2 y 3)
(1, 2, '2026-11-16', '10:00:00', 'https://meet.google.com/qwe-rtyu-iop'),
(2, 3, '2026-11-18', '15:30:00', 'https://meet.google.com/asd-fghj-klz'),

-- Entrevistas para otros candidatos
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

-- -----------------------------------------------------------------------------------
-- 10. PROYECTOS FREELANCE (23 Proyectos en estados variados)
-- Estados: BUSCANDO_TALENTO, EN_PROGRESO, FINALIZADO
-- -----------------------------------------------------------------------------------
INSERT IGNORE INTO proyecto (proyecto_id, nombre_proyecto, area_proyecto, descripcion, fecha_creacion, presupuesto_max, presupuesto_min, duracion, tipo_pago, estado, freelancer_asignado_id, cliente_id) VALUES
-- Proyectos en progreso asignados a Joaquin Nieva (employee 4)
(1, 'Portal Bancario B2B', 'Backend', 'Desarrollo de API REST en Spring Boot 3 con seguridad OAuth2 y liquidaciones automáticas.', '2026-10-01', 3500.00, 2500.00, '2 meses', 'Precio Fijo', 'EN_PROGRESO', 4, 2),
(2, 'Dashboard Analytics', 'Frontend', 'Dashboard interactivo en Angular con gráficos en tiempo real y exportación PDF/Excel.', '2026-10-15', 2200.00, 1600.00, '1 mes', 'Precio Fijo', 'EN_PROGRESO', 4, 3),

-- Proyecto finalizado asignado a Joaquin Nieva (employee 4)
(3, 'Microservicio Facturacion', 'Backend', 'Módulo de generación de comprobantes electrónicos con firma digital AFIP.', '2026-08-10', 1800.00, 1200.00, '3 semanas', 'Precio Fijo', 'FINALIZADO', 4, 5),

-- Proyectos en progreso asignados a otros freelancers
(4, 'App Delivery React Native', 'Mobile', 'Desarrollo de app móvil de tracking en vivo de pedidos para cadena gastronómica.', '2026-10-05', 4000.00, 3000.00, '2 meses', 'Precio Fijo', 'EN_PROGRESO', 7, 5),
(5, 'Infraestructura Terraform AWS', 'DevOps', 'Automatización de infraestructura como código con VPC, ECS Fargate y RDS.', '2026-10-12', 3000.00, 2200.00, '1 mes', 'Por Hora', 'EN_PROGRESO', 5, 2),
(6, 'Rediseño E-commerce Moda', 'Diseño', 'Diseño UX/UI completo en Figma con prototipo navegable y kit de componentes.', '2026-10-18', 2500.00, 1800.00, '3 semanas', 'Precio Fijo', 'EN_PROGRESO', 6, 3),
(7, 'Plataforma Telemedicina', 'FullStack', 'Videoconsultas médicas en WebRTC, agenda de turnos y recetas digitales.', '2026-09-25', 5000.00, 3800.00, '3 meses', 'Precio Fijo', 'EN_PROGRESO', 3, 4),
(8, 'Suite Testing Automatizado', 'QA', 'Implementación de pruebas automatizadas E2E en Playwright para pasarela de pagos.', '2026-10-08', 2000.00, 1400.00, '1 mes', 'Precio Fijo', 'EN_PROGRESO', 9, 2),

-- Proyectos finalizados con calificaciones
(9, 'Landing Page Corporativa', 'Frontend', 'Landing page de alta conversión optimizada para SEO con Next.js y Tailwind.', '2026-07-01', 1200.00, 900.00, '2 semanas', 'Precio Fijo', 'FINALIZADO', 1, 3),
(10, 'App Monitoreo de Cultivos', 'Mobile', 'App offline-first para registro de fertilización y humedad con geolocalización.', '2026-07-15', 3600.00, 2800.00, '2 meses', 'Precio Fijo', 'FINALIZADO', 7, 7),
(11, 'Pipeline ETL Datos Ventas', 'Data', 'Extracción y transformación de datos de Shopify a almacén PostgreSQL en Python.', '2026-08-01', 2500.00, 1800.00, '3 semanas', 'Precio Fijo', 'FINALIZADO', 8, 3),
(12, 'Auditoría Seguridad Web', 'DevOps', 'Penetration testing y hardening de servidores Linux y bases de datos.', '2026-08-20', 2000.00, 1500.00, '2 semanas', 'Precio Fijo', 'FINALIZADO', 5, 6),
(13, 'Plataforma Cursos Online', 'FullStack', 'LMS interactivo con reproducción de videos, exámenes y emisión de certificados.', '2026-06-10', 4500.00, 3500.00, '3 meses', 'Precio Fijo', 'FINALIZADO', 3, 6),

-- Proyectos abiertos buscando talento
(14, 'Integración Pasarela Pagos', 'Backend', 'Integración de webhooks de Mercado Pago y Stripe con reintentos exponenciales.', '2026-11-01', 1800.00, 1300.00, '3 semanas', 'Precio Fijo', 'BUSCANDO_TALENTO', NULL, 2),
(15, 'App Control de Stock IoT', 'Mobile', 'Lectura de códigos QR y chips RFID desde teléfonos Android en almacén.', '2026-11-02', 3200.00, 2400.00, '6 semanas', 'Precio Fijo', 'BUSCANDO_TALENTO', NULL, 5),
(16, 'Migración Cloud Kubernetes', 'DevOps', 'Migración de arquitectura monolítica a clúster Kubernetes en Google Cloud.', '2026-11-03', 4500.00, 3200.00, '2 meses', 'Por Hora', 'BUSCANDO_TALENTO', NULL, 6),
(17, 'Sistema Turnos Consultorios', 'FullStack', 'Sistema web para reserva de citas médicas con recordatorios automáticos por WhatsApp.', '2026-11-04', 2800.00, 2000.00, '1 mes', 'Precio Fijo', 'BUSCANDO_TALENTO', NULL, 4),
(18, 'Design System Componentes', 'Diseño', 'Creación de biblioteca de componentes reutilizables en Figma y Storybook.', '2026-11-05', 2000.00, 1500.00, '3 semanas', 'Precio Fijo', 'BUSCANDO_TALENTO', NULL, 3),
(19, 'Modelo Scoring Predictivo', 'Data', 'Algoritmo de clasificación en Scikit-Learn para predecir morosidad crediticia.', '2026-11-06', 3000.00, 2200.00, '1 mes', 'Precio Fijo', 'BUSCANDO_TALENTO', NULL, 2),
(20, 'Automatización Pruebas Web', 'QA', 'Creación de pipeline de pruebas funcionales automatizadas con Cypress.', '2026-11-07', 1500.00, 1000.00, '3 semanas', 'Precio Fijo', 'BUSCANDO_TALENTO', NULL, 6),
(21, 'Plataforma Subastas Ganado', 'FullStack', 'Página web para remates ganaderos en vivo con streaming de baja latencia.', '2026-11-08', 5500.00, 4000.00, '3 meses', 'Precio Fijo', 'BUSCANDO_TALENTO', NULL, 7),
(22, 'Optimización Motor MySQL', 'Backend', 'Tuning de base de datos MySQL con más de 20 millones de registros transaccionales.', '2026-11-09', 2400.00, 1800.00, '2 semanas', 'Precio Fijo', 'BUSCANDO_TALENTO', NULL, 5),
(23, 'App Billetera Virtual Crypto', 'Mobile', 'Frontend móvil para consulta de saldos y transacciones en stablecoins.', '2026-11-10', 4200.00, 3000.00, '2 meses', 'Precio Fijo', 'BUSCANDO_TALENTO', NULL, 2);

-- Habilidades de proyectos
INSERT IGNORE INTO proyecto_habilidades (proyecto_id, habilidad) VALUES
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

-- Hitos de proyectos
INSERT IGNORE INTO hito_proyecto (hito_id, titulo, descripcion, monto, estado, fecha_limite, proyecto_id) VALUES
-- Hitos Proyecto 1 (Joaquin Nieva)
(1, 'Diseño de Modelo Relacional y Auth', 'Estructura de base de datos y autenticación JWT.', 1200.00, 'COMPLETADO', '2026-10-25', 1),
(2, 'Endpoints de Liquidación y Webhooks', 'Lógica transaccional de pagos B2B.', 1300.00, 'PENDIENTE', '2026-11-20', 1),
(3, 'Pruebas de Carga y Entrega Final', 'Auditoría de rendimiento y documentación Swagger.', 1000.00, 'PENDIENTE', '2026-12-05', 1),

-- Hitos Proyecto 2 (Joaquin Nieva)
(4, 'Arquitectura de Componentes y Estado', 'Configuración de Angular Signals y routing.', 800.00, 'COMPLETADO', '2026-10-30', 2),
(5, 'Integración Gráficos y Exportación', 'Visualización con Chart.js y exportación a PDF.', 1400.00, 'PENDIENTE', '2026-11-25', 2),

-- Hitos Proyecto 3 (Joaquin Nieva - Finalizado)
(6, 'Conexión Web Services AFIP', 'Comunicación con certificados y claves fiscales.', 900.00, 'PAGADO', '2026-08-25', 3),
(7, 'Generación de PDFs y Envíos Mail', 'Generación de facturas A y B con código QR.', 900.00, 'PAGADO', '2026-09-02', 3),

-- Hitos Proyectos Finalizados (9, 10, 11, 12, 13)
(8, 'Maquetación responsive', 'Diseño de todas las secciones en Next.js.', 600.00, 'PAGADO', '2026-07-08', 9),
(9, 'Optimización SEO y Deploy Vercel', 'Core Web Vitals verdes y analytics.', 600.00, 'PAGADO', '2026-07-15', 9),
(10, 'Módulo GPS y Base SQLite Local', 'Sincronización bidireccional offline.', 1800.00, 'PAGADO', '2026-08-10', 10),
(11, 'Entrega en Play Store', 'Publicación en canal privado de la empresa.', 1800.00, 'PAGADO', '2026-09-15', 10),
(12, 'Conectores API Shopify a Postgres', 'Pipeline de carga incremental en Python.', 1250.00, 'PAGADO', '2026-08-12', 11),
(13, 'Validación de Consistencia y Dashboards', 'Reporte automático de discrepancias.', 1250.00, 'PAGADO', '2026-08-22', 11),
(14, 'Reporte de Vulnerabilidades Encontradas', 'Informe técnico de puntos críticos.', 1000.00, 'PAGADO', '2026-08-28', 12),
(15, 'Hardening y Re-testing', 'Verificación de correcciones de seguridad.', 1000.00, 'PAGADO', '2026-09-05', 12),
(16, 'Arquitectura de Vídeo y Streaming', 'Integración CDN y reproductor seguro.', 2250.00, 'PAGADO', '2026-07-20', 13),
(17, 'Sistema de Exámenes y Certificados', 'Generación de PDFs dinámicos con firma hash.', 2250.00, 'PAGADO', '2026-09-10', 13);

-- Calificaciones de proyectos finalizados
INSERT IGNORE INTO calificacion_proyecto (calificacion_id, puntaje, titulo_resena, comentario, fecha_comentario, proyecto_id) VALUES
(1, 5, 'Excelente dominio técnico de Spring Boot', 'Joaquín entregó la integración fiscal sin fisuras y en tiempo récord. Código impecable.', '2026-09-05', 3),
(2, 5, 'Landing veloz y diseño moderno', 'Gran trabajo con Next.js y Tailwind. Mejoró notablemente la conversión de la agencia.', '2026-07-16', 9),
(3, 5, 'App robusta para el campo', 'Funciona a la perfección sin conexión a internet en medio de los lotes agrícolas.', '2026-09-18', 10),
(4, 5, 'Pipeline veloz y automatizado', 'Valentina resolvió los cuellos de botella de datos y ahora el reporte es diario.', '2026-08-25', 11),
(5, 5, 'Auditoría rigurosa y profesional', 'Mateo encontró brechas que ningún scanner detectaba y nos ayudó a mitigarlas.', '2026-09-08', 12),
(6, 5, 'LMS de clase mundial', 'Sofía desarrolló una plataforma muy fluida que ya usan más de 2000 estudiantes.', '2026-09-12', 13);

-- Postulaciones a proyectos freelance
INSERT IGNORE INTO postulacion_proyecto (post_proy_id, descripcion, valor_propuesto, fecha_postulacion, estado_postulacion, employee_id, proyecto_id) VALUES
(1, 'Cuento con amplia experiencia en APIs financieras de Spring Boot. Me encantaría desarrollar este portal.', 3200.00, '2026-10-02', 'ACEPTADA', 4, 1),
(2, 'Especialista en Angular 19 y arquitectura reactiva. Puedo completar el dashboard en 4 semanas.', 2000.00, '2026-10-16', 'ACEPTADA', 4, 2),
(3, 'Tengo módulos fiscales AFIP ya desarrollados listos para adaptar en Java.', 1800.00, '2026-08-11', 'ACEPTADA', 4, 3),
(4, 'Desarrollador Flutter con experiencia en geolocalización en tiempo real.', 3800.00, '2026-10-06', 'ACEPTADA', 7, 4),
(5, 'Arquitecto cloud certificado AWS. Infraestructura lista en Terraform en 3 semanas.', 2800.00, '2026-10-13', 'ACEPTADA', 5, 5),
(6, 'Diseño interfaces centradas en usuario con sistemas de diseño atómicos.', 2300.00, '2026-10-19', 'ACEPTADA', 6, 6),
(7, 'Desarrollo fullstack con WebRTC y APIs seguras en Node y React.', 4800.00, '2026-09-26', 'ACEPTADA', 3, 7),
(8, 'Ingeniero QA con amplia experiencia en automatización de checkout y pasarelas.', 1800.00, '2026-10-09', 'ACEPTADA', 9, 8),
-- Postulaciones pendientes en proyectos abiertos buscando talento
(9, 'Puedo integrar Mercado Pago y Stripe con tolerancia a fallos y reintentos.', 1600.00, '2026-11-02', 'PENDIENTE', 4, 14),
(10, 'Experiencia integrando lectores de código QR y RFID en Android nativo.', 3000.00, '2026-11-03', 'PENDIENTE', 7, 15),
(11, 'Especialista en migraciones Kubernetes sin downtime en GCP.', 4000.00, '2026-11-04', 'PENDIENTE', 5, 16),
(12, 'Desarrollé sistemas de agendamiento médico similares en Angular y Spring.', 2600.00, '2026-11-05', 'PENDIENTE', 4, 17),
(13, 'Puedo crear el Design System completo en Figma y documentarlo en Storybook.', 1800.00, '2026-11-06', 'PENDIENTE', 6, 18),
(14, 'Data scientist con experiencia en modelos de riesgo y scoring crediticio.', 2800.00, '2026-11-07', 'PENDIENTE', 8, 19),
(15, 'Automatización de tests de extremo a extremo con Cypress y reportes Allure.', 1300.00, '2026-11-08', 'PENDIENTE', 9, 20);

COMMIT;
