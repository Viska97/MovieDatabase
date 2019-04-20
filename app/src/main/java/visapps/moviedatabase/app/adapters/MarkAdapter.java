package visapps.moviedatabase.app.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moviedatabase.app.R;
import visapps.moviedatabase.app.models.Mark;

import java.util.ArrayList;
import java.util.List;

public class MarkAdapter extends RecyclerView.Adapter<MarkAdapter.ViewHolder>{



    private List<Mark> items;
    private Context context;


    public MarkAdapter(Context context){
        this.context = context;
        items = new ArrayList<>();
    }



    public void setItems(List<Mark> items){
        this.items = items;
        notifyDataSetChanged();
    }

    public void clear(){
        items.clear();
        notifyDataSetChanged();
    }

    @Override
    public MarkAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mark_item, null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MarkAdapter.ViewHolder holder, int position) {
        Mark mark = items.get(position);
        holder.NickName.setText(mark.getNickName());
        String date = mark.getDateAndTime().substring(0,10);
        String time = mark.getDateAndTime().substring(11,19);
        holder.DateAndTime.setText(date + " " + time);
        int rating = mark.getRating();
        if(rating<5){
            holder.Rating.setTextColor(Color.RED);
        }
        if(rating >= 5 && rating<7){
            holder.Rating.setTextColor(Color.YELLOW);
        }
        if(rating >= 7){
            holder.Rating.setTextColor(Color.GREEN);
        }
        holder.Rating.setText(String.valueOf(rating));
        if(mark.getComment() == null){
            holder.Comment.setVisibility(View.GONE);
        }
        else{
            holder.Comment.setVisibility(View.VISIBLE);
            holder.Comment.setText(mark.getComment());
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView NickName,DateAndTime,Rating,Comment;



        public ViewHolder(View itemView) {
            super(itemView);
            NickName = itemView.findViewById(R.id.NickName);
            DateAndTime = itemView.findViewById(R.id.DateAndTime);
            Rating = itemView.findViewById(R.id.Rating);
            Comment = itemView.findViewById(R.id.Comment);
        }



    }

}
