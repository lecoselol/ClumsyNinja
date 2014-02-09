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
    private TextView mTitleView;
    private TextView mBodyView;

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
            AsyncAllTheThings.selectAllTheNotes(this, NinjaApplication.getUserKey());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_note_detail, container, false);
        mTitleView = ((TextView) rootView.findViewById(R.id.txt_iltitolodellacard));
        mBodyView = ((TextView) rootView.findViewById(R.id.txt_elcuerpodelsenhorcardo));

        return rootView;
    }

    @Override
    public void execute(Collection<Note> notes) {
        // Assumes Roberto can do anything right
        if (notes.size() > 0) {
            mItem = ((ArrayList<Note>) notes).get(getArguments().getInt(ARG_ITEM_ID));
        }

        if (mItem != null) {

            mTitleView.setText(mItem.getTitle());
            mBodyView.setText(mItem.getBody());
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mItem == null) {
            AsyncAllTheThings.insertNote(getSafeText(mTitleView),
                                         getSafeText(mBodyView),
                                         NinjaApplication.getUserKey());
        }
        else {
            AsyncAllTheThings.editNote(mItem);
        }
    }

    private static String getSafeText(TextView v) {
        CharSequence text = v.getText();

        return text != null ? text.toString() : null;
    }
}
