package hbworld.com.notes.database;

/**
 * Created by Hbworld_new on 7/19/2016.
 */
public class DBModel {

    private String _notes_id;
    private String _notes_title;
    private String _notes_description;


    public DBModel() {
    }

    public DBModel(String _notes_title, String _notes_description) {
        this._notes_title = _notes_title;
        this._notes_description = _notes_description;
    }

    public String get_notes_id() {
        return _notes_id;
    }

    public void set_notes_id(String _notes_id) {
        this._notes_id = _notes_id;
    }

    public String get_notes_title() {
        return _notes_title;
    }

    public void set_notes_title(String _notes_title) {
        this._notes_title = _notes_title;
    }

    public String get_notes_description() {
        return _notes_description;
    }

    public void set_notes_description(String _notes_description) {
        this._notes_description = _notes_description;
    }

    @Override
    public String toString() {
        return "{" +
                "_notes_id='" + _notes_id + '\'' +
                ", _notes_title='" + _notes_title + '\'' +
                ", _notes_description='" + _notes_description +
                '}';
    }
}

