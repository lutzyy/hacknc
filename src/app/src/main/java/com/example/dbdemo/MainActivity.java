package com.example.dbdemo;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;



public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void startPhotoActivity(View view) {
        Intent photoActivityIntent = new Intent(this, Photo.class);
        startActivity(photoActivityIntent);
    }


    public void startSketchActivity(View view) {
        Intent sketchIntent = new Intent(this, NewSketch.class);
        startActivity(sketchIntent);
    }
}
