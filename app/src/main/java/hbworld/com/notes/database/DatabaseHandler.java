package hbworld.com.notes.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hbworld_new on 7/19/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 4;

    // Database Name
    private static final String DATABASE_NAME = "DemoNotes";

    // Contacts table name
    private static final String TABLE_CONTACTS = "notes";

    // Contacts Table Columns names
    private static final String NOTES_ID = "notes_id";
    private static final String NOTES_TITLE = "notes_title";
    private static final String NOTES_DESCRIPTION = "notes_description";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + NOTES_ID + " Text,"
                + NOTES_TITLE + " TEXT,"
                + NOTES_DESCRIPTION + " TEXT"
                + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        // Create tables again
        onCreate(db);
    }

    /***
     *
     * @param note
     */
    public void addNote(DBModel note) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(NOTES_ID, note.get_notes_id());
            values.put(NOTES_TITLE, note.get_notes_title());
            values.put(NOTES_DESCRIPTION, note.get_notes_description());
            // Inserting Row
            System.out.println("DB Entries Insert value: " + values.toString());
            db.insert(TABLE_CONTACTS, null, values);
            db.close(); // Closing database connection
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Getting All Notes
    /***
     *
     * @return
     */
    public List<DBModel> getAllNotes() {
        List<DBModel> noteList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        try {
            if (cursor.moveToFirst()) {
                do {
                    DBModel noteModel = new DBModel();
                    noteModel.set_notes_id(cursor.getString(0));
                    noteModel.set_notes_title(cursor.getString(1));
                    noteModel.set_notes_description(cursor.getString(2));
                    noteList.add(noteModel);
                } while (cursor.moveToNext());
            }

        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            db.close();
        }
        return noteList;
    }

    /***
     *
     * @param model
     * @param id
     */
    public void EditNote(DBModel model, String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NOTES_ID, model.get_notes_id());
        values.put(NOTES_TITLE, model.get_notes_title());
        values.put(NOTES_DESCRIPTION, model.get_notes_description());
        // updating row
        int i = db.update(TABLE_CONTACTS, values, NOTES_ID + " = ?",
                new String[]{String.valueOf(id)});
        System.out.println("DB Entries Update  " + i);

        db.close();
    }

    /***
     *
     * @param id
     */
    public void deleteNoteEntry(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete(TABLE_CONTACTS, NOTES_ID + " = ?",
                new String[]{String.valueOf(id)});
        System.out.println("DB Entries Update  " + i);

        db.close();
    }

}
