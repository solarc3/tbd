INSERT INTO
	users (username, email, password)
VALUES
	(
		'cliente1',
		'cliente1@example.com',
		'$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6'
	);

INSERT INTO
	farmacia (nombre_farmacia, direccion)
VALUES
	('Farmacia Salud', 'Av. Principal 123');

INSERT INTO
	producto (
		id_producto,
		nombre_producto,
		precio,
		categoria,
		requiere_receta,
		image_url
	)
VALUES
	(
		1,
		'Multivitamínico Bion3 Senior 60 Comp',
		24995,
		'Suplemento',
		false,
		'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dwa1090340/images/products/91076/91076.jpg'
	),
	(
		2,
		'Crema Corporal Nivea Soft Milk 1000ML',
		6859,
		'Dermatológico',
		false,
		'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw818f7b1e/images/products/76366/76366.jpg'
	);

INSERT INTO
	producto_farmacia (id_producto, id_farmacia, stock_producto)
VALUES
	(1, 1, 50),
	(2, 1, 75);

INSERT INTO
	repartidor (nombre_repartidor, fecha_contratacion)
VALUES
	('Juan Pérez', '2022-01-15');

INSERT INTO
	farmacia_repartidor (id_farmacia, id_repartidor)
VALUES
	(1, 1);
