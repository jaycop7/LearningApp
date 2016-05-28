package at.campus02.gang_of_four.learningapp.rest;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import at.campus02.gang_of_four.learningapp.model.Frage;
import at.campus02.gang_of_four.learningapp.model.Schwierigkeit;
import at.campus02.gang_of_four.learningapp.rest.asyncResponse.AsyncFrageResponse;
import at.campus02.gang_of_four.learningapp.rest.asyncResponse.AsyncFragenResponse;
import at.campus02.gang_of_four.learningapp.rest.asyncResponse.AsyncImageResponse;
import at.campus02.gang_of_four.learningapp.rest.asyncResponse.AsyncKategorienResponse;
import at.campus02.gang_of_four.learningapp.rest.asyncResponse.AsyncSuccessResponse;

public class RestDataService {
    private static final String baseUrl = "http://campus02learningapp.azurewebsites.net/api/";
    private static RequestQueue requestQueue = null;
    Gson gson = new Gson();

    public RestDataService(Context context) {
        requestQueue = Volley.newRequestQueue(context);
    }

    public void getKategorien(final AsyncKategorienResponse listener) {
        String url = baseUrl + "kategorie";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try {
                    List<String> kategorien = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        kategorien.add(response.getString(i));
                    }
                    listener.success(kategorien);
                } catch (JSONException e) {
                    listener.error();
                    Log.e("ERROR", "Json error in getKategorien." + e.getLocalizedMessage());
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                listener.error();
                Log.e("ERROR", "Error in getKategorien!");
            }
        });
        requestQueue.add(request);
    }

    public void createFrage(Frage frage, final AsyncSuccessResponse listener) {
        String url = baseUrl + "fragen";
        try {
            JSONObject jsonObject = new JSONObject(new Gson().toJson(frage, Frage.class));
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    listener.success();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    listener.error();
                }
            });
            requestQueue.add(request);
        } catch (JSONException e) {
            listener.error();
        }
    }

    //FIXME CopyPaste Code
    public void updateFrage(Frage frage, final AsyncSuccessResponse listener) {
        String url = baseUrl + "fragen/" + frage.getFrageID();
        try {
            JSONObject jsonObject = new JSONObject(new Gson().toJson(frage, Frage.class));
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    listener.success();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    listener.error();
                }
            });
            requestQueue.add(request);
        } catch (JSONException e) {
            listener.error();
        }
    }

    public void getFrage(String id, final AsyncFrageResponse listener) {
        String url = baseUrl + "fragen/" + id;
        GsonRequest<Frage> request = new GsonRequest<>(url, Frage.class, null, new Response.Listener<Frage>() {
            @Override
            public void onResponse(Frage frage) {
                listener.success(frage);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.error();
                Log.e("ERROR", "Error in RestDataService.getFrage.");
            }
        });
        requestQueue.add(request);
    }

    public void getFragen(AsyncFragenResponse listener) {
        String url = baseUrl + "fragen";
        getFragen(url, listener);
    }

    public void getFragenByKategorie(String kategorie, AsyncFragenResponse listener) {
        String url = baseUrl + "fragen/kategorie/" + kategorie;
        getFragen(url, listener);
    }

    public void getFragenBySchwierigkeit(Schwierigkeit schwierigkeit, AsyncFragenResponse listener) {
        String url = baseUrl + "fragen/schwierigkeit/" + schwierigkeit.getId();
        getFragen(url, listener);
    }

    private void getFragen(String url, final AsyncFragenResponse listener) {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try {
                    List<Frage> fragen = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        String jsonString = response.getString(i);
                        Frage frage = gson.fromJson(jsonString, Frage.class);
                        fragen.add(frage);
                    }
                    listener.processResponse(fragen);
                    Log.i("SUCCESS", "Fragen loaded successful");
                } catch (JSONException e) {
                    listener.error();
                    Log.e("ERROR", "Error in getFragen Json.");
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                listener.error();
                Log.e("ERROR", "Error in RestDateService.getFragen.");
            }
        });
        requestQueue.add(request);
    }

    public void loadImage(String imageUrl, final AsyncImageResponse listener) {
        ImageRequest request = new ImageRequest(imageUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        listener.success(bitmap);
//                        mImageView.setImageBitmap(bitmap);
                    }
                }, 0, 0, null, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        listener.error();
                        Log.e("Fehler", "Fehler in RestDataService.loadImage()");
//                        mImageView.setImageResource(R.drawable.image_load_error);
                    }
                });
    }

}
