package gj.udacity.project1.popularmovies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetail extends Fragment {

    private static final java.lang.String ARG = "Position";
    private MovieData currentMovie;
    private int optionNo;
    private ImageView moviePoster;
    private TextView movieTitle,moviePlot,movieDate;
    private RatingBar movieRating;

    public static MovieDetail newInstance(int position) {

        Bundle args = new Bundle();
        args.putInt(ARG,position);
        MovieDetail fragment = new MovieDetail();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        optionNo = getArguments().getInt(ARG);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_movie_detail,container,false);

        currentMovie = MovieList.movieInfo.get(optionNo);

        movieDate = (TextView) view.findViewById(R.id.movieDate);
        moviePlot = (TextView) view.findViewById(R.id.moviePlot);
        movieTitle = (TextView) view.findViewById(R.id.movieTitle);
        moviePoster = (ImageView) view.findViewById(R.id.moviePoster);
        movieRating = (RatingBar) view.findViewById(R.id.movieRating);

        moviePlot.setText(currentMovie.getOverview());
        movieTitle.setText(currentMovie.getMovieTitle());
        movieDate.setText(currentMovie.getReleaseDate());

        String url = "http://image.tmdb.org/t/p/w342";
        Picasso.with(getActivity())
                .load(url+currentMovie.getImage())
                .resize(340,510)
                .into(moviePoster);

        movieRating.setRating((float) currentMovie.getVoteAvg()/2);


        return view;
    }
}
