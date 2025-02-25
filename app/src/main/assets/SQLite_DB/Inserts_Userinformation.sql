-- Einfügen eines Benutzers in die User-Tabelle
INSERT INTO User (name, birth_date, goal, trainingdaysPerWeek) 
VALUES ("Max Mustermann", "15-06-1990", "Abnehmen", 4);

-- Einfügen mehrerer Einträge in die UserInformation-Tabelle für den Verlauf
INSERT INTO UserInformation (user_id, date, height, weight, KFA) 
VALUES 
    ((SELECT id FROM User WHERE name = "Max Mustermann"), "01-01-2024", 180, 90, 25),
    ((SELECT id FROM User WHERE name = "Max Mustermann"), "15-01-2024", 180, 88, 24),
    ((SELECT id FROM User WHERE name = "Max Mustermann"), "01-02-2024", 180, 85, 22),
    ((SELECT id FROM User WHERE name = "Max Mustermann"), "15-02-2024", 180, 83, 21),
    ((SELECT id FROM User WHERE name = "Max Mustermann"), "01-03-2024", 180, 80, 19);
