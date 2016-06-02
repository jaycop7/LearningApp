package at.campus02.gang_of_four.learningapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import at.campus02.gang_of_four.learningapp.model.Frage;
import at.campus02.gang_of_four.learningapp.model.FragenModus;
import at.campus02.gang_of_four.learningapp.rest.RestDataService;
import at.campus02.gang_of_four.learningapp.rest.restListener.FragenListener;
import at.campus02.gang_of_four.learningapp.rest.restListener.ImageListener;
import at.campus02.gang_of_four.learningapp.utils.Preferences;
import at.campus02.gang_of_four.learningapp.utils.Utils;

public class FrageAnzeigeActivity extends SwipeActivity {
    public static final String EXTRA_FRAGEN_MODUS = "at.campus02.gang_of_four.learningapp.ExtraFragenModus";
    public static final String EXTRA_FRAGEN_KATEGORIE = "at.campus02.gang_of_four.learningapp.ExtraFragenKategorie";

    RestDataService service = null;
    View progress = null;
    LinearLayout anzeigeLayout = null;
    TextView fragenHeader = null;
    TextView frageText = null;
    TextView frageAntwort = null;
    TextView frageNavigator = null;
    ImageView bildAnzeige = null;

    List<Frage> fragen = new ArrayList<>();
    FragenModus fragenModus = FragenModus.ALLE;
    int currentFragePosition = 0;
    String kategorie = "";
    int schwierigkeit = 0;
    int gpsUmkreis = 0;
    Set<String> wiederholungsFragenIds = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frage_anzeige);
        service = new RestDataService();
        linkLayoutViews();
        loadPreferences();
        Intent intent = getIntent();
        retrieveIntentExtra(intent);
        ladeFragen();
    }


    @Override
    protected void swipePrevious() {
        displayPreviousFrage();
    }

    @Override
    protected void swipeNext() {
        displayNextFrage();
    }

    public void displayNextFrage() {
        if (currentFragePosition < fragen.size() - 1) {
            currentFragePosition++;
            displayFrage();
        } else
            Utils.showToast(getString(R.string.detail_keine_weiteren_fragen), this);
    }

    public void displayPreviousFrage() {
        if (currentFragePosition > 0) {
            currentFragePosition--;
            displayFrage();
        } else
            Utils.showToast(getString(R.string.detail_keine_vorherigen_fragen), this);
    }

    public void showAntwortClick(View view) {
        frageAntwort.setVisibility(View.VISIBLE);
    }

    public void wiederholungsFrageClick(View view) {
        addCurrentToWiederholungsfragen();
        Utils.showToast(getString(R.string.detail_frage_gemerkt), this);
    }

    @Override
    public void onBackPressed() {
        Utils.navigateToMainActivity(this);
    }

    private void ladeFragen() {
        progress.setVisibility(View.VISIBLE);
        hideLayout();
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
            case WIEDERHOLUNG:
                service.getFragenByIdSet(wiederholungsFragenIds, new FragenListenerImpl());
                break;
            default:
                service.getAlleFragen(new FragenListenerImpl());
                break;
        }
    }

    private void hideLayout() {
        anzeigeLayout.setVisibility(View.INVISIBLE);
    }

    private void showLayout() {
        anzeigeLayout.setVisibility(View.VISIBLE);
    }

    private void addCurrentToWiederholungsfragen() {
        Frage frage = fragen.get(currentFragePosition);
        if (!wiederholungsFragenIds.contains(frage.getFrageID())) {
            wiederholungsFragenIds.add(frage.getFrageID());
            saveWiederholungsfragenIds();
        }
    }

    private void removeCurrentFromWiederholungsfragen() {
        Frage frage = fragen.get(currentFragePosition);
        if (wiederholungsFragenIds.contains(frage.getFrageID())) {
            wiederholungsFragenIds.remove(frage.getFrageID());
            saveWiederholungsfragenIds();
        }
    }

    private void saveWiederholungsfragenIds() {
        Preferences.setWiederholungsFragenIds(wiederholungsFragenIds, this);
    }

    private void retrieveIntentExtra(Intent intent) {
        fragenModus = (FragenModus) intent.getSerializableExtra(EXTRA_FRAGEN_MODUS);
        kategorie = intent.getStringExtra(EXTRA_FRAGEN_KATEGORIE);
    }

    private void linkLayoutViews() {
        progress = findViewById(R.id.fragenAnzeigeProgress);
        anzeigeLayout = (LinearLayout) findViewById(R.id.frageAnzeigeLayout);
        fragenHeader = (TextView) findViewById(R.id.frageAnzeigeHeader);
        frageText = (TextView) findViewById(R.id.frageAnzeigeFrage);
        frageAntwort = (TextView) findViewById(R.id.frageAnzeigeAntwort);
        frageNavigator = (TextView) findViewById(R.id.frageAnzeigeNavigator);
        bildAnzeige = (ImageView) findViewById(R.id.frageAnzeigeFrageFoto);
    }

    private void loadPreferences() {
        gpsUmkreis = Preferences.getGpsUmkreis(this);
        schwierigkeit = Preferences.getSchwierigkeit(this);
        wiederholungsFragenIds = Preferences.getWiederholungsFragenIds(this);
    }

    private class FragenListenerImpl implements FragenListener {

        @Override
        public void success(List<Frage> fragenList) {
            fragenReceived(fragenList);
        }

        @Override
        public void error() {
            displayFragenErrorMessage();
        }
    }

    private void fragenReceived(List<Frage> fragenList) {
        progress.setVisibility(View.INVISIBLE);
        this.fragen = fragenList;
        Utils.showToast(String.format("%s %s.", fragen.size(), fragen.size() == 1 ? getString(R.string.frage_geladen) : getString(R.string.fragen_geladen)), this);
        if (fragen != null && fragen.size() > 0)
            displayFrage();
    }

    private void displayFrage() {
        hideLayout();
        Frage frage = fragen.get(currentFragePosition);
        fragenHeader.setText(String.format("%s (%s)", frage.getKategorie(), frage.getSchwierigkeitsgrad()));
        frageText.setText(frage.getFragetext());
        frageAntwort.setText(frage.getAntwort());
        frageAntwort.setVisibility(View.INVISIBLE);
        updateNavigator();
        if (frage.getBild() != null && !frage.getBild().isEmpty()) {
            setBild(frage.getBild());
        } else
            showLayout();
    }

    private void updateNavigator() {
        frageNavigator.setText(String.format("Frage %s von %s", currentFragePosition + 1, fragen.size()));
    }


    private void displayFragenErrorMessage() {
        progress.setVisibility(View.INVISIBLE);
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
        showLayout();
    }

    private void imageError() {
        bildAnzeige.setVisibility(View.INVISIBLE);
        showLayout();
    }
}
