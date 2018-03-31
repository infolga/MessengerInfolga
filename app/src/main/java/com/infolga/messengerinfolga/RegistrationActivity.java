package com.infolga.messengerinfolga;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.github.pinball83.maskededittext.MaskedEditText;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "RegistrationActivity";

    private MaskedEditText mPhoneView;
    private EditText mPasswordView;
    private EditText mPasswordReturnView;
    private EditText mUserNameView;
    private EditText mFirstNameView;
    private EditText mLastNameView;
    private EditText mEmailNameView;
    private Handler mHandlerActiveViwe;
    private View mProgressView;
    private View mRegistrationFormView;
    private boolean shABoolean = false;
    private FloatingActionButton floatingActionButton;

    private Toolbar mToolbar;

    @Override
    public void onClick(View view) {
        int i = view.getId();


        switch (view.getId()) {
            case (R.id.fab):

                if (attemptRegistration()) {
                    shABoolean = true;
                    showProgress(true);

                    Bundle bundle = new Bundle();
                    bundle.putCharSequence(MSG.XML_ELEMENT_PHONE, mPhoneView.getUnmaskedText());
                    bundle.putCharSequence(MSG.XML_ELEMENT_PASSWORD, mPasswordView.getText().toString());
                    bundle.putCharSequence(MSG.XML_ELEMENT_USER_NAME, mUserNameView.getText().toString());
                    bundle.putCharSequence(MSG.XML_ELEMENT_FIRST_NAME, mFirstNameView.getText().toString());
                    bundle.putCharSequence(MSG.XML_ELEMENT_LAST_NAME, mLastNameView.getText().toString());
                    bundle.putCharSequence(MSG.XML_ELEMENT_EMAIL, mEmailNameView.getText().toString());


                    Message message = new Message();
                    message.what = MSG.USER_REGISTRATION;
                    message.obj = bundle;
                    DD_SQL.instanse(this).HsendMessage(message);
                    Log.e(TAG, mPhoneView.getUnmaskedText());


                }
                break;

            default:
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Log.e("CDA", "onKeyDown Called");
            if (!shABoolean) {
                onBackPressed();
            }

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
          mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);



        mPhoneView = findViewById(R.id.masked_edit_text);
        mPasswordView = findViewById(R.id.password);
        mPasswordReturnView = findViewById(R.id.passwordReturn);
        mUserNameView = findViewById(R.id.user_name);
        mFirstNameView = findViewById(R.id.first_name);
        mLastNameView = findViewById(R.id.last_name);
        mEmailNameView = findViewById(R.id.email);
        floatingActionButton = findViewById(R.id.fab);

        floatingActionButton.setOnClickListener(this);

        mRegistrationFormView = findViewById(R.id.registration_form);
        mProgressView = findViewById(R.id.registration_progress);

        mHandlerActiveViwe = new MyHandlerActiveViwe();
        DD_SQL.instanse(this).setmHandlerActiveViwe(mHandlerActiveViwe);
        showProgress(shABoolean);

    }


    private boolean attemptRegistration() {

        mPhoneView.setError(null);
        mPasswordView.setError(null);
        mPasswordReturnView.setError(null);
        mUserNameView.setError(null);
        mFirstNameView.setError(null);
        mLastNameView.setError(null);
        mEmailNameView.setError(null);

        // Store values at the time of the login attempt.
        String phone = mPhoneView.getUnmaskedText();
        String password = mPasswordView.getText().toString();
        String password2 = mPasswordReturnView.getText().toString();
        String username = mUserNameView.getText().toString();
        String firstname = mFirstNameView.getText().toString();
        String lastname = mLastNameView.getText().toString();
        String email = mEmailNameView.getText().toString();

        boolean cancel = false;
        View focusView = null;

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

        if (!isPasswordMatch(password, password2)) {
            mPasswordReturnView.setError(getString(R.string.error_not_match));
            focusView = mPasswordReturnView;
            cancel = true;
        }
        if (!isPasswordValid(username)) {
            mUserNameView.setError(getString(R.string.error_invalid_username));
            focusView = mUserNameView;
            cancel = true;
        }
        if (!isPasswordValid(firstname)) {
            mFirstNameView.setError(getString(R.string.error_invalid_firstname));
            focusView = mFirstNameView;
            cancel = true;
        }
        if (lastname.length() > 20) {
            mLastNameView.setError(getString(R.string.error_invalid_field));
            focusView = mLastNameView;
            cancel = true;
        }
        if (email.length() > 20 || (email.length() > 0 && !email.contains("@".toLowerCase()))) {
            mEmailNameView.setError(getString(R.string.error_invalid_field));
            focusView = mEmailNameView;
            cancel = true;
        }
        if (cancel) {

            focusView.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        DD_SQL.instanse(this).setmHandlerActiveViwe(mHandlerActiveViwe);
    }

    private boolean isEmailValid(String phone) {
        return phone.length() == 12 && phone.matches("\\d+");
    }

    private boolean isPasswordValid(String password) {
        return (password.length() >= 6 && password.length() < 14 && password.matches("^[a-zA-Z0-9]+$"));
    }

    private boolean isPasswordMatch(String password, String password2) {
        return password.equals(password2);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.


        if (show) {
            floatingActionButton.hide();
        } else {
            floatingActionButton.show();
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {

            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);


            mRegistrationFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegistrationFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegistrationFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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

            floatingActionButton.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mRegistrationFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private class MyHandlerActiveViwe extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Log.e(TAG, msg.toString());


            switch (msg.what) {
                case MSG.USER_REGISTRATION_SUCCESSFUL:

                    finish();


                    break;
                case MSG.USER_REGISTRATION_FAIL_PHONE:
                    shABoolean = false;
                    showProgress(false);
                    mPhoneView.setError(getString(R.string.error_field_required));
                    mPhoneView.requestFocus();

                    break;
                case MSG.USER_REGISTRATION_FAIL_USER_NAME:
                    shABoolean = false;
                    showProgress(false);
                    mUserNameView.setError(getString(R.string.error_invalid_username));
                    mUserNameView.requestFocus();
                    break;
                default:
                    break;


            }
        }
    }

}


