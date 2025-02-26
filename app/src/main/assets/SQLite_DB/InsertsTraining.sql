-- Erstelle den Trainingsplan
INSERT INTO Trainingplan (name, isActive) VALUES ("Ganzkörper Anfänger", 0);

-- IDs der Trainingstage speichern
INSERT INTO Trainingday (name, Trainingplan_id) VALUES
("Ganzkörper Tag 1", 1),
("Ganzkörper Tag 2", 1),
("Ganzkörper Tag 3", 1);

-- Beispiel-Übungszuweisungen für Ganzkörper Tag 1
INSERT INTO TrainingdayExerciseAssignment (Trainingday_id, Exercise_id) VALUES
(1, 1), -- Kniebeugen
(1, 2), -- Bankdrücken
(1, 3); -- Klimmzüge

-- Beispiel-Übungszuweisungen für Ganzkörper Tag 2
INSERT INTO TrainingdayExerciseAssignment (Trainingday_id, Exercise_id) VALUES
(2, 4), -- Kreuzheben
(2, 5), -- Schulterdrücken
(2, 6); -- Rudern

-- Beispiel-Übungszuweisungen für Ganzkörper Tag 3
INSERT INTO TrainingdayExerciseAssignment (Trainingday_id, Exercise_id) VALUES
(3, 7), -- Beinpresse
(3, 8), -- Dips
(3, 9); -- Face Pulls


-- Erstelle den Trainingsplan
INSERT INTO Trainingplan (name, isActive) VALUES ("Push Pull Beine", 0);

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
(4, 2), -- Bankdrücken
(4, 5), -- Schulterdrücken
(4, 8); -- Dips

-- Pull 1
INSERT INTO TrainingdayExerciseAssignment (Trainingday_id, Exercise_id) VALUES
(5, 3), -- Klimmzüge
(5, 6), -- Rudern
(5, 9); -- Face Pulls

-- Beine 1
INSERT INTO TrainingdayExerciseAssignment (Trainingday_id, Exercise_id) VALUES
(6, 1), -- Kniebeugen
(6, 4), -- Kreuzheben
(6, 7); -- Beinpresse

-- Push 2
INSERT INTO TrainingdayExerciseAssignment (Trainingday_id, Exercise_id) VALUES
(7, 2), -- Bankdrücken
(7, 5), -- Schulterdrücken
(7, 8); -- Dips

-- Pull 2
INSERT INTO TrainingdayExerciseAssignment (Trainingday_id, Exercise_id) VALUES
(8, 3), -- Klimmzüge
(8, 6), -- Rudern
(8, 9); -- Face Pulls

-- Beine 2
INSERT INTO TrainingdayExerciseAssignment (Trainingday_id, Exercise_id) VALUES
(9, 1), -- Kniebeugen
(9, 4), -- Kreuzheben
(9, 7); -- Beinpresse
