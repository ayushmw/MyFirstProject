package com.example.ayush.customerapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Ayush on 28-05-2015.
 */
public class FragmentCardList extends android.support.v4.app.Fragment {
    private RecyclerView recyclerView;
    private VizAdapter adapter;
    private List<Integer> icons;
    List<String> cardNo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment, container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        adapter = new VizAdapter(getActivity(), getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }
    public List<Information> getData(){
        List<Information> data = new ArrayList<>();
        icons = new ArrayList<>();
        cardNo = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            icons.add(R.drawable.logo4);
            cardNo.add("52XX-XXXX-XXXX-1234");
        }

        Iterator iterator1 = icons.iterator();
        Iterator iterator2 = cardNo.iterator();
        while(iterator1.hasNext() && iterator2.hasNext()){
            Information current = new Information();
            current.iconID = (int) iterator1.next();
            current.cardNum = (String) iterator2.next();
            data.add(current);
        }
        return data;
    }
    public void addCard(String cardNumber, String name, String cvv, String cardLabel, String month, String year){
        icons.add(R.drawable.logo4);
        cardNo.add(cardNumber);
    }
}
