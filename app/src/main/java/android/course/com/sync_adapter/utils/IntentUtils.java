package android.course.com.sync_adapter.utils;

import android.content.Context;
import android.content.Intent;
import android.course.com.sync_adapter.model.Droid;
import android.course.com.sync_adapter.service.DroidService;

/**
 * Created by nongdenchet on 5/25/15.
 */
public class IntentUtils {

    public static void startDroidServiceQuery(Context context) {
        Intent intent = new Intent(context, DroidService.class);
        intent.putExtra("action", DroidService.PARSE_ACTION.QUERY.toString());
        context.startService(intent);
    }

    public static void startDroidServiceDelete(Context context, String id) {
        Intent intent = new Intent(context, DroidService.class);
        intent.putExtra("action", DroidService.PARSE_ACTION.DELETE.toString());
        intent.putExtra("id", id);
        context.startService(intent);
    }

    public static void startDroidServiceUpdate(Context context, Droid droid) {
        Intent intent = new Intent(context, DroidService.class);
        intent.putExtra("action", DroidService.PARSE_ACTION.UPDATE.toString());
        intent.putExtra("droid", droid);
        context.startService(intent);
    }

    public static void startDroidServiceInsert(Context context, String title) {
        Intent intent = new Intent(context, DroidService.class);
        intent.putExtra("action", DroidService.PARSE_ACTION.INSERT.toString());
        intent.putExtra("title", title);
        context.startService(intent);
    }

    public static void stopDroidService(Context context) {
        Intent intent = new Intent(context, DroidService.class);
        context.stopService(intent);
    }

}
