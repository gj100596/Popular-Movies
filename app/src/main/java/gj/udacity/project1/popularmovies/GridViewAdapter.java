package gj.udacity.project1.popularmovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GridViewAdapter extends ArrayAdapter {

    private ArrayList<MovieData> movieList;
    private Context context;

    public GridViewAdapter(Context context, ArrayList<MovieData> movieList) {
        super(context,movieList.size());
        this.movieList = movieList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return movieList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String url = "http://image.tmdb.org/t/p/w342";
        ImageView imageView = new ImageView(context);

        Picasso.with(context)
                .load(url+movieList.get(position).getImage())
                .into(imageView);

        return imageView;
    }
}

