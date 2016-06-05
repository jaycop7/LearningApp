package at.campus02.gang_of_four.learningapp;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import at.campus02.gang_of_four.learningapp.model.Frage;
import at.campus02.gang_of_four.learningapp.model.FrageMaintenanceModus;
import at.campus02.gang_of_four.learningapp.model.Schwierigkeit;
import at.campus02.gang_of_four.learningapp.rest.RestDataClient;
import at.campus02.gang_of_four.learningapp.rest.restListener.FrageListener;
import at.campus02.gang_of_four.learningapp.rest.restListener.SaveFrageListener;
import at.campus02.gang_of_four.learningapp.utils.Preferences;
import at.campus02.gang_of_four.learningapp.utils.Utils;

public class FrageBearbeitenActivity extends AppCompatActivity {
    public static final String EXTRA_MAINTENANCE_MODE = "at.campus02.gang_of_four.learningapp.FrageMaintenanceModus";
    public static final String EXTRA_FRAGE_ID = "at.campus02.gang_of_four.learningapp.FrageId";

    EditText frageView = null;
    EditText antwortView = null;
    Spinner schwierigkeitView = null;
    EditText kategorieView = null;
    EditText bildView = null;
    TextView positionView = null;
    Button speichernButton = null;

    Frage currentFrage = null;
    FrageMaintenanceModus maintenanceModus = null;
    String editFrageId = null;

    RestDataClient restClient = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frage_bearbeiten);
        restClient = new RestDataClient();
        populateSchwierigkeitSpinner();
        linkLayoutViews();


        Intent intent = getIntent();
        retrieveIntentExtra(intent);
        if (maintenanceModus == FrageMaintenanceModus.CREATE) {
            speichernButton.setText(getText(R.string.frage_bearbeiten_erstellen));
            currentFrage = new Frage();
            fillCurrentLocation();
        } else {
            loadEditFrage();
            speichernButton.setText(getText(R.string.frage_bearbeiten_speichern));
        }
    }

    private void fillCurrentLocation() {
        Location location = Utils.getCurrentLocation(this);
        String coordinate;
        if (location != null) {
            String latidue = String.valueOf(location.getLatitude());
            String altitude = String.valueOf(location.getAltitude());
            coordinate = latidue + ";" + altitude;
        } else {
            String fail = getString(R.string.location_nicht_moeglich);
            Utils.showLongToast(fail, this);
            coordinate = getString(R.string.frage_bearbeiten_aktuelle_position);

        }
        positionView.setText(coordinate);
    }

    private void linkLayoutViews() {
        frageView = (EditText) findViewById(R.id.editText_Frage);
        antwortView = (EditText) findViewById(R.id.editText_Antwort);
        schwierigkeitView = (Spinner) findViewById(R.id.editText_Schwierigkeit);
        kategorieView = (EditText) findViewById(R.id.editText_Kategorie);
        bildView = (EditText) findViewById(R.id.editText_bild);
        positionView = (TextView) findViewById(R.id.textView_aktuelle_position);
        speichernButton = (Button) findViewById(R.id.maintenanceSpeichernButton);
    }

    private void retrieveIntentExtra(Intent intent) {
        maintenanceModus = (FrageMaintenanceModus) intent.getSerializableExtra(EXTRA_MAINTENANCE_MODE);
        editFrageId = intent.getStringExtra(EXTRA_FRAGE_ID);
    }

    private void loadEditFrage() {
        if (!editFrageId.isEmpty())
            restClient.getFrage(editFrageId, new FrageListener() {
                @Override
                public void success(Frage frage) {
                    editFrageGeladen(frage);
                }

                @Override
                public void error() {
                    editFrageError();
                }
            });
        else
            editFrageError();
    }

    private void editFrageGeladen(Frage frage) {
        currentFrage = frage;
        frageView.setText(frage.getFragetext());
        antwortView.setText(frage.getAntwort());
        kategorieView.setText(frage.getKategorie());
        positionView.setText(frage.getLaengenUndBreitengrad());
        int schwierigkeitId = frage.getSchwierigkeitsgrad();
        int index = 0;
        List<Schwierigkeit> schwierigkeiten = Utils.getSchwierigkeiten();
        for (Schwierigkeit s : schwierigkeiten) {
            if (s.getId() == schwierigkeitId)
                break;
            index++;
        }
        schwierigkeitView.setSelection(index);
        bildView.setText(frage.getBild());
    }

    private void editFrageError() {
        Utils.showLongToast(getString(R.string.frage_bearbeiten_error_loading), this);
    }

    public void frageSpeichern(View view) {
        if (frageView.getText() == null || frageView.getText().toString().isEmpty()) {
            showFailMessage("Keine 'Frage'");
            return;
        }
        currentFrage.setFragetext(frageView.getText().toString());

        if (antwortView.getText() == null || antwortView.getText().toString().isEmpty()) {
            showFailMessage("Keine 'Antwort'");
            return;
        }
        currentFrage.setAntwort(antwortView.getText().toString());

        if (bildView.getText() != null || !bildView.getText().toString().isEmpty()) {
            currentFrage.setBild(bildView.getText().toString());
        }

        currentFrage.setLaengenUndBreitengrad(positionView.getText().toString());

        if (kategorieView.getText() == null || kategorieView.getText().toString().isEmpty()) {
            showFailMessage("Keine 'Kategorie'");
            return;
        }
        currentFrage.setKategorie(kategorieView.getText().toString());
        currentFrage.setSchwierigkeitsgrad(((Schwierigkeit) schwierigkeitView.getSelectedItem()).getId());
        if (maintenanceModus == FrageMaintenanceModus.CREATE) {
            restClient.createFrage(currentFrage, new FrageErstellenListenerImpl());
        } else {
            restClient.updateFrage(currentFrage, new FrageErstellenListenerImpl());
        }
    }

    private class FrageErstellenListenerImpl implements SaveFrageListener {

        @Override
        public void success(String guid) {
            frageSuccess(guid);
        }

        @Override
        public void error() {
            showFailMessage();
        }
    }

    private void frageSuccess(String guid) {
        if (maintenanceModus == FrageMaintenanceModus.CREATE) {
            addEigeneFrageId(guid);
        }
        Utils.navigateToMainActivity(this);
        String info = getString(R.string.frage_gespeichert);
        Utils.showLongToast(info, this);
    }

    private void addEigeneFrageId(String guid) {
        Set<String> eigeneFragenIds = new HashSet<>(Preferences.getEigeneFragenIds(this));
        eigeneFragenIds.add(guid);
        Preferences.setEigeneFragenIds(eigeneFragenIds, this);
    }

    private void showFailMessage() {
        String fail = getString(R.string.frage_nicht_erstellt);
        Utils.showLongToast(fail, this);
    }

    private void showFailMessage(String text) {
        String fail = getString(R.string.frage_bearbeiten_validation_fehler) + ": " + text;
        Utils.showLongToast(fail, this);
    }

    private void populateSchwierigkeitSpinner() {
        List<Schwierigkeit> schwierigkeiten = Utils.getSchwierigkeiten();
        ArrayAdapter schwierigkeitAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, schwierigkeiten);
        Spinner userSpinner = (Spinner) findViewById(R.id.editText_Schwierigkeit);
        if (userSpinner != null) {
            userSpinner.setAdapter(schwierigkeitAdapter);
        }
    }

    public void setLocalPosition(View view) {

        fillCurrentLocation();

    }
}
