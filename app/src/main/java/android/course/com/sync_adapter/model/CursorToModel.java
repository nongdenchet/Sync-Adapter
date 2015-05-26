package android.course.com.sync_adapter.model;

import android.course.com.sync_adapter.database.DroidTable;
import android.database.Cursor;

/**
 * Created by nongdenchet on 5/26/15.
 */
public class CursorToModel {
    public static Droid cursorToDroid(Cursor cursor) {
        if (cursor != null) {
            cursor.moveToFirst();
            String id = cursor.getString(cursor.getColumnIndex(DroidTable.COLUMN_ID));
            String title = cursor.getString(cursor.getColumnIndex(DroidTable.COLUMN_TITLE));
            return new Droid(id, title);
        }
        return null;
    }
}
