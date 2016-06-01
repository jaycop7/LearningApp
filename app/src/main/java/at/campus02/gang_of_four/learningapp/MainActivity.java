package at.campus02.gang_of_four.learningapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import at.campus02.gang_of_four.learningapp.rest.TestRestService;
import at.campus02.gang_of_four.learningapp.utils.Preferences;
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

        TestRestService test = new TestRestService(this);
        test.createFrage();
        test.updateFragen();
//        test.deleteTestFragen();
        Utils.showToast("Hallo " + Preferences.getBenutzername(this), this);
//        test.getImage();
    }

    public void displayKategorien(List<String> kategorien) {

        Utils.showToast(kategorien.get(0), this);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void fragenAnzeigen(View view) {
        Intent intent = new Intent(this, FragenAuswahlActivity.class);
        startActivity(intent);
    }

    public void fragenErstellen(View view) {
        Intent intent = new Intent(this, FrageErstellenActivity.class);
        startActivity(intent);
    }

    public void einstellungenOeffnen(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

}
