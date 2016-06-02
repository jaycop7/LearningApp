package at.campus02.gang_of_four.learningapp;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import at.campus02.gang_of_four.learningapp.model.Frage;
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

        frage = (EditText) findViewById(R.id.editText_Frage);
        antwort = (EditText) findViewById(R.id.editText_Antwort);
        //   schwierigkeit = (Spinner)findViewById(R.id.schwierigkeit);

        bild = (EditText) findViewById(R.id.editText_bild);
        position = (TextView) findViewById(R.id.textView_aktuelle_position);

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

    public void frageSpeichern(View view) {
        newFrage.setFragetext(frage.getText().toString());
        newFrage.setAntwort(antwort.getText().toString());
        newFrage.setLaengenUndBreitengrad("default");
        newFrage.setBild(bild.getText().toString());
        newFrage.setKategorie("tbd");
        newFrage.setSchwierigkeitsgrad(1);
        newFrage.setFrageID("1");

        service.createFrage(newFrage, new SuccessListener() {
            @Override
            public void success() {
                showSucessMessage();
            }

            @Override
            public void error() {
                showFailMessage();
            }
        });

        Utils.navigateToMainActivity(this);
    }

    private void showSucessMessage() {
        String info = getString(R.string.frage_erstellt);
        Utils.showToast(info, this);
    }

    private void showFailMessage() {
        String fail = getString(R.string.frage_nicht_erstellt);
        Utils.showToast(fail, this);
    }
}
