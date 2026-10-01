CREATE TABLE proveedor (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    telefono VARCHAR(30),
    correo VARCHAR(150),
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

-- Se permite NULL para conservar los productos creados antes de V3.
-- La asociación se completa mediante la API en el reto final.
ALTER TABLE producto
ADD COLUMN proveedor_id INTEGER;

ALTER TABLE producto
ADD CONSTRAINT fk_producto_proveedor
    FOREIGN KEY (proveedor_id)
    REFERENCES proveedor(id);
