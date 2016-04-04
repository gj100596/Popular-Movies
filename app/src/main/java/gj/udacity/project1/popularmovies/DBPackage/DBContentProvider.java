package gj.udacity.project1.popularmovies.DBPackage;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

public class DBContentProvider extends ContentProvider {

    private static final int MOVIE = 1;
    private static final int MOVIE_DETAIL = 2;

    private static final UriMatcher sUriMatcher = createUriMatcher();
    private DBHelper mOpenHelper;


    static UriMatcher createUriMatcher(){
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        String authority = DBContract.CONTENT_AUTHORITY;
        matcher.addURI(authority , DBContract.PATH_MOVIE ,MOVIE);
        matcher.addURI(authority , DBContract.PATH_MOVIE + "/#" ,MOVIE_DETAIL);

        return  matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        int match = sUriMatcher.match(uri);

        switch (match){
            case MOVIE:
                return DBContract.MovieEntry.CONTENT_TYPE;
            case MOVIE_DETAIL:
                return  DBContract.MovieEntry.CONTENT_ITEM_TYPE;
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                return getAllFavoriteMovies(projection,sortOrder);
            case MOVIE_DETAIL:
                return getFavoriteMovieDetail(uri,projection,sortOrder);
        }
        return null;
    }

    private Cursor getFavoriteMovieDetail(Uri uri, String[] projection, String sortOrder) {
        String MovieIDSelectionString = DBContract.MovieEntry.TABLE+
                "." + DBContract.MovieEntry._ID + "=?";

        return mOpenHelper.getReadableDatabase().query(
                DBContract.MovieEntry.TABLE,
                projection,
                MovieIDSelectionString,
                new String[]{uri.getPathSegments().get(1)},
                null,
                null,
                sortOrder
        );
    }

    private Cursor getAllFavoriteMovies(String[] projection, String sortOrder) {

        return mOpenHelper.getReadableDatabase().query(
                DBContract.MovieEntry.TABLE,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        long id = mOpenHelper.getWritableDatabase().insert(DBContract.MovieEntry.TABLE,null,values);
        getContext().getContentResolver().notifyChange(uri, null);
        return DBContract.MovieEntry.buildLocationUri(id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int rowDeleted  = mOpenHelper.getWritableDatabase().delete(DBContract.MovieEntry.TABLE,selection,selectionArgs);
        if(rowDeleted != 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return rowDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int rowsUpdated = mOpenHelper.getWritableDatabase().update(DBContract.MovieEntry.TABLE,values,selection,selectionArgs);

        if (rowsUpdated != 0)
            getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
