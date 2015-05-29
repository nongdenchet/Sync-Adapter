package android.course.com.sync_adapter.database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.course.com.sync_adapter.R;
import android.course.com.sync_adapter.model.Droid;
import android.course.com.sync_adapter.utils.ParseUtils;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

/**
 * Created by nongdenchet on 5/29/15.
 */
public class DataAccessObject {
    public interface IDatabaseCallBack {
        void onComplete();
        void onStart();
    }

    private Context mContext;
    private ContentResolver mContentResolver;

    public DataAccessObject(Context context) {
        mContext = context;
        mContentResolver = mContext.getContentResolver();
    }

    public void deleteData(final String id, final IDatabaseCallBack callBack) {
        callBack.onStart();
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
                        callBack.onComplete();
                    }
                });
    }

    public void insertData(String title, final IDatabaseCallBack callBack) {
        callBack.onStart();
        final ParseObject object = ParseObject.create("Droid");
        object.put("title", title);
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    ContentValues value = ParseUtils.fromParseObjectToValue(object);
                    mContentResolver.insert(DroidContentProvider.CONTENT_URI, value);
                } else {
                    Toast.makeText(mContext, "Problem occurs", Toast.LENGTH_SHORT).show();
                }
                callBack.onComplete();
            }
        });
    }

    public void updateData(final Droid droid, final IDatabaseCallBack callBack) {
        callBack.onStart();
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
                                        ParseUtils.fromParseObjectToValue(parseObject), where, new String[]{droid.getId()});
                            } else {
                                Toast.makeText(mContext, "Problem occurs", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(mContext, "Problem occurs", Toast.LENGTH_SHORT).show();
                }
                callBack.onComplete();
            }
        });

    }
}
