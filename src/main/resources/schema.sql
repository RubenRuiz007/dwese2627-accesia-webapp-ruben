-- Primero se borra la tabla hija (tipo_entrada) y después la padre (establecimiento)
DROP TABLE IF EXISTS tipo_entrada;
DROP TABLE IF EXISTS establecimiento;

-- Tabla ESTABLECIMIENTO (lado "1" de la relación "Ofrece")
-- AforoActual no se guarda porque es un atributo derivado (se calcularía a partir de los accesos).
CREATE TABLE establecimiento
(
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre                  VARCHAR(150)  NOT NULL,
    tipo                    VARCHAR(30)   NOT NULL,
    direccion               VARCHAR(255)  NOT NULL,
    descripcion             VARCHAR(1000),
    aforo_maximo            INT           NOT NULL,
    precio_referencia       DECIMAL(8, 2) NOT NULL,
    edad_minima             INT,
    exige_cuenta_confirmada BOOLEAN       NOT NULL DEFAULT FALSE,
    aforo_publico           BOOLEAN       NOT NULL DEFAULT TRUE,
    activo                  BOOLEAN       NOT NULL DEFAULT TRUE,
    imagen                  VARCHAR(500),
    -- No puede haber dos establecimientos con el mismo nombre en la misma dirección
    CONSTRAINT uk_establecimiento_nombre_direccion UNIQUE (nombre, direccion)
);

-- Tabla TIPO_ENTRADA (lado "N" de la relación "Ofrece")
-- Es una entidad fuerte: tiene su propio id.
-- Cada tipo de entrada pertenece obligatoriamente a UN establecimiento (establecimiento_id NOT NULL).
-- ON DELETE RESTRICT: no deja borrar un establecimiento si todavía tiene tipos de entrada.
CREATE TABLE tipo_entrada
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre             VARCHAR(100)  NOT NULL,
    precio_referencia  DECIMAL(8, 2) NOT NULL,
    cupo               INT,
    establecimiento_id BIGINT        NOT NULL,
    CONSTRAINT fk_tipo_entrada_establecimiento
        FOREIGN KEY (establecimiento_id) REFERENCES establecimiento (id)
            ON DELETE RESTRICT,
    -- Un mismo establecimiento no puede tener dos tipos de entrada con el mismo nombre
    CONSTRAINT uk_tipo_entrada_establecimiento_nombre UNIQUE (establecimiento_id, nombre)
);
