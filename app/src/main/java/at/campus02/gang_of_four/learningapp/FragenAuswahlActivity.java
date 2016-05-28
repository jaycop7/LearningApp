package at.campus02.gang_of_four.learningapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.List;
import java.util.UUID;

import at.campus02.gang_of_four.learningapp.model.Frage;
import at.campus02.gang_of_four.learningapp.rest.RestDataService;
import at.campus02.gang_of_four.learningapp.rest.asyncResponse.AsyncFragenResponse;
import at.campus02.gang_of_four.learningapp.rest.asyncResponse.AsyncKategorienResponse;
import at.campus02.gang_of_four.learningapp.rest.asyncResponse.AsyncSuccessResponse;

public class FragenAuswahlActivity extends AppCompatActivity {

    //private ProgressDialog progressDialog = new ProgressDialog(FragenAuswahlActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragen_auswahl);

        final RestDataService service = new RestDataService(this);

        service.getFragen(new AsyncFragenResponse() {
            @Override
            public void processResponse(List<Frage> fragen) {
                Log.i("Jaaa", fragen.get(0).getFragetext());
                Frage newFrage = fragen.get(0);
                newFrage.setFrageID(UUID.randomUUID().toString());
                service.createFrage(newFrage, new AsyncSuccessResponse() {
                    @Override
                    public void success() {
                        Log.i("Super", "Frage erstellt");
                    }

                    @Override
                    public void error() {
                        Log.e("Neinnnn.", "Fehler");
                    }
                });
            }

            @Override
            public void error() {
                Log.i("Neiiin", "Fehler");
            }
        });
    }

    public void fragenNachKategorie(View view) {
        Intent intent = new Intent(this, FragenKategorieAuswahl.class);
        startActivity(intent);
    }
}
