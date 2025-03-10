package com.example.fitnesstracker.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.fitnesstracker.database.DatabaseHelper;
import com.example.fitnesstracker.model.MuscleGroup;
import java.util.ArrayList;
import java.util.List;

public class MuscleGroupRepository {
    private final DatabaseHelper dbHelper;

    public MuscleGroupRepository(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    public List<MuscleGroup> getAllMuscleGroups() {
        List<MuscleGroup> muscleGroups = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM MuscleGroup ORDER BY name", null);
        if (cursor.moveToFirst()) {
            do {
                int muscleGroupId = cursor.getInt(0);
                String muscleGroupName = cursor.getString(1);
                String muscleGroupPicturePath = cursor.getString(2);
                MuscleGroup muscleGroup = new MuscleGroup(muscleGroupId, muscleGroupName, muscleGroupPicturePath);
                muscleGroups.add(muscleGroup);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return muscleGroups;
    }
}
