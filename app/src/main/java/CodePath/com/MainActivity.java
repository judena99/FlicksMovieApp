package CodePath.com;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.Toast;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import CodePath.com.models.Movie;
import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    //Base de l'API
    public final static String API_BASE_URL = "http://api.themoviedb.org/3";
    //Le nom dparametre de l'API
    public final static String API_KEY_PARAM = "api_key";
    //Cle de l'API
    //tag for loggin from this activity
    public final static String TAG = ("MainActivity");

    AsyncHttpClient client;

    //Lien pour telcharger l'image
    String imageBaseUrl;
    ///le poster size utiliser pour fecthing l'image
    String posterSize;
    //liste de film qui jouent actuellement
    ArrayList<Movie> movies;
    //the recyclerview
//    RecyclerView rvMovies;
    //the recyclerAdapter wired to the recyclerView
    MovieAdapter adapter;

    ShimmerRecyclerView rvMovies;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Initialiser le client
        client = new AsyncHttpClient();
        //initialiser la liste de films
        movies = new ArrayList<>();
        //initialize the adapter --- movies array cannot be initialized after this point
        adapter = new MovieAdapter(this, movies);
        //reslve the recyclerView connect a layout manager and the adsapter
        rvMovies = (ShimmerRecyclerView) findViewById(R.id.rvMovies);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        rvMovies.setAdapter(adapter);

        rvMovies.showShimmerAdapter();




        //get the configurattion on app creation
        getConfiguration();

    }

    //prend la liste de films qui jouent actuellement
    private void getNowPlaying() {
        //creer le lien
        String url = API_BASE_URL + "/movie/now_playing";
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key));// api key always
        //execute a get request expecting a JSON object
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //load the result into movie list
                try {
                    JSONArray results = response.getJSONArray("results");
                    movies.clear();
                    movies.addAll(Movie.transformListToMovie(results));
                    adapter.notifyDataSetChanged();
                    Log.i(TAG, String.format("Loaded movie", results.length()));
                } catch (JSONException e) {
                    logError("Failed to parse now playing movie", e, true);
                }


                rvMovies.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rvMovies.hideShimmerAdapter();
                    }
                }, 2000);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                logError("Failed to get data and nw playing endpoint", throwable, true);
            }
        });
    }

    //Prend la configuration de l'API
    private void getConfiguration() {
        //creer le lien
        String url = API_BASE_URL + "/configuration";
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key));// api key always
        //execute a get request expecting a JSON object
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    //prendre le lien de l'image
                    JSONObject images = response.getJSONObject("images");
                    imageBaseUrl = images.getString("secure_base_url");
                    //prendre le poster size
                    JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");
                    //utiliser l'option at index o3 or w342 as a feedback
                    posterSize = posterSizeOptions.optString(3, "w342");
//                    Log.i(TAG, String.format("Loaded the URL with imageBaseUrl %a and posterSize %a", imageBaseUrl, posterSize));
                    //get the nowPlaying list
                    getNowPlaying();
                } catch (JSONException e) {
                    logError("Failed parsing configuration", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed getting configuration", throwable, true);
            }
        });
    }

    //handle error, log and alert
    private void logError(String message, Throwable error, Boolean alertUser) {
        //tjr donner l'erreur
        Log.e(TAG, message, error);
        if (alertUser) {
            //show a long toast with the erreur
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }
}
