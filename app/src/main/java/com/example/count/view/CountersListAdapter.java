package com.example.count.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.count.R;

import java.util.ArrayList;

public class CountersListAdapter extends RecyclerView.Adapter<CountersListAdapter.MyViewHolder>{

    private ArrayList<Counter> counterArrayList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView counterValueTv, lastUpdatedTv, dateCreatedTv;

        public MyViewHolder(View view) {
            super(view);
            counterValueTv = (TextView) view.findViewById(R.id.counter_value_tv);
            lastUpdatedTv = (TextView) view.findViewById(R.id.last_updated_tv);
            dateCreatedTv = (TextView) view.findViewById(R.id.date_created_tv);
        }
    }

    public CountersListAdapter(ArrayList<Counter> counterArrayList) {
        this.counterArrayList = counterArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.counter_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Counter counter = counterArrayList.get(position);
        holder.dateCreatedTv.setText(counter.getCreationTime().toDate().toString());
        holder.lastUpdatedTv.setText(counter.getLastUpdatedTime().toDate().toString());
        holder.counterValueTv.setText(counter.getCounterValue() + "");
    }

    @Override
    public int getItemCount() {
        return counterArrayList.size();
    }
}
