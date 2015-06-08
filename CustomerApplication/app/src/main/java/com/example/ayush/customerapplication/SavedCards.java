package com.example.ayush.customerapplication;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Created by Ayush on 28-05-2015.
 */
public class SavedCards extends ActionBarActivity implements VizAdapter.ClickListener, DeleteDialog.Communicator {
    private RecyclerView recyclerView;
    private VizAdapter adapter;
    private List<Integer> bgImage;
    List<String> cardNo, name, cardLabel, month, year, rowId;
    List<Integer> indexTab, indexNewLine;
    int getPosition;
    RelativeLayout relative_layout_top, rel;
    TextView tvAddCardInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_cards);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        adapter = new VizAdapter(this, getData());
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (indexNewLine.get(0) == -1){
            rel = (RelativeLayout) findViewById(R.id.rel);
            relative_layout_top = new RelativeLayout(this);
            RelativeLayout.LayoutParams layoutRules = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT
            );
            rel.addView(relative_layout_top, layoutRules);
            tvAddCardInfo = new TextView(this);
            tvAddCardInfo.setText("Tap the plus button below to add cards to this list");
            RelativeLayout.LayoutParams tvRules = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            tvAddCardInfo.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            tvRules.addRule(RelativeLayout.CENTER_HORIZONTAL);
            tvRules.addRule(RelativeLayout.CENTER_VERTICAL);
            tvAddCardInfo.setGravity(Gravity.CENTER);
            relative_layout_top.addView(tvAddCardInfo, tvRules);
        }

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
        rowId = new ArrayList<>();
        cardNo = new ArrayList<>();
        name = new ArrayList<>();
        cardLabel = new ArrayList<>();
        month = new ArrayList<>();
        year = new ArrayList<>();
        indexTab = new ArrayList<>();
        indexNewLine = new ArrayList<>();
        bgImage.add(R.drawable.logo4);
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
        indexTab.add(dataFromDB.indexOf('\t'));
        int i = 0;
        while (dataFromDB.indexOf('\t', indexTab.get(i) + 1) != -1) {
            indexTab.add(dataFromDB.indexOf('\t', indexTab.get(i) + 1));
            i++;
        }

        i = 0;
        indexNewLine.add(dataFromDB.indexOf('\n'));
        while (dataFromDB.indexOf('\n', indexNewLine.get(i) + 1) != -1) {
            indexNewLine.add(dataFromDB.indexOf('\n', indexNewLine.get(i) + 1));
            i++;
        }
        int tabsInOneLine = indexTab.size() / indexNewLine.size();
        if(indexTab.size() > 1){
            rowId.add(dataFromDB.substring(0, indexTab.get(0)));
        }
        for (i = 0; i < indexTab.size() - 6; i += tabsInOneLine){
            rowId.add(dataFromDB.substring(indexNewLine.get(i / tabsInOneLine) + 1, indexTab.get(i + tabsInOneLine)));
        }
        for (i = 0; i < indexTab.size() -1; i += tabsInOneLine){
            cardNo.add(dataFromDB.substring(indexTab.get(i) + 1, indexTab.get(i+1)));
            name.add(dataFromDB.substring(indexTab.get(i+1) + 1, indexTab.get(i+2)));
            cardLabel.add(dataFromDB.substring(indexTab.get(i+3) + 1, indexTab.get(i+4)));
            month.add(dataFromDB.substring(indexTab.get(i+4) + 1, indexTab.get(i+5)));
            year.add(dataFromDB.substring(indexTab.get(i+5) + 1, indexNewLine.get(i / tabsInOneLine)));
        }

        //TextView tvTest = (TextView) findViewById(R.id.tvTest);
        //tvTest.setText(rowId.get(0));

        //Iterator iterator1 = bgImage.iterator();
        Iterator iterator2 = cardNo.iterator();
        Iterator iterator3 = name.iterator();
        Iterator iterator4 = cardLabel.iterator();
        Iterator iterator5 = month.iterator();
        Iterator iterator6 = year.iterator();
        Iterator iterator0 = rowId.iterator();
        while(iterator2.hasNext() && iterator3.hasNext() && iterator4.hasNext() && iterator5.hasNext() && iterator6.hasNext() && iterator0.hasNext()){
            Information current = new Information();
            current.bgImageID = bgImage.get(0);
            current.cardNum = (String) iterator2.next();
            current.name = (String) iterator3.next();
            current.cardLabel = (String) iterator4.next();
            current.month = (String) iterator5.next();
            current.year = (String) iterator6.next();
            current.rowID = (String) iterator0.next();
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

    @Override
    public void itemClicked(View view, int position) {
        if (view.getId() == R.id.bDeleteCard){
            FragmentManager fragmentManager = getFragmentManager();
            DeleteDialog deleteDialog = new DeleteDialog();
            deleteDialog.show(fragmentManager, "DeleteDialog");
            getPosition = position;
        }
    }

    @Override
    public void onDialog(boolean delete) {

        VizAdapter vizAdapter = new VizAdapter(this, getData());
        if (delete){
            try {
                long lRowIDToDelete = Long.parseLong(rowId.get(getPosition));
                SavedCardsDB entryToDelete = new SavedCardsDB(this);
                entryToDelete.open();
                entryToDelete.deleteEntry(lRowIDToDelete);
                entryToDelete.close();
                recreate();
                Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
            } catch (SQLException e) {
                Dialog dialog2 = new Dialog(this);
                dialog2.setTitle("Shoot! Something went wrong");
                TextView tv = new TextView(this);
                tv.setText(e.toString());
                dialog2.setContentView(tv);
                dialog2.show();
            } finally {
                vizAdapter.deleteRow(getPosition);
            }
        }
    }
}
