package at.campus02.gang_of_four.learningapp.at.campus02.gang_of_four.leaningapp.model;

/**
 * Created by Jakob on 21.04.2016.
 */
public class Frage {
    private String frageID;
    private String fragetext;
    private String antwort;
    private int schwierigkeitsgrad;
    private String kategorie;
    private String laengenUndBreitengrad;
    private String bild;

    public String getFrageID() {
        return frageID;
    }

    public void setFrageID(String frageID) {
        this.frageID = frageID;
    }

    public String getFragetext() {
        return fragetext;
    }

    public void setFragetext(String fragetext) {
        this.fragetext = fragetext;
    }

    public String getAntwort() {
        return antwort;
    }

    public void setAntwort(String antwort) {
        this.antwort = antwort;
    }

    public int getSchwierigkeitsgrad() {
        return schwierigkeitsgrad;
    }

    public void setSchwierigkeitsgrad(int schwierigkeitsgrad) {
        this.schwierigkeitsgrad = schwierigkeitsgrad;
    }

    public String getKategorie() {
        return kategorie;
    }

    public void setKategorie(String kategorie) {
        this.kategorie = kategorie;
    }

    public String getLaengenUndBreitengrad() {
        return laengenUndBreitengrad;
    }

    public void setLaengenUndBreitengrad(String laengenUndBreitengrad) {
        this.laengenUndBreitengrad = laengenUndBreitengrad;
    }

    public String getBild() {
        return bild;
    }

    public void setBild(String bild) {
        this.bild = bild;
    }
}
