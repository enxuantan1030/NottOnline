package com.example.nottonline.Login;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.nottonline.MainActivity;
import com.example.nottonline.R;

/**
 * The {@code ForgotPasswordActivity} class represents an activity that allows users
 * to recover their forgotten passwords. It provides a simple interface with a back
 * button to return to the login screen.
 */
public class ForgotPasswordActivity extends AppCompatActivity {

    /**
     * Called when the activity is first created. Responsible for setting up the
     * layout and handling interactions, such as the back button click.
     *
     * @param savedInstanceState A {@code Bundle} containing the activity's
     *                           previously saved state, or null if there is none.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Find the ImageButton by its ID
        ImageButton imageButton = findViewById(R.id.button_back);

        // Set an OnClickListener for the ImageButton
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an Intent to navigate back to the login activity
                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
