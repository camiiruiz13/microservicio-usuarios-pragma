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
