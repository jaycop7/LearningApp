package at.campus02.gang_of_four.learningapp.model;

public class KategorieWithAnzahl {
    private int anzahl;
    private String kategorie;

    public int getAnzahl() {
        return anzahl;
    }

    public void setAnzahl(int anzahl) {
        this.anzahl = anzahl;
    }

    public KategorieWithAnzahl(String kategorie, int anzahl) {
        this.kategorie = kategorie;
        this.anzahl = anzahl;
    }

    public String getKategorie() {
        return kategorie;
    }

    public void setKategorie(String kategorie) {
        this.kategorie = kategorie;
    }
}
