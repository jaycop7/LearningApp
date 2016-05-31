package at.campus02.gang_of_four.learningapp.rest.restListener;

import java.util.List;

import at.campus02.gang_of_four.learningapp.model.Frage;

public interface FragenListener extends BasicListener {
    void processResponse(List<Frage> fragen);
}
