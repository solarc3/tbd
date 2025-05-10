INSERT INTO users (username, email, password) VALUES
('cliente1', 'cliente1@example.com', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6' ),
('cliente2', 'cliente2@example.com', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6'),
('cliente3', 'cliente3@example.com', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6'),
('cliente4', 'cliente4@example.com', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6'),
('cliente5', 'cliente5@example.com', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6'),
('cliente6', 'cliente6@example.com', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6'),
('cliente7', 'cliente7@example.com', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6'),
('cliente8', 'cliente8@example.com', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6'),
('cliente9', 'cliente9@example.com', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6'),
('cliente10', 'cliente10@example.com', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6');

INSERT INTO farmacia (nombre_farmacia, direccion)VALUES
('Farmacia Salud', 'Av. Principal 123'),
('Farmacia Cruz Verde', 'Av. Los Leones 2345'),
('Farmacia Ahumada', 'Calle Providencia 768'),
('Farmacia Dr. Simi', 'Av. Macul 1234'),
('Farmacia Bienestar', 'Av. Irarrázaval 5678'),
('Farmacia SanaSana', 'Av. Vicuña Mackenna 4321');

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
     'Medicamentos',
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
        'Medicamento',
        true,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw0afc6c9c/images/products/15226/15226.jpg'
    ),
    (
        62,
        'Kitadol 500 mg x 24 Comprimidos',
        995,
        'Medicamento',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw850274b1/images/products/15414/15414.jpg'
    ),
    (
        63,
        'Losartan 50 mg x 30 Comprimidos Recubiertos CHILE',
        2799,
        'Medicamento',
        true,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dwf9766b70/images/products/86132/86132.jpg'
    ),
    (
        64,
        'Natuvit Caramelo Propoleo x 10 Comprimidos',
        1295,
        'Medicamento',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw2761754a/images/products/49014/49014.jpg'
    ),
    (
        65,
        'Omeprazol 20 mg x 60 Cápsulas CHILE',
        3999,
        'Medicamento',
        true,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dwe22f7f24/images/products/84417/84417.jpg'
    ),
    (
        66,
        'Femelle x 28 Comprimidos Recubiertos',
        15339,
        'Medicamento',
        true,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dwbd9607ef/images/products/41554/41554.jpg'
    ),
    (
        67,
        'Ibuprofeno 200 mg x 20 Comprimidos Recubiertos',
        1999,
        'Medicamento',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw063fa220/images/products/86357/86357.jpg'
    ),
    (
        68,
        'Xumadol 1 g Sobres x 20 Sobres Con Granulado Efervescente',
        10922,
        'Medicamento',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw41d1f00e/images/products/44884/44884.jpg'
    ),
    (
        69,
        'Gaviscon Comprimidos Masticables Doble Acción x8',
        4759,
        'Medicamento',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dwaf8aaf98/images/products/72036/72036.jpg'
    ),
    (
        70,
        'Ciruelax Forte 125 mg x 60 Comprimidos Recubiertos',
        9264,
        'Medicamento',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw6099a442/images/products/79941/79941.jpg'
    ),
    (
        71,
        'Panadol Niños 100mg/Ml Gotas 15ml',
        3495,
        'Medicamento',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw19b1c268/images/products/90333/90333.jpg'
    ),
    (
        72,
        'Neuroval Cd 10 mg Caja 30 Comp. Dispersables',
        13194,
        'Medicamento',
        true,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw13ccae30/images/products/78701/78701.jpg'
    ),
    (
        73,
        'Cardioaspirina EC 100 mg x 50 Comprimidos Con Recubrimiento Entérico',
        11432,
        'Medicamento',
        true,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dwa1f85b49/images/products/1302/1302.jpg'
    ),
    (
        74,
        'Terbinafina 250 mg x 30 Comprimidos',
        8699,
        'Medicamento',
        true,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dwdc98599a/images/products/92811/92811.jpg'
    ),
    (
        75,
        'Nogesta 75 mcg x 28 Comprimidos Recubiertos',
        11179,
        'Medicamento',
        true,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw78ce1f46/images/products/44024/44024.jpg'
    ),
    (
        76,
        'MUXOL JARABE ADULTO Ambroxol Clorhidrato 600 mg 100 ml',
        3995,
        'Medicamento',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dwc884d75b/images/products/9257/9257.jpg'
    ),
    (
        77,
        'Abrilar 35 mg/5 mL x 100 mL Jarabe',
        6017,
        'Medicamento',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw569c027d/images/products/26896/26896.jpg'
    ),
    (
        78,
        'Tapsin 100 mg/mL x 15 mL Solución Oral Para Gotas',
        3654,
        'Medicamento',
        false,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw8552a39f/images/products/19274/19274.jpg'
    ),
    (
        79,
        'Pilocarpina Clorhidrato 4 % x 10 ml Solución Oftálmica SAVAL S.A.',
        14199,
        'Medicamento',
        true,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dw977e3535/images/products/8412/8412.jpg'
    ),
    (
        80,
        'Engystol x 50 Comprimidos',
        20314,
        'Medicamento',
        true,
        'https://www.farmaciasahumada.cl/dw/image/v2/BJVH_PRD/on/demandware.static/-/Sites-ahumada-master-catalog/default/dwe74a2eed/images/products/49937/49937.jpg'
    );

INSERT INTO pedido (id_pedido, monto, fecha_pedido, es_urgente, estado_pedido, id_cliente, id_farmacia) VALUES
(2, 15600, '2025-05-09 14:30:00', false, 'POR_CONFIRMAR', 3, 2),
(3, 9800, '2025-05-08 10:15:00', false, 'CONFIRMADO', 5, 3),
(4, 23500, '2025-05-07 16:45:00', true, 'ENTREGADO', 2, 1),
(5,  7900, '2025-05-07 09:20:00', false, 'CANCELADO', 4, 6),
(6, 31200, '2025-05-06 13:10:00', true, 'ENTREGADO', 1, 4),
(7, 12400, '2025-05-06 11:25:00', false, 'CONFIRMADO', 7, 5),
(8, 18900, '2025-05-05 15:50:00', false, 'ENTREGADO', 6, 2),
(9, 27500, '2025-05-04 08:40:00', true, 'ENTREGADO', 3, 1),
(10, 14300, '2025-05-03 17:15:00', false, 'CANCELADO', 9, 3),
(11, 19800, '2025-05-02 12:30:00', false, 'ENTREGADO', 8, 4),
(12, 8700, '2025-05-01 14:45:00', false, 'ENTREGADO', 10, 6),
(13, 22100, '2025-04-30 11:20:00', true, 'CONFIRMADO', 2, 5),
(14, 16700, '2025-04-29 09:35:00', false, 'ENTREGADO', 4, 2),
(15, 29400, '2025-04-28 16:10:00', true, 'ENTREGADO', 7, 3),
(16, 11200, '2025-04-27 13:50:00', false, 'CANCELADO', 1, 1),
(17, 21800, '2025-04-26 10:25:00', false, 'ENTREGADO', 5, 6),
(18, 13500, '2025-04-25 15:40:00', false, 'ENTREGADO', 9, 4),
(19, 26300, '2025-04-24 08:15:00', true, 'CONFIRMADO', 6, 5),
(20, 17900, '2025-04-23 14:55:00', false, 'ENTREGADO', 8, 2);

INSERT INTO producto_farmacia (id_producto, id_farmacia, stock_producto) VALUES
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

INSERT INTO repartidor (nombre_repartidor, fecha_contratacion) VALUES
('María González', '2022-03-10'),
('Carlos Rodríguez', '2022-02-05'),
('Ana Martínez', '2022-06-15'),
('Diego Sánchez', '2022-04-22'),
('Valeria Muñoz', '2022-05-18'),
('Roberto Fernández', '2022-07-01'),
('Camila Torres', '2022-03-28'),
('José Hernández', '2022-02-12'),
('Lucía Flores', '2022-08-05'),
('Andrés López', '2022-09-11');

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

INSERT INTO detalle_pedido (id_detalle_pedido, id_pedido, id_repartidor, metodo_pago, fecha_entrega) VALUES
(1, 2, 5, 'Tarjeta de Crédito', NULL),
(2, 3, 9, 'Efectivo', NULL),
(3, 4, 3, 'Tarjeta de Débito', '2025-05-07 19:20:00'),
(4, 5, 4, 'Transferencia', NULL),
(5, 6, 10, 'Tarjeta de Crédito', '2025-05-06 15:45:00'),
(6, 7, 2, 'Efectivo', NULL),
(7, 8, 7, 'Tarjeta de Débito', '2025-05-05 18:30:00'),
(8, 9, 3, 'Tarjeta de Crédito', '2025-05-04 11:15:00'),
(9, 10, 8, 'Efectivo', NULL),
(10, 11, 1, 'Transferencia', '2025-05-02 15:10:00'),
(11, 12, 5, 'Tarjeta de Crédito', '2025-05-01 17:30:00'),
(12, 13, 3, 'Tarjeta de Débito', NULL),
(13, 14, 6, 'Efectivo', '2025-04-29 12:15:00'),
(14, 15, 9, 'Tarjeta de Crédito', '2025-04-28 18:45:00'),
(15, 16, 2, 'Transferencia', NULL),
(16, 17, 4, 'Tarjeta de Débito', '2025-04-26 13:00:00'),
(17, 18, 10, 'Efectivo', '2025-04-25 18:15:00'),
(18, 19, 3, 'Tarjeta de Crédito', NULL),
(19, 20, 7, 'Transferencia', '2025-04-23 17:30:00');

-- Connect products to orders
INSERT INTO producto_pedido (id_producto_pedido, id_pedido, id_producto, cantidad, receta_validada) VALUES
-- Order 2: POR_CONFIRMAR
(1, 2, 1, 2, NULL),
(2, 2, 5, 1, NULL),
(3, 2, 13, 1, NULL),

-- Order 3: CONFIRMADO
(4, 3, 7, 1, NULL),
(5, 3, 10, 1, NULL),
(6, 3, 14, 1, NULL),

-- Order 4: ENTREGADO
(7, 4, 3, 1, TRUE),
(8, 4, 8, 2, NULL),
(9, 4, 12, 1, NULL),

-- Order 5: CANCELADO
(10, 5, 4, 1, NULL),
(11, 5, 13, 1, NULL),

-- Order 6: ENTREGADO (urgent)
(12, 6, 6, 1, TRUE),
(13, 6, 9, 1, TRUE),
(14, 6, 11, 1, NULL),
(15, 6, 14, 2, NULL),

-- Order 7: CONFIRMADO
(16, 7, 2, 1, NULL),
(17, 7, 5, 1, NULL),
(18, 7, 10, 1, NULL),

-- Order 8: ENTREGADO
(19, 8, 1, 2, NULL),
(20, 8, 7, 1, NULL),
(21, 8, 12, 1, NULL),

-- Order 9: ENTREGADO (urgent)
(22, 9, 3, 1, TRUE),
(23, 9, 6, 1, TRUE),
(24, 9, 15, 2, TRUE),

-- Order 10: CANCELADO
(25, 10, 4, 1, NULL),
(26, 10, 8, 1, NULL),
(27, 10, 11, 1, NULL),

-- Additional orders
(28, 11, 2, 2, NULL),
(29, 11, 5, 1, NULL),
(30, 11, 9, 1, FALSE),

(31, 12, 1, 1, NULL),
(32, 12, 13, 1, NULL),

(33, 13, 3, 1, TRUE),
(34, 13, 6, 1, TRUE),
(35, 13, 10, 1, NULL),

(36, 14, 4, 2, NULL),
(37, 14, 7, 1, NULL),
(38, 14, 14, 1, NULL),

(39, 15, 9, 1, TRUE),
(40, 15, 15, 2, TRUE),

(41, 16, 1, 1, NULL),
(42, 16, 8, 1, NULL),
(43, 16, 13, 1, NULL),

(44, 17, 3, 1, TRUE),
(45, 17, 5, 2, NULL),
(46, 17, 12, 1, NULL),

(47, 18, 2, 1, NULL),
(48, 18, 10, 1, NULL),
(49, 18, 14, 1, NULL),

(50, 19, 6, 1, TRUE),
(51, 19, 9, 1, TRUE),
(52, 19, 15, 1, TRUE),

(53, 20, 1, 2, NULL),
(54, 20, 7, 1, NULL),
(55, 20, 11, 1, NULL);

INSERT INTO calificacion (id_calificacion, id_detalle_pedido, cliente_id, puntuacion) VALUES
(1, 3, 2, 4.5),  -- Order 4
(2, 5, 1, 5.0),  -- Order 6
(3, 7, 6, 3.5),  -- Order 8
(4, 8, 3, 4.0),  -- Order 9
(5, 10, 8, 4.5), -- Order 11
(6, 11, 10, 3.0), -- Order 12
(7, 13, 4, 5.0),  -- Order 14
(8, 14, 7, 4.0),  -- Order 15
(9, 16, 5, 3.5),  -- Order 17
(10, 17, 9, 4.5), -- Order 18
(11, 19, 8, 5.0); -- Order 20

-- Add notifications for unvalidated prescriptions
INSERT INTO notificacion (id_notificacion, id_pedido, id_producto, mensaje, fecha_creacion) VALUES
(1, 11, 9, 'Receta no validada para Clonazepam 2mg', '2025-05-02 12:35:00'),
(2, 19, 15, 'Receta no validada para Losartán 50mg', '2025-04-24 08:20:00');