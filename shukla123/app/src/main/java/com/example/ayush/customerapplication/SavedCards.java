package com.example.ayush.customerapplication;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Created by Ayush on 28-05-2015.
 */
public class SavedCards extends AppCompatActivity implements VizAdapter.ClickListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, ResultCallback<People.LoadPeopleResult> {
    private RecyclerView recyclerView;
    private VizAdapter adapter;
    private List<Integer> bgImage;
    List<String> cardNo, name, cardLabel, month, year, rowId, defaultCard, background_color, activeEmail;
    List<Integer> indexTab, indexNewLine;
    List<Information> data;
    int getPosition;
    RelativeLayout relative_layout_top, rel;
    TextView tvAddCardInfo;
    Toolbar toolbar;
    GoogleApiClient mGoogleApiClient;
    boolean mSignInClicked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_cards);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        adapter = new VizAdapter(this, getData());
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_PROFILE).build();

        SavedCardsDB info = new SavedCardsDB(this);
        try {
            info.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (info.getRowIds().size() == 0) {
            addCardInfo();
        }
        info.close();
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
                        Intent intent = new Intent(SavedCards.this, CardDetails.class);
                        startActivity(intent);
                    }
                }
        );

    }

    private void addCardInfo() {
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

    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle arg0) {
        mSignInClicked = false;
        Plus.PeopleApi.loadVisible(mGoogleApiClient, null).setResultCallback(this);
    }

    public void onResult(People.LoadPeopleResult arg0) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.mAbout) {
            Intent intent2 = new Intent(SavedCards.this, About.class);
            startActivity(intent2);
            return true;
        }

        if (id == R.id.mHelp) {
            Intent intent2 = new Intent(SavedCards.this, Help.class);
            startActivity(intent2);
            return true;
        }

        if (id == R.id.logOut) {
            Log.v("Internet Connection", "" + isNetworkAvailable());
            if (isNetworkAvailable() == true) {
                if (mGoogleApiClient.isConnected()) {
                    Log.v("ponds", "imadsa");
                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                    mGoogleApiClient.disconnect();
                    mGoogleApiClient.connect();
                    Intent intent1 = new Intent(SavedCards.this, SignInActivity.class);
                    startActivity(intent1);
                    SavedCardsDB info = new SavedCardsDB(this);
                    try {
                        info.open();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    info.logout();
                    info.close();
                }
            } else if (!isNetworkAvailable()) {
                Toast.makeText(this, "Please check your Internet Connection", Toast.LENGTH_SHORT).show();
            }

        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    public List<Information> getData() {
        data = new ArrayList<>();
        bgImage = new ArrayList<>();
        rowId = new ArrayList<>();
        cardNo = new ArrayList<>();
        name = new ArrayList<>();
        cardLabel = new ArrayList<>();
        month = new ArrayList<>();
        year = new ArrayList<>();
        indexTab = new ArrayList<>();
        indexNewLine = new ArrayList<>();
        defaultCard = new ArrayList<>();
        background_color = new ArrayList<>();
        activeEmail = new ArrayList<>();
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
        SavedCardsDB info2 = new SavedCardsDB(this);
        try {
            info2.open();
            if (info2.getRowIds().size() == 1 && !info2.getRowIds().get(0).trim().contentEquals(info2.getRowOfDefaultCard().trim())) {
                String rowIDToBeModified = info2.getRowIds().get(0).trim();
                info2.setDefaultCard(rowIDToBeModified, true);
                info2.close();
                recreate();
            }
        } catch (SQLException e) {
            Dialog dialog = new Dialog(this);
            dialog.setTitle("Shoot! Something went wrong");
            TextView tv = new TextView(this);
            tv.setText(e.toString());
            dialog.setContentView(tv);
            dialog.show();
        }
        //Toast.makeText(this, "" + indexNewLine.size(), Toast.LENGTH_SHORT).show();
        int tabsInOneLine = indexTab.size() / indexNewLine.size();
        if (indexTab.size() > 1) {
            rowId.add(dataFromDB.substring(0, indexTab.get(0)));
        }
        for (i = 0; i < indexTab.size() - tabsInOneLine; i += tabsInOneLine) {
            rowId.add(dataFromDB.substring(indexNewLine.get(i / tabsInOneLine) + 1, indexTab.get(i + tabsInOneLine)));
        }
        for (i = 0; i < indexTab.size() - 1; i += tabsInOneLine) {
            cardNo.add(dataFromDB.substring(indexTab.get(i) + 1, indexTab.get(i + 1)));
            name.add(dataFromDB.substring(indexTab.get(i + 1) + 1, indexTab.get(i + 2)));
            cardLabel.add(dataFromDB.substring(indexTab.get(i + 3) + 1, indexTab.get(i + 4)));
            month.add(dataFromDB.substring(indexTab.get(i + 4) + 1, indexTab.get(i + 5)));
            year.add(dataFromDB.substring(indexTab.get(i + 5) + 1, indexTab.get(i + 6)));
            defaultCard.add(dataFromDB.substring(indexTab.get(i + 6) + 1, indexTab.get(i + 7)));
            background_color.add(dataFromDB.substring(indexTab.get(i + 7) + 1, indexTab.get(i + 8)));
            activeEmail.add(dataFromDB.substring(indexTab.get(i + 8) + 1, indexNewLine.get(i / tabsInOneLine)));
        }

        Iterator iterator2 = cardNo.iterator();
        Iterator iterator3 = name.iterator();
        Iterator iterator4 = cardLabel.iterator();
        Iterator iterator5 = month.iterator();
        Iterator iterator6 = year.iterator();
        Iterator iterator0 = rowId.iterator();
        Iterator iterator7 = defaultCard.iterator();
        Iterator iterator8 = background_color.iterator();
        Iterator iterator9 = activeEmail.iterator();
        while (iterator2.hasNext() && iterator3.hasNext() && iterator4.hasNext() && iterator5.hasNext() && iterator6.hasNext() && iterator0.hasNext() && iterator7.hasNext() && iterator8.hasNext() && iterator9.hasNext()) {
            Information current = new Information();
            current.cardNum = (String) iterator2.next();
            current.name = (String) iterator3.next();
            current.cardLabel = (String) iterator4.next();
            current.month = (String) iterator5.next();
            current.year = (String) iterator6.next();
            current.rowID = (String) iterator0.next();
            current.defaultCard = (String) iterator7.next();
            current.background_color = (String) iterator8.next();
            current.activeEmail = (String) iterator9.next();
            SavedCardsDB info3 = new SavedCardsDB(this);
            try {
                info3.open();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (info3.getActiveEmail().contentEquals(current.activeEmail)) {
                data.add(current);
            }
            info3.close();
        }
        return data;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onConnectionFailed(ConnectionResult arg0) {

    }

    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }

    private static long back_pressed;

    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()) super.onBackPressed();
        else
            Toast.makeText(getBaseContext(), "Press back once again to exit!", Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();
    }

    @Override
    public void itemClicked(int id, int position, boolean isChecked) {
        switch (id) {
            case 2131493002:
            case R.id.mSetAsDefault:
                try {
                    SavedCardsDB entryToBeModified = new SavedCardsDB(this);
                    entryToBeModified.open();
                    String rowIDToBeModified = entryToBeModified.getRowIds().get(position);
                    entryToBeModified.setDefaultCard(rowIDToBeModified, true);
                    entryToBeModified.close();
                    recreate();
                    //cardView.setCardBackgroundColor(Color.parseColor("#00897B"));
                    Toast.makeText(this, "Default card updated", Toast.LENGTH_SHORT).show();
                } catch (SQLException e) {
                    Dialog dialog2 = new Dialog(this);
                    dialog2.setTitle("Shoot! Something went wrong");
                    TextView tv = new TextView(this);
                    tv.setText(e.toString());
                    dialog2.setContentView(tv);
                    dialog2.show();
                }
                break;
            case R.id.mSetColor:
                getPosition = position;
                Intent intent3 = new Intent(SavedCards.this, ColorPicker.class);
                startActivityForResult(intent3, 0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String result = data.getStringExtra("bgColor");
            try {
                SavedCardsDB entryToBeModified = new SavedCardsDB(this);
                entryToBeModified.open();
                String rowIDToBeModified = entryToBeModified.getRowIds().get(getPosition);
                entryToBeModified.setCardBackgroundColor(rowIDToBeModified, result);
                entryToBeModified.close();
                recreate();
                Toast.makeText(this, "Card color updated", Toast.LENGTH_SHORT).show();
            } catch (SQLException e) {
                Dialog dialog2 = new Dialog(this);
                dialog2.setTitle("Shoot! Something went wrong");
                TextView tv = new TextView(this);
                tv.setText(e.toString());
                dialog2.setContentView(tv);
                dialog2.show();
            }
        }
    }
}
