package com.jhm69.farhad_fishingapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhm69.farhad_fishingapp.Activity.MyProfileActivity;
import com.jhm69.farhad_fishingapp.Model.Comment;
import com.jhm69.farhad_fishingapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

    private List<Comment> commentList;
    private Context context;

    public CommentAdapter(List<Comment>comments, Context context){

        this.commentList = comments;
        this.context=context;

    }

    @Override
    public CommentAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate( R.layout.comment_each,viewGroup,false);
        return new CommentAdapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(CommentAdapter.MyViewHolder viewHolder, int i) {
        Picasso.get().load( commentList.get( i ).getImg()).into( viewHolder.userIMG );
        viewHolder.Myname.setText(commentList.get(i).getName());
        viewHolder.MyDate.setText(commentList.get(i).getDate());
        viewHolder.MyBody.setText(commentList.get(i).getComment());
        final String uid = commentList.get(i).getUserId();
        viewHolder.Myname.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(v.getContext(), MyProfileActivity.class );
                in.putExtra("uid",uid );
                v.getContext().startActivity(in);
            }
        } );


           }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView Myname,MyBody,MyDate;
        ImageView userIMG;
        CardView next;
        public MyViewHolder(View itemView) {
            super(itemView);
            Myname =(TextView)itemView.findViewById(R.id.text_nameC);
            MyDate =(TextView)itemView.findViewById(R.id.dateC);
            MyBody = (TextView)itemView.findViewById(R.id.bodyTVC );
            userIMG = (ImageView) itemView.findViewById(R.id.pro_imageC);

        }
    }

}
