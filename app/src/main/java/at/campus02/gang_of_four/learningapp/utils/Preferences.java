package at.campus02.gang_of_four.learningapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    public static final String PREFS_NAME = "LearningAppPreferences";
    public static final String MAX_FRAGEN = "maxFragenProRunde";
    public static final String GPS_UMKREIS = "gpsUmkreis";
    public static final String BENUTZER_NAME = "benutzername";
    public static final String SCHWIERIGKEIT = "schwierigkeit";
    public static final String FRAGEN_UEBERSPRINGEN = "anzahlFragenUeberspringen";

    private static SharedPreferences getSettings(Context context) {
        return context.getSharedPreferences(PREFS_NAME, 0);
    }

    private static SharedPreferences.Editor getSettingsEditor(Context context) {
        SharedPreferences settings = getSettings(context);
        return settings.edit();
    }

    public static int getMaxFragen(Context context) {
        SharedPreferences settings = getSettings(context);
        return settings.getInt(MAX_FRAGEN, 10);
    }

    public static void setMaxFragen(int maxFragen, Context context) {
        SharedPreferences.Editor settingsEditor = getSettingsEditor(context);
        settingsEditor.putInt(MAX_FRAGEN, maxFragen);
        settingsEditor.commit();
    }

    public static int getGpsUmkreis(Context context) {
        return getSettings(context).getInt(GPS_UMKREIS, 20);
    }

    public static void setGpsUmkreis(int gpsUmkreis, Context context){
        SharedPreferences.Editor settingsEditor = getSettingsEditor(context);
        settingsEditor.putInt(GPS_UMKREIS, gpsUmkreis);
        settingsEditor.commit();
    }

    public static String getBenutzername(Context context) {
        return getSettings(context).getString(BENUTZER_NAME, "Benutzer 1");
    }

    public static void setBenutzername(String benutzername, Context context){
        SharedPreferences.Editor settingsEditor = getSettingsEditor(context);
        settingsEditor.putString(BENUTZER_NAME, benutzername);
        settingsEditor.commit();
    }

    public static int getSchwierigkeit(Context context) {
        return getSettings(context).getInt(SCHWIERIGKEIT, 10); //10 = zufällige Schwierigkeit
    }

    public static void setSchwierigkeit(int schwierigkeit, Context context){
        SharedPreferences.Editor settingsEditor = getSettingsEditor(context);
        settingsEditor.putInt(SCHWIERIGKEIT, schwierigkeit);
        settingsEditor.commit();
    }

    public static int getFragenUeberspringenAnzahl(Context context) {
        return getSettings(context).getInt(FRAGEN_UEBERSPRINGEN, 1);
    }

    public static void setFragenUeberspringenAnzahl(int fragenUeberspringenAnzahl, Context context){
        SharedPreferences.Editor settingsEditor = getSettingsEditor(context);
        settingsEditor.putInt(FRAGEN_UEBERSPRINGEN, fragenUeberspringenAnzahl);
        settingsEditor.commit();
    }
}