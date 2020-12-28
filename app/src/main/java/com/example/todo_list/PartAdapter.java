package com.example.todo_list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PartAdapter extends RecyclerView.Adapter<PartAdapter.ViewHolder>{
    List<Part> parts;
    ListItemClickListener mListItemClickListener;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, count;
        ListItemClickListener listItemClickListener;
        public ViewHolder(View view, ListItemClickListener listItemClickListener) {
            super(view);
            // Define click listener for the ViewHolder's View
            this.listItemClickListener = listItemClickListener;

            title = itemView.findViewById(R.id.partTitle);
            count = itemView.findViewById(R.id.partCount);

            view.setOnClickListener(this);
        }

        public void setData(Part part) {
            title.setText(part.getTitle());
            count.setText("" + part.getCount());
        }

        @Override
        public void onClick(View v) {
            listItemClickListener.onListItemClick(getAdapterPosition());
        }
    }


    public PartAdapter(List<Part> categories, ListItemClickListener listItemClickListener) {
        this.parts = parts;
        this.mListItemClickListener = listItemClickListener;
    }



    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.activity_part_slice, viewGroup, false);

        return new ViewHolder(view, mListItemClickListener);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your list at this position and replace the
        // contents of the view with that element
        viewHolder.setData(parts.get(position));


    }

    @Override
    public int getItemCount() {
        return parts.size();
    }

    interface ListItemClickListener {
        void onListItemClick(int position);
    }
}
