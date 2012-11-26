package com.example.cmput301.application;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseSingleton
{
	private static volatile DatabaseSingleton instance = null;
	
	public static DatabaseSingleton getInstance(Context context)
	{
		if(instance==null)
		{
			synchronized(DatabaseSingleton.class)
			{
				if(instance==null)
				{
					instance = new DatabaseSingleton(context.getApplicationContext());
				}
			}
		}
		return instance;
	}
	private SQLiteDatabase db;
	public DatabaseSingleton(Context context)
	{
		db = new DatabaseOpenHelper(context).getWritableDatabase();
	}
	public SQLiteDatabase getDB()
	{
		return this.db;
	}

}
