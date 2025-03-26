CREATE TABLE IF NOT EXISTS Exercise (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    difficulty INTEGER NOT NULL,  -- 1-3
    info TEXT NOT NULL,
    picture_path TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS MuscleGroup (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    picture_path TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS ExerciseMuscleGroupAssignment (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    Exercise_id INTEGER NOT NULL,
    MuscleGroup_id INTEGER NOT NULL,
    FOREIGN KEY (Exercise_id) REFERENCES Exercise (id) ON DELETE CASCADE,
    FOREIGN KEY (MuscleGroup_id) REFERENCES MuscleGroup (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Trainingplan (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    isActive INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS Trainingday (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    Trainingplan_id INTEGER NOT NULL,
    FOREIGN KEY (Trainingplan_id) REFERENCES Trainingplan (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS TrainingdayExerciseAssignment (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    Trainingday_id INTEGER NOT NULL,
    Exercise_id INTEGER NOT NULL,
    FOREIGN KEY (Trainingday_id) REFERENCES Trainingday (id) ON DELETE CASCADE,
    FOREIGN KEY (Exercise_id) REFERENCES Exercise (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS ExerciseSet (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    exercise_id INTEGER NOT NULL,
    set_number INTEGER NOT NULL,
    repetitions INTEGER NOT NULL,
    weight INTEGER NOT NULL,
    date TEXT NOT NULL, -- Format: "YYYY-MM-DD"
    FOREIGN KEY (exercise_id) REFERENCES Exercise (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS User (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    birth_date TEXT, -- "DD-MM-YYYY"
    goal TEXT NOT NULL CHECK (goal IN ("Abnehmen", "Gewicht halten", "Zunehmen")),
    trainingdaysPerWeek INTEGER NOT NULL CHECK (trainingdaysPerWeek BETWEEN 0 AND 7)
);

CREATE TABLE IF NOT EXISTS UserInformation (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER,
    date TEXT, -- "DD-MM-YYYY"
    height INTEGER NOT NULL,
    weight REAL NOT NULL,
    KFA INTEGER NOT NULL, -- Körperfettanteil in Prozent
    FOREIGN KEY (user_id) REFERENCES User (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Nutritionday (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    date TEXT -- "DD-MM-YYYY"
);

CREATE TABLE IF NOT EXISTS NutritiondayNutritionAssignment (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nutritionday_id INTEGER NOT NULL,
    time TEXT, --"HH-MM"
    nutrition_name_english TEXT NOT NULL,
    nutrition_name_german TEXT DEFAULT NULL,
    nutrition_mass INTEGER NOT NULL, -- in g
    nutrition_cals INTEGER NOT NULL,
    nutrition_carbs INTEGER NOT NULL, -- in g
    nutrition_fats INTEGER NOT NULL,
    nutrition_proteins INTEGER NOT NULL,
    nutrition_picture_path TEXT DEFAULT NULL,
    FOREIGN KEY (nutritionday_id) REFERENCES Nutritionday (id) ON DELETE CASCADE
);
