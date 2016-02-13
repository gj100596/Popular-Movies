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

/*
This fragment show detail of selected movie.
 */
public class MovieDetailFragment extends Fragment{

    private static final java.lang.String ARG = "Position";
    private MovieDataClass currentMovie;
    private int optionNo;
    private ImageView moviePoster;
    private TextView movieTitle, moviePlot, movieDate;
    private RatingBar movieRating;

    public static MovieDetailFragment newInstance(int position) {

        Bundle args = new Bundle();
        args.putInt(ARG, position);
        MovieDetailFragment fragment = new MovieDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        optionNo = getArguments().getInt(ARG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        currentMovie = MovieListFragment.movieInfo.get(optionNo);

        movieDate = (TextView) view.findViewById(R.id.movieDate);
        moviePlot = (TextView) view.findViewById(R.id.moviePlot);
        movieTitle = (TextView) view.findViewById(R.id.movieTitle);
        moviePoster = (ImageView) view.findViewById(R.id.moviePoster);
        movieRating = (RatingBar) view.findViewById(R.id.movieRating);

        moviePlot.setText(currentMovie.getOverview());
        movieTitle.setText(currentMovie.getMovieTitle());
        movieDate.setText(currentMovie.getReleaseDate());

        /*Here a new request is being sent to fetch image but actually it won't bring whole image
        back because volley would have cached the imaged previously when it called it first to show list.
         */
        String url = "http://image.tmdb.org/t/p/w342";
        Picasso.with(getActivity())
                .load(url + currentMovie.getImage())
                .into(moviePoster);

        movieRating.setRating((float) currentMovie.getVoteAvg() / 2);

        return view;
    }

}
