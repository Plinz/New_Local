--IMAGE TABLE--
INSERT INTO IMAGE (id,name,description,image_path) VALUES (1, 'default_image', 'image par défaut', 'https://res.cloudinary.com/newlocal/image/upload/v1545298249/default_image.jpg');

INSERT INTO IMAGE (id,name,description,image_path) VALUES (2, 'fruit', 'categorie fruit', 'https://res.cloudinary.com/newlocal/image/upload/v1545298178/8.-Astuces-pour-reconna%C3%AEtre-les-fruits-m%C3%BBrs-e1438157989509.jpg');
INSERT INTO IMAGE (id,name,description,image_path) VALUES (3, 'légume', 'categorie légume', 'https://res.cloudinary.com/newlocal/image/upload/v1545298839/iStock-589415708-1080x675.jpg');
INSERT INTO IMAGE (id,name,description,image_path) VALUES (4, 'viande', 'categorie viande', 'https://res.cloudinary.com/newlocal/image/upload/v1545298839/signes-officiels-de-qualite.jpg');
INSERT INTO IMAGE (id,name,description,image_path) VALUES (5, 'poisson', 'categorie poisson', 'https://res.cloudinary.com/newlocal/image/upload/v1545298838/Poissonnerie-4-750x502.jpg');
INSERT INTO IMAGE (id,name,description,image_path) VALUES (6, 'épicerie', 'categorie épicerie', 'https://res.cloudinary.com/newlocal/image/upload/v1545298839/legumes.jpg');

INSERT INTO IMAGE (id,name,description,image_path) VALUES (7, 'pomme', 'product_type pomme', 'https://res.cloudinary.com/newlocal/image/upload/v1545316790/dinde.jpg');
INSERT INTO IMAGE (id,name,description,image_path) VALUES (8, 'poire', 'product_type poire', 'https://res.cloudinary.com/newlocal/image/upload/v1545317603/vinaigre-balsamique-poire.jpg');
INSERT INTO IMAGE (id,name,description,image_path) VALUES (9, 'citron', 'product_type citron', 'https://res.cloudinary.com/newlocal/image/upload/v1545316789/citron-jardin.jpg');
INSERT INTO IMAGE (id,name,description,image_path) VALUES (10, 'clémentine', 'product_type clémentine', 'https://res.cloudinary.com/newlocal/image/upload/v1545316788/clementine_346_346_filled.jpg');
INSERT INTO IMAGE (id,name,description,image_path) VALUES (11, 'brocoli', 'product_type brocoli', 'https://res.cloudinary.com/newlocal/image/upload/v1545316790/10659093.jpg');
INSERT INTO IMAGE (id,name,description,image_path) VALUES (12, 'pomme de terre', 'product_type pomme de terre', 'https://res.cloudinary.com/newlocal/image/upload/v1545316788/11085-2-pommes-de-terre-allians-bio-origine-france-cal-35-1-5-kg-20885.jpg');
INSERT INTO IMAGE (id,name,description,image_path) VALUES (13, 'carrote', 'product_type carrote', 'https://res.cloudinary.com/newlocal/image/upload/v1545316789/10659145.jpg');
INSERT INTO IMAGE (id,name,description,image_path) VALUES (14, 'concombre', 'product_type concombre', 'https://res.cloudinary.com/newlocal/image/upload/v1545316790/10659242.jpg');
INSERT INTO IMAGE (id,name,description,image_path) VALUES (15, 'épinard', 'product_type épinard', 'https://res.cloudinary.com/newlocal/image/upload/v1545316790/10659093.jpg');
INSERT INTO IMAGE (id,name,description,image_path) VALUES (16, 'agneau', 'product_type agneau', 'https://res.cloudinary.com/newlocal/image/upload/v1545316790/fotolia_6358672_subscription_xl-copy.jpg');
INSERT INTO IMAGE (id,name,description,image_path) VALUES (17, 'porc', 'product_type porc', 'https://res.cloudinary.com/newlocal/image/upload/v1545316795/viande-porc-roti-dans-le-filet.jpg');
INSERT INTO IMAGE (id,name,description,image_path) VALUES (18, 'poulet', 'product_type poulet', 'https://res.cloudinary.com/newlocal/image/upload/v1545316793/poulet-fermier.jpg');
INSERT INTO IMAGE (id,name,description,image_path) VALUES (19, 'thon', 'product_type thon', 'https://res.cloudinary.com/newlocal/image/upload/v1545318017/menual136_0.jpg');
INSERT INTO IMAGE (id,name,description,image_path) VALUES (20, 'crevette', 'product_type crevette', 'https://res.cloudinary.com/newlocal/image/upload/v1545316789/crevettes-roses-cuites.jpg');
INSERT INTO IMAGE (id,name,description,image_path) VALUES (21, 'langoustine', 'product_type langoustine', 'https://res.cloudinary.com/newlocal/image/upload/v1545316793/langoustine-accord-mets-vins-Copy.jpg');
INSERT INTO IMAGE (id,name,description,image_path) VALUES (22, 'moule', 'product_type moule', 'https://res.cloudinary.com/newlocal/image/upload/v1545316795/r63_moules-bouchot_fo.jpg');
INSERT INTO IMAGE (id,name,description,image_path) VALUES (23, 'riz', 'product_type riz', 'https://res.cloudinary.com/newlocal/image/upload/v1545318125/riz_article.jpg');
INSERT INTO IMAGE (id,name,description,image_path) VALUES (24, 'cacahuète', 'product_type cacahuète', 'https://res.cloudinary.com/newlocal/image/upload/v1545316791/fotolia_51988975_subscription_xxl-copy.jpg');
INSERT INTO IMAGE (id,name,description,image_path) VALUES (25, 'chocolat', 'product_type chocolat', 'https://res.cloudinary.com/newlocal/image/upload/v1545316795/Tablette-chocolat-noir-200g.jpg');

--CATEGORY TABLE--
INSERT INTO CATEGORY (id,name,description,image_id,category_parent_id) VALUES (1, 'Fruit','Un aliment végétal, à la saveur sucrée, généralement consommé cru', 2, null);
INSERT INTO CATEGORY (id,name,description,image_id,category_parent_id) VALUES (2, 'Légume','Généralement herbacés, utilisés dans alimentation humaine et dont la production relève du maraîchage ou du jardin potager', 3, null);
INSERT INTO CATEGORY (id,name,description,image_id,category_parent_id) VALUES (3, 'Viande', 'Aliment constitué des tissus musculaires de certains animaux, notamment les mammifères, les oiseaux et les reptiles', 4, null);
INSERT INTO CATEGORY (id,name,description,image_id,category_parent_id) VALUES (4, 'Poisson', 'Animaux vertébrés aquatiques à branchies, pourvus de nageoires', 5, null);
INSERT INTO CATEGORY (id,name,description,image_id,category_parent_id) VALUES (5, 'Épicerie', 'Un commerce de détail de proximité de denrées alimentaires', 6, null);

