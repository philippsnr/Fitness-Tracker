-- Erstelle den Trainingsplan
INSERT INTO Trainingplan (name, isActive) VALUES ("Ganzkörper Anfänger", 0);

-- IDs der Trainingstage speichern
INSERT INTO Trainingday (name, Trainingplan_id) VALUES
("Ganzkörper Tag 1", 1),
("Ganzkörper Tag 2", 1),
("Ganzkörper Tag 3", 1);

-- Beispiel-Übungszuweisungen für Ganzkörper Tag 1
INSERT INTO TrainingdayExerciseAssignment (Trainingday_id, Exercise_id) VALUES
(1, 39), -- Kniebeugen
(1, 1), -- Bankdrücken
(1, 10); -- Klimmzüge

-- Beispiel-Übungszuweisungen für Ganzkörper Tag 2
INSERT INTO TrainingdayExerciseAssignment (Trainingday_id, Exercise_id) VALUES
(2, 11), -- LatPulldown
(2, 75), -- Schulterdrücken
(2, 19); -- Rudern

-- Beispiel-Übungszuweisungen für Ganzkörper Tag 3
INSERT INTO TrainingdayExerciseAssignment (Trainingday_id, Exercise_id) VALUES
(3, 43), -- Beinpresse
(3, 9), -- Dips
(3, 74); -- Face Pulls


-- Erstelle den Trainingsplan
INSERT INTO Trainingplan (name, isActive) VALUES ("Push Pull Beine", 1);

-- IDs der Trainingstage speichern
INSERT INTO Trainingday (name, Trainingplan_id) VALUES
("Push 1", 2),
("Pull 1", 2),
("Beine 1", 2),
("Push 2", 2),
("Pull 2", 2),
("Beine 2", 2);

-- Push 1
INSERT INTO TrainingdayExerciseAssignment (Trainingday_id, Exercise_id) VALUES
(4, 1), -- Bankdrücken
(4, 75), -- Schulterdrücken
(4, 9); -- Dips

-- Pull 1
INSERT INTO TrainingdayExerciseAssignment (Trainingday_id, Exercise_id) VALUES
(5, 10), -- Klimmzüge
(5, 19), -- Rudern
(5, 74); -- Face Pulls

-- Beine 1
INSERT INTO TrainingdayExerciseAssignment (Trainingday_id, Exercise_id) VALUES
(6, 39), -- Kniebeugen
(6, 10), -- pullups
(6, 43); -- Beinpresse

-- Push 2
INSERT INTO TrainingdayExerciseAssignment (Trainingday_id, Exercise_id) VALUES
(7, 1), -- Bankdrücken
(7, 75), -- Schulterdrücken
(7, 9); -- Dips

-- Pull 2
INSERT INTO TrainingdayExerciseAssignment (Trainingday_id, Exercise_id) VALUES
(8, 10), -- Klimmzüge
(8, 19), -- Rudern
(8, 74); -- Face Pulls

-- Beine 2
INSERT INTO TrainingdayExerciseAssignment (Trainingday_id, Exercise_id) VALUES
(9, 39), -- Kniebeugen
(9, 35), -- rumanian deadlift
(9, 43); -- Beinpresse

-- Week 1 (26-02-2025)
-- Bankdrücken (Bench Press) - 3 Sets
INSERT INTO ExerciseSet (TrainingdayExerciseAssignment_id, set_number, repetitions, weight, date) VALUES
(4, 1, 10, 60, "26-02-2025"),
(4, 2, 8, 65, "26-02-2025"),
(4, 3, 6, 70, "26-02-2025"),
(4, 2, 8, 65, "26-02-2025"),
(4, 3, 6, 70, "26-02-2025");

-- Klimmzüge (Pull-Ups) - 3 Sets
INSERT INTO ExerciseSet (TrainingdayExerciseAssignment_id, set_number, repetitions, weight, date) VALUES
(5, 1, 10, 0, "26-02-2025"),
(5, 2, 8, 5, "26-02-2025"),
(5, 3, 6, 10, "26-02-2025"),
(5, 3, 6, 10, "26-02-2025");

-- Beinpresse (Leg Press) - 3 Sets
INSERT INTO ExerciseSet (TrainingdayExerciseAssignment_id, set_number, repetitions, weight, date) VALUES
(9, 1, 12, 100, "26-02-2025"),
(9, 2, 10, 110, "26-02-2025"),
(9, 3, 8, 120, "26-02-2025");

--------------------------------------------------

-- Week 2 (05-03-2025)
-- Bankdrücken (Bench Press) - 3 Sets
INSERT INTO ExerciseSet (TrainingdayExerciseAssignment_id, set_number, repetitions, weight, date) VALUES
(4, 1, 10, 62, "05-03-2025"),
(4, 2, 8, 67, "05-03-2025"),
(4, 3, 6, 72, "05-03-2025");

