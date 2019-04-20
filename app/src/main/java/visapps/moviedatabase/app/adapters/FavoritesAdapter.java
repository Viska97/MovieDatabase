package visapps.moviedatabase.app.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.moviedatabase.app.R;
import visapps.moviedatabase.app.models.Film;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Visek on 18.03.2018.
 */

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder>{



    private List<Film> items;
    private Context context;
    private FilmsAdapter.FilmsAdapterCallback callback;


    public void setCallback(FilmsAdapter.FilmsAdapterCallback callback) {
        this.callback = callback;
    }

    public FavoritesAdapter(Context context){
        this.context = context;
        items = new ArrayList<>();
    }



    public void setItems(List<Film> items){
        this.items = items;
        notifyDataSetChanged();
    }

    public void clear(){
        items.clear();
        notifyDataSetChanged();
    }

    @Override
    public FavoritesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.favorite_item, null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoritesAdapter.ViewHolder holder, int position) {
        Film film = items.get(position);
        holder.FilmName.setText(film.getFilmName());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView FilmName;
        Button removebutton;

        View container;


        public ViewHolder(View itemView) {
            super(itemView);
            FilmName = itemView.findViewById(R.id.FilmName);
            removebutton = itemView.findViewById(R.id.removebutton);
            removebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callback.onRemove(items.get(getAdapterPosition()).getFilmId());
                }
            });
            container = itemView.findViewById(R.id.filmcontainer);
            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callback.onClick(items.get(getAdapterPosition()).getFilmId());
                }
            });
        }



    }

}
