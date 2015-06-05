package com.example.ayush.customerapplication;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Ayush on 05-06-2015.
 */
public class DeleteDialog extends DialogFragment implements View.OnClickListener {
    Button bCancel, bDelete;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.confirm_delete, null);
        bCancel = (Button) view.findViewById(R.id.bCancelDelete);
        bDelete = (Button) view.findViewById(R.id.bConfirmDelete);
        bCancel.setOnClickListener(this);
        bDelete.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bCancelDelete:
                break;
            case R.id.bConfirmDelete:
                break;
        }
    }
}
