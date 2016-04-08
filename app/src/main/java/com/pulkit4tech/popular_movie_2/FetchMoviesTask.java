package com.pulkit4tech.popular_movie_2;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.pulkit4tech.popular_movie_2.data_object.MovieDetailDO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;



public class FetchMoviesTask extends AsyncTask<String, Void, String> {

    private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
    ArrayList<MovieDetailDO> details = new ArrayList<MovieDetailDO>();
    private String posterPath;
    private String requestURL;
    public CompletedTask delegate = null;

    //Set the delegate
    public FetchMoviesTask (CompletedTask delegate) {
        this.delegate = delegate;
    }

    @Override
    protected String doInBackground(String... params) {
        requestURL = params[0];

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String movieJsonStr = null;
        try {
            Uri builtUri = Uri.parse(requestURL).buildUpon()
                    .build();

            URL url = new URL(builtUri.toString());
            Log.v(LOG_TAG, "Built URI " + builtUri.toString());

            // Create the request to MovieDB, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                Log.v(LOG_TAG, "InputStream is null");
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                Log.v(LOG_TAG, "Buffer length is 0");
                // Stream was empty.  No point in parsing.
                return null;
            }
            movieJsonStr = buffer.toString();
            Log. v(LOG_TAG, "Movie string: " + movieJsonStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } finally{
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
        return movieJsonStr;
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.completedTask(result);
    }

    public interface CompletedTask {
        void completedTask(String result);
    }


}
