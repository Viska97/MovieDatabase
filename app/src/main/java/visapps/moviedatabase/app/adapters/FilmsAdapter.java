package visapps.moviedatabase.app.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


import com.squareup.picasso.Picasso;
import com.moviedatabase.app.R;
import visapps.moviedatabase.app.models.Film;

public class FilmsAdapter extends RecyclerView.Adapter<FilmsAdapter.ViewHolder>{

    public interface  FilmsAdapterCallback{
        void onClick(int id);
        void onRemove(int id);
    }

    private List<Film> items;
    private Context context;
    private FilmsAdapterCallback callback;


    public void setCallback(FilmsAdapterCallback callback) {
        this.callback = callback;
    }

    public FilmsAdapter(Context context){
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
    public FilmsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.film_item, null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FilmsAdapter.ViewHolder holder, int position) {
        Film film = items.get(position);
        holder.filmName.setText(film.getFilmName());
        holder.ageLimit.setText(String.valueOf(film.getAgeLimit()) + "+");
        Picasso.get().load(film.getPosterLink()).fit().centerCrop().into(holder.poster);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView poster;
        TextView filmName, ageLimit;

        View container;


        public ViewHolder(View itemView) {
            super(itemView);
            filmName = itemView.findViewById(R.id.filmName);
            ageLimit = itemView.findViewById(R.id.ageLimit);
            poster = itemView.findViewById(R.id.poster);
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

