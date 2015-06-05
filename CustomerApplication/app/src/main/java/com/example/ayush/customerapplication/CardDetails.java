package com.example.ayush.customerapplication;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnKeyListener;
import android.view.KeyEvent;

import java.sql.SQLException;
import java.util.Calendar;


public class CardDetails extends ActionBarActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private String[] month = {"Month", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    Spinner spinner1, spinner2;
    EditText etCardNo, etName, etCVV, etCardLabel;
    private String year[] = new String[52];
    int initialYear, monthReturned = 0, yearReturned = 0, keyDel;
    Toolbar toolbar;
    Button bSaveCard;
    String a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_details);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Using arrayAdapter to fill data in months
        ArrayAdapter<String> adapter_state1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, month);

        //to increase size of items in dropdown list
        adapter_state1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1 = (Spinner) findViewById(R.id.month);
        spinner1.setAdapter(adapter_state1);


        //Adding items to dropdown menu of year
        Calendar c = Calendar.getInstance();
        int first_year = c.get(Calendar.YEAR);
        initialYear = first_year;

        year[0] = "Year";
        for (int i = 1; i < 52; i++) {
            year[i] = Integer.toString(first_year);
            first_year += 1;
        }


        ArrayAdapter<String> adapter_state2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, year);
        adapter_state2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2 = (Spinner) findViewById(R.id.year);
        spinner2.setAdapter(adapter_state2);
        etCardNo = (EditText) findViewById(R.id.cardInput);
        etName = (EditText) findViewById(R.id.nameInput);
        etCVV = (EditText) findViewById(R.id.cvvInput);
        etCardLabel = (EditText) findViewById(R.id.cardLabelText);
        bSaveCard = (Button) findViewById(R.id.bSaveCard);
        bSaveCard.setOnClickListener(this);
        spinner1.setOnItemSelectedListener(this);
        spinner2.setOnItemSelectedListener(this);
        //To put - after 4 digits
        final EditText text = (EditText) findViewById(R.id.cardInput);


        //we are using text watcher to keep eye on the input text
        text.addTextChangedListener(new TextWatcher() {


            /*beforeTextChanged(CharSequence s, int start, int count, int after).
         This means that the characters are about to be replaced with some new text. The text is uneditable.
          In s,some number of characters say x are getting replaced from a position y to position z.
                Use: when you need to take a look at the old text which is about to change.
          */
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }




            /*onTextChanged(CharSequence s, int start, int before, int count).
            Changes have been made, some characters have just been replaced. The text is uneditable.
            Use: when you need to see which characters in the text are new.
            */

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                boolean flag = true;
                String eachBlock[] = text.getText().toString().split("-");
                //eachBlock is an array not a String
                //.split is used to break string. Ex: a= "How are you" and we did a.split(" ") then result will be How,are,you.


                //this for loop will be executed everytime we entered a digit
                for (int i = 0; i < eachBlock.length; i++) {
                    if (eachBlock[i].length() > 4) {
                        flag = false;
                    }
                }
                if (flag) {

                    text.setOnKeyListener(new OnKeyListener() {

                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {

                            if (keyCode == KeyEvent.KEYCODE_DEL)
                                keyDel = 1;
                            return false;
                        }
                    });

                    if (keyDel == 0) {

                        //this if is used if no. of digits entered is multiple of 4
                        if (((text.getText().length() + 1) % 5) == 0) {

                            //this if is used as there should not be any - after 16 digits
                            if (text.getText().toString().split("-").length <= 3) {
                                text.setText(text.getText() + "-");
                                text.setSelection(text.getText().length());
                            }
                        }
                        a = text.getText().toString();
                    } else {
                        a = text.getText().toString();
                        keyDel = 0;
                    }

                } else {
                    text.setText(a);
                }

            }

            /*afterTextChanged(Editable s).
             The same as onTextChanged, except now the text is editable.
              Use: when a you need to see and possibly edit new text.
             */
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
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
            Intent intent2 = new Intent("com.example.ayush.customerapplication.VIEWDB");
            startActivity(intent2);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int position1 = spinner1.getSelectedItemPosition();
        int position2 = spinner2.getSelectedItemPosition();
        for (monthReturned = 1; monthReturned <= 12; monthReturned++) {
            if (monthReturned == position1)
                break;
        }
        for (yearReturned = initialYear; yearReturned <= initialYear + 50; yearReturned++) {
            if (yearReturned == position2 + initialYear - 1)
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bSaveCard) {
            boolean didItWork = true;
            try {
                String cardNo = etCardNo.getText().toString();
                String name = etName.getText().toString();
                String cvv = etCVV.getText().toString();
                String cardLabel = etCardLabel.getText().toString();
                SavedCardsDB entry = new SavedCardsDB(CardDetails.this);
                entry.open();
                entry.createEntry(cardNo, name, cvv, cardLabel, Integer.toString(monthReturned), Integer.toString(yearReturned));
                entry.close();
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
                }
            }
            Intent intent = new Intent("com.example.ayush.customerapplication.SAVEDCARDS");
            startActivity(intent);
        }
    }
}
