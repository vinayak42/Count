package com.example.count.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.count.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public final class CounterAdapter extends FirestoreRecyclerAdapter<Counter, CounterAdapter.CounterHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public CounterAdapter(@NonNull FirestoreRecyclerOptions<Counter> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CounterHolder counterHolder, int i, @NonNull Counter counter) {
        counterHolder.counterTitleTextView.setText(counter.getTitle());
        counterHolder.counterValueTextView.setText(String.valueOf(counter.getValue()));
        counterHolder.creationTimestampTextView.setText(counter.getCreationTimestamp().toString());
        counterHolder.lastUpdationTimestampTextView.setText(counter.getLastUpdationTimestamp().toString());
    }

    @NonNull
    @Override
    public CounterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.counter_list_item, parent, false);
        return new CounterHolder(view);
    }

    class CounterHolder extends RecyclerView.ViewHolder {

        TextView counterTitleTextView;
        TextView counterValueTextView;
        TextView creationTimestampTextView;
        TextView lastUpdationTimestampTextView;

        public CounterHolder(@NonNull View itemView) {
            super(itemView);
            counterTitleTextView = (TextView) itemView.findViewById(R.id.counter_title_text_view);
            counterValueTextView = (TextView) itemView.findViewById(R.id.counter_value_text_view);
            creationTimestampTextView = (TextView) itemView.findViewById(R.id.last_updated_text_view);
            lastUpdationTimestampTextView = (TextView) itemView.findViewById(R.id.date_created_text_view);
        }
    }
}
