package com.example.juaninstagramapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getName();

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ParseUser currUser = ParseUser.getCurrentUser();

        if ( currUser != null ) goToMainActivity();

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString();
                String password = etUsername.getText().toString();
                login(username, password);
            }
        });
    }

    private void login(String username, String password) {
        // TODO - nav to new activity if login successful
        Log.d(TAG, "logging in");

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                // non null exception means that error occured
                if ( e != null ) {
                    // TODO - handle errors better
                    Log.e(TAG, "Exception occured logging in: ", e);
                    return;
                }

                goToMainActivity();
            }
        });
    }

    private void goToMainActivity() {
        Log.d(TAG, "Login attempt successful, navigating to main activity");
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        // activity won't be in stack so pressing back won't return here
        finish();
    }
}
