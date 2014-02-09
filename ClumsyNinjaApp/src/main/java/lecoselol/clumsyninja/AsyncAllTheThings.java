package lecoselol.clumsyninja;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Collection;

public class AsyncAllTheThings
{
    /**
     * Interface to instantiate in order to create the callback for the Select *
     */
    public interface Callback
    {
        public void execute(Collection<Note> notes);
    }

    private static final Void[] emptyArray = new Void[] { };

    private static final class Insert extends AsyncTask<Void, Void, Void>
    {
        private final String title, text, key;

        public Insert(String title, String text, String key)
        {
            this.title = title;
            this.text = text;
            this.key = key;
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            final Context mContext = NinjaApplication.getInstance();

            try
            {
                byte[] encryptedTitle = Crypto.encryptBytes(key, title);
                byte[] encryptedText = Crypto.encryptBytes(key, text);
                final long result = EncryptedDatabase.insertNote(mContext, encryptedTitle, encryptedText);

                if (result == EncryptedDatabase.INSERT_ERROR)
                {
                    showErrorToast(mContext);
                }
                else
                {
                    final Collection<Note> allTheNotes = EncryptedDatabase.getNotes(mContext);
                    NoteBaasboxService.update(mContext, allTheNotes);
                }
            }
            catch (Exception e)
            {
                showErrorToast(mContext);
            }

            return null;
        }

        private void showErrorToast(Context context)
        {

            //TODO insert Emoji
            final String errorMessage = context.getString(R.string.insert_error_text);

            SplendidToast.show(context, errorMessage);
        }
    }

    private static final class Delete extends AsyncTask<Void, Void, Void>
    {
        private Collection<Note> notes;

        public Delete(Collection<Note> notes)
        {
            this.notes = notes;
        }

        @Override
        protected Void doInBackground(Void... voids)
        {
            EncryptedDatabase.deleteNotes(NinjaApplication.getInstance(), notes);

            return null;
        }
    }

    private static final class Edit extends AsyncTask<Note, Void, Void>
    {
        @Override
        protected Void doInBackground(Note... notes)
        {
            if (notes.length > 0)
            {
                final Context app = NinjaApplication.getInstance();
                if (EncryptedDatabase.editNote(NinjaApplication.getInstance(),
                                               notes[0],
                                               NinjaApplication.getUserKey()) > 0)
                {
                    final Collection<Note> allTheNotes = EncryptedDatabase.getNotes(app);
                    NoteBaasboxService.update(app, allTheNotes);
                }
            }

            return null;
        }
    }

    private static final class GetAllEntries extends AsyncTask<Void, Void, ArrayList<Note>>
    {
        private Callback callback = null;
        private String key;

        public GetAllEntries(Callback callback, String key)
        {
            this.callback = callback;
            this.key = key;
        }

        @Override
        protected ArrayList<Note> doInBackground(Void... voids)
        {
            return EncryptedDatabase.getNotes(NinjaApplication.getInstance());
        }


        @Override
        protected void onPostExecute(ArrayList<Note> notes)
        {
            if (null != callback) callback.execute(notes);
        }
    }

    //Public methods

    /**
     * Inserts a note asynchronously
     *
     * @param title title of the note.
     * @param text  body of the note.
     * @param key   encryption key.
     */
    public static void insertNote(String title, String text, String key)
    {
        final Insert task = new Insert(title, text, key);
        task.execute(emptyArray);
    }

    /**
     * Deletes all the notes selected
     *
     * @param notes Collection(Note) containing all the notes to delete
     */
    public static void deleteNotes(Collection<Note> notes)
    {
        final Delete task = new Delete(notes);
        task.execute(emptyArray);
    }

    /**
     * Edits a note
     *
     * @param note note to be edited
     */
    public static void editNote(Note note)
    {
        final Note[] notes = new Note[] { note };
        final Edit task = new Edit();

        task.execute(notes);
    }

    /**
     * Selects all the notes.
     *
     * @param callback Callback that will manage the adapter of the ListView.
     */
    public static void selectAllTheNotes(Callback callback, String key)
    {
        final GetAllEntries task = new GetAllEntries(callback, key);
        task.execute(emptyArray);
    }
}
