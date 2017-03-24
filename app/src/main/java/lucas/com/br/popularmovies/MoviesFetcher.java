package lucas.com.br.popularmovies;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.List;

/**
 * Created by lucas on 24/03/17.
 */

public class MoviesFetcher extends AsyncTask<String, Void, List<Movie>> {

    public static final String LOG_TAG = MoviesFetcher.class.getName();
    private static final String API_KEY = BuildConfig.API_KEY;

    private ImageAdapter mAdapter;

    public MoviesFetcher(ImageAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    protected List<Movie> doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        try {
            final String SORT_ORDER = params[0];
            final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie/"+SORT_ORDER+"?";
            final String API_KEY_PARAM = "api_key";
            Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                    .appendQueryParameter(API_KEY_PARAM, API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());
            Log.v(LOG_TAG,builtUri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
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
                // Stream was empty.  No point in parsing.
                return null;
            }

            Log.v(LOG_TAG,buffer.toString());
            Type listType = new TypeToken<List<Movie>>() {}.getType();
            Gson gson = new Gson();
            JsonObject obj = new JsonParser().parse(buffer.toString()).getAsJsonObject();
            String results = obj.getAsJsonArray("results").toString();
            List<Movie> movies = gson.fromJson(results,listType);
            return movies;
        }   catch (IOException e) {
            Log.e("MoviesFetcher", "Error ", e);
            return null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("Movies", "Error closing stream", e);
                }
            }
        }
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        Log.d(LOG_TAG,"Data Loaded");
        if(movies != null && !movies.isEmpty()) {
            Log.d(LOG_TAG,"Data updated");
            mAdapter.getMovies().clear();
            mAdapter.setMovies(movies);
            mAdapter.notifyDataSetChanged();
        }
    }
}
