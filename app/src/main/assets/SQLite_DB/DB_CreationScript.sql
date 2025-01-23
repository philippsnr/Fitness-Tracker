CREATE TABLE Exercise (
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL,
    difficulty TEXT NOT NULL,
    info TEXT NOT NULL
) STRICT;

CREATE TABLE ExerciseSet (
    id INTEGER PRIMARY KEY,
    TrainingdayExerciseAssignment_id INTEGER NOT NULL,
    set_number INTEGER NOT NULL,
    repetitions INTEGER NOT NULL,
    weight INTEGER NOT NULL,
    FOREIGN KEY (TrainingdayExerciseAssignment_id) REFERENCES TrainingdayExerciseAssignment (id)
) STRICT;

CREATE TABLE MuscleGroup (
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL
) STRICT;

CREATE TABLE ExerciseMuscleGroupAssignment (
    id INTEGER PRIMARY KEY,
    targetSpecies TEXT NOT NULL CHECK (targetSpecies IN ("Hauptmuskel", "Hilfsmuskel")),
    Exercise_id INTEGER NOT NULL,
    MuscleGroup_id INTEGER NOT NULL,
    FOREIGN KEY (Exercise_id) REFERENCES Exercise (id),
    FOREIGN KEY (MuscleGroup_id) REFERENCES MuscleGroup (id)
) STRICT;

CREATE TABLE Trainingplan (
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL,
    isActive BOOLEAN NOT NULL DEFAULT FALSE
) STRICT;

CREATE Table Trainingday (
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL,
    Trainingplan_id INTEGER NOT NULL,
    FOREIGN KEY (Trainingplan_id) REFERENCES Trainingplan (id)
) STRICT;

CREATE TABLE TrainingdayExerciseAssignment (
    id INTEGER PRIMARY KEY,
    Trainingday_id INTEGER NOT NULL,
    Exercise_id INTEGER NOT NULL,
    FOREIGN KEY (Trainingday_id) REFERENCES Trainingday (id),
    FOREIGN KEY (Exercise_id) REFERENCES Exercise (id)
) STRICT;

CREATE TABLE User (
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL,
    age INTEGER NOT NULL,
    height INTEGER NOT NULL,
    weight INTEGER NOT NULL,
    KFA INTEGER NOT NULL, -- Körperfettanteil in Prozent -> Bild zur Schätzung
    goal TEXT NOT NULL CHECK (goal IN ("Abnehmen", "Gewicht halten", "Zunehmen")),
    trainingdaysPerWeek INTEGER NOT NULL CHECK (trainingdaysPerWeek BETWEEN 0 AND 7)
) STRICT;