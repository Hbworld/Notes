package hbworld.com.notes.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import hbworld.com.notes.database.DBModel;
import hbworld.com.notes.database.DatabaseHandler;
import hbworld.com.notes.R;
import hbworld.com.notes.utils.Constant;

public class NotesDetailActivity extends BaseActivity implements View.OnClickListener {

    private Context context;
    private EditText etTitle, etDescription;
    private Button btnEdit;
    private Intent i;
    private Dialog dialog;
    private boolean editFlag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_detail);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        configureToolbar("Notes Detail");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        context = NotesDetailActivity.this;
        i = getIntent();
        findViews();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_notes_detail;
    }

    private void findViews() {
        etTitle = (EditText) findViewById(R.id.etTitle);
        etDescription = (EditText) findViewById(R.id.etDescription);
        btnEdit = (Button) findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(this);
        etTitle.setEnabled(false);
        etDescription.setEnabled(false);

        etTitle.setText(i.getStringExtra(Constant.NOTES_TITLE));
        etDescription.setText(i.getStringExtra(Constant.NOTES_DESCRIPTION));
    }

    /***
     *
     */
    private void updateNoteEntry(){
        DatabaseHandler dbHelper = new DatabaseHandler(context);
        DBModel model = new DBModel();
        model.set_notes_title(etTitle.getText().toString().trim());
        model.set_notes_description(etDescription.getText().toString().trim());
        model.set_notes_id(i.getStringExtra(Constant.NOTES_ID));
        if (TextUtils.isEmpty(model.get_notes_title())) {
            Toast.makeText(context, R.string.title_required, Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(model.get_notes_description())) {
            Toast.makeText(context, R.string.description_required, Toast.LENGTH_LONG).show();
        } else {
            dbHelper.EditNote(model, i.getStringExtra(Constant.NOTES_ID));
            btnEdit.setText(getResources().getString(R.string.edit_note));
            etTitle.setEnabled(false);
            etDescription.setEnabled(false);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            editFlag = true;
        }
    }

    private void openNoteDeleteBox() {
        if (dialog != null && dialog.isShowing())
            return;
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.delete_note_dialog);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animationdialog;
        dialog.setCancelable(true);

        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog.findViewById(R.id.txtOK).setOnClickListener(this);
        dialog.findViewById(R.id.txtCANCEL).setOnClickListener(this);
        dialog.show();
    }

    /***
     *
     */
    private void deleteNote() {
        dialog.dismiss();
        DatabaseHandler dbHelper = new DatabaseHandler(context);
        dbHelper.deleteNoteEntry(i.getStringExtra(Constant.NOTES_ID));
        NotesDetailActivity.this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.note_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.push_right_in, R.anim.push_up_out_new);
            NotesDetailActivity.this.finish();
            return true;
        } else if (id == R.id.delete) {
            openNoteDeleteBox();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnEdit:
                if (editFlag) {
                    btnEdit.setText(getResources().getString(R.string.done));
                    etTitle.setEnabled(true);
                    etDescription.setEnabled(true);
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    editFlag = false;

                } else {
                    updateNoteEntry();
                }
                break;
            case R.id.txtOK:
                deleteNote();
                break;
            case R.id.txtCANCEL:
                dialog.dismiss();
                break;

        }
    }
}
