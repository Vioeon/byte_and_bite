USE bytebite;

DROP TABLE IF EXISTS reply;
DROP TABLE IF EXISTS news;
DROP TABLE IF EXISTS post;
DROP TABLE IF EXISTS restaurant;
DROP TABLE IF EXISTS member;


# SELECT post_id, title, image
# FROM post
# ORDER BY post_id DESC;
#
# SELECT post_id, image
# FROM post;

# SELECT restaurant_id, rname
# FROM restaurant;
#
# SELECT restaurant_id, rname
# FROM restaurant
# WHERE rname LIKE '%없는식당%';
#
# SELECT restaurant_id, rname
# FROM restaurant;
#
# SELECT restaurant_id, rname
# FROM restaurant
# WHERE rname LIKE '%없는식당%';

CREATE TABLE member (
                        member_id INT AUTO_INCREMENT PRIMARY KEY,
                        email VARCHAR(100) NOT NULL UNIQUE,
                        password VARCHAR(255) NOT NULL,
                        nickname VARCHAR(50) NOT NULL,
                        role VARCHAR(7) NOT NULL DEFAULT 'USER',
                        status VARCHAR(10) NOT NULL DEFAULT 'ACTIVE',
                        created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                        updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE restaurant (
                            restaurant_id INT AUTO_INCREMENT PRIMARY KEY,
                            member_id INT NOT NULL UNIQUE,
                            category VARCHAR(3) NOT NULL,
                            rname VARCHAR(50) NOT NULL,
                            address VARCHAR(100) NOT NULL,
                            phone VARCHAR(20) NOT NULL,
                            image VARCHAR(100) NOT NULL,
                            created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                            updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            CONSTRAINT fk_restaurant_member FOREIGN KEY (member_id) REFERENCES member(member_id) ON DELETE CASCADE
);

CREATE TABLE post (
                      post_id INT AUTO_INCREMENT PRIMARY KEY,
                      member_id INT NOT NULL,
                      restaurant_id INT NOT NULL,
                      type VARCHAR(4) NOT NULL,
                      title VARCHAR(200) NOT NULL,
                      content TEXT NOT NULL,
                      view_count INT NOT NULL DEFAULT 0,
                      image VARCHAR(100) NOT NULL,
                      created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                      updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                      CONSTRAINT fk_post_member FOREIGN KEY (member_id) REFERENCES member(member_id) ON DELETE CASCADE,
                      CONSTRAINT fk_post_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurant(restaurant_id) ON DELETE CASCADE
);

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

ALTER TABLE post
    MODIFY COLUMN image TEXT;