package lucas.com.br.popularmovies;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by lucas on 24/03/17.
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public static final String MOVIES_BASE_URL = "http://image.tmdb.org/t/p/w185//";
    private List<Movie> movies;

    public ImageAdapter(Context c, List<Movie> movies) {
        mContext = c;
        this.movies = movies;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Object getItem(int position) {
        return movies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if(convertView == null) {
            imageView = new ImageView(mContext);
            //imageView.setLayoutParams(new GridView.LayoutParams(85,85));
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(0,0,0,0);
        } else {
            imageView = (ImageView)convertView;
        }
        Log.v("MoviesAdapter",MOVIES_BASE_URL+movies.get(position).getPosterPath());
        Picasso.with(mContext).load(MOVIES_BASE_URL+movies.get(position).getPosterPath()).into(imageView);
        return imageView;
    }

}
