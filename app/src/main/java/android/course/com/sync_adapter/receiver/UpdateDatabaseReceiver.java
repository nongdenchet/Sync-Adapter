package android.course.com.sync_adapter.receiver;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.course.com.sync_adapter.R;
import android.course.com.sync_adapter.utils.IntentUtils;
import android.course.com.sync_adapter.utils.PrefUtils;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;

/**
 * Created by nongdenchet on 5/25/15.
 */
public class UpdateDatabaseReceiver extends ParsePushBroadcastReceiver {
    private final String TAG = UpdateDatabaseReceiver.class.getSimpleName();

    @Override
    protected Notification getNotification(Context context, Intent intent) {
        // Deactive notification
        return null;
    }

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        Log.d(TAG, "Receive");
        if (PrefUtils.getInstance(context).get(context.getString(R.string.app_running), false))
            IntentUtils.startDroidServiceQuery(context);
    }

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        Log.d(TAG, "Open");
    }

}