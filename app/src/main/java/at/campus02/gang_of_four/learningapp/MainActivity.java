package at.campus02.gang_of_four.learningapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import at.campus02.gang_of_four.learningapp.model.FrageMaintenanceModus;
import at.campus02.gang_of_four.learningapp.utils.Utils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkNetwork();

        Log.e("startup", "start");
        Utils.getCurrentLocation(this);
    }

    private void checkNetwork() {
        if (!Utils.isNetworkOnline(this)) {
            String text = getString(R.string.no_network);
            Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
            toast.show();
        }
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
        Intent intent = new Intent(this, FrageBearbeitenActivity.class);
        intent.putExtra(FrageBearbeitenActivity.EXTRA_MAINTENANCE_MODE, FrageMaintenanceModus.CREATE);
        startActivity(intent);
    }

    public void eigeneFragen(View view) {
        Intent intent = new Intent(this, EigeneFragenAuswahlActivity.class);
        startActivity(intent);
    }

    public void einstellungenOeffnen(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

}
