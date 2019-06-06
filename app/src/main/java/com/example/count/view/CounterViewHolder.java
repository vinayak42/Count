package com.example.count.view;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.count.R;

public final class CounterViewHolder extends RecyclerView.ViewHolder {

    TextView counterTitleTextView;
    TextView counterValueTextView;
    TextView creationTimestampTextView;
    TextView lastUpdationTimestampTextView;

    public CounterViewHolder(@NonNull View itemView) {
        super(itemView);
        counterTitleTextView = (TextView) itemView.findViewById(R.id.counter_title_text_view);
        counterValueTextView = (TextView) itemView.findViewById(R.id.counter_value_text_view);
        creationTimestampTextView = (TextView) itemView.findViewById(R.id.last_updated_text_view);
        lastUpdationTimestampTextView = (TextView) itemView.findViewById(R.id.date_created_text_view);
    }
}
