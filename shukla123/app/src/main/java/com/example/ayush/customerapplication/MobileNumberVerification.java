package com.example.ayush.customerapplication;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.Random;


public class MobileNumberVerification extends AppCompatActivity {
    Toolbar toolbar;
    EditText etMobileNumber;
    Button bSendOTP;
    int otp;
    Intent intent;
    String personName, email;
    TextView tvGPlusName, tvGPlusEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mobile_number_verification);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        intent = getIntent();
        personName = intent.getStringExtra("personName");
        email = intent.getStringExtra("email");
        tvGPlusName = (TextView) findViewById(R.id.tvGPlusName);
        tvGPlusEmail = (TextView) findViewById(R.id.tvGPlusEmail);
        tvGPlusName.setText(personName);
        tvGPlusEmail.setText(email);
        bSendOTP = (Button) findViewById(R.id.bSendOTP);
        etMobileNumber = (EditText) findViewById(R.id.etMobileNumber);
        bSendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etMobileNumber.getText().toString().length() == 10) {
                    sendOTP();
                    new MaterialDialog.Builder(MobileNumberVerification.this)
                            .title("Verify Mobile")
                            .content("We have sent a One Time Password (OTP) to your mobile number for verification, enter it below")
                            .positiveText("Verify")
                            .negativeText("Resend OTP")
                            .inputType(InputType.TYPE_CLASS_NUMBER)
                            .inputMaxLength(4)
                            .input("OTP", null, new MaterialDialog.InputCallback() {
                                @Override
                                public void onInput(MaterialDialog materialDialog, CharSequence charSequence) {
                                    boolean result = verifyOTP(charSequence);
                                    if (result)
                                        materialDialog.dismiss();
                                }
                            })
                            .autoDismiss(false)
                            .callback(new MaterialDialog.ButtonCallback() {
                                          @Override
                                          public void onPositive(MaterialDialog dialog) {
                                          }

                                          @Override
                                          public void onNegative(MaterialDialog dialog) {
                                              sendOTP();
                                          }
                                      }
                            )
                            .show();
                } else etMobileNumber.setError("Enter 10 digit mobile number");
            }
        });
    }

    private boolean verifyOTP(CharSequence charSequence) {
        boolean result;
        if (charSequence.toString().length() == 0) {
            Toast.makeText(this, "Incorrect OTP", Toast.LENGTH_SHORT).show();
            result = false;
        } else if (Integer.parseInt(charSequence.toString()) == otp) {
            result = true;
            boolean didItWork = true;
            try {
                String mobNo = etMobileNumber.getText().toString();
                SavedCardsDB entry = new SavedCardsDB(this);
                entry.open();
                entry.createEntryGP(email, personName, 1, mobNo, "true");
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
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        } else {
            Toast.makeText(this, "Incorrect OTP", Toast.LENGTH_SHORT).show();
            result = false;
        }
        return result;
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
