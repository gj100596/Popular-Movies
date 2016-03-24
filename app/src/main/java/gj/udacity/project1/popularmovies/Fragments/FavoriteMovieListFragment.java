package gj.udacity.project1.popularmovies.Fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import gj.udacity.project1.popularmovies.Activity.DetailActivity;
import gj.udacity.project1.popularmovies.Activity.MainActivity;
import gj.udacity.project1.popularmovies.DBPackage.DBContract;
import gj.udacity.project1.popularmovies.DBPackage.DBCursorAdapter;
import gj.udacity.project1.popularmovies.R;

/*
This fragment load list of favorite movies from database
 */
public class FavoriteMovieListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int CURSORLOADER_ID = 1;

    private GridView container;
    private DBCursorAdapter cursorAdapter;

    public static FavoriteMovieListFragment newInstance() {
        return new FavoriteMovieListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup containerView, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movie_list, containerView, false);

        container = (GridView) view.findViewById(R.id.container);

        container.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Show Detail of Movie

                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                Long movieId = cursor.getLong(cursor.getColumnIndex(DBContract.MovieEntry._ID));
                if(MainActivity.tabletDevice){
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.detailFragment,FavoriteMovieDetailFragment.newInstance(movieId))
                            .commit();
                }
                else {
                    Intent detail = new Intent(getActivity(), DetailActivity.class);
                    Bundle arg = new Bundle();
                    arg.putString("Type",getString(R.string.favorite));
                    arg.putLong("ID", movieId);
                    detail.putExtras(arg);
                    startActivity(detail);
                }
            }
        });
        
            loadSavedMovie();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(CURSORLOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    private void loadSavedMovie() {
        String sortOrder = DBContract.MovieEntry.COLUMN_RELEASE_DATE + " ASC";

        Cursor cur = getActivity().getContentResolver().query(DBContract.MovieEntry.CONTENT_URI,
                null/*new String[]{DBContract.MovieEntry.COLUMN_IMAGE_URL}*/, null, null, sortOrder);

         cursorAdapter = new DBCursorAdapter(getActivity(), cur, 0);

        container.setAdapter(cursorAdapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Sort order:  Ascending, by date.
        String sortOrder = DBContract.MovieEntry.COLUMN_RELEASE_DATE + " ASC";

        return new CursorLoader(getActivity(),
                DBContract.MovieEntry.CONTENT_URI,
                null,   //new String[]{DBContract.MovieEntry.COLUMN_IMAGE_URL},
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }
}


