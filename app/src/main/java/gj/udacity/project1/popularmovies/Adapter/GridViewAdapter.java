package gj.udacity.project1.popularmovies.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
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

    class ViewHolder {
        ImageView moviePoster;
    }

    public GridViewAdapter(Context context, ArrayList<MovieDataClass> movieList) {
        super(context, movieList.size());
        this.movieList = movieList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return movieList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.movie_grid_image, parent, false);
            holder = new ViewHolder();
            holder.moviePoster = (ImageView) convertView.findViewById(R.id.imageViewInGrid);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String url = "http://image.tmdb.org/t/p/w342";

        Picasso.with(context)
                .load(url + movieList.get(position).getImage())
                .placeholder(R.drawable.placeholder)
                .into(holder.moviePoster);

        return convertView;
    }
}

