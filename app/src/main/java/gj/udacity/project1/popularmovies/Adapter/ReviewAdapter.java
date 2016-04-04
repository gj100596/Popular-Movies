package gj.udacity.project1.popularmovies.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import gj.udacity.project1.popularmovies.R;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.viewHolder>{
    Context context;
    JSONArray reviewArray;

    public ReviewAdapter(Context activity, JSONArray results) {
        this.context = activity;
        this.reviewArray = results;
    }

    class viewHolder extends RecyclerView.ViewHolder{
        TextView author,comment;

        public viewHolder(View itemView) {
            super(itemView);
            author = (TextView) itemView.findViewById(R.id.authorInReviews);
            comment = (TextView) itemView.findViewById(R.id.commentInReviews);
        }
    }
    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_list_layout,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(final viewHolder holder, int position) {
        try {
            JSONObject reviewDetail = new JSONObject(reviewArray.getString(position));
            holder.author.setText(reviewDetail.getString("author"));
            holder.comment.setText(reviewDetail.getString("context"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return reviewArray.length();
    }
}
