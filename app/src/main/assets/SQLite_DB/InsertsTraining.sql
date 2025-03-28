-- Insert Trainingplans
INSERT INTO Trainingplan (id, name) VALUES
(1, 'Full Body (2x pro Woche)'),
(2, 'Upper/Lower, Fullbody (3x pro Woche)'),
(3, 'Upper/Lower (4x pro Woche)'),
(4, 'Push, Pull, Legs, Upper, Lower (5x pro Woche)'),
(5, 'Push, Pull, Legs (6x pro Woche)'),
(6, 'Full Body (1x pro Woche)'),;

-- Insert Trainingdays
INSERT INTO Trainingday (id, name, Trainingplan_id) VALUES
-- Plan 1
(1, 'Full Body A', 1),
(2, 'Full Body B', 1),

-- Plan 2
(3, 'Upper Body', 2),
(4, 'Lower Body', 2),
(5, 'Full Body', 2),

-- Plan 3
(6, 'Upper Body A', 3),
(7, 'Lower Body A', 3),
(8, 'Upper Body B', 3),
(9, 'Lower Body B', 3),

-- Plan 4
(10, 'Push Day', 4),
(11, 'Pull Day', 4),
(12, 'Leg Day', 4),
(13, 'Upper Body', 4),
(14, 'Lower Body', 4),

-- Plan 5
(15, 'Push Day A', 5),
(16, 'Pull Day A', 5),
(17, 'Leg Day A', 5),
(18, 'Push Day B', 5),
(19, 'Pull Day B', 5),
(20, 'Leg Day B', 5),

--Plan 6
(1, 'Full Body', 6);


-- Insert Exercise Assignments
INSERT INTO TrainingdayExerciseAssignment (Trainingday_id, Exercise_id) VALUES
-- Plan 1
(1, 35), (1, 3), (1, 10), (1, 42), (1, 82),  -- Full Body A
(2, 37), (2, 39), (2, 5), (2, 19), (2, 86),  -- Full Body B

-- Plan 2
(3, 1), (3, 5), (3, 11), (3, 20), (3, 71), (3, 67), (3, 44), (3, 53),  -- Upper Body
(4, 36), (4, 39), (4, 35), (4, 42), (4, 32), (4, 23), (4, 82),         -- Lower Body
(5, 37), (5, 29), (5, 4), (5, 19), (5, 86),                           -- Full Body

-- Plan 3
(6, 1), (6, 5), (6, 11), (6, 20), (6, 71), (6, 67), (6, 44), (6, 53),  -- Upper A
(7, 36), (7, 39), (7, 35), (7, 42), (7, 32), (7, 23), (7, 82),         -- Lower A
(8, 1), (8, 5), (8, 11), (8, 19), (8, 71), (8, 67), (8, 79), (8, 56),  -- Upper B
(9, 37), (9, 29), (9, 35), (9, 42), (9, 32), (9, 26), (9, 27),         -- Lower B

-- Plan 4
(10, 5), (10, 3), (10, 8), (10, 67), (10, 44), (10, 79), (10, 83),     -- Push Day
(11, 10), (11, 18), (11, 23), (11, 71), (11, 56), (11, 49), (11, 82),  -- Pull Day
(12, 37), (12, 40), (12, 35), (12, 42), (12, 32), (12, 26), (12, 27),  -- Leg Day
(13, 1), (13, 5), (13, 11), (13, 20), (13, 71), (13, 67), (13, 44), (13, 53),  -- Upper
(14, 36), (14, 39), (14, 35), (14, 42), (14, 32), (14, 23), (14, 82),          -- Lower

