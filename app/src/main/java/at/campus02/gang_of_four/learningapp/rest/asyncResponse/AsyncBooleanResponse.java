package at.campus02.gang_of_four.learningapp.rest.asyncResponse;

import at.campus02.gang_of_four.learningapp.model.Frage;

public interface AsyncBooleanResponse {
    void processResponse(boolean success);
}
