package CodePath.com;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.ArrayList;

import CodePath.com.models.Movie;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.viewHolder> {
    //liste of movie
    ArrayList<Movie> movies;
    //initialiser avec liste
    Context context;
    int orientation;


    public MovieAdapter(Context context, ArrayList<Movie> movieList) {
        this.movies = movieList;
        this.context = context;
        orientation = this.context.getResources().getConfiguration().orientation;

    }


    public ArrayList<Movie> getMovies() {
        return movies;
    }

    @NonNull

    //create and inflates a new view

    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //get the context and create the inflater
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        //create the view movie the item movie layout
        View movieView = inflater.inflate(R.layout.item_movie, parent, false);
        //return a viewHolder
        return new viewHolder(movieView);
    }

    //brins an inflated a new item
    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        //get the movie data of the specified position
        Movie movie = movies.get(position);
        //populate the view with the movie App
        holder.tvTitle.setText(movie.getTitle());
        holder.tvOverview.setText(movie.getOverview());
        //TODO set image with the glide
        Log.d("IMAGE", movie.getPosterPath());




//        String size = "w342";

        if (orientation == Configuration.ORIENTATION_LANDSCAPE){
            Glide.with(context)
                    .load("https://image.tmdb.org/t/p/w342" + movie.getBackdropPath())
                    .placeholder(R.drawable.flicks_movie_placeholder).transform(new RoundedCorners(20))
                    .into(holder.ivPosterImage);
        }else{
            Glide.with(context)
                    .load("https://image.tmdb.org/t/p/w342" + movie.getPosterPath())
                    .placeholder(R.drawable.flicks_movie_placeholder).transform(new RoundedCorners(20))
                    .into(holder.ivPosterImage);
        }





    }

    //retumns the total number of item in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    //create a viewHolder as a static class inner
    public static class viewHolder extends RecyclerView.ViewHolder {
        ImageView ivPosterImage;
        TextView tvTitle;
        TextView tvOverview;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            ivPosterImage = (ImageView) itemView.findViewById(R.id.ivPosterImage);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvOverview = (TextView) itemView.findViewById(R.id.tvOverview);

        }
    }
}
