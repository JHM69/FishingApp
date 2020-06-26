package com.jhm69.farhad_fishingapp.Adapters;


import android.content.*;

import androidx.annotation.NonNull;

import android.view.*;
import android.widget.*;

import com.jhm69.farhad_fishingapp.Model.Video;
import com.jhm69.farhad_fishingapp.Activity.Video_Activity;
import com.squareup.picasso.*;

import java.util.*;

import com.jhm69.farhad_fishingapp.R;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import static com.jhm69.farhad_fishingapp.Adapters.UserPostAdapter.getDateFromTimeStamp;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.UsersAdapterVh> implements Filterable {

    private List<Video> videos;
    private List<Video> getUserModelListFiltered;
    private Context context;

    public VideoAdapter(List<Video> userModelList, Context context) {
        this.videos = userModelList;
        this.getUserModelListFiltered = userModelList;
        this.context =context;
    }

    @NonNull
    @Override
    public UsersAdapterVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new UsersAdapterVh(LayoutInflater.from(parent.getContext()).inflate( R.layout.video_sample,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final UsersAdapterVh holder, int position) {

        final Video video = videos.get(position);
        Picasso.get().load( video.getImage() ).into( holder.image );
        //holder.image.setImageBitmap(decodeImage( myPost.getImage() ) );
        holder.TitleTV.setText(video.getTitle());
        holder.dateTV.setText(getDateFromTimeStamp( video.getDate()) );
        holder.commentCount.setText("0" );
        final Video video1 = new Video(video.getId(),video.getTitle(),video.getDate(),video.getText(),video.getImage(), video.getLink(),video.getCategory());
        holder.fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), Video_Activity.class);
                i.putExtra("37",  video1 );
                v.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videos.size();
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

                    List<Video> resultData = new ArrayList<>();

                    for(Video userModel: getUserModelListFiltered){
                        if(userModel.getTitle().toLowerCase().contains(searchChr) || userModel.getText().toLowerCase().contains(searchChr)){
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

                videos = (List<Video>) filterResults.values;
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