INSERT INTO `user_shifts` (`user_id`, `shift_id`, `created_date`)
SELECT u.id, s.id, CURRENT_TIMESTAMP
FROM `user` u
CROSS JOIN `shift_details` s
WHERE s.id IN (2, 3);