package com.example.ayush.customerapplication;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
    List<String> rows;

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
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Information current = data.get(position);
        holder.tvCardNumber.setText(current.cardNum);
        holder.tvName.setText(current.name);
        holder.tvExpiry.setText(current.month + "/" + current.year);
        holder.tvCardLabel.setText(current.cardLabel);
        holder.imBgImage.setImageResource(current.bgImageID);
        rows.add(current.rowID);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvCardNumber, tvExpiry, tvName, tvCardLabel;
        ImageView imBgImage;
        Button bDeleteCard;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvCardNumber = (TextView) itemView.findViewById(R.id.tvCardNumber);
            tvExpiry = (TextView) itemView.findViewById(R.id.tvExpiry);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvCardLabel = (TextView) itemView.findViewById(R.id.tvCardLabel);
            imBgImage = (ImageView) itemView.findViewById(R.id.imBgImage);
            bDeleteCard = (Button) itemView.findViewById(R.id.bDeleteCard);
            bDeleteCard.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            try {
                long lRowIDToDelete = Long.parseLong(rows.get(getPosition()));
                SavedCardsDB entryToDelete = new SavedCardsDB(c);
                entryToDelete.open();
                entryToDelete.deleteEntry(lRowIDToDelete);
                entryToDelete.close();
                deleteRow(getPosition());
                Toast.makeText(c, "Deleted", Toast.LENGTH_SHORT).show();
            } catch (SQLException e) {
                Dialog dialog2 = new Dialog(c);
                dialog2.setTitle("Shoot! Something went wrong");
                TextView tv = new TextView(c);
                tv.setText(e.toString());
                dialog2.setContentView(tv);
                dialog2.show();
            }
        }
    }
}