--PRODUCT_TYPE TABLE--
INSERT INTO PRODUCT_TYPE (id,name,description,image_id,category_id) VALUES (1,'pomme','est un fruit comestible à pépins dun goût sucré et acidulé et à la propriété plus ou moins astringente selon les variétés', 1, 1);
INSERT INTO PRODUCT_TYPE (id,name,description,image_id,category_id) VALUES (2,'poire','est un fruit à pépins comestible au goût doux et sucré dont il existe plus de deux mille variétés cultivées', 1,1);
INSERT INTO PRODUCT_TYPE (id,name,description,image_id,category_id) VALUES (3,'citron','est un agrume, fruit du citronnier (Citrus limon) arbuste de 5 à 10 mètres de haut, à feuilles persistantes, de la famille des Rutacées', 1,1);
INSERT INTO PRODUCT_TYPE (id,name,description,image_id,category_id) VALUES (4,'clémentine','est un agrume, fruit du clémentinier, un arbre hybride de la famille des rutacées, issu du croisement entre un mandarinier et un oranger', 1,1);
INSERT INTO PRODUCT_TYPE (id,name,description,image_id,category_id) VALUES (5,'brocoli','est une variété de chou originaire du sud de Italie', 1,2);
INSERT INTO PRODUCT_TYPE (id,name,description,image_id,category_id) VALUES (6,'pomme de terre','est un tubercule comestible produit par l’espèce Solanum tuberosum', 1,2);
INSERT INTO PRODUCT_TYPE (id,name,description,image_id,category_id) VALUES (7,'carrote',' est une plante bisannuelle de la famille des apiacées, largement cultivée pour sa racine pivotante charnue, comestible, de couleur généralement orangée', 1,2);
INSERT INTO PRODUCT_TYPE (id,name,description,image_id,category_id) VALUES (8,'concombre','est une plante potagère herbacée, rampante, de la même famille que la calebasse africaine, le melon ou la courge', 1,2);
INSERT INTO PRODUCT_TYPE (id,name,description,image_id,category_id) VALUES (9,'épinard','est une plante potagère, annuelle ou bisannuelle, de la famille des Chenopodiaceae, lobortis', 1,2);
INSERT INTO PRODUCT_TYPE (id,name,description,image_id,category_id) VALUES (10,'agneau','est une production agricole résultante de élevage du mouton', 1,3);
INSERT INTO PRODUCT_TYPE (id,name,description,image_id,category_id) VALUES (11,'porc','est la viande tirée un animal, le porc, ', 1,3);
INSERT INTO PRODUCT_TYPE (id,name,description,image_id,category_id) VALUES (12,'poulet','est une jeune volaille, mâle ou femelle, de la sous-espèce Gallus gallus domesticus', 1,3);
INSERT INTO PRODUCT_TYPE (id,name,description,image_id,category_id) VALUES (13,'thon','est un genre de poissons de la famille des Scombridés', 1,4);
INSERT INTO PRODUCT_TYPE (id,name,description,image_id,category_id) VALUES (14,'crevette','nom commun pour les petits crustacés aquatiques avec un exosquelette et dix pattes', 1,4);
INSERT INTO PRODUCT_TYPE (id,name,description,image_id,category_id) VALUES (15,'langoustine','est le nom vernaculaire donné à de nombreux crustacés', 1,4);
INSERT INTO PRODUCT_TYPE (id,name,description,image_id,category_id) VALUES (16,'moule','est le nom commun utilisé pour les membres de plusieurs familles de mollusques bivalves', 1,4);
INSERT INTO PRODUCT_TYPE (id,name,description,image_id,category_id) VALUES (17,'riz','est une céréale de la famille des poacées, cultivée dans les régions tropicales', 1,5);
INSERT INTO PRODUCT_TYPE (id,name,description,image_id,category_id) VALUES (18,'cacahuète','graine de l’arachide, dont on extrait de l’huile ou que l’on consomme torréfié', 1,5);
INSERT INTO PRODUCT_TYPE (id,name,description,image_id,category_id) VALUES (19,'chocolat','est un aliment plus ou moins sucré produit à partir de la fève de cacao', 1,5);


