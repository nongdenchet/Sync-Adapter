package android.course.com.sync_adapter.utils;

import android.content.ContentValues;
import android.course.com.sync_adapter.database.DroidTable;
import android.course.com.sync_adapter.model.Droid;
import android.database.Cursor;

import com.parse.ParseObject;

import java.util.List;

/**
 * Created by nongdenchet on 5/26/15.
 */
public class ParseUtils {
    public static Droid cursorToDroid(Cursor cursor) {
        if (cursor != null) {
            cursor.moveToFirst();
            String id = cursor.getString(cursor.getColumnIndex(DroidTable.COLUMN_ID));
            String title = cursor.getString(cursor.getColumnIndex(DroidTable.COLUMN_TITLE));
            return new Droid(id, title);
        }
        return null;
    }

    public static ContentValues[] fromParseObjectToValues(List<ParseObject> droids) {
        ContentValues[] values = new ContentValues[droids.size()];
        for (int i = 0; i < droids.size(); i++) {
            values[i] = fromParseObjectToValue(droids.get(i));
        }
        return values;
    }

    public static ContentValues fromParseObjectToValue(ParseObject obj) {
        ContentValues value = new ContentValues(2);
        value.put(DroidTable.COLUMN_ID, obj.getObjectId());
        value.put(DroidTable.COLUMN_TITLE, obj.getString("title"));
        return value;
    }
}
