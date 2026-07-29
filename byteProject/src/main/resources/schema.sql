USE bytebite;

-- 최초 실행 후 bytebite에 모든 기존테이블 삭제된것 확인하고 삭제
DROP TABLE IF EXISTS reply;
DROP TABLE IF EXISTS likes;
DROP TABLE IF EXISTS restaurant;
DROP TABLE IF EXISTS post;
DROP TABLE IF EXISTS member;
--

DROP TABLE IF EXISTS reply;
DROP TABLE IF EXISTS news;
DROP TABLE IF EXISTS post;
DROP TABLE IF EXISTS restaurant;
DROP TABLE IF EXISTS member;

CREATE TABLE member (
                        member_id INT AUTO_INCREMENT PRIMARY KEY,
                        email VARCHAR(100) NOT NULL UNIQUE,
                        password VARCHAR(255) NOT NULL,
                        nickname VARCHAR(50) NOT NULL,
                        role TINYINT NOT NULL DEFAULT 0,
                        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                        updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS restaurant;

CREATE TABLE restaurant (
                            restaurant_id INT AUTO_INCREMENT PRIMARY KEY,
                            member_id INT NOT NULL UNIQUE,
                            category TINYINT NOT NULL,
                            rname VARCHAR(50) NOT NULL,
                            address VARCHAR(100) NOT NULL,
                            phone VARCHAR(20) NOT NULL,
                            image VARCHAR(50) NOT NULL,
                            created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                            updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            CONSTRAINT fk_restaurant_member FOREIGN KEY (member_id) REFERENCES member(member_id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS post;

CREATE TABLE post (
                      post_id INT AUTO_INCREMENT PRIMARY KEY,
                      member_id INT NOT NULL,
                      restaurant_id INT NOT NULL,
                      title VARCHAR(200) NOT NULL,
                      content TEXT NOT NULL,
                      view_count INT NOT NULL DEFAULT 0,
                      image VARCHAR(50) NOT NULL,
                      created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                      updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                      CONSTRAINT fk_post_member FOREIGN KEY (member_id) REFERENCES member(member_id) ON DELETE CASCADE,
                      CONSTRAINT fk_post_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurant(restaurant_id) ON DELETE CASCADE
);

CREATE TABLE news (
                      news_id INT AUTO_INCREMENT PRIMARY KEY,
                      member_id INT NOT NULL,
                      restaurant_id INT NOT NULL,
                      title VARCHAR(200) NOT NULL,
                      content TEXT NOT NULL,
                      view_count INT NOT NULL DEFAULT 0,
                      image VARCHAR(50) NOT NULL,
                      created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                      updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                      CONSTRAINT fk_news_member FOREIGN KEY (member_id) REFERENCES member(member_id) ON DELETE CASCADE,
                      CONSTRAINT fk_news_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurant(restaurant_id) ON DELETE CASCADE
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