--JHI_USER TABLE--
INSERT INTO JHI_USER (ID,LOGIN,PASSWORD_HASH,FIRST_NAME,LAST_NAME,EMAIL,ACTIVATED,LANG_KEY,ACTIVATION_KEY,RESET_KEY,CREATED_BY,CREATED_DATE,RESET_DATE,LAST_MODIFIED_BY,LAST_MODIFIED_DATE) VALUES (7,'UGR72EJO4BR','PAL51JFF5RW','Florian','Gourdeau','vitae.florian@gmail.com','TRUE','fr', null, null,'system','2018-11-15 14:44:10.842','2018-11-15 14:44:10.842',' null','2018-11-15 14:44:10.842');
INSERT INTO JHI_USER (ID,LOGIN,PASSWORD_HASH,FIRST_NAME,LAST_NAME,EMAIL,ACTIVATED,LANG_KEY,ACTIVATION_KEY,RESET_KEY,CREATED_BY,CREATED_DATE,RESET_DATE,LAST_MODIFIED_BY,LAST_MODIFIED_DATE) VALUES (8,'JST68WLQ7UH','HWO25OEP1NI','Rick','Hellman','vitae@gmail.com','TRUE','fr', null, null,'system','2018-11-15 14:44:10.842','2018-11-15 14:44:10.842',' null','2018-11-15 14:44:10.842');
INSERT INTO JHI_USER (ID,LOGIN,PASSWORD_HASH,FIRST_NAME,LAST_NAME,EMAIL,ACTIVATED,LANG_KEY,ACTIVATION_KEY,RESET_KEY,CREATED_BY,CREATED_DATE,RESET_DATE,LAST_MODIFIED_BY,LAST_MODIFIED_DATE) VALUES (9,'TER86TZQ8FE','HES53LDC7OD','Antoine','Duquennoy','antoine.magna@gmail.com','TRUE','fr', null, null,'system','2018-11-15 14:44:10.842','2018-11-15 14:44:10.842',' null','2018-11-15 14:44:10.842');
INSERT INTO JHI_USER (ID,LOGIN,PASSWORD_HASH,FIRST_NAME,LAST_NAME,EMAIL,ACTIVATED,LANG_KEY,ACTIVATION_KEY,RESET_KEY,CREATED_BY,CREATED_DATE,RESET_DATE,LAST_MODIFIED_BY,LAST_MODIFIED_DATE) VALUES (10,'QPP10GPI5YI','PIT70GNK9KP','Tarik','Ghannam','tarik.Sed@gmail.com','TRUE','fr', null, null,'system','2018-11-15 14:44:10.842','2018-11-15 14:44:10.842',' null','2018-11-15 14:44:10.842');
INSERT INTO JHI_USER (ID,LOGIN,PASSWORD_HASH,FIRST_NAME,LAST_NAME,EMAIL,ACTIVATED,LANG_KEY,ACTIVATION_KEY,RESET_KEY,CREATED_BY,CREATED_DATE,RESET_DATE,LAST_MODIFIED_BY,LAST_MODIFIED_DATE) VALUES (11,'HBE71LVM5GK','VPE75AMZ5ED','Stephanie','Barona','stephanie.aliquet@gmail.com','TRUE','fr', null, null,'system','2018-11-15 14:44:10.842','2018-11-15 14:44:10.842',' null','2018-11-15 14:44:10.842');
INSERT INTO JHI_USER (ID,LOGIN,PASSWORD_HASH,FIRST_NAME,LAST_NAME,EMAIL,ACTIVATED,LANG_KEY,ACTIVATION_KEY,RESET_KEY,CREATED_BY,CREATED_DATE,RESET_DATE,LAST_MODIFIED_BY,LAST_MODIFIED_DATE) VALUES (12,'CPN88LVC9DR','GYY80WGB8KV','Paul','Posso','eget@hotmail.com','TRUE','fr', null, null,'system','2018-11-15 14:44:10.842','2018-11-15 14:44:10.842',' null','2018-11-15 14:44:10.842');
INSERT INTO JHI_USER (ID,LOGIN,PASSWORD_HASH,FIRST_NAME,LAST_NAME,EMAIL,ACTIVATED,LANG_KEY,ACTIVATION_KEY,RESET_KEY,CREATED_BY,CREATED_DATE,RESET_DATE,LAST_MODIFIED_BY,LAST_MODIFIED_DATE) VALUES (13,'XRL46DKS6RM','ABI47OPY0GX','Camille','Benounnas','cbenounnas@gmail.com','TRUE','fr', null, null,'system','2018-11-15 14:44:10.842','2018-11-15 14:44:10.842',' null','2018-11-15 14:44:10.842');
INSERT INTO JHI_USER (ID,LOGIN,PASSWORD_HASH,FIRST_NAME,LAST_NAME,EMAIL,ACTIVATED,LANG_KEY,ACTIVATION_KEY,RESET_KEY,CREATED_BY,CREATED_DATE,RESET_DATE,LAST_MODIFIED_BY,LAST_MODIFIED_DATE) VALUES (14,'ZQF43SCA9CJ','JDR05BDR4QH','Salomon','Zamora','zamora76@hotmail.com','TRUE','fr', null, null,'system','2018-11-15 14:44:10.842','2018-11-15 14:44:10.842',' null','2018-11-15 14:44:10.842');
INSERT INTO JHI_USER (ID,LOGIN,PASSWORD_HASH,FIRST_NAME,LAST_NAME,EMAIL,ACTIVATED,LANG_KEY,ACTIVATION_KEY,RESET_KEY,CREATED_BY,CREATED_DATE,RESET_DATE,LAST_MODIFIED_BY,LAST_MODIFIED_DATE) VALUES (15,'HUX35VUM4UG','XCB36OKG8ZE','Valentin','Otero','otero.val@gmail.com','TRUE','fr', null, null,'system','2018-11-15 14:44:10.842','2018-11-15 14:44:10.842',' null','2018-11-15 14:44:10.842');
INSERT INTO JHI_USER (ID,LOGIN,PASSWORD_HASH,FIRST_NAME,LAST_NAME,EMAIL,ACTIVATED,LANG_KEY,ACTIVATION_KEY,RESET_KEY,CREATED_BY,CREATED_DATE,RESET_DATE,LAST_MODIFIED_BY,LAST_MODIFIED_DATE) VALUES (16,'VAT60YTK7MQ','RWR23OHH0JJ','Iyas','Corini','corini.sed@gmail.com','TRUE','fr', null, null,'system','2018-11-15 14:44:10.842','2018-11-15 14:44:10.842',' null','2018-11-15 14:44:10.842');
INSERT INTO JHI_USER (ID,LOGIN,PASSWORD_HASH,FIRST_NAME,LAST_NAME,EMAIL,ACTIVATED,LANG_KEY,ACTIVATION_KEY,RESET_KEY,CREATED_BY,CREATED_DATE,RESET_DATE,LAST_MODIFIED_BY,LAST_MODIFIED_DATE) VALUES (17,'ENW39YLI6CR','SOP19WBZ1JA','Noel','Sarraj','sarraj098@hotmail.com','TRUE','fr', null, null,'system','2018-11-15 14:44:10.842','2018-11-15 14:44:10.842',' null','2018-11-15 14:44:10.842');
INSERT INTO JHI_USER (ID,LOGIN,PASSWORD_HASH,FIRST_NAME,LAST_NAME,EMAIL,ACTIVATED,LANG_KEY,ACTIVATION_KEY,RESET_KEY,CREATED_BY,CREATED_DATE,RESET_DATE,LAST_MODIFIED_BY,LAST_MODIFIED_DATE) VALUES (18,'TAB17TWF1GE','XXX67ZDB6BW','Riad','Khundadze','rKhundadze@gmail.com','TRUE','fr', null, null,'system','2018-11-15 14:44:10.842','2018-11-15 14:44:10.842',' null','2018-11-15 14:44:10.842');
INSERT INTO JHI_USER (ID,LOGIN,PASSWORD_HASH,FIRST_NAME,LAST_NAME,EMAIL,ACTIVATED,LANG_KEY,ACTIVATION_KEY,RESET_KEY,CREATED_BY,CREATED_DATE,RESET_DATE,LAST_MODIFIED_BY,LAST_MODIFIED_DATE) VALUES (19,'WBE80QBI3PF','WMM49VKU7BB','Benjamin','Gontard','BnjGontard@gmail.com','TRUE','fr', null, null,'system','2018-11-15 14:44:10.842','2018-11-15 14:44:10.842',' null','2018-11-15 14:44:10.842');
INSERT INTO JHI_USER (ID,LOGIN,PASSWORD_HASH,FIRST_NAME,LAST_NAME,EMAIL,ACTIVATED,LANG_KEY,ACTIVATION_KEY,RESET_KEY,CREATED_BY,CREATED_DATE,RESET_DATE,LAST_MODIFIED_BY,LAST_MODIFIED_DATE) VALUES (20,'OYE37UDN7WE','FLR08QBB0XP','Ian','Viteri','viteriI92@hotmail.com','TRUE','fr', null, null,'system','2018-11-15 14:44:10.842','2018-11-15 14:44:10.842',' null','2018-11-15 14:44:10.842');
INSERT INTO JHI_USER (ID,LOGIN,PASSWORD_HASH,FIRST_NAME,LAST_NAME,EMAIL,ACTIVATED,LANG_KEY,ACTIVATION_KEY,RESET_KEY,CREATED_BY,CREATED_DATE,RESET_DATE,LAST_MODIFIED_BY,LAST_MODIFIED_DATE) VALUES (21,'CMX58YST8ZI','PVA09INH2LT','Bertha','Ratnaparkhi','bert_rat@hotmail.com','TRUE','fr', null, null,'system','2018-11-15 14:44:10.842','2018-11-15 14:44:10.842',' null','2018-11-15 14:44:10.842');
INSERT INTO JHI_USER (ID,LOGIN,PASSWORD_HASH,FIRST_NAME,LAST_NAME,EMAIL,ACTIVATED,LANG_KEY,ACTIVATION_KEY,RESET_KEY,CREATED_BY,CREATED_DATE,RESET_DATE,LAST_MODIFIED_BY,LAST_MODIFIED_DATE) VALUES (22,'ZOT54TAE8QW','UIQ84DAC3OX','Carlos','Sous','sous_car@hotmail.com','TRUE','fr', null, null,'system','2018-11-15 14:44:10.842','2018-11-15 14:44:10.842',' null','2018-11-15 14:44:10.842');
INSERT INTO JHI_USER (ID,LOGIN,PASSWORD_HASH,FIRST_NAME,LAST_NAME,EMAIL,ACTIVATED,LANG_KEY,ACTIVATION_KEY,RESET_KEY,CREATED_BY,CREATED_DATE,RESET_DATE,LAST_MODIFIED_BY,LAST_MODIFIED_DATE) VALUES (23,'KVP07ULT2EN','FQZ45JKC0FG','Kristen','Beaubois','k_beau65@gmail.com','TRUE','fr', null, null,'system','2018-11-15 14:44:10.842','2018-11-15 14:44:10.842',' null','2018-11-15 14:44:10.842');
INSERT INTO JHI_USER (ID,LOGIN,PASSWORD_HASH,FIRST_NAME,LAST_NAME,EMAIL,ACTIVATED,LANG_KEY,ACTIVATION_KEY,RESET_KEY,CREATED_BY,CREATED_DATE,RESET_DATE,LAST_MODIFIED_BY,LAST_MODIFIED_DATE) VALUES (24,'TZF25LRP5EH','FQF89KZK6GC','Nathaniel','Cadavid','cad_nath74@gmail.com','TRUE','fr', null, null,'system','2018-11-15 14:44:10.842','2018-11-15 14:44:10.842',' null','2018-11-15 14:44:10.842');
INSERT INTO JHI_USER (ID,LOGIN,PASSWORD_HASH,FIRST_NAME,LAST_NAME,EMAIL,ACTIVATED,LANG_KEY,ACTIVATION_KEY,RESET_KEY,CREATED_BY,CREATED_DATE,RESET_DATE,LAST_MODIFIED_BY,LAST_MODIFIED_DATE) VALUES (25,'DVW15TQB8NF','JTC41HBX2UR','Flavia','Vanderfaeillie','flavia_vander@gmail.com','TRUE','fr', null, null,'system','2018-11-15 14:44:10.842','2018-11-15 14:44:10.842',' null','2018-11-15 14:44:10.842');
INSERT INTO JHI_USER (ID,LOGIN,PASSWORD_HASH,FIRST_NAME,LAST_NAME,EMAIL,ACTIVATED,LANG_KEY,ACTIVATION_KEY,RESET_KEY,CREATED_BY,CREATED_DATE,RESET_DATE,LAST_MODIFIED_BY,LAST_MODIFIED_DATE) VALUES (26,'HDO84QNF5FE','PKZ31ZCC2HB','Castor','Brochier','cast_brochier@hotmail.com','TRUE','fr', null, null,'system','2018-11-15 14:44:10.842','2018-11-15 14:44:10.842',' null','2018-11-15 14:44:10.842');
INSERT INTO JHI_USER (ID,LOGIN,PASSWORD_HASH,FIRST_NAME,LAST_NAME,EMAIL,ACTIVATED,LANG_KEY,ACTIVATION_KEY,RESET_KEY,CREATED_BY,CREATED_DATE,RESET_DATE,LAST_MODIFIED_BY,LAST_MODIFIED_DATE) VALUES (27,'UVG39ZVB6FD','PMM47BAV2LE','Maryam','Bigeater','bigeater_mary54@gmail.com','TRUE','fr', null, null,'system','2018-11-15 14:44:10.842','2018-11-15 14:44:10.842',' null','2018-11-15 14:44:10.842');
INSERT INTO JHI_USER (ID,LOGIN,PASSWORD_HASH,FIRST_NAME,LAST_NAME,EMAIL,ACTIVATED,LANG_KEY,ACTIVATION_KEY,RESET_KEY,CREATED_BY,CREATED_DATE,RESET_DATE,LAST_MODIFIED_BY,LAST_MODIFIED_DATE) VALUES (28,'OZJ96HFD6TC','PTL25JAC4SC','Ahmed','Simonetti','simahmed@hotmail.com','TRUE','fr', null, null,'system','2018-11-15 14:44:10.842','2018-11-15 14:44:10.842',' null','2018-11-15 14:44:10.842');
INSERT INTO JHI_USER (ID,LOGIN,PASSWORD_HASH,FIRST_NAME,LAST_NAME,EMAIL,ACTIVATED,LANG_KEY,ACTIVATION_KEY,RESET_KEY,CREATED_BY,CREATED_DATE,RESET_DATE,LAST_MODIFIED_BY,LAST_MODIFIED_DATE) VALUES (29,'VHP31OUI4NC','FNB52JYY9XF','Tomas','Kamwiziku','tomaskami@gmail.com','TRUE','fr', null, null,'system','2018-11-15 14:44:10.842','2018-11-15 14:44:10.842',' null','2018-11-15 14:44:10.842');
INSERT INTO JHI_USER (ID,LOGIN,PASSWORD_HASH,FIRST_NAME,LAST_NAME,EMAIL,ACTIVATED,LANG_KEY,ACTIVATION_KEY,RESET_KEY,CREATED_BY,CREATED_DATE,RESET_DATE,LAST_MODIFIED_BY,LAST_MODIFIED_DATE) VALUES (30,'OIP66YVN1EW','MGE75TXB1WM','Nathalie','Fanjas','fanjasNath52@gmail.com','TRUE','fr', null, null,'system','2018-11-15 14:44:10.842','2018-11-15 14:44:10.842',' null','2018-11-15 14:44:10.842');


