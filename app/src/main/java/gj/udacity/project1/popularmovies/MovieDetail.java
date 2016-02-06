package gj.udacity.project1.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetail extends AppCompatActivity {

    private MovieData currentMovie;
    private int optionNo;
    private ImageView moviePoster;
    private TextView movieTitle,moviePlot,movieDate;
    private RatingBar movieRating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle param = getIntent().getExtras();
        optionNo = param.getInt("Position");
        currentMovie = MainActivity.movieInfo.get(optionNo);

        movieDate = (TextView) findViewById(R.id.movieDate);
        moviePlot = (TextView) findViewById(R.id.moviePlot);
        movieTitle = (TextView) findViewById(R.id.movieTitle);
        moviePoster = (ImageView) findViewById(R.id.moviePoster);
        movieRating = (RatingBar) findViewById(R.id.movieRating);

        moviePlot.setText(currentMovie.getOverview());
        movieTitle.setText(currentMovie.getMovieTitle());
        movieDate.setText(currentMovie.getReleaseDate());

        String url = "http://image.tmdb.org/t/p/w342";
        Picasso.with(this)
                .load(url+currentMovie.getImage())
                .into(moviePoster);

        movieRating.setRating((float) currentMovie.getVoteAvg());


    }
}
