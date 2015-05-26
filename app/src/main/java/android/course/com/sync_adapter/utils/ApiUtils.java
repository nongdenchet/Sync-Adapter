package android.course.com.sync_adapter.utils;

import android.os.Build;

/**
 * Created by nongdenchet on 4/29/15.
 */
public class ApiUtils {
    public static final int LEVEL = Build.VERSION.SDK_INT;
    public static final int FROYO = Build.VERSION_CODES.FROYO;
    public static final int GINGERBREAD = Build.VERSION_CODES.GINGERBREAD;
    public static final int GINGERBREAD_MR1 = Build.VERSION_CODES.GINGERBREAD_MR1;
    public static final int HONEYCOMB = Build.VERSION_CODES.HONEYCOMB;
    public static final int ICS = Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    public static final int JELLYBEAN = Build.VERSION_CODES.JELLY_BEAN;
    public static final int JELLY_BEAN_MR1 = Build.VERSION_CODES.JELLY_BEAN_MR1;
    public static final int JELLY_BEAN_MR2 = Build.VERSION_CODES.JELLY_BEAN_MR2;
    public static final int KITKAT = Build.VERSION_CODES.KITKAT;

    public static boolean isMin(int level) {
        return LEVEL >= level;
    }
}
