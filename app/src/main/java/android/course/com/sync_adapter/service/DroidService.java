package android.course.com.sync_adapter.service;

import android.app.Service;
import android.content.Intent;
import android.course.com.sync_adapter.adapter.SyncAdapter;
import android.course.com.sync_adapter.model.Droid;
import android.os.IBinder;

/**
 * Created by nongdenchet on 5/25/15.
 */
public class DroidService extends Service {
    public enum PARSE_ACTION {
        INSERT,
        UPDATE,
        DELETE,
        QUERY,
    }
    private SyncAdapter mSyncAdapter;
    private static final String TAG = DroidService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        mSyncAdapter = new SyncAdapter(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getStringExtra("action");
        switch (PARSE_ACTION.valueOf(action)) {
            case INSERT:
                String title = intent.getStringExtra("title");
                mSyncAdapter.insertData(title);
                break;
            case UPDATE:
                Droid droid = intent.getParcelableExtra("droid");
                mSyncAdapter.updateData(droid);
                break;
            case DELETE:
                String id = intent.getStringExtra("id");
                mSyncAdapter.deleteData(id);
                break;
            case QUERY:
                mSyncAdapter.syncData();
                break;
        }
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
