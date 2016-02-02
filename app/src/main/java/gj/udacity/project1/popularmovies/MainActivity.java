package gj.udacity.project1.popularmovies;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
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

public class MainActivity extends AppCompatActivity {

    GridView container;
    ArrayList<MovieData> movieInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movieInfo = new ArrayList<>(0);

        container = (GridView) findViewById(R.id.container);

        loadMovieData();
    }

    private void loadMovieData() {
        final ProgressDialog loading = new ProgressDialog(this);
        loading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loading.setMessage("Loading Movie Data");

        String url = "http://api.themoviedb.org/3/discover/tv?sort_by=popularity.desc&api_key="+FixedData.API;
        JsonObjectRequest page = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        loading.cancel();
                        try {
                            JSONArray movies = jsonObject.getJSONArray("results");

                            for(int i=0;i<movies.length();i++)
                                movieInfo.add(new MovieData(movies.getJSONObject(i)));

                            container.setAdapter(new GridViewAdapter(getApplicationContext(),movieInfo));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        loading.cancel();
                        Toast.makeText(MainActivity.this, "Error Occurred! " + volleyError.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        loading.show();
        queue.add(page);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R);
        return super.onCreateOptionsMenu(menu);
    }
}


