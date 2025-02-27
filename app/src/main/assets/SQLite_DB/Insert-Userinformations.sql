DELETE FROM User WHERE 1;
INSERT INTO User (id, name, birth_date, goal, trainingdaysPerWeek)
VALUES (1, "Max Mustermann", "15-06-1990", "Abnehmen", 4);

DELETE FROM UserInformation WHERE 1;
INSERT INTO UserInformation (user_id, date, height, weight, KFA)
VALUES
    (1, "01-01-2025", 180, 90.0, 25),
    (1, "05-01-2025", 180, 89.0, 24),
    (1, "10-01-2025", 180, 89.2, 24),
    (1, "15-01-2025", 180, 88.5, 23),
    (1, "20-01-2025", 180, 88.8, 23),
    (1, "25-01-2025", 180, 88.4, 22),
    (1, "01-02-2025", 180, 87.9, 22),
    (1, "05-02-2025", 180, 87.5, 22),
    (1, "10-02-2025", 180, 87.2, 21),
    (1, "15-02-2025", 180, 87.4, 21),
    (1, "20-02-2025", 180, 86.8, 21)
