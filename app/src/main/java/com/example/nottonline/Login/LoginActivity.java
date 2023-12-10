package com.example.nottonline.Login;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nottonline.MainActivity;
import com.example.nottonline.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

/**
 * The {@code LoginActivity} class represents the initial screen for user login.
 * It provides user authentication and navigation to the main activity or a
 * password recovery screen. User credentials are checked against predefined
 * values, and successful login redirects the user to the {@code MainActivity}.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Instance of the {@code Users} class for checking user credentials.
     */
    Users users = new Users();

    /**
     * TextInputEditText for entering the username.
     */
    TextInputEditText username_input;

    /**
     * TextInputEditText for entering the password.
     */
    TextInputEditText password_input;

    /**
     * User ID obtained after successful login.
     */
    int ID;

    /**
     * Called when the activity is first created. Responsible for setting up the layout,
     * handling user interactions, and performing user authentication.
     *
     * @param savedInstanceState A {@code Bundle} containing the activity's
     *                           previously saved state, or null if there is none.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Find the ImageButton and TextView by their IDs
        ImageButton imageButton = findViewById(R.id.loginButton);
        TextView textView = findViewById(R.id.forgotPasswordLink2);

        username_input = findViewById(R.id.username_input);
        password_input = findViewById(R.id.outlined_edit_text);

        // Set an OnClickListener for the ImageButton
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an Intent to navigate to another activity
                String username = username_input.getText().toString();
                String password = password_input.getText().toString();
                Login(username, password);
            }
        });

        // Set an OnClickListener for the TextView
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an Intent to navigate to another layout
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Performs user authentication based on the provided username and password.
     * If the credentials match predefined values, the user is redirected to the
     * {@code MainActivity} with the corresponding user ID.
     *
     * @param username The entered username.
     * @param password The entered password.
     */
    public void Login(String username, String password) {
        if (username.equals(users.username) && password.equals(users.password)) {
            ID = users.ID;
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("key", ID);
            startActivity(intent);
            Toast.makeText(this, "Welcome " + username, Toast.LENGTH_LONG).show();
        } else if (username.equals(users.username2) && password.equals(users.password2)) {
            ID = users.ID2;
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("key", ID);
            startActivity(intent);
            Toast.makeText(this, "Welcome " + username, Toast.LENGTH_LONG).show();
        } else if (username.equals(users.username3) && password.equals(users.password3)) {
            ID = users.ID3;
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("key", ID);
            startActivity(intent);
            Toast.makeText(this, "Welcome " + username, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Invalid Username or Password!", Toast.LENGTH_SHORT).show();
        }
    }
}

