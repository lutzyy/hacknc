package com.example.dbdemo;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;


public class CheckConditionsActivity extends AppCompatActivity {
    private String[] diseaseNames = {
            "dangerouscollum",
            "excatecollum",
            "intricantcollum",
            "nontremblcollum",
            "severuscollum",
            "evencollum",
            "flexibilecollum",
            "dangerousgula",
            "excategula",
            "intricantgula",
            "nontremblgula",
            "severusgula",
            "evengula",
            "flexibilegula",
            "dangerousguttur",
            "excateguttur",
            "intricantguttur",
            "nontremblguttur",
            "severusguttur",
            "evenguttur"
    };

    private int buttonCount = diseaseNames.length;
    private SQLiteDatabase database;
    private TextView textView1;
    private EditText findTags;

    private int questionsIndex;
    private int questionsLength;
    private TextView resultText;
    HashSet<String> symptomsSet;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private int tableLength;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        database = openOrCreateDatabase("my_database.db", MODE_PRIVATE, null);
        createTableIfNotExists();

        textView1 = findViewById(R.id.resultOne);

        symptomsSet = new HashSet<>();
        resultText = findViewById(R.id.resultText);

        listView = findViewById(R.id.listView);

        // Create an ArrayAdapter and associate it with the ListView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);

        LinearLayout layout = findViewById(R.id.buttonLayout); // Replace with your layout ID

        for (int i = 0; i < buttonCount; i++) {
            Button button = new Button(this);
            button.setText(diseaseNames[i]); // Set the text from the array
            button.setId(View.generateViewId());

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setEnabled(false); // Disable the clicked button

                    v.setBackgroundColor(getResources().getColor(android.R.color.darker_gray)); // Set a gray background
                }
            });

            layout.addView(button);
        }

    }

    private void createTableIfNotExists() {
        // add if not exists later
        database.execSQL("DROP TABLE IF EXISTS conditionsTable");
        database.execSQL("CREATE TABLE conditionsTable ("
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "CONDITIONS TEXT, "
                + "SYMPTOMS TEXT, "
                + "DONT_TAKE TEXT"
                + ");");
        // Check if the table is empty and insert sample values if needed
        Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM conditionsTable", null);
        if (cursor != null && cursor.moveToFirst()) {
            createDummyData(cursor);
        }
    }

    private void createDummyData(Cursor cursor) {
        int count = cursor.getInt(0);
        cursor.close();
        if (count == 0) {
            database.execSQL("INSERT INTO conditionsTable (CONDITIONS, SYMPTOMS, DONT_TAKE) VALUES ('tuberculosis', 'coughing,screaming', 'peanut butter');");
            database.execSQL("INSERT INTO conditionsTable (CONDITIONS, SYMPTOMS, DONT_TAKE) VALUES ('hergitis', 'eating', 'peanut butter');");
            database.execSQL("INSERT INTO conditionsTable (CONDITIONS, SYMPTOMS, DONT_TAKE) VALUES ('sinitosis', 'wheezing', 'peanut butter');");
        }
    }





    public void findConditionById(View view) {
        String idString = String.valueOf(findTags.getText());
//      later do error checking if value is a real integer, and in database
        try {
            Integer.valueOf(idString);
        } catch (Exception e) {
            return;
        }

        String query = "SELECT * FROM conditionsTable WHERE CONDITION_ID = "+idString+";";
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToLast();

        @SuppressLint("Range") int CONDITION_ID = cursor.getInt(1);
        @SuppressLint("Range") String CONDITIONS = cursor.getString(2);
        @SuppressLint("Range") String MED = cursor.getString(3);
        String entryName = "Condition: " + CONDITIONS + " with medication: " + MED + "\n ID:" +
                CONDITION_ID;
        textView1.setText(entryName);
        cursor.close();

//        this closes the keyboard and resets the field to blank
        findTags.setText("");
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(findTags.getWindowToken(), 0);

    }

    public void backHome(View view) {
        Intent homeIntent = new Intent(this, MainActivity.class);
        startActivity(homeIntent);
    }

    public void onCheckClick(View view) {
        // do nothing so far
    }

    private String[] getRowSymptomList(int index) {
        String query = "SELECT * FROM conditionsTable WHERE ID = " + index;
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToLast();
        @SuppressLint("Range") String symptoms = cursor.getString(cursor.getColumnIndex("SYMPTOMS"));
        String[] wordsArray = symptoms.split(",\\s*");
        cursor.close();
        return wordsArray;
    }

    private String getRowDisease(int index) {
        String query = "SELECT * FROM conditionsTable WHERE ID = " + index;
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToLast();
        @SuppressLint("Range") String condition = cursor.getString(cursor.getColumnIndex("CONDITIONS"));
        cursor.close();
        return condition;
    }

    private String[] getRowDontTake(int index) {
        String query = "SELECT * FROM conditionsTable WHERE ID = " + index;
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToLast();
        @SuppressLint("Range") String symptoms = cursor.getString(cursor.getColumnIndex("DONT_TAKE"));
        String[] wordsArray = symptoms.split(",\\s*");
        cursor.close();
        return wordsArray;
    }






    public void clickEyes(View view) {
        symptomsSet.add("dry eyes");
        adapter.add("dry eyes");
        view.setBackgroundColor(getResources().getColor(android.R.color.darker_gray)); // Set a gray background

    }

    public void clickCoughing(View view) {
        symptomsSet.add("coughing");
        adapter.add("coughing");
        view.setBackgroundColor(getResources().getColor(android.R.color.darker_gray)); // Set a gray background

    }

    public void clickWheezing(View view) {

        symptomsSet.add("wheezing");
        adapter.add("wheezing");
        view.setBackgroundColor(getResources().getColor(android.R.color.darker_gray)); // Set a gray background
    }

    private double getPercentageSymptoms(String[] rowSymptoms) {
        double amount = 0.0;
        for (int i = 0; i < rowSymptoms.length; i++) {
            if(symptomsSet.contains(rowSymptoms[i])) {
                amount++;
            }
        }
        return amount/rowSymptoms.length;
    }

    public void searchSymptoms(View view) {
        tableLength = 4;
        double maxPercentage = 0.0;
        int maxIndex = 0;


        for (int i = 1; i < tableLength; i++) {
            try {
                String[] rowSymptoms = getRowSymptomList(i);
                double rowPercentage = getPercentageSymptoms(rowSymptoms);
                if (rowPercentage > maxPercentage) {
                    maxPercentage = rowPercentage;
                    maxIndex = i;
                }
            } catch (Exception e) {

            }
        }




        String result;
        if (maxPercentage > 0.5) {
            String mostLikely = getRowDisease(maxIndex);
            String[] dontTake = getRowDontTake(maxIndex);
            StringBuilder symptomBuilder = new StringBuilder();
            symptomBuilder.append(dontTake[0]);
            for (int i = 1; i < dontTake.length; i++) {
                symptomBuilder.append(", "+dontTake[i]);
            }
            result = "You matched with " + (maxPercentage * 100) +"% of the known symptoms for " + mostLikely + " " +
                    "which means these medications should be avoided: " + symptomBuilder.toString();

        } else {
            result = "result is inconclusive " + maxPercentage;

        }
        resultText.setText(result);
    }
}
