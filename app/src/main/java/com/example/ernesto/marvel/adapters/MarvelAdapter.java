package com.example.ernesto.marvel.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.ernesto.marvel.R;
import com.example.ernesto.marvel.VolleySingleton;
import com.example.ernesto.marvel.pojo.MarvelDude;

import java.util.List;

/**
 * Created by ernesto on 2/2/18.
 */

public class MarvelAdapter extends ArrayAdapter<MarvelDude> {

    private Context context;

    public MarvelAdapter(Context context, int resource, List<MarvelDude> objects) {
        super(context, resource, objects);
        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MarvelDude marveldude = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.marvel_layout, parent, false);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.collection);
        NetworkImageView networkImageView = (NetworkImageView)convertView.findViewById(R.id.imageView2);

        textView.setText(marveldude.name);
        RequestQueue requestQueue = VolleySingleton.getInstance(context).getRequestQueue();

        ImageLoader imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {

            private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(10);

            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });

        networkImageView.setImageUrl(marveldude.url, imageLoader);
        //networkImageView.setImageUrl("http://i.annihil.us/u/prod/marvel/i/mg/3/40/4bb4680432f73/portrait_xlarge.jpg", imageLoader);

        //https://goo.gl/mBTVGT
        return convertView;
    }
}
