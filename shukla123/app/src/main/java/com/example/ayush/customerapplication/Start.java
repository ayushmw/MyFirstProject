package com.example.ayush.customerapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.sql.SQLException;

/**
 * Created by Ayush on 01-06-2015.
 */
public class Start extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        final SavedCardsDB info = new SavedCardsDB(this);
        try {
            info.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Thread timer = new Thread(){
            public void run(){
                try{
                    sleep(1000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }finally {
                    if (!info.getActive().contains("true")){
                        Intent intent = new Intent(Start.this, SignInActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(Start.this, SavedCards.class);
                        startActivity(intent);
                    }

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
