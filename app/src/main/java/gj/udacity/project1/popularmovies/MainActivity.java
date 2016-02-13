package gj.udacity.project1.popularmovies;

import android.content.Context;
import android.content.res.Resources.Theme;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
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

                //Start Popular Fragment when simple 0th position is click(No rotation things)
                if (position == 0 && savedInstanceState == null)
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.mainFragment, MovieList.newInstance(getString(R.string.popular)))
                            .commit();
                //Once Device is rotated then saveInsatnceState!=null
                //At that time when 0th option is selected this method will be called
                else if(position == 0){
                    //Check which fragment was there before rotation
                    int lastSpinnerOption = savedInstanceState.getInt("Position");

                    //If Highest Rating was there that means user now want Popular movie Fragment, so start it
                    if(lastSpinnerOption == 1){
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.mainFragment, MovieList.newInstance(getString(R.string.popular)))
                                .commit();
                    }
                    //If rotation was in Detail fragment, do nothing stay there only
                    else if(lastSpinnerOption == -1){
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        spinner.setVisibility(View.INVISIBLE);
                    }
                    //And if 0th is click when last Fragment was
                }
                else if(position == 1 && savedInstanceState == null)
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.mainFragment, MovieList.newInstance(getString(R.string.rating)))
                            .commit();
                else if(position == 1){
                    int lastSpinnerOption = savedInstanceState.getInt("Position");
                    if(lastSpinnerOption == 0){
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.mainFragment, MovieList.newInstance(getString(R.string.rating)))
                                .commit();
                    }
                    else if(lastSpinnerOption == -1){
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        spinner.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        if(spinner.getVisibility() == View.INVISIBLE)
            outState.putInt("Position",-1);
        else
            outState.putInt("Position",spinner.getSelectedItemPosition());
        super.onSaveInstanceState(outState);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

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