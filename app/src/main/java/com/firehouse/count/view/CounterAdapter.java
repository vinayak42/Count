package com.firehouse.count.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firehouse.count.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public final class CounterAdapter extends RecyclerView.Adapter<CounterAdapter.CounterHolder>{

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private TextView emptyTextView;
    private List<Counter> counterList = new ArrayList<>();

    public CounterAdapter() {
    }

    @NonNull
    @Override
    public CounterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.counter_list_item, parent, false);
        return new CounterHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CounterHolder counterHolder, int position) {

        Counter counter = counterList.get(position);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm:ss");

        String creationTimestampString = simpleDateFormat.format(counter.getCreationTimestamp()) + " at " + simpleTimeFormat.format(counter.getCreationTimestamp());
        String lastUpdationTimestampString = simpleDateFormat.format(counter.getLastUpdationTimestamp()) + " at " + simpleTimeFormat.format(counter.getLastUpdationTimestamp());


        counterHolder.counterTitleTextView.setText(counter.getTitle());
        counterHolder.counterValueTextView.setText(String.valueOf(counter.getValue()));
        counterHolder.creationTimestampTextView.setText(creationTimestampString);
        counterHolder.lastUpdationTimestampTextView.setText(lastUpdationTimestampString);
    }



    @Override
    public int getItemCount() {
        return counterList.size();
    }

    public void addCounter(Counter counter)
    {
        counterList.add(counter);
        notifyDataSetChanged();
    }

    public List<Counter> getCounterList() {
        return counterList;
    }

    class CounterHolder extends RecyclerView.ViewHolder {

        View itemView;
        TextView counterTitleTextView;
        TextView counterValueTextView;
        TextView creationTimestampTextView;
        TextView lastUpdationTimestampTextView;

        public CounterHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            this.itemView = itemView;
            counterTitleTextView = (TextView) itemView.findViewById(R.id.counter_title_text_view);
            counterValueTextView = (TextView) itemView.findViewById(R.id.counter_value_text_view);
            creationTimestampTextView = (TextView) itemView.findViewById(R.id.date_created_text_view);
            lastUpdationTimestampTextView = (TextView) itemView.findViewById(R.id.last_updated_text_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }

    public void setCounterList(List<Counter> counterList) {
        this.counterList = counterList;
        notifyDataSetChanged(); // TODO replace this for efficiency
    }
}

