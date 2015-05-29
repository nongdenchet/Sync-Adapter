package android.course.com.sync_adapter.service;

import android.app.Service;
import android.content.Intent;
import android.course.com.sync_adapter.adapter.SyncAdapter;
import android.os.IBinder;

/**
 * Created by nongdenchet on 5/25/15.
 */
public class DroidService extends Service {
    private static final Object mSyncAdapterLock = new Object();
    private SyncAdapter mSyncAdapter;
    private static final String TAG = DroidService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        synchronized (mSyncAdapterLock) {
            if (mSyncAdapter == null)
                mSyncAdapter = new SyncAdapter(getApplicationContext(), true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mSyncAdapter.getSyncAdapterBinder();
    }
}
