package com.example.ayush.customerapplication;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.Random;


public class MobileNumberVerification extends AppCompatActivity {
    Toolbar toolbar;
    EditText etMobileNumber, etOTP;
    Button bSendOTP, bVerify;
    int otp;
    LinearLayout linearLayoutBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobile_number_verification);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        linearLayoutBottom = (LinearLayout) findViewById(R.id.linearLayoutBottom);
        bSendOTP = (Button) findViewById(R.id.bSendOTP);
        bVerify = (Button) findViewById(R.id.bVerify);
        etMobileNumber = (EditText) findViewById(R.id.etMobileNumber);
        etOTP = (EditText) findViewById(R.id.etOTP);
        bSendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etMobileNumber.getText().toString().length() == 10){
                    sendOTP();
                    linearLayoutBottom.setVisibility(View.VISIBLE);
                }
                else etMobileNumber.setError("Enter 10 digit mobile number");
            }
        });
        bVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyOTP();
            }
        });
    }

    private void verifyOTP() {
        if (Integer.parseInt(etOTP.getText().toString()) == otp) {
            boolean didItWork = true;
            try {
                long mobNo = Long.parseLong(etMobileNumber.getText().toString());
                SavedCardsDB entry = new SavedCardsDB(this);
                entry.open();
                entry.createEntryMN(mobNo);
                entry.close();
            } catch (Exception e) {
                didItWork = false;
                Dialog dialog = new Dialog(this);
                dialog.setTitle("Shoot! Something is wrong");
                TextView tv = new TextView(this);
                tv.setText(e.toString());
                dialog.setContentView(tv);
                dialog.show();
            } finally {
                if (didItWork) {
                    Toast.makeText(this, "Registered", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MobileNumberVerification.this, SavedCards.class);
                    startActivity(intent);
                    finish();
                }
            }
        } else {
            Toast.makeText(this, "Incorrect OTP", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendOTP() {
        Random random = new Random();
        otp = random.nextInt((9999 - 1000) + 1) + 1000;
        Toast.makeText(this, "Your OTP is " + otp, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mobile_number_verification, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
