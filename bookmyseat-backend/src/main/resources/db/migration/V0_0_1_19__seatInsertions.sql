INSERT INTO `seat` (`id`, `seat_number`, `floor_id`, `restriction_id`)
SELECT
    @id := @id + 1 AS id,
    CASE
        WHEN floor_number = 1 THEN seat_number
        WHEN floor_number = 2 THEN (seat_number - 120) % 48 + 1
        WHEN floor_number = 3 THEN (seat_number - 168) % 123 + 1
        WHEN floor_number = 4 THEN (seat_number - 291) % 123 + 1
        WHEN floor_number = 5 THEN (seat_number - 414) % 93 + 1
        WHEN floor_number = 6 THEN (seat_number - 507) % 28 + 1
    END AS seat_number,
    floor_number AS floor_id,
    NULL AS restriction_id
FROM (
    SELECT
        ROW_NUMBER() OVER () AS seat_number,
        CASE
            WHEN ROW_NUMBER() OVER () BETWEEN 1 AND 119 THEN 1
            WHEN ROW_NUMBER() OVER () BETWEEN 120 AND 167 THEN 2
            WHEN ROW_NUMBER() OVER () BETWEEN 168 AND 290 THEN 3
            WHEN ROW_NUMBER() OVER () BETWEEN 291 AND 413 THEN 4
            WHEN ROW_NUMBER() OVER () BETWEEN 414 AND 506 THEN 5
            WHEN ROW_NUMBER() OVER () BETWEEN 507 AND 534 THEN 6
        END AS floor_number
    FROM INFORMATION_SCHEMA.COLUMNS
    LIMIT 534
) AS seat_numbers
CROSS JOIN (SELECT @id := 0) AS init;