-- Plan 5
(15, 5), (15, 3), (15, 2), (15, 67), (15, 44), (15, 79), (15, 83),     -- Push A
(16, 10), (16, 18), (16, 23), (16, 71), (16, 56), (16, 49), (16, 82),  -- Pull A
(17, 37), (17, 40), (17, 35), (17, 42), (17, 32), (17, 26), (17, 27),  -- Leg A
(18, 5), (18, 4), (18, 8), (18, 67), (18, 44), (18, 79), (18, 83),     -- Push B
(19, 11), (19, 20), (19, 18), (19, 71), (19, 56), (19, 49), (19, 86),  -- Pull B
(20, 38), (20, 29), (20, 35), (20, 42), (20, 32), (20, 26), (20, 27), -- Leg B

-- Plan 6
(6, 35), (6, 3), (6, 10), (6, 42), (6, 82);  -- Full Body

INSERT INTO ExerciseSet (exercise_id, set_number, repetitions, weight, date) VALUES
-- Trainingsplan 1 (Januar 2024)
(35, 1, 8, 70, '2024-01-05'), (35, 2, 8, 72.5, '2024-01-05'),  -- Romanian Deadlift
(35, 1, 10, 75, '2024-01-12'), (35, 2, 10, 75, '2024-01-12'),
(3, 1, 12, 60, '2024-01-05'), (3, 2, 10, 62.5, '2024-01-05'),  -- Incline-Benchpress
(10, 1, 8, 0, '2024-01-12'), (10, 2, 6, 0, '2024-01-12'),       -- Pullups (Bodyweight)
(42, 1, 15, 40, '2024-01-19'), (42, 2, 12, 45, '2024-01-19'),   -- Leg Extensions
(82, 1, 12, 0, '2024-01-05'), (82, 2, 15, 0, '2024-01-05'),     -- Leg Raises

-- Trainingsplan 2 (Februar 2024)
(1, 1, 10, 80, '2024-02-07'), (1, 2, 8, 85, '2024-02-07'),      -- Benchpress
(11, 1, 12, 55, '2024-02-14'), (11, 2, 10, 60, '2024-02-14'),  -- Lat Pulldown
(20, 1, 8, 70, '2024-02-21'), (20, 2, 8, 75, '2024-02-21'),     -- Wide Machine Row
(67, 1, 12, 15, '2024-02-28'), (67, 2, 15, 15, '2024-02-28'),   -- Lateral Raises

-- Trainingsplan 3 (März 2024)
(37, 1, 12, 50, '2024-03-04'), (37, 2, 10, 55, '2024-03-04'),  -- Leg Curls Laying
(40, 1, 8, 100, '2024-03-11'), (40, 2, 6, 110, '2024-03-11'),  -- Hack Squats
(19, 1, 10, 60, '2024-03-18'), (19, 2, 8, 65, '2024-03-18'),   -- Wide T-Bar Row
(26, 1, 20, 70, '2024-03-25'), (26, 2, 18, 75, '2024-03-25'),  -- Adduktor

-- Trainingsplan 4 (April 2024)
(5, 1, 15, 40, '2024-04-02'), (5, 2, 12, 45, '2024-04-02'),     -- Butterfly Machine
(8, 1, 10, 60, '2024-04-09'), (8, 2, 8, 65, '2024-04-09'),      -- Chest Press
(79, 1, 8, 50, '2024-04-16'), (79, 2, 6, 55, '2024-04-16'),     -- Overhead Press
(83, 1, 20, 0, '2024-04-23'), (83, 2, 25, 0, '2024-04-23'),     -- Abs Machine

-- Trainingsplan 5 (Mai 2024)
(2, 1, 12, 25, '2024-05-03'), (2, 2, 10, 27.5, '2024-05-03'),   -- Dumbell Benchpress
(18, 1, 8, 80, '2024-05-10'), (18, 2, 6, 85, '2024-05-10'),     -- Wide Barbell Row
(38, 1, 15, 30, '2024-05-17'), (38, 2, 12, 35, '2024-05-17'),   -- Standing Leg Curls
(27, 1, 20, 60, '2024-05-24'), (27, 2, 18, 65, '2024-05-24');  -- Abduktor