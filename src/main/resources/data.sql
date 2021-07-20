REPLACE INTO `role`
VALUES (1, 'ADMIN');
REPLACE INTO `role`
VALUES (2, 'STAFF');
REPLACE INTO `role`
VALUES (3, 'MANAGER');
REPLACE INTO `role`
VALUES (4, 'TECHNICIAN');
REPLACE INTO `position`
VALUES (1, 'Casino Host');
REPLACE INTO `position`
VALUES (2, 'Cruise Ship Attendant');
REPLACE INTO `position`
VALUES (3, 'Front Desk Associate');
REPLACE INTO `position`
VALUES (4, 'Front Desk Supervisor');
REPLACE INTO `position`
VALUES (5, 'Front Office Attendant');
REPLACE INTO `position`
VALUES (6, 'Front-of-House Manager');
REPLACE INTO `position`
VALUES (7, 'Hotel Receptionist');
REPLACE INTO `user`
-- admin@gmail.com 1234
VALUES ('1', 1, 'admin@gmail.com', '', '', '$2a$04$ROGPlZSa/RNFShmW75qjS.PL2Ie.mARwG.lmb13OdIKS/zwCmd..i');
REPLACE INTO `user_role`
VALUES (1, 1);