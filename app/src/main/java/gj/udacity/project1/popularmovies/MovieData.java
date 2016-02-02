package gj.udacity.project1.popularmovies;

import org.json.JSONException;
import org.json.JSONObject;

public class MovieData {
    String image,overview,releaseDate;
    int movieId;
    double voteAvg;

    public MovieData(JSONObject movieJson){
        try {
            image = movieJson.getString("poster_path");
            movieId = movieJson.getInt("id");
            overview = movieJson.getString("overview");
            voteAvg = movieJson.getDouble("vote_average");


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
