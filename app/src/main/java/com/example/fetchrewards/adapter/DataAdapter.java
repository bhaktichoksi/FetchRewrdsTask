package com.example.fetchrewards.adapter;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.fetchrewards.R;
import com.example.fetchrewards.model.Rewards;
import com.github.aakira.expandablelayout.ExpandableLayout;
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private List<List<Rewards>> data;
    private Context context;
    private SparseBooleanArray expandState = new SparseBooleanArray();

    public DataAdapter(final List<List<Rewards>> listdata) {
        this.data = listdata;
        Log.e("ss", "data: " + listdata.size());
        for (int i = 0; i < data.size(); i++) {
            expandState.append(i, false);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        this.context = parent.getContext();
        return new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.recycler_view_list_row, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Rewards item = data.get(position).get(0);
        Log.e("ss", "onBindViewHolder: " + item);
        holder.setIsRecyclable(false);
        holder.txtListId.setText("List ID - " + item.getLid());
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true));

        holder.filterAdapter = new FilterAdapter(data.get(position));
        holder.recyclerView.setAdapter(holder.filterAdapter);

        holder.expandableLayout.setInRecyclerView(true);
        holder.expandableLayout.setExpanded(expandState.get(position));
        holder.expandableLayout.setListener(new ExpandableLayoutListenerAdapter() {
            @Override
            public void onPreOpen() {
                holder.iv_arrow.setImageResource(R.drawable.iv_down_arrow);
                expandState.put(position, true);
            }

            @Override
            public void onPreClose() {
                holder.iv_arrow.setImageResource(R.drawable.iv_right_arrow);
                expandState.put(position, false);
            }
        });

        holder.buttonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                onClickButton(holder.expandableLayout);
            }
        });
    }

    private void onClickButton(final ExpandableLayout expandableLayout) {
        expandableLayout.toggle();
    }

    @Override
    public int getItemCount() {
        Log.e("ss", "getItemCount: " + data.size());
        return data.size();
    }

    public void setdata(List<List<Rewards>> filterArrayList1) {
        data = filterArrayList1;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtListId;
        public RelativeLayout buttonLayout;
        public ImageView iv_arrow;
        RecyclerView recyclerView;
        FilterAdapter filterAdapter;

        public ExpandableLinearLayout expandableLayout;

        public ViewHolder(View v) {
            super(v);
            iv_arrow = (ImageView) v.findViewById(R.id.iv_arrow);
            txtListId = (TextView) v.findViewById(R.id.txtListId);
            buttonLayout = (RelativeLayout) v.findViewById(R.id.button);
            expandableLayout = (ExpandableLinearLayout) v.findViewById(R.id.expandableLayout);
            recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        }
    }

}
