package com.example.maptest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class EntryActivity extends AppCompatActivity {

    private ImageView imageView;
    private Handler entryHandler =  new Handler(){

        public void handleMessage(Message msg){
            getToGuide();
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_entry);
        imageView = findViewById(R.id.entryImageView);
        imageView.setImageResource(R.drawable.entry);
        entryHandler.sendEmptyMessageAtTime(0,  SystemClock.uptimeMillis()+2000);

    }

    private void getToGuide(){
        Intent intent = new Intent(EntryActivity.this, GuideActivity.class);
        startActivity(intent);
        this.finish();
    }
}
