package gj.udacity.project1.popularmovies.DBPackage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Gaurav on 16-03-2016.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final int VERSION = 1;
    public static final String DB_FILE_NAME = "movie.db";

    public DBHelper(Context context){
        super(context, DB_FILE_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "create table " + DBContract.TABLE + "("+
                        DBContract._ID + "VARCHAR2(50)" +
                        DBContract.COLUMN_IMAGE_URL + "VARCCHAR2(50)" +
                        DBContract.COLUMN_MOVIE_TITLE + "VARCCHAR2(50)" +
                        DBContract.COLUMN_OVERVIEW + "VARCCHAR2(400)" +
                        DBContract.COLUMN_VOTE_AVG + "REAL" +
                        DBContract.COLUMN_RELEASE_DATE + "VARCHAR(20));";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + DBContract.TABLE);
        onCreate(db);
    }
}
