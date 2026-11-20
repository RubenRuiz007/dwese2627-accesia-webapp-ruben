-- Establecimientos
INSERT INTO establecimiento (nombre, tipo, direccion, descripcion, aforo_maximo, precio_referencia, edad_minima,
                             exige_cuenta_confirmada, aforo_publico, activo, imagen)
VALUES ('Sala Luna', 'DISCOTECA', 'Calle Betis 12, Sevilla', 'Discoteca con música electrónica los fines de semana.',
        500, 15.00, 18, TRUE, TRUE, TRUE, NULL);

INSERT INTO establecimiento (nombre, tipo, direccion, descripcion, aforo_maximo, precio_referencia, edad_minima,
                             exige_cuenta_confirmada, aforo_publico, activo, imagen)
VALUES ('El Rincón de Pepe', 'BAR', 'Avenida de la Constitución 5, Sevilla', 'Bar de tapas con terraza.',
        80, 0.00, NULL, FALSE, TRUE, TRUE, NULL);

INSERT INTO establecimiento (nombre, tipo, direccion, descripcion, aforo_maximo, precio_referencia, edad_minima,
                             exige_cuenta_confirmada, aforo_publico, activo, imagen)
VALUES ('Cartuja Center Live', 'SALA_CONCIERTOS', 'Calle Leonardo da Vinci 7, Sevilla', 'Sala de conciertos y eventos.',
        1500, 25.00, 16, TRUE, FALSE, TRUE, NULL);

INSERT INTO establecimiento (nombre, tipo, direccion, descripcion, aforo_maximo, precio_referencia, edad_minima,
                             exige_cuenta_confirmada, aforo_publico, activo, imagen)
VALUES ('FitZone Alixar', 'GIMNASIO', 'Calle Real 40, Castilleja de la Cuesta', 'Gimnasio con sala de musculación y clases.',
        120, 30.00, 16, TRUE, TRUE, TRUE, NULL);

INSERT INTO establecimiento (nombre, tipo, direccion, descripcion, aforo_maximo, precio_referencia, edad_minima,
                             exige_cuenta_confirmada, aforo_publico, activo, imagen)
VALUES ('The Old Pub', 'PUB', 'Calle Feria 21, Sevilla', 'Pub irlandés con música en directo.',
        150, 5.00, 18, FALSE, TRUE, FALSE, NULL);

-- Tipos de entrada (cada uno de UN establecimiento)
INSERT INTO tipo_entrada (nombre, precio_referencia, cupo, establecimiento_id) VALUES ('General', 15.00, 400, 1);
INSERT INTO tipo_entrada (nombre, precio_referencia, cupo, establecimiento_id) VALUES ('VIP', 40.00, 50, 1);
INSERT INTO tipo_entrada (nombre, precio_referencia, cupo, establecimiento_id) VALUES ('Lista de invitados', 0.00, 50, 1);
INSERT INTO tipo_entrada (nombre, precio_referencia, cupo, establecimiento_id) VALUES ('Pista', 25.00, 1200, 3);
INSERT INTO tipo_entrada (nombre, precio_referencia, cupo, establecimiento_id) VALUES ('Grada', 35.00, 300, 3);
INSERT INTO tipo_entrada (nombre, precio_referencia, cupo, establecimiento_id) VALUES ('Pase de día', 8.00, NULL, 4);
INSERT INTO tipo_entrada (nombre, precio_referencia, cupo, establecimiento_id) VALUES ('Bono mensual', 30.00, 100, 4);
INSERT INTO tipo_entrada (nombre, precio_referencia, cupo, establecimiento_id) VALUES ('Entrada con consumición', 5.00, 150, 5);
