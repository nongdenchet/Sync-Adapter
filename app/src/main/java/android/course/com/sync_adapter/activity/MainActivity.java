package android.course.com.sync_adapter.activity;

import android.course.com.sync_adapter.R;
import android.course.com.sync_adapter.fragment.DroidListFragment;
import android.course.com.sync_adapter.utils.PrefUtils;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.SaveCallback;


public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PrefUtils.getInstance(getApplicationContext()).set(getString(R.string.app_running), true);

        // Start polling service
        registerPush();

        // Set up fragment
        DroidListFragment fragment = DroidListFragment.newInstance(getApplicationContext());
        // LoginFragment fragment = LoginFragment.newInstance();
        addFragment(fragment);
    }

    /**
     * register notification to Parse server
     */
    private void registerPush() {
        // Register push notification
        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d(TAG, "Successfully subscribed to the broadcast channel");
                } else {
                    Log.e(TAG, "Failed to subscribe for push", e);
                }
            }
        });
    }

    /**
     * add Fragment into this activity
     * @param fragment
     * The fragment you want to replace for the activity
     */
    private void addFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        PrefUtils.getInstance(getApplicationContext()).set(getString(R.string.app_running), false);
        super.onDestroy();
    }
}
