package com.infolga.messengerinfolga;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
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

    @Override
    public void onClick(View view) {
        int i = view.getId();
        switch (view.getId()) {
            case (R.id.fab):
                attemptRegistration();
                break;

            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mPhoneView = (MaskedEditText) findViewById(R.id.masked_edit_text);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordReturnView = (EditText) findViewById(R.id.passwordReturn);
        mUserNameView = (EditText) findViewById(R.id.user_name);
        mFirstNameView = (EditText) findViewById(R.id.first_name);
        mLastNameView = (EditText) findViewById(R.id.last_name);
        mEmailNameView = (EditText) findViewById(R.id.email);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void attemptRegistration() {
//        if (mAuthTask != null) {
//            return;
//        }

        // Reset errors.
        mPhoneView.setError(null);
        mPasswordView.setError(null);
        mPasswordReturnView.setError(null);
        mUserNameView.setError(null);
        mFirstNameView.setError(null);
        mLastNameView.setError(null);
        mEmailNameView.setError(null);

        // Store values at the time of the login attempt.
        String phone = mPhoneView.getUnmaskedText().toString();
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

        if (!isPasswordMatch(password,password2)){
            mPasswordReturnView.setError(getString(R.string.error_not_match));
            focusView = mPasswordReturnView;
            cancel = true;
        }
        if (!isPasswordValid(username)){
            mUserNameView.setError(getString(R.string.error_invalid_username));
            focusView = mUserNameView;
            cancel = true;
        }
        if (!isPasswordValid(firstname)){
            mFirstNameView.setError(getString(R.string.error_invalid_firstname));
            focusView = mFirstNameView;
            cancel = true;
        }
        if ( lastname.length()>20 ){
            mLastNameView.setError(getString(R.string.error_invalid_field));
            focusView = mLastNameView;
            cancel = true;
        }
        if ( email.length()>20 ||( email.length()>0&& !email.contains("@".toLowerCase()))){
            mEmailNameView.setError(getString(R.string.error_invalid_field));
            focusView = mEmailNameView;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
           /* showProgress(true);
            mAuthTask = new LoginActivity.UserLoginTask(phone, password);
            mAuthTask.execute((Void) null);*/
        }
    }


    private boolean isEmailValid(String phone) {
        return phone.length() == 12 && phone.matches("\\d+");
    }

    private boolean isPasswordValid(String password) {
        return (password.length() >= 6 && password.length() <14 && password.matches("^[a-zA-Z0-9]+$"));
    }

    private boolean isPasswordMatch(String password, String password2) {
        return password.equals(password2);
    }

}
