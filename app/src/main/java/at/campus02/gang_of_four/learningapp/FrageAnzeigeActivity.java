package at.campus02.gang_of_four.learningapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import at.campus02.gang_of_four.learningapp.model.Frage;
import at.campus02.gang_of_four.learningapp.model.FragenModus;
import at.campus02.gang_of_four.learningapp.rest.RestDataClient;
import at.campus02.gang_of_four.learningapp.rest.restListener.FragenListener;
import at.campus02.gang_of_four.learningapp.rest.restListener.ImageListener;
import at.campus02.gang_of_four.learningapp.utils.Preferences;
import at.campus02.gang_of_four.learningapp.utils.Utils;

public class FrageAnzeigeActivity extends SwipeActivity {
    public static final String EXTRA_FRAGEN_MODUS = "at.campus02.gang_of_four.learningapp.ExtraFragenModus";
    public static final String EXTRA_FRAGEN_KATEGORIE = "at.campus02.gang_of_four.learningapp.ExtraFragenKategorie";
    public static final String CURRENT_FRAGE = "CurrentFrage";
    public static final String FRAGEN = "Fragen";
    public static final String FRAGE_MODUS = "Fragemodus";
    public static final String KATEGORIE = "Kategorie";

    RestDataClient restClient = null;
    View progress = null;
    LinearLayout anzeigeLayout = null;
    TextView fragenHeader = null;
    TextView fragenSchwierigkeit = null;
    TextView frageText = null;
    TextView frageAntwort = null;
    TextView frageNavigator = null;
    ImageView bildAnzeige = null;
    Button wiederholungsButton = null;
    TextView keineFragen = null;

    List<Frage> fragen = new ArrayList<>();
    FragenModus fragenModus = FragenModus.ALLE;
    int currentFragePosition = 0;
    String kategorie = "";
    int schwierigkeit = 0;
    int gpsUmkreis = 0;
    Set<String> wiederholungsFragenIds = null;
    Set<String> eigeneFragenIds = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frage_anzeige);
        restClient = new RestDataClient();
        linkLayoutViews();
        loadPreferences();
