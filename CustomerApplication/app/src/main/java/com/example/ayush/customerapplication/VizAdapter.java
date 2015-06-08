package com.example.ayush.customerapplication;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Ayush on 28-05-2015.
 */
public class VizAdapter extends RecyclerView.Adapter<VizAdapter.MyViewHolder> {
    private LayoutInflater inflater;
    List<Information> data = Collections.emptyList();
    Context c;
    public List<String> rows;
    private ClickListener clickListener;

    public VizAdapter(Context context, List<Information> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.c = context;
        rows = new ArrayList<>();
    }

    public void deleteRow(int position){
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_row, parent, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.card_toolbar);
        toolbar.setTitle("My Card");
        if (toolbar != null) {
            // inflate your menu
            toolbar.inflateMenu(R.menu.card_menu);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    return true;
                }
            });
        }
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Information current = data.get(position);
        String currentCardNo;
        currentCardNo = current.cardNum.substring(0,2) + "XX-XXXX-XXXX-" + current.cardNum.substring(15);
        holder.tvCardNumber.setText(currentCardNo);
        holder.tvName.setText(current.name);
        holder.tvExpiry.setText(current.month + "/" + current.year);
        if (current.cardLabel.trim().length() > 0){
            holder.toolbar.setTitle(current.cardLabel);
        }
        holder.imBgImage.setImageResource(current.bgImageID);
        rows.add(current.rowID);
    }

    public void setClickListener(ClickListener clickListener){
        this.clickListener = clickListener;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvCardNumber, tvExpiry, tvName;
        ImageView imBgImage;
        Button bDeleteCard;
        Toolbar toolbar;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvCardNumber = (TextView) itemView.findViewById(R.id.tvCardNumber);
            tvExpiry = (TextView) itemView.findViewById(R.id.tvExpiry);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            imBgImage = (ImageView) itemView.findViewById(R.id.imBgImage);
            bDeleteCard = (Button) itemView.findViewById(R.id.bDeleteCard);
            bDeleteCard.setOnClickListener(this);
            toolbar = (Toolbar) itemView.findViewById(R.id.card_toolbar);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null){
                clickListener.itemClicked(v, getPosition());
            }
        }
    }

    public interface ClickListener{
        public void itemClicked(View view, int position);
    }
}
