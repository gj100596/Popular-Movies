package gj.udacity.project1.popularmovies.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

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

        int position = getIntent().getExtras().getInt("Position");

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.detailFragment, MovieDetailFragment.newInstance(position))
                .commit();
    }

}
