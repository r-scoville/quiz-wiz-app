package com.example.quizwiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.Intent; // Intents
import android.util.*; // Log
import java.io.*;
import java.util.*;

import android.net.Uri; // URLs
import android.provider.ContactsContract; // Contacts manager

import com.google.android.material.chip.Chip;

public class QuizActivity extends AppCompatActivity {

    // Controls/views
    Button btnNext;
    Chip option1, option2, option3, option4;
    TextView tvQuizTerm;
    TextView tvUserName;
    TextView tvUserScore;

    // Variables
    ArrayList<String> terms = new ArrayList<String>();
    ArrayList<String> definitions = new ArrayList<String>();
    Map<String,String> hashMap = new HashMap<String,String>();
    String currentTerm;
    int userScore = 0;
    final String ERRORMSG = "Error: unable to run quiz. :(";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Instantiate controls
        btnNext = findViewById(R.id.btn_cta_next);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        tvQuizTerm = findViewById(R.id.tv_quiz_term);
        tvUserName = findViewById(R.id.tv_user_name);
        tvUserScore = findViewById(R.id.tv_user_score);

        // Listeners
        option1.setOnClickListener(onOptionSelect);
        option2.setOnClickListener(onOptionSelect);
        option3.setOnClickListener(onOptionSelect);
        option4.setOnClickListener(onOptionSelect);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Only refresh quiz if there are still unanswered questions
                if (terms.size() > 0) {
                    runQuiz();
                }
                // Otherwise go to completion page
                else {
                    Intent i = new Intent("CompletionActivity");
                    Bundle extras = new Bundle();
                    extras.putString("SCORE", String.valueOf(userScore)); // Send user score to next page
                    i.putExtras(extras);
                    startActivity(i, extras); // Note: startActivityForResult is deprecated.
                }
            } // end onClick
        }); // end listener

        // Create bundle to receive incoming bundle data
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String s = extras.getString("NAME") + "'s Score:";
            tvUserName.setText(s);
        }

        parseQuiz();
        runQuiz();

    } // end onCreate

    // -----------
    // QUIZ LOGIC
    // -----------

    // Code referenced:
    // https://nscconline.desire2learn.com/d2l/le/content/211294/viewContent/2601882/View

    public void parseQuiz() {
        String str;
        BufferedReader br;

        try {
            // Get delimited file in
            InputStream is = getResources().openRawResource(R.raw.quiz);
            br = new BufferedReader(new InputStreamReader(is));
            System.out.println("Quiz file is open (RAW).");

            // Read record from file (term and matching definition)
            while ((str = br.readLine()) != null) {
                System.out.println(str); // for testing

                try {
                    // Split record at delimiter (",") and parse
                    String[] separatedStr = str.split(":");
                    String term = separatedStr[0];
                    String definition = separatedStr[1];

                    terms.add(term); // Add term to terms list
                    definitions.add(definition); // Add definition to definitions list
                    hashMap.put(term, definition); // Add both to hashmap to hold relationship

                    System.out.println(hashMap); // for testing
                }
                catch (Exception e) {
                    e.printStackTrace();
                } // end try-catch
            } // end while

            // Close file
            is.close();
            br.close();
            System.out.println("Quiz file is closed (RAW)."); // for testing
        }
        catch (IOException e) { // I/O exception handler
            e.printStackTrace();
        }
        catch (Exception e) { // Generic exception handler
            e.printStackTrace();
        } // end try-catch
    } // end parseQuiz

    public void setTvQuizTerm() {
        Collections.shuffle(terms);
        tvQuizTerm.setText(terms.get(0));
        currentTerm = terms.get(0);
        terms.remove(terms.get(0)); // Remove term once used to prevent question repetition
    }

    public void setOptions() {
        Collections.shuffle(definitions);

        List<String> tempDefList = new ArrayList<String>(4);
        tempDefList.add(hashMap.get(currentTerm)); // Ensure correct definition is included
        definitions.remove(hashMap.get(currentTerm)); // Ensure the correct definition doesn't show twice
        tempDefList.add(definitions.get(0));
        tempDefList.add(definitions.get(1));
        tempDefList.add(definitions.get(2));
        definitions.add(hashMap.get(currentTerm)); // Add correct definition back to the pool for next time

        Collections.shuffle(tempDefList); // Ensure random placement of correct definition

        // Populate options with definitions
        option1.setText(tempDefList.get(0));
        option2.setText(tempDefList.get(1));
        option3.setText(tempDefList.get(2));
        option4.setText(tempDefList.get(3));

        // Reset option background colors
        int colorDefault = Color.parseColor("#ffffff");
        option1.setChipBackgroundColor(ColorStateList.valueOf(colorDefault));
        option2.setChipBackgroundColor(ColorStateList.valueOf(colorDefault));
        option3.setChipBackgroundColor(ColorStateList.valueOf(colorDefault));
        option4.setChipBackgroundColor(ColorStateList.valueOf(colorDefault));
    }

    public boolean isCorrectDef(String selection) {
        return hashMap.get(currentTerm).equals(selection);
    }

    public void runQuiz(){
        setTvQuizTerm();
        setOptions();
        tvUserScore.setText(userScore + "/10");
    }

    // ----------------
    // OPTION SELECTION
    // ----------------

    public View.OnClickListener onOptionSelect = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Chip c = (Chip)v;

            // Display correct/incorrect answer to user
            int colorSuccess = Color.parseColor("#FF7CDBC3");
            int colorError = Color.parseColor("#FFFFB09E");

            switch (v.getId()) {
                case R.id.option1:{
                    if (isCorrectDef(option1.getText().toString())) {
                        option1.setChipBackgroundColor(ColorStateList.valueOf(colorSuccess));
                        userScore += 1;
                    }
                    else {
                        option1.setChipBackgroundColor(ColorStateList.valueOf(colorError));
                    }
                }
                    break;
                case R.id.option2:{
                    if (isCorrectDef(option2.getText().toString())) {
                        option2.setChipBackgroundColor(ColorStateList.valueOf(colorSuccess));
                        userScore += 1;
                    }
                    else {
                        option2.setChipBackgroundColor(ColorStateList.valueOf(colorError));
                    }
                    break;
                }
                case R.id.option3:{
                    if (isCorrectDef(option3.getText().toString())) {
                        option3.setChipBackgroundColor(ColorStateList.valueOf(colorSuccess));
                        userScore += 1;
                    }
                    else {
                        option3.setChipBackgroundColor(ColorStateList.valueOf(colorError));
                    }
                    break;
                }
                case R.id.option4:{
                    if (isCorrectDef(option4.getText().toString())) {
                        option4.setChipBackgroundColor(ColorStateList.valueOf(colorSuccess));
                        userScore += 1;
                    }
                    else {
                        option4.setChipBackgroundColor(ColorStateList.valueOf(colorError));
                    }
                    break;
                }
                default:{
                    break;
                }
            } // end switch

        } // end on click
    }; // end listener

} // end class