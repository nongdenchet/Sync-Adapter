package android.course.com.sync_adapter.database;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

public class DroidContentProvider extends ContentProvider {
	private static final int DROIDS = 1;
	private static final int DROID_ID = 2;
	public static final String AUTHORITY = "android.course.com.sync_adapter.database.contentprovider";
	private static final String BASE_PATH = "droids";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + BASE_PATH);
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/droids";
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/droid";

	// Matcher
	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		sURIMatcher.addURI(AUTHORITY, BASE_PATH, DROIDS);
		sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", DROID_ID);
	}

	// Helper
	private MySQLiteHelper dbHelper;

	@Override
	public boolean onCreate() {
		dbHelper = new MySQLiteHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		// Uisng SQLiteQueryBuilder instead of query() method
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		// Set the table
		queryBuilder.setTables(DroidTable.TABLE_DROID);

		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case DROIDS:
			break;
		case DROID_ID:
			// adding the ID to the original query
			queryBuilder.appendWhere(DroidTable.COLUMN_ID + "="
					+ uri.getLastPathSegment());
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = queryBuilder.query(db, projection, selection,
				selectionArgs, null, null, sortOrder);

		// Notify listeners that implement LoaderCallback
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		long id;
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();


		switch (uriType) {
		case DROIDS:
			id = sqlDB.insertWithOnConflict(DroidTable.TABLE_DROID, DroidTable.COLUMN_ID,
					values, SQLiteDatabase.CONFLICT_REPLACE);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		// Notify listeners that implement LoaderCallback
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(BASE_PATH + "/" + id);
	}

	@Override
	public int bulkInsert(Uri uri, @NonNull ContentValues[] values) {
		int numInserted = 0;
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();

		switch (uriType) {
			case DROIDS:
				sqlDB.beginTransaction();
				try {
					for (ContentValues value : values) {
						long newID = sqlDB.insertWithOnConflict(DroidTable.TABLE_DROID, DroidTable.COLUMN_ID,
								value, SQLiteDatabase.CONFLICT_REPLACE);
					}
					sqlDB.setTransactionSuccessful();
					getContext().getContentResolver().notifyChange(uri, null);
					numInserted = values.length;
				} finally {
					sqlDB.endTransaction();
				}
				break;
		}
		return numInserted;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int rowsDeleted;
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();

		switch (uriType) {
		case DROIDS:
			rowsDeleted = sqlDB.delete(DroidTable.TABLE_DROID, selection,
					selectionArgs);
			break;
		case DROID_ID:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsDeleted = sqlDB.delete(DroidTable.TABLE_DROID,
						DroidTable.COLUMN_ID + "=" + id, null);
			} else {
				rowsDeleted = sqlDB.delete(DroidTable.TABLE_DROID,
						DroidTable.COLUMN_ID + "=" + id + " and " + selection,
						selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		// Notify listeners that implement LoaderCallback
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsDeleted;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int rowsUpdated;
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();

		switch (uriType) {
		case DROIDS:
			rowsUpdated = sqlDB.update(DroidTable.TABLE_DROID, values,
					selection, selectionArgs);
			break;
		case DROID_ID:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsUpdated = sqlDB.update(DroidTable.TABLE_DROID, values,
						DroidTable.COLUMN_ID + "=" + id, null);
			} else {
				rowsUpdated = sqlDB.update(DroidTable.TABLE_DROID, values,
						DroidTable.COLUMN_ID + "=" + id + " and " + selection,
						selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		// Notify listeners that implement LoaderCallback
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}

}
