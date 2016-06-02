package at.campus02.gang_of_four.learningapp.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import at.campus02.gang_of_four.learningapp.R;

public class KategorieAdapter extends ArrayAdapter<String> {

    public KategorieAdapter(Context context, List<String> items) {
        super(context, R.layout.kategorie_item, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) convertView;
        if (view == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            view = (TextView) vi.inflate(R.layout.kategorie_item, null);
        }

        String item = getItem(position);

        if (item != null) {
            view.setText(item);
        }
        return view;
    }
}
