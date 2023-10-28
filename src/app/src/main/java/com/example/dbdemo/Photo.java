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





public class Photo extends AppCompatActivity {
    private SQLiteDatabase database;
    private TextView textView1, textView2, textView3;
    private EditText makeTags, findTags;
    private ImageView bigImg, imageOne, imageTwo, imageThree;
    private Bitmap bigImgBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        database = openOrCreateDatabase("my_database.db", MODE_PRIVATE, null);

        // Create the first table if it doesn't exist
        database.execSQL("CREATE TABLE IF NOT EXISTS table1 ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "date TEXT, "
                + "time TEXT, "
                + "image BLOB, "
                + "tags TEXT" // store comma-separated tags
                + ");");
        // Check if the table is empty and insert sample values if needed
        Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM table1", null);
        if (cursor != null && cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            cursor.close();

            if (count == 0) {
                // In case no images in db or not enough matching source, make first three rows tagged 'unavailable' an
                // w/o an image
                database.execSQL("INSERT INTO table1 (date, time, image, tags) VALUES ('', '', NULL, 'unavailable');");
                database.execSQL("INSERT INTO table1 (date, time, image, tags) VALUES ('', '', NULL, 'unavailable');");
                database.execSQL("INSERT INTO table1 (date, time, image, tags) VALUES ('', '', NULL, 'unavailable');");
            }
        }

        textView1 = findViewById(R.id.resultOne);
        textView2 = findViewById(R.id.resultTwo);
        textView3 = findViewById(R.id.resultThree);
        makeTags = findViewById(R.id.tagsInput);
        findTags = findViewById(R.id.tagSearchInput);
        bigImg = findViewById(R.id.bigImg);
        imageOne = findViewById(R.id.imageOne);
        imageTwo = findViewById(R.id.imageTwo);
        imageThree = findViewById(R.id.imageThree);
        bigImgBitmap = null;

        fetchTopThreeEntriesFromTable1();
    }

    private void fetchTopThreeEntriesFromTable1() {
        String query = "SELECT * FROM table1;";
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToLast();

        int index = 0;
        while (index < 3) {
            @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("date"));
            @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex("time"));
            @SuppressLint("Range") String tags = cursor.getString(cursor.getColumnIndex("tags"));
            @SuppressLint("Range") byte[] ba = cursor.getBlob(cursor.getColumnIndex("image"));
            String entryName = tags + "\n" + date + " - " + time;
            Bitmap b = null;
            if (ba != null) {
                b = BitmapFactory.decodeByteArray(ba, 0, ba.length);
            }
            if (index == 0) {
                textView1.setText(entryName);
                if (b != null) {
                    imageOne.setImageBitmap(b);
                }
            } else if (index == 1) {
                textView2.setText(entryName);
                if (b != null) {
                    imageTwo.setImageBitmap(b);
                }
            } else if (index == 2) {
                textView3.setText(entryName);
                if (b != null) {
                    imageThree.setImageBitmap(b);
                }            }
            index++;
            cursor.moveToPrevious();
        }

        cursor.close();
    }

    private void fetchTopThreeEntriesFromTable1ByTag(String searchTag) {

        String query = "SELECT * FROM table1;";
        Cursor cursor = database.rawQuery(query, null);
        cursor.moveToLast();

        int index = 0;
        while (index < 3) {
            @SuppressLint("Range") String tags = cursor.getString(cursor.getColumnIndex("tags"));
            String[] wordsArray = tags.split(",\\s*");
            HashSet<String> tagSet = new HashSet<>(Arrays.asList(wordsArray));
            if(tagSet.contains(searchTag) || tagSet.contains("unavailable")) {
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("date"));
                @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex("time"));
                @SuppressLint("Range") byte[] ba = cursor.getBlob(cursor.getColumnIndex("image"));
                String entryName = tags + "\n" + date + " - " + time;
                Bitmap b = null;
                if (ba != null) {
                    b = BitmapFactory.decodeByteArray(ba, 0, ba.length);
                }
                if (index == 0) {
                    textView1.setText(entryName);
                    imageOne.setImageBitmap(b);

                } else if (index == 1) {
                    textView2.setText(entryName);
                    imageTwo.setImageBitmap(b);

                } else if (index == 2) {
                    textView3.setText(entryName);
                    imageThree.setImageBitmap(b);
                }
                index++;
            }
            cursor.moveToPrevious();
        }
        cursor.close();
    }



    public void startCamera(View view) {
        Intent cam_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cam_intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            bigImgBitmap = (Bitmap) extras.get("data");
            bigImg.setImageBitmap(bigImgBitmap);
        }
    }

    public void saveImage(View view) {
        if (bigImgBitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bigImgBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] ba = stream.toByteArray();

            String tags = String.valueOf(makeTags.getText());
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(makeTags.getWindowToken(), 0);
            makeTags.setText("");

            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy", Locale.US);
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mma", Locale.US);

            String dateStr = dateFormat.format(now);
            String timeStr = timeFormat.format(now);
            timeStr = timeStr.toLowerCase(Locale.US);

            ContentValues cv = new ContentValues();
            cv.put("date", dateStr);
            cv.put("time", timeStr);
            cv.put("image", ba);
            cv.put("tags", tags);
            database.insert("table1", null, cv);
            fetchTopThreeEntriesFromTable1();
        }

    }

    public void findTags(View view) {
        String searchText = String.valueOf(findTags.getText());
        if (searchText.equals("") || searchText.equals(" ")) {
            fetchTopThreeEntriesFromTable1();
        } else {
            fetchTopThreeEntriesFromTable1ByTag(searchText);
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
