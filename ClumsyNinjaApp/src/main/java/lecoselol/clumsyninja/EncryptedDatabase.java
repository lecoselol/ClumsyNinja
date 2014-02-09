package lecoselol.clumsyninja;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.Collection;

public class EncryptedDatabase {
    //Public constants
    public static final long INSERT_ERROR = -1;

    //Field names
    private static final class NoteTable implements BaseColumns {
        public static final String TABLE_NAME = "notes";
        public static final String NOTE_TITLE = "title";
        public static final String NOTE_BODY = "body";
    }

    //Separators - Operators
    private static final String TEXT_TYPE = "TEXT";
    private static final String ID_SELECTION = NoteTable._ID + " = ?";

    //Queries
    private static final String CREATE_TABLE = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s %s, %s %s)",
                                                             NoteTable.TABLE_NAME, NoteTable._ID, NoteTable.NOTE_TITLE,
                                                             TEXT_TYPE, NoteTable.NOTE_BODY, TEXT_TYPE);

    private static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + NoteTable.TABLE_NAME;

    private static final class Helper extends SQLiteOpenHelper {
        private static final int VERSION = 1;
        private static final String NAME = "notes.db";

        public Helper(Context context) {
            super(context, NAME, null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DELETE_TABLE);
            onCreate(db);
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }

    private static SQLiteDatabase getWDatabase(Context context) {
        return new Helper(context).getWritableDatabase();
    }

    private static ContentValues createNote(String title, String text) {
        ContentValues newNote = new ContentValues();
        newNote.put(NoteTable.NOTE_TITLE, title);
        newNote.put(NoteTable.NOTE_BODY, text);

        return newNote;
    }

    private static void deleteNote(SQLiteDatabase database, int noteId) {
        final String[] selectionArgs = new String[] {String.valueOf(noteId)};

        database.delete(NoteTable.TABLE_NAME,
                        ID_SELECTION,
                        selectionArgs);
    }

    //Public methods

    /**
     * Inserts a crypted note into local database.
     *
     * @param context
     * @param title   Crypted title of the note.
     * @param text    Crypted body of the note.
     *
     * @return INSERT_ERROR in case of failure, any other value otherwise
     */
    public static long insertNote(Context context, String title, String text) {
        final SQLiteDatabase database = getWDatabase(context);
        final ContentValues newNote = createNote(title, text);

        final long result = database.insert(NoteTable.TABLE_NAME, null, newNote);

        database.close();

        return result;
    }

    /**
     * Updates a crypted note into local database.
     *
     * @param context
     * @param note    note to be updated.
     *
     * @return the number of rows affected by the update.
     */
    public static long editNote(Context context, Note note) {
        final SQLiteDatabase database = getWDatabase(context);
        final ContentValues editNote = createNote(note.getTitle(), note.getBody());

        final String[] selectionArgs = new String[] {String.valueOf(note.getId())};

        final long affectedRows = database.update(
            NoteTable.TABLE_NAME,
            editNote,
            ID_SELECTION,
            selectionArgs);

        database.close();

        return affectedRows;
    }

    /**
     * Deletes a list of notes passed as parameter. MUAHAHAHAHAHAH.
     *
     * @param context
     * @param notes   list of notes to be deleted.
     */
    public static void deleteNotes(Context context, Collection<Note> notes) {
        final SQLiteDatabase database = getWDatabase(context);

        for (Note note : notes) {
            deleteNote(database, note.getId());
        }

        database.close();
    }

    /**
     * Selects all the notes on local database
     *
     * @param context
     *
     * @return a list of all the encrypted notes present on local database.
     */
    public static Collection<Note> getNotes(Context context) {
        final Helper helper = new Helper(context);
        final SQLiteDatabase database = helper.getReadableDatabase();

        final Cursor cursor = database.query(NoteTable.TABLE_NAME, null, null, null, null, null, null);

        cursor.moveToFirst();

        Note tmpNote;
        Collection<Note> notes = new ArrayList<Note>();

        while (!cursor.isAfterLast()) {
            try {
                tmpNote = new Note();
                tmpNote.setId(cursor.getInt(cursor.getColumnIndex(NoteTable._ID)));
                tmpNote.setTitle(cursor.getString(cursor.getColumnIndex(NoteTable.NOTE_TITLE)));
                tmpNote.setBody(cursor.getString(cursor.getColumnIndex(NoteTable.NOTE_BODY)));
                notes.add(tmpNote);
            }
            catch (Exception e) {/** Recovery part to avoid crashes **/}

            cursor.moveToNext();
        }

        database.close();

        return notes;
    }
}
