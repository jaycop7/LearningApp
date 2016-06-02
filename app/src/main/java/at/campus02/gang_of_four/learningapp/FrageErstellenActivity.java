package at.campus02.gang_of_four.learningapp;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import at.campus02.gang_of_four.learningapp.model.Frage;
import at.campus02.gang_of_four.learningapp.model.Schwierigkeit;
import at.campus02.gang_of_four.learningapp.rest.RestDataService;
import at.campus02.gang_of_four.learningapp.rest.restListener.SuccessListener;
import at.campus02.gang_of_four.learningapp.utils.Utils;

public class FrageErstellenActivity extends AppCompatActivity {

    EditText frage = null;
    EditText antwort = null;
    Spinner schwierigkeit = null;
    EditText kategorie = null;
    EditText bild = null;
    TextView position = null;

    String coordinate = null;
    Frage newFrage = null;

    RestDataService service = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frage_erstellen);
        service = new RestDataService();
        populateSchwierigkeitSpinner();
        linkLayout();

        Location location = Utils.getCurrentLocation(this);
        if (location != null) {
            String latidue = String.valueOf(location.getLatitude());
            String altitude = String.valueOf(location.getAltitude());
            coordinate = latidue + "N " + altitude + "E";
        } else {
            String fail = getString(R.string.location_nicht_moeglich);
            Utils.showToast(fail, this);
            coordinate = getString(R.string.erstellen_aktuelle_position);

        }
        position.setText(coordinate);
        newFrage = new Frage();
    }

    private void linkLayout() {
        frage = (EditText) findViewById(R.id.editText_Frage);
        antwort = (EditText) findViewById(R.id.editText_Antwort);
        schwierigkeit = (Spinner) findViewById(R.id.editText_Schwierigkeit);
        kategorie = (EditText) findViewById(R.id.editText_Kategorie);
        bild = (EditText) findViewById(R.id.editText_bild);
        position = (TextView) findViewById(R.id.textView_aktuelle_position);
    }

    public void frageSpeichern(View view) {
        newFrage.setFragetext(frage.getText().toString());
        newFrage.setAntwort(antwort.getText().toString());
        newFrage.setLaengenUndBreitengrad("default");
        newFrage.setBild(bild.getText().toString());
        newFrage.setKategorie(kategorie.getText().toString());
        newFrage.setSchwierigkeitsgrad(((Schwierigkeit) schwierigkeit.getSelectedItem()).getId());
        newFrage.setFrageID("1");

        service.createFrage(newFrage, new SuccessListener() {
            @Override
            public void success() {
                frageSuccess();
            }

            @Override
            public void error() {
                showFailMessage();
            }
        });
    }

    private void frageSuccess() {
        String info = getString(R.string.frage_erstellt);
        Utils.showToast(info, this);
        Utils.navigateToMainActivity(this);
    }

    private void showFailMessage() {
        String fail = getString(R.string.frage_nicht_erstellt);
        Utils.showToast(fail, this);
    }

    private void populateSchwierigkeitSpinner() {
        List<Schwierigkeit> schwierigkeiten = Utils.getSchwierigkeiten();
        ArrayAdapter schwierigkeitAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, schwierigkeiten);
        Spinner userSpinner = (Spinner) findViewById(R.id.editText_Schwierigkeit);
        if (userSpinner != null) {
            userSpinner.setAdapter(schwierigkeitAdapter);
        }
    }
}
