package lecoselol.clumsyninja;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A fragment representing a single Note detail screen.
 * This fragment is either contained in a {@link NoteListActivity}
 * in two-pane mode (on tablets) or a {@link NoteDetailActivity}
 * on handsets.
 */
public class NoteDetailFragment extends Fragment implements AsyncAllTheThings.Callback {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    private Note mItem;
    private View mRootView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NoteDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID) &&
            getArguments().getInt(ARG_ITEM_ID) >= 0) {
            AsyncAllTheThings.selectAllTheNotes(this, "DAT PIN");    // TODO get dat PIN
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_note_detail, container, false);

        return mRootView;
    }

    @Override
    public void execute(Collection<Note> notes) {
        // Assumes Roberto can do anything right
        mItem = ((ArrayList<Note>) notes).get(getArguments().getInt(ARG_ITEM_ID));

        if (mItem != null) {
            ((TextView) mRootView.findViewById(R.id.txt_iltitolodellacard)).setText(mItem.getTitle());
            ((TextView) mRootView.findViewById(R.id.txt_elcuerpodelsenhorcardo)).setText(mItem.getBody());
        }
    }
}
