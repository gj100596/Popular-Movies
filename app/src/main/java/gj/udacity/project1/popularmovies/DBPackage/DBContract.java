package gj.udacity.project1.popularmovies.DBPackage;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Gaurav on 16-03-2016.
 */
public class DBContract {

    public static final String CONTENT_AUTHORITY = "gj.udacity.project1.popularmovies";

    public static final Uri BASE_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public  static final String PATH_MOVIE = "favorite_movie";

    public static class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static final String TABLE = "favorite_movie";
        public static final String COLUMN_IMAGE_URL = "image_url";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_MOVIE_TITLE = "movie_title";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_VOTE_AVG = "vote_avg";

        public static Uri buildLocationUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
