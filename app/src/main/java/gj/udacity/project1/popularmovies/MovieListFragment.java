package gj.udacity.project1.popularmovies;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/*
This fragment load list of movies either popular ones or highest rated ones.
This choice depend on variable "type", whose value I have received from Main Activity.
 */
public class MovieListFragment extends Fragment {

    private static final String ARG = "Type" ;
    private GridView container;
    static ArrayList<MovieDataClass> movieInfo;
    private String type;

    public static MovieListFragment newInstance(String type) {
        MovieListFragment fragment = new MovieListFragment();
        Bundle arg = new Bundle();
        arg.putString(ARG,type);
        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getString(ARG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup containerView, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movie_list,containerView,false);

        movieInfo = new ArrayList<>(0);
        //Set Home button false. This is needed when we come from detail movie to popular/highest rating movie fragment
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        MainActivity.spinner.setVisibility(View.VISIBLE);

        container = (GridView) view.findViewById(R.id.container);

        container.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Show Detail of Movie

                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                MainActivity.spinner.setVisibility(View.INVISIBLE);

                getFragmentManager().beginTransaction()
                        .replace(R.id.mainFragment, MovieDetailFragment.newInstance(position))
                        .addToBackStack(getString(R.string.info))
                        .commit();
            }
        });

        loadMovieData();

        return view;
    }

    private void loadMovieData() {
        final ProgressDialog loading = new ProgressDialog(getContext());
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.setMessage(getString(R.string.loading));

        String url;
        if(type.equalsIgnoreCase(getString(R.string.popular)))
            url = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key="+FixedData.API;
        else
            url = "http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key="+FixedData.API;
        JsonObjectRequest page = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        loading.cancel();
                        try {
                            JSONArray movies = jsonObject.getJSONArray("results");

                            //store the list of movie so that it can be used again
                            for(int i=0;i<movies.length();i++)
                                movieInfo.add(new MovieDataClass(movies.getJSONObject(i)));

                            container.setAdapter(new GridViewAdapter(getContext(),movieInfo));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        loading.cancel();
                        Toast.makeText(getActivity(), "Error Occurred! " + volleyError.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        );

        RequestQueue queue = Volley.newRequestQueue(getContext());
        loading.show();
        queue.add(page);
    }
}


