package at.campus02.gang_of_four.learningapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import at.campus02.gang_of_four.learningapp.model.FragenModus;

public class FragenAuswahlActivity extends AppCompatActivity {

    //private ProgressDialog progressDialog = new ProgressDialog(FragenAuswahlActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragen_auswahl);
    }

    public void alleFragenClick(View view) {
        Intent intent = new Intent(this, FrageAnzeigeActivity.class);
        intent.putExtra(FrageAnzeigeActivity.EXTRA_FRAGEN_MODUS, FragenModus.ALLE);
        startActivity(intent);
    }

    public void fragenNachGpsUmkreisClick(View view) {
        Intent intent = new Intent(this, FrageAnzeigeActivity.class);
        intent.putExtra(FrageAnzeigeActivity.EXTRA_FRAGEN_MODUS, FragenModus.GPS_UMKREIS);
        startActivity(intent);
    }

    public void fragenNachSchwierigkeitClick(View view) {
        Intent intent = new Intent(this, FrageAnzeigeActivity.class);
        intent.putExtra(FrageAnzeigeActivity.EXTRA_FRAGEN_MODUS, FragenModus.SCHWIERIGKEIT);
        startActivity(intent);
    }

    public void eigeneFragenClick(View view) {
        //TODO Phips Activity aufrufen
    }

    public void wiederholungsFragenClick(View view) {
        Intent intent = new Intent(this, FrageAnzeigeActivity.class);
        intent.putExtra(FrageAnzeigeActivity.EXTRA_FRAGEN_MODUS, FragenModus.WIEDERHOLUNG);
        startActivity(intent);
    }

    public void fragenNachKategorieClick(View view) {
        Intent intent = new Intent(this, FragenKategorieAuswahlActivity.class);
        startActivity(intent);
    }
}
