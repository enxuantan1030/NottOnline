package com.example.nottonline.Database;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.WindowDecorActionBar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nottonline.R;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private Context context;
    private List<Event> eventList;
    private OnItemClickListener onItemClickListener;
    public EventAdapter(Context context, List<Event> events, OnItemClickListener listener){
        this.context = context;
        this.eventList = events;
        this.onItemClickListener = listener;
    }

    public void setEventList(List<Event> eventList){
        this.eventList = eventList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the card layout
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card, parent, false);
        return new EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.EventViewHolder holder, int position) {
        holder.Date.setText(eventList.get(position).getDate());
        holder.Time.setText(eventList.get(position).getTime());
        holder.Title.setText(eventList.get(position).getTitle());

        // Display only the first 50 characters of the description
        String description = eventList.get(position).getDescription();
        String truncatedDescription = description.length() > 50 ? description.substring(0, 50) + "..." : description;

        holder.Desc.setText(truncatedDescription);

        // Bind event data to the views in the ViewHolder
        // Set click listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Trigger the onItemClick method of the listener
                onItemClickListener.onItemClick(eventList.get(position));
            }
        });
    }


    public interface OnItemClickListener {
        void onItemClick(Event event);
    }

    @Override
    public int getItemCount() {
        return eventList != null ? eventList.size() : 0;
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {

        TextView Date, Time, Title, Desc;

        EventViewHolder(View itemView) {
            super(itemView);

            Date = itemView.findViewById(R.id.Date);
            Time = itemView.findViewById(R.id.Time);
            Title = itemView.findViewById(R.id.Title);
            Desc =  itemView.findViewById(R.id.Desc);
        }
    }
}