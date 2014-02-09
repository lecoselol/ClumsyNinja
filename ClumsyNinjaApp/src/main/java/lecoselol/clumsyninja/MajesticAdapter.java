package lecoselol.clumsyninja;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;

public class MajesticAdapter extends BaseAdapter {
    private ArrayList<Note> notes, originalNotes;

    public MajesticAdapter(Collection<Note> notes) {
        this.notes = (ArrayList<Note>) notes;
        this.originalNotes = (ArrayList<Note>) notes;
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public Object getItem(int i) {
        return notes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return notes.get(i).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if (null == view) {
            final LayoutInflater inflater = LayoutInflater.from(NinjaApplication.getInstance());
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.senor_cardo, null);
            holder.title = (TextView) view.findViewById(R.id.txt_iltitolodellacard);
            holder.body = (TextView)view.findViewById(R.id.txt_elcuerpodelsenhorcardo);
            view.setTag(holder);
        }
        else {
            holder = (ViewHolder) view.getTag();
        }

        final Note currentNote = notes.get(position);

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
/*
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();

                if (charSequence.length() == 0) {
                    results.values = originalNotes;
                    notes = originalNotes; //reset previous state
                }
                else {
                    ArrayList<Note> filteredNotes = new ArrayList<>();
                    for (Note note : notes) {
                        if (note.getTitle().contains(charSequence) || note.getBody().contains(charSequence)) {
                            filteredNotes.add(note);
                        }
                    }
                    results.values = filteredNotes;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                notes = (ArrayList<Note>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }*/

    private class ViewHolder {
        TextView title, body;
    }
}
