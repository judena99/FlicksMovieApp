package CodePath.com.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Movie {
    private String title;
    private String overview;
    private String posterPath; // only the path
    private  String backdropPath;

    //initialiser a partir du JSON data
    public Movie(JSONObject object) throws JSONException {
        title = object.getString("title");
        overview = object.getString("overview");
        posterPath = object.getString("poster_path");
        backdropPath = object.getString("backdrop_path");
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public static ArrayList<Movie> transformListToMovie(JSONArray arrayJson){

        ArrayList<Movie> list = new ArrayList<>();

        for (int i = 0; i < arrayJson.length(); i++) {
            Movie movie = null;
            try {
                movie = new Movie(arrayJson.getJSONObject(i));
                list.add(movie);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return list;
    }
}
