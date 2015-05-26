package android.course.com.sync_adapter.adapter;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.course.com.sync_adapter.R;
import android.course.com.sync_adapter.database.DroidContentProvider;
import android.course.com.sync_adapter.database.DroidTable;
import android.course.com.sync_adapter.model.Droid;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

/**
 * Created by nongdenchet on 5/25/15.
 */
public class SyncAdapter {
    private Context mContext;
    private ContentResolver mContentResolver;
    private final String TAG = getClass().getSimpleName();

    public SyncAdapter(Context context) {
        mContext = context;
        mContentResolver = mContext.getContentResolver();
    }

    public void syncData() {
        // Query prase database
        ParseQuery<ParseObject> query = ParseQuery.getQuery(mContext.getString(R.string.droid));

        // Start query in background
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> droids, ParseException e) {
                if (e == null) {
                    // Update database
                    mContentResolver.delete(DroidContentProvider.CONTENT_URI, null, null);
                    mContentResolver
                            .bulkInsert(DroidContentProvider.CONTENT_URI,
                                    fromParseObjectToValues(droids));
                } else {
                    Toast.makeText(mContext, "Problem occurs", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void deleteData(final String id) {
        ParseObject.createWithoutData(mContext.getString(R.string.droid), id)
                .deleteInBackground(new DeleteCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            String where = DroidTable.COLUMN_ID + "=?";
                            mContentResolver.delete(DroidContentProvider.CONTENT_URI, where, new String[]{id});
                        } else {
                            Toast.makeText(mContext, "Problem occurs", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void insertData(String title) {
        final ParseObject object = ParseObject.create("Droid");
        object.put("title", title);
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    ContentValues value = fromParseObjectToValue(object);
                    mContentResolver.insert(DroidContentProvider.CONTENT_URI, value);
                } else {
                    Toast.makeText(mContext, "Problem occurs", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void updateData(final Droid droid) {
        ParseQuery<ParseObject> query = new ParseQuery(mContext.getString(R.string.droid));
        query.whereEqualTo("objectId", droid.getId());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {  // Query the object from parse
                if (e == null || list == null || list.size() == 0) {
                    final ParseObject parseObject = list.get(0);
                    parseObject.put("title", droid.getTitle());
                    parseObject.saveEventually(new SaveCallback() {
                        @Override
                        public void done(ParseException e) { // Update that object
                            if (e == null) {
                                String where = DroidTable.COLUMN_ID + "=?";
                                mContentResolver.update(DroidContentProvider.CONTENT_URI,
                                        fromParseObjectToValue(parseObject), where, new String[]{droid.getId()});
                            } else {
                                Toast.makeText(mContext, "Problem occurs", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(mContext, "Problem occurs", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    // Parse data
    private ContentValues[] fromParseObjectToValues(List<ParseObject> droids) {
        ContentValues[] values = new ContentValues[droids.size()];
        for (int i = 0; i < droids.size(); i++) {
            values[i] = fromParseObjectToValue(droids.get(i));
        }
        return values;
    }

    private ContentValues fromParseObjectToValue(ParseObject obj) {
        ContentValues value = new ContentValues(2);
        value.put(DroidTable.COLUMN_ID, obj.getObjectId());
        value.put(DroidTable.COLUMN_TITLE, obj.getString(mContext.getString(R.string.key_title)));
        return value;
    }
}
