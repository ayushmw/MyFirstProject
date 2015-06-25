package com.example.ayush.customerapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnKeyListener;
import android.view.KeyEvent;

import java.sql.SQLException;
import java.util.Calendar;

// check validity when we click on save button

public class CardDetails extends AppCompatActivity implements View.OnClickListener {
    EditText etCardNo, etName, etCVV, etCardLabel, etExpiry;
    int monthReturned = 0, yearReturned = 0, keyDel;
    public int count = 0;
    Toolbar toolbar;
    Button bSaveCard;
    String a;

    Integer length;
    String TAG = "WTF";
    Long x;
    int flag5 = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_details);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Using arrayAdapter to fill data in months

        //Adding items to dropdown menu of year
        etCardNo = ((com.iangclifton.android.floatlabel.FloatLabel) findViewById(R.id.cardInput)).getEditText();
        etName = ((com.iangclifton.android.floatlabel.FloatLabel) findViewById(R.id.nameInput)).getEditText();
        etCVV = ((com.iangclifton.android.floatlabel.FloatLabel) findViewById(R.id.cvvInput)).getEditText();
        etExpiry = ((com.iangclifton.android.floatlabel.FloatLabel) findViewById(R.id.expiryInput)).getEditText();
        etCardLabel = ((com.iangclifton.android.floatlabel.FloatLabel) findViewById(R.id.cardLabelText)).getEditText();
        bSaveCard = (Button) findViewById(R.id.bSaveCard);
        bSaveCard.setOnClickListener(this);

        etCardNo.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                //since nothing is in here, nothing will happen.
                return true;
            }
        });

        etCVV.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                //since nothing is in here, nothing will happen.
                return true;
            }
        });

        etCardNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // code to execute when EditText loses focus
                    //always use .equals("") rather than null as there is difference between them
                    //"" is an actual string, albeit an empty one.(we can use .length , .substring)
                    // null, however, means that the String variable points to nothing.(we can't use .length , .substring)
                    count = count + 1;
                    if (etCardNo.getText().toString().equals("")) {
                        etCardNo.setNextFocusDownId(etName.getId());
                    } else {
                        if (!checkCard()) {
                            etCardNo.setError("Invalid Card Number");
                        }
                    }
                }
            }
               /* if (hasFocus && count != 0) {
                    checkCard();
                    Log.d("TAG2", "has focus");

                }*/
        });

        //we are using text watcher to keep eye on the input text
        etCardNo.addTextChangedListener(new TextWatcher() {

            /*beforeTextChanged(CharSequence s, int start, int count, int after).
            This means that the characters are about to be replaced with some new text. The text is uneditable.
            In s,some number of characters say x are getting replaced from a position y to position z.
            Use: when you need to take a look at the old text which is about to change.
            */
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            /*onTextChanged(CharSequence s, int start, int before, int count).
            Changes have been made, some characters have just been replaced. The text is uneditable.
            Use: when you need to see which characters in the text are new.
            */
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etCardNo.getText().toString().replace("-", "").length() == 16) {
                    if (!checkCard()) {
                        etCardNo.setError("Invalid Card Number");
                    }
                } else {
                    boolean flag = true;
                    String eachBlock[] = etCardNo.getText().toString().split("-");
                    for (String anEachBlock : eachBlock) {
                        if (anEachBlock.length() > 4) {
                            flag = false;
                        }
                    }

                    if (flag) {

                        etCardNo.setOnKeyListener(new OnKeyListener() {

                            @Override
                            public boolean onKey(View v, int keyCode, KeyEvent event) {

                                if (keyCode == KeyEvent.KEYCODE_DEL)
                                    keyDel = 1;
                                return false;
                            }
                        });

                        if (keyDel == 0) {

                            if (((etCardNo.getText().length() + 1) % 5) == 0) {

                                if (etCardNo.getText().toString().split("-").length <= 3) {
                                    etCardNo.setText(etCardNo.getText() + "-");
                                    etCardNo.setSelection(etCardNo.getText().length());
                                }
                            }
                            a = etCardNo.getText().toString();
                        } else {
                            a = etCardNo.getText().toString();
                            etCardNo.setSelection(etCardNo.getText().length());
                            keyDel = 0;
                        }


                    } else {
                        etCardNo.setText(a);
                    }
                }
            }

            /*afterTextChanged(Editable s).
             The same as onTextChanged, except now the text is editable.
              Use: when a you need to see and possibly edit new text.
             */
            @Override
            public void afterTextChanged(Editable s) {
                if (count != 0) {
                    if (etCardNo.getText().toString().replace("-", "").length() != 16) {
                        etCardNo.setError("Invalid Card Number");
                    } else if (!checkCard()) {
                        etCardNo.setError("Invalid Card Number");
                    }
                }
            }

        });

        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (!etName.getText().toString().equals("")) {
                    etName.setError(null);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etCVV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (flag5 == 1) {
                    if (etCVV.getText().toString().length() != 3) {
                        etCVV.setError("Invalid CVV");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etExpiry.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    expiry();
            }
        });
        etExpiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expiry();
            }
        });
    }

    public boolean checkCard() {
        String text_string = etCardNo.getText().toString();
        String new_text_string = text_string.replace("-", "");
        length = new_text_string.length();

        try {
            x = Long.parseLong(new_text_string);
            Log.v(TAG, text_string);
        } catch (NumberFormatException nFE) {
            Log.v(TAG, Long.toString(x));


        }
        Long number = x;

        long[] array = new long[16];
        for (int i = 0; i < 16; i++) {
            array[i] = number % 10;
            number = number / 10;
        }
        return check(array);
    }

    public static boolean check(long[] digits) {
        int sum = 0;

        for (int i = 0; i < 16; i++) {

            long digit = digits[i];
            // every 2nd number multiply with 2
            if (i % 2 == 1) {
                digit *= 2;
            }
            sum += digit > 9 ? digit - 9 : digit;
        }
        return sum % 10 == 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_card_deatils, menu);
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
            Intent intent2 = new Intent(CardDetails.this, ViewDB.class);
            startActivity(intent2);
            return true;
        }

        if (id == R.id.mAboutCardDetails) {
            Intent intent2 = new Intent(CardDetails.this, About.class);
            startActivity(intent2);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void onClick(View v) {
        int flag = 0, flag1 = 0, flag2 = 0, flag3 = 0, flag4 = 0, flag6 = 0;
        if (v.getId() == R.id.bSaveCard) {
            if (etCardNo.getText().toString().equals("")) {
                etCardNo.setError("Invalid Card Number");
                flag = 1;
                flag1 = 1;
            }
            if (flag == 0) {
                if (!checkCard()) {
                    etCardNo.setError("Invalid Card Number");
                    flag2 = 1;
                }
            }
            if (etName.getText().toString().equals("")) {
                etName.setError("Invalid Details");
                flag3 = 1;
            }
            if (etCVV.getText().toString().length() != 3 && !(etCardNo.getText().toString().startsWith("50") || etCardNo.getText().toString().startsWith("56") || etCardNo.getText().toString().startsWith("57") || etCardNo.getText().toString().startsWith("58") || etCardNo.getText().toString().startsWith("59") || etCardNo.getText().toString().startsWith("6"))) {
                etCVV.setError("CVV must be of 3 digits");
                flag4 = 1;
                flag5 = 1;
            }
            SavedCardsDB info = new SavedCardsDB(this);
            try {
                info.open();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (info.getCardNumbers().contains(etCardNo.getText().toString())) {
                etCardNo.setError("Same card already exists");
                flag6 = 1;
            }
            if (flag1 == 0 && flag2 == 0 && flag3 == 0 && flag4 == 0 && flag6 == 0) {
                boolean didItWork = true;
                try {
                    String cardNo = etCardNo.getText().toString();
                    String name = etName.getText().toString();
                    String cvv = etCVV.getText().toString();
                    String cardLabel = etCardLabel.getText().toString();
                    SavedCardsDB entry = new SavedCardsDB(CardDetails.this);
                    entry.open();
                    entry.createEntry(cardNo, name, cvv, cardLabel, Integer.toString(monthReturned), Integer.toString(yearReturned), "false", "#FFFFFF", entry.getActiveEmail());
                    entry.close();
                    Log.d("final", etName.getText().toString());
                } catch (Exception e) {
                    didItWork = false;
                    Dialog dialog = new Dialog(this);
                    dialog.setTitle("Shoot! Something went wrong");
                    TextView tv = new TextView(this);
                    tv.setText(e.toString());
                    dialog.setContentView(tv);
                    dialog.show();
                } finally {
                    if (didItWork) {
                        Toast.makeText(CardDetails.this, "Card saved", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CardDetails.this, SavedCards.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
            }
            if (flag1 == 1 || flag2 == 1 || flag6 == 1) {
                etCardNo.setFocusableInTouchMode(true);
                etCardNo.requestFocus();
            } else if (flag3 == 1) {
                etName.setFocusableInTouchMode(true);
                etName.requestFocus();
            } else if (flag4 == 1) {
                etCVV.setFocusableInTouchMode(true);
                etCVV.requestFocus();
            }
        }
    }

    public void expiry() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthReturned = monthOfYear;
                yearReturned = year;
                etExpiry.setText("" + (monthReturned + 1) + "/" + yearReturned);
            }
        }, year, month, day);
        DatePicker datePicker = datePickerDialog.getDatePicker();
        datePicker.setMinDate(c.getTimeInMillis());
        datePickerDialog.getDatePicker().findViewById(Resources.getSystem().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
        datePickerDialog.show();
    }
}
