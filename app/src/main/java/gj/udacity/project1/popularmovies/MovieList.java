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

public class MovieList extends Fragment {

    private static final String ARG = "Type" ;
    private GridView container;
    static ArrayList<MovieData> movieInfo;
    private String type;

    public static MovieList newInstance(String type) {
        MovieList fragment = new MovieList();
        Bundle arg = new Bundle();
        arg.putString(ARG,type);
        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getString(ARG);
        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup containerView, Bundle savedInstanceState) {

        if(savedInstanceState == null){
        View view = inflater.inflate(R.layout.fragment_movie_list,containerView,false);

        movieInfo = new ArrayList<>(0);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        MainActivity.spinner.setVisibility(View.VISIBLE);

        container = (GridView) view.findViewById(R.id.container);

        container.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                MainActivity.spinner.setVisibility(View.INVISIBLE);

                getFragmentManager().beginTransaction()
                        .replace(R.id.mainFragment,MovieDetail.newInstance(position))
                        .addToBackStack("Some")
                        .commit();
            }
        });

        loadMovieData();

        return view;}
        return null;
    }

    private void loadMovieData() {
        final ProgressDialog loading = new ProgressDialog(getContext());
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.setMessage("Loading Movie Data");

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

                            for(int i=0;i<movies.length();i++)
                                movieInfo.add(new MovieData(movies.getJSONObject(i)));

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


