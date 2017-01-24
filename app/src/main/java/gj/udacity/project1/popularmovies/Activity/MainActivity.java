package gj.udacity.project1.popularmovies.Activity;

import android.content.Context;
import android.content.res.Resources.Theme;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.splunk.mint.Mint;


import gj.udacity.project1.popularmovies.Data.Connectivity;
import gj.udacity.project1.popularmovies.Fragments.FavoriteMovieListFragment;
import gj.udacity.project1.popularmovies.Fragments.MovieDetailFragment;
import gj.udacity.project1.popularmovies.Fragments.MovieListFragment;
import gj.udacity.project1.popularmovies.R;

/*
The Only Activity in whole App. It will inflate 3 type of fragments:
 1) Popular Fragment: Fragment showing popular movies
 2) Highest rating Fragment: Fragment showing highest rating movies
 3) Movie Detail Fragment: Fragment showing movie detail.
A spinner is used to go from one fragment to other
I have written comments at most point of code for explanation.
 */
public class MainActivity extends AppCompatActivity {
    public static Spinner spinner;
    public static boolean tabletDevice = false;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Mint.initAndStartSession(this.getApplication(), "47614951");

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (findViewById(R.id.detailFragment) != null) {
            tabletDevice = true;
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.detailFragment, MovieDetailFragment.newInstance(-1))
                    .commit();
        }

        // Setup spinner
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(new MyAdapter(
                toolbar.getContext(),
                new String[]{
                        getString(R.string.popular),
                        getString(R.string.rating),
                        getString(R.string.favorite)
                }));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.mainFragment,
                                        MovieListFragment.newInstance(getString(R.string.popular)),
                                        getString(R.string.popular))
                                .commit();
                        break;
                    case 1:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.mainFragment,
                                        MovieListFragment.newInstance(getString(R.string.rating)),
                                        getString(R.string.rating))
                                .commit();
                        break;
                    case 2:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.mainFragment,
                                        FavoriteMovieListFragment.newInstance(),
                                        getString(R.string.favorite))
                                .commit();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //If user is not connected to user send him/her to favorite page
        if(!Connectivity.isConnected(this)){
            Toast.makeText(MainActivity.this, "You are not Connected to Internet. Please Connect to see other content!",
                    Toast.LENGTH_SHORT).show();
            spinner.setSelection(2);
        }
    }

    /*
        This adapter is for spinner
        I din't wrote this whole Adapter, it was pre-written when I selected activity with spinner
        But I understood the code and even tried removing it, which result just change in color of drop-down menu
        Hence this Adapter is for theming the drop down of spinner.
         */
    private static class MyAdapter extends ArrayAdapter<String> implements ThemedSpinnerAdapter {
        private final ThemedSpinnerAdapter.Helper mDropDownHelper;

        public MyAdapter(Context context, String[] objects) {
            super(context, android.R.layout.simple_list_item_activated_1, objects);
            mDropDownHelper = new ThemedSpinnerAdapter.Helper(context);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                // Inflate the drop down using the helper's LayoutInflater
                LayoutInflater inflater = mDropDownHelper.getDropDownViewInflater();
                view = inflater.inflate(android.R.layout.simple_list_item_activated_1, parent, false);
            } else {
                view = convertView;
            }

            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(getItem(position));

            return view;
        }

        @Override
        public Theme getDropDownViewTheme() {
            return mDropDownHelper.getDropDownViewTheme();
        }

        @Override
        public void setDropDownViewTheme(Theme theme) {
            mDropDownHelper.setDropDownViewTheme(theme);
        }
    }
}