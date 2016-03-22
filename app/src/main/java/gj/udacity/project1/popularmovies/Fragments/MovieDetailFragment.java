package gj.udacity.project1.popularmovies.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import gj.udacity.project1.popularmovies.Data.FixedData;
import gj.udacity.project1.popularmovies.Data.MovieDataClass;
import gj.udacity.project1.popularmovies.R;

/*
This fragment show detail of selected movie.
 */
public class MovieDetailFragment extends Fragment {

    private static final java.lang.String ARG = "Position";
    private MovieDataClass currentMovie;
    private int optionNo;
    private ImageView moviePoster;
    private TextView movieTitle, moviePlot, movieDate, movieTrailer;
    private RatingBar movieRating;
    private Button reviewButton,favoriteButton;

    public static MovieDetailFragment newInstance(int position) {

        Bundle args = new Bundle();
        args.putInt(ARG, position);
        MovieDetailFragment fragment = new MovieDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        optionNo = getArguments().getInt(ARG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        if (optionNo != -1) {
            currentMovie = MovieListFragment.movieInfo.get(optionNo);

            movieDate = (TextView) view.findViewById(R.id.movieDate);
            moviePlot = (TextView) view.findViewById(R.id.moviePlot);
            movieTitle = (TextView) view.findViewById(R.id.movieTitle);
            moviePoster = (ImageView) view.findViewById(R.id.moviePoster);
            movieRating = (RatingBar) view.findViewById(R.id.movieRating);
            movieTrailer = (TextView) view.findViewById(R.id.movieTrailer);
            reviewButton = (Button) view.findViewById(R.id.reviewButton);
            favoriteButton = (Button) view.findViewById(R.id.movieFavorite);

            moviePlot.setText(currentMovie.getOverview());
            movieTitle.setText(currentMovie.getMovieTitle());
            movieDate.setText(currentMovie.getReleaseDate());

        /*Here a new request is being sent to fetch image but actually it won't bring whole image
        back because volley would have cached the imaged previously when it called it first to show list.
         */
            String url = "http://image.tmdb.org/t/p/w342";
            Picasso.with(getActivity())
                    .load(url + currentMovie.getImage())
                    .into(moviePoster);

            movieRating.setRating((float) currentMovie.getVoteAvg() / 2);

            movieTrailer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    youtubeIntent();
                }
            });

            reviewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().beginTransaction().replace(R.id.mainFragment, Review.newInstance("" + currentMovie.getMovieId())).commit();
                }
            });

            favoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //insert();
                }
            });
        }
        return view;
    }


    private void youtubeIntent() {

        String url = "http://api.themoviedb.org/3/movie/" + currentMovie.getMovieId() + "/videos?&api_key=" + FixedData.API;
        JsonObjectRequest detail = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        String trailerKey = null;
                        try {
                            trailerKey = jsonObject.getJSONArray("results").getJSONObject(0).getString("key");
                            Log.e("ssd", trailerKey);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("sd", "Sdsd");
                        }
                        Intent youtube = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + trailerKey));
                        startActivity(youtube);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }
        );

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(detail);
    }

}
