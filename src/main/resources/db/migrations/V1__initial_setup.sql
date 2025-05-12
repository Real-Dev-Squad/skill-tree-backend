-- Skills table
CREATE TABLE `skills` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  `updated_at` datetime(6) DEFAULT NULL,
  `created_by` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `skill_type` enum('ATOMIC') NOT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Endorsements table
CREATE TABLE `endorsements` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) NOT NULL,
  `deleted_at` datetime(6) DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  `updated_at` datetime(6) DEFAULT NULL,
  `endorse_id` varchar(255) NOT NULL,
  `endorser_id` varchar(255) NOT NULL,
  `message` text NOT NULL,
  `skill_id` int NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_endorsements_skill_id` FOREIGN KEY (`skill_id`) REFERENCES `skills` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- User skills table
CREATE TABLE `user_skills` (
  `id` int NOT NULL AUTO_INCREMENT,
  `status` enum('APPROVED','PENDING','REJECTED') NOT NULL,
  `user_id` varchar(255) NOT NULL,
  `skill_id` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_user_skills` (`user_id`, `skill_id`),
  CONSTRAINT `fk_user_skills_skill_id` FOREIGN KEY (`skill_id`) REFERENCES `skills` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;