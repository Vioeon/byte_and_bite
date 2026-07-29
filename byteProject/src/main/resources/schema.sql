DROP TABLE IF EXISTS member;

CREATE TABLE member (
                        member_id INT AUTO_INCREMENT PRIMARY KEY,
                        email VARCHAR(100) NOT NULL UNIQUE,
                        password VARCHAR(255) NOT NULL,
                        nickname VARCHAR(50) NOT NULL,
                        is_manager BOOLEAN NOT NULL DEFAULT FALSE,
                        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                        updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS restaurant;

CREATE TABLE restaurant (
                            restaurant_id INT AUTO_INCREMENT PRIMARY KEY,
                            member_id INT NOT NULL UNIQUE,
                            category INT NOT NULL,
                            name VARCHAR(100) NOT NULL,
                            address VARCHAR(255) NOT NULL,
                            phone VARCHAR(20) NOT NULL,
                            CONSTRAINT fk_restaurant_member FOREIGN KEY (member_id) REFERENCES member(member_id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS post;

CREATE TABLE post (
                      post_id INT AUTO_INCREMENT PRIMARY KEY,
                      member_id INT NOT NULL,
                      title VARCHAR(200) NOT NULL,
                      content TEXT NOT NULL,
                      view_count INT NOT NULL DEFAULT 0,
                      created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                      updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                      CONSTRAINT fk_post_member FOREIGN KEY (member_id) REFERENCES member(member_id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS `like`;

CREATE TABLE `like` (
                        like_id INT AUTO_INCREMENT PRIMARY KEY,
                        post_id INT NOT NULL,
                        member_id INT NOT NULL,
                        is_checked BOOLEAN NOT NULL DEFAULT FALSE,
                        UNIQUE KEY uk_post_member (post_id, member_id),  -- 중복 좋아요 방지
                        FOREIGN KEY (post_id) REFERENCES post(post_id) ON DELETE CASCADE,
                        FOREIGN KEY (member_id) REFERENCES member(member_id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS reply;

CREATE TABLE reply (
                       reply_id INT AUTO_INCREMENT PRIMARY KEY,
                       post_id INT NOT NULL,
                       member_id INT NOT NULL,
                       content TEXT NOT NULL,
                       created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                       updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       CONSTRAINT fk_reply_post FOREIGN KEY (post_id) REFERENCES post(post_id) ON DELETE CASCADE,
                       CONSTRAINT fk_reply_member FOREIGN KEY (member_id) REFERENCES member(member_id) ON DELETE CASCADE
);