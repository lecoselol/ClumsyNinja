package lecoselol.clumsyninja;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;

public class MajesticAdapter extends BaseAdapter implements Filterable
{
    private ArrayList<Note> notes, originalNotes;

    public MajesticAdapter(Collection<Note> notes)
    {
        this.notes = (ArrayList<Note>)notes;
        this.originalNotes = (ArrayList<Note>)notes;
    }

    @Override
    public int getCount()
    {
        return notes.size();
    }

    @Override
    public Object getItem(int i)
    {
        return notes.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return notes.get(i).getId();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup)
    {
        ViewHolder holder;
        final LayoutInflater inflater =
                (LayoutInflater)NinjaApplication.getInstance().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (null == view)
        {
            //TODO inserire il layout del singolo elemento
            holder = new ViewHolder();
            //view = inflater.inflate(R.layout.main, null); //TODO modificare qui
            //holder.title = view.findViewById(R.id.txtTitle);
            //holder.body = view.findViewById(R.id.txtBody);
            view.setTag(holder);
        }
        else
            holder = (ViewHolder)view.getTag();

        final Note currentNote = notes.get(position);

        holder.title.setText(currentNote.getTitle());
        holder.body.setText(currentNote.getBody());

        return view;
    }

    @Override
    public Filter getFilter()
    {
        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence)
            {
                FilterResults results = new FilterResults();

                if (charSequence.length() == 0)
                {
                    results.values = originalNotes;
                    notes = originalNotes; //reset previous state
                }
                else
                {
                    ArrayList<Note> filteredNotes = new ArrayList<>();
                    for (Note note : notes)
                    {
                        if (note.getTitle().contains(charSequence) || note.getBody().contains(charSequence))
                            filteredNotes.add(note);
                    }
                    results.values = filteredNotes;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults)
            {
                notes = (ArrayList<Note>)filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    private class ViewHolder
    {
        TextView title, body;
    }
}
