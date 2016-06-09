package at.campus02.gang_of_four.learningapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import at.campus02.gang_of_four.learningapp.model.Frage;
import at.campus02.gang_of_four.learningapp.model.FrageMaintenanceModus;
import at.campus02.gang_of_four.learningapp.model.Schwierigkeit;
import at.campus02.gang_of_four.learningapp.rest.RestDataClient;
import at.campus02.gang_of_four.learningapp.rest.restListener.FrageListener;
import at.campus02.gang_of_four.learningapp.rest.restListener.SaveFrageListener;
import at.campus02.gang_of_four.learningapp.utils.Preferences;
import at.campus02.gang_of_four.learningapp.utils.Utils;

public class FrageBearbeitenActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {
    public static final String EXTRA_MAINTENANCE_MODE = "at.campus02.gang_of_four.learningapp.FrageMaintenanceModus";
    public static final String EXTRA_FRAGE_ID = "at.campus02.gang_of_four.learningapp.FrageId";
    public static final float MAP_ZOOM_LEVEL = 4f;

    EditText frageView = null;
    EditText antwortView = null;
    Spinner schwierigkeitView = null;
    EditText kategorieView = null;
    EditText bildView = null;
    Button speichernButton = null;

    Frage currentFrage = null;
    FrageMaintenanceModus maintenanceModus = null;
    String editFrageId = null;

    RestDataClient restClient = null;
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap map;
    private MarkerOptions currentMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frage_bearbeiten);
        restClient = new RestDataClient();
        populateSchwierigkeitSpinner();
        linkLayoutViews();

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        retrieveIntentExtra(intent);
        if (maintenanceModus == FrageMaintenanceModus.CREATE) {
            speichernButton.setText(getText(R.string.frage_bearbeiten_erstellen));
            currentFrage = new Frage();
        } else {
            loadEditFrage();
            speichernButton.setText(getText(R.string.frage_bearbeiten_speichern));
        }
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                addMarker(latLng);
            }
        });
        if (maintenanceModus == FrageMaintenanceModus.CREATE)
            fillCurrentLocation();
    }

    private void addMarker(LatLng latLng) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
