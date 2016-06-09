package at.campus02.gang_of_four.learningapp.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import at.campus02.gang_of_four.learningapp.R;
import at.campus02.gang_of_four.learningapp.model.Frage;

public class FragenAdapter extends ArrayAdapter<Frage> {

    public FragenAdapter(Context context, List<Frage> items) {
        super(context, R.layout.frage_item, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        FrageHolder holder;

        if (view == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            view = vi.inflate(R.layout.frage_item, parent, false);
            holder = new FrageHolder();
            holder.frage = (TextView) view.findViewById(R.id.frageItemText);
            holder.detail = (TextView) view.findViewById(R.id.frageItemDetail);
            view.setTag(holder);
        } else
            holder = (FrageHolder) view.getTag();

        Frage frage = getItem(position);
        if (frage != null) {
            holder.frage.setText(frage.getFragetext());
            holder.detail.setText(formatDetail(frage.getKategorie(), frage.getSchwierigkeitsgrad()));
        }
        return view;
    }

    static class FrageHolder {
        TextView frage;
        TextView detail;
    }

    private String formatDetail(String kategorie, int schwierigkeit) {
        return String.format("Kategorie: %s, Schwierigkeitsgrad: %s", kategorie, Utils.getSchwierigkeitBezeichnung(schwierigkeit));
    }
}
