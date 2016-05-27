package at.campus02.gang_of_four.learningapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {
    public static boolean isNetworkOnline(Context context) {
        if(context == null)
            return false;
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nwInfo = connectivityManager.getActiveNetworkInfo();
        return nwInfo != null && nwInfo.isConnectedOrConnecting();
    }
}
