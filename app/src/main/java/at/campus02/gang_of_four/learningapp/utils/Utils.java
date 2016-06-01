package at.campus02.gang_of_four.learningapp.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import at.campus02.gang_of_four.learningapp.MainActivity;
import at.campus02.gang_of_four.learningapp.model.Schwierigkeit;

public class Utils {
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

    public static void navigateToMainActivity(Context context) {
        //redirect to main activity
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void showToast(String message, Context context) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }


    @SuppressWarnings("ResourceType")
    public static Location getCurrentLocation(Context context) {
        // Get the location manager
        LocationManager locationManager = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);

        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();

        String provider = locationManager.getBestProvider(criteria, false);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        return locationManager.getLastKnownLocation(provider);
    }
}
