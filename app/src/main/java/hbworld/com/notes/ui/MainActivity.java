package hbworld.com.notes.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import hbworld.com.notes.adapter.NotesAdapter;
import hbworld.com.notes.database.DBModel;
import hbworld.com.notes.database.DatabaseHandler;
import hbworld.com.notes.database.NotesItems;
import hbworld.com.notes.database.GetAllNotes;
import hbworld.com.notes.R;
import hbworld.com.notes.utils.Constant;

public class MainActivity extends BaseActivity implements NotesAdapter.OnItemClickListener, View.OnClickListener {
    private NotesAdapter notesAdapter;
    private Context context;
    private Dialog dialog;
    private EditText etTitle, etDescription;
    private RecyclerView verticalRecyclerView;
    private TextView txtCenter;
    private ArrayList<NotesItems> notes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configureToolbar("Notes");
        context = MainActivity.this;
        notesAdapter = new NotesAdapter(context, this);
        findViews();
        setUpItemTouchHelper();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    private void findViews() {
        verticalRecyclerView = (RecyclerView) findViewById(R.id.vertical_recycler_view);
        findViewById(R.id.btnAddNew).setOnClickListener(this);
        txtCenter = (TextView) findViewById(R.id.txtCenter);
        LinearLayoutManager LayoutManager
                = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

        assert verticalRecyclerView != null;
        verticalRecyclerView.setLayoutManager(LayoutManager);
        verticalRecyclerView.setAdapter(notesAdapter);
        verticalRecyclerView.setHasFixedSize(true);


    }

    private void setUpItemTouchHelper() {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            boolean initiated;

            private void init() {
                initiated = true;
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int swipedPosition = viewHolder.getAdapterPosition();
                NotesAdapter adapter = (NotesAdapter) verticalRecyclerView.getAdapter();

                NotesItems item = adapter.getNotes(swipedPosition);

                DatabaseHandler dbHelper = new DatabaseHandler(context);
                System.out.println(item.getId());
                dbHelper.deleteNoteEntry(item.getId());

                adapter.remove(swipedPosition);

                if (adapter.getItemCount() == 0) {
                    txtCenter.setVisibility(View.VISIBLE);
                } else txtCenter.setVisibility(View.GONE);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                // not sure why, but this method get's called for viewholder that are already swiped away
                if (viewHolder.getAdapterPosition() == -1) {
                    // not interested in those
                    return;
                }
                if (!initiated) {
                    init();
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        mItemTouchHelper.attachToRecyclerView(verticalRecyclerView);
    }

    private void openAddNewDialog() {
        if (dialog != null && dialog.isShowing())
            return;
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_new_dialog);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animationdialog;
        dialog.setCancelable(true);

        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        etTitle = (EditText) dialog.findViewById(R.id.etTitle);
        etDescription = (EditText) dialog.findViewById(R.id.etDescription);
        dialog.findViewById(R.id.btnCancel).setOnClickListener(this);
        dialog.findViewById(R.id.btnDone).setOnClickListener(this);
        dialog.show();
    }

    private void createNote() {

        DatabaseHandler dbHelper = new DatabaseHandler(context);
        String timeStamp = System.currentTimeMillis() + "";
        DBModel model = new DBModel();
        model.set_notes_title(etTitle.getText().toString().trim());
        model.set_notes_description(etDescription.getText().toString().trim());
        model.set_notes_id(timeStamp);

        if (TextUtils.isEmpty(model.get_notes_title())) {
            Toast.makeText(context, R.string.title_required, Toast.LENGTH_LONG).show();

        } else if (TextUtils.isEmpty(model.get_notes_description())) {
            Toast.makeText(context, R.string.description_required, Toast.LENGTH_LONG).show();

        } else {
            dialog.dismiss();
            dbHelper.addNote(model);
            NotesItems items = new NotesItems();
            notes.clear();
            items.setTitle(etTitle.getText().toString().trim());
            items.setDescription(etDescription.getText().toString().trim());
            items.setID(timeStamp);
            notes.add(items);
            notesAdapter.addItem(notes);
            txtCenter.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new GetAllNotes(context, notesAdapter, notes, txtCenter).execute();
    }

    @Override
    public void onItemClick(NotesItems item) {
        Intent i = new Intent(context, NotesDetailActivity.class);
        i.putExtra(Constant.NOTES_TITLE, item.getTitle());
        i.putExtra(Constant.NOTES_DESCRIPTION, item.getDescription());
        i.putExtra(Constant.NOTES_ID, item.getId());
        startActivity(i);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_up_out);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddNew:
                openAddNewDialog();
                break;
            case R.id.btnDone:
                createNote();
                break;
            case R.id.btnCancel:
                dialog.dismiss();
                break;

        }
    }
}
