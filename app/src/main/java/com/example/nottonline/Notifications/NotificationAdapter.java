package com.example.nottonline.Notifications;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nottonline.R;
import com.example.nottonline.Database.Event;

import java.util.List;

/**
 * The {@code NotificationAdapter} class is a RecyclerView adapter responsible for binding
 * notification data to the corresponding views in the notification card layout.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private Context context;
    private List<Event> notificationList;
    private OnItemClickListener onItemClickListener;

    /**
     * Constructs a new instance of the NotificationAdapter.
     *
     * @param context         The context in which the adapter will be used.
     * @param notifications   The list of notification events to be displayed.
     */
    public NotificationAdapter(Context context, List<Event> notifications) {
        this.context = context;
        this.notificationList = notifications;
    }

    /**
     * Sets a new list of notifications for the adapter.
     *
     * @param notificationList The new list of notification events.
     */
    public void setNotificationList(List<Event> notificationList) {
        this.notificationList = notificationList;
        notifyDataSetChanged();
    }

    /**
     * Called when RecyclerView needs a new {@code NotificationViewHolder} of the given type
     * to represent an item.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new {@code NotificationViewHolder} that holds a View of the given view type.
     */
    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the card layout
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_card, parent, false);
        return new NotificationViewHolder(itemView);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * @param holder   The {@code NotificationViewHolder} that should be updated to represent
     *                 the contents of the item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        holder.Time.setText(notificationList.get(position).getTime());
        holder.NotificationMessage.setText(notificationList.get(position).getNotificationMessage());
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in the adapter's data set.
     */
    @Override
    public int getItemCount() {
        return notificationList != null ? notificationList.size() : 0;
    }

    /**
     * The {@code NotificationViewHolder} class represents a ViewHolder for the notification items
     * in the RecyclerView.
     */
    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView Time, NotificationMessage;

        /**
         * Constructs a new instance of the {@code NotificationViewHolder}.
         *
         * @param itemView The view associated with this ViewHolder.
         */
        NotificationViewHolder(View itemView) {
            super(itemView);

            Time = itemView.findViewById(R.id.Time);
            NotificationMessage = itemView.findViewById(R.id.NotificationMessage);
        }
    }

    /**
     * The interface to handle item click events in the RecyclerView.
     */
    public interface OnItemClickListener {
        /**
         * Called when an item in the RecyclerView is clicked.
         *
         * @param event The clicked event item.
         */
        void onItemClick(Event event);
    }
}
