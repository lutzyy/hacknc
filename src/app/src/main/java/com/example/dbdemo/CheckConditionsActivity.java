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
    private EditText findTags;


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
        findTags = findViewById(R.id.tagSearchInput);


        fetchTopThreeEntriesFromconditionsTable();
    }

    private void fetchTopThreeEntriesFromconditionsTable() {
        String query = "SELECT * FROM conditionsTable;";
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToLast();


//      this is reused so make into helper method later
        @SuppressLint("Range") int CONDITION_ID = cursor.getInt(1);
        @SuppressLint("Range") String CONDITION = cursor.getString(2);
        @SuppressLint("Range") String MED = cursor.getString(3);
        String entryName = "Condition: " + CONDITION + " with medication: " + MED + "\n ID:" +
                CONDITION_ID;
        textView1.setText(entryName);
        cursor.close();
    }



    public void findConditionById(View view) {
        String idString = String.valueOf(findTags.getText());
//      later do error checking if value is a real integer, and in database

        String query = "SELECT * FROM conditionsTable WHERE CONDITION_ID = "+idString+";";
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToLast();

        @SuppressLint("Range") int CONDITION_ID = cursor.getInt(1);
        @SuppressLint("Range") String CONDITION = cursor.getString(2);
        @SuppressLint("Range") String MED = cursor.getString(3);
        String entryName = "Condition: " + CONDITION + " with medication: " + MED + "\n ID:" +
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
}
