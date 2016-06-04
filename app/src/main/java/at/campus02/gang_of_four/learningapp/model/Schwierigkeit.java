package at.campus02.gang_of_four.learningapp.model;

public class Schwierigkeit {
    int id;
    String bezeichnung;

    public Schwierigkeit(int id, String bezeichnung) {
        this.id = id;
        this.bezeichnung = bezeichnung;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String name) {
        this.bezeichnung = name;
    }

    @Override
    public String toString() {
        return bezeichnung;
    }
}
