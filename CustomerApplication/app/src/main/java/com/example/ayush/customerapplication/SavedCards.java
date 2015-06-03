package com.example.ayush.customerapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Ayush on 28-05-2015.
 */
public class SavedCards extends ActionBarActivity {
    private RecyclerView recyclerView;
    private VizAdapter adapter;
    private List<Integer> bgImage;
    List<String> cardNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_cards);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        adapter = new VizAdapter(this, getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.plus);
        FloatingActionButton addCardButton = new FloatingActionButton.Builder(this)
                .setContentView(imageView)
                .setBackgroundDrawable(R.drawable.selector_plus_button)
                .build();
        addCardButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent("com.example.ayush.customerapplication.CARDETAILS"));
                    }
                }
        );
    }

    public List<Information> getData(){
        List<Information> data = new ArrayList<>();
        bgImage = new ArrayList<>();
        cardNo = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            bgImage.add(R.drawable.logo4);
            cardNo.add("52XX-XXXX-XXXX-1234");
        }
        SavedCardsDB info = new SavedCardsDB(this);
        try {
            info.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String dataFromDB = null;
        try {
            dataFromDB = info.getData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        info.close();
        dataFromDB.trim();
        int index = dataFromDB.indexOf('\t');


        Iterator iterator1 = bgImage.iterator();
        Iterator iterator2 = cardNo.iterator();
        while(iterator1.hasNext() && iterator2.hasNext()){
            Information current = new Information();
            current.bgImageID = (int) iterator1.next();
            current.cardNum = (String) iterator2.next();
            data.add(current);
        }
        return data;
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
