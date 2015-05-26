package android.course.com.sync_adapter.application;

import android.app.Application;
import android.course.com.sync_adapter.R;

import com.parse.Parse;

/**
 * Created by nongdenchet on 5/24/15.
 */
public class DroidApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Initalize the parse with keys
        Parse.initialize(this, getString(R.string.application_parse_id),
                getString(R.string.client_parse_id));
    }
}
