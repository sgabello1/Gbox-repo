package com.example.gbox.addrope;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "ropesManager";

	// Contacts table name
	private static final String TABLE_ROPES = "ropes";

	// Contacts Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_LAST_UPDATE = "last_update";
	private static final String KEY_USAGE = "usage";
	private static final String KEY_MAX_FORCE = "max_force";
	private static final String KEY_MND = "mnd";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_ROPES_TABLE = "CREATE TABLE " + TABLE_ROPES + "("
				+ KEY_ID + " INTEGER PRIMARY KEY, " 
				+ KEY_NAME + " TEXT, "
				+ KEY_LAST_UPDATE + " TEXT, " 
				+ KEY_USAGE + " INTEGER, " 
				+ KEY_MAX_FORCE + " INTEGER, " 
				+ KEY_MND + " INTEGER " + ")";
		db.execSQL(CREATE_ROPES_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROPES);

		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new contact
	void addRope(Rope rope) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, rope.getName()); //  Name
		values.put(KEY_LAST_UPDATE, rope.getLastUpdate()); //  Last update
		values.put(KEY_USAGE, rope.getUsage()); // Usage
		values.put(KEY_MAX_FORCE, rope.getMaxForce()); // Max force
		values.put(KEY_MND, rope.getMND());

		// Inserting Row
		db.insert(TABLE_ROPES, null, values);
		db.close(); // Closing database connection
	}

	// Getting single contact
	Rope getRope(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_ROPES, new String[] { KEY_ID,
				KEY_NAME,KEY_LAST_UPDATE, KEY_USAGE,
				KEY_MAX_FORCE, KEY_MND}, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Rope rope = new Rope(cursor.getInt(0),
				cursor.getString(1), cursor.getString(2), cursor.getInt(3)
				, cursor.getFloat(4),cursor.getInt(5));
		// return Rope
		return rope;
	}
	
	// Getting All Contacts
	public List<Rope> getAllRopes() {
		List<Rope> contactList = new ArrayList<Rope>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_ROPES;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Rope rope = new Rope();
				rope.setID(cursor.getInt(0));
				rope.setName(cursor.getString(1));
				rope.setLastUpdate(cursor.getString(2));
				rope.setUsage(cursor.getInt(3));
				rope.setMaxForce(cursor.getFloat(4));
				rope.setMND(cursor.getInt(5));
				
				// Adding Rope to list
				contactList.add(rope);
			} while (cursor.moveToNext());
		}

		// return contact list
		return contactList;
	}

	// Updating single contact
	public int updateContact(Rope rope) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, rope.getName());
		values.put(KEY_LAST_UPDATE, rope.getLastUpdate());
		values.put(KEY_USAGE, rope.getUsage());
		values.put(KEY_MAX_FORCE, rope.getMaxForce());
		values.put(KEY_MND, rope.getMND());

		// updating row
		return db.update(TABLE_ROPES, values, KEY_ID + " = ?",
				new String[] { String.valueOf(rope.getID()) });
	}

/*	// Deleting single contact
	public void deleteContact(Contact contact) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
				new String[] { String.valueOf(contact.getID()) });
		db.close();
	}*/


	// Getting contacts Count
	public int getContactsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_ROPES;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}

}
