package gj.udacity.project1.popularmovies.Data;

import org.json.JSONException;
import org.json.JSONObject;

/*
This class save data of all fetched movies.
 */
public class MovieDataClass {
    private String image,overview,releaseDate,movieTitle;
    private int movieId;
    private double voteAvg;

    public MovieDataClass(JSONObject movieJson){
        try {
            image = movieJson.getString("poster_path");
            movieId = movieJson.getInt("id");
            overview = movieJson.getString("overview");
            voteAvg = movieJson.getDouble("vote_average");
            movieTitle = movieJson.getString("original_title");
            releaseDate = movieJson.getString("release_date");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public double getVoteAvg() {
        return voteAvg;
    }

    public int getMovieId() {
        return movieId;
    }

    public String getImage() {
        return image;
    }

    public String getOverview() {
        return overview;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getReleaseDate() {
        return releaseDate;
    }
}
