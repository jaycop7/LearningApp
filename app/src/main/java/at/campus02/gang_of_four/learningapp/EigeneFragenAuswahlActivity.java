package at.campus02.gang_of_four.learningapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.List;
import java.util.Set;

import at.campus02.gang_of_four.learningapp.model.Frage;
import at.campus02.gang_of_four.learningapp.model.FrageMaintenanceModus;
import at.campus02.gang_of_four.learningapp.rest.RestDataClient;
import at.campus02.gang_of_four.learningapp.rest.restListener.FragenListener;
import at.campus02.gang_of_four.learningapp.utils.FragenAdapter;
import at.campus02.gang_of_four.learningapp.utils.Preferences;
import at.campus02.gang_of_four.learningapp.utils.Utils;

public class EigeneFragenAuswahlActivity extends AppCompatActivity {

    View progress = null;
    RestDataClient restClient = null;
    List<Frage> eigeneFragen = null;
    TextView keineFragenText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eigene_fragen_auswahl);
        progress = findViewById(R.id.eigeneFragenProgress);
        keineFragenText = (TextView) findViewById(R.id.eigeneFragenKeineFragen);
        restClient = new RestDataClient();
        loadEigeneFragen();
    }

    @Override
    public void onBackPressed() {
        Utils.navigateToMainActivity(this);
    }

    private void loadEigeneFragen() {
        progress.setVisibility(View.VISIBLE);
        Set<String> eigeneFragenIds = Preferences.getEigeneFragenIds(this);
        if (eigeneFragenIds.size() > 0) {
            restClient.getFragenByIdSet(eigeneFragenIds, new FragenListener() {
                @Override
                public void success(List<Frage> fragen) {
                    fragenReceived(fragen);
                }

                @Override
                public void error() {
                    keineFragenVerfuegbar();
                }
            });
        } else
            keineFragenVerfuegbar();
    }

    private void fragenReceived(List<Frage> fragen) {
        progress.setVisibility(View.GONE);
        this.eigeneFragen = fragen;
        if (eigeneFragen.size() > 0) {
            hideKeineFragenText();
            FragenAdapter adapter = new FragenAdapter(this, eigeneFragen);
            AbsListView gridView = (AbsListView) findViewById(R.id.eigeneFragenGrid);
            if (gridView != null) {
                gridView.setAdapter(adapter);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        frageClicked(position);
                    }
                });
            }
        } else
            keineFragenVerfuegbar();
    }

    private void frageClicked(int position) {
        Frage frage = eigeneFragen.get(position);
        Intent intent = new Intent(this, FrageBearbeitenActivity.class);
        intent.putExtra(FrageBearbeitenActivity.EXTRA_MAINTENANCE_MODE, FrageMaintenanceModus.EDIT);
        intent.putExtra(FrageBearbeitenActivity.EXTRA_FRAGE_ID, frage.getFrageID());
        startActivity(intent);
    }

    private void keineFragenVerfuegbar() {
        Utils.showLongToast(getString(R.string.eigene_fragen_fehler), this);

        keineFragenText.setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);
    }

    private void hideKeineFragenText() {
        keineFragenText.setVisibility(View.INVISIBLE);
    }
}
