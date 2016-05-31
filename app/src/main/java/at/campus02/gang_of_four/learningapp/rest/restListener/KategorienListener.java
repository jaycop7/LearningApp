package at.campus02.gang_of_four.learningapp.rest.restListener;

import java.util.List;

public interface KategorienListener extends BasicListener {
    void success(List<String> kategorien);
}
