-- Tabla: roles
CREATE TABLE IF NOT EXISTS roles (
                                     id SERIAL PRIMARY KEY,
                                     nombre VARCHAR(255) NOT NULL UNIQUE,
    descripcion VARCHAR(255)
    );

-- Tabla: usuarios
CREATE TABLE IF NOT EXISTS usuarios (
                                        id SERIAL PRIMARY KEY,
                                        nombre VARCHAR(255),
    apellido VARCHAR(255),
    numero_documento VARCHAR(50),
    celular VARCHAR(50),
    fecha_nacimiento DATE,
    correo VARCHAR(255) UNIQUE NOT NULL,
    clave VARCHAR(255) NOT NULL,
    id_rol INTEGER,
    CONSTRAINT fk_usuarios_roles FOREIGN KEY (id_rol) REFERENCES roles (id)
    );
