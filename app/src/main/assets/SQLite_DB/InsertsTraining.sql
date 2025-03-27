-- 1. Trainingspläne einfügen
INSERT INTO Trainingplan (name, isActive) VALUES ('Ganzkörper', 1);
INSERT INTO Trainingplan (name, isActive) VALUES ('Push Pull Legs', 0);

-- 2. Trainingstage anlegen

-- Trainingstage für den "Ganzkörper"-Plan (angenommene Trainingplan-ID = 1)
INSERT INTO Trainingday (name, Trainingplan_id) VALUES ('Ganzkörper Tag 1', 1);
INSERT INTO Trainingday (name, Trainingplan_id) VALUES ('Ganzkörper Tag 2', 1);
INSERT INTO Trainingday (name, Trainingplan_id) VALUES ('Ganzkörper Tag 3', 1);

-- Trainingstage für den "Push Pull Legs"-Plan (angenommene Trainingplan-ID = 2)
INSERT INTO Trainingday (name, Trainingplan_id) VALUES ('Push Tag', 2);
INSERT INTO Trainingday (name, Trainingplan_id) VALUES ('Pull Tag', 2);
INSERT INTO Trainingday (name, Trainingplan_id) VALUES ('Legs Tag', 2);
INSERT INTO Trainingday (name, Trainingplan_id) VALUES ('Push Tag 2', 2);
INSERT INTO Trainingday (name, Trainingplan_id) VALUES ('Pull Tag 2', 2);
INSERT INTO Trainingday (name, Trainingplan_id) VALUES ('Legs Tag 2', 2);

-- 3. Übungen den Trainingstagen zuweisen

-- Für "Ganzkörper"
INSERT INTO TrainingdayExerciseAssignment (Trainingday_id, Exercise_id) VALUES
  (1, 1), (1, 2), (1, 3), (1, 4);
INSERT INTO TrainingdayExerciseAssignment (Trainingday_id, Exercise_id) VALUES
  (2, 5), (2, 6), (2, 7), (2, 8);
INSERT INTO TrainingdayExerciseAssignment (Trainingday_id, Exercise_id) VALUES
  (3, 9), (3, 10), (3, 11), (3, 12);

-- Für "Push Pull Legs"
INSERT INTO TrainingdayExerciseAssignment (Trainingday_id, Exercise_id) VALUES
  (4, 1), (4, 2), (4, 44), (4, 45);
INSERT INTO TrainingdayExerciseAssignment (Trainingday_id, Exercise_id) VALUES
  (5, 10), (5, 11), (5, 12), (5, 13);
INSERT INTO TrainingdayExerciseAssignment (Trainingday_id, Exercise_id) VALUES
  (6, 39), (6, 40), (6, 41), (6, 43);
INSERT INTO TrainingdayExerciseAssignment (Trainingday_id, Exercise_id) VALUES
  (7, 76), (7, 77), (7, 78), (7, 79);
INSERT INTO TrainingdayExerciseAssignment (Trainingday_id, Exercise_id) VALUES
  (8, 49), (8, 50), (8, 51), (8, 52);
INSERT INTO TrainingdayExerciseAssignment (Trainingday_id, Exercise_id) VALUES
  (9, 31), (9, 35), (9, 36), (9, 37);

-- 4. Beispielhafte ExerciseSet-Einträge

-- Für Benchpress (Exercise ID 1) am 25.03.2025: 3 Sätze
INSERT INTO ExerciseSet (exercise_id, set_number, repetitions, weight, date) VALUES
  (1, 1, 10, 80, '2025-03-25');
INSERT INTO ExerciseSet (exercise_id, set_number, repetitions, weight, date) VALUES
  (1, 2, 8, 85, '2025-03-25');
INSERT INTO ExerciseSet (exercise_id, set_number, repetitions, weight, date) VALUES
  (1, 3, 6, 90, '2025-03-25');

