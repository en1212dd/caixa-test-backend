-- INIT.SQL for MariaDB
-- Database initialization script

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS loan_db;
USE loan_db;

-- Table: document_type (Enum)
CREATE TABLE IF NOT EXISTS document_type (
    id TINYINT UNSIGNED NOT NULL PRIMARY KEY,
    code VARCHAR(20) NOT NULL UNIQUE,
    description VARCHAR(100) NOT NULL
);

-- Table: loan_status (Enum)
CREATE TABLE IF NOT EXISTS loan_status (
    id TINYINT UNSIGNED NOT NULL PRIMARY KEY,
    code VARCHAR(20) NOT NULL UNIQUE,
    description VARCHAR(100) NOT NULL
);

-- Table: currency
CREATE TABLE IF NOT EXISTS currency (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(10) NOT NULL UNIQUE,
    description VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Table: client
CREATE TABLE IF NOT EXISTS client (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(255) NOT NULL,
    document_type_id TINYINT UNSIGNED NOT NULL,
    document_number VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT uk_client_document UNIQUE (document_type_id, document_number),
    CONSTRAINT fk_client_document_type 
        FOREIGN KEY (document_type_id) 
        REFERENCES document_type(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

-- Table: loan_request
CREATE TABLE IF NOT EXISTS loan_request (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    client_id BIGINT NOT NULL,
    amount DECIMAL(15,2) NOT NULL CHECK (amount > 0),
    currency_id BIGINT NOT NULL,
    loan_status_id TINYINT UNSIGNED NOT NULL DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_loan_request_client 
        FOREIGN KEY (client_id) 
        REFERENCES client(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_loan_request_currency 
        FOREIGN KEY (currency_id) 
        REFERENCES currency(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,
    CONSTRAINT fk_loan_request_status 
        FOREIGN KEY (loan_status_id) 
        REFERENCES loan_status(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

-- Insert enum values for document_type
INSERT INTO document_type (id, code, description) VALUES
(1, 'DNI', 'National Identity Document'),
(2, 'NIE', 'Foreigner Identification Number'),
(3, 'PASSPORT', 'Passport');

-- Insert enum values for loan_status
INSERT INTO loan_status (id, code, description) VALUES
(1, 'PENDING', 'Pending review'),
(2, 'APPROVED', 'Approved'),
(3, 'REJECTED', 'Rejected'),
(4, 'CANCELLED', 'Cancelled');

-- Insert some default currencies
INSERT INTO currency (code, description) VALUES
('EUR', 'Euro'),
('USD', 'US Dollar'),
('GBP', 'British Pound'),
('JPY', 'Japanese Yen'),
('CHF', 'Swiss Franc');

-- Insert random client data with different document types
INSERT INTO client (full_name, document_type_id, document_number) VALUES
-- DNI documents (Spanish ID)
('John Smith', 1, '12345678A'),
('Maria Garcia', 1, '87654321B'),
('Carlos Rodriguez', 1, '23456789C'),
('Ana Lopez', 1, '98765432D'),
('David Martinez', 1, '34567890E'),

-- NIE documents (Foreigner ID in Spain)
('Emma Wilson', 2, 'X1234567A'),
('Luca Bianchi', 2, 'Y9876543B'),
('Sophie Dubois', 2, 'Z2345678C'),
('Mohamed Ali', 2, 'X8765432D'),
('Wei Chen', 2, 'Y3456789E'),

-- Passport documents
('James Johnson', 3, 'P12345678'),
('Sarah Miller', 3, 'P87654321'),
('Robert Brown', 3, 'P23456789'),
('Lisa Taylor', 3, 'P98765432'),
('Michael Wilson', 3, 'P34567890'),

-- Mixed additional clients
('Thomas Anderson', 1, '45678901F'),
('Olivia Parker', 2, 'Z4567890F'),
('Daniel White', 3, 'P45678901'),
('Isabella Scott', 1, '56789012G'),
('William Green', 2, 'X5678901G');
/*

Datos inecesarios para pruebas de loan_request creados por la IA

-- Insert random loan requests
INSERT INTO loan_request (client_id, amount, currency_id, loan_status_id) VALUES
-- Pending requests
(1, 5000.00, 1, 1),
(2, 12000.50, 1, 1),
(3, 7500.00, 2, 1),
(4, 30000.00, 3, 1),
(5, 15000.75, 1, 1),

-- Approved requests
(6, 25000.00, 1, 2),
(7, 18000.00, 2, 2),
(8, 9500.50, 1, 2),
(9, 42000.00, 3, 2),
(10, 12500.00, 1, 2),

-- Rejected requests
(11, 80000.00, 1, 3),
(12, 22000.00, 2, 3),
(13, 15000.00, 1, 3),
(14, 35000.00, 3, 3),
(15, 28000.50, 1, 3),

-- Cancelled requests
(16, 10000.00, 1, 4),
(17, 16500.00, 2, 4),
(18, 9000.00, 1, 4),

-- Multiple requests for same client
(1, 8000.00, 1, 2),
(2, 20000.00, 1, 1),
(6, 15000.00, 2, 2),
(9, 30000.00, 1, 3);
*/

/* inecesario para una base de datos tan pequeña
-- Create indexes for better performance
CREATE INDEX idx_client_full_name ON client(full_name);
CREATE INDEX idx_client_document_number ON client(document_number);
CREATE INDEX idx_loan_request_client ON loan_request(client_id);
CREATE INDEX idx_loan_request_status ON loan_request(loan_status_id);
CREATE INDEX idx_loan_request_created ON loan_request(created_at);
*/
-- Create a view for client information with document type
-- interesante para endpoint de cliente
CREATE OR REPLACE VIEW v_client_detail AS
SELECT 
    c.id,
    c.full_name,
    dt.code AS document_type,
    c.document_number,
    c.created_at,
    c.updated_at
FROM client c
JOIN document_type dt ON c.document_type_id = dt.id;

-- Create a view for loan requests with all details
-- interesante para endpoint de solicitud de prestamo
CREATE OR REPLACE VIEW v_loan_request_detail AS
SELECT 
    lr.id,
    lr.amount,
    c.code AS currency,
    ls.code AS status,
    cl.full_name AS client_name,
    dt.code AS client_document_type,
    cl.document_number,
    lr.created_at,
    lr.updated_at
FROM loan_request lr
JOIN client cl ON lr.client_id = cl.id
JOIN currency c ON lr.currency_id = c.id
JOIN loan_status ls ON lr.loan_status_id = ls.id
JOIN document_type dt ON cl.document_type_id = dt.id;
/*
Vista inecesaria para pruebas de resumen creadas por la IA
-- Create a summary view
CREATE OR REPLACE VIEW v_loan_summary AS
SELECT 
    ls.code AS status,
    c.code AS currency,
    COUNT(*) AS total_requests,
    SUM(lr.amount) AS total_amount,
    AVG(lr.amount) AS average_amount
FROM loan_request lr
JOIN loan_status ls ON lr.loan_status_id = ls.id
JOIN currency c ON lr.currency_id = c.id
GROUP BY ls.code, c.code;
*/ 
-- Show sample data from the views
--interesante para ver el estado inicial en docker
SELECT '=== CLIENT DETAILS (First 5) ===' AS section;
SELECT * FROM v_client_detail LIMIT 5;

SELECT '=== DATABASE INITIALIZED SUCCESSFULLY ===' AS message;
SELECT 
    (SELECT COUNT(*) FROM client) AS total_clients,
    (SELECT COUNT(DISTINCT document_type_id) FROM client) AS document_types_used;