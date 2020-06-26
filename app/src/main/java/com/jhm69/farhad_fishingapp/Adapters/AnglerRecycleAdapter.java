package com.jhm69.farhad_fishingapp.Adapters;

import com.jhm69.farhad_fishingapp.Model.Angler;
import com.jhm69.farhad_fishingapp.Activity.MyProfileActivity;
import com.squareup.picasso.*;
import java.util.*;

import android.content.*;
import android.net.*;
import android.view.*;
import android.widget.*;

import com.jhm69.farhad_fishingapp.R;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class AnglerRecycleAdapter extends RecyclerView.Adapter<AnglerRecycleAdapter.MyViewHolder> implements  Filterable {

    private List<Angler>anglerList;
    private Context context;
    private List<Angler> getUserModelListFiltered;

    public AnglerRecycleAdapter(List<Angler>anglers, Context context){
        //getUserModelListFiltered =  new ArrayList<>();
        this.anglerList = anglers;
        this.context=context;
        this.getUserModelListFiltered=anglers;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate( R.layout.sample_anglers,viewGroup,false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int i) {
        //anglerList =  new ArrayList<>();
        final Angler angler = anglerList.get(i);
        Picasso.get().load( angler.getImg()).into(viewHolder.image);
        viewHolder.nameTV.setText(angler.getName());
        viewHolder.addressTV.setText(angler.getHome() );

        //final FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
        viewHolder.fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent sharingIntent = new Intent(Intent.ACTION_VIEW);
                    sharingIntent.setType("URL");
                    sharingIntent.setData(Uri.parse("fb://facewebmodal/f?href="+angler.getFb()));
                    sharingIntent.setPackage("com.facebook.katana");
                    v.getContext().startActivity(sharingIntent);
                } catch (ActivityNotFoundException e) {
                    Uri uri = Uri.parse(angler.getFb());
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    v.getContext().startActivity(goToMarket);
                }

            }
        });
        viewHolder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+angler.getNumber()));
                v.getContext().startActivity(intent);
            }
        });
        viewHolder.all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(v.getContext(), MyProfileActivity.class );
                in.putExtra("uid",angler.getUid() );
                v.getContext().startActivity(in);

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

                    List<Angler> resultData = new ArrayList<>();

                    for(Angler userModel: getUserModelListFiltered){
                        if(userModel.getName().toLowerCase().contains(searchChr)){
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

                anglerList = (List<Angler>) filterResults.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView addressTV, nameTV;
        ImageView image;
        CardView fb;
        CardView call;
        ConstraintLayout all;

        public MyViewHolder(View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.name);
            addressTV = itemView.findViewById(R.id.address);
            image =  itemView.findViewById(R.id.img);
            fb = itemView.findViewById(R.id.fb);
            call = itemView.findViewById(R.id.call);
            all = itemView.findViewById(R.id.naa);



        }
    }

}
