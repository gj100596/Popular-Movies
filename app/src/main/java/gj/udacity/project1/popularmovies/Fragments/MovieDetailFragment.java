package gj.udacity.project1.popularmovies.Fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import gj.udacity.project1.popularmovies.DBPackage.DBContract;
import gj.udacity.project1.popularmovies.Data.FixedData;
import gj.udacity.project1.popularmovies.Data.MovieDataClass;
import gj.udacity.project1.popularmovies.R;

/*
This fragment show detail of selected movie.
 */
public class MovieDetailFragment extends Fragment{

    private static final java.lang.String ARG = "Position";
    private MovieDataClass currentMovie;
    private int optionNo;
    private ImageView moviePoster;
    private TextView movieTitle, moviePlot, movieDate;
    private RatingBar movieRating;
    private LinearLayout movieTrailerList,movieReviewList;
    private ImageView favoriteButton;

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
            //reviewButton = (Button) view.findViewById(R.id.reviewButton);
            favoriteButton = (ImageView) view.findViewById(R.id.movieFavorite);
            movieTrailerList = (LinearLayout) view.findViewById(R.id.movieTrailerList);
            movieReviewList = (LinearLayout) view.findViewById(R.id.movieReviewList);

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

            loadTrailer();
            loadReviews();

            favoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ContentValues values = new ContentValues();
                    values.put(DBContract.MovieEntry._ID,currentMovie.getMovieId());
                    values.put(DBContract.MovieEntry.COLUMN_IMAGE_URL,currentMovie.getImage());
                    values.put(DBContract.MovieEntry.COLUMN_MOVIE_TITLE,currentMovie.getMovieTitle());
                    values.put(DBContract.MovieEntry.COLUMN_OVERVIEW,currentMovie.getOverview());
                    values.put(DBContract.MovieEntry.COLUMN_VOTE_AVG,currentMovie.getVoteAvg());
                    values.put(DBContract.MovieEntry.COLUMN_RELEASE_DATE,currentMovie.getReleaseDate());

                    getActivity().getContentResolver().insert(DBContract.BASE_URI,values);
                    favoriteButton.setImageResource(R.drawable.ic_star_black_36dp);
                    //insert();
                }
            });
        }
        return view;
    }

    private void loadTrailer() {
        String url = "http://api.themoviedb.org/3/movie/" + currentMovie.getMovieId() + "/videos?&api_key=" + FixedData.API;
        JsonObjectRequest detail = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        JSONArray trailersArray = null;
                        try {
                            ImageView trailerImage = null;
                            trailersArray = jsonObject.getJSONArray("results");
                            for(int position=0;position<trailersArray.length();position++) {
                                JSONObject trailerDetail = new JSONObject(trailersArray.getString(position));
                                trailerImage = new ImageView(movieTrailerList.getContext());
                                String key = trailerDetail.getString("key");
                                trailerImage.setTag(key);

                                trailerImage.setPadding(10,0,10,0);

                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT);
                                trailerImage.setLayoutParams(params);

                                Picasso.with(getActivity())
                                        .load("http://img.youtube.com/vi/" + key + "/sddefault.jpg")
                                        .placeholder(R.drawable.placeholder)
                                        .into(trailerImage);

                                movieTrailerList.addView(trailerImage);
                            }
                            assert trailerImage != null;
                            trailerImage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent youtube = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" +
                                            v.getTag().toString()));
                                    getActivity().startActivity(youtube);
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e){}
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

    private void loadReviews() {
        String url = "http://api.themoviedb.org/3/movie/" + currentMovie.getMovieId() + "/reviews?&api_key=" + FixedData.API;
        JsonObjectRequest detail = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        JSONArray reviewsArray = null;
                        try {
                            reviewsArray = jsonObject.getJSONArray("results");
                            for (int position = 0; position < reviewsArray.length(); position++) {
                                JSONObject reviewDetail = new JSONObject(reviewsArray.getString(position));

                                LinearLayout review = new LinearLayout(movieReviewList.getContext());
                                review.setOrientation(LinearLayout.VERTICAL);

                                TextView author = new TextView(review.getContext());
                                author.setText(reviewDetail.getString("author"));
                                TextView comment = new TextView(review.getContext());
                                author.setText(reviewDetail.getString("content"));

                                review.addView(author);
                                review.addView(comment);

                                review.setPadding(10, 0, 10, 0);

                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT);
                                review.setLayoutParams(params);

                                movieReviewList.addView(review);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
