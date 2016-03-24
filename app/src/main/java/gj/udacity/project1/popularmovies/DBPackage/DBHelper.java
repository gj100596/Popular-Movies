package gj.udacity.project1.popularmovies.DBPackage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final int VERSION = 1;
    public static final String DB_FILE_NAME = "movie.db";

    public DBHelper(Context context){
        super(context, DB_FILE_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "create table " + DBContract.MovieEntry.TABLE + "("+
                        DBContract.MovieEntry._ID+ " INTEGER," +
                        DBContract.MovieEntry.COLUMN_IMAGE_URL + " VARCCHAR2(50)," +
                        DBContract.MovieEntry.COLUMN_MOVIE_TITLE + " VARCCHAR2(50)," +
                        DBContract.MovieEntry.COLUMN_OVERVIEW + " VARCCHAR2(400)," +
                        DBContract.MovieEntry.COLUMN_VOTE_AVG + " REAL," +
                        DBContract.MovieEntry.COLUMN_RELEASE_DATE + " VARCHAR(20));";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + DBContract.MovieEntry.TABLE);
        onCreate(db);
    }
}
