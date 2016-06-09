package at.campus02.gang_of_four.learningapp.rest.restListener;

import java.util.List;

import at.campus02.gang_of_four.learningapp.model.KategorieWithAnzahl;

public interface KategorienWithAnzahlListener extends BasicListener {
    void success(List<KategorieWithAnzahl> kategorien);
}
