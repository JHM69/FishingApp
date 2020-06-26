package com.jhm69.farhad_fishingapp.Adapters;


import android.content.*;

import androidx.annotation.NonNull;

import android.view.*;
import android.widget.*;

import com.jhm69.farhad_fishingapp.Model.MyPost;
import com.jhm69.farhad_fishingapp.Activity.Post_deteails;
import com.squareup.picasso.*;
import java.util.*;

import com.jhm69.farhad_fishingapp.R;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import static com.jhm69.farhad_fishingapp.Adapters.UserPostAdapter.getDateFromTimeStamp;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.UsersAdapterVh> implements Filterable {

    private List<MyPost> anglerList;
    private List<MyPost> getUserModelListFiltered;
    private Context context;

    public PostAdapter(List<MyPost> userModelList, Context context) {
        this.anglerList = userModelList;
        this.getUserModelListFiltered = userModelList;
        this.context =context;
    }

    @NonNull
    @Override
    public UsersAdapterVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        return new UsersAdapterVh(LayoutInflater.from(parent.getContext()).inflate( R.layout.frist_item_of_post,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final UsersAdapterVh holder, int position) {

        final MyPost myPost = anglerList.get(position);

        Picasso.get().load( myPost.getImage() ).into( holder.image );
        //holder.image.setImageBitmap(decodeImage( myPost.getImage() ) );
        holder.TitleTV.setText(myPost.getTitle());
        holder.dateTV.setText(getDateFromTimeStamp( myPost.getDate() ));
        holder.commentCount.setText(myPost.getCategory() );
        final MyPost myPost1 = new MyPost(myPost.getId(),myPost.getTitle(),myPost.getDate(),myPost.getText(),myPost.getImage(),myPost.getCategory());
        holder.fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), Post_deteails.class);
                i.putExtra("127", myPost1);
                v.getContext().startActivity(i);
            }
        });
    }
    @Override
    public int getItemCount() {
        return anglerList.size();
    }

    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();

                if(charSequence == null | charSequence.length() == 0){
                    filterResults.count = getUserModelListFiltered.size();
                    filterResults.values = getUserModelListFiltered;

                }else{
                    String searchChr = charSequence.toString().toLowerCase();

                    List<MyPost> resultData = new ArrayList<>();

                    for(MyPost userModel: getUserModelListFiltered){
                        if(userModel.getTitle().toLowerCase().contains(searchChr) || userModel.getCategory().toLowerCase().contains(searchChr) || userModel.getText().toLowerCase().contains(searchChr)){
                            resultData.add(userModel);
                        }
                    }
                    filterResults.count = resultData.size();
                    filterResults.values = resultData;

                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                anglerList = (List<MyPost>) filterResults.values;
                notifyDataSetChanged();

            }
        };
        return filter;
    }

    public class UsersAdapterVh extends RecyclerView.ViewHolder {

        TextView TitleTV;
        TextView dateTV;
        TextView commentCount;
        ImageView image;
        CardView fb;

        public UsersAdapterVh(@NonNull View itemView) {
            super(itemView);
            TitleTV = itemView.findViewById(R.id.postTitleTV);
            dateTV = itemView.findViewById(R.id.dateTV);
            commentCount = itemView.findViewById(R.id.commentCountTV);
            image =  itemView.findViewById(R.id.postImg);
            fb = itemView.findViewById(R.id.Nex);

        }
    }
                }