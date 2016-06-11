package com.project.cs454.minesweeper;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
import java.util.Arrays;
import java.util.List;

public class HighScore extends AppCompatActivity {

    private ArrayAdapter<String> scoreAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String[] data = {"DUMMY", "SCORES"};
        List<String> scores = new ArrayList<String>(Arrays.asList(data));
        scoreAdapter = new ArrayAdapter<String>( this, R.layout.highscore_view, R.id.list_item_score_textview, scores);

        ListView listView = (ListView) findViewById(R.id.listview_score);
        listView.setAdapter(scoreAdapter);
        final HighScore context = this;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String score = scoreAdapter.getItem(position);
                Toast.makeText(context, score, Toast.LENGTH_SHORT).show();
            }
        });
        FetchScoresTask fetchScoresTask = new FetchScoresTask();
        fetchScoresTask.execute();
    }

    public class FetchScoresTask extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = FetchScoresTask.class.getSimpleName();

        private String[] getScoreDataFromJson(String scoreJsonStr)
                throws JSONException {
            final String SCORES_OBJECT = "result";
            final String SCORES = "rows";

            JSONObject scoreJson = new JSONObject(scoreJsonStr);
            JSONObject resultObject = scoreJson.getJSONObject(SCORES_OBJECT);
            JSONArray scoreArray = resultObject.getJSONArray(SCORES);

            String[] resultStrs = new String[scoreArray.length()];
            for(int i = 0; i < scoreArray.length(); i++) {
                String scoreTitle, nameTitle, difficulty;
                JSONObject score = scoreArray.getJSONObject(i);
                scoreTitle = score.getString("score");
                nameTitle = score.getString("name");
                difficulty = score.getString("difficulty");

                resultStrs[i] = nameTitle + " : " + scoreTitle + " - " + difficulty;
            }
            return resultStrs;

        }
        @Override
        protected String[] doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String scoreJsonStr = null;


            try {
                URL url = new URL("http://tylerthome.com/minesweeper/scores");

                Log.d(LOG_TAG, "Call to HTTP API: " + url);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                scoreJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getScoreDataFromJson(scoreJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            if (result != null) {
                scoreAdapter.clear();
                for(String dayScoreStr : result) {
                    scoreAdapter.add(dayScoreStr);
                }
            }
        }
    }

}
