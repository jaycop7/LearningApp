package at.campus02.gang_of_four.learningapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import at.campus02.gang_of_four.learningapp.model.Frage;
import at.campus02.gang_of_four.learningapp.model.FragenModus;
import at.campus02.gang_of_four.learningapp.rest.RestDataService;
import at.campus02.gang_of_four.learningapp.rest.restListener.FragenListener;
import at.campus02.gang_of_four.learningapp.rest.restListener.ImageListener;
import at.campus02.gang_of_four.learningapp.utils.Preferences;
import at.campus02.gang_of_four.learningapp.utils.Utils;

public class FrageAnzeigeActivity extends AppCompatActivity {
    public static final String EXTRA_FRAGEN_MODUS = "at.campus02.gang_of_four.learningapp.ExtraFragenModus";
    public static final String EXTRA_FRAGEN_KATEGORIE = "at.campus02.gang_of_four.learningapp.ExtraFragenKategorie";

    RestDataService service = null;
    List<Frage> currentFragen = new ArrayList<>();
    FragenModus fragenModus = FragenModus.ALLE;
    String kategorie = "";
    int schwierigkeit = 0;
    int gpsUmkreis = 0;
    ImageView bildAnzeige = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frage_anzeige);
        bildAnzeige = (ImageView) findViewById(R.id.frageAnzeigeFrageFoto);
        service = new RestDataService(this);
        gpsUmkreis = Preferences.getGpsUmkreis(this);
        schwierigkeit = Preferences.getSchwierigkeit(this);
        Intent intent = getIntent();
        fragenModus = (FragenModus) intent.getSerializableExtra(EXTRA_FRAGEN_MODUS);
        kategorie = intent.getStringExtra(EXTRA_FRAGEN_KATEGORIE);
        setBild("http://www.sportwetten-blog.eu/wp-content/uploads/2011/05/sk-rapid-logo.jpg");
        Utils.showToast("Display Fragen im Modus " + fragenModus.toString() + " und Kategorie " + kategorie, this);
    }

    private void ladeFragen() {
        switch (fragenModus) {
            case ALLE:
                service.getAlleFragen(new FragenListenerImpl());
                break;
            case GPS_UMKREIS:
                Location location = Utils.getCurrentLocation(this);
                service.getFragenByGpsKoordinaten(location, gpsUmkreis, new FragenListenerImpl());
                break;
            case KATEGORIE:
                service.getFragenByKategorie(kategorie, new FragenListenerImpl());
                break;
            case SCHWIERIGKEIT:
                service.getFragenBySchwierigkeit(schwierigkeit, new FragenListenerImpl());
                break;
            default:
                service.getAlleFragen(new FragenListenerImpl());
                break;
        }
    }

    private class FragenListenerImpl implements FragenListener {

        @Override
        public void success(List<Frage> fragen) {
            fragenReceived(fragen);
        }

        @Override
        public void error() {
            displayFragenErrorMessage();
        }
    }

    private void fragenReceived(List<Frage> fragen) {
        Utils.showToast("WE RECEIVED FRAGEN! Anzahl: " + fragen.size(), this);
    }

    private void displayFragenErrorMessage() {
        Utils.showToast(getString(R.string.detail_fragen_error), this);
    }

    private void setBild(String url) {
        service.getImage(url, new ImageListener() {
            @Override
            public void success(Bitmap immage) {
                imageLoaded(immage);
            }

            @Override
            public void error() {
                imageError();
            }
        });
    }

    private void imageLoaded(Bitmap image) {
        bildAnzeige.setVisibility(View.VISIBLE);
        bildAnzeige.setImageBitmap(image);
    }

    private void imageError() {
        bildAnzeige.setVisibility(View.INVISIBLE);
    }
}