-- Für Dumbell-Benchpress (Exercise ID 2) am 26.03.2025: 4 Sätze
INSERT INTO ExerciseSet (exercise_id, set_number, repetitions, weight, date) VALUES
  (2, 1, 12, 50, '2025-03-26');
INSERT INTO ExerciseSet (exercise_id, set_number, repetitions, weight, date) VALUES
  (2, 2, 10, 55, '2025-03-26');
INSERT INTO ExerciseSet (exercise_id, set_number, repetitions, weight, date) VALUES
  (2, 3, 8, 60, '2025-03-26');
INSERT INTO ExerciseSet (exercise_id, set_number, repetitions, weight, date) VALUES
  (2, 4, 6, 65, '2025-03-26');

INSERT INTO ExerciseSet (exercise_id, set_number, repetitions, weight, date) VALUES
  (2, 1, 12, 50, '2025-03-29');
INSERT INTO ExerciseSet (exercise_id, set_number, repetitions, weight, date) VALUES
  (2, 2, 10, 55, '2025-03-29');
INSERT INTO ExerciseSet (exercise_id, set_number, repetitions, weight, date) VALUES
  (2, 3, 8, 60, '2025-03-29');

-- Für Tricep-Pushdowns (Exercise ID 44) am 27.03.2025: 2 Sätze
INSERT INTO ExerciseSet (exercise_id, set_number, repetitions, weight, date) VALUES
  (44, 1, 15, 30, '2025-03-27');
INSERT INTO ExerciseSet (exercise_id, set_number, repetitions, weight, date) VALUES
  (44, 2, 12, 35, '2025-03-27');

-- Für Squats - Barbell (Exercise ID 39) am 28.03.2025: 4 Sätze
INSERT INTO ExerciseSet (exercise_id, set_number, repetitions, weight, date) VALUES
  (39, 1, 10, 100, '2025-03-28');
INSERT INTO ExerciseSet (exercise_id, set_number, repetitions, weight, date) VALUES
  (39, 2, 8, 110, '2025-03-28');
INSERT INTO ExerciseSet (exercise_id, set_number, repetitions, weight, date) VALUES
  (39, 3, 6, 120, '2025-03-28');
INSERT INTO ExerciseSet (exercise_id, set_number, repetitions, weight, date) VALUES
  (39, 4, 4, 130, '2025-03-28');

-- Für Dumbell-Shoulderpress (Exercise ID 76) am 29.03.2025: 3 Sätze
INSERT INTO ExerciseSet (exercise_id, set_number, repetitions, weight, date) VALUES
  (76, 1, 10, 20, '2025-03-29');
INSERT INTO ExerciseSet (exercise_id, set_number, repetitions, weight, date) VALUES
  (76, 2, 10, 20, '2025-03-29');
INSERT INTO ExerciseSet (exercise_id, set_number, repetitions, weight, date) VALUES
  (76, 3, 8, 22, '2025-03-29');

-- Für Split-Squats (Exercise ID 31) am 30.03.2025: 3 Sätze
INSERT INTO ExerciseSet (exercise_id, set_number, repetitions, weight, date) VALUES
  (31, 1, 12, 40, '2025-03-30');
INSERT INTO ExerciseSet (exercise_id, set_number, repetitions, weight, date) VALUES
  (31, 2, 10, 45, '2025-03-30');
INSERT INTO ExerciseSet (exercise_id, set_number, repetitions, weight, date) VALUES
  (31, 3, 8, 50, '2025-03-30');

-- Für Preacher Curls - Barbell (Exercise ID 50) am 31.03.2025: 2 Sätze
INSERT INTO ExerciseSet (exercise_id, set_number, repetitions, weight, date) VALUES
  (50, 1, 10, 30, '2025-03-31');
INSERT INTO ExerciseSet (exercise_id, set_number, repetitions, weight, date) VALUES
  (50, 2, 8, 32, '2025-03-31');
