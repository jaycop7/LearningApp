package at.campus02.gang_of_four.learningapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;
import java.util.List;

import at.campus02.gang_of_four.learningapp.model.Schwierigkeit;

public class Utils {
    public static boolean isNetworkOnline(Context context) {
        if(context == null)
            return false;
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nwInfo = connectivityManager.getActiveNetworkInfo();
        return nwInfo != null && nwInfo.isConnectedOrConnecting();
    }

    public static List<Schwierigkeit> getSchwierigkeiten(){
        List<Schwierigkeit> liste = new ArrayList<>();
        liste.add(new Schwierigkeit(0,"Leicht"));
        liste.add(new Schwierigkeit(1,"Mittel"));
        liste.add(new Schwierigkeit(2,"Schwer"));
        liste.add(new Schwierigkeit(3,"Sehr schwer"));
        liste.add(new Schwierigkeit(10,"Zufällig"));
        return liste;
    }
}
