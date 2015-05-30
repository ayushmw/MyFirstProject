package eko.addcarddetails;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

// Edited by shukla
public class

        MainActivity extends ActionBarActivity {

    private String[] month= {"Month","Jan","Feb","Mar","Apr","May","Jun", "Jul", "Aug","Sep", "Oct","Nov","Dec"};

    private String year[] =  new String[100];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Adding items to dropdown menu of month
        ArrayAdapter<String> adapter_state1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, month);
        Spinner spinner1 = (Spinner)findViewById(R.id.month);
        spinner1.setAdapter(adapter_state1);


        //Adding items to dropdown menu of year
        for(int i=1950; i<2050; i++ ){
                year[i-1950]= Integer.toString(i);
        }


        ArrayAdapter<String> adapter_state2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, year);
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
