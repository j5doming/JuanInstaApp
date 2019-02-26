package com.example.juaninstagramapp;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    public static final String TAG = SignUpActivity.class.getName();

    private Button btnNewSignUp;
    private EditText etEmail;
    private EditText etUsername;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btnNewSignUp = findViewById(R.id.btnNewSignUp);
        etEmail = findViewById(R.id.email);
        etUsername = findViewById(R.id.etNewUsername);
        etPassword = findViewById(R.id.etNewPassword);

        btnNewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString();
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                signup(email, username, password);
            }
        });
    }

    private void signup(final String email, final String username, final String password) {
        Log.d(TAG, "Signing up new user ");

        // create a new ParseUser object that will be signed up
        ParseUser user = new ParseUser();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);

        user.put("handle", username);

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                Intent i = getIntent();
                if ( e == null ) {
                    Log.i(TAG, "Success signing up, logging in");
                    // return to login activity and automatically log in
                    i.putExtra("username", username);
                    i.putExtra("password", password);
                    setResult(RESULT_OK, i);
                    finish();
                } else {
                    Log.e(TAG, "Sign up error occured with username " + username + "(" + email + ") : ", e);
                    setResult(RESULT_CANCELED, i);
                    Toast.makeText(SignUpActivity.this, "Couldn't Sign up! Please Try again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
