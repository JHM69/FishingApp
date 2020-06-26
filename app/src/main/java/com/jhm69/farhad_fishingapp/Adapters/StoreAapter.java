package com.jhm69.farhad_fishingapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhm69.farhad_fishingapp.Activity.MyProfileActivity;
import com.jhm69.farhad_fishingapp.Model.Angler;
import com.jhm69.farhad_fishingapp.Model.Store;
import com.jhm69.farhad_fishingapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class StoreAapter extends RecyclerView.Adapter<StoreAapter.MyViewHolder> implements Filterable {

    private List<Store> storeList;
    private Context context;
    private List<Store> getUserModelListFiltered;

    public StoreAapter(List<Store>anglers, Context context){
        //getUserModelListFiltered =  new ArrayList<>();
        this.storeList = anglers;
        this.context=context;
        this.getUserModelListFiltered=anglers;

    }

    @Override
    public StoreAapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate( R.layout.sample_store,viewGroup,false);
        return new StoreAapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(StoreAapter.MyViewHolder viewHolder, int i) {
        final Store store = storeList.get(i);
        Picasso.get().load( store.getShopLogo()).into(viewHolder.image);
        viewHolder.nameTV.setText(store.getShopName());
        viewHolder.addressTV.setText(store.getShopAddress());

        viewHolder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+88"+store.getShopNumber()));
                v.getContext().startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return storeList.size();
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

                    List<Store> resultData = new ArrayList<>();

                    for(Store userModel: getUserModelListFiltered){
                        if(userModel.getShopName().toLowerCase().contains(searchChr)){
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

                storeList = (List<Store>) filterResults.values;
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
            nameTV = itemView.findViewById(R.id.shopName);
            addressTV = itemView.findViewById(R.id.ShopAddress);
            image =  itemView.findViewById(R.id.img);
            call = itemView.findViewById(R.id.call);
            all = itemView.findViewById(R.id.naa);
        }
    }

}
