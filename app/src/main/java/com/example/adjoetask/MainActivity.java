package com.example.adjoetask;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adjoetask.adapter.AdjoeAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setViews();
        setValues();
    }

    private void setViews() {
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void setValues() {
        registerReceiver();
        if(isNetworkConnected())
          new ImageDownloadTask().execute(Util.BASE_URL);
        else {
            Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }
    }



    private void setAdapter(JSONArray jsonArray) {
        AdjoeAdapter adapter = new AdjoeAdapter(MainActivity.this, sortedArray(jsonArray));
        recyclerView.setAdapter(adapter);
    }

    private JSONArray sortedArray(JSONArray jsonArray) {
        JSONArray sortedJsonArray = new JSONArray();
        try {
            List list = new ArrayList();
            for (int i = 0; i < jsonArray.length(); i++) {
                list.add(jsonArray.getJSONObject(i));
            }

            Collections.sort(list, new Comparator<JSONObject>() {
                @Override
                public int compare(JSONObject o1, JSONObject o2) {
                    int valueOne = 0, valueTwo = 0;
                    try {
                        valueOne = o1.getInt("id");
                        valueTwo = o2.getInt("id");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    return Integer.compare(valueOne, valueTwo);
                }
            });
            for (int i = 0; i < list.size(); i++) {
                sortedJsonArray.put(list.get(i));
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return sortedJsonArray;

    }

    public void registerReceiver() {
        MyStartServiceReceiver myReceiver = new MyStartServiceReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(myReceiver, filter);
    }

    private class ImageDownloadTask extends AsyncTask<String, Void, String> {


        protected String doInBackground(String... addresses) {
            URL url = null;
            StringBuilder stringBuilder;
            try {
                url = new URL(addresses[0]);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                InputStream in = conn.getInputStream();
                stringBuilder = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return stringBuilder.toString();

        }

        @Override
        protected void onPostExecute(String result) {

            try {
                JSONArray jsonArray = new JSONArray(result);
                setAdapter(jsonArray);

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

        }
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

}