--GRADE TABLE--
INSERT INTO GRADE (id,grade,nb_voter,seller_id,product_type_id) VALUES (1,0,36,26,1);
INSERT INTO GRADE (id,grade,nb_voter,seller_id,product_type_id) VALUES (2,0,849,11,5);
INSERT INTO GRADE (id,grade,nb_voter,seller_id,product_type_id) VALUES (3,1,612,30,13);
INSERT INTO GRADE (id,grade,nb_voter,seller_id,product_type_id) VALUES (4,5,140,30,7);
INSERT INTO GRADE (id,grade,nb_voter,seller_id,product_type_id) VALUES (5,3,628,9,2);
INSERT INTO GRADE (id,grade,nb_voter,seller_id,product_type_id) VALUES (6,4,248,25,12);
INSERT INTO GRADE (id,grade,nb_voter,seller_id,product_type_id) VALUES (7,0,206,27,11);
INSERT INTO GRADE (id,grade,nb_voter,seller_id,product_type_id) VALUES (8,5,716,16,5);
INSERT INTO GRADE (id,grade,nb_voter,seller_id,product_type_id) VALUES (9,5,87,21,8);
INSERT INTO GRADE (id,grade,nb_voter,seller_id,product_type_id) VALUES (10,0,412,23,7);
INSERT INTO GRADE (id,grade,nb_voter,seller_id,product_type_id) VALUES (11,3,528,12,15);
INSERT INTO GRADE (id,grade,nb_voter,seller_id,product_type_id) VALUES (12,4,231,10,12);
INSERT INTO GRADE (id,grade,nb_voter,seller_id,product_type_id) VALUES (13,108,27,6,16);
INSERT INTO GRADE (id,grade,nb_voter,seller_id,product_type_id) VALUES (14,5,325,23,8);
INSERT INTO GRADE (id,grade,nb_voter,seller_id,product_type_id) VALUES (15,1,190,19,3);
INSERT INTO GRADE (id,grade,nb_voter,seller_id,product_type_id) VALUES (16,1,100,8,2);
INSERT INTO GRADE (id,grade,nb_voter,seller_id,product_type_id) VALUES (17,4,299,22,5);
INSERT INTO GRADE (id,grade,nb_voter,seller_id,product_type_id) VALUES (18,2,528,16,10);
INSERT INTO GRADE (id,grade,nb_voter,seller_id,product_type_id) VALUES (19,1,622,9,6);
INSERT INTO GRADE (id,grade,nb_voter,seller_id,product_type_id) VALUES (20,4,687,18,4);
INSERT INTO GRADE (id,grade,nb_voter,seller_id,product_type_id) VALUES (21,4,377,14,2);
INSERT INTO GRADE (id,grade,nb_voter,seller_id,product_type_id) VALUES (22,4,682,24,14);
INSERT INTO GRADE (id,grade,nb_voter,seller_id,product_type_id) VALUES (23,1,415,22,9);
INSERT INTO GRADE (id,grade,nb_voter,seller_id,product_type_id) VALUES (24,1,554,11,1);
INSERT INTO GRADE (id,grade,nb_voter,seller_id,product_type_id) VALUES (25,0,498,7,11);

