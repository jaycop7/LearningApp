package at.campus02.gang_of_four.learningapp.rest.asyncResponse;

import java.util.List;

import at.campus02.gang_of_four.learningapp.model.Frage;

public interface AsyncFragenResponse extends AsyncResponse {
    void processResponse(List<Frage> fragen);
}
