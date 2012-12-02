package com.example.cmput301.application;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *	Opens/updates our local SQLite Database
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper
{
	private static final String DATABASE_NAME = "Task_DB";
	private static final int DATABASE_VERSION = 4;
	private static String LOCAL_TASK_TABLE_CREATE =
			"CREATE TABLE " + StringRes.LOCAL_TASK_TABLE_NAME + " (" +
					"id" 				+ " TEXT, "  +
					"content" 			+ " TEXT  "  +
					");";
	private static final String REMOTE_TASK_TABLE_CREATE =
			"CREATE TABLE " + StringRes.REMOTE_TASK_TABLE_NAME + " (" +
					"id" 				+ " TEXT, "  +
					"content" 			+ " TEXT  "  +
					");";

	public DatabaseOpenHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(LOCAL_TASK_TABLE_CREATE);
		db.execSQL(REMOTE_TASK_TABLE_CREATE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		if(oldVersion < 4)
		{
			final String DROP_TASK_TABLE =
					"DROP TABLE task_table;";
			db.execSQL(DROP_TASK_TABLE);
			db.execSQL(LOCAL_TASK_TABLE_CREATE);
			db.execSQL(REMOTE_TASK_TABLE_CREATE);
					
		}

	}

}
