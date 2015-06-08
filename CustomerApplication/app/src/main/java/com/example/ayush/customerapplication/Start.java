package com.example.ayush.customerapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by Ayush on 01-06-2015.
 */
public class Start extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        Thread timer = new Thread(){
            public void run(){
                try{
                    sleep(2000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }finally {
                    Intent intent = new Intent("com.example.ayush.customerapplication.SAVEDCARDS");
                    startActivity(intent);
                }
            }
        };
        timer.start();
    }
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
