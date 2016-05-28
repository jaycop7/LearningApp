package at.campus02.gang_of_four.learningapp.rest.asyncResponse;

import android.graphics.Bitmap;

public interface AsyncImageResponse extends AsyncResponse {
    void success(Bitmap immage);
}
