package at.campus02.gang_of_four.learningapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import at.campus02.gang_of_four.learningapp.rest.RestServiceTest;
import at.campus02.gang_of_four.learningapp.utils.Utils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!Utils.isNetworkOnline(this)) {
            String text = getString(R.string.no_network);
            Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
            toast.show();
        }

        RestServiceTest test = new RestServiceTest(this);
        test.createFrage();
        test.updateFragen();
        test.deleteTestFragen();
    }

    public void fragenAnzeigen(View view) {
        Intent intent = new Intent(this, FragenAuswahlActivity.class);
        startActivity(intent);
    }

    public void fragenErstellen(View view) {
        Intent intent = new Intent(this, FrageErstellenActivity.class);
        startActivity(intent);
        //Servus
    }

    public void einstellungenOeffnen(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

}
