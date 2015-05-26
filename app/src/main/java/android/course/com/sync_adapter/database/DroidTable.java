package android.course.com.sync_adapter.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DroidTable {
	// Database table
	public static final String TABLE_DROID = "droid_table";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TITLE = "title";

	// Database creation SQL statement
	private static final String DATABASE_CREATE = "create table " + TABLE_DROID
			+ "(" + COLUMN_ID + " text not null, "
			+ COLUMN_TITLE + " text not null" + ");";

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		Log.w(DroidTable.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_DROID);
		onCreate(database);
	}
}