--LOCATION TABLE--
INSERT INTO LOCATION (id,city,country,zip,address,lon,lat,user_id) VALUES (1,'Lyon','France','69001','60 av Debourg','4.830183','45.731463',22);
INSERT INTO LOCATION (id,city,country,zip,address,lon,lat,user_id) VALUES (2,'Grenoble','France','38000','18 Rue HUmbert II','5.723922','45.185617',30);
INSERT INTO LOCATION (id,city,country,zip,address,lon,lat,user_id) VALUES (3,'Saint-Etienne','France','42000','59 r Richelandière','4.405465','45.437884',21); 45.437884, 4.405465
INSERT INTO LOCATION (id,city,country,zip,address,lon,lat,user_id) VALUES (4,'Annecy','France','74000','1 av Berthollet','6.122455','45.902929',15); 45.902929, 6.122455
INSERT INTO LOCATION (id,city,country,zip,address,lon,lat,user_id) VALUES (5,'Valence','France','26000','58 av Lautagne','4.906004','44.905298',18); 44.905298, 4.906004
INSERT INTO LOCATION (id,city,country,zip,address,lon,lat,user_id) VALUES (6,'Chamberry','France','73018','108 r Aristide Bergès','5.901844','45.586631',21); 45.586631, 5.901844
INSERT INTO LOCATION (id,city,country,zip,address,lon,lat,user_id) VALUES (7,'Saint-Etienne','France','42000','6 A r Léon Lamaizière','4.380768','45.466002',20); 45.466002, 4.380768
INSERT INTO LOCATION (id,city,country,zip,address,lon,lat,user_id) VALUES (8,'Montelimar','France','26200','bd Georges Pompidou','4.773231','44.546744',18);
44.546744, 4.773231
INSERT INTO LOCATION (id,city,country,zip,address,lon,lat,user_id) VALUES (9,'Grenoble','France','38000','26 av Malherbe','5.734604','45.169706',24); 45.169706, 5.734604
INSERT INTO LOCATION (id,city,country,zip,address,lon,lat,user_id) VALUES (10,'Vienne','France','4758','34 av Leclerc','4.864548','45.512369',16); 45.512369, 4.864548
INSERT INTO LOCATION (id,city,country,zip,address,lon,lat,user_id) VALUES (11,'Lille','France','59800','109 Rue Colbert','3.044003','50.631416',28); 50.631416, 3.044003
INSERT INTO LOCATION (id,city,country,zip,address,lon,lat,user_id) VALUES (12,'Marseille','France','13010','83 Avenue de la Timone','-49.23681','46.76744',19);
13010 Marseille
INSERT INTO LOCATION (id,city,country,zip,address,lon,lat,user_id) VALUES (13,'Grenoble','France','38000','36 cours Berriat','-55.08963','-84.42674',24);
INSERT INTO LOCATION (id,city,country,zip,address,lon,lat,user_id) VALUES (14,'Valence','France','26000','58 av Lautagne','-168.5462','31.41882',21);
INSERT INTO LOCATION (id,city,country,zip,address,lon,lat,user_id) VALUES (15,'Montelimar','France','26200','1 av Stéphane Mallarmé','-84.54906','37.64259',26);
INSERT INTO LOCATION (id,city,country,zip,address,lon,lat,user_id) VALUES (16,'Chamberry','France','73018','235 av Alsace Lorraine','71.05359','-30.40316',15);
INSERT INTO LOCATION (id,city,country,zip,address,lon,lat,user_id) VALUES (17,'Valence','France','26000','435 av Victor Hugo','153.78035','-24.94079',29);
INSERT INTO LOCATION (id,city,country,zip,address,lon,lat,user_id) VALUES (18,'Paris','France','75000','27 bd Soult','61.59794','66.0858',17);
INSERT INTO LOCATION (id,city,country,zip,address,lon,lat,user_id) VALUES (19,'Nice','France','06000','73 bd Carnot','-97.76654','-71.71388',14);
INSERT INTO LOCATION (id,city,country,zip,address,lon,lat,user_id) VALUES (20,'Toulouse','France','31200','8 r Jean Gilles','32.7974','-49.67637',7);
INSERT INTO LOCATION (id,city,country,zip,address,lon,lat,user_id) VALUES (21,'Toulouse','France','31200','5 r Perbosc','107.81874','-52.03581',20);


--HOLDING TABLE--
INSERT INTO HOLDING (id,siret,name,description,image_id,location_id,owner_id) VALUES (1,35510266353376,'PRIMLAND','Société existant depuis plusieurs années spécialisée dans la commercialisation en gros et négoce de fruits', 1,14,28);
INSERT INTO HOLDING (id,siret,name,description,image_id,location_id,owner_id) VALUES (2,51018813629634,'GIORDAN ET CIE','Société spécialisée dans commercialisation des produits agricoles, de fruits et légumes frais, proposant en permanence des fruits et légumes en fonction du climat', 1,21,11);
INSERT INTO HOLDING (id,siret,name,description,image_id,location_id,owner_id) VALUES (3,20258648297749,'BONDUELLE FRAIS FRANCE','est un acteur reconnu sur le marché du fruits', 1,9,13);
INSERT INTO HOLDING (id,siret,name,description,image_id,location_id,owner_id) VALUES (4,95931812617927,'SOVIFRUITS',' Commerce de gros (commerce interentreprises) de fruits et légumes', 1,16,30);
INSERT INTO HOLDING (id,siret,name,description,image_id,location_id,owner_id) VALUES (5,44159788899123,'TOMAT CERIZ','Commercialisation de produits alimentaires frais, les fruits et légumes ', 1,20,11);
INSERT INTO HOLDING (id,siret,name,description,image_id,location_id,owner_id) VALUES (6,45314601794816,'COOPERATIVE DES PRODUCTEURS LEGUMIERS','Spécialiste de la production, transformation et commercialisation de légume brut en prêt à emploi destinée à la restauration', 1,17,16);
INSERT INTO HOLDING (id,siret,name,description,image_id,location_id,owner_id) VALUES (7,54725759970024,'TAM KY','est spécialisée dans le commerce de produits alimentations exotiques(légumes)', 1,15,19);
INSERT INTO HOLDING (id,siret,name,description,image_id,location_id,owner_id) VALUES (8,90284661385230,'SOPA','Société de production, conditionnement et commercialisation de légumes frais comme culture plein champ, culture sous serre et culture biologique.', 1,21,14);
INSERT INTO HOLDING (id,siret,name,description,image_id,location_id,owner_id) VALUES (9,45579464863985,'AMOROS ET CIE','Commercialisation de viande frais, commerce de gros de agneau et porc pour les professionnels des métiers de bouche, les distributeurs au détail', 1,20,15);
INSERT INTO HOLDING (id,siret,name,description,image_id,location_id,owner_id) VALUES (10,34561075288802,'FLEURY MICHON','est une entreprise de industrie agroalimentaire française', 1,12,18);
INSERT INTO HOLDING (id,siret,name,description,image_id,location_id,owner_id) VALUES (11,78177381535060,'DESPI-LE-BOUCHER','a pour mission de redonner à agriculture et à la coopération agricole françaises toute leur place dans la chaîne de valeur alimentaire mondiale', 1,19,25);
INSERT INTO HOLDING (id,siret,name,description,image_id,location_id,owner_id) VALUES (12,45759830428287,'NUTRIXO','est un des acteurs européens de la meunerie et de la boulangerie. Une entreprise agroalimentaire au positionnement qualitatif et innovant', 1,10,7);
INSERT INTO HOLDING (id,siret,name,description,image_id,location_id,owner_id) VALUES (13,21951251435093,'ISLATUNA','Organisation armateurs qui commercialise des produits de pêche, spécialistes en thon, crabes, maquereaux, sardines, anguilles, congres, chiens de mer, etc. ', 1,9,21);
INSERT INTO HOLDING (id,siret,name,description,image_id,location_id,owner_id) VALUES (14,14194483258761,'PLATINUM SEAFOOD SAS','est commissionnaire en produits de la mer, négociant en poisson et marée implanté ', 1,14,14);
INSERT INTO HOLDING (id,siret,name,description,image_id,location_id,owner_id) VALUES (15,19790238379500,'TOP LANGOUSTE','est une société spécialisée dans la vente en gros de poissons, crustacés et mollusques', 1,20,18);
INSERT INTO HOLDING (id,siret,name,description,image_id,location_id,owner_id) VALUES (16,32822713665664,'MAS SA','Entreprise tunisienne spécialisée dans la vente et export de poissons frais en gros à des prix négociables', 1,12,10);
INSERT INTO HOLDING (id,siret,name,description,image_id,location_id,owner_id) VALUES (17,40301620410755,'GEORGES S DARAS','est spécialisée dans la commercialisation en gros et à international des produits du sol à savoir les épices, les fruits secs e', 1,14,27);
INSERT INTO HOLDING (id,siret,name,description,image_id,location_id,owner_id) VALUES (18,86815033587627,'AEON HANDELSGESELLSCHAFT MBH','Importation de produits alimentaires et alimentation animale. Produits à base de soja, cristaux de menthol, condiments, graines de puce, oignons, ail, lentilles, riz, menthe, levure sèche, chicorée, herbes.', 1,17,22);
INSERT INTO HOLDING (id,siret,name,description,image_id,location_id,owner_id) VALUES (19,62988160378299,'GLOBAL FOODS TRADING GMBH','est spécialisée dans le commerce des condiments extraits et épices', 1,11,29);
INSERT INTO HOLDING (id,siret,name,description,image_id,location_id,owner_id) VALUES (20,40570132541470,'CHOCOLATS JANIN SARL EXPLOIT','Spécialiste de la transformation de la fève de cacao', 1,16,16);

