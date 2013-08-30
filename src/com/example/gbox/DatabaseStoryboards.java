package com.example.gbox;

import java.util.ArrayList;
import java.util.List;

import com.example.gbox.addrope.Rope;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseStoryboards extends SQLiteOpenHelper{
	
	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "storyboard";

	// Contacts table name
	
	private static final String TABLE_STORY = "story";

	// Contacts Table Columns names
	
	private static final String KEY_ID = "id";
	private static final String KEY_DATE = "date";
	private static final String KEY_DUMPS = "dumps";
	private static final String KEY_MAX_ACC = "max_acc";
	private static final String KEY_MIN_ACC = "min_acc";
	
	
	public DatabaseStoryboards(Context context) {
		
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String CREATE_ROPES_TABLE = "CREATE TABLE " + TABLE_STORY + "("
				+ KEY_ID + " INTEGER PRIMARY KEY, " 
				+ KEY_DATE + " TEXT, "
				+ KEY_DUMPS + " INTEGER, " 
				+ KEY_MAX_ACC + " INTEGER, " 
				+ KEY_MIN_ACC + " INTEGER " + ")";
		db.execSQL(CREATE_ROPES_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		// Drop older table if existed
				db.execSQL("DROP TABLE IF EXISTS " + TABLE_STORY);

				// Create tables again
				onCreate(db);
	}

	// Adding new contact
	void addStory(Story story) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_DATE, story.getDate()); //  Name
		values.put(KEY_DUMPS, story.getDumps()); //  Last update
		values.put(KEY_MAX_ACC, story.getMaxAcc()); // Usage
		values.put(KEY_MIN_ACC, story.getMinAcc()); // Max force

		// Inserting Row
		db.insert(TABLE_STORY, null, values);
		db.close(); // Closing database connection
	}
	
	// Getting single contact
		Story getStory(int id) {
			SQLiteDatabase db = this.getReadableDatabase();

			Cursor cursor = db.query(TABLE_STORY, new String[] { KEY_ID,
					KEY_DATE,KEY_DUMPS, KEY_MAX_ACC,
					KEY_MIN_ACC}, KEY_ID + "=?",
					new String[] { String.valueOf(id) }, null, null, null, null);
			if (cursor != null)
				cursor.moveToFirst();

			Story story = new Story(cursor.getString(1),
					 cursor.getInt(2), cursor.getInt(3)
					, cursor.getInt(4));
			// return Rope
			return story;
		}
		
		// Getting All Contacts
		public List<Story> getAllStory() {
			List<Story> storiesList = new ArrayList<Story>();
			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_STORY;

			SQLiteDatabase db = getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Story story = new Story();
					story.setID(cursor.getInt(0));
					story.setDate(cursor.getString(1));
					story.setDumps(cursor.getInt(2));
					story.setMaxAcc(cursor.getInt(3));
					story.setMinAcc(cursor.getInt(4));
					
					
					// Adding Rope to list
					storiesList.add(story);
				} while (cursor.moveToNext());
			}

			// return contact list
			return storiesList;
		}

		// Updating single contact
		public int updateStory(Story story) {
			SQLiteDatabase db = this.getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(KEY_DATE, story.getDate());
			values.put(KEY_DUMPS, story.getDumps());
			values.put(KEY_MAX_ACC, story.getMaxAcc());
			values.put(KEY_MIN_ACC, story.getMinAcc());

			// updating row
			return db.update(TABLE_STORY, values, KEY_ID + " = ?",
					new String[] { String.valueOf(story.getID()) });
		}

		// Deleting single contact
		public void deleteStory(Story story) {
			SQLiteDatabase db = this.getWritableDatabase();
			db.delete(TABLE_STORY, KEY_ID + " = ?",
					new String[] { String.valueOf(story.getID()) });
			db.close();
		}


		// Getting contacts Count
		public int getStoryCount() {
			String countQuery = "SELECT  * FROM " + TABLE_STORY;
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(countQuery, null);
			cursor.close();

			// return count
			return cursor.getCount();
		}		
}
