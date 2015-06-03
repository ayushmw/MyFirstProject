package com.example.ayush.customerapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by Ayush on 28-05-2015.
 */
public class VizAdapter extends RecyclerView.Adapter<VizAdapter.MyViewHolder> {
    private LayoutInflater inflater;
    List<Information> data = Collections.emptyList();

    public VizAdapter (Context context, List<Information> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
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
        holder.tvExpiry.setText(current.month + "/" + current.year);
        holder.tvCardLabel.setText(current.cardLabel);
        holder.imBgImage.setImageResource(current.bgImageID);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvCardNumber, tvExpiry, tvName, tvCardLabel;
        ImageView imBgImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvCardNumber = (TextView) itemView.findViewById(R.id.tvCardNumber);
            tvExpiry = (TextView) itemView.findViewById(R.id.tvExpiry);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvCardLabel = (TextView) itemView.findViewById(R.id.tvCardLabel);
            imBgImage = (ImageView) itemView.findViewById(R.id.imBgImage);
        }
    }
}
