DROP TABLE IF EXISTS categorias;
CREATE TABLE categorias(
	codigo_cat serial not null,
	nombre varchar(100) not null,
	categoria_padre int,
	constraint categorias_pk primary key(codigo_cat),
	constraint categorias_fk foreign key(categoria_padre)
	references categorias(codigo_cat)
);

INSERT INTO categorias(nombre, categoria_padre)
	VALUES('Materia prima', null),
			('Proteina', 1),
			('Salsas', 1),
			('Punto de venta', null),
			('Bebidas', 4),
			('Con alcohol', 5),
			('Sin alcohol', 5);			
SELECT * FROM categorias;

DROP TABLE IF EXISTS unidades_medida;
CREATE TABLE unidades_medida(
	nombre varchar(50) not null,
	descripcion varchar(50) not null,
	categoria_udm char(1) not null,
	constraint unidades_medida_pk primary key(nombre),
	constraint categoria_unidad_medida_unidades_medida_fk FOREIGN KEY(categoria_udm)
	references categorias_unidad_medida(codigo)
);

INSERT INTO unidades_medida(nombre, descripcion, categoria_udm)
				VALUES('ml', 'mililitros', 'V'),
						('l', 'litros', 'V'),
						('u', 'unidad', 'U'),
						('d', 'docena', 'U'),
						('g', 'gramos', 'P'),
						('kg', 'kilogramos', 'P'),
						('lb', 'libras', 'P');
SELECT * FROM unidades_medida;

DROP TABLE IF EXISTS categorias_unidad_medida;
CREATE TABLE categorias_unidad_medida(
	codigo char(1) not null,
	nombre varchar(100) not null,
	constraint categorias_unidad_medida_pk primary key(codigo)
);

INSERT INTO categorias_unidad_medida(codigo, nombre)
						VALUES('U', 'Unidades'),
								('V', 'Volumen'),
								('P', 'Peso');
SELECT * FROM categorias_unidad_medida;

DROP TABLE IF EXISTS productos;
CREATE TABLE productos(
	codigo_prod serial not null,
	nombre varchar(100) not null,
	udm varchar(50) not null,
	precio_venta money not null,
	tiene_iva boolean not null,
	coste money not null,
	categoria int not null,
	stock int not null,
	constraint productos_pk primary key(codigo_prod),
	constraint unidades_medida_productos_fk FOREIGN KEY(udm) REFERENCES unidades_medida(nombre),
	constraint categorias_productos_fk FOREIGN KEY(categoria) REFERENCES categorias(codigo_cat)
);

INSERT INTO productos(nombre, udm, precio_venta, tiene_iva, coste, categoria, stock)
				VALUES('Coca_cola pequeña', 'ml', 0.5804, true, 0.3729, 7, 105),
						('Salsa de tomate', 'kg', 0.95, true, 0.8736, 3, 0),
						('Mostaza', 'kg', 0.95, true, 0.89, 3, 0),
						('Fuze tea', 'ml', 0.80, true, 0.70, 7, 49);
SELECT * FROM productos;

DROP TABLE IF EXISTS historial_stock;
CREATE TABLE historial_stock(
	codigo serial not null,
	fecha timestamp,
	referencia varchar(50) not null,
	producto int not null,
	cantidad int not null,
	constraint historial_stock_pk primary key(codigo),
	constraint productos_historial_stock_fk FOREIGN KEY(producto) REFERENCES productos(codigo_prod)
);

INSERT INTO historial_stock(fecha, referencia, producto, cantidad)
					VALUES('20/11/2023 19:59','Pedido 1', 1, 100),
							('20/11/2023 19:59','Pedido 1', 4, 50),
							('20/11/2023 20:00','Pedido 2', 1, 10),
							('20/11/2023 20:00','Venta 1', 1, -5),
							('20/11/2023 20:00','VEnta 1', 4, 1);
SELECT * FROM historial_stock;

DROP TABLE IF EXISTS cabecera_ventas;
CREATE TABLE cabecera_ventas(
	codigo serial not null,
	fecha timestamp not null,
	total_sin_iva money,
	iva money not null,
	total money not null,
	constraint cabecera_ventas_pk primary key(codigo)
);

INSERT INTO cabecera_ventas(fecha, total_sin_iva, iva, total)
					VALUES('20/11/2023 20:00', 3.26, 0.39, 3.65);
SELECT * FROM cabecera_ventas;

