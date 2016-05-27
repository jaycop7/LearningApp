package at.campus02.gang_of_four.learningapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import at.campus02.gang_of_four.learningapp.rest.RestDataService;

public class FragenAuswahlActivity extends AppCompatActivity {

    //private ProgressDialog progressDialog = new ProgressDialog(FragenAuswahlActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragen_auswahl);
//        RestDataService service = new RestDataService(this);
//        service.getKategorien();
//        service.getFragen();
    }

    public void fragenNachKategorie(View view) {
        getSystemService(ACCOUNT_SERVICE);
        Intent intent = new Intent(this, FragenKategorieAuswahl.class);
        startActivity(intent);
    }
}
