package at.campus02.gang_of_four.learningapp.rest.asyncResponse;

import java.util.List;

public interface AsyncKategorienResponse extends AsyncResponse {
    void success(List<String> kategorien);
}
