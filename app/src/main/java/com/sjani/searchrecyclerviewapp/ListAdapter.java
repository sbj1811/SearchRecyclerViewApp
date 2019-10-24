package com.sjani.searchrecyclerviewapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sjani.searchrecyclerviewapp.model.PizzaItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> implements Filterable {

    public static final String NAME = "name";
    public static final String POSITION = "position";
    public static final String TAG = ListAdapter.class.getName();
    ItemClickListener clickListener;

    List<PizzaItem> pizzaItems = new ArrayList<>();
    List<PizzaItem> pizzaItemList;

    public ListAdapter(ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            PizzaItem pizzaItem = pizzaItems.get(position);
            String image = pizzaItem.getAssets().getMenu().get(0).getUrl();
            holder.textView.setText(pizzaItem.getName());
            if(!image.equals("")) {
                Glide.with(holder.imageView.getContext())
                        .load(image)
                        .into(holder.imageView);
            }
    }

    @Override
    public int getItemCount() {
        return pizzaItems.size();
    }

    public void swapResults(List<PizzaItem> results){
        this.pizzaItems = results;
        this.pizzaItemList = new ArrayList<>(this.pizzaItems);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return pizzaFilter;
    }

    private Filter pizzaFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<PizzaItem> filteredItems =  new ArrayList<>();
            if(constraint == null || constraint.length()==0){
                filteredItems.addAll(pizzaItemList);
            } else {
                String fillterPatten =  constraint.toString().toLowerCase().trim();
                for(PizzaItem pizzaItem : pizzaItemList){
                    if(pizzaItem.getName().toLowerCase().contains(fillterPatten)){
                        Log.e(TAG, "performFiltering: HERE "+pizzaItem.getName());
                        filteredItems.add(pizzaItem);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredItems;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
                pizzaItems.clear();;
                pizzaItems.addAll((List) results.values);
                notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.image)
        ImageView imageView;

        @BindView(R.id.name)
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            String name = pizzaItems.get(position).getName();
            clickListener.itemClick(position,name);
        }
    }

}
