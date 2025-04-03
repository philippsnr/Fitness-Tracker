package com.example.fitnesstracker.repository;

import static org.junit.Assert.*;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import com.example.fitnesstracker.database.DatabaseHelper;
import com.example.fitnesstracker.model.UserInformation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class UserInformationRepositoryTest {
    private UserInformationRepository userInformationRepository;
    private DatabaseHelper dbHelper;
    private Context context;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM UserInformation");
        db.close();
        userInformationRepository = new UserInformationRepository(context);
    }

    @After
    public void tearDown() {
        dbHelper.close();
    }

    @Test
    public void testWriteAndGetAllUserInformation() {
        UserInformation userInfo = new UserInformation(1, 1, "2025-03-12", 180, 75.0, 15);
        userInformationRepository.writeUserInformation(userInfo);

        List<UserInformation> userInfoList = userInformationRepository.getAllUserInformation();
        assertNotNull(userInfoList);
        assertEquals(1, userInfoList.size());

        UserInformation retrievedUserInfo = userInfoList.get(0);
        assertEquals(userInfo.getUserId(), retrievedUserInfo.getUserId());
        assertEquals(userInfo.getDate(), retrievedUserInfo.getDate());
        assertEquals(userInfo.getHeight(), retrievedUserInfo.getHeight());
        assertEquals(userInfo.getWeight(), retrievedUserInfo.getWeight(), 0.0);
        assertEquals(userInfo.getKfa(), retrievedUserInfo.getKfa());
    }


    @Test
    public void testGetLatestUserInformation() {
        UserInformation userInfo1 = new UserInformation(1, 1, "2025-03-12", 180, 75.0, 15);
        userInformationRepository.writeUserInformation(userInfo1);

        UserInformation userInfo2 = new UserInformation(2, 1, "2025-03-13", 182, 78.0, 14);
        userInformationRepository.writeUserInformation(userInfo2);

        UserInformation latestUserInfo = userInformationRepository.getLatestUserInformation();
        assertNotNull(latestUserInfo);
        assertEquals(userInfo2.getUserId(), latestUserInfo.getUserId());
        assertEquals(userInfo2.getDate(), latestUserInfo.getDate());
        assertEquals(userInfo2.getHeight(), latestUserInfo.getHeight());
        assertEquals(userInfo2.getWeight(), latestUserInfo.getWeight(), 0.0);
        assertEquals(userInfo2.getKfa(), latestUserInfo.getKfa());

        UserInformation userInfo3 = new UserInformation(3, 1, "2025-03-14", 0, 80.0, 0);
        userInformationRepository.writeUserInformation(userInfo3);

        UserInformation latestUserInfoAfterMissing = userInformationRepository.getLatestUserInformation();
        assertNotNull(latestUserInfoAfterMissing);
        assertEquals(userInfo2.getHeight(), latestUserInfoAfterMissing.getHeight());
        assertEquals(userInfo2.getKfa(), latestUserInfoAfterMissing.getKfa());
        assertEquals(userInfo3.getWeight(), latestUserInfoAfterMissing.getWeight(), 0.0);
    }
}
