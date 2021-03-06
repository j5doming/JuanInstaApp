package com.example.juaninstagramapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = LoginActivity.class.getName();
    public static final int SIGN_UP_REQUEST_CODE = 100;

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnSignUp;
    private CheckBox cbRememberMe;
    
    private ParseUser currUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getIntent();

        currUser = ParseUser.getCurrentUser();

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);
        cbRememberMe = findViewById(R.id.cbRememberMe);

        if (currUser != null ) goToMainActivity();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString();
                String password = etUsername.getText().toString();
                login(username, password);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToSignUpActivity();
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
        i.putExtra("RememberMe", cbRememberMe.isChecked());
        startActivity(i);
        // activity won't be in stack so pressing back won't return here
        finish();
    }

    private void goToSignUpActivity() {
        Log.d(TAG, "Navigating to sign up activity");
        Intent i = new Intent(this, SignUpActivity.class);
        startActivityForResult(i, SIGN_UP_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( requestCode == SIGN_UP_REQUEST_CODE && resultCode == RESULT_OK) {
            if ( currUser == null) {
                // log in with new user
                String username = data.getStringExtra("username");
                String password = data.getStringExtra("password");
                login(username, password);
            } else {
                Toast.makeText(this, "Log out of all accounts first!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
