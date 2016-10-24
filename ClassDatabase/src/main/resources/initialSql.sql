CREATE TABLE `batchTemp` (
  `BATCH_ID` int(11) NOT NULL,
  `CLASS_ID` int(11) NOT NULL,
  `div_id` int(11) NOT NULL,
  `BATCH_NAME` varchar(255) DEFAULT NULL,
  `SUB_ID` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `roll_count` int(11) DEFAULT NULL,
  PRIMARY KEY (`BATCH_ID`,`CLASS_ID`,`div_id`)
) ENGINE=InnoDB