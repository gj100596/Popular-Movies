package gj.udacity.project1.popularmovies;

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

public class MainActivity extends AppCompatActivity {
    static Spinner spinner;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Setup spinner
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(new MyAdapter(
                toolbar.getContext(),
                new String[]{
                        getString(R.string.popular),
                        getString(R.string.rating),
                }));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.mainFragment, MovieList.newInstance(getString(R.string.popular)), getString(R.string.popular))
                            .commit();
                else
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.mainFragment, MovieList.newInstance(getString(R.string.rating)), getString(R.string.rating))
                            .commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        /*
        if(savedInstanceState==null) {
            spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // When the given dropdown item is selected, show its contents in the
                    // container view.
                    String  type;
                    if (position == 0)// && savedInstanceState == null)
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.mainFragment, MovieList.newInstance("Popular"), "Popular")
                                .commit();
                   /* else if (position == 0 && (type = savedInstanceState.getString("Type")).equals("Popular")) {
                        if (type.equals("Rating")) {
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.mainFragment, MovieList.newInstance("Popular"), "Rating")
                                    .commit();
                        } else {
                            spinner.setVisibility(View.INVISIBLE);
                            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        }
                    } else if (position == 0)
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.mainFragment, MovieList.newInstance("Popular"), "Rating")
                                .commit();
                    else if (position == 1 && savedInstanceState == null)
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.mainFragment, MovieList.newInstance("Rating"), "Rating")
                                .commit();
                    else if (position == 1 && !(type = savedInstanceState.getString("Type")).equals("Rating")) {
                        if (type.equals("Popular")) {
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.mainFragment, MovieList.newInstance("Rating"), "Rating")
                                    .commit();
                        } else {
                            spinner.setVisibility(View.INVISIBLE);
                            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        }
                    } else if (position == 1)
                    *
                    else
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.mainFragment, MovieList.newInstance("Rating"), "Rating")
                                .commit();


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
        else{

        }*/

    }

    /*
    @Override
    public void onSaveInstanceState(Bundle outState) {//}, PersistableBundle outPersistentState) {
        Fragment currentFragment;
        currentFragment = getSupportFragmentManager().findFragmentByTag("Popular");
        if (currentFragment != null && currentFragment.isVisible())
            outState.putInt("Type", 0);
        else {
            currentFragment = getSupportFragmentManager().findFragmentByTag("Rating");
            if (currentFragment != null && currentFragment.isVisible())
                outState.putInt("Type", 1);
            else
                outState.putInt("Type", 2);
        }
        super.onSaveInstanceState(outState);//, outPersistentState);

    }*/

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