INSERT INTO roles (nombre, descripcion)
SELECT 'ADMIN', 'Administrador del sistema'
    WHERE NOT EXISTS (SELECT 1 FROM roles WHERE nombre = 'ADMIN');

INSERT INTO roles (nombre, descripcion)
SELECT 'PROPIETARIO', 'Dueño de restaurante'
    WHERE NOT EXISTS (SELECT 1 FROM roles WHERE nombre = 'PROPIETARIO');

INSERT INTO roles (nombre, descripcion)
SELECT 'EMPLEADO', 'Empleado del restaurante'
    WHERE NOT EXISTS (SELECT 1 FROM roles WHERE nombre = 'EMPLEADO');

INSERT INTO roles (nombre, descripcion)
SELECT 'CLIENTE', 'Cliente del restaurante'
    WHERE NOT EXISTS (SELECT 1 FROM roles WHERE nombre = 'CLIENTE');

-- Insertar usuario administrador si no existe (por correo)
INSERT INTO usuarios (
    nombre, apellido, numero_documento, celular,
    fecha_nacimiento, correo, clave, id_rol
)
SELECT
    'Camilo',
    'Ruiz',
    '1110234',
    '+573211234567',
    '1995-01-01',
    'ccruiz@mail.com',
    '$2a$10$CJLx2p03iMbnWNEQBV0ElehOw62dZfNlbSdQBOP2UGLP1rGy5STr.',
    1
    WHERE NOT EXISTS (
    SELECT 1 FROM usuarios WHERE correo = 'ccruiz@mail.com'
);
