package com.example.ayush.customerapplication;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.security.auth.callback.Callback;

public class VizAdapter extends RecyclerView.Adapter<VizAdapter.MyViewHolder> {
    private LayoutInflater inflater;
    List<Information> data = Collections.emptyList();
    Context context;
    private ClickListener clickListener;

    public VizAdapter(Context context, List<Information> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    public void deleteRow(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = inflater.inflate(R.layout.custom_row, parent, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.card_toolbar);
        toolbar.setTitle("My Card");
        if (toolbar != null) {
            // inflate your menu
            /*toolbar.inflateMenu(R.menu.card_menu);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.mSetAsDefault:
                            break;
                    }
                    return true;
                }
            });*/
        }
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Information current = data.get(position);
        SavedCardsDB info = new SavedCardsDB(context);
        try {
            info.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (info.getActiveEmail().contentEquals(current.activeEmail)) {
            String currentCardNo;
            currentCardNo = current.cardNum.substring(0, 2) + "XX-XXXX-XXXX-" + current.cardNum.substring(15);
            holder.tvCardNumber.setText(currentCardNo);
            holder.tvName.setText(current.name);
            holder.cardView.setCardBackgroundColor(Color.parseColor(current.background_color));
            if (current.cardLabel.trim().length() > 0) {
                if (current.cardLabel.length() > 20) {
                    holder.toolbar.setTitle(current.cardLabel.substring(0, 19) + "...");
                } else {
                    holder.toolbar.setTitle(current.cardLabel);
                }
            }
            if (Boolean.parseBoolean(current.defaultCard)) {
                holder.imDefaultIndicator.setVisibility(View.VISIBLE);
            }
            int cardLogoId = R.drawable.unknown_logo2;
            String cardType;
            if (current.cardNum.startsWith("4")) {
                cardType = "Visa";
                cardLogoId = R.drawable.visa_logo;
            } else if (current.cardNum.startsWith("5")) {
                cardType = "MasterCard";
                cardLogoId = R.drawable.master_card_logo;
            } else if (current.cardNum.startsWith("37")) {
                cardType = "American Express";
                cardLogoId = R.drawable.american_express_logo;
            } else if (current.cardNum.startsWith("50") || current.cardNum.startsWith("56") || current.cardNum.startsWith("57") || current.cardNum.startsWith("58") || current.cardNum.startsWith("59") || current.cardNum.startsWith("6")) {
                cardType = "Maestro Card";
                cardLogoId = R.drawable.maestro_logo;
            } else {
                cardType = "Unknown Card Type";
                cardLogoId = R.drawable.unknown_logo2;
            }
            holder.cardLogo.setImageResource(cardLogoId);
        }
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener, View.OnLongClickListener {

        TextView tvCardNumber, tvName;
        Button bMenu;
        Toolbar toolbar;
        CardView cardView;
        ImageView cardLogo, imDefaultIndicator;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvCardNumber = (TextView) itemView.findViewById(R.id.tvCardNumber);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            toolbar = (Toolbar) itemView.findViewById(R.id.card_toolbar);
            bMenu = (Button) itemView.findViewById(R.id.bMenu);
            bMenu.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            cardView = (CardView) itemView.findViewById(R.id.cv);
            cardLogo = (ImageView) itemView.findViewById(R.id.card_logo);
            imDefaultIndicator = (ImageView) itemView.findViewById(R.id.imDefaultIndicator);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.bMenu:
                    PopupMenu popupMenu = new PopupMenu(context, v);
                    MenuInflater inflater = popupMenu.getMenuInflater();
                    inflater.inflate(R.menu.card_menu, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(this);
                    popupMenu.show();
                    break;
            }

        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.mSetAsDefault:
                    SavedCardsDB info = new SavedCardsDB(context);
                    try {
                        info.open();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    if (!info.getRowIds().get(getAdapterPosition()).trim().contentEquals(info.getRowOfDefaultCard().trim())) {
                        if (clickListener != null) {
                            clickListener.itemClicked(item.getItemId(), getAdapterPosition(), true);
                        }
                    } else {
                        Toast.makeText(context, "Already set as default card", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.mDelete:
                    final SavedCardsDB info2 = new SavedCardsDB(context);
                    try {
                        info2.open();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    if (!info2.getRowIds().get(getAdapterPosition()).trim().contentEquals(info2.getRowOfDefaultCard().trim())) {
                        new MaterialDialog.Builder(context)
                                //.title("Delete Card")
                                .content("Delete card?")
                                .positiveText("Delete")
                                .negativeText("Cancel")
                                .callback(new MaterialDialog.ButtonCallback() {
                                              @Override
                                              public void onPositive(MaterialDialog dialog) {
                                                  deleteCard();
                                              }

                                              @Override
                                              public void onNegative(MaterialDialog dialog) {
                                              }
                                          }
                                )
                                .show();
                    } else if (info2.getRowIds().size() == 1) {
                        new MaterialDialog.Builder(context)
                                .title("Delete Card?")
                                .content("Are you sure you want to delete your only card? You will have no saved cards and you will not ba able to use use this app.")
                                .positiveText("Delete")
                                .negativeText("Cancel")
                                .callback(new MaterialDialog.ButtonCallback() {
                                              @Override
                                              public void onPositive(MaterialDialog dialog) {
                                                  deleteCard();
                                              }

                                              @Override
                                              public void onNegative(MaterialDialog dialog) {
                                              }
                                          }
                                )
                                .show();
                    } else {
                        Toast.makeText(context, "Can't delete default card", Toast.LENGTH_SHORT).show();
                    }
                    info2.close();
                    break;
                case R.id.mSetColor:
                    if (clickListener != null) {
                        clickListener.itemClicked(item.getItemId(), getAdapterPosition(), true);
                    }
                    break;
            }
            return false;
        }

        private void deleteCard() {
            try {
                SavedCardsDB entryToDelete = new SavedCardsDB(context);
                entryToDelete.open();
                String rowIDToDelete = entryToDelete.getRowIds().get(getAdapterPosition());
                entryToDelete.deleteEntry(rowIDToDelete);
                entryToDelete.close();
                deleteRow(getAdapterPosition());
                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
            } catch (SQLException e) {
                Dialog dialog2 = new Dialog(context);
                dialog2.setTitle("Shoot! Something went wrong");
                TextView tv = new TextView(context);
                tv.setText(e.toString());
                dialog2.setContentView(tv);
                dialog2.show();
            }
        }


        @Override
        public boolean onLongClick(View v) {
            final int id = v.getId();
            SavedCardsDB info = new SavedCardsDB(context);
            try {
                info.open();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (!info.getRowIds().get(getAdapterPosition()).trim().contentEquals(info.getRowOfDefaultCard().trim())) {
                MaterialDialog materialDialog = new MaterialDialog.Builder(context)
                        //.title("Default Card")
                        .content("Set as default card?")
                        .positiveText("Set Default")
                        .negativeText("Cancel")
                        .callback(new MaterialDialog.ButtonCallback() {
                                      @Override
                                      public void onPositive(MaterialDialog dialog) {
                                          if (clickListener != null) {
                                              clickListener.itemClicked(id, getAdapterPosition(), true);
                                          }
                                      }

                                      @Override
                                      public void onNegative(MaterialDialog dialog) {
                                      }
                                  }
                        )
                        .show();
            } else {
                Toast.makeText(context, "Already set as default card", Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    }

    public interface ClickListener {
        public void itemClicked(int id, int position, boolean isChecked);
    }
}