INSERT INTO HOLDING (id,siret,name,description,image_id,location_id,owner_id) VALUES (21,15436596567742,'BUTTIENS FRUITS','entreprise Jens Thiele GmbH importe et livre des fruits exotiques en conserve', 1,10,15);
INSERT INTO HOLDING (id,siret,name,description,image_id,location_id,owner_id) VALUES (22,44629894727841,'VÉGÉPACK','Toute la saveur des légumes et des legumes frais', 1,12,12);
INSERT INTO HOLDING (id,siret,name,description,image_id,location_id,owner_id) VALUES (23,19996940973214,'FAGOU','Nous sommes une entreprise commerciale d’exportation de produits agroalimentaires, congelés', 1,13,8);
INSERT INTO HOLDING (id,siret,name,description,image_id,location_id,owner_id) VALUES (24,75054652085527,'MASCATO','est une entreprise consacrée à la commercialisation des produits de la mer', 1,17,17);
INSERT INTO HOLDING (id,siret,name,description,image_id,location_id,owner_id) VALUES (25,55509336320683,'LE DELAS','Des Produits frais, secs et surgelés à disposition sur place ou expédiés en France', 1,4,23);

--WAREHOUSE TABLE--
INSERT INTO WAREHOUSE (id,name,description,tel,image_id,location_id) VALUES (1,'grandfrais','turpis. In condimentum.','05 87 07 36 49', 1,6);
INSERT INTO WAREHOUSE (id,name,description,tel,image_id,location_id) VALUES (2,'Accumsan Laoreet Limited','neque. In ornare sagittis felis. Donec tempor, est','05 26 28 31 54', 1,14);
INSERT INTO WAREHOUSE (id,name,description,tel,image_id,location_id) VALUES (3,'Sapien Limited','et magnis dis parturient montes, nascetur ridiculus mus. Donec','03 99 28 70 59', 1,21);
INSERT INTO WAREHOUSE (id,name,description,tel,image_id,location_id) VALUES (4,'Proin Inc.','aliquam adipiscing lacus. Ut','05 62 16 05 38', 1,20);
INSERT INTO WAREHOUSE (id,name,description,tel,image_id,location_id) VALUES (5,'In Company','Duis gravida. Praesent eu','09 81 69 79 12', 1,12);
INSERT INTO WAREHOUSE (id,name,description,tel,image_id,location_id) VALUES (6,'Faucibus Orci Inc.','pulvinar arcu et pede. Nunc sed orci lobortis augue','02 98 72 40 91', 1,19);
INSERT INTO WAREHOUSE (id,name,description,tel,image_id,location_id) VALUES (7,'Amet Orci Ut Corporation','vel','07 92 16 99 15', 1,12);
INSERT INTO WAREHOUSE (id,name,description,tel,image_id,location_id) VALUES (8,'Montes Nascetur Ridiculus Company','cursus','01 26 62 79 46', 1,16);
INSERT INTO WAREHOUSE (id,name,description,tel,image_id,location_id) VALUES (9,'Scelerisque Ltd','lectus sit amet luctus vulputate, nisi','05 51 89 31 88', 1,11);
INSERT INTO WAREHOUSE (id,name,description,tel,image_id,location_id) VALUES (10,'Tellus Lorem Eu Inc.','Mauris blandit enim','06 52 37 06 30', 1,12);
INSERT INTO WAREHOUSE (id,name,description,tel,image_id,location_id) VALUES (11,'Pharetra LLP','Suspendisse eleifend. Cras sed leo.','04 79 88 05 78', 1,19);
INSERT INTO WAREHOUSE (id,name,description,tel,image_id,location_id) VALUES (12,'Rhoncus Id Limited','Sed nulla ante, iaculis nec, eleifend non, dapibus rutrum,','02 84 46 01 70', 1,17);
INSERT INTO WAREHOUSE (id,name,description,tel,image_id,location_id) VALUES (13,'In Foundation','Phasellus dapibus quam','05 93 51 98 80', 1,14);
INSERT INTO WAREHOUSE (id,name,description,tel,image_id,location_id) VALUES (14,'Nunc Nulla Vulputate Company','augue ac ipsum. Phasellus vitae mauris sit','08 66 61 70 99', 1,16);
INSERT INTO WAREHOUSE (id,name,description,tel,image_id,location_id) VALUES (15,'Nam Nulla Magna Ltd','a tortor.','08 48 39 58 39', 1,18);
INSERT INTO WAREHOUSE (id,name,description,tel,image_id,location_id) VALUES (16,'Mollis Duis Industries','ac arcu. Nunc','04 65 06 43 93', 1,15);
INSERT INTO WAREHOUSE (id,name,description,tel,image_id,location_id) VALUES (17,'Eu Nulla Industries','Aliquam vulputate ullamcorper magna. Sed eu eros. Nam','07 89 50 45 48', 1,11);
INSERT INTO WAREHOUSE (id,name,description,tel,image_id,location_id) VALUES (18,'Mollis Vitae Posuere PC','Cras vehicula aliquet libero. Integer','05 74 29 14 53', 1,20);
INSERT INTO WAREHOUSE (id,name,description,tel,image_id,location_id) VALUES (19,'Per LLC','eleifend non, dapibus rutrum, justo. Praesent luctus. Curabitur egestas','01 85 43 03 11', 1,18);
INSERT INTO WAREHOUSE (id,name,description,tel,image_id,location_id) VALUES (20,'Dignissim Magna A Corporation','sem ut dolor dapibus gravida. Aliquam tincidunt, nunc ac mattis','01 10 40 03 19', 1,8);
INSERT INTO WAREHOUSE (id,name,description,tel,image_id,location_id) VALUES (21,'Donec At PC','pharetra.','08 81 86 52 00', 1,19);
INSERT INTO WAREHOUSE (id,name,description,tel,image_id,location_id) VALUES (22,'Lectus Pede Limited','aliquet magna a neque.','07 34 83 20 00', 1,20);
INSERT INTO WAREHOUSE (id,name,description,tel,image_id,location_id) VALUES (23,'Ornare Placerat Orci Incorporated','Aliquam gravida mauris','04 93 58 58 62', 1,15);
INSERT INTO WAREHOUSE (id,name,description,tel,image_id,location_id) VALUES (24,'Ac Eleifend Inc.','accumsan convallis, ante lectus convallis est, vitae sodales','05 00 31 31 43', 1,10);
INSERT INTO WAREHOUSE (id,name,description,tel,image_id,location_id) VALUES (25,'Nullam Ltd','condimentum. Donec at arcu. Vestibulum ante ipsum primis','07 19 46 67 09', 1,19);
INSERT INTO WAREHOUSE (id,name,description,tel,image_id,location_id) VALUES (26,'Dictum Institute','ad litora torquent per conubia nostra, per inceptos hymenaeos.','05 04 72 84 21', 1,8);
INSERT INTO WAREHOUSE (id,name,description,tel,image_id,location_id) VALUES (27,'Nec Tempus Consulting','non justo. Proin non massa non ante','08 24 44 17 56', 1,12);


