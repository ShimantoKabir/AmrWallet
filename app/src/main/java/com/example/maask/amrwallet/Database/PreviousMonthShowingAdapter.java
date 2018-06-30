package com.example.maask.amrwallet.Database;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.maask.amrwallet.R;

import java.util.ArrayList;

/**
 * Created by Maask on 1/5/2018.
 */

public class PreviousMonthShowingAdapter extends ArrayAdapter<String> {

    private Context context;
    private ArrayList<String> strings;
    int resources;

    public PreviousMonthShowingAdapter(@NonNull Context context, ArrayList<String> strings, int resources) {
        super(context,resources,strings);

        this.context = context;
        this.strings = strings;
        this.resources = resources;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(resources,parent,false);

        TextView textView = convertView.findViewById(R.id.show_in);

        textView.setText(strings.get(position));

        return convertView;

    }
}
