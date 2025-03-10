DELETE FROM User WHERE 1;
INSERT INTO User (id, name, birth_date, goal, trainingdaysPerWeek)
VALUES (1, "Max Mustermann", "1990-06-15", "Abnehmen", 4);

DELETE FROM UserInformation WHERE 1;
INSERT INTO UserInformation (user_id, date, height, weight, KFA)
VALUES
    (1, "2025-01-01", 180, 90.0, 25),
    (1, "2025-01-05", 180, 89.0, 24),
    (1, "2025-01-10", 180, 89.2, 24),
    (1, "2025-01-15", 180, 88.5, 23),
    (1, "2025-01-20", 180, 88.8, 23),
    (1, "2025-01-25", 180, 88.4, 22),
    (1, "2025-02-01", 180, 87.9, 22),
    (1, "2025-02-05", 180, 87.5, 22),
    (1, "2025-02-10", 180, 87.2, 21),
    (1, "2025-02-15", 180, 87.4, 21),
    (1, "2025-02-20", 180, 86.8, 21);
    