DROP TABLE IF EXISTS detalle_ventas;
CREATE TABLE detalle_ventas(
	codigo serial not null,
	cabecera_ventas int not null,
	producto int not null,
	cantidad int not null,
	precio_venta money not null,
	subtotal money not null,
	subtotal_con_iva money not null,
	constraint detalle_ventas_pk primary key(codigo),
	constraint productos_detalle_ventas_fk FOREIGN KEY(producto) REFERENCES productos(codigo_prod),
	constraint cabecera_ventas_detalle_ventas_fk FOREIGN KEY(cabecera_ventas) REFERENCES cabecera_ventas(codigo)
);

INSERT INTO detalle_ventas(cabecera_ventas, producto, cantidad, precio_venta, subtotal, subtotal_con_iva)
					VALUES(1, 1, 5, 0.58, 2.90, 3.25),
							(1, 4, 1, 0.36, 0.36, 0.40);
SELECT * FROM detalle_ventas;

DROP TABLE IF EXISTS tipo_documento;
CREATE TABLE tipo_documento(
	codigo char(1) not null,
	descripcion varchar(50) not null,
	constraint tipo_documento_pk primary key(codigo)
);

INSERT INTO tipo_documento(codigo, descripcion)
					VALUES('C', 'Cedula'),
							('R', 'Ruc');
SELECT * FROM tipo_documento;

DROP TABLE IF EXISTS proveedores;
CREATE TABLE proveedores(
	identificador varchar(20) not null,
	tipo_documento char(1) not null,
	nombre varchar(50) not null,
	telefono varchar(20) not null,
	correo varchar(100),
	direccion varchar(150) not null,
	constraint proveedores_pk primary key(identificador),
	constraint tipo_documento_proveedores_fk FOREIGN KEY(tipo_documento) REFERENCES tipo_documento(codigo)
);

INSERT INTO proveedores(identificador, tipo_documento, nombre, telefono, correo, direccion)
		VALUES('1792285747', 'C', 'Santiago Mosquera', '0992920306', 'santiago@gmail.com', 'Cumbayork'),
				('1792285747001', 'R','Snacks SA', '0992920398', 'snacks@gmail.com', 'La Tola');
SELECT * FROM proveedores;

DROP TABLE IF EXISTS estado_pedidos;
CREATE TABLE estado_pedidos(
	codigo char(1) not null,
	descripcion varchar(50) not null,
	constraint estado_pedidos_pk primary key(codigo)
);

INSERT INTO estado_pedidos(codigo, descripcion)
					VALUES('S', 'Solicitado'),
							('R', 'Recibido');
SELECT * FROM estado_pedidos;

DROP TABLE IF EXISTS cabecera_pedidos;
CREATE TABLE cabecera_pedidos(
	numero serial not null,
	proveedor varchar(20) not null,
	fecha date not null,
	estado char(1) not null,
	constraint cabecera_pedidos_pk primary key(numero),
	constraint proveedores_cabecera_pedidos_fk FOREIGN KEY(proveedor) REFERENCES proveedores(identificador),
	constraint estado_pedidos_cabecera_pedidos_fk FOREIGN KEY(estado) REFERENCES estado_pedidos(codigo)
);

INSERT INTO cabecera_pedidos(proveedor, fecha, estado)
						VALUES('1792285747', '20/11/2023','R'),
								('1792285747', '20/11/2023','R');
SELECT * FROM cabecera_pedidos;

DROP TABLE IF EXISTS detalle_pedidos;
CREATE TABLE detalle_pedidos(
	codigo serial not null,
	cabecera_pedido int not null,
	producto int not null,
	cantidad_solicitada int not null,
	subtotal money not null,
	cantidad_recibida int not null,
	constraint detalle_pedidos_pk primary key(codigo),
	constraint cabecera_pedidos_detalle_pedidos_fk FOREIGN KEY(cabecera_pedido) REFERENCES cabecera_pedidos(numero),
	constraint productos_detalle_pedidos_fk FOREIGN KEY(producto) REFERENCES productos(codigo_prod)
);

INSERT INTO detalle_pedidos(cabecera_pedido, producto, cantidad_solicitada, subtotal, cantidad_recibida)
					VALUES(1, 1, 100, 37.29, 100),
							(1, 4, 50, 11.80, 50),
							(2, 1, 10, 3.73, 10);
SELECT * FROM detalle_pedidos;


