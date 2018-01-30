package com.example.ernesto.marvel.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ernesto.marvel.R;
import com.example.ernesto.marvel.pojo.Itune;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ernesto on 1/26/18.
 */

public class ItuneArrayAdapter extends ArrayAdapter<Itune> {

    private ArrayList<Itune> arrayList;


    public ItuneArrayAdapter(Context context, int resource, List<Itune> objects) {
        super(context, resource, objects);
        arrayList = (ArrayList<Itune>) objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Itune itune = arrayList.get(position);
        //Itune itune1  = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.itunes_layout, parent, false);
        }

        TextView collectionName = (TextView) convertView.findViewById(R.id.collection);
        TextView trackName      = (TextView) convertView.findViewById(R.id.trackName);
        TextView trackPrice     = (TextView) convertView.findViewById(R.id.trackPrice);

        collectionName.setText(itune.collectionName);
        trackName.setText(itune.trackName);
        trackPrice.setText(itune.trackPrice+"");

        return convertView;
    }
}
