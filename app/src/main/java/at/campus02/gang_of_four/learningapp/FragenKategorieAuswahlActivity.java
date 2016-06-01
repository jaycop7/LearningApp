package at.campus02.gang_of_four.learningapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import at.campus02.gang_of_four.learningapp.model.FragenModus;
import at.campus02.gang_of_four.learningapp.rest.RestDataService;
import at.campus02.gang_of_four.learningapp.rest.restListener.KategorienListener;
import at.campus02.gang_of_four.learningapp.utils.Utils;

public class FragenKategorieAuswahlActivity extends AppCompatActivity {

    RestDataService service = null;
    List<String> kategorien = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragen_kategorie_auswahl);
        service = new RestDataService(this);
        loadKategorien();
    }


    private void loadKategorien() {
        service.getKategorien(new KategorienListener() {
            @Override
            public void success(List<String> kategorienListe) {
                populateKategorien(kategorienListe);
            }

            @Override
            public void error() {
                showErrorMessage();
            }
        });
    }

    private void populateKategorien(List<String> kategorieListe) {
        this.kategorien = kategorieListe;
        ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.kategorie_item, kategorien);
        GridView gridView = (GridView) findViewById(R.id.kategorieAuswahlGrid);
        if (gridView != null) {
            gridView.setAdapter(adapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    kategorieClicked(position);
                }
            });
        }
    }

    private void kategorieClicked(int position) {
        String kategorie = kategorien.get(position);
//        Utils.showToast("Start Fragen der Kategorie: " + kategorie, this);

        Intent intent = new Intent(this, FrageAnzeigeActivity.class);
        intent.putExtra(FrageAnzeigeActivity.EXTRA_FRAGEN_MODUS, FragenModus.KATEGORIE);
        intent.putExtra(FrageAnzeigeActivity.EXTRA_FRAGEN_KATEGORIE, kategorie);
        startActivity(intent);
    }

    private void showErrorMessage() {
        Utils.showToast(getString(R.string.auswahl_kategorien_fehler), this);
    }
}
