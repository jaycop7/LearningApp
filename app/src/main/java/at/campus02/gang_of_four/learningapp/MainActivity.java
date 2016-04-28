package at.campus02.gang_of_four.learningapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
