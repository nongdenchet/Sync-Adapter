package android.course.com.sync_adapter.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.course.com.sync_adapter.R;
import android.course.com.sync_adapter.activity.LoginCallBack;
import android.course.com.sync_adapter.utils.AccountUtils;
import android.course.com.sync_adapter.utils.NetworkUtils;
import android.course.com.sync_adapter.utils.PrefUtils;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Created by nongdenchet on 5/25/15.
 */
public class LoginFragment extends Fragment {
    private EditText mUserNameEdt, mPasswordEdt, mEmailEdt;
    private TextView mHelper, mSubmitBtn;
    private ProgressDialog mProgressDialog;
    private boolean login = true;
    private PrefUtils mPrefs;
    private LoginCallBack mCallBack;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPrefs = PrefUtils.getInstance(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login, container, false);
        setUpView(root);
        setUpProgressDialog();
        return root;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallBack = (LoginCallBack) activity;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
    }

    private void setUpProgressDialog() {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMessage(getString(R.string.loading));
    }

    /**
     * Disable email text field
     */
    private void disableEmail() {
        mEmailEdt.setAlpha(0.4f);
        mEmailEdt.setEnabled(false);
    }

    /**
     * Enable email text field
     */
    private void enableEmail() {
        mEmailEdt.setAlpha(1);
        mEmailEdt.setEnabled(true);
    }

    private void setUpView(View root) {
        mUserNameEdt = (EditText) root.findViewById(R.id.edt_username);
        mPasswordEdt = (EditText) root.findViewById(R.id.edt_password);
        mEmailEdt = (EditText) root.findViewById(R.id.edt_email);
        disableEmail();

        mSubmitBtn = (TextView) root.findViewById(R.id.submit_button);
        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginSignUp();
            }
        });

        mHelper = (TextView) root.findViewById(R.id.helper);
        mHelper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login = !login;
                if (login) {
                    mHelper.setText(getString(R.string.sign_up));
                    mSubmitBtn.setText(getString(R.string.login));
                    disableEmail();
                } else {
                    mHelper.setText(getString(R.string.login));
                    mSubmitBtn.setText(getString(R.string.sign_up));
                    enableEmail();
                }
            }
        });
    }

    private void loginSignUp() {
        final String username = mUserNameEdt.getText().toString().trim();
        final String password = mPasswordEdt.getText().toString().trim();
        String email = mEmailEdt.getText().toString().trim();

        // Check network
        if (!NetworkUtils.isOnline(getActivity()))
            return;

        // Check valid
        if (!AccountUtils.checkValidNameOrPassword(username)
                || !AccountUtils.checkValidNameOrPassword(password)) {
            Toast.makeText(getActivity(), getString(R.string.check_string),
                    Toast.LENGTH_LONG).show();
            return;
        }

        if (login) {
            // Start loging-in
            mProgressDialog.show();
            ParseUser.logInInBackground(username, password, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    mProgressDialog.dismiss();
                    if (e == null) {
                        // SuccessPrefUtils prefUtils = PrefUtils.getInstance(getActivity());
                        mPrefs.set("username", username);
                        mPrefs.set("password", password);
                        mCallBack.login();
                    } else {
                        // Fail
                        showDialog(e.getMessage());
                    }
                }
            });
        } else {
            // Check email
            if (!AccountUtils.isEmailValid(email)) {
                Toast.makeText(getActivity(), getString(R.string.invalid_email),
                        Toast.LENGTH_LONG).show();
                return;
            }

            // Create new user
            ParseUser newUser = new ParseUser();
            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.setEmail(email);

            // Start signing-up
            mProgressDialog.show();
            newUser.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    mProgressDialog.dismiss();
                    if (e == null) {
                        // Success
                        showDialog(getString(R.string.success_account));
                    } else {
                        // Fail
                        showDialog(e.getMessage());
                    }
                }
            });
        }
    }

    private void showDialog(String title) {
        new MaterialDialog.Builder(getActivity())
                .title(title)
                .positiveText("Ok")
                .show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