//        if (savedInstanceState != null) {
//            currentFragePosition = savedInstanceState.getInt(CURRENT_FRAGE);
//            fragen = (List<Frage>) savedInstanceState.getSerializable(FRAGEN);
//            fragenModus = (FragenModus) savedInstanceState.getSerializable(FRAGE_MODUS);
//            kategorie = savedInstanceState.getString(KATEGORIE);
//        } else {
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            retrieveIntentExtra(intent);
            ladeFragen();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_FRAGE, currentFragePosition);
        outState.putSerializable(FRAGEN, (Serializable) fragen);
        outState.putSerializable(FRAGE_MODUS, fragenModus);
        outState.putString(KATEGORIE, kategorie);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentFragePosition = savedInstanceState.getInt(CURRENT_FRAGE);
        fragen = (List<Frage>) savedInstanceState.getSerializable(FRAGEN);
        fragenModus = (FragenModus) savedInstanceState.getSerializable(FRAGE_MODUS);
        kategorie = savedInstanceState.getString(KATEGORIE);
        displayFrage();
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
            Utils.showLongToast(getString(R.string.detail_keine_weiteren_fragen), this);
    }

    public void displayPreviousFrage() {
        if (currentFragePosition > 0) {
            currentFragePosition--;
            displayFrage();
        } else
            Utils.showLongToast(getString(R.string.detail_keine_vorherigen_fragen), this);
    }

    public void showAntwortClick(View view) {
        frageAntwort.setVisibility(View.VISIBLE);
    }

    public void wiederholungsFrageClick(View view) {
        Frage frage = getCurrentFrage();
        if (frage != null) {
            if (!wiederholungsFragenIds.contains(frage.getFrageID()))
                addCurrentToWiederholungsfragen();
            else
                removeCurrentFromWiederholungsfragen();
        }
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
                restClient.getAlleFragen(new FragenListenerImpl());
                break;
            case GPS_UMKREIS:
                Location location = Utils.getCurrentLocation(this);
                if (location == null) {
                    Utils.showShortToast(getString(R.string.detail_gps_fehler), this);
                    //              location = new Location();

                    break;
                }
                restClient.getFragenByGpsKoordinaten(location, gpsUmkreis, new FragenListenerImpl());
                break;
            case KATEGORIE:
                restClient.getFragenByKategorie(kategorie, new FragenListenerImpl());
                break;
            case SCHWIERIGKEIT:
                if (schwierigkeit == Preferences.ZUFALL_SCHWIERIGKEIT)
                    restClient.getAlleFragen(new FragenListenerImpl());
                else
                    restClient.getFragenBySchwierigkeit(schwierigkeit, new FragenListenerImpl());
                break;
            case WIEDERHOLUNG:
                restClient.getFragenByIdSet(wiederholungsFragenIds, new FragenListenerImpl());
                break;
            case EIGENE:
                restClient.getFragenByIdSet(eigeneFragenIds, new FragenListenerImpl());
                break;
            default:
                restClient.getAlleFragen(new FragenListenerImpl());
                break;
        }
    }

    private void hideLayout() {
        anzeigeLayout.setVisibility(View.INVISIBLE);
    }

    private void showLayout() {
        anzeigeLayout.setVisibility(View.VISIBLE);
    }

    private Frage getCurrentFrage() {
        if (fragen.size() > 0)
            return fragen.get(currentFragePosition);
        return null;
    }

    private void addCurrentToWiederholungsfragen() {
        Frage frage = getCurrentFrage();
        if (frage != null) {
            wiederholungsFragenIds.add(frage.getFrageID());
            saveWiederholungsfragenIds();
            Utils.showShortToast(getString(R.string.detail_frage_gemerkt), this);
            updateWiederholungsButton();
        }
    }

    private void removeCurrentFromWiederholungsfragen() {
        Frage frage = getCurrentFrage();
        if (frage != null) {
            wiederholungsFragenIds.remove(frage.getFrageID());
            saveWiederholungsfragenIds();
            Utils.showShortToast(getString(R.string.detail_frage_gemerkt_aufgehoben), this);
            updateWiederholungsButton();
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
        fragenSchwierigkeit = (TextView) findViewById(R.id.frageAnzeigeSchwierigkeit);
        frageText = (TextView) findViewById(R.id.frageAnzeigeFrage);
        frageAntwort = (TextView) findViewById(R.id.frageAnzeigeAntwort);
        frageNavigator = (TextView) findViewById(R.id.frageAnzeigeNavigator);
        bildAnzeige = (ImageView) findViewById(R.id.frageAnzeigeFrageFoto);
        wiederholungsButton = (Button) findViewById(R.id.frageAnzeigeWiederholungsfrage);
        keineFragen = (TextView) findViewById(R.id.frageAnzeigenKeineFragen);
    }

    private void loadPreferences() {
        gpsUmkreis = Preferences.getGpsUmkreis(this);
        schwierigkeit = Preferences.getSchwierigkeit(this);
        wiederholungsFragenIds = new HashSet<>(Preferences.getWiederholungsFragenIds(this));
        eigeneFragenIds = new HashSet<>(Preferences.getEigeneFragenIds(this));
    }

    private class FragenListenerImpl implements FragenListener {

        @Override
        public void success(List<Frage> fragenList) {
            fragenReceived(fragenList);
        }

        @Override
        public void error() {
            displayKeineFragen();
        }

    }

    private void fragenReceived(List<Frage> fragenList) {
        progress.setVisibility(View.INVISIBLE);
        if (fragenModus != FragenModus.EIGENE && fragenModus != FragenModus.WIEDERHOLUNG)
            this.fragen = filterFragen(fragenList);
        else
            this.fragen = fragenList;
        if (fragen != null && fragen.size() > 0) {
            hideNoFragen();
            displayFrage();
        } else {
            displayKeineFragen();
        }
    }

    /**
     * Filtert die Fragen nach der Max Anzahl anzuzeigender Fragen und mischt diese durch.
     */
    private List<Frage> filterFragen(List<Frage> fragen) {
        int maxFragen = Preferences.getMaxFragen(this);
        Collections.shuffle(fragen, new Random(System.nanoTime()));
        return fragen.size() > maxFragen ? new ArrayList<>(fragen.subList(0, maxFragen)) : fragen;
    }

    private void hideNoFragen() {
        keineFragen.setVisibility(View.GONE);
    }

    private void displayFrage() {
        hideLayout();
        Frage frage = getCurrentFrage();
        if (frage != null) {
            fragenHeader.setText(frage.getKategorie());
            fragenSchwierigkeit.setText(getString(R.string.einstellungen_schwierigkeit) + ": " + Utils.getSchwierigkeitBezeichnung(frage.getSchwierigkeitsgrad()));
            frageText.setText(frage.getFragetext());
            frageAntwort.setText(frage.getAntwort());
            frageAntwort.setVisibility(View.GONE);
            updateWiederholungsButton();
            updateNavigator();
            if (frage.getBild() != null && !frage.getBild().isEmpty()) {
                setBild(frage.getBild());
            } else
                imageError();
        } else
            displayKeineFragen();
    }

    private void updateWiederholungsButton() {
        Frage frage = getCurrentFrage();
        if (wiederholungsFragenIds.contains(frage.getFrageID())) {
            Drawable dw = getApplicationContext().getResources().getDrawable(R.drawable.ic_star_black_24dp); //FIXME Deprecated
            wiederholungsButton.setCompoundDrawablesWithIntrinsicBounds(dw, null, null, null);
            wiederholungsButton.setText(getString(R.string.detail_wiederholen_gemerkt));
        } else {
            Drawable dw = getApplicationContext().getResources().getDrawable(R.drawable.ic_star_border_black_24dp);
            wiederholungsButton.setCompoundDrawablesWithIntrinsicBounds(dw, null, null, null);
            wiederholungsButton.setText(getString(R.string.detail_wiederholen));
        }
    }

    private void updateNavigator() {
        if (fragen.size() == 0)
            frageNavigator.setText("Frage 0 von 0");
        else
            frageNavigator.setText(String.format("Frage %s von %s", currentFragePosition + 1, fragen.size()));
    }

    private void displayKeineFragen() {
        progress.setVisibility(View.INVISIBLE);
        updateNavigator();
        keineFragen.setVisibility(View.VISIBLE);
    }

    private void setBild(String url) {
        progress.setVisibility(View.VISIBLE);
        restClient.getImage(url, new ImageListener() {
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
        progress.setVisibility(View.INVISIBLE);
        showLayout();
        bildAnzeige.setVisibility(View.VISIBLE);
        bildAnzeige.setImageBitmap(image);
    }

    private void imageError() {
        bildAnzeige.setVisibility(View.GONE);
        showLayout();
    }

    public void frageTeilen(View view) {
        if (getCurrentFrage() != null) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.detail_teilen_einleitung_nachricht));
            String bild = "";
            if (!(getCurrentFrage().getBild() == null || getCurrentFrage().getBild().isEmpty())) {
                bild = " Hier ein Link zum Bild: " + getCurrentFrage().getBild();
            }
            String inhalt = "Weißt du die Antwort auf diese Frage? '" + getCurrentFrage().getFragetext() + "'" + bild + " Liebe Grüße " + Preferences.getBenutzername(this);
            shareIntent.putExtra(Intent.EXTRA_TEXT, inhalt);
            startActivity(Intent.createChooser(shareIntent, "Teilen mit ..."));
        }
    }
}
