package com.jhm69.farhad_fishingapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhm69.farhad_fishingapp.Model.Collection;
import com.jhm69.farhad_fishingapp.Model.Comment;
import com.jhm69.farhad_fishingapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.MyViewHolder> {

    List<Collection> commentList;
    Context context;

    public CollectionAdapter(List<Collection>comments, Context context){

        this.commentList = comments;
        this.context=context;

    }

    @Override
    public CollectionAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate( R.layout.sample_collection,viewGroup,false);
        return new CollectionAdapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(CollectionAdapter.MyViewHolder viewHolder, int i) {
        viewHolder.model.setText(commentList.get(i).getModel());
        viewHolder.type.setText(commentList.get(i).getType());
        viewHolder.subtitle.setText(commentList.get(i).getSubtitle());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView model, subtitle, type;
        public MyViewHolder(View itemView) {
            super(itemView);
            model =(TextView)itemView.findViewById(R.id.model);
            subtitle =(TextView)itemView.findViewById(R.id.subtitle);
            type = (TextView)itemView.findViewById(R.id.type );

        }
    }

}
