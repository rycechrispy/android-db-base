package com.bums.library;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

import com.bums.small.DepartmentData;
import com.bums.small.OfficeData;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "small";

	// Login table name
	private static final String TABLE_LOGIN = "login";
	private static final String TABLE_OFFICE = "office";
	private static final String TABLE_DEPARTMENT = "department";

	// Login Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_USERNAME = "username";
	private static final String KEY_PASSWORD = "password";

	//office column names
	private static final String KEY_OFFICETYPE = "officetype";
	private static final String KEY_ISLEADER = "isLeader";

	//department column names
	private static final String KEY_DEPARTMENT = "department";
	private static final String KEY_GROUP = "organization";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
				+ KEY_ID + " INTEGER PRIMARY KEY,"
				+ KEY_USERNAME + " TEXT," 
				+ KEY_PASSWORD + " TEXT" +")";
		db.execSQL(CREATE_LOGIN_TABLE);

		String CREATE_OFFICE_TABLE = "CREATE TABLE " + TABLE_OFFICE + "("
				+ KEY_ID + " INTEGER,"
				+ KEY_OFFICETYPE + " TEXT," 
				+ KEY_ISLEADER + " TEXT, PRIMARY KEY (" + KEY_ID + "," + KEY_OFFICETYPE + "," + KEY_ISLEADER + ")" +")";
		db.execSQL(CREATE_OFFICE_TABLE);

		String CREATE_DEPARTMENT_TABLE = "CREATE TABLE " + TABLE_DEPARTMENT + "("
				+ KEY_ID + " INTEGER,"
				+ KEY_DEPARTMENT + " TEXT," 
				+ KEY_GROUP + " TEXT, PRIMARY KEY (" + KEY_ID + "," + KEY_DEPARTMENT + "," + KEY_GROUP + ")" +")";
		db.execSQL(CREATE_DEPARTMENT_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_OFFICE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEPARTMENT);
		// Create tables again
		onCreate(db);
	}

	/**
	 * Storing user details in database
	 * */
	public void addUser(String id, String username, String password) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_USERNAME, username); // UserName
		values.put(KEY_ID, id); // ID
		values.put(KEY_PASSWORD, password); // password

		// Inserting Row
		db.insert(TABLE_LOGIN, null, values);
		db.close(); // Closing database connection
	}

	public boolean addOffice(String id, String officeType, String isLeader) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ID, id); // ID
		values.put(KEY_OFFICETYPE, officeType);
		values.put(KEY_ISLEADER, isLeader); 

		// Inserting Row
		if (db.insert(TABLE_OFFICE, null, values) == -1) {
			db.close();
			return false;
		}
		db.close(); // Closing database connection
		return true;
	}

	public void deleteOffice(String id, String officeType) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_OFFICE, "id = "+id +" and " + "officeType = "+ "'" + officeType + "'", null);
		db.close(); // Closing database connection
	}

	public boolean addDepartment(String id, String organization, String department) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ID, id); // ID
		values.put(KEY_DEPARTMENT, department);
		values.put(KEY_GROUP, organization); 

		// Inserting Row
		if (db.insert(TABLE_DEPARTMENT, null, values) == -1) {
			db.close();
			return false;
		}
		db.close(); // Closing database connection
		return true;
	}

	public void deleteDepartment(String id, String group) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_DEPARTMENT, "id = "+id +" and " + "organization = "+ "'" + group + "'", null);
		db.close(); // Closing database connection
	}

	/**
	 * Getting offices data from database
	 * */
	public ArrayList<OfficeData> getOffices(String id){
		ArrayList<OfficeData> offices = new ArrayList<OfficeData>();
		String selectQuery = "SELECT rowid _id, * FROM " + TABLE_OFFICE + " WHERE id =" + id;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// Move to first row
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			do {
				String officeType = cursor.getString(2);
				String isLeader = cursor.getString(3);

				offices.add(new OfficeData(officeType, isLeader));
			} while (cursor.moveToNext());
			cursor.close();
		}
		db.close();
		return offices;
	}

	/**
	 * Getting offices data from database
	 * */
	public ArrayList<DepartmentData> getDepartment(String id){
		ArrayList<DepartmentData> organizations = new ArrayList<DepartmentData>();
		String selectQuery = "SELECT rowid _id, * FROM " + TABLE_DEPARTMENT + " WHERE id =" + id;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// Move to first row
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				String organization = cursor.getString(3);
				String department = cursor.getString(2);

				organizations.add(new DepartmentData(organization, department));
			} while (cursor.moveToNext());
			cursor.close();
		}
		db.close();
		return organizations;
	}


	/**
	 * Getting user data from database
	 * */
	public HashMap<String, String> getUserDetails(){
		HashMap<String,String> user = new HashMap<String,String>();
		String selectQuery = "SELECT rowid _id, * FROM " + TABLE_LOGIN;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// Move to first row
		cursor.moveToFirst();
		if(cursor.getCount() > 0){
			user.put("id", cursor.getString(1));
			user.put("username", cursor.getString(2));
			user.put("password", cursor.getString(3));
		}
		cursor.close();
		db.close();
		// return user
		return user;
	}

	/**
	 * Getting user login status
	 * return true if rows are there in table
	 * */
	public int getRowCount() {
		String countQuery = "SELECT rowid _id, * FROM " + TABLE_OFFICE;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int rowCount = cursor.getCount();
		db.close();
		cursor.close();

		// return row count
		return rowCount;
	}


	/**
	 * Re create database
	 * Delete all tables and create them again
	 * */
	public void resetTables(){
		SQLiteDatabase db = this.getWritableDatabase();
		// Delete All Rows
		db.delete(TABLE_LOGIN, null, null);
		db.close();
	}

}
