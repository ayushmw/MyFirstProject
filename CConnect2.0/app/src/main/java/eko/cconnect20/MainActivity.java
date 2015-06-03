package eko.cconnect20;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import java.util.Calendar;


public class MainActivity extends ActionBarActivity {



    private String[] month= {"MONTH","JAN","FEB","MAR","APR","MAY","JUN", "JUL", "AUG","SEP", "OCT","NOV","DEC"};

    private String year[] =  new String[52];

    private Toolbar toolbar;

    String a;
    int keyDel;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Using arrayAdapter to fill data in month spinner
        ArrayAdapter<String> adapter_state1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, month);


        //to increase size of items in dropdown list in month spinner
        adapter_state1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner1 = (Spinner) findViewById(R.id.month);
        spinner1.setAdapter(adapter_state1);


        //Adding items to dropdown menu of year
        // Using system time to get the current year (to fill in the first year in the 'year' spinner
        Calendar c = Calendar.getInstance();
        int first_year = c.get(Calendar.YEAR);

      // Storing total years in an array
        year[0]= "YEAR";
        for(int i=1; i<52; i++ ){
            year[i]= Integer.toString(first_year);
            first_year=first_year+1;
        }

        //Using arrayAdapter to fill data in year spinner
        ArrayAdapter<String> adapter_state2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, year);

        //to increase size of items in dropdown list in year spinner
        adapter_state2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner2 = (Spinner) findViewById(R.id.year);
        spinner2.setAdapter(adapter_state2);



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
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
