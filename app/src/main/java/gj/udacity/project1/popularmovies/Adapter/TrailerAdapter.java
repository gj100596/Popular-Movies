package gj.udacity.project1.popularmovies.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import gj.udacity.project1.popularmovies.R;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.viewHolder>{
    Context context;
    JSONArray trailersArray;
    private String movieID;

    public TrailerAdapter(Context activity, JSONArray results, long movieID) {
        this.context = activity;
        this.trailersArray = results;
        this.movieID = movieID+"";
    }

    class viewHolder extends RecyclerView.ViewHolder{
        ImageView trailerImage;

        public viewHolder(View itemView) {
            super(itemView);
            trailerImage = (ImageView) itemView.findViewById(R.id.imageInTrailer);
        }
    }
    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_list_layout,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(final viewHolder holder, int position) {
        try {
            JSONObject trailerDetail = new JSONObject(trailersArray.getString(position));
            holder.trailerImage.setTag(trailerDetail.getString("key"));

            Picasso.with(context)
                    .load("http://img.youtube.com/vi/"+movieID+"/mqdefault.jpg")
                    .placeholder(R.drawable.placeholder)
                    .into(holder.trailerImage);
            holder.trailerImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent youtube = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" +
                            holder.trailerImage.getTag().toString()));
                    context.startActivity(youtube);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return trailersArray.length();
    }
}
