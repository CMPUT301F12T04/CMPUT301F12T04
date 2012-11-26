package com.example.cmput301;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper
{
	private static final String DATABASE_NAME = "Task_DB";
	private static final int DATABASE_VERSION = 4;
	private static final String LOCAL_TASK_TABLE_NAME = "local_task_table";
	private static final String REMOTE_TASK_TABLE_NAME = "remote_task_table";
	private static final String LOCAL_TASK_TABLE_CREATE =
			"CREATE TABLE " + LOCAL_TASK_TABLE_NAME + " (" +
					"id" 				+ " TEXT, "  +
					"content" 			+ " TEXT  "  +
					");";
	private static final String REMOTE_TASK_TABLE_CREATE =
			"CREATE TABLE " + REMOTE_TASK_TABLE_NAME + " (" +
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
		// TODO Auto-generated method stub
		db.execSQL(LOCAL_TASK_TABLE_CREATE);
		db.execSQL(REMOTE_TASK_TABLE_CREATE);
	}
	

	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// TODO Auto-generated method stub
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
