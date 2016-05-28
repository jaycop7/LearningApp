package at.campus02.gang_of_four.learningapp.model;


public class Frage {
    private String FrageID;
    private String Fragetext;
    private String Antwort;
    private int Schwierigkeitsgrad;
    private String Kategorie;
    private String LaengenUndBreitengrad;
    private String Bild;

    public String getFrageID() {
        return FrageID;
    }

    public void setFrageID(String frageID) {
        this.FrageID = frageID;
    }

    public String getFragetext() {
        return Fragetext;
    }

    public void setFragetext(String fragetext) {
        this.Fragetext = fragetext;
    }

    public String getAntwort() {
        return Antwort;
    }

    public void setAntwort(String antwort) {
        this.Antwort = antwort;
    }

    public int getSchwierigkeitsgrad() {
        return Schwierigkeitsgrad;
    }

    public void setSchwierigkeitsgrad(int schwierigkeitsgrad) {
        this.Schwierigkeitsgrad = schwierigkeitsgrad;
    }

    public String getKategorie() {
        return Kategorie;
    }

    public void setKategorie(String kategorie) {
        this.Kategorie = kategorie;
    }

    public String getLaengenUndBreitengrad() {
        return LaengenUndBreitengrad;
    }

    public void setLaengenUndBreitengrad(String laengenUndBreitengrad) {
        this.LaengenUndBreitengrad = laengenUndBreitengrad;
    }

    public String getBild() {
        return Bild;
    }

    public void setBild(String bild) {
        this.Bild = bild;
    }
}
