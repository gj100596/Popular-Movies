package gj.udacity.project1.popularmovies.DBPackage;

import android.provider.BaseColumns;

/**
 * Created by Gaurav on 16-03-2016.
 */
public class DBContract implements BaseColumns {

    public static final String TABLE = "FAVORITE_MOVIE";
    public static final String COLUMN_IMAGE_URL = "IMAGE_URL";
    public static final String COLUMN_OVERVIEW = "OVERVIEW";
    public static final String COLUMN_RELEASE_DATE = "RELEASE_DATE";
    public static final String COLUMN_MOVIE_TITLE= "MOVIE_TITLE";
    public static final String COLUMN_MOVIE_ID = "MOVIE_ID";
    public static final String COLUMN_VOTE_AVG = "VOTE_AVG";
}