//        markerOptions.title("Your Position");
        currentMarker = markerOptions;
        map.clear();
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, MAP_ZOOM_LEVEL));
        map.addMarker(markerOptions);
    }

    private void fillCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location != null) {
            addMarker(new LatLng(location.getLatitude(), location.getLongitude()));
        } else {
            String fail = getString(R.string.location_nicht_moeglich);
            Utils.showLongToast(fail, this);
            addMarker(new LatLng(47.0880728, 15.439806500000032));
        }
    }

    private void linkLayoutViews() {
        frageView = (EditText) findViewById(R.id.editText_Frage);
        antwortView = (EditText) findViewById(R.id.editText_Antwort);
        schwierigkeitView = (Spinner) findViewById(R.id.editText_Schwierigkeit);
        kategorieView = (EditText) findViewById(R.id.editText_Kategorie);
        bildView = (EditText) findViewById(R.id.editText_bild);
        speichernButton = (Button) findViewById(R.id.maintenanceSpeichernButton);
    }

    private void retrieveIntentExtra(Intent intent) {
        maintenanceModus = (FrageMaintenanceModus) intent.getSerializableExtra(EXTRA_MAINTENANCE_MODE);
        editFrageId = intent.getStringExtra(EXTRA_FRAGE_ID);
    }

    private void loadEditFrage() {
        if (!editFrageId.isEmpty())
            restClient.getFrage(editFrageId, new FrageListener() {
                @Override
                public void success(Frage frage) {
                    editFrageGeladen(frage);
                }

                @Override
                public void error() {
                    editFrageError();
                }
            });
        else
            editFrageError();
    }

    private void editFrageGeladen(Frage frage) {
        currentFrage = frage;
        frageView.setText(frage.getFragetext());
        antwortView.setText(frage.getAntwort());
        kategorieView.setText(frage.getKategorie());
        if (frage.getLaengenUndBreitengrad() != null && !frage.getLaengenUndBreitengrad().isEmpty()) {
            String laengenUndBreitengrad = frage.getLaengenUndBreitengrad();
            double latitude = Double.parseDouble(frage.getLaengenUndBreitengrad().substring(0, laengenUndBreitengrad.indexOf(';')));
            double longitude = Double.parseDouble(frage.getLaengenUndBreitengrad().substring(laengenUndBreitengrad.indexOf(';') + 1, laengenUndBreitengrad.length()));
            LatLng position = new LatLng(latitude, longitude);
            addMarker(position);
        }
        int schwierigkeitId = frage.getSchwierigkeitsgrad();
        int index = 0;
        List<Schwierigkeit> schwierigkeiten = Utils.getSchwierigkeiten();
        for (Schwierigkeit s : schwierigkeiten) {
            if (s.getId() == schwierigkeitId)
                break;
            index++;
        }
        schwierigkeitView.setSelection(index);
        bildView.setText(frage.getBild());
    }

    private void editFrageError() {
        Utils.showLongToast(getString(R.string.frage_bearbeiten_error_loading), this);
    }

    public void frageSpeichern(View view) {
        boolean validationError = false;
        if (frageView.getText() == null || frageView.getText().toString().isEmpty()) {
            frageView.setError("Bitte Frage eingeben");
            validationError = true;
//            showFailMessage("Keine 'Frage'");
        }
        currentFrage.setFragetext(frageView.getText().toString());

        if (antwortView.getText() == null || antwortView.getText().toString().isEmpty()) {
            antwortView.setError("Bitte Antwort eingeben");
            validationError = true;
//            showFailMessage("Keine 'Antwort'");
        }
        currentFrage.setAntwort(antwortView.getText().toString());

        if (kategorieView.getText() == null || kategorieView.getText().toString().isEmpty()) {
            kategorieView.setError("Bitte Kategorie eingeben");
            validationError = true;
//            showFailMessage("Keine 'Kategorie'");
        }
        currentFrage.setKategorie(kategorieView.getText().toString());

        if (bildView.getText() != null || !bildView.getText().toString().isEmpty()) {
            currentFrage.setBild(bildView.getText().toString());
        }

        if (currentMarker != null)
            currentFrage.setLaengenUndBreitengrad(String.format("%s;%s", currentMarker.getPosition().latitude, currentMarker.getPosition().longitude));

        currentFrage.setKategorie(kategorieView.getText().toString());
        currentFrage.setSchwierigkeitsgrad(((Schwierigkeit) schwierigkeitView.getSelectedItem()).getId());

        if (!validationError) {
            if (maintenanceModus == FrageMaintenanceModus.CREATE) {
                restClient.createFrage(currentFrage, new FrageErstellenListenerImpl());
            } else {
                restClient.updateFrage(currentFrage, new FrageErstellenListenerImpl());
            }
        } else
            Utils.showShortToast("Eingaben sind nicht vollständig.", this);
    }

    private class FrageErstellenListenerImpl implements SaveFrageListener {

        @Override
        public void success(String guid) {
            frageSuccess(guid);
        }

        @Override
        public void error() {
            showFailMessage();
        }
    }

    private void frageSuccess(String guid) {
        if (maintenanceModus == FrageMaintenanceModus.CREATE) {
            addEigeneFrageId(guid);
        }
        Utils.navigateToMainActivity(this);
        String info = getString(R.string.frage_gespeichert);
        Utils.showLongToast(info, this);
    }

    private void addEigeneFrageId(String guid) {
        Set<String> eigeneFragenIds = new HashSet<>(Preferences.getEigeneFragenIds(this));
        eigeneFragenIds.add(guid);
        Preferences.setEigeneFragenIds(eigeneFragenIds, this);
    }

    private void showFailMessage() {
        String fail = getString(R.string.frage_nicht_erstellt);
        Utils.showLongToast(fail, this);
    }

    private void showFailMessage(String text) {
        String fail = getString(R.string.frage_bearbeiten_validation_fehler) + ": " + text;
        Utils.showLongToast(fail, this);
    }

    private void populateSchwierigkeitSpinner() {
        List<Schwierigkeit> schwierigkeiten = Utils.getSchwierigkeiten();
        ArrayAdapter schwierigkeitAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, schwierigkeiten);
        Spinner userSpinner = (Spinner) findViewById(R.id.editText_Schwierigkeit);
        if (userSpinner != null) {
            userSpinner.setAdapter(schwierigkeitAdapter);
        }
    }

    public void setLocalPosition(View view) {
        fillCurrentLocation();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
