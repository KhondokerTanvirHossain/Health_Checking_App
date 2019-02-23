package com.example.v.bluetooth;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class AdapterActivity extends ArrayAdapter<Health>{

    public AdapterActivity(@NonNull Context context, int resource, ArrayList<Health> options) {
        super(context, resource, options);
    }
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null)
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_view,parent,false);
        Health current = getItem(position);
        TextView textStart = (TextView)listItemView.findViewById(R.id.list_bmp);
        textStart.setText(current.getBpm());
        TextView textStop = (TextView)listItemView.findViewById(R.id.list_so2);
        textStop.setText(current.getSo2());
        TextView textTime = (TextView)listItemView.findViewById(R.id.list_temp);
        textTime.setText(current.getTem());
        return listItemView;
    }
}
