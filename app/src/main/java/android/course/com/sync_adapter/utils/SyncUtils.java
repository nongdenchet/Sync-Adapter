package android.course.com.sync_adapter.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.course.com.sync_adapter.database.DroidContentProvider;
import android.course.com.sync_adapter.service.AccountService;
import android.os.Bundle;

/**
 * Created by nongdenchet on 5/29/15.
 */
public class SyncUtils {
    private static final String CONTENT_AUTHORITY = DroidContentProvider.AUTHORITY;

    /**
     * Create an entry for this application in the system account list, if it isn't already there.
     *
     * @param context Context
     */
    public static void CreateSyncAccount(Context context) {
        boolean newAccount = false;
        boolean setupComplete = PrefUtils.getInstance(context)
                .get("setup_complete", false);
        String password = PrefUtils.getInstance(context)
                .get("password", "");

        // Create account, if it's missing. (Either first run, or user has deleted account.)
        Account account = AccountService.getAccount(context);
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account
        if (accountManager.addAccountExplicitly(account, password, null)) {
            // Inform the system that this account supports sync
            ContentResolver.setIsSyncable(account, CONTENT_AUTHORITY, 1);
            newAccount = true;
        }

        // Schedule an initial sync
        if (newAccount || !setupComplete) {
            triggerRefresh(context);
            PrefUtils.getInstance(context).set("setup_complete", true);
        }
    }

    /**
     * Helper method to trigger an immediate sync ("refresh").
     */
    public static void triggerRefresh(Context context) {
        Bundle bundle = new Bundle();

        // Disable sync backoff and ignore sync preferences. In other words...perform sync now
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(
                AccountService.getAccount(context),
                DroidContentProvider.AUTHORITY,
                bundle);
    }
}
