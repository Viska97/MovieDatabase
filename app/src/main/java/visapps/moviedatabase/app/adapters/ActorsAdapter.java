package visapps.moviedatabase.app.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moviedatabase.app.R;
import visapps.moviedatabase.app.models.Role;

import java.util.ArrayList;
import java.util.List;

public class ActorsAdapter extends RecyclerView.Adapter<ActorsAdapter.ViewHolder>{



    private List<Role> items;
    private Context context;


    public ActorsAdapter(Context context){
        this.context = context;
        items = new ArrayList<>();
    }



    public void setItems(List<Role> items){
        this.items = items;
        notifyDataSetChanged();
    }

    public void clear(){
        items.clear();
        notifyDataSetChanged();
    }

    @Override
    public ActorsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.role_item, null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ActorsAdapter.ViewHolder holder, int position) {
        Role role = items.get(position);
        holder.Actor.setText(role.getActor());
        holder.Role.setText(role.getRole());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView Actor, Role;


        public ViewHolder(View itemView) {
            super(itemView);
            Actor = itemView.findViewById(R.id.Actor);
            Role = itemView.findViewById(R.id.Role);
        }



    }

}

