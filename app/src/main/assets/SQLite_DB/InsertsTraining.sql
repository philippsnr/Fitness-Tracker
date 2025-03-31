-- Insert Trainingplans in neuer Reihenfolge
INSERT INTO Trainingplan (id, name, isActive) VALUES
(1, 'Full Body (1x pro Woche)', 0),      -- Ehemals Plan 6
(2, 'Full Body (2x pro Woche)', 0),      -- Ehemals Plan 1
(3, 'Upper/Lower, Fullbody (3x pro Woche)', 0), -- Ehemals Plan 2
(4, 'Upper/Lower (4x pro Woche)', 1),    -- Ehemals Plan 3
(5, 'Push, Pull, Legs, Upper, Lower (5x pro Woche)', 0), -- Ehemals Plan 4
(6, 'Push, Pull, Legs (6x pro Woche)', 0); -- Ehemals Plan 5

-- Insert Trainingdays mit neuen IDs
INSERT INTO Trainingday (id, name, Trainingplan_id) VALUES
-- Plan 1 (ehemals 6)
(1, 'Full Body', 1),

-- Plan 2 (ehemals 1)
(2, 'Full Body A', 2),
(3, 'Full Body B', 2),

-- Plan 3 (ehemals 2)
(4, 'Upper Body', 3),
(5, 'Lower Body', 3),
(6, 'Full Body', 3),

-- Plan 4 (ehemals 3)
(7, 'Upper Body A', 4),
(8, 'Lower Body A', 4),
(9, 'Upper Body B', 4),
(10, 'Lower Body B', 4),

-- Plan 5 (ehemals 4)
(11, 'Push Day', 5),
(12, 'Pull Day', 5),
(13, 'Leg Day', 5),
(14, 'Upper Body', 5),
(15, 'Lower Body', 5),

-- Plan 6 (ehemals 5)
(16, 'Push Day A', 6),
(17, 'Pull Day A', 6),
(18, 'Leg Day A', 6),
(19, 'Push Day B', 6),
(20, 'Pull Day B', 6),
(21, 'Leg Day B', 6);

-- Insert Exercise Assignments mit neuen IDs
INSERT INTO TrainingdayExerciseAssignment (Trainingday_id, Exercise_id) VALUES
-- Plan 1 (Full Body 1x)
(1, 35), (1, 3), (1, 10), (1, 42), (1, 82),

-- Plan 2 (Full Body 2x)
(2, 35), (2, 3), (2, 10), (2, 42), (2, 82),  -- Full Body A
(3, 37), (3, 39), (3, 5), (3, 19), (3, 86),  -- Full Body B

-- Plan 3 (Upper/Lower 3x)
(4, 1), (4, 5), (4, 11), (4, 20), (4, 71), (4, 67), (4, 44), (4, 53),  -- Upper Body
(5, 36), (5, 39), (5, 35), (5, 42), (5, 32), (5, 23), (5, 82),         -- Lower Body
(6, 37), (6, 29), (6, 4), (6, 19), (6, 86),                           -- Full Body

-- Plan 4 (Upper/Lower 4x)
(7, 1), (7, 5), (7, 11), (7, 20), (7, 71), (7, 67), (7, 44), (7, 53),  -- Upper A
(8, 36), (8, 39), (8, 35), (8, 42), (8, 32), (8, 23), (8, 82),         -- Lower A
(9, 1), (9, 5), (9, 11), (9, 19), (9, 71), (9, 67), (9, 79), (9, 56),  -- Upper B
(10, 37), (10, 29), (10, 35), (10, 42), (10, 32), (10, 26), (10, 27),  -- Lower B

-- Plan 5 (Push/Pull 5x)
(11, 5), (11, 3), (11, 8), (11, 67), (11, 44), (11, 79), (11, 83),     -- Push Day
(12, 10), (12, 18), (12, 23), (12, 71), (12, 56), (12, 49), (12, 82),  -- Pull Day
(13, 37), (13, 40), (13, 35), (13, 42), (13, 32), (13, 26), (13, 27),  -- Leg Day
(14, 1), (14, 5), (14, 11), (14, 20), (14, 71), (14, 67), (14, 44), (14, 53),  -- Upper
(15, 36), (15, 39), (15, 35), (15, 42), (15, 32), (15, 23), (15, 82),          -- Lower

-- Plan 6 (Push/Pull 6x)
(16, 5), (16, 3), (16, 2), (16, 67), (16, 44), (16, 79), (16, 83),     -- Push A
(17, 10), (17, 18), (17, 23), (17, 71), (17, 56), (17, 49), (17, 82),  -- Pull A
(18, 37), (18, 40), (18, 35), (18, 42), (18, 32), (18, 26), (18, 27),  -- Leg A
(19, 5), (19, 4), (19, 8), (19, 67), (19, 44), (19, 79), (19, 83),     -- Push B
(20, 11), (20, 20), (20, 18), (20, 71), (20, 56), (20, 49), (20, 86),  -- Pull B
(21, 38), (21, 29), (21, 35), (21, 42), (21, 32), (21, 26), (21, 27); -- Leg B


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