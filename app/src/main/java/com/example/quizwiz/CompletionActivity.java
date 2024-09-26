package com.example.quizwiz;

import androidx.appcompat.app.AppCompatActivity;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.Intent; // Intents
import android.net.Uri; // URLs
import android.provider.ContactsContract; // Contacts manager

public class CompletionActivity extends AppCompatActivity {

    // Declare controls (views)
    TextView tvFinalScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completion);

        // Instantiate controls
        tvFinalScore = findViewById(R.id.tv_final_score);

        // Create bundle to receive incoming bundle data
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String s = "Final score: " + extras.getString("SCORE") + "/10";
            tvFinalScore.setText(s);
        }

    } // end onCreate
} // end class