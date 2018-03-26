package com.infolga.messengerinfolga;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.pinball83.maskededittext.MaskedEditText;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";
    // UI references.
    private MaskedEditText mPhoneView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Handler mHandlerActiveViwe;
    private boolean shABoolean = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mPhoneView = (MaskedEditText) findViewById(R.id.masked_edit_text);


        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(this);


        Button registration_button = (Button) findViewById(R.id.registration_button);
        registration_button.setOnClickListener(this);

        ServerConnect.instanse(this);

        mHandlerActiveViwe = new MyHandlerActiveViwe();
        DD_SQL.instanse(this).setmHandlerActiveViwe(mHandlerActiveViwe);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        showProgress(shABoolean);


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Log.e("CDA", "onKeyDown Called");
            //onBackPressed();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putBoolean("shABoolean", shABoolean);
        super.onSaveInstanceState(outState);
    }

    // получение ранее сохраненного состояния
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        shABoolean = savedInstanceState.getBoolean("shABoolean");
        showProgress(shABoolean);
    }


    @Override
    public void onBackPressed() {
        Log.e("CDA", "onBackPressed Called");
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }

    private boolean attemptLogin() {

        // Reset errors.
        mPhoneView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String phone = mPhoneView.getUnmaskedText().toString();
        String password = mPasswordView.getText().toString();
        Log.w(phone, this.toString());
        Log.w(password, this.toString());
        boolean cancel = false;
        View focusView = null;
        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        } else {
            if (!isPasswordValid(password)) {

                mPasswordView.setError(getString(R.string.error_Valid_password));
                focusView = mPasswordView;
                cancel = true;

            }
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(phone)) {
            mPhoneView.setError(getString(R.string.error_field_required));
            focusView = mPhoneView;
            cancel = true;
        }
        if (!isEmailValid(phone)) {
            mPhoneView.setError(getString(R.string.error_invalid_phone));
            focusView = mPhoneView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            return false;
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            return true;
            // ServerConnect.instanse(this).getmHandlerServerConnect().sendEmptyMessage(ServerConnect.SERVER_CONNECT);
        }
    }

    private boolean isEmailValid(String phone) {
        return phone.length() == 12 && phone.matches("\\d+");
    }

    private boolean isPasswordValid(String password) {
        Log.w("" + password.matches("^[a-zA-Z0-9]+$"), this.toString());
        return password.length() >= 6 && password.matches("^[a-zA-Z0-9]+$");
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.registration_button:
                Intent intent = new Intent(this, RegistrationActivity.class);
                startActivity(intent);
                break;
            case R.id.email_sign_in_button:

                if (attemptLogin()) {





                    shABoolean = true;
                    showProgress(true);
                    Bundle bundle = new Bundle();
                    bundle.putCharSequence(MSG.XML_ELEMENT_PHONE, mPhoneView.getUnmaskedText().toString());
                    bundle.putCharSequence(MSG.XML_ELEMENT_PASSWORD, mPasswordView.getText().toString());


                    Message message = new Message();
                    message.what=MSG.USER_LOGIN;
                    message.obj= bundle;
                    DD_SQL.instanse(this).HsendMessage(message);
                    Log.e(TAG, mPhoneView.getUnmaskedText());
                }


                break;
            default:
                break;
        }
    }


    private class MyHandlerActiveViwe extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            Log.e(TAG, "# сообщенее: " + msg.what);
//            switch (msg.what) {
//                case ServerConnect.CONNECTION_SUCCESSFUL:
//                    shABoolean = false;
//                    showProgress(false);
//                    Toast.makeText(getApplicationContext(), "SUCCESSFUL", Toast.LENGTH_LONG).show();
//                    finish();
//
//                    break;
//                case ServerConnect.CONNECTION_ERROR:
//                    shABoolean = false;
//                    showProgress(false);
//                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
//                    break;
//                default:
//                    break;
//
//
//            }

            // process incoming messages here


        }
    }


}

