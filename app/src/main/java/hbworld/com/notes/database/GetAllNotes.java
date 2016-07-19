package hbworld.com.notes.database;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hbworld.com.notes.adapter.NotesAdapter;
import hbworld.com.notes.database.DBModel;
import hbworld.com.notes.database.DatabaseHandler;
import hbworld.com.notes.database.NotesItems;

/**
 * Created by Hbworld_new on 7/19/2016.
 */
public class GetAllNotes extends AsyncTask<Void, Void, Void> {

    private Context context;
    private NotesAdapter notesAdapter;
    private TextView txtCenter;
    private ArrayList<NotesItems> notes = new ArrayList<>();


    public GetAllNotes(Context context, NotesAdapter notesAdapter, ArrayList<NotesItems> notes, TextView txtCenter) {
        this.context = context;
        this.notesAdapter = notesAdapter;
        this.notes = notes;
        this.txtCenter = txtCenter;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        notes.clear();
        notesAdapter.clear();
        notesAdapter.notifyDataSetChanged();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        DatabaseHandler dbHelper = new DatabaseHandler(context);
        List<DBModel> modelList = dbHelper.getAllNotes();
        for (DBModel obj : modelList) {

            NotesItems items = new NotesItems();
            items.setID(obj.get_notes_id());
            items.setTitle(obj.get_notes_title());
            items.setDescription(obj.get_notes_description());
            notes.add(items);

        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        notesAdapter.addItem(notes);
        if (notesAdapter.getItemCount() == 0) {
            txtCenter.setVisibility(View.VISIBLE);
        } else txtCenter.setVisibility(View.GONE);


    }
}
