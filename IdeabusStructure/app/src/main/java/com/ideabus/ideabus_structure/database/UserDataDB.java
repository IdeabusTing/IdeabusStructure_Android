package com.ideabus.ideabus_structure.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Ting on 15/11/20.
 */
public class UserDataDB {

    private SQLiteDatabase db;

    private BaseDB dbHelper;

    public UserDataDB(Context context) {
        dbHelper = new BaseDB(context);
        db = dbHelper.getReadableDatabase();
    }


    public void close() {
        db.close();
        dbHelper.close();
    }

    public boolean checkExistence(String name){
        Cursor cursor = db.query(UserData.USER_TB_NAME, null, UserData.NAME + " = ? ", new String[]{name}, null, null, "1");

        boolean isExist = false;
        if(cursor.moveToFirst())
            isExist = true;
        cursor.close();

        return isExist;
    }

    public ArrayList<UserData> getAllUserData() {
        ArrayList<UserData> userDataArray = new ArrayList<>();

        Cursor cursor = db.query(UserData.USER_TB_NAME, null, null, null, null, null, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            UserData userData = new UserData();
            userData.setName(cursor.getString(0));
            userData.setAge(cursor.getInt(1));
            userData.setWeight(cursor.getInt(2));
            userData.setGender(cursor.getInt(3));
            userData.setGoals(cursor.getString(4));
            userData.setNotes(cursor.getString(5));
            userDataArray.add(userData);

            cursor.moveToNext();
        }
        cursor.close();
        return userDataArray;
    }

    public boolean saveUserData(UserData userData) {
        String newName = userData.getName();
        ContentValues values = new ContentValues();
        values.put(UserData.NAME, newName);
        values.put(UserData.AGE, userData.getAge());
        values.put(UserData.WEIGHT, userData.getWeight());
        values.put(UserData.GENDER, userData.getGender());
        values.put(UserData.GOALS, userData.getGoals());
        values.put(UserData.NOTES, userData.getNotes());

        long uid = db.insert(UserData.USER_TB_NAME, null, values);

        return uid != -1;
    }

    public boolean updateUserData(String userName, UserData userData) {
        String newName = userData.getName();
        ContentValues values = new ContentValues();
        values.put(UserData.NAME, newName);
        values.put(UserData.AGE, userData.getAge());
        values.put(UserData.WEIGHT, userData.getWeight());
        values.put(UserData.GENDER, userData.getGender());
        values.put(UserData.GOALS, userData.getGoals());
        values.put(UserData.NOTES, userData.getNotes());

        long uid = db.update(UserData.USER_TB_NAME, values, UserData.NAME + " = ?", new String[]{userName});

        return uid != -1;
    }

    public UserData getUserData(String name) {
        Cursor cursor = db.query(UserData.USER_TB_NAME, null, UserData.NAME + " = ? ", new String[]{name}, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            UserData userData = new UserData();
            userData.setName(cursor.getString(0));
            userData.setAge(cursor.getInt(1));
            userData.setWeight(cursor.getInt(2));
            userData.setGender(cursor.getInt(3));
            userData.setGoals(cursor.getString(4));
            userData.setNotes(cursor.getString(5));
            cursor.close();
            return userData;
        }
        return null;
    }

    public Integer deleteUserData(String name) {
        return db.delete(UserData.USER_TB_NAME, UserData.NAME + " = ? ", new String[]{name});
    }

}
