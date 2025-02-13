CREATE TABLE IF NOT EXISTS Exercise (
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL,
    difficulty TEXT NOT NULL,
    info TEXT NOT NULL,
    picture_path TEXT NOT NULL -- Bilder laut ChatGPT in app/res/drawable
) STRICT;

CREATE TABLE IF NOT EXISTS ExerciseSet (
    id INTEGER PRIMARY KEY,
    TrainingdayExerciseAssignment_id INTEGER NOT NULL,
    set_number INTEGER NOT NULL,
    repetitions INTEGER NOT NULL,
    weight INTEGER NOT NULL,
    FOREIGN KEY (TrainingdayExerciseAssignment_id) REFERENCES TrainingdayExerciseAssignment (id)
) STRICT;

CREATE TABLE IF NOT EXISTS MuscleGroup (
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL,
    picture_path TEXT NOT NULL -- Bilder laut ChatGPT in app/res/drawable
) STRICT;

CREATE TABLE IF NOT EXISTS ExerciseMuscleGroupAssignment (
    id INTEGER PRIMARY KEY,
    Exercise_id INTEGER NOT NULL,
    MuscleGroup_id INTEGER NOT NULL,
    FOREIGN KEY (Exercise_id) REFERENCES Exercise (id),
    FOREIGN KEY (MuscleGroup_id) REFERENCES MuscleGroup (id)
) STRICT;

CREATE TABLE IF NOT EXISTS Trainingplan (
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL,
    isActive INTEGER  NOT NULL DEFAULT 0
) STRICT;

CREATE Table IF NOT EXISTS Trainingday (
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL,
    Trainingplan_id INTEGER NOT NULL,
    FOREIGN KEY (Trainingplan_id) REFERENCES Trainingplan (id)
) STRICT;

CREATE TABLE IF NOT EXISTS TrainingdayExerciseAssignment (
    id INTEGER PRIMARY KEY,
    Trainingday_id INTEGER NOT NULL,
    Exercise_id INTEGER NOT NULL,
    FOREIGN KEY (Trainingday_id) REFERENCES Trainingday (id),
    FOREIGN KEY (Exercise_id) REFERENCES Exercise (id)
) STRICT;

CREATE TABLE IF NOT EXISTS User (
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL,
    birth_date TEXT, -- "DD-MM-YYYY"
    goal TEXT NOT NULL CHECK (goal IN ("Abnehmen", "Gewicht halten (=Recomposition)", "Zunehmen")), --alle Ziele beinhalten den Wunsch Muskeln aufzubauen
    trainingdaysPerWeek INTEGER NOT NULL CHECK (trainingdaysPerWeek BETWEEN 0 AND 7)
) STRICT;

CREATE TABLE IF NOT EXISTS UserInformation (
    id INTEGER PRIMARY KEY,
    user_id INTEGER,
    date TEXT, -- "DD-MM-YYYY"
    height INTEGER NOT NULL,
    weight INTEGER NOT NULL,
    KFA INTEGER NOT NULL, -- Körperfettanteil in Prozent -> Bild zur Schätzung
    FOREIGN KEY (user_id) REFERENCES User (id)
) STRICT;

CREATE TABLE IF NOT EXISTS Nutritionday (
    id INTEGER PRIMARY KEY,
    date TEXT -- "DD-MM-YYYY"
) STRICT;

CREATE TABLE IF NOT EXISTS NutritiondayNutritionAssignment (
    id INTEGER PRIMARY KEY,
    nutritionday_id INTEGER NOT NULL,
    time TEXT, --"HH-MM"
    nutrition_name TEXT NOT NULL,
    nutrition_mass INTEGER NOT NULL, -- in g
    nutrition_cals INTEGER NOT NULL,
    nutrition_carbs INTEGER NOT NULL, -- in g
    nutrition_fats INTEGER NOT NULL,
    nutrition_proteins INTEGER NOT NULL,
    FOREIGN KEY (nutritionday_id) REFERENCES Nutritionday (id)
) STRICT;
