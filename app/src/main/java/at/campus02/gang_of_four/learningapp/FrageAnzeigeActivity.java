package at.campus02.gang_of_four.learningapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import at.campus02.gang_of_four.learningapp.rest.RestDataService;
import at.campus02.gang_of_four.learningapp.rest.asyncResponse.AsyncKategorienResponse;

public class FrageAnzeigeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frage_anzeige);

    }
}
