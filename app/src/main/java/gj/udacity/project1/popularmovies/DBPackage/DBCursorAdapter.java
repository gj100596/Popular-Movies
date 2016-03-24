package gj.udacity.project1.popularmovies.DBPackage;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import gj.udacity.project1.popularmovies.R;

public class DBCursorAdapter extends CursorAdapter {
    String url = "http://image.tmdb.org/t/p/w342";

    public DBCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return  new ImageView(context);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        int ImageColumnIndex = cursor.getColumnIndex(DBContract.MovieEntry.COLUMN_IMAGE_URL);

        Picasso.with(context)
                .load(url+cursor.getString(ImageColumnIndex))
                .placeholder(R.drawable.placeholder)
                .into((ImageView) view);

    }
}
