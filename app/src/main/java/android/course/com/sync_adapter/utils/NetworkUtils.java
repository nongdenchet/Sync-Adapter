package android.course.com.sync_adapter.utils;

import android.content.Context;
import android.course.com.sync_adapter.R;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by nongdenchet on 5/25/15.
 */
public class NetworkUtils {
    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        boolean status = netInfo != null && netInfo.isConnectedOrConnecting();
        if (!status)
            Toast.makeText(context, context.getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        return status;
    }
}
