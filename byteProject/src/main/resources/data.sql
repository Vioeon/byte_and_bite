USE bytebite;

INSERT INTO member (email, password, nickname, role, created_at) VALUES
                                                                     ('apple@likelion.net', '1234', 'apple', 'MANAGER', CURRENT_TIMESTAMP),
                                                                     ('banana@likelion.net', '1234', 'banana', 'MANAGER', CURRENT_TIMESTAMP),
                                                                     ('orange@likelion.net', '1234', 'orange', 'MANAGER', CURRENT_TIMESTAMP),
                                                                     ('peach@likelion.net', '1234', 'peach', 'MANAGER', CURRENT_TIMESTAMP),
                                                                     ('tomato@likelion.net', '1234', 'tomato', 'MANAGER', CURRENT_TIMESTAMP);

INSERT INTO restaurant (member_id, category, rname, address, phone, image) VALUES
                                                                               (1, 'KOR', '진국', '서울특별시 강남구 역삼동 1', '02-123-4567', 'default.jpg'),
                                                                               (2, 'WST', '파스타집', '서울특별시 종로구 원서동 33', '02-345-6789', 'default.jpg'),
                                                                               (3, 'CHN', '홍반점', '서울특별시 용산구 이태원동 44', '02-456-7890', 'default.jpg'),
                                                                               (4, 'JPN', '스시야', '서울특별시 마포구 서교동 22', '02-234-5678', 'default.jpg'),
                                                                               (5, 'ETC', '분식왕', '서울특별시 성동구 성수동 55', '02-567-8901', 'default.jpg');

INSERT INTO news (member_id, restaurant_id, title, content, view_count, image) VALUES
                                                                                   (1, 1, '진국 영업시간 안내', '평일은 오전 11시부터 영업합니다.', 0, 'default.jpg'),
                                                                                   (2, 2, '파스타집 신메뉴 출시', '크림파스타 신메뉴가 나왔습니다.', 0, 'default.jpg'),
                                                                                   (3, 3, '홍반점 휴무 공지', '이번 주 화요일은 휴무입니다.', 0, 'default.jpg'),
                                                                                   (4, 4, '스시야 재료 입고', '오늘 신선한 참치가 입고됐습니다.', 0, 'default.jpg'),
                                                                                   (5, 5, '분식왕 이벤트 안내', '떡볶이 1+1 이벤트 진행 중입니다.', 0, 'default.jpg');


