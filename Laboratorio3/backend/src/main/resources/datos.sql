INSERT INTO users (username, email, password, first_name, last_name, rut, location) VALUES
('cliente1', 'cliente1@example.com', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6', 'Juan', 'González', '12.345.678-9', ST_SetSRID(ST_MakePoint(-70.6215, -33.4785), 4326)),
('cliente2', 'cliente2@example.com', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6', 'María', 'Rodríguez', '11.987.654-3', ST_SetSRID(ST_MakePoint(-70.6370, -33.5187), 4326)),
('cliente3', 'cliente3@example.com', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6', 'Carlos', 'Pérez', '15.678.901-2', ST_SetSRID(ST_MakePoint(-70.6345, -33.5330), 4326)),
('cliente4', 'cliente4@example.com', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6', 'Ana', 'López', '17.432.765-K', ST_SetSRID(ST_MakePoint(-70.6540, -33.4050), 4326)),
('cliente5', 'cliente5@example.com', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6', 'Diego', 'Martínez', '14.765.238-5', ST_SetSRID(ST_MakePoint(-70.6520, -33.5300), 4326)),
('cliente6', 'cliente6@example.com', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6', 'Valentina', 'Hernández', '16.543.219-8', ST_SetSRID(ST_MakePoint(-70.5420, -33.4900), 4326)),
('cliente7', 'cliente7@example.com', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6', 'Sebastián', 'Torres', '13.987.654-1', ST_SetSRID(ST_MakePoint(-70.6140, -33.4370), 4326)),
('cliente8', 'cliente8@example.com', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6', 'Camila', 'Sánchez', '18.234.567-7', ST_SetSRID(ST_MakePoint(-70.5450, -33.4500), 4326)),
('cliente9', 'cliente9@example.com', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6', 'Matías', 'Fernández', '19.876.543-4', ST_SetSRID(ST_MakePoint(-70.7650, -33.5750), 4326)),
('cliente10', 'cliente10@example.com', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6', 'Javiera', 'Muñoz', '10.543.876-6', ST_SetSRID(ST_MakePoint(-70.6800, -33.2000), 4326));

INSERT INTO farmacia (nombre_farmacia, direccion, ubicacion) VALUES
('Farmacia Salud', 'Gral. Körner 824', ST_SetSRID(ST_MakePoint(-70.68211, -33.54934), 4326)),
('Farmacia Cruz Verde', 'Las Bellotas 182, Local 2', ST_SetSRID(ST_MakePoint(-70.60817, -33.42212), 4326)),
('Farmacia Ahumada', 'Av. Los Leones 1160', ST_SetSRID(ST_MakePoint(-70.60227, -33.43013), 4326)),
('Farmacia Dr. Simi', 'Av. Providencia 2327', ST_SetSRID(ST_MakePoint(-70.60751, -33.42064), 4326)),
('Farmacia Bienestar', 'Gran Av. José Miguel Carrera 8766', ST_SetSRID(ST_MakePoint(-70.66394, -33.53659), 4326)),
('Farmacia SanaSana', 'Av. Domingo Sta. María 3465', ST_SetSRID(ST_MakePoint(-70.69291, -33.40899), 4326));

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
		'Suplementos',
		false,
		'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dwa1090340/images/products/91076/91076.jpg'
	),
	(
		2,
		'Crema Corporal Nivea Soft Milk 1000ML',
		6859,
		'Dermatologia',
		false,
		'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw818f7b1e/images/products/76366/76366.jpg'
	),
	(
		3,
		'Agua Micelar Bifásica NIVEA Rose Care 400 ml',
		5298,
		'Dermatologia',
		false,
		'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw8209c984/images/products/88493/88493.jpg'
	),
	(
		4,
		'Crema facial NIVEA Rose Care en Gel 50ml',
		6005,
		'Dermatologia',
		false,
		'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw60443129/images/products/88860/88860.jpg'
	),
	(
		5,
		'Loreal Serum Rellenador Revitalift 1,5% Ácido Hialurónico 30 mL',
		14099,
		'Dermatologia',
		false,
		'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw9e9c9348/images/products/87900001/87900001.jpg'
	),
	(
		6,
		'LOCION MICELAR 3 EN 1 NIVEA PIEL SENSIBLE 400ML',
		3877,
		'Dermatologia',
		false,
		'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dweeebcc33/images/products/83593/83593.jpg'
	),
	(
		7,
		'Serum Loreal Revitalif Retinol 30Ml',
		18199,
		'Dermatologia',
		false,
		'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw02618021/images/products/91374/91374.jpg'
	),
	(
		8,
		'Serum de Ojos Revitalift 2,5% Ácido Hialurónico + Cafeína 20Ml',
		14099,
		'Dermatologia',
		false,
		'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw540d53b1/images/products/91010/91010.jpg'
	),
	(
		9,
		'Travel Kit Centella Skin1004',
		45999,
		'Dermatologia',
		false,
		'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw91e18f04/images/products/94894/94894.jpg'
	),
	(
		10,
		'Crema de Manos Té Verde',
		2119,
		'Dermatologia',
		false,
		'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dwd2f0b970/images/products/92626/92626.jpg'
	),
	(
		11,
		'Garnier Skin Active Mascarilla Express Aclara 28 g',
		2619,
		'Dermatologia',
		false,
		'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dwd17e053a/images/products/88202/88202.jpg'
	),
	(
		12,
		'Serum Nivea Expert Lift 30Ml',
		12361,
		'Dermatologia',
		false,
		'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw48cbacf7/images/products/92573/92573.jpg'
	),
	(
		13,
		'Humectante Care Intensive 3 en 1 100ml',
		8899,
		'Dermatologia',
		false,
		'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw94733ebe/images/products/90941/90941.jpg'
	),
	(
		14,
		'Serum Nivea Q10 Expert 15Ml',
		8833,
		'Dermatologia',
		false,
		'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw4dc4a6bf/images/products/91920/91920.jpg'
	),
	(
		15,
		'CREMA MULTIPROPOSITO NIVEA SOFT CARA MANOS CUERPO 200ML',
		4948,
		'Dermatologia',
		false,
		'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dwb2f20a32/images/products/7300/7300.jpg'
	),
	(
		16,
		'Loreal Crema Revitalift Acido Hialurónico Dia FPS 20 50 mL',
		14099,
		'Dermatologia',
		false,
		'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dwdba41833/images/products/86957001/86957001.jpg'
	),
	(
		17,
		'Nivea Crema Hyaluron Cellular Filler Día 50 mL',
		11654,
		'Dermatologia',
		false,
		'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw10ae4b39/images/products/87399/87399.jpg'
	),
	(
		18,
		'Crema Nivea Face Energy Noche 50ml',
		9470,
		'Dermatologia',
		false,
		'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw6d902e07/images/products/91461/91461.jpg'
	),
	(
		19,
		'Cicatricure Crema 60 G',
		17999,
		'Dermatologia',
		false,
		'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dwd7276205/images/products/70077/70077.jpg'
	),
	(
		20,
		'Loreal Crema Hidra Total 5 Colageno 35 50 mL',
		6299,
		'Dermatologia',
		false,
		'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw0ccd5ed8/images/products/82003/82003.jpg'
	),
    (
     21,
     'Clonazepam 2 mg Caja 30 Comp.',
     5999,
     'Medicamentoss',
     true,
     'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dwdf3a7e8e/images/products/69943/69943.jpg'
    ),
    (
        22,
        'Máquina de Afeitar Gillete Prestrobarba Ultra Grip Bolsa 5 un',
        7899,
        'Higiene',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw48a0f65e/images/products/50702/50702.jpg'
    ),
    (
        23,
        'Espuma de Afeitar Nivea For Men Deep 200 mL',
        6439,
        'Higiene',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw400bae5f/images/products/88112/88112.jpg'
    ),
    (
        24,
        'Toallas Higiénicas Kotex Ultrafina Suave Con Alas 16 un',
        2590,
        'Higiene',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw1036c2e2/images/products/48810/48810.jpg'
    ),
    (
        25,
        'Jabón Líquido Dove Mandarina Recarga 700 mL',
        3690,
        'Higiene',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dwa5c5bb2c/images/products/87273/87273.jpg'
    ),
    (
        26,
        'Máquina de Afeitar Gillette Venus Sensitive Suave 2 un',
        3899,
        'Higiene',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw911eea88/images/products/87366/87366.jpg'
    ),
    (
        27,
        'Cepillo de Dientes Oral B Advanced Suave 2 un',
        6049,
        'Higiene',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw198c1393/images/products/67824/67824.jpg'
    ),
    (
        28,
        'Enjuague Bucal Vitis Orthodontic 500 mL',
        8899,
        'Higiene',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dwd275acee/images/products/63068/63068.jpg'
    ),
    (
        29,
        'PACK ELITE PAÑUELO NORM.AROMA FRUT.6U',
        2499,
        'Higiene',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw12bbb2bb/images/products/63126/63126.jpg'
    ),
    (
        30,
        'Repuesto Máquina de Afeitar Venus Breeze 3 Hojas 4 un',
        12999,
        'Higiene',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dwf5327e0f/images/products/93753/93753.jpg'
    ),
    (
        31,
        'Desosdorante Rexona Clinical Women Classic 48 g',
        7049,
        'Higiene',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dwa2c0a42b/images/products/64467/64467.jpg'
    ),
    (
        32,
        'Desosdorante Rexona Clinical Women Barra Extra Dry 48 g',
        6899,
        'Higiene',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw4bd6500a/images/products/76162001/76162001.jpg'
    ),
    (
        33,
        'Pasta Dental Colgate Tiple Acción 75 mL 3 un',
        2990,
        'Higiene',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dwbfb82ea0/images/products/62918/62918.jpg'
    ),
    (
        34,
        'Repuesto Cabezal Cepillo de Dientes Eléctrico Oral B Sensitive Clean 2 un',
        14069,
        'Higiene',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw75c18e7a/images/products/87249/87249.jpg'
    ),
    (
        35,
        'Pasta Dental Vitis Cpc Protect 100ml',
        5399,
        'Higiene',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw9954ad4a/images/products/89587/89587.jpg'
    ),
    (
        36,
        'Máquina de Afeitar Schick Intuition Mujer 2 un',
        7799,
        'Higiene',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw4bb0c8a9/images/products/83130/83130.jpg'
    ),
    (
        37,
        'Cepillo Interdental Interprox Plus Nano 6 un',
        6749,
        'Higiene',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw95e5fab0/images/products/68474/68474.jpg'
    ),
    (
        38,
        'TOA HIG NOSOTRAS BUENAS NOCHES ULTRA 16U',
        3929,
        'Higiene',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dwbcdb21c9/images/products/88134/88134.jpg'
    ),
    (
        39,
        'TALCO DESODORANTE PARA PIES HANSAPLAST 300 GR',
        6499,
        'Higiene',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw2a1eb20d/images/products/40783/40783.jpg'
    ),
    (
        40,
        'Gel de Ducha Nivea Waterlilly & Oil 240 mL',
        3303,
        'Higiene',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw134ba184/images/products/86180/86180.jpg'
    ),

    (
        41,
        'BÁLSAMO LABIAL NIVEA BLACKBERRY SHINE 4,8GR',
        3534,
        'Cosméticos',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw15f15282/images/products/88514/88514.jpg'
    ),
    (
        42,
        'Super Lock Glue',
        7069,
        'Cosméticos',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dwff8bb0db/images/products/94131/94131.jpg'
    ),
    (
        43,
        'Labial Maybelline Super Stay Teddy Tint 20 Mascara Tear',
        9599,
        'Cosméticos',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dwc7573133/images/products/94548/94548.jpg'
    ),
    (
        44,
        'S-NAILS ISDIN UÑAS 2ML',
        20799,
        'Cosméticos',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw09b9c174/images/products/88389/88389.jpg'
    ),
    (
        45,
        'Máscara De Pestañas Lash Sensational Sky High A Prueba De Agua',
        13509,
        'Cosméticos',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw7d503cc8/images/products/90671/90671.jpg'
    ),
    (
        46,
        'Labial Maybelline Super Stay Teddy Tint 50 Wild At Heart',
        9599,
        'Cosméticos',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw0e5ecdee/images/products/94534/94534.jpg'
    ),
    (
        47,
        'Máscara de Pestañas Falsies Surreal Extensions Overload -WTP',
        13509,
        'Cosméticos',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dwf1c7d4cd/images/products/92440/92440.jpg'
    ),
    (
        48,
        'Sombra Maybelline Rivals Extra X Lowkey',
        4189,
        'Cosméticos',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw0ccf78a3/images/products/93059/93059.jpg'
    ),
    (
        49,
        'Trim Encrespador De Pestañas Easy Hold x 1 Unidad',
        6599,
        'Cosméticos',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw981cc27c/images/products/76368/76368.jpg'
    ),
    (
        50,
        'Brillo Labial Lip Lifter Gloss Moon',
        7299,
        'Cosméticos',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw95055cda/images/products/91832/91832.jpg'
    ),
    (
        51,
        'Sunkisser Glow Blush - Blazing Blush',
        7269,
        'Cosméticos',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dwef56cbe0/images/products/94085/94085.jpg'
    ),
    (
        52,
        'Labial Vinyl Ink SULTRY',
        10389,
        'Cosméticos',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw1f3b647e/images/products/92743/92743.jpg'
    ),
    (
        53,
        'Polvo Infallible Loreal Sand 9Gr',
        9999,
        'Cosméticos',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw037d7f46/images/products/89619/89619.jpg'
    ),
    (
        54,
        'Super stay hybrid compacto 24h 110',
        8499,
        'Cosméticos',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dwfafde22e/images/products/94114/94114.jpg'
    ),
    (
        55,
        'Ecopads Garnier Discos Desmaquillantes Reutilizables 3 Un',
        8199,
        'Cosméticos',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw82d117ef/images/products/90937/90937.jpg'
    ),
    (
        56,
        'Alicate de Pieles Manicura Beter',
        8999,
        'Cosméticos',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw709ba910/images/products/94542/94542.jpg'
    ),
    (
        57,
        'Rizador Pestañas Beter Automatico',
        5299,
        'Cosméticos',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw8ca2fe96/images/products/94579/94579.jpg'
    ),
    (
        58,
        'Bálsamo Labial Nivea Edición Limitada Pride Kiss 4,8 g',
        5299,
        'Cosméticos',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw8e36ae4f/images/products/94968/94968.jpg'
    ),
    (
        59,
        'Pack Ojos Maybelline Mirada Sky',
        18709,
        'Cosméticos',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw18588c51/images/products/93018/93018.jpg'
    ),
    (
        60,
        'BÁLSAMO LABIAL NIVEA CHERRY SHINE 4,8GR',
        3534,
        'Cosméticos',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw740598ca/images/products/71477/71477.jpg'
    ),
    (
        61,
        'Tapsin Compuesto Noche 5 g x 1 Sobre Polvo Para Solución Oral',
        693,
        'Medicamentos',
        true,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw0afc6c9c/images/products/15226/15226.jpg'
    ),
    (
        62,
        'Kitadol 500 mg x 24 Comprimidos',
        995,
        'Medicamentos',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw850274b1/images/products/15414/15414.jpg'
    ),
    (
        63,
        'Losartan 50 mg x 30 Comprimidos Recubiertos CHILE',
        2799,
        'Medicamentos',
        true,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dwf9766b70/images/products/86132/86132.jpg'
    ),
    (
        64,
        'Natuvit Caramelo Propoleo x 10 Comprimidos',
        1295,
        'Medicamentos',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw2761754a/images/products/49014/49014.jpg'
    ),
    (
        65,
        'Omeprazol 20 mg x 60 Cápsulas CHILE',
        3999,
        'Medicamentos',
        true,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dwe22f7f24/images/products/84417/84417.jpg'
    ),
    (
        66,
        'Femelle x 28 Comprimidos Recubiertos',
        15339,
        'Medicamentos',
        true,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dwbd9607ef/images/products/41554/41554.jpg'
    ),
    (
        67,
        'Ibuprofeno 200 mg x 20 Comprimidos Recubiertos',
        1999,
        'Medicamentos',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw063fa220/images/products/86357/86357.jpg'
    ),
    (
        68,
        'Xumadol 1 g Sobres x 20 Sobres Con Granulado Efervescente',
        10922,
        'Medicamentos',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw41d1f00e/images/products/44884/44884.jpg'
    ),
    (
        69,
        'Gaviscon Comprimidos Masticables Doble Acción x8',
        4759,
        'Medicamentos',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dwaf8aaf98/images/products/72036/72036.jpg'
    ),
    (
        70,
        'Ciruelax Forte 125 mg x 60 Comprimidos Recubiertos',
        9264,
        'Medicamentos',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw6099a442/images/products/79941/79941.jpg'
    ),
    (
        71,
        'Panadol Niños 100mg/Ml Gotas 15ml',
        3495,
        'Medicamentos',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw19b1c268/images/products/90333/90333.jpg'
    ),
    (
        72,
        'Neuroval Cd 10 mg Caja 30 Comp. Dispersables',
        13194,
        'Medicamentos',
        true,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw13ccae30/images/products/78701/78701.jpg'
    ),
    (
        73,
        'Cardioaspirina EC 100 mg x 50 Comprimidos Con Recubrimiento Entérico',
        11432,
        'Medicamentos',
        true,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dwa1f85b49/images/products/1302/1302.jpg'
    ),
    (
        74,
        'Terbinafina 250 mg x 30 Comprimidos',
        8699,
        'Medicamentos',
        true,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dwdc98599a/images/products/92811/92811.jpg'
    ),
    (
        75,
        'Nogesta 75 mcg x 28 Comprimidos Recubiertos',
        11179,
        'Medicamentos',
        true,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw78ce1f46/images/products/44024/44024.jpg'
    ),
    (
        76,
        'MUXOL JARABE ADULTO Ambroxol Clorhidrato 600 mg 100 ml',
        3995,
        'Medicamentos',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dwc884d75b/images/products/9257/9257.jpg'
    ),
    (
        77,
        'Abrilar 35 mg/5 mL x 100 mL Jarabe',
        6017,
        'Medicamentos',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw569c027d/images/products/26896/26896.jpg'
    ),
    (
        78,
        'Tapsin 100 mg/mL x 15 mL Solución Oral Para Gotas',
        3654,
        'Medicamentos',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw8552a39f/images/products/19274/19274.jpg'
    ),
    (
        79,
        'Pilocarpina Clorhidrato 4 % x 10 ml Solución Oftálmica SAVAL S.A.',
        14199,
        'Medicamentos',
        true,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw977e3535/images/products/8412/8412.jpg'
    ),
    (
        80,
        'Engystol x 50 Comprimidos',
        20314,
        'Medicamentos',
        true,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dwe74a2eed/images/products/49937/49937.jpg'
    );

INSERT INTO pedido (monto, fecha_pedido, es_urgente, estado_pedido, id_cliente, id_farmacia, ruta_estimada)
VALUES
    (0, '2025-05-09 14:30:00', false, 'POR_CONFIRMAR', 1, 1,
     ST_MakeLine(
             (SELECT ubicacion FROM farmacia WHERE id_farmacia = 1),
             (SELECT location FROM users WHERE id = 1)
     )),
    (15600, '2025-05-09 14:30:00', false, 'POR_CONFIRMAR', 3, 2,
     ST_MakeLine(
             (SELECT ubicacion FROM farmacia WHERE id_farmacia = 2),
             (SELECT location FROM users WHERE id = 3)
     )),
    (9800, '2025-05-08 10:15:00', false, 'CONFIRMADO', 5, 3,
     ST_MakeLine(
             (SELECT ubicacion FROM farmacia WHERE id_farmacia = 3),
             (SELECT location FROM users WHERE id = 5)
     )),
    (23500, '2025-05-07 16:45:00', true, 'ENTREGADO', 2, 1,
     ST_MakeLine(
             (SELECT ubicacion FROM farmacia WHERE id_farmacia = 1),
             (SELECT location FROM users WHERE id = 2)
     )),
    (7900, '2025-05-07 09:20:00', false, 'CANCELADO', 4, 6,
     ST_MakeLine(
             (SELECT ubicacion FROM farmacia WHERE id_farmacia = 6),
             (SELECT location FROM users WHERE id = 4)
     )),
    (31200, '2025-05-06 13:10:00', true, 'ENTREGADO', 1, 4,
     ST_MakeLine(
             (SELECT ubicacion FROM farmacia WHERE id_farmacia = 4),
             (SELECT location FROM users WHERE id = 1)
     )),
    (12400, '2025-05-06 11:25:00', false, 'CONFIRMADO', 7, 5,
     ST_MakeLine(
             (SELECT ubicacion FROM farmacia WHERE id_farmacia = 5),
             (SELECT location FROM users WHERE id = 7)
     )),
    (18900, '2025-05-05 15:50:00', false, 'ENTREGADO', 6, 2,
     ST_MakeLine(
             (SELECT ubicacion FROM farmacia WHERE id_farmacia = 2),
             (SELECT location FROM users WHERE id = 6)
     )),
    (27500, '2025-05-04 08:40:00', true, 'ENTREGADO', 3, 1,
     ST_MakeLine(
             (SELECT ubicacion FROM farmacia WHERE id_farmacia = 1),
             (SELECT location FROM users WHERE id = 3)
     )),
    (14300, '2025-05-03 17:15:00', false, 'CANCELADO', 9, 3,
     ST_MakeLine(
             (SELECT ubicacion FROM farmacia WHERE id_farmacia = 3),
             (SELECT location FROM users WHERE id = 9)
     )),
    (19800, '2025-05-02 12:30:00', false, 'ENTREGADO', 8, 4,
     ST_MakeLine(
             (SELECT ubicacion FROM farmacia WHERE id_farmacia = 4),
             (SELECT location FROM users WHERE id = 8)
     )),
    (8700, '2025-05-01 14:45:00', false, 'ENTREGADO', 10, 6,
     ST_MakeLine(
             (SELECT ubicacion FROM farmacia WHERE id_farmacia = 6),
             (SELECT location FROM users WHERE id = 10)
     )),
    (22100, '2025-04-30 11:20:00', true, 'CONFIRMADO', 2, 5,
     ST_MakeLine(
             (SELECT ubicacion FROM farmacia WHERE id_farmacia = 5),
             (SELECT location FROM users WHERE id = 2)
     )),
    (16700, '2025-04-29 09:35:00', false, 'ENTREGADO', 4, 2,
     ST_MakeLine(
             (SELECT ubicacion FROM farmacia WHERE id_farmacia = 2),
             (SELECT location FROM users WHERE id = 4)
     )),
    (29400, '2025-04-28 16:10:00', true, 'ENTREGADO', 7, 3,
     ST_MakeLine(
             (SELECT ubicacion FROM farmacia WHERE id_farmacia = 3),
             (SELECT location FROM users WHERE id = 7)
     )),
    (11200, '2025-04-27 13:50:00', false, 'CANCELADO', 1, 1,
     ST_MakeLine(
             (SELECT ubicacion FROM farmacia WHERE id_farmacia = 1),
             (SELECT location FROM users WHERE id = 1)
     )),
    (21800, '2025-04-26 10:25:00', false, 'ENTREGADO', 5, 6,
     ST_MakeLine(
             (SELECT ubicacion FROM farmacia WHERE id_farmacia = 6),
             (SELECT location FROM users WHERE id = 5)
     )),
    (13500, '2025-04-25 15:40:00', false, 'ENTREGADO', 9, 4,
     ST_MakeLine(
             (SELECT ubicacion FROM farmacia WHERE id_farmacia = 4),
             (SELECT location FROM users WHERE id = 9)
     )),
    (26300, '2025-04-24 08:15:00', true, 'CONFIRMADO', 6, 5,
     ST_MakeLine(
             (SELECT ubicacion FROM farmacia WHERE id_farmacia = 5),
             (SELECT location FROM users WHERE id = 6)
     )),
    (17900, '2025-04-23 14:55:00', false, 'ENTREGADO', 8, 2,
     ST_MakeLine(
             (SELECT ubicacion FROM farmacia WHERE id_farmacia = 2),
             (SELECT location FROM users WHERE id = 8)
     ));

INSERT INTO producto_farmacia (id_producto, id_farmacia, stock_producto)
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
    (20, 1, 65),
    (21, 1, 60),
    (22, 1, 45),
    (23, 1, 55),
    (24, 1, 80),
    (25, 1, 70),
    (26, 1, 40),
    (27, 1, 65),
    (28, 1, 30),
    (29, 1, 90),
    (30, 1, 35),
    (31, 1, 50),
    (32, 1, 45),
    (33, 1, 75),
    (34, 1, 30),
    (35, 1, 40),
    (36, 1, 35),
    (37, 1, 60),
    (38, 1, 70),
    (39, 1, 45),
    (40, 1, 55),
    (41, 1, 40),
    (42, 1, 30),
    (43, 1, 35),
    (44, 1, 25),
    (45, 1, 40),
    (46, 1, 35),
    (47, 1, 30),
    (48, 1, 45),
    (49, 1, 50),
    (50, 1, 35),
    (51, 1, 40),
    (52, 1, 30),
    (53, 1, 25),
    (54, 1, 30),
    (55, 1, 40),
    (56, 1, 35),
    (57, 1, 45),
    (58, 1, 40),
    (59, 1, 30),
    (60, 1, 50),
    (61, 1, 100),
    (62, 1, 150),
    (63, 1, 80),
    (64, 1, 90),
    (65, 1, 70),
    (66, 1, 60),
    (67, 1, 120),
    (68, 1, 90),
    (69, 1, 80),
    (70, 1, 60),
    (71, 1, 75),
    (72, 1, 50),
    (73, 1, 70),
    (74, 1, 55),
    (75, 1, 60),
    (76, 1, 80),
    (77, 1, 65),
    (78, 1, 90),
    (79, 1, 40),
    (80, 1, 35);

-- Additional producto_farmacia data for Farmacia Cruz Verde (id=2)
INSERT INTO producto_farmacia (id_producto, id_farmacia, stock_producto)
VALUES
    (1, 2, 35),
    (2, 2, 60),
    (3, 2, 45),
    (4, 2, 70),
    (5, 2, 55),
    (6, 2, 80),
    (8, 2, 30),
    (10, 2, 65),
    (12, 2, 40),
    (14, 2, 25),
    (16, 2, 50),
    (18, 2, 75),
    (20, 2, 45),
    (22, 2, 60),
    (24, 2, 70),
    (26, 2, 55),
    (28, 2, 40),
    (30, 2, 25),
    (32, 2, 65),
    (34, 2, 50),
    (36, 2, 30),
    (38, 2, 75),
    (40, 2, 40),
    (42, 2, 55),
    (44, 2, 30),
    (46, 2, 45),
    (48, 2, 60),
    (50, 2, 25),
    (52, 2, 40),
    (54, 2, 35),
    (56, 2, 50),
    (58, 2, 45),
    (60, 2, 60),
    (62, 2, 120),
    (64, 2, 75),
    (66, 2, 45),
    (68, 2, 80),
    (70, 2, 50),
    (72, 2, 35),
    (74, 2, 40),
    (76, 2, 65),
    (78, 2, 70),
    (80, 2, 30);

-- Additional producto_farmacia data for Farmacia Ahumada (id=3)
INSERT INTO producto_farmacia (id_producto, id_farmacia, stock_producto)
VALUES
    (1, 3, 45),
    (3, 3, 55),
    (5, 3, 35),
    (7, 3, 40),
    (9, 3, 30),
    (11, 3, 50),
    (13, 3, 60),
    (15, 3, 45),
    (17, 3, 55),
    (19, 3, 40),
    (21, 3, 50),
    (23, 3, 65),
    (25, 3, 55),
    (27, 3, 70),
    (29, 3, 85),
    (31, 3, 45),
    (33, 3, 80),
    (35, 3, 35),
    (37, 3, 55),
    (39, 3, 40),
    (41, 3, 35),
    (43, 3, 40),
    (45, 3, 45),
    (47, 3, 35),
    (49, 3, 55),
    (51, 3, 45),
    (53, 3, 30),
    (55, 3, 50),
    (57, 3, 40),
    (59, 3, 25),
    (61, 3, 85),
    (63, 3, 70),
    (65, 3, 60),
    (67, 3, 110),
    (69, 3, 75),
    (71, 3, 65),
    (73, 3, 55),
    (75, 3, 45),
    (77, 3, 60),
    (79, 3, 35);

-- Additional producto_farmacia data for Farmacia Dr. Simi (id=4)
INSERT INTO producto_farmacia (id_producto, id_farmacia, stock_producto)
VALUES
    (2, 4, 50),
    (4, 4, 65),
    (6, 4, 55),
    (8, 4, 40),
    (10, 4, 70),
    (12, 4, 35),
    (14, 4, 45),
    (16, 4, 30),
    (18, 4, 50),
    (20, 4, 60),
    (22, 4, 40),
    (24, 4, 75),
    (26, 4, 35),
    (28, 4, 25),
    (30, 4, 40),
    (32, 4, 50),
    (34, 4, 35),
    (36, 4, 45),
    (38, 4, 60),
    (40, 4, 45),
    (42, 4, 35),
    (44, 4, 20),
    (46, 4, 40),
    (48, 4, 50),
    (50, 4, 30),
    (52, 4, 35),
    (54, 4, 25),
    (56, 4, 40),
    (58, 4, 35),
    (60, 4, 45),
    (62, 4, 130),
    (64, 4, 85),
    (66, 4, 40),
    (68, 4, 75),
    (70, 4, 55),
    (72, 4, 30),
    (74, 4, 45),
    (76, 4, 70),
    (78, 4, 80),
    (80, 4, 25);

-- Additional producto_farmacia data for Farmacia Bienestar (id=5)
INSERT INTO producto_farmacia (id_producto, id_farmacia, stock_producto)
VALUES
    (1, 5, 40),
    (2, 5, 55),
    (5, 5, 30),
    (7, 5, 45),
    (9, 5, 35),
    (11, 5, 55),
    (13, 5, 40),
    (15, 5, 50),
    (17, 5, 35),
    (19, 5, 25),
    (21, 5, 55),
    (23, 5, 50),
    (25, 5, 65),
    (27, 5, 60),
    (29, 5, 75),
    (31, 5, 40),
    (33, 5, 65),
    (35, 5, 35),
    (37, 5, 50),
    (39, 5, 40),
    (41, 5, 30),
    (43, 5, 45),
    (45, 5, 35),
    (47, 5, 25),
    (49, 5, 45),
    (51, 5, 40),
    (53, 5, 30),
    (55, 5, 45),
    (61, 5, 90),
    (62, 5, 115),
    (63, 5, 65),
    (65, 5, 50),
    (67, 5, 105),
    (69, 5, 65),
    (71, 5, 70),
    (73, 5, 60),
    (75, 5, 50),
    (77, 5, 55),
    (79, 5, 30);

-- Additional producto_farmacia data for Farmacia SanaSana (id=6)
INSERT INTO producto_farmacia (id_producto, id_farmacia, stock_producto)
VALUES
    (2, 6, 45),
    (4, 6, 60),
    (6, 6, 50),
    (8, 6, 35),
    (10, 6, 65),
    (12, 6, 30),
    (14, 6, 40),
    (16, 6, 25),
    (18, 6, 45),
    (20, 6, 55),
    (22, 6, 35),
    (24, 6, 70),
    (26, 6, 40),
    (28, 6, 30),
    (30, 6, 45),
    (32, 6, 55),
    (34, 6, 30),
    (36, 6, 40),
    (38, 6, 55),
    (40, 6, 40),
    (42, 6, 30),
    (44, 6, 25),
    (46, 6, 35),
    (48, 6, 45),
    (50, 6, 35),
    (52, 6, 30),
    (54, 6, 20),
    (56, 6, 35),
    (58, 6, 40),
    (60, 6, 50),
    (61, 6, 75),
    (62, 6, 100),
    (64, 6, 70),
    (66, 6, 35),
    (68, 6, 65),
    (70, 6, 45),
    (72, 6, 25),
    (74, 6, 40),
    (76, 6, 60),
    (78, 6, 75);

INSERT INTO repartidor (nombre_repartidor, fecha_contratacion, ubicacion) VALUES
('María González', '2022-03-10', ST_SetSRID(ST_MakePoint(-70.6500, -33.4500), 4326)),       -- Santiago Centro
('Carlos Rodríguez', '2022-02-05', ST_SetSRID(ST_MakePoint(-70.6000, -33.4200), 4326)),     -- Providencia
('Ana Martínez', '2022-06-15', ST_SetSRID(ST_MakePoint(-70.7000, -33.4800), 4326)),         -- Quinta Normal
('Diego Sánchez', '2022-04-22', ST_SetSRID(ST_MakePoint(-70.5500, -33.5000), 4326)),        -- La Florida
('Valeria Muñoz', '2022-05-18', ST_SetSRID(ST_MakePoint(-70.7500, -33.5300), 4326)),        -- Maipú
('Roberto Fernández', '2022-07-01', ST_SetSRID(ST_MakePoint(-70.6200, -33.4600), 4326)),    -- Ñuñoa
('Camila Torres', '2022-03-28', ST_SetSRID(ST_MakePoint(-70.5800, -33.5800), 4326)),        -- Puente Alto
('José Hernández', '2022-02-12', ST_SetSRID(ST_MakePoint(-70.7200, -33.3800), 4326)),       -- Las Condes
('Lucía Flores', '2022-08-05', ST_SetSRID(ST_MakePoint(-70.6700, -33.5200), 4326)),         -- San Miguel
('Andrés López', '2022-09-11', ST_SetSRID(ST_MakePoint(-70.7800, -33.4300), 4326));         -- Cerrillos

INSERT INTO farmacia_repartidor (id_farmacia, id_repartidor) VALUES
-- Farmacia Salud (id=1)
(1, 2),
(1, 3),
(1, 4),
-- Farmacia Cruz Verde (id=2)
(2, 5),
(2, 6),
(2, 7),
-- Farmacia Ahumada (id=3)
(3, 8),
(3, 9),
-- Farmacia Dr. Simi (id=4)
(4, 10),
(4, 1),
-- Farmacia Bienestar (id=5)
(5, 2),
(5, 3),
-- Farmacia SanaSana (id=6)
(6, 4),
(6, 5);

INSERT INTO detalle_pedido (id_pedido, id_repartidor, metodo_pago, fecha_entrega)
VALUES
    (2, 5, 'Tarjeta de Crédito', NULL),
    (3, 9, 'Efectivo', NULL),
    (4, 3, 'Tarjeta de Débito', '2025-05-07 19:20:00'),
    (5, 4, 'Transferencia', NULL),
    (6, 10, 'Tarjeta de Crédito', '2025-05-06 15:45:00'),
    (7, 2, 'Efectivo', NULL),
    (8, 7, 'Tarjeta de Débito', '2025-05-05 18:30:00'),
    (9, 3, 'Tarjeta de Crédito', '2025-05-04 11:15:00'),
    (10, 8, 'Efectivo', NULL),
    (11, 1, 'Transferencia', '2025-05-02 15:10:00'),
    (12, 5, 'Tarjeta de Crédito', '2025-05-01 17:30:00'),
    (13, 3, 'Tarjeta de Débito', NULL),
    (14, 6, 'Efectivo', '2025-04-29 12:15:00'),
    (15, 9, 'Tarjeta de Crédito', '2025-04-28 18:45:00'),
    (16, 2, 'Transferencia', NULL),
    (17, 4, 'Tarjeta de Débito', '2025-04-26 13:00:00'),
    (18, 10, 'Efectivo', '2025-04-25 18:15:00'),
    (19, 3, 'Tarjeta de Crédito', NULL),
    (20, 7, 'Transferencia', '2025-04-23 17:30:00');

INSERT INTO producto_pedido (id_pedido, id_producto, cantidad, receta_validada)
VALUES
    -- Order 2: POR_CONFIRMAR
    (2, 1, 2, NULL),
    (2, 5, 1, NULL),
    (2, 13, 1, NULL),

    -- Order 3: CONFIRMADO
    (3, 7, 1, NULL),
    (3, 10, 1, NULL),
    (3, 14, 1, NULL),

    -- Order 4: ENTREGADO
    (4, 3, 1, TRUE),
    (4, 8, 2, NULL),
    (4, 12, 1, NULL),

    -- Order 5: CANCELADO
    (5, 4, 1, NULL),
    (5, 13, 1, NULL),

    -- Order 6: ENTREGADO (urgent)
    (6, 6, 1, TRUE),
    (6, 9, 1, TRUE),
    (6, 11, 1, NULL),
    (6, 14, 2, NULL),

    -- Order 7: CONFIRMADO
    (7, 2, 1, NULL),
    (7, 5, 1, NULL),
    (7, 10, 1, NULL),

    -- Order 8: ENTREGADO
    (8, 1, 2, NULL),
    (8, 7, 1, NULL),
    (8, 12, 1, NULL),

    -- Order 9: ENTREGADO (urgent)
    (9, 3, 1, TRUE),
    (9, 6, 1, TRUE),
    (9, 15, 2, TRUE),

    -- Order 10: CANCELADO
    (10, 4, 1, NULL),
    (10, 8, 1, NULL),
    (10, 11, 1, NULL),

    -- Additional orders
    (11, 2, 2, NULL),
    (11, 5, 1, NULL),
    (11, 9, 1, FALSE),

    (12, 1, 1, NULL),
    (12, 13, 1, NULL),

    (13, 3, 1, TRUE),
    (13, 6, 1, TRUE),
    (13, 10, 1, NULL),

    (14, 4, 2, NULL),
    (14, 7, 1, NULL),
    (14, 14, 1, NULL),

    (15, 9, 1, TRUE),
    (15, 15, 2, TRUE),

    (16, 1, 1, NULL),
    (16, 8, 1, NULL),
    (16, 13, 1, NULL),

    (17, 3, 1, TRUE),
    (17, 5, 2, NULL),
    (17, 12, 1, NULL),

    (18, 2, 1, NULL),
    (18, 10, 1, NULL),
    (18, 14, 1, NULL),

    (19, 6, 1, TRUE),
    (19, 9, 1, TRUE),
    (19, 15, 1, TRUE),

    (20, 1, 2, NULL),
    (20, 7, 1, NULL),
    (20, 11, 1, NULL);

INSERT INTO calificacion (id_detalle_pedido, cliente_id, puntuacion) VALUES
(3, 2, 4.5),  -- Order 4
(5, 1, 5.0),  -- Order 6
(7, 6, 3.5),  -- Order 8
(8, 3, 4.0),  -- Order 9
(10, 8, 4.5), -- Order 11
(11, 10, 3.0), -- Order 12
(12, 4, 5.0),  -- Order 14
( 14, 7, 4.0),  -- Order 15
( 16, 5, 3.5),  -- Order 17
( 17, 9, 4.5), -- Order 18
( 19, 8, 5.0); -- Order 20

-- Add notifications for unvalidated prescriptions
INSERT INTO notificacion (id_pedido, id_producto, mensaje, fecha_creacion) VALUES
(11, 9, 'Receta no validada para Clonazepam 2mg', '2025-05-02 12:35:00'),
(19, 15, 'Receta no validada para Losartán 50mg', '2025-04-24 08:20:00');