package android.course.com.sync_adapter.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.course.com.sync_adapter.R;
import android.course.com.sync_adapter.activity.LoginCallBack;
import android.course.com.sync_adapter.database.DataAccessObject;
import android.course.com.sync_adapter.database.DroidContentProvider;
import android.course.com.sync_adapter.database.DroidTable;
import android.course.com.sync_adapter.model.Droid;
import android.course.com.sync_adapter.utils.NetworkUtils;
import android.course.com.sync_adapter.utils.ParseUtils;
import android.course.com.sync_adapter.utils.SyncUtils;
import android.course.com.sync_adapter.utils.UiUtils;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.melnykov.fab.FloatingActionButton;

/**
 * Created by nongdenchet on 5/25/15.
 */
public class DroidListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int ANIM_DURATION_TOOLBAR = 300;
    private static final int ANIM_DURATION_LISTVIEW = 600;
    private static final int ANIM_DURATION_ADDBUTTON = 900;
    private boolean hasAnimation = false;

    private Toolbar mToolbar;
    private ProgressDialog mProgressDialog;
    private Context mContext;
    private FloatingActionButton mAddButton;
    private TextView mNoDroidText;
    private ListView mListView;

    private SimpleCursorAdapter mAdapter;
    private String[] mCursorFrom = new String[]{DroidTable.COLUMN_TITLE};
    private int[] mCursorTo = new int[]{R.id.title};
    private LoginCallBack mCallback;
    private DataAccessObject dataAccessObject;

    public DroidListFragment() {
    }

    public static DroidListFragment newInstance(Context context) {
        DroidListFragment fragment = new DroidListFragment();
        fragment.mContext = context;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataAccessObject = new DataAccessObject(mContext);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SyncUtils.triggerRefresh(mContext);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallback = (LoginCallBack) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View root = inflater.inflate(R.layout.fragment_droid_list, container, false);
        setUpActionBar(root);
        setUpProgressDialog();
        setUpListView(root);
        setUpFloatingButton(root);
        return root;
    }

    private void setUpActionBar(View root) {
        mToolbar = (Toolbar) root.findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.app_name);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

        // Init position
        int actionbarSize = (int) UiUtils.dpToPx(56, mContext);
        mToolbar.setTranslationY(-actionbarSize);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpProgressDialog() {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Syncing...");
        mProgressDialog.setCanceledOnTouchOutside(false);
    }

    private void setUpListView(View root) {
        // Setup list view
        mNoDroidText = (TextView) root.findViewById(R.id.no_droids);
        mListView = (ListView) root.findViewById(R.id.list_droids);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cursor = (Cursor) mListView.getAdapter().getItem(i);
                Toast.makeText(mContext, cursor.getString(cursor.getColumnIndex(DroidTable.COLUMN_TITLE)),
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Register context menu
        registerForContextMenu(mListView);

        // Init position
        mListView.setTranslationY(UiUtils.getScreenSize(mContext).getHeight());
    }

    // Start the animation
    private void startIntroAnimation() {
        if (hasAnimation) return;
        hasAnimation = true;
        mToolbar.animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(300)
                .start();
        mListView.animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_LISTVIEW)
                .setStartDelay(400)
                .start();
        mAddButton.animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_ADDBUTTON)
                .setStartDelay(500)
                .start();
    }

    // Update data for the list
    private void refreshDroidList(Cursor data) {
        mAdapter = new SimpleCursorAdapter(mContext, R.layout.item_droid,
                data, mCursorFrom, mCursorTo, 0);
        mListView.setAdapter(mAdapter);

        if (mAdapter.getCount() == 0) {
            mListView.setVisibility(View.INVISIBLE);
            mNoDroidText.setVisibility(View.VISIBLE);
        } else {
            mListView.setVisibility(View.VISIBLE);
            mNoDroidText.setVisibility(View.INVISIBLE);
        }

        // Start animation
        startIntroAnimation();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_reset:
                // Check internet
                if (NetworkUtils.isOnline(mContext))
                    SyncUtils.triggerRefresh(mContext);
                return true;
            case R.id.action_logout:
                // Check internet
                if (NetworkUtils.isOnline(mContext))
                    mCallback.onLogout();
                return true;
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, v.getId(), 0, mContext.getString(R.string.context_menu_delete_droid));
        menu.add(0, v.getId(), 0, mContext.getString(R.string.context_menu_update_droid));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // Retrieve data
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Cursor cursor = (Cursor) mListView.getAdapter().getItem(info.position);

        if (!NetworkUtils.isOnline(mContext)) { // Check network state
            return true;
        } else if (item.getTitle().equals(mContext.getString(R.string.context_menu_delete_droid))) {
            extractDataAndStartDelete(cursor);
            return true;
        } else if (item.getTitle().equals(mContext.getString(R.string.context_menu_update_droid))) {
            extractDataAndStartUpdate(cursor);
            return true;
        }
        return super.onContextItemSelected(item);
    }

    // Ask for update
    private void extractDataAndStartUpdate(Cursor cursor) {
        final Droid droid = ParseUtils.cursorToDroid(cursor);
        new MaterialDialog.Builder(getActivity())
                .negativeText("Cancel")
                .title("Type in your new title")
                .inputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS)
                .input("Title", droid.getTitle(), new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        // Do something
                        droid.setTitle(input.toString());
                        dataAccessObject.updateData(droid, new DataAccessObject.IDatabaseCallBack() {
                            @Override
                            public void onComplete() {
                                mProgressDialog.dismiss();
                            }

                            @Override
                            public void onStart() {
                                mProgressDialog.show();
                            }
                        });
                    }
                }).show();
    }

    // Ask for delete
    private void extractDataAndStartDelete(Cursor cursor) {
        String id = cursor.getString(cursor.getColumnIndex(DroidTable.COLUMN_ID));
        dataAccessObject.deleteData(id, new DataAccessObject.IDatabaseCallBack() {
            @Override
            public void onComplete() {
                mProgressDialog.dismiss();
            }

            @Override
            public void onStart() {
                mProgressDialog.show();
            }
        });
    }

    // Button for create new droid
    private void setUpFloatingButton(View root) {
        mAddButton = (FloatingActionButton) root.findViewById(R.id.fab);
        mAddButton.setTranslationY(UiUtils.getScreenSize(mContext).getHeight()
                + UiUtils.dpToPx(100, mContext));
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(getActivity())
                        .negativeText("Cancel")
                        .title("Type in your droid title")
                        .inputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS)
                        .input("Title", "", new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                // Request add
                                dataAccessObject.insertData(input.toString(), new DataAccessObject.IDatabaseCallBack() {
                                    @Override
                                    public void onComplete() {
                                        mProgressDialog.dismiss();
                                    }

                                    @Override
                                    public void onStart() {
                                        mProgressDialog.show();
                                    }
                                });
                            }
                        }).show();
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {DroidTable.COLUMN_ID,
                DroidTable.COLUMN_TITLE};
        return new CursorLoader(mContext, DroidContentProvider.CONTENT_URI,
                projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            Toast.makeText(mContext, getString(R.string.update), Toast.LENGTH_SHORT).show();
            refreshDroidList(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        refreshDroidList(null);
    }
}