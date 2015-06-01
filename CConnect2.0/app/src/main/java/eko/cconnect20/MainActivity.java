package eko.cconnect20;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


import java.util.Calendar;


public class MainActivity extends ActionBarActivity {


    private String[] month= {"Month","Jan","Feb","Mar","Apr","May","Jun", "Jul", "Aug","Sep", "Oct","Nov","Dec"};

    private String year[] =  new String[100];

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//Using arrayAdapter to fill data in months 
ArrayAdapter<String> adapter_state1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, month);

        //to increase size of items in dropdown list
        adapter_state1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner1 = (Spinner)findViewById(R.id.month);
        spinner1.setAdapter(adapter_state1);


        //Adding items to dropdown menu of year
        Calendar c = Calendar.getInstance();
        int first_year = c.get(Calendar.YEAR);

        year[0]= "Year";
        for(int i=1; i<50; i++ ){
            year[i]= Integer.toString(first_year);
            first_year=first_year+1;
        }


        ArrayAdapter<String> adapter_state2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, year);
        adapter_state2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner2 = (Spinner)findViewById(R.id.year);
        spinner2.setAdapter(adapter_state2);

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
