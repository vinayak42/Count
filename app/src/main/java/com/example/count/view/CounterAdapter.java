package com.example.count.view;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.count.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public final class CounterAdapter extends RecyclerView.Adapter<CounterAdapter.CounterHolder>{

    private TextView emptyTextView;
    // TODO remove this if it gets really useless
//    private OnItemClickListener listener;
    private ArrayList<Counter> counterArrayList;

    public CounterAdapter(TextView emptyTextView, ArrayList<Counter> counterArrayList) {
        this.emptyTextView = emptyTextView;
        this.counterArrayList = counterArrayList;
    }

    @NonNull
    @Override
    public CounterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.counter_list_item, parent, false);
        return new CounterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CounterHolder counterHolder, int position) {
        // TODO provide a simpler date format using SimpleDateFormat
        Counter counter = counterArrayList.get(position);
        counterHolder.counterTitleTextView.setText(counter.getTitle());
        counterHolder.counterValueTextView.setText(String.valueOf(counter.getValue()));
        counterHolder.creationTimestampTextView.setText(counter.getCreationTimestamp().toString());
        counterHolder.lastUpdationTimestampTextView.setText(counter.getLastUpdationTimestamp().toString());
    }

    @Override
    public int getItemCount() {
        if(counterArrayList == null) {
            return 0;
        }
        return counterArrayList.size();
    }

    public void addCounter(Counter counter)
    {
        counterArrayList.add(counter);
        notifyDataSetChanged();
    }

    public ArrayList<Counter> getCounterArrayList() {
        return counterArrayList;
    }

    class CounterHolder extends RecyclerView.ViewHolder {

        View itemView;
        TextView counterTitleTextView;
        TextView counterValueTextView;
        TextView creationTimestampTextView;
        TextView lastUpdationTimestampTextView;

        public CounterHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            counterTitleTextView = (TextView) itemView.findViewById(R.id.counter_title_text_view);
            counterValueTextView = (TextView) itemView.findViewById(R.id.counter_value_text_view);
            creationTimestampTextView = (TextView) itemView.findViewById(R.id.last_updated_text_view);
            lastUpdationTimestampTextView = (TextView) itemView.findViewById(R.id.date_created_text_view);

            //TODO replace with a suitable method
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = getAdapterPosition();
//                    if (position != RecyclerView.NO_POSITION && listener != null) {
//                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
//                    }
//                }
//            });

        }
    }

    public void setCounterArrayList(ArrayList<Counter> counterArrayList) {
        this.counterArrayList = counterArrayList;
    }

    //TODO remove when it gets really useless
//    public interface OnItemClickListener {
//        public abstract void onItemClick(DocumentSnapshot documentSnapshot, int position);
//    }
//    public void setOnItemClickListener(OnItemClickListener listener) {
//        this.listener = listener;
//    }
}

