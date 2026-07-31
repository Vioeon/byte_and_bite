USE bytebite;

INSERT INTO member (email, password, nickname, role, created_at) VALUES
    ('manager1@likelion.net', '12341234', 'manager1', 'MANAGER', '2026-07-27 12:00:00'),
    ('manager2@likelion.net', '12341234', 'manager2', 'MANAGER', '2026-07-27 12:30:00'),
    ('manager3@likelion.net', '12341234', 'manager3', 'MANAGER', '2026-07-27 13:00:00'),
    ('manager4@likelion.net', '12341234', 'manager4', 'MANAGER', '2026-07-27 13:30:00'),
    ('manager5@likelion.net', '12341234', 'manager6', 'MANAGER', '2026-07-27 14:00:00'),
    ('user1@likelion.net', '12341234', 'user1', 'USER', '2026-07-27 14:30:00'),
    ('user2@likelion.net', '12341234', 'user2', 'USER', '2026-07-27 15:00:00'),
    ('user3@likelion.net', '12341234', 'user3', 'USER', '2026-07-27 15:30:00'),
    ('user4@likelion.net', '12341234', 'user4', 'USER', '2026-07-27 16:00:00'),
    ('user5@likelion.net', '12341234', 'user5', 'USER', '2026-07-27 16:30:00');

INSERT INTO restaurant (member_id, category, rname, address, phone, image) VALUES
    (1, 'KOR', '진국', '서울특별시 강남구 역삼동 1', '02-123-4567', 'default.jpg'),
    (2, 'WST', '파스타집', '서울특별시 종로구 원서동 33', '02-345-6789', 'default.jpg'),
    (3, 'CHN', '홍반점', '서울특별시 용산구 이태원동 44', '02-456-7890', 'default.jpg'),
    (4, 'JPN', '스시야', '서울특별시 마포구 서교동 22', '02-234-5678', 'default.jpg'),
    (5, 'ETC', '분식왕', '서울특별시 성동구 성수동 55', '02-567-8901', 'default.jpg');

INSERT INTO post (member_id, restaurant_id, type, title, content, view_count, image, created_at) VALUES
    (1, 1, 'NEWS', '진국 영업시간 안내', '평일은 오전 11시부터 영업합니다.', 7, 'default.jpg', '2026-07-28 13:00:00'),
    (2, 2, 'NEWS', '파스타집 신메뉴 출시', '크림파스타 신메뉴가 나왔습니다.', 33, 'default.jpg', '2026-07-28 15:00:00'),
    (3, 3, 'NEWS', '홍반점 휴무 공지', '이번 주 화요일은 휴무입니다.', 12, 'default.jpg', '2026-07-28 14:00:00'),
    (4, 4, 'NEWS', '스시야 재료 입고', '오늘 신선한 참치가 입고됐습니다.', 21, 'default.jpg', '2026-07-28 16:00:00'),
    (5, 5, 'NEWS', '분식왕 이벤트 안내', '떡볶이 1+1 이벤트 진행 중입니다.', 14, 'default.jpg', '2026-07-28 12:00:00'),
    (6, 2, 'POST', '파스타집 다녀왔어요', '크림파스타가 인생 최고였어요.', 9, 'default.jpg', '2026-07-29 11:00:00'),
    (7, 4, 'POST', '스시야 초밥 최고', '재료가 신선하고 사장님이 친절해요.', 17, 'default.jpg', '2026-07-29 13:00:00'),
    (8, 1, 'POST', '진국 후기', '국물이 진하고 맛있어요. 재방문 의사 있습니다.', 5, 'default.jpg', '2026-07-29 10:00:00'),
    (9, 5, 'POST', '분식왕 떡볶이 후기', '매콤달콤 딱 좋아요. 다음에 또 갈게요.', 8, 'default.jpg', '2026-07-29 14:00:00'),
    (10, 3, 'POST', '홍반점 솔직 후기', '양은 많은데 조금 짠 편이었어요.', 3, 'default.jpg', '2026-07-29 12:00:00');;

INSERT INTO reply (post_id, member_id, content, created_at) VALUES
    (6, 1, '저도 여기 자주 가는데 크림파스타 진짜 맛있죠!', '2026-07-29 11:30:00'),
    (6, 3, '다음에 꼭 가봐야겠어요.', '2026-07-29 12:10:00'),
    (8, 5, '국물 진짜 진하긴 하죠 ㅎㅎ', '2026-07-29 10:45:00'),
    (9, 2, '맛있겠다 저도 먹고 싶네요.', '2026-07-29 14:20:00'),
    (9, 6, '이 집 떡볶이 양도 많아요!', '2026-07-29 15:00:00'),
    (9, 7, '주말엔 웨이팅 있던데 평일이 나은가요?', '2026-07-29 15:30:00');

SELECT post_id, title, created_at FROM post WHERE type = 'NEWS';

SELECT * FROM reply WHERE post_id = 9;