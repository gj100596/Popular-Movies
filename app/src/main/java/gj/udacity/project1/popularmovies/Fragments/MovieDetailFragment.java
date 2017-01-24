package gj.udacity.project1.popularmovies.Fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import gj.udacity.project1.popularmovies.DBPackage.DBContract;
import gj.udacity.project1.popularmovies.Data.Connectivity;
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
    private boolean alreadyFavorite;
    private long MovieID;

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
            MovieID = currentMovie.getMovieId();

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
            final String url = "http://image.tmdb.org/t/p/w342";
            Picasso.with(getActivity())
                    .load(url + currentMovie.getImage())
                    .into(moviePoster);

            movieRating.setRating((float) currentMovie.getVoteAvg() / 2);

            loadTrailer();
            loadReviews();

            final Cursor cursor = getActivity().getContentResolver()
                    .query(DBContract.MovieEntry.buildLocationUri(MovieID),
                            null,null,null,null);
            if (cursor == null || cursor.getCount() == 0) {
                favoriteButton.setImageResource(R.drawable.ic_star_border_black_36dp);
                alreadyFavorite = false;
            }
            else {
                favoriteButton.setImageResource(R.drawable.ic_star_black_36dp);
                alreadyFavorite = true;
            }
            favoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(alreadyFavorite){
                        favoriteButton.setImageResource(R.drawable.ic_star_border_black_36dp);
                        getActivity().getContentResolver().delete(DBContract.MovieEntry.CONTENT_URI,
                                null, new String[]{""+ MovieID});
                        Toast.makeText(getActivity(),"Movie Removed From Favorites",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        ContentValues values = new ContentValues();
                        values.put(DBContract.MovieEntry._ID, MovieID);
                        values.put(DBContract.MovieEntry.COLUMN_IMAGE_URL, currentMovie.getImage());
                        values.put(DBContract.MovieEntry.COLUMN_MOVIE_TITLE, currentMovie.getMovieTitle());
                        values.put(DBContract.MovieEntry.COLUMN_OVERVIEW, currentMovie.getOverview());
                        values.put(DBContract.MovieEntry.COLUMN_VOTE_AVG, currentMovie.getVoteAvg());
                        values.put(DBContract.MovieEntry.COLUMN_RELEASE_DATE, currentMovie.getReleaseDate());

                        getActivity().getContentResolver().insert(DBContract.BASE_URI, values);

                        Picasso.with(getActivity())
                                .load(url + currentMovie.getImage())
                                .into(new Target() {
                                    @Override
                                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                        try {
                                            File savePoster =
                                                    new File(getActivity().getFilesDir() + "/" + MovieID + ".jpg");
                                            FileOutputStream outputStream = new FileOutputStream(savePoster);

                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                                            outputStream.flush();
                                            outputStream.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                    }

                                    @Override
                                    public void onBitmapFailed(Drawable errorDrawable) {

                                    }

                                    @Override
                                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                                    }
                                });

                        favoriteButton.setImageResource(R.drawable.ic_star_black_36dp);
                        Toast.makeText(getActivity(),"Movie Added As Favorites",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        return view;
    }

    private void loadTrailer() {
        String url = "http://api.themoviedb.org/3/movie/" + MovieID + "/videos?&api_key=" + FixedData.API;
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

                                trailerImage.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent youtube = new Intent(
                                                Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" +
                                                v.getTag().toString()));
                                        getActivity().startActivity(youtube);
                                    }
                                });
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

    private void loadReviews() {
        String url = "http://api.themoviedb.org/3/movie/" + MovieID + "11/reviews?&api_key=" + FixedData.API;
        JsonObjectRequest detail = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        JSONArray reviewsArray = null;
                        try {
                            reviewsArray = jsonObject.getJSONArray("results");
                            for (int position = 0; position < reviewsArray.length(); position++) {
                                JSONObject reviewDetail = new JSONObject(reviewsArray.getString(position));

                                LinearLayout.LayoutParams mainParams = new LinearLayout.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT);

                                TextView author = new TextView(movieReviewList.getContext());
                                author.setText(reviewDetail.getString("author"));
                                author.setLayoutParams(mainParams);
                                author.setTextSize(22);
                                author.setTextColor(Color.BLACK);
                                TextView comment = new TextView(movieReviewList.getContext());
                                comment.setText(reviewDetail.getString("content"));
                                comment.setLayoutParams(mainParams);
                                comment.setTextSize(12);

                                movieReviewList.addView(author);
                                movieReviewList.addView(comment);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        volleyError.getMessage();
                        String s  = new String(volleyError.networkResponse.data);
                        if(!Connectivity.isConnected(getActivity())){
                            Toast.makeText(getActivity(),"Please Connect To Internet",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getActivity(), "Error Occurred! " + volleyError.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(detail);
    }
}
