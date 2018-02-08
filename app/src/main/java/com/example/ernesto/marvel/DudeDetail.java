package com.example.ernesto.marvel;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.LruCache;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.ernesto.marvel.pojo.MarvelDude;

public class DudeDetail extends Activity {

    TextView name, description;
    NetworkImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dude_detail);

        MarvelDude marvelDude = (MarvelDude) getIntent().getSerializableExtra("MarvelDude");

        imageView = (NetworkImageView) findViewById(R.id.imageView);
        name = (TextView) findViewById(R.id.name);
        description = (TextView) findViewById(R.id.description);


        RequestQueue requestQueue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
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
        imageView.setImageUrl(marvelDude.url, imageLoader);

        name.setText(marvelDude.name);
        description.setText(marvelDude.description);
    }

    public void goBack(View view) {
        Intent intent = new Intent(DudeDetail.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
