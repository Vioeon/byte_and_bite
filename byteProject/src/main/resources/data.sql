-- category (1:한식 2:일식 3:양식 4:중식 5:기타)
INSERT INTO member (email, password, nickname, role, created_at) VALUES
    ('apple@likelion.net', '1234', 'apple', 1, CURRENT_TIMESTAMP),
    ('banana@likelion.net', '1234', 'banana', 1, CURRENT_TIMESTAMP),
    ('orange@likelion.net', '1234', 'orange', 1, CURRENT_TIMESTAMP),
    ('peach@likelion.net', '1234', 'peach', 1, CURRENT_TIMESTAMP),
    ('tomato@likelion.net', '1234', 'tomato', 1, CURRENT_TIMESTAMP);


INSERT INTO restaurant
(member_id, category, rname, address, phone, image)
VALUES
    (1, 1, '교촌치킨', '서울시', '010-1111-1111', 'test.jpg'),
    (2, 1, 'BBQ 역삼점', '서울시 강남구', '010-2222-2222', 'bbq.jpg'),
    (3, 1, 'BHC 선릉점', '서울시 강남구', '010-3333-3333', 'bhc.jpg');

INSERT INTO post
(member_id, restaurant_id, title, content, image, created_at, updated_at)
VALUES
    (1, 1, '교촌치킨 허니콤보',
     '111',
     'test.jpg',
     CURRENT_TIMESTAMP,
     CURRENT_TIMESTAMP),

    (2, 2, 'BBQ 황금올리브',
     '111',
     'bbq.jpg',
     CURRENT_TIMESTAMP,
     CURRENT_TIMESTAMP);