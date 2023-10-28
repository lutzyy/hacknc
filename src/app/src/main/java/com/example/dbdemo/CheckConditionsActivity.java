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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;


public class CheckConditionsActivity extends AppCompatActivity {
    private SQLiteDatabase database;
    private TextView textView1, textView2, textView3;
    private EditText makeTags, findTags;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        database = openOrCreateDatabase("my_database.db", MODE_PRIVATE, null);

        // Create the first table if it doesn't exist
        database.execSQL("CREATE TABLE IF NOT EXISTS conditionsTable ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "CONDITION_ID INTEGER, "
                + "CONDITION TEXT, "
                + "MED TEXT"
                + ");");






        // Check if the table is empty and insert sample values if needed
        Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM conditionsTable", null);
        if (cursor != null && cursor.moveToFirst()) {

            int count = cursor.getInt(0);
            cursor.close();
            if (count == 0) {
                database.execSQL("INSERT INTO conditionsTable (CONDITION_ID, CONDITION, MED) VALUES (88, 'spontaneous combustion', 'unavailable');");
                database.execSQL("INSERT INTO conditionsTable (CONDITION_ID, CONDITION, MED) VALUES (124, 'ligma', 'skooby snacka');");
                database.execSQL("INSERT INTO conditionsTable (CONDITION_ID, CONDITION, MED) VALUES (125, 'ligma', 'skooby snacka');");

            }
        }

        textView1 = findViewById(R.id.resultOne);
        textView2 = findViewById(R.id.resultTwo);
        textView3 = findViewById(R.id.resultThree);
        makeTags = findViewById(R.id.tagsInput);
        findTags = findViewById(R.id.tagSearchInput);


        fetchTopThreeEntriesFromconditionsTable();
    }

    private void fetchTopThreeEntriesFromconditionsTable() {
        String query = "SELECT * FROM conditionsTable;";
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToLast();


//
            @SuppressLint("Range") int CONDITION_ID = cursor.getInt(1);
            @SuppressLint("Range") String CONDITION = cursor.getString(2);
            @SuppressLint("Range") String MED = cursor.getString(3);
            String entryName = "Condition: " + CONDITION + " with medication: " + MED;
            textView1.setText(entryName);



        cursor.close();
    }

    private void fetchTopThreeEntriesFromconditionsTableByTag(String searchTag) {

        String query = "SELECT * FROM conditionsTable;";
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToLast();

        int index = 0;
        while (index < 3) {
            @SuppressLint("Range") String tags = cursor.getString(cursor.getColumnIndex("tags"));
            // this splits the
            String[] wordsArray = tags.split(",\\s*");
            HashSet<String> tagSet = new HashSet<>(Arrays.asList(wordsArray));
            if (tagSet.contains(searchTag) || tagSet.contains("unavailable")) {
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("date"));
                @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex("time"));
                String entryName = tags + "\n" + date + " - " + time;

                if (index == 0) {
                    textView1.setText(entryName);

                } else if (index == 1) {
                    textView2.setText(entryName);

                } else if (index == 2) {
                    textView3.setText(entryName);
                }
                index++;
            }
            cursor.moveToPrevious();
        }
        cursor.close();
    }


    public void saveCondition(View view) {



//            String tags = String.valueOf(makeTags.getText());
//            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(makeTags.getWindowToken(), 0);
//            makeTags.setText("");
//
//
//
//
//
//            ContentValues cv = new ContentValues();
//            cv.put("CONDITION", tags);
//            cv.put("CONDITION_ID", timeStr);
//
//            cv.put("tags", tags);
//            database.insert("conditionsTable", null, cv);
//            fetchTopThreeEntriesFromconditionsTable();


    }

    public void findTags(View view) {
        String searchText = String.valueOf(findTags.getText());
        if (searchText.equals("") || searchText.equals(" ")) {
            fetchTopThreeEntriesFromconditionsTable();
        } else {
            fetchTopThreeEntriesFromconditionsTableByTag(searchText);
            findTags.setText("");
        }
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(findTags.getWindowToken(), 0);

    }

    public void backHome(View view) {
        Intent homeIntent = new Intent(this, MainActivity.class);
        startActivity(homeIntent);
    }
}
