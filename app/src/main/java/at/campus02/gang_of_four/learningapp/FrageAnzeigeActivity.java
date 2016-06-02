package at.campus02.gang_of_four.learningapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
    RelativeLayout progress = null;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frage_anzeige);
        registerSwipeControl();
        service = new RestDataService();
        linkLayout();
        gpsUmkreis = Preferences.getGpsUmkreis(this);
        schwierigkeit = Preferences.getSchwierigkeit(this);
        Intent intent = getIntent();
        retrieveIntentExtra(intent);
        ladeFragen();
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

    private void registerSwipeControl() {
        findViewById(R.id.frageAnzeigeRootlayout).setOnTouchListener(new SwipeTouchListener(this));
    }

    private void retrieveIntentExtra(Intent intent) {
        fragenModus = (FragenModus) intent.getSerializableExtra(EXTRA_FRAGEN_MODUS);
        kategorie = intent.getStringExtra(EXTRA_FRAGEN_KATEGORIE);
    }

    private void linkLayout() {
        progress = (RelativeLayout) findViewById(R.id.fragenKategorieProgress);
        anzeigeLayout = (LinearLayout) findViewById(R.id.frageAnzeigeLayout);
        fragenHeader = (TextView) findViewById(R.id.frageAnzeigeHeader);
        frageText = (TextView) findViewById(R.id.frageAnzeigeFrage);
        frageAntwort = (TextView) findViewById(R.id.frageAnzeigeAntwort);
        frageNavigator = (TextView) findViewById(R.id.frageAnzeigeNavigator);
        bildAnzeige = (ImageView) findViewById(R.id.frageAnzeigeFrageFoto);
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
        if (frage.getBild() != null) {
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

    /**
     * Detects left and right swipes across a view.
     */
    private class SwipeTouchListener implements View.OnTouchListener {

        private final GestureDetector gestureDetector;

        public SwipeTouchListener(Context context) {
            gestureDetector = new GestureDetector(context, new GestureListener());
        }

        public void onSwipeLeft() {
            displayNextFrage();
        }

        public void onSwipeRight() {
            displayPreviousFrage();
        }

        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }

        private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

            private static final int SWIPE_DISTANCE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float distanceX = e2.getX() - e1.getX();
                float distanceY = e2.getY() - e1.getY();
                if (Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (distanceX > 0)
                        onSwipeRight();
                    else
                        onSwipeLeft();
                    return true;
                }
                return false;
            }
        }
    }
}
