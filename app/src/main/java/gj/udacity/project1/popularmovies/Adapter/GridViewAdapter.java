package gj.udacity.project1.popularmovies.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import gj.udacity.project1.popularmovies.Data.MovieDataClass;
import gj.udacity.project1.popularmovies.R;

/*
It just inflate an image view to show movie poster
 */
public class GridViewAdapter extends ArrayAdapter {

    private ArrayList<MovieDataClass> movieList;
    private Context context;

    public GridViewAdapter(Context context, ArrayList<MovieDataClass> movieList) {
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
                .placeholder(R.drawable.placeholder)
                .into(imageView);

        return imageView;
    }
}

