package com.example.fetchrewards.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.fetchrewards.R;
import com.example.fetchrewards.model.Rewards;

import java.util.List;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.MyViewHolder> {

    private List<Rewards> FilterList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName, txtID;

        public MyViewHolder(View view) {
            super(view);
            txtName = (TextView) view.findViewById(R.id.txtName);
            txtID = (TextView) view.findViewById(R.id.txtID);
        }
    }

    public FilterAdapter(List<Rewards> FilterList) {
        this.FilterList = FilterList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_main_indices, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Rewards list = FilterList.get(position);
        if (list.getName() != null) {
            holder.txtName.setText("" + list.getName());
        }
        holder.txtID.setText("ID = " + list.getId());
    }

    @Override
    public int getItemCount() {
        return FilterList.size();
    }
}
