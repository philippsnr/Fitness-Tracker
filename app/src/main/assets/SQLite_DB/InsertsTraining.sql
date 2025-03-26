-- ===============================
-- SAMPLE DATA FÜR TRAININGSPLÄNE
-- ===============================

-- Trainingsplan "Ganzkörper Anfänger"
-- Trainingstag 1 (Ganzkörper Tag 1) verwendet:
--   - Squats - Barbell (exercise_id = 39)
--   - Benchpress (exercise_id = 1)
--   - Pullups (exercise_id = 10)

-- Für Week 1 (2025-02-26)
-- Benchpress
INSERT INTO ExerciseSet (exercise_id, set_number, repetitions, weight, date) VALUES
(1, 1, 10, 60, "2025-02-26"),
(1, 2, 8, 65, "2025-02-26"),
(1, 3, 6, 70, "2025-02-26");

-- Squats - Barbell
INSERT INTO ExerciseSet (exercise_id, set_number, repetitions, weight, date) VALUES
(39, 1, 10, 100, "2025-02-26"),
(39, 2, 8, 110, "2025-02-26"),
(39, 3, 6, 120, "2025-02-26");

-- Pullups
INSERT INTO ExerciseSet (exercise_id, set_number, repetitions, weight, date) VALUES
(10, 1, 10, 0, "2025-02-26"),
(10, 2, 8, 0, "2025-02-26"),
(10, 3, 6, 0, "2025-02-26");

-- Trainingstag 2 (Ganzkörper Tag 2) verwendet:
--   - Lat-Pulldown (exercise_id = 11)
--   - Shoulderpress-Machine (exercise_id = 75)
--   - Wide T-Bar Row (exercise_id = 19)

-- Lat-Pulldown
INSERT INTO ExerciseSet (exercise_id, set_number, repetitions, weight, date) VALUES
(11, 1, 10, 50, "2025-02-26"),
(11, 2, 8, 55, "2025-02-26"),
(11, 3, 6, 60, "2025-02-26");

-- Shoulderpress-Machine
INSERT INTO ExerciseSet (exercise_id, set_number, repetitions, weight, date) VALUES
(75, 1, 10, 40, "2025-02-26"),
(75, 2, 8, 45, "2025-02-26"),
(75, 3, 6, 50, "2025-02-26");

-- Wide T-Bar Row
INSERT INTO ExerciseSet (exercise_id, set_number, repetitions, weight, date) VALUES
(19, 1, 10, 70, "2025-02-26"),
(19, 2, 8, 75, "2025-02-26"),
(19, 3, 6, 80, "2025-02-26");

-- Trainingstag 3 (Ganzkörper Tag 3) verwendet:
--   - Leg-Press (exercise_id = 43)
--   - Dips (exercise_id = 9)
--   - Face-Pulls (exercise_id = 74)

-- Leg-Press
INSERT INTO ExerciseSet (exercise_id, set_number, repetitions, weight, date) VALUES
(43, 1, 12, 200, "2025-02-26"),
(43, 2, 10, 210, "2025-02-26"),
(43, 3, 8, 220, "2025-02-26");

-- Dips
INSERT INTO ExerciseSet (exercise_id, set_number, repetitions, weight, date) VALUES
(9, 1, 10, 0, "2025-02-26"),
(9, 2, 8, 0, "2025-02-26"),
(9, 3, 6, 0, "2025-02-26");

-- Face-Pulls
INSERT INTO ExerciseSet (exercise_id, set_number, repetitions, weight, date) VALUES
(74, 1, 12, 20, "2025-02-26"),
(74, 2, 10, 25, "2025-02-26"),
(74, 3, 8, 30, "2025-02-26");


-- Trainingsplan "Push Pull Beine"
-- Trainingstag 4 (Push 1) verwendet:
--   - Benchpress (exercise_id = 1)
--   - Shoulderpress-Machine (exercise_id = 75)
--   - Dips (exercise_id = 9)

-- (Benchpress und Shoulderpress-Machine & Dips wurden bereits eingefügt, hier kannst du alternative Sätze oder weitere Wochen ergänzen)
-- Beispiel: Für Week 1 (2025-02-26) nochmal für Push 1:
INSERT INTO ExerciseSet (exercise_id, set_number, repetitions, weight, date) VALUES
(1, 1, 10, 60, "2025-02-26"),
(1, 2, 8, 65, "2025-02-26"),
(75, 1, 10, 40, "2025-02-26"),
(75, 2, 8, 45, "2025-02-26"),
(9, 1, 10, 0, "2025-02-26"),
(9, 2, 8, 0, "2025-02-26");

