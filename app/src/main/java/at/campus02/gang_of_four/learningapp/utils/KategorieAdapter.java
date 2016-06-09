package at.campus02.gang_of_four.learningapp.utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import at.campus02.gang_of_four.learningapp.R;
import at.campus02.gang_of_four.learningapp.model.KategorieWithAnzahl;

public class KategorieAdapter extends ArrayAdapter<KategorieWithAnzahl> {

    public KategorieAdapter(Context context, List<KategorieWithAnzahl> items) {
        super(context, R.layout.kategorie_item, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        KategorieHolder holder;

        if (view == null) {
            LayoutInflater vi;
            vi = ((Activity) getContext()).getLayoutInflater();
            view = vi.inflate(R.layout.kategorie_item, parent, false);

            holder = new KategorieHolder();
            holder.kategorie = (TextView) view.findViewById(R.id.kategorieItemText);
            holder.anzahl = (TextView) view.findViewById(R.id.kategorieItemAnzahl);

            view.setTag(holder);
        } else {
            holder = (KategorieHolder) view.getTag();
        }

        KategorieWithAnzahl item = getItem(position);

        if (item != null) {
            holder.kategorie.setText(item.getKategorie());
            holder.anzahl.setText(formatAnzahlAnzeige(item.getAnzahl()));
        }
        return view;
    }

    static class KategorieHolder {
        TextView kategorie;
        TextView anzahl;
    }

    private String formatAnzahlAnzeige(int anzahl) {
        if (anzahl == 1)
            return String.format("%s Frage", anzahl);
        else
            return String.format("%s Fragen", anzahl);
    }
}