-- Klimmzüge (Pull-Ups) - 3 Sets
INSERT INTO ExerciseSet (TrainingdayExerciseAssignment_id, set_number, repetitions, weight, date) VALUES
(5, 1, 10, 0, "05-03-2025"),
(5, 3, 6, 10, "05-03-2025");

-- Beinpresse (Leg Press) - 3 Sets
INSERT INTO ExerciseSet (TrainingdayExerciseAssignment_id, set_number, repetitions, weight, date) VALUES
(9, 1, 12, 105, "05-03-2025"),
(9, 2, 10, 115, "05-03-2025");

--------------------------------------------------

-- Week 3 (12-03-2025)
-- Bankdrücken (Bench Press) - 3 Sets
INSERT INTO ExerciseSet (TrainingdayExerciseAssignment_id, set_number, repetitions, weight, date) VALUES
(4, 1, 10, 64, "12-03-2025"),
(4, 2, 8, 69, "12-03-2025"),
(4, 3, 6, 74, "12-03-2025"),
(4, 1, 10, 64, "12-03-2025"),
(4, 2, 8, 69, "12-03-2025"),
(4, 2, 8, 69, "12-03-2025"),
(4, 3, 6, 74, "12-03-2025");

-- Klimmzüge (Pull-Ups) - 3 Sets
INSERT INTO ExerciseSet (TrainingdayExerciseAssignment_id, set_number, repetitions, weight, date) VALUES
(5, 1, 10, 0, "12-03-2025"),
(5, 2, 8, 5, "12-03-2025"),
(5, 3, 6, 10, "12-03-2025"),
(5, 1, 10, 0, "12-03-2025"),
(5, 2, 8, 5, "12-03-2025"),
(5, 3, 6, 10, "12-03-2025");

-- Beinpresse (Leg Press) - 3 Sets
INSERT INTO ExerciseSet (TrainingdayExerciseAssignment_id, set_number, repetitions, weight, date) VALUES
(9, 1, 12, 110, "12-03-2025"),
(9, 2, 10, 120, "12-03-2025"),
(9, 3, 8, 130, "12-03-2025");

--------------------------------------------------

-- Week 4 (19-03-2025)
-- Bankdrücken (Bench Press) - 3 Sets
INSERT INTO ExerciseSet (TrainingdayExerciseAssignment_id, set_number, repetitions, weight, date) VALUES
(4, 1, 10, 66, "19-03-2025"),
(4, 2, 8, 71, "19-03-2025"),
(4, 3, 6, 76, "19-03-2025"),
(4, 2, 8, 71, "19-03-2025"),
(4, 3, 6, 76, "19-03-2025");

-- Klimmzüge (Pull-Ups) - 3 Sets
INSERT INTO ExerciseSet (TrainingdayExerciseAssignment_id, set_number, repetitions, weight, date) VALUES
(5, 1, 10, 0, "19-03-2025"),
(5, 2, 8, 5, "19-03-2025"),
(5, 3, 6, 10, "19-03-2025"),
(5, 2, 8, 5, "19-03-2025"),
(5, 3, 6, 10, "19-03-2025");

-- Beinpresse (Leg Press) - 3 Sets
INSERT INTO ExerciseSet (TrainingdayExerciseAssignment_id, set_number, repetitions, weight, date) VALUES
(9, 1, 12, 115, "19-03-2025"),
(9, 2, 10, 125, "19-03-2025"),
(9, 3, 8, 135, "19-03-2025");

--------------------------------------------------

-- Week 5 (26-03-2025)
-- Bankdrücken (Bench Press) - 3 Sets
INSERT INTO ExerciseSet (TrainingdayExerciseAssignment_id, set_number, repetitions, weight, date) VALUES
(4, 1, 10, 68, "26-03-2025"),
(4, 2, 8, 73, "26-03-2025"),
(4, 3, 6, 78, "26-03-2025"),
(4, 2, 8, 73, "26-03-2025"),
(4, 3, 6, 78, "26-03-2025");

-- Klimmzüge (Pull-Ups) - 3 Sets
INSERT INTO ExerciseSet (TrainingdayExerciseAssignment_id, set_number, repetitions, weight, date) VALUES
(5, 1, 10, 0, "26-03-2025"),
(5, 3, 6, 10, "26-03-2025");

-- Beinpresse (Leg Press) - 3 Sets
INSERT INTO ExerciseSet (TrainingdayExerciseAssignment_id, set_number, repetitions, weight, date) VALUES
(9, 1, 12, 120, "26-03-2025"),
(9, 2, 10, 130, "26-03-2025"),
(9, 3, 8, 140, "26-03-2025"),
(9, 3, 8, 140, "26-03-2025");
