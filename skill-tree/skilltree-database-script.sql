CREATE DATABASE IF NOT EXISTS skilltree;
USE skilltree;

CREATE TABLE `users` (
  `id` binary(16) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `first_name` varchar(255) NOT NULL,
  `image_url` varchar(255) NOT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `rds_user_id` varchar(255) DEFAULT NULL,
  `user_role` enum('MAVEN','MEMBER','SUPERUSER','USER') NOT NULL,
  `created_by` binary(16) DEFAULT NULL,
  `updated_by` binary(16) DEFAULT NULL,
  `user_type` tinyint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKibk1e3kaxy5sfyeekp8hbhnim` (`created_by`),
  KEY `FKci7xr690rvyv3bnfappbyh8x0` (`updated_by`),
  CONSTRAINT `FKci7xr690rvyv3bnfappbyh8x0` FOREIGN KEY (`updated_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FKibk1e3kaxy5sfyeekp8hbhnim` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `skill` (
  `id` binary(16) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `is_deleted` bit(1) NOT NULL,
  `name` varchar(255) NOT NULL,
  `skill_type` enum('ATOMIC','DERIVED') NOT NULL,
  `created_by` binary(16) DEFAULT NULL,
  `updated_by` binary(16) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uc_skill_name` (`name`),
  KEY `FKk9ihlls00bo5ljvdgnsgf7r5w` (`created_by`),
  KEY `FKga0cp46ei9oe50sknbkty2xh7` (`updated_by`),
  CONSTRAINT `FKga0cp46ei9oe50sknbkty2xh7` FOREIGN KEY (`updated_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FKk9ihlls00bo5ljvdgnsgf7r5w` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `endorsements` (
  `id` binary(16) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `endorsement_status` enum('APPROVED','PENDING','REJECTED') DEFAULT NULL,
  `created_by` binary(16) DEFAULT NULL,
  `updated_by` binary(16) DEFAULT NULL,
  `skill_id` binary(16) DEFAULT NULL,
  `user_id` binary(16) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3sf16mlcqdyg1onb1jc6mxeio` (`created_by`),
  KEY `FK6emgcd942r9a82xxexnqnylq1` (`updated_by`),
  KEY `FKjuhgii7d9bdgjke3oj23bekjr` (`skill_id`),
  KEY `FKqifr4ov88b3t52y9y0ulyuk84` (`user_id`),
  CONSTRAINT `FK3sf16mlcqdyg1onb1jc6mxeio` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FK6emgcd942r9a82xxexnqnylq1` FOREIGN KEY (`updated_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FKjuhgii7d9bdgjke3oj23bekjr` FOREIGN KEY (`skill_id`) REFERENCES `skill` (`id`),
  CONSTRAINT `FKqifr4ov88b3t52y9y0ulyuk84` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `endorsement_list` (
  `id` binary(16) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `is_deleted` bit(1) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `type` enum('NEGATIVE','POSITIVE') NOT NULL,
  `created_by` binary(16) DEFAULT NULL,
  `updated_by` binary(16) DEFAULT NULL,
  `endorsement_id` binary(16) DEFAULT NULL,
  `user_id` binary(16) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_b2pke547fdyxfb3oxigi5pjm3` (`user_id`),
  KEY `FKqqxe0t3a2s23kgb2f48holnkk` (`created_by`),
  KEY `FKpegfdaa9fjc38wu10554ib3bj` (`updated_by`),
  KEY `FK1b6wybyeg8g4h5ov6pku3or7x` (`endorsement_id`),
  CONSTRAINT `FK1b6wybyeg8g4h5ov6pku3or7x` FOREIGN KEY (`endorsement_id`) REFERENCES `endorsements` (`id`),
  CONSTRAINT `FKpegfdaa9fjc38wu10554ib3bj` FOREIGN KEY (`updated_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FKqqxe0t3a2s23kgb2f48holnkk` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`),
  CONSTRAINT `FKr2uc9u9bi64lae1hiy61v206h` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `user_skill` (
  `user_id` binary(16) NOT NULL,
  `skill_id` binary(16) NOT NULL,
  PRIMARY KEY (`user_id`,`skill_id`),
  KEY `FKj53flyds4vknyh8llw5d7jdop` (`skill_id`),
  CONSTRAINT `FKc2o84wspod5se9pa8lxmhig0q` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKj53flyds4vknyh8llw5d7jdop` FOREIGN KEY (`skill_id`) REFERENCES `skill` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
