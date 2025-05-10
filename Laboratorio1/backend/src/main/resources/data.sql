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
	),
	(
		3,
		'Agua Micelar Bifásica NIVEA Rose Care 400 ml',
		5298,
		'Dermatológico',
		false,
		'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw8209c984/images/products/88493/88493.jpg'
	),
	(
		4,
		'Crema facial NIVEA Rose Care en Gel 50ml',
		6005,
		'Dermatológico',
		false,
		'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw60443129/images/products/88860/88860.jpg'
	),
	(
		5,
		'Loreal Serum Rellenador Revitalift 1,5% Ácido Hialurónico 30 mL',
		14099,
		'Dermatológico',
		false,
		'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw9e9c9348/images/products/87900001/87900001.jpg'
	),
	(
		6,
		'LOCION MICELAR 3 EN 1 NIVEA PIEL SENSIBLE 400ML',
		3877,
		'Dermatológico',
		false,
		'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dweeebcc33/images/products/83593/83593.jpg'
	),
	(
		7,
		'Serum Loreal Revitalif Retinol 30Ml',
		18199,
		'Dermatológico',
		false,
		'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw02618021/images/products/91374/91374.jpg'
	),
	(
		8,
		'Serum de Ojos Revitalift 2,5% Ácido Hialurónico + Cafeína 20Ml',
		14099,
		'Dermatológico',
		false,
		'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw540d53b1/images/products/91010/91010.jpg'
	),
	(
		9,
		'Travel Kit Centella Skin1004',
		45999,
		'Dermatológico',
		false,
		'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw91e18f04/images/products/94894/94894.jpg'
	),
	(
		10,
		'Crema de Manos Té Verde',
		2119,
		'Dermatológico',
		false,
		'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dwd2f0b970/images/products/92626/92626.jpg'
	),
	(
		11,
		'Garnier Skin Active Mascarilla Express Aclara 28 g',
		2619,
		'Dermatológico',
		false,
		'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dwd17e053a/images/products/88202/88202.jpg'
	),
	(
		12,
		'Serum Nivea Expert Lift 30Ml',
		12361,
		'Dermatológico',
		false,
		'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw48cbacf7/images/products/92573/92573.jpg'
	),
	(
		13,
		'Humectante Care Intensive 3 en 1 100ml',
		8899,
		'Dermatológico',
		false,
		'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw94733ebe/images/products/90941/90941.jpg'
	),
	(
		14,
		'Serum Nivea Q10 Expert 15Ml',
		8833,
		'Dermatológico',
		false,
		'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw4dc4a6bf/images/products/91920/91920.jpg'
	),
	(
		15,
		'CREMA MULTIPROPOSITO NIVEA SOFT CARA MANOS CUERPO 200ML',
		4948,
		'Dermatológico',
		false,
		'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dwb2f20a32/images/products/7300/7300.jpg'
	),
	(
		16,
		'Loreal Crema Revitalift Acido Hialurónico Dia FPS 20 50 mL',
		14099,
		'Dermatológico',
		false,
		'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dwdba41833/images/products/86957001/86957001.jpg'
	),
	(
		17,
		'Nivea Crema Hyaluron Cellular Filler Día 50 mL',
		11654,
		'Dermatológico',
		false,
		'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw10ae4b39/images/products/87399/87399.jpg'
	),
	(
		18,
		'Crema Nivea Face Energy Noche 50ml',
		9470,
		'Dermatológico',
		false,
		'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw6d902e07/images/products/91461/91461.jpg'
	),
	(
		19,
		'Cicatricure Crema 60 G',
		17999,
		'Dermatológico',
		false,
		'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dwd7276205/images/products/70077/70077.jpg'
	),
	(
		20,
		'Loreal Crema Hidra Total 5 Colageno 35 50 mL',
		6299,
		'Dermatológico',
		false,
		'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw0ccd5ed8/images/products/82003/82003.jpg'
	),
    (
     21,
     'Clonazepam 2 mg Caja 30 Comp.',
     5999,
     'Medicamentos',
     true,
     'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dwdf3a7e8e/images/products/69943/69943.jpg'
    );

INSERT INTO
	producto_farmacia (id_producto, id_farmacia, stock_producto)
VALUES
	(1, 1, 50),
	(2, 1, 75),
	(3, 1, 60),
	(4, 1, 55),
	(5, 1, 40),
	(6, 1, 70),
	(7, 1, 35),
	(8, 1, 45),
	(9, 1, 25),
	(10, 1, 80),
	(11, 1, 65),
	(12, 1, 30),
	(13, 1, 50),
	(14, 1, 40),
	(15, 1, 60),
	(16, 1, 35),
	(17, 1, 45),
	(18, 1, 55),
	(19, 1, 30),
	(20, 1, 65);

INSERT INTO
	repartidor (nombre_repartidor, fecha_contratacion)
VALUES
	('Juan Pérez', '2022-01-15');

INSERT INTO
	farmacia_repartidor (id_farmacia, id_repartidor)
VALUES
	(1, 1);

INSERT INTO
    pedido (monto, fecha_pedido, es_urgente, estado_pedido, id_cliente, id_farmacia)
VALUES
    (18309, '2025-05-10', false, 'CONFIRMADO', 1, 1);

INSERT INTO
    producto_pedido (id_pedido, id_producto, cantidad, receta_validada)
VALUES
    (1, 4, 2, NULL),
    (1, 20, 1, NULL),
    (1, 21, 1, FALSE);



