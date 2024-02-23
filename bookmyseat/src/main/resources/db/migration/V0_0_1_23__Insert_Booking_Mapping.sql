INSERT INTO booking_mapping 
(booking_date, booking_id, additional_desktop, tea_coffee, lunch, parking, parking_type, tea_coffee_type, modified_date, marked_status)
VALUES 
(CURRENT_DATE(), 1, b'1', b'1', b'1', b'0', NULL, 'COFFEE',NULL, b'0'),
(CURRENT_DATE(), 2, b'0', b'1', b'1', b'1', 'TWO_WHEELER', 'COFFEE', NULL, b'0'),
(CURRENT_DATE(), 3, b'1', b'1', b'1', b'0', NULL, 'COFFEE', NULL, b'0'),
(CURRENT_DATE(), 4, b'0', b'1', b'1', b'1', 'FOUR_WHEELER', 'COFFEE', NULL, b'0'),
(CURRENT_DATE(), 5, b'1', b'1', b'1', b'0', NULL, 'COFFEE', NULL, b'0'), 
(DATE_ADD(CURRENT_DATE(), INTERVAL 1 DAY), 5, b'1', b'1', b'1', b'1', 'TWO_WHEELER', 'COFFEE', NULL, b'0'),
(DATE_ADD(CURRENT_DATE(), INTERVAL 2 DAY), 5, b'1', b'1', b'1', b'1', 'TWO_WHEELER', 'COFFEE', NULL, b'0'),
(DATE_ADD(CURRENT_DATE(), INTERVAL 3 DAY), 5, b'1', b'1', b'1', b'1', 'TWO_WHEELER', 'COFFEE', NULL, b'0'),
(DATE_ADD(CURRENT_DATE(), INTERVAL 4 DAY), 5, b'1', b'1', b'1', b'1', 'TWO_WHEELER', 'COFFEE', NULL, b'0'),
(DATE_ADD(CURRENT_DATE(), INTERVAL 5 DAY), 5, b'1', b'1', b'1', b'1', 'TWO_WHEELER', 'COFFEE', NULL, b'0'),
(DATE_ADD(CURRENT_DATE(), INTERVAL 6 DAY), 5, b'1', b'1', b'1', b'1', 'TWO_WHEELER', 'COFFEE', NULL, b'0'),
(CURRENT_DATE(), 6, b'1', b'1', b'1', b'1', 'FOUR_WHEELER', 'COFFEE', NULL, b'0'); 