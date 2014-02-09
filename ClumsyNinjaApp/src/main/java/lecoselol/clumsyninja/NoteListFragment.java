package lecoselol.clumsyninja;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A list fragment representing a list of Notes. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link NoteDetailFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class NoteListFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, AsyncAllTheThings.Callback {
    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = sDummyCallbacks;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;
    private GridView mListView;

    private ActionMode mActionMode;
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.context_menu, menu);
            getActivity().getWindow().setBackgroundDrawable(
                    getActivity().getResources().getDrawable(R.drawable.extended_window_cab_background));
            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_share:
                    shareCurrentItem();
                    mode.finish(); // Action picked, so close the CAB
                    return true;
                case R.id.action_delete:
                    deleteCurrentItem();
                    mode.finish(); // Action picked, so close the CAB
                    return true;
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
            getActivity().getWindow().setBackgroundDrawable(
                    getActivity().getResources().getDrawable(R.drawable.extended_window_background));
        }
    };

    private void deleteCurrentItem() {
        final ArrayList<Note> notes = new ArrayList<>();
        notes.add((Note) mListView.getSelectedItem());
        AsyncAllTheThings.deleteNotes(notes);
    }

    private void shareCurrentItem() {
        final Note item = (Note) mListView.getSelectedItem();
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, item.getShareableString());
        startActivity(shareIntent);
    }

    public GridView getListView() {
        return mListView;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (mActionMode != null) {
            return false;
        }

        // Start the CAB using the ActionMode.Callback defined above
        mActionMode = getActivity().startActionMode(mActionModeCallback);
        view.setSelected(true);
        return true;
    }


    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(String id);
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(String id) {
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NoteListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_cosechescorrono, container, false);
        mListView = (GridView) v.findViewById(android.R.id.list);
        mListView.setEmptyView(v.findViewById(android.R.id.empty));
        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);
        refreshList();

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
            && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        final Note item = (Note) mListView.getItemAtPosition(position);
        mCallbacks.onItemSelected(String.valueOf(item.getId()));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick
                                    ? ListView.CHOICE_MODE_SINGLE
                                    : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        }
        else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

    public void refreshList() {
        AsyncAllTheThings.selectAllTheNotes(this, NinjaApplication.getUserKey());
    }

    @Override
    public void execute(final Collection<Note> notes) {
        final ArrayList<Note> list = (ArrayList<Note>) notes;

        final BaseAdapter adapter =
            new BaseAdapter() {
                @Override
                public int getCount() {
                    return list.size();
                }

                @Override
                public Object getItem(int position) {
                    return list.get(position);
                }

                @Override
                public long getItemId(int position) {
                    return list.get(position).getId();
                }

                @Override
                public View getView(int position, View view, ViewGroup parent) {
                    ViewHolder holder;

                    Log.d("ORCATROIA", "Uno: " + position);

                    if (null == view) {
                        final LayoutInflater inflater = LayoutInflater.from(NinjaApplication.getInstance());
                        holder = new ViewHolder();
                        view = inflater.inflate(R.layout.senor_cardo, null);
                        holder.title = (TextView) view.findViewById(R.id.txt_iltitolodellacard);
                        holder.body = (TextView) view.findViewById(R.id.txt_elcuerpodelsenhorcardo);
                        view.setTag(holder);
                    }
                    else {
                        holder = (ViewHolder) view.getTag();
                    }

                    final Note currentNote = list.get(position);

                    final String title = currentNote.getTitle();
                    if (TextUtils.isEmpty(title)) {
                        holder.title.setVisibility(View.GONE);
                    }
                    else {
                        holder.title.setVisibility(View.VISIBLE);
                        holder.title.setText(title);
                    }

                    holder.body.setText(currentNote.getBody());

                    return view;
                }

                class ViewHolder {
                    public TextView title;
                    public TextView body;
                }
            };

        mListView.setAdapter(adapter);

    }

    static private final int COLORS_NUMBER = 8;
    private Drawable getColor(int position)
    {
        final int color = position % COLORS_NUMBER;
        int id;
        switch (color)
        {
            default: case 0: id = R.color.bianco; break;
            case 1: id = R.color.azzurro; break;
            case 2: id = R.color.arancione; break;
            case 3: id = R.color.giallo; break;
            case 4: id = R.color.grigio; break;
            case 5: id = R.color.melerde; break;
            case 6: id = R.color.rosso; break;
            case 7: id = R.color.verzurro; break;
        }
        return NinjaApplication.getInstance().getResources().getDrawable(id);
    }
}
