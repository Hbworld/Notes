package hbworld.com.notes.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import hbworld.com.notes.database.NotesItems;
import hbworld.com.notes.R;

/**
 * Created by Hbworld_new on 7/19/2016.
 */
public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> {

    private ArrayList<NotesItems> items = new ArrayList<>();
    private Context context;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(NotesItems item);
    }


    public NotesAdapter(Context context, OnItemClickListener listener) {
        this.context = context;
        this.listener = listener;

    }

    public void addItem(ArrayList<NotesItems> c) {
        items.addAll(c);
        notifyDataSetChanged();
    }

    public void clear() {
        items.clear();
    }

    public NotesItems getNotes(int position){
       return items.get(position);
    }

    public void remove(int position) {
        NotesItems item = items.get(position);

        if (items.contains(item)) {
            items.remove(position);
            notifyItemRemoved(position);
        }

    }

    @Override
    public int getItemCount() {
        return items.size();

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notes_item_view, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.bind(items.get(position), listener);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTitle, txtDescription;
        public LinearLayout linearLayout;

        public MyViewHolder(View view) {
            super(view);
            txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            txtDescription = (TextView) view.findViewById(R.id.txtDescription);
            linearLayout = (LinearLayout) view.findViewById(R.id.ll);

        }

        public void bind(final NotesItems item, final OnItemClickListener listener) {

            txtTitle.setText(item.getTitle());
            txtDescription.setText(item.getDescription());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}

