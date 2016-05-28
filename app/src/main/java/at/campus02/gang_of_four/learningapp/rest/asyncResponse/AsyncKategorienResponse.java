package at.campus02.gang_of_four.learningapp.rest.asyncResponse;

import java.util.List;

public interface AsyncKategorienResponse {
    void processResponse(List<String> kategorien);
}
