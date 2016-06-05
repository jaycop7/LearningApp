package at.campus02.gang_of_four.learningapp.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import at.campus02.gang_of_four.learningapp.MainActivity;
import at.campus02.gang_of_four.learningapp.model.Schwierigkeit;

public class Utils {

    private static final int REQUEST_CODE_LOCATION = 2;


    public static boolean isNetworkOnline(Context context) {
        if (context == null)
            return false;
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nwInfo = connectivityManager.getActiveNetworkInfo();
        return nwInfo != null && nwInfo.isConnectedOrConnecting();
    }

    public static List<Schwierigkeit> getSchwierigkeiten() {
        List<Schwierigkeit> liste = new ArrayList<>();
        liste.add(new Schwierigkeit(0, "Leicht"));
        liste.add(new Schwierigkeit(1, "Mittel"));
        liste.add(new Schwierigkeit(2, "Schwer"));
        liste.add(new Schwierigkeit(3, "Sehr schwer"));
        liste.add(new Schwierigkeit(10, "Zufällig"));
        return liste;
    }

    public static String getSchwierigkeitBezeichnung(int schwierigkeitId) {
        String bezeichnung = String.valueOf(schwierigkeitId);
        List<Schwierigkeit> schwierigkeiten = Utils.getSchwierigkeiten();
        for (Schwierigkeit s : schwierigkeiten) {
            if (s.getId() == schwierigkeitId)
                bezeichnung = s.getBezeichnung();
        }
        return bezeichnung;
    }

    public static void navigateToMainActivity(Context context) {
        //redirect to main activity
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void showShortToast(String message, Context context) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void showLongToast(String message, Context context) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }

    @SuppressWarnings("ResourceType")
    public static Location getCurrentLocation(Activity context) {

        Location rv = null;

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now

            if (ActivityCompat.shouldShowRequestPermissionRationale(context,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Display UI and wait for user interaction
            } else {
                ActivityCompat.requestPermissions(
                        context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE_LOCATION);
            }
        }

        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("GPS check ", "NO PERMISSION");
            return null;
        }
        try {

            LocationManager manager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
            if (manager != null) {
                rv = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (rv == null) {
                    rv = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
            }
            return rv;

        } catch (Exception ex) {
            Log.e("LocationManager", "Error creating location service: " + ex.getMessage());
            return null;
        }
    }
}
