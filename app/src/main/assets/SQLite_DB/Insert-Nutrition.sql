-- Testdaten für Nutritionday
INSERT INTO Nutritionday (date) VALUES
    ('2025-03-01'),
    ('2025-03-02'),
    ('2025-03-03');

-- Testdaten für NutritiondayNutritionAssignment
INSERT INTO NutritiondayNutritionAssignment (nutritionday_id, time, nutrition_name_english, nutrition_name_german, nutrition_mass, nutrition_cals, nutrition_carbs, nutrition_fats, nutrition_proteins, nutrition_picture_path) VALUES
    (1, '08-00', 'Oatmeal with Banana', 'Haferbrei mit Banane', 250, 320, 60, 6, 10, 'images/oatmeal.jpg'),
    (1, '12-30', 'Grilled Chicken with Rice', 'Gegrilltes Hähnchen mit Reis', 400, 600, 70, 15, 45, 'images/chicken_rice.jpg'),
    (1, '19-00', 'Salmon with Vegetables', 'Lachs mit Gemüse', 350, 550, 20, 35, 40, 'images/salmon.jpg'),

    (2, '07-45', 'Scrambled Eggs with Toast', 'Rührei mit Toast', 300, 400, 35, 20, 25, 'images/eggs_toast.jpg'),
    (2, '13-00', 'Pasta with Tomato Sauce', 'Nudeln mit Tomatensauce', 500, 650, 100, 12, 20, 'images/pasta.jpg'),
    (2, '18-30', 'Greek Yogurt with Nuts', 'Griechischer Joghurt mit Nüssen', 200, 250, 15, 10, 20, 'images/yogurt_nuts.jpg'),

    (3, '08-15', 'Avocado Toast with Egg', 'Avocado-Toast mit Ei', 280, 450, 40, 20, 18, 'images/avocado_toast.jpg'),
    (3, '12-45', 'Beef Steak with Sweet Potatoes', 'Rindersteak mit Süßkartoffeln', 450, 700, 50, 30, 55, 'images/steak_potatoes.jpg'),
    (3, '19-30', 'Quinoa Salad with Tofu', 'Quinoasalat mit Tofu', 300, 500, 55, 18, 35, 'images/quinoa_tofu.jpg');
