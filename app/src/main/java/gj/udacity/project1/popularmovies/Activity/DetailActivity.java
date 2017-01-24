package gj.udacity.project1.popularmovies.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import gj.udacity.project1.popularmovies.Fragments.FavoriteMovieDetailFragment;
import gj.udacity.project1.popularmovies.Fragments.MovieDetailFragment;
import gj.udacity.project1.popularmovies.R;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String type = getIntent().getExtras().getString("Type");

        Button b = (Button) findViewById(R.id.crashbt);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a = "Ssa";
                Integer.parseInt(a);
            }
        });

        if(type.equals(getString(R.string.favorite))){
            long id = getIntent().getExtras().getLong("ID");

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.detailFragment, FavoriteMovieDetailFragment.newInstance(id))
                    .commit();
        }
        else {
            int position = getIntent().getExtras().getInt("Position");

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.detailFragment, MovieDetailFragment.newInstance(position))
                    .commit();
        }
    }
}
