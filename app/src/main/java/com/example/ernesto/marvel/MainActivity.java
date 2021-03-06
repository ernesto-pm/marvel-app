package com.example.ernesto.marvel;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.ernesto.marvel.adapters.ItuneArrayAdapter;
import com.example.ernesto.marvel.adapters.MarvelAdapter;
import com.example.ernesto.marvel.pojo.MarvelDude;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class MainActivity extends Activity {

    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ItuneArrayAdapter ituneArrayAdapter;
    private MarvelAdapter marvelAdapter;

    private RequestQueue mQueue;

    private String offset, limit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.lista);


        offset = "0";
        limit = "100";

        /*
        ituneArrayAdapter = new ItuneArrayAdapter(this,
                R.layout.marvel_layout, new ArrayList<Itune>());
        listView.setAdapter(ituneArrayAdapter);

        new ProcesaJson(ituneArrayAdapter).execute("https://itunes.apple.com/search?term=la+banda+machos");
        */




        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<String>());
        //listView.setAdapter(adapter);
        //mQueue = VolleySingleton.getInstance(this).getRequestQueue();
        //jsonMarvel(getMarvelString(), adapter);

        marvelAdapter = new MarvelAdapter(this, R.layout.marvel_layout, new ArrayList<MarvelDude>());
        listView.setAdapter(marvelAdapter);
        mQueue = VolleySingleton.getInstance(this).getRequestQueue();
        jsonMarvel(getMarvelString(), marvelAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                MarvelDude marvelDude = marvelAdapter.getItem(i);
                //Log.i("Algo", marvelDude.id);
                //Toast.makeText(getApplicationContext(), marvelDude.id, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(MainActivity.this, DudeDetail.class);
                intent.putExtra("MarvelDude", marvelDude);
                startActivity(intent);
                finish();
            }
        });



        //new MarvelJson(adapter).execute();

    }

    public void incrementOffset(View view) {
        int off = Integer.parseInt(offset);
        //int lim = Integer.parseInt(limit);

        off += 100;
        //lim += 100;

        Log.i("Offset", ""+off);
        //Log.i("Limit", ""+lim);

        offset = off + "";
        //limit = lim + "";

        marvelAdapter = new MarvelAdapter(this, R.layout.marvel_layout, new ArrayList<MarvelDude>());
        listView.setAdapter(marvelAdapter);
        mQueue = VolleySingleton.getInstance(this).getRequestQueue();
        jsonMarvel(getMarvelString(), marvelAdapter);
    }

    public void reduceOffset(View view) {
        int off = Integer.parseInt(offset);
        //int lim = Integer.parseInt(limit);

        off -= 100;
        //lim += 100;

        Log.i("Offset", ""+off);
        //Log.i("Limit", ""+lim);

        offset = off + "";
        //limit = lim + "";

        marvelAdapter = new MarvelAdapter(this, R.layout.marvel_layout, new ArrayList<MarvelDude>());
        listView.setAdapter(marvelAdapter);
        mQueue = VolleySingleton.getInstance(this).getRequestQueue();
        jsonMarvel(getMarvelString(), marvelAdapter);


    }



    private void jsonMarvel(String url, final MarvelAdapter adapter) {
        adapter.clear();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                url, null,
                // success
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject data = response.getJSONObject("data");
                            JSONArray jsonArray = data.getJSONArray("results");

                            for(int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                JSONObject thumbnail = jsonObject.getJSONObject("thumbnail");

                                StringBuffer imageUrl =  new StringBuffer();
                                imageUrl.append(thumbnail.getString("path"));
                                imageUrl.append("/portrait_small");
                                imageUrl.append(".");
                                imageUrl.append(thumbnail.getString("extension"));

                                MarvelDude marveldude = new MarvelDude();
                                marveldude.name = jsonObject.getString("name");
                                marveldude.url = imageUrl.toString();
                                marveldude.id = jsonObject.getLong("id") + "";
                                marveldude.description = jsonObject.getString("description");

                                Log.i(marveldude.name,marveldude.description);

                                //adapter.add(jsonObject.getString("name"));
                                adapter.add(marveldude);
                            }
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                },
                // error
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        mQueue.add(request);
    }

    private final String LOG_TAG = "MARVEL";
    private static char[] HEXCodes = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};

    private String getMarvelString() {
        /*
            Investiga y reporta qué es md5?

            */
        String ts = Long.toString(System.currentTimeMillis() / 1000);
        String apikey = "c27d28b65c43202b5efb264465e5d737";
        String hash = md5(ts + "66b44afe56c273f14b42aa48c3b7458774965fcb" + "c27d28b65c43202b5efb264465e5d737");
        ArrayList<String> arrayList = new ArrayList<>();


            /*
                Conexión con el getway de marvel
            */
        final String CHARACTER_BASE_URL =
                "http://gateway.marvel.com/v1/public/characters";

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

            /*
                Configuración de la petición
            */
        String characterJsonStr = null;
        final String TIMESTAMP = "ts";
        final String API_KEY = "apikey";
        final String HASH = "hash";
        final String ORDER = "orderBy";

        Uri builtUri;
        builtUri = Uri.parse(CHARACTER_BASE_URL+"?").buildUpon()
                .appendQueryParameter(TIMESTAMP, ts)
                .appendQueryParameter(API_KEY, apikey)
                .appendQueryParameter(HASH, hash)
                .appendQueryParameter(ORDER, "name")
                .appendQueryParameter("limit", limit)
                .appendQueryParameter("offset",offset)
                .build();

        return builtUri.toString();
    }

    /*
        Investiga y reporta qué es md5:

    */
    public static String md5(String s) {
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            String hash = new String(hexEncode(digest.digest()));
            return hash;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    /*
        Investiga y reporta qué hace esta aplicación
    */
    public static String hexEncode(byte[] bytes) {
        char[] result = new char[bytes.length*2];
        int b;
        for (int i = 0, j = 0; i < bytes.length; i++) {
            b = bytes[i] & 0xff;
            result[j++] = HEXCodes[b >> 4];
            result[j++] = HEXCodes[b & 0xf];
        }
        return new String(result);
    }


}
