package gj.udacity.project1.popularmovies.Fragments;

import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import gj.udacity.project1.popularmovies.DBPackage.DBContract;
import gj.udacity.project1.popularmovies.DBPackage.DBContract.MovieEntry;
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

    // these constants correspond to the projection defined above
    private static final int ID_PROJECTION_NO = 0;
    private static final int IMAGE_PROJECTION_NO = 1;
    private static final int TITLE_PROJECTION_NO = 2;
    private static final int OVERVIEW_PROJECTION_NO = 3;
    private static final int RELEASE_PROJECTION_NO = 4;
    private static final int VOTE_AVG_PROJECTION_NO = 5;


    private ImageView moviePoster;
    private TextView movieTitle, moviePlot, movieDate;
    private RatingBar movieRating;
    private ImageView favoriteButton;
    private LinearLayout trailerLayout,reviewLayout;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        movieDate = (TextView) view.findViewById(R.id.movieDate);
        moviePlot = (TextView) view.findViewById(R.id.moviePlot);
        movieTitle = (TextView) view.findViewById(R.id.movieTitle);
        moviePoster = (ImageView) view.findViewById(R.id.moviePoster);
        movieRating = (RatingBar) view.findViewById(R.id.movieRating);
        favoriteButton = (ImageView) view.findViewById(R.id.movieFavorite);
        trailerLayout = (LinearLayout) view.findViewById(R.id.trailerLayout);
        reviewLayout = (LinearLayout) view.findViewById(R.id.reviewLayout);

        trailerLayout.setVisibility(View.GONE);
        reviewLayout.setVisibility(View.GONE);
        moviePoster.setImageBitmap(BitmapFactory.decodeFile(getActivity().getFilesDir() + "/" + movieID + ".jpg"));

        favoriteButton.setImageResource(R.drawable.ic_star_black_36dp);
        /**
         * Again clicking the star means removing movie from favorite list.
         * So this method delete that movie from DB.
         * And go to favorite movie list again
         */
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getContentResolver().delete(MovieEntry.CONTENT_URI,
                        null, new String[]{""+movieID});
                favoriteButton.setImageResource(R.drawable.ic_star_border_black_36dp);
                getActivity().onBackPressed();
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
