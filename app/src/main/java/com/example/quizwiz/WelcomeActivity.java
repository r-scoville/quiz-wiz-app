package com.example.quizwiz;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.Intent; // Intents
import android.net.Uri; // URLs
import android.provider.ContactsContract; // Contacts manager

import com.google.android.material.textfield.TextInputEditText;

public class WelcomeActivity extends AppCompatActivity {

    // Declare controls (views)
    Button btnStart;
    TextInputEditText inputName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Instantiate controls
        btnStart = findViewById(R.id.btn_cta_start);
        inputName = findViewById(R.id.input_name);

        // Listener
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Input validation for not null input
                if (!inputName.getText().toString().equals("")) {
                    Intent i = new Intent("QuizActivity");
                    Bundle extras = new Bundle();
                    extras.putString("NAME", inputName.getText().toString());
                    i.putExtras(extras);
                    startActivity(i, extras); // Note: startActivityForResult is deprecated.
                }
                else {
                    int colorError = Color.parseColor("#FFFFB09E");
                    inputName.setBackgroundColor(colorError);
                }
            }
        }); // end listener

    } // end onCreate
} // end class