package gj.udacity.project1.popularmovies.Fragments;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import gj.udacity.project1.popularmovies.DBPackage.DBContract;
import gj.udacity.project1.popularmovies.DBPackage.DBContract.MovieEntry;
import gj.udacity.project1.popularmovies.Data.FixedData;
import gj.udacity.project1.popularmovies.R;

/*
This fragment show detail of selected movie.
 */
public class FavoriteMovieDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String ARG = "ID";
    private static final int CURSORLOADER_ID = 2;

    private long movieID;

    private static final String[] PROJECTION_COLUMNS = {
            MovieEntry.TABLE + "." + MovieEntry._ID,
            MovieEntry.COLUMN_IMAGE_URL,
            MovieEntry.COLUMN_MOVIE_TITLE,
            MovieEntry.COLUMN_OVERVIEW,
            MovieEntry.COLUMN_RELEASE_DATE,
            MovieEntry.COLUMN_VOTE_AVG
    };

    // these constants correspond to the projection defined above, and must change if the
    // projection changes
    private static final int ID_PROJECTION_NO = 0;
    private static final int IMAGE_PROJECTION_NO = 1;
    private static final int TITLE_PROJECTION_NO = 2;
    private static final int OVERVIEW_PROJECTION_NO = 3;
    private static final int RELEASE_PROJECTION_NO = 4;
    private static final int VOTE_AVG_PROJECTION_NO = 5;


    private ImageView moviePoster;
    private TextView movieTitle, moviePlot, movieDate, movieTrailer;
    private RatingBar movieRating;
    private ImageView reviewButton, favoriteButton;

    public static FavoriteMovieDetailFragment newInstance(long id) {

        Bundle args = new Bundle();
        args.putLong(ARG, id);
        FavoriteMovieDetailFragment fragment = new FavoriteMovieDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movieID = getArguments().getLong(ARG);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        movieDate = (TextView) view.findViewById(R.id.movieDate);
        moviePlot = (TextView) view.findViewById(R.id.moviePlot);
        movieTitle = (TextView) view.findViewById(R.id.movieTitle);
        moviePoster = (ImageView) view.findViewById(R.id.moviePoster);
        movieRating = (RatingBar) view.findViewById(R.id.movieRating);
        //movieTrailer = (TextView) view.findViewById(R.id.movieTrailer);
        //reviewButton = (Button) view.findViewById(R.id.reviewButton);
        favoriteButton = (ImageView) view.findViewById(R.id.movieFavorite);

        movieTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                youtubeIntent();
            }
        });

        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.mainFragment,
                        Review.newInstance("" + movieID)).commit();
            }
        });

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getContentResolver().delete(DBContract.MovieEntry.buildLocationUri(movieID), null, null);
                //insert();
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(CURSORLOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);
        MenuItem menuItem = menu.findItem(R.id.share);

        ShareActionProvider mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        mShareActionProvider.setShareIntent(createShareForecastIntent());
    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "http://www.youtube.com/watch?v=somekye");
        return shareIntent;
    }

    private void youtubeIntent() {

        String url = "http://api.themoviedb.org/3/movie/" + movieID + "/videos?&api_key=" + FixedData.API;
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

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(),
                DBContract.MovieEntry.buildLocationUri(movieID),
                PROJECTION_COLUMNS,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        moviePlot.setText(data.getString(OVERVIEW_PROJECTION_NO));
        movieTitle.setText(data.getString(TITLE_PROJECTION_NO));
        movieDate.setText(data.getString(RELEASE_PROJECTION_NO));

        /*Here a new request is being sent to fetch image but actually it won't bring whole image
        back because volley would have cached the imaged previously when it called it first to show list.
         */
        String url = "http://image.tmdb.org/t/p/w342";
        Picasso.with(getActivity())
                .load(url + data.getString(IMAGE_PROJECTION_NO))
                .into(moviePoster);

        movieRating.setRating((float) data.getDouble(VOTE_AVG_PROJECTION_NO) / 2);
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}
