package at.campus02.gang_of_four.learningapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class FragenAuswahlActivity extends AppCompatActivity {

    //private ProgressDialog progressDialog = new ProgressDialog(FragenAuswahlActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragen_auswahl);
    }

    public void fragenNachKategorie(View view) {
        Intent intent = new Intent(this, FragenKategorieAuswahl.class);
        startActivity(intent);
    }
}