-- Trainingstag 5 (Pull 1) verwendet:
--   - Pullups (exercise_id = 10)
--   - Wide T-Bar Row (exercise_id = 19)
--   - Face-Pulls (exercise_id = 74)
-- (Auch hier wurden bereits Sätze für Pullups, Wide T-Bar Row und Face-Pulls eingefügt)

-- Trainingstag 6 (Beine 1) verwendet:
--   - Squats - Barbell (exercise_id = 39)
--   - Pullups (exercise_id = 10)
--   - Leg-Press (exercise_id = 43)
-- (Für Beine 1 sind Sätze für Squats, Pullups und Leg-Press bereits vorhanden)

-- Trainingstag 7 (Push 2) verwendet:
--   - Benchpress (exercise_id = 1)
--   - Shoulderpress-Machine (exercise_id = 75)
--   - Dips (exercise_id = 9)
-- Trainingstag 8 (Pull 2) verwendet:
--   - Pullups (exercise_id = 10)
--   - Wide T-Bar Row (exercise_id = 19)
--   - Face-Pulls (exercise_id = 74)
-- Trainingstag 9 (Beine 2) verwendet:
--   - Squats - Barbell (exercise_id = 39)
--   - Romanian Deadlift (exercise_id = 35)
--   - Leg-Press (exercise_id = 43)

-- Für den Trainingsplan "Push Pull Beine" fügen wir für Week 2 (2025-03-05) alternative Sätze ein:

-- Benchpress (exercise_id = 1) Week 2
INSERT INTO ExerciseSet (exercise_id, set_number, repetitions, weight, date) VALUES
(1, 1, 10, 62, "2025-03-05"),
(1, 2, 8, 67, "2025-03-05"),
(1, 3, 6, 72, "2025-03-05");

-- Squats - Barbell (exercise_id = 39) Week 2
INSERT INTO ExerciseSet (exercise_id, set_number, repetitions, weight, date) VALUES
(39, 1, 10, 102, "2025-03-05"),
(39, 2, 8, 112, "2025-03-05"),
(39, 3, 6, 122, "2025-03-05");

-- Pullups (exercise_id = 10) Week 2
INSERT INTO ExerciseSet (exercise_id, set_number, repetitions, weight, date) VALUES
(10, 1, 10, 0, "2025-03-05"),
(10, 2, 8, 0, "2025-03-05"),
(10, 3, 6, 0, "2025-03-05");

-- Lat-Pulldown (exercise_id = 11) Week 2
INSERT INTO ExerciseSet (exercise_id, set_number, repetitions, weight, date) VALUES
(11, 1, 10, 52, "2025-03-05"),
(11, 2, 8, 57, "2025-03-05"),
(11, 3, 6, 62, "2025-03-05");

-- Shoulderpress-Machine (exercise_id = 75) Week 2
INSERT INTO ExerciseSet (exercise_id, set_number, repetitions, weight, date) VALUES
(75, 1, 10, 42, "2025-03-05"),
(75, 2, 8, 47, "2025-03-05"),
(75, 3, 6, 52, "2025-03-05");

-- Wide T-Bar Row (exercise_id = 19) Week 2
INSERT INTO ExerciseSet (exercise_id, set_number, repetitions, weight, date) VALUES
(19, 1, 10, 72, "2025-03-05"),
(19, 2, 8, 77, "2025-03-05"),
(19, 3, 6, 82, "2025-03-05");

-- Leg-Press (exercise_id = 43) Week 2
INSERT INTO ExerciseSet (exercise_id, set_number, repetitions, weight, date) VALUES
(43, 1, 12, 202, "2025-03-05"),
(43, 2, 10, 212, "2025-03-05"),
(43, 3, 8, 222, "2025-03-05");

-- Dips (exercise_id = 9) Week 2
INSERT INTO ExerciseSet (exercise_id, set_number, repetitions, weight, date) VALUES
(9, 1, 10, 0, "2025-03-05"),
(9, 2, 8, 0, "2025-03-05"),
(9, 3, 6, 0, "2025-03-05");

-- Face-Pulls (exercise_id = 74) Week 2
INSERT INTO ExerciseSet (exercise_id, set_number, repetitions, weight, date) VALUES
(74, 1, 12, 22, "2025-03-05"),
(74, 2, 10, 27, "2025-03-05"),
(74, 3, 8, 32, "2025-03-05");

-- Romanian Deadlift (exercise_id = 35) Week 2
INSERT INTO ExerciseSet (exercise_id, set_number, repetitions, weight, date) VALUES
(35, 1, 10, 82, "2025-03-05"),
(35, 2, 8, 87, "2025-03-05"),
(35, 3, 6, 92, "2025-03-05");
