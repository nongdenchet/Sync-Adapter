package android.course.com.sync_adapter.adapter;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.course.com.sync_adapter.R;
import android.course.com.sync_adapter.database.DroidContentProvider;
import android.course.com.sync_adapter.utils.ParseUtils;
import android.os.Bundle;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by nongdenchet on 5/25/15.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {
    private Context mContext;
    private ContentResolver mContentResolver;
    private final String TAG = getClass().getSimpleName();

    public SyncAdapter(Context context, boolean authorize) {
        super(context, authorize);
        mContext = context;
        mContentResolver = mContext.getContentResolver();
    }

    public void syncData() {
        // Query parse database
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
                                    ParseUtils.fromParseObjectToValues(droids));
                    Toast.makeText(mContext, mContext.getString(R.string.update), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "Problem occurs", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String s,
                              ContentProviderClient contentProviderClient, SyncResult syncResult) {
            // Perform syncing data from the server
            syncData();
    }
}
