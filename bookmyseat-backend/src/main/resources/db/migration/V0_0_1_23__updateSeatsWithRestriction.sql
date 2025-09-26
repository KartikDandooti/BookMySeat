UPDATE `seat` SET `restriction_id`=1 WHERE `id` BETWEEN 120 AND 143;
UPDATE `seat` SET `restriction_id`=2 WHERE `id` BETWEEN 144 AND 167;
UPDATE `seat` SET `restriction_id`=3 WHERE `floor_id`=3 and `seat_number`=2;