--STOCK TABLE--
INSERT INTO STOCK (id,name,description,quantity_init,quantity_remaining,price_unit,on_sale_date,expiry_date,bio,available,image_id,product_type_id,holding_id,seller_id,warehouse_id) VALUES (1,'myrtille alsacienne','vulputate mauris sagittis',379,33,38,'18-01-29','18-10-02','false','true', 1,1,1,15,26);
INSERT INTO STOCK (id,name,description,quantity_init,quantity_remaining,price_unit,on_sale_date,expiry_date,bio,available,image_id,product_type_id,holding_id,seller_id,warehouse_id) VALUES (2,'myrtille alsacienne','vehicula et,',98,58,17,'18-06-03','18-10-15','false','true', 1,2,2,30,4);
INSERT INTO STOCK (id,name,description,quantity_init,quantity_remaining,price_unit,on_sale_date,expiry_date,bio,available,image_id,product_type_id,holding_id,seller_id,warehouse_id) VALUES (3,'ananas cendré','tortor, dictum eu, placerat eget, venenatis a, magna. Lorem ipsum',17,88,80,'18-04-11','19-03-31','true','true', 1,3,3,9,10);
INSERT INTO STOCK (id,name,description,quantity_init,quantity_remaining,price_unit,on_sale_date,expiry_date,bio,available,image_id,product_type_id,holding_id,seller_id,warehouse_id) VALUES (4,'pomme de terre asiatique','Etiam gravida molestie arcu. Sed eu nibh vulputate mauris',113,41,67,'18-05-08','19-03-30','false','false', 1,4,4,28,24);
INSERT INTO STOCK (id,name,description,quantity_init,quantity_remaining,price_unit,on_sale_date,expiry_date,bio,available,image_id,product_type_id,holding_id,seller_id,warehouse_id) VALUES (5,'mangue savoyarde','lobortis augue scelerisque mollis. Phasellus libero',555,59,39,'18-05-12','19-05-12','true','true', 1,5,5,8,7);
INSERT INTO STOCK (id,name,description,quantity_init,quantity_remaining,price_unit,on_sale_date,expiry_date,bio,available,image_id,product_type_id,holding_id,seller_id,warehouse_id) VALUES (6,'riz pilaf ','Donec egestas. Duis ac arcu.',300,67,91,'18-11-07','19-03-26','true','true', 1,6,6,20,1);
INSERT INTO STOCK (id,name,description,quantity_init,quantity_remaining,price_unit,on_sale_date,expiry_date,bio,available,image_id,product_type_id,holding_id,seller_id,warehouse_id) VALUES (7,'veau marengo','ridiculus mus. Proin vel',287,43,22,'18-03-27','19-07-17','false','false', 1,7,7,19,6);
INSERT INTO STOCK (id,name,description,quantity_init,quantity_remaining,price_unit,on_sale_date,expiry_date,bio,available,image_id,product_type_id,holding_id,seller_id,warehouse_id) VALUES (8,'veau marengo','massa. Mauris vestibulum, neque sed dictum',320,8,22,'18-04-26','19-02-02','true','false', 1,8,8,8,22);
INSERT INTO STOCK (id,name,description,quantity_init,quantity_remaining,price_unit,on_sale_date,expiry_date,bio,available,image_id,product_type_id,holding_id,seller_id,warehouse_id) VALUES (9,'veau marengo','massa. Mauris vestibulum, neque sed dictum',320,8,22,'18-04-26','19-02-02','true','false', 1,8,8,9,22);
INSERT INTO STOCK (id,name,description,quantity_init,quantity_remaining,price_unit,on_sale_date,expiry_date,bio,available,image_id,product_type_id,holding_id,seller_id,warehouse_id) VALUES (10,'papaya de chez stephanie','pretium et, rutrum non, hendrerit id, ante. Nunc mauris sapien,',827,55,77,'18-06-26','19-07-04','true','false', 1,9,10,9,14);
INSERT INTO STOCK (id,name,description,quantity_init,quantity_remaining,price_unit,on_sale_date,expiry_date,bio,available,image_id,product_type_id,holding_id,seller_id,warehouse_id) VALUES (11,'pomme de terre asiatique','urna. Nullam lobortis quam a felis ullamcorper viverra. Maecenas iaculis',864,6,23,'18-02-04','18-12-07','false','true', 1,10,11,20,12);
INSERT INTO STOCK (id,name,description,quantity_init,quantity_remaining,price_unit,on_sale_date,expiry_date,bio,available,image_id,product_type_id,holding_id,seller_id,warehouse_id) VALUES (12,'calamar croustillant','dolor sit amet,',483,52,61,'18-05-18','19-02-03','false','true', 1,11,12,14,2);
INSERT INTO STOCK (id,name,description,quantity_init,quantity_remaining,price_unit,on_sale_date,expiry_date,bio,available,image_id,product_type_id,holding_id,seller_id,warehouse_id) VALUES (13,'ananas cendré','nec ante blandit viverra. Donec tempus,',714,79,61,'18-02-09','19-07-20','true','false', 1,12,13,11,27);
INSERT INTO STOCK (id,name,description,quantity_init,quantity_remaining,price_unit,on_sale_date,expiry_date,bio,available,image_id,product_type_id,holding_id,seller_id,warehouse_id) VALUES (14,'papaya de chez stephanie','magna. Lorem ipsum dolor sit amet, consectetuer',16,70,67,'18-08-24','19-03-05','true','false', 1,13,14,27,20);
INSERT INTO STOCK (id,name,description,quantity_init,quantity_remaining,price_unit,on_sale_date,expiry_date,bio,available,image_id,product_type_id,holding_id,seller_id,warehouse_id) VALUES (15,'mangue savoyarde','montes, nascetur',702,52,43,'18-02-18','18-10-04','false','false', 1,14,15,16,11);
INSERT INTO STOCK (id,name,description,quantity_init,quantity_remaining,price_unit,on_sale_date,expiry_date,bio,available,image_id,product_type_id,holding_id,seller_id,warehouse_id) VALUES (16,'veau marengo','senectus',678,4,43,'18-02-25','18-12-20','false','false', 1,15,16,20,13);
INSERT INTO STOCK (id,name,description,quantity_init,quantity_remaining,price_unit,on_sale_date,expiry_date,bio,available,image_id,product_type_id,holding_id,seller_id,warehouse_id) VALUES (17,'brocoli du nord','eget,',493,85,32,'18-03-03','18-11-12','true','true', 1,16,17,18,26);
INSERT INTO STOCK (id,name,description,quantity_init,quantity_remaining,price_unit,on_sale_date,expiry_date,bio,available,image_id,product_type_id,holding_id,seller_id,warehouse_id) VALUES (18,'ananas cendré','ac risus. Morbi metus. Vivamus euismod urna. Nullam lobortis quam',117,1,47,'18-04-25','19-05-20','true','false', 1,17,18,18,10);
INSERT INTO STOCK (id,name,description,quantity_init,quantity_remaining,price_unit,on_sale_date,expiry_date,bio,available,image_id,product_type_id,holding_id,seller_id,warehouse_id) VALUES (19,'riz pilaf ','eu dui. Cum',9,98,7,'18-02-01','19-05-10','true','true', 1,18,19,21,10);
INSERT INTO STOCK (id,name,description,quantity_init,quantity_remaining,price_unit,on_sale_date,expiry_date,bio,available,image_id,product_type_id,holding_id,seller_id,warehouse_id) VALUES (20,'poulet bordelais','vulputate, risus a ultricies',684,78,39,'18-10-28','19-05-29','true','true', 1,19,18,24,12);
INSERT INTO STOCK (id,name,description,quantity_init,quantity_remaining,price_unit,on_sale_date,expiry_date,bio,available,image_id,product_type_id,holding_id,seller_id,warehouse_id) VALUES (21,'calamar croustillant','arcu. Sed et libero. Proin',547,39,64,'18-03-15','19-03-21','true','false', 1,20,20,25,19);
INSERT INTO STOCK (id,name,description,quantity_init,quantity_remaining,price_unit,on_sale_date,expiry_date,bio,available,image_id,product_type_id,holding_id,seller_id,warehouse_id) VALUES (22,'calamar croustillant','ornare, libero at auctor ullamcorper,',371,30,72,'18-07-16','18-11-08','false','false', 1,1,21,15,10);
INSERT INTO STOCK (id,name,description,quantity_init,quantity_remaining,price_unit,on_sale_date,expiry_date,bio,available,image_id,product_type_id,holding_id,seller_id,warehouse_id) VALUES (23,'riz pilaf ','magna, malesuada vel, convallis in,',405,97,40,'18-02-01','19-06-19','false','true', 1,5,22,15,25);
INSERT INTO STOCK (id,name,description,quantity_init,quantity_remaining,price_unit,on_sale_date,expiry_date,bio,available,image_id,product_type_id,holding_id,seller_id,warehouse_id) VALUES (24,'papaya de chez stephanie','massa. Mauris vestibulum,',140,78,81,'18-05-13','19-05-16','false','false', 1,10,23,13,18);
INSERT INTO STOCK (id,name,description,quantity_init,quantity_remaining,price_unit,on_sale_date,expiry_date,bio,available,image_id,product_type_id,holding_id,seller_id,warehouse_id) VALUES (25,'pâtes rigate','pede. Suspendisse dui. Fusce diam nunc, ullamcorper eu,',65,29,63,'18-11-18','19-05-09','true','true', 1,14,24,20,2);
INSERT INTO STOCK (id,name,description,quantity_init,quantity_remaining,price_unit,on_sale_date,expiry_date,bio,available,image_id,product_type_id,holding_id,seller_id,warehouse_id) VALUES (26,'veau marengo','eget magna. Suspendisse',175,18,39,'18-05-02','18-11-19','false','false', 1,18,25,17,17);

--PURCHASE TABLE--
INSERT INTO PURCHASE (id,sale_date,quantity,withdraw,stock_id,client_id) VALUES (1,'18-10-31',5,'true',4,13);
INSERT INTO PURCHASE (id,sale_date,quantity,withdraw,stock_id,client_id) VALUES (2,'18-07-14',9,'false',24,16);
INSERT INTO PURCHASE (id,sale_date,quantity,withdraw,stock_id,client_id) VALUES (3,'18-01-07',4,'true',18,7);
INSERT INTO PURCHASE (id,sale_date,quantity,withdraw,stock_id,client_id) VALUES (4,'18-09-15',2,'true',22,15);
INSERT INTO PURCHASE (id,sale_date,quantity,withdraw,stock_id,client_id) VALUES (5,'18-02-15',5,'true',16,26);
INSERT INTO PURCHASE (id,sale_date,quantity,withdraw,stock_id,client_id) VALUES (6,'18-11-18',4,'false',21,30);
INSERT INTO PURCHASE (id,sale_date,quantity,withdraw,stock_id,client_id) VALUES (7,'18-08-22',5,'true',3,18);
INSERT INTO PURCHASE (id,sale_date,quantity,withdraw,stock_id,client_id) VALUES (8,'18-05-31',5,'false',13,18);
INSERT INTO PURCHASE (id,sale_date,quantity,withdraw,stock_id,client_id) VALUES (9,'18-01-01',10,'false',17,19);
INSERT INTO PURCHASE (id,sale_date,quantity,withdraw,stock_id,client_id) VALUES (10,'18-03-19',7,'false',19,16);
INSERT INTO PURCHASE (id,sale_date,quantity,withdraw,stock_id,client_id) VALUES (11,'18-07-04',6,'false',16,15);
INSERT INTO PURCHASE (id,sale_date,quantity,withdraw,stock_id,client_id) VALUES (12,'18-05-30',4,'true',25,29);
INSERT INTO PURCHASE (id,sale_date,quantity,withdraw,stock_id,client_id) VALUES (13,'18-07-29',6,'true',3,27);
INSERT INTO PURCHASE (id,sale_date,quantity,withdraw,stock_id,client_id) VALUES (14,'18-05-21',3,'false',17,23);
INSERT INTO PURCHASE (id,sale_date,quantity,withdraw,stock_id,client_id) VALUES (15,'18-08-21',3,'true',18,13);
INSERT INTO PURCHASE (id,sale_date,quantity,withdraw,stock_id,client_id) VALUES (16,'18-03-14',9,'false',20,23);
INSERT INTO PURCHASE (id,sale_date,quantity,withdraw,stock_id,client_id) VALUES (17,'18-02-04',8,'true',24,20);
INSERT INTO PURCHASE (id,sale_date,quantity,withdraw,stock_id,client_id) VALUES (18,'18-01-29',2,'true',10,26);
INSERT INTO PURCHASE (id,sale_date,quantity,withdraw,stock_id,client_id) VALUES (19,'18-06-10',2,'false',17,15);
INSERT INTO PURCHASE (id,sale_date,quantity,withdraw,stock_id,client_id) VALUES (20,'18-07-12',10,'true',21,25);
INSERT INTO PURCHASE (id,sale_date,quantity,withdraw,stock_id,client_id) VALUES (21,'18-02-12',10,'false',25,11);
INSERT INTO PURCHASE (id,sale_date,quantity,withdraw,stock_id,client_id) VALUES (22,'18-08-24',8,'true',6,12);
INSERT INTO PURCHASE (id,sale_date,quantity,withdraw,stock_id,client_id) VALUES (23,'18-03-21',6,'false',23,15);
INSERT INTO PURCHASE (id,sale_date,quantity,withdraw,stock_id,client_id) VALUES (24,'18-02-07',1,'true',18,23);
INSERT INTO PURCHASE (id,sale_date,quantity,withdraw,stock_id,client_id) VALUES (25,'18-07-29',7,'false',9,28);
INSERT INTO PURCHASE (id,sale_date,quantity,withdraw,stock_id,client_id) VALUES (26,'18-06-21',5,'false',13,27);
INSERT INTO PURCHASE (id,sale_date,quantity,withdraw,stock_id,client_id) VALUES (27,'18-06-19',2,'false',12,12);

