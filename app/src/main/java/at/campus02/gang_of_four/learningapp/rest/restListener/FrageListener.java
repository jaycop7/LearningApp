package at.campus02.gang_of_four.learningapp.rest.restListener;

import at.campus02.gang_of_four.learningapp.model.Frage;

public interface FrageListener extends BasicListener {
    void success(Frage frage);
}
