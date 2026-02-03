-- INIT.SQL for MariaDB
-- Database initialization script

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS prestamos_db;
USE prestamos_db;

-- Table: tipo_documento (Enum)
CREATE TABLE tipo_documento (
    id TINYINT UNSIGNED NOT NULL PRIMARY KEY,
    codigo VARCHAR(10) NOT NULL UNIQUE,
    descripcion VARCHAR(50) NOT NULL
);

-- Table: estado_solicitud (Enum)
CREATE TABLE estado_solicitud (
    id TINYINT UNSIGNED NOT NULL PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    descripcion VARCHAR(50) NOT NULL
);

-- Table: divisa
CREATE TABLE divisa (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    codigo VARCHAR(3) NOT NULL UNIQUE,
    descripcion VARCHAR(100) NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Table: cliente
CREATE TABLE cliente (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    nombre_completo VARCHAR(255) NOT NULL,
    tipo_documento_id TINYINT UNSIGNED NOT NULL,
    numero_documento VARCHAR(50) NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT uk_cliente_documento UNIQUE (tipo_documento_id, numero_documento),
    CONSTRAINT fk_cliente_tipo_documento 
        FOREIGN KEY (tipo_documento_id) 
        REFERENCES tipo_documento(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

-- Table: solicitud_prestamo
CREATE TABLE solicitud_prestamo (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    cliente_id BIGINT NOT NULL,
    importe DECIMAL(15,2) NOT NULL CHECK (importe > 0),
    divisa_id BIGINT NOT NULL,
    estado_solicitud_id TINYINT UNSIGNED NOT NULL DEFAULT 1,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_solicitud_cliente 
        FOREIGN KEY (cliente_id) 
        REFERENCES cliente(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_solicitud_divisa 
        FOREIGN KEY (divisa_id) 
        REFERENCES divisa(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    CONSTRAINT fk_solicitud_estado 
        FOREIGN KEY (estado_solicitud_id) 
        REFERENCES estado_solicitud(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

-- Insert enum values for tipo_documento
INSERT INTO tipo_documento (id, codigo, descripcion) VALUES
(1, 'DNI', 'Documento Nacional de Identidad'),
(2, 'NIE', 'Número de Identificación de Extranjero'),
(3, 'PASSPORT', 'Pasaporte');

-- Insert enum values for estado_solicitud
INSERT INTO estado_solicitud (id, codigo, descripcion) VALUES
(1, 'PENDIENTE', 'Solicitud pendiente de revisión'),
(2, 'APROBADA', 'Solicitud aprobada'),
(3, 'RECHAZADA', 'Solicitud rechazada'),
(4, 'CANCELADA', 'Solicitud cancelada');

-- Insert some default currencies
INSERT INTO divisa (codigo, descripcion) VALUES
('EUR', 'Euro'),
('USD', 'Dólar estadounidense'),
('GBP', 'Libra esterlina');

-- Create indexes for better performance
CREATE INDEX idx_cliente_nombre ON cliente(nombre_completo);
CREATE INDEX idx_cliente_documento ON cliente(numero_documento);
CREATE INDEX idx_solicitud_cliente ON solicitud_prestamo(cliente_id);
CREATE INDEX idx_solicitud_estado ON solicitud_prestamo(estado_solicitud_id);
CREATE INDEX idx_solicitud_fecha ON solicitud_prestamo(fecha_creacion);

-- Create a view for client information with document type
CREATE VIEW v_cliente_detalle AS
SELECT 
    c.id,
    c.nombre_completo,
    td.codigo AS tipo_documento,
    c.numero_documento,
    c.fecha_creacion,
    c.fecha_modificacion
FROM cliente c
JOIN tipo_documento td ON c.tipo_documento_id = td.id;

-- Create a view for loan requests with all details
CREATE VIEW v_solicitud_detalle AS
SELECT 
    sp.id,
    sp.importe,
    d.codigo AS divisa,
    es.codigo AS estado,
    c.nombre_completo,
    sp.fecha_creacion,
    sp.fecha_modificacion
FROM solicitud_prestamo sp
JOIN cliente c ON sp.cliente_id = c.id
JOIN divisa d ON sp.divisa_id = d.id
JOIN estado_solicitud es ON sp.estado_solicitud_id = es.id;

-- Create a user for the application (optional)
-- CREATE USER IF NOT EXISTS 'prestamos_user'@'%' IDENTIFIED BY 'secure_password';
-- GRANT SELECT, INSERT, UPDATE, DELETE ON prestamos_db.* TO 'prestamos_user'@'%';
-- FLUSH PRIVILEGES;

-- Show confirmation
SELECT 'Database initialized successfully' AS message;