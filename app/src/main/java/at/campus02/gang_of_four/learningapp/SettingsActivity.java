package at.campus02.gang_of_four.learningapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

import at.campus02.gang_of_four.learningapp.model.Schwierigkeit;
import at.campus02.gang_of_four.learningapp.utils.Preferences;
import at.campus02.gang_of_four.learningapp.utils.Utils;

public class SettingsActivity extends AppCompatActivity {

    EditText maxFragen = null;
    EditText gpsUmkreis = null;
    EditText benutzername = null;
    Spinner schwierigkeit = null;
    EditText fragenUeberspringen = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        maxFragen = (EditText)findViewById(R.id.maxFragen);
        gpsUmkreis = (EditText)findViewById(R.id.gpsUmkreis);
        benutzername = (EditText)findViewById(R.id.benutzername);
        schwierigkeit = (Spinner)findViewById(R.id.schwierigkeit);
        fragenUeberspringen = (EditText)findViewById(R.id.fragenUeberspringen);
        populateSchwierigkeitSpinner();
        einstellungenLaden();
    }

    public void einstellungenSpeichern(View view) {
        Preferences.setMaxFragen(Integer.valueOf(maxFragen.getText().toString()),this);
        Preferences.setGpsUmkreis(Integer.valueOf(gpsUmkreis.getText().toString()), this);
        Preferences.setBenutzername(benutzername.getText().toString(), this);
        Preferences.setFragenUeberspringenAnzahl(Integer.valueOf(fragenUeberspringen.getText().toString()), this);
        Preferences.setSchwierigkeit(((Schwierigkeit) schwierigkeit.getSelectedItem()).getId(), this);

        //redirect to main activity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void einstellungenLaden(){
        maxFragen.setText(String.valueOf(Preferences.getMaxFragen(this)));
        gpsUmkreis.setText(String.valueOf(Preferences.getGpsUmkreis(this)));
        benutzername.setText(Preferences.getBenutzername(this));
        fragenUeberspringen.setText(String.valueOf(Preferences.getFragenUeberspringenAnzahl(this)));
        int schwierigkeitId = Preferences.getSchwierigkeit(this);
        int index = 0;
        List<Schwierigkeit> schwierigkeiten = Utils.getSchwierigkeiten();
        for (Schwierigkeit s : schwierigkeiten) {
            if(s.getId() == schwierigkeitId)
                break;
            index++;
        }
        schwierigkeit.setSelection(index);
    }

    private void populateSchwierigkeitSpinner() {
        List<Schwierigkeit> schwierigkeiten = Utils.getSchwierigkeiten();
        ArrayAdapter schwierigkeitAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, schwierigkeiten);
        Spinner userSpinner = (Spinner) findViewById(R.id.schwierigkeit);
        userSpinner.setAdapter(schwierigkeitAdapter);
    }

}
