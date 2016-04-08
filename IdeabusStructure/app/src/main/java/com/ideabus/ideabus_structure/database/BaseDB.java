package com.ideabus.ideabus_structure.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDB extends SQLiteOpenHelper {

	public final static String DB_NAME = "your db name";
	protected final static int DB_VERSION = 1;

	public BaseDB(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override 
	public void onCreate(SQLiteDatabase db) {
		createUserDB(db);
		createXXXDB(db);
	}
	
	private void createUserDB(SQLiteDatabase db){
		db.execSQL("CREATE TABLE IF NOT EXISTS " + UserData.USER_TB_NAME + "(" +
						UserData.NAME + " CHAR(50) PRIMARY KEY," +
						UserData.AGE + " INT," +
						UserData.WEIGHT + " INT," +
						UserData.GENDER + " INT," +
						UserData.GOALS + " CHAR(100)," +
						UserData.NOTES + " CHAR(100)" +
						")"
		);
	}

	private void createXXXDB(SQLiteDatabase db){
        //your table
	}
	
	@Override   
	public void onOpen(SQLiteDatabase db) {     
	   super.onOpen(db);       
	   	
	} 
	 
	@Override
	public synchronized void close() {
		super.close();
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}