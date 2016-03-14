package gj.udacity.project1.popularmovies.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import gj.udacity.project1.popularmovies.Data.FixedData;
import gj.udacity.project1.popularmovies.R;

public class Review extends Fragment {

    ListView review;
    String movieId;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "movieId";

    public static Review newInstance(String param1){
        Review fragment = new Review();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }
    public Review() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movieId = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_review, container, false);
        review = (ListView) view.findViewById(R.id.reviewList);

        loadData();
        return view;
    }

    private void loadData() {
        String url = "http://api.themoviedb.org/3/movie/"+movieId+"/reviews?&api_key="+ FixedData.API   ;
        JsonObjectRequest detail = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            JSONArray trailerKey = jsonObject.getJSONArray("results");
                            String reviewStrings[] = new String[trailerKey.length()];
                            for(int i=0;i<trailerKey.length();i++){
                                StringBuilder builder = new StringBuilder();
                                JSONObject obj = trailerKey.getJSONObject(i);
                                builder.append("Author: " + obj.getString("author")+ "\n\n");
                                builder.append(obj.getString("content"));
                                reviewStrings[i] = builder.toString();
                            }

                            review.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,reviewStrings));

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("sd","Sdsd");
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }
        );

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(detail);
    }

}
