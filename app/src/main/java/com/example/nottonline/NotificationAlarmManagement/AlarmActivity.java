package com.example.nottonline.NotificationAlarmManagement;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nottonline.R;

/**
 * The {@code AlarmActivity} class represents an activity that handles alarms,
 * including playing a ringtone and vibrating the device. It provides methods to
 * start and stop the alarm, and includes a button to manually turn off the alarm.
 */
public class AlarmActivity extends AppCompatActivity {

    /**
     * The ringtone object that plays the alarm sound.
     */
    private Ringtone ringtone;

    /**
     * The vibrator object that controls device vibration.
     */
    private Vibrator vibrator;

    /**
     * Called when the activity is first created. Responsible for initializing
     * the ringtone, vibrator, and starting the alarm.
     *
     * @param savedInstanceState A {@code Bundle} containing the activity's
     *                           previously saved state, or null if there is none.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        // Initialize Ringtone
        Uri ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtone = RingtoneManager.getRingtone(this, ringtoneUri);

        // Initialize Vibrator
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Start the ringtone
        ringtone.play();
        Toast.makeText(this, "Phone is ringing!", Toast.LENGTH_SHORT).show();

        // Start the vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(new long[]{0, 1000, 500}, 0));
        }
        Toast.makeText(this, "Phone is vibrating!", Toast.LENGTH_SHORT).show();

        // Handle the UI components in your alarm layout
        Button offButton = findViewById(R.id.offButton);
        offButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Stop the alarm when the "off" button is clicked
                stopAlarm();

                // You can also finish the AlarmActivity to close the UI
                finish();
            }
        });
    }

    /**
     * Stops the alarm by stopping the ringtone and vibrator. Also displays
     * Toast messages indicating that the ringtone and vibration have been stopped.
     */
    private void stopAlarm() {
        // Stop the ringtone
        if (ringtone.isPlaying()) {
            ringtone.stop();
            Toast.makeText(this, "Ringtone stopped!", Toast.LENGTH_SHORT).show();
        }

        // Stop the vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.cancel();
            Toast.makeText(this, "Vibration stopped!", Toast.LENGTH_SHORT).show();
        }
    }
}
