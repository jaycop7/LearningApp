package at.campus02.gang_of_four.learningapp.rest;

import android.graphics.Bitmap;
import android.location.Location;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import at.campus02.gang_of_four.learningapp.ApplicationController;
import at.campus02.gang_of_four.learningapp.model.Frage;
import at.campus02.gang_of_four.learningapp.model.KategorieWithAnzahl;
import at.campus02.gang_of_four.learningapp.rest.restListener.FrageListener;
import at.campus02.gang_of_four.learningapp.rest.restListener.FragenListener;
import at.campus02.gang_of_four.learningapp.rest.restListener.ImageListener;
import at.campus02.gang_of_four.learningapp.rest.restListener.KategorienListener;
import at.campus02.gang_of_four.learningapp.rest.restListener.KategorienWithAnzahlListener;
import at.campus02.gang_of_four.learningapp.rest.restListener.SaveFrageListener;
import at.campus02.gang_of_four.learningapp.rest.restListener.SuccessListener;

public class RestDataClient {
    private static final String baseUrl = "http://campus02learningapp.azurewebsites.net/api/";
    Gson gson = new Gson();

    public void getKategorien(final KategorienListener listener) {
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
        ApplicationController.getInstance().addToRequestQueue(request);
    }

    public void getKategorienWithAnzahl(final KategorienWithAnzahlListener listener) {
        getKategorien(new KategorienListener() {
            @Override
            public void success(final List<String> kategorien) {
                getAlleFragen(new FragenListener() {
                    @Override
                    public void success(List<Frage> fragen) {
                        ArrayList<KategorieWithAnzahl> kategorienWithAnzahlListe = new ArrayList<>();
                        for (int i = 0; i < kategorien.size(); i++) {
                            int count = 0;
                            for (Frage frage : fragen) {
                                if (frage.getKategorie().equals(kategorien.get(i)))
                                    count++;
                            }
                            kategorienWithAnzahlListe.add(new KategorieWithAnzahl(kategorien.get(i), count));
                        }
                        listener.success(kategorienWithAnzahlListe);
                    }

                    @Override
                    public void error() {
                        listener.error();
                    }
                });
            }

            @Override
            public void error() {
                listener.error();
            }
        });
    }

    public void getAlleFragen(FragenListener listener) {
        String url = baseUrl + "fragen";
        getFragen(url, listener);
    }

    public void getFragenByKategorie(String kategorie, FragenListener listener) {
        String url = baseUrl + "fragen/kategorie/" + kategorie;
        getFragen(url, listener);
    }

    public void getFragenBySchwierigkeit(int schwierigkeit, FragenListener listener) {
        String url = baseUrl + "fragen/schwierigkeitsgrad/" + schwierigkeit;
        getFragen(url, listener);
    }

    public void getFragenByGpsKoordinaten(Location location, int distance, FragenListener listener) {
        String url = baseUrl + String.format("fragen/koordinaten/?La=%s&Lo=%s&Di=%s", location.getLatitude(), location.getLongitude(), distance);
        getFragen(url, listener);
    }

    public void getFragenWithPaging(int ueberspringen, int anzahl, FragenListener listener) {
        String url = baseUrl + String.format("fragen/paging/?ueberspringen=%s&anzahl=%s", ueberspringen, anzahl);
        getFragen(url, listener);
    }

    public void getFragenByIdSet(final Set<String> ids, final FragenListener listener) {
        String url = baseUrl + "fragen";
        getFragen(url, new FragenListener() {
            @Override
            public void success(List<Frage> fragen) {
                List<Frage> filteredFragen = new ArrayList<>();
                for (Frage frage : fragen) {
                    if (ids.contains(frage.getFrageID())) {
                        filteredFragen.add(frage);
                    }
                }
                listener.success(filteredFragen);
            }

            @Override
            public void error() {
                listener.error();
            }
        });
    }

    public void getFrage(String id, final FrageListener listener) {
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
                Log.e("ERROR", "Error in RestDataClient.getFrage.");
            }
        });
        ApplicationController.getInstance().addToRequestQueue(request);
    }

    public void createFrage(Frage frage, final SaveFrageListener listener) {
        String url = baseUrl + "fragen";
        final String jsonFrage = new Gson().toJson(frage, Frage.class);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String guid) {
                guid = new Gson().fromJson(guid, String.class);
                listener.success(guid);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.error();
            }
        }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                return jsonFrage.getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        ApplicationController.getInstance().addToRequestQueue(request);
    }

    public void updateFrage(Frage frage, final SaveFrageListener listener) {
        String url = baseUrl + "fragen/" + frage.getFrageID();
        try {
            JSONObject jsonObject = new JSONObject(new Gson().toJson(frage, Frage.class).toString());
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.PUT,
                    url,
                    jsonObject,

                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Frage updatedFrage = new Gson().fromJson(response.toString(), Frage.class);
                            listener.success(updatedFrage.getFrageID());
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            listener.error();
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }
            };
            ApplicationController.getInstance().addToRequestQueue(request);
        } catch (JSONException e) {
            listener.error();
        }
    }

    public void deleteFrage(Frage frage, final SuccessListener listener) {
        String url = baseUrl + "fragen/" + frage.getFrageID();
        StringRequest request = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.success();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.error();
            }
        });
        ApplicationController.getInstance().addToRequestQueue(request);
    }

    private void getFragen(String url, final FragenListener listener) {
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
                    listener.success(fragen);
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
        ApplicationController.getInstance().addToRequestQueue(request);
    }

//    private void persistFrage(Frage frage, String url, int method, final SaveFrageListener listener) {
//        final String jsonFrage = new Gson().toJson(frage, Frage.class);
//        StringRequest request = new StringRequest(method, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String guid) {
//                guid = new Gson().fromJson(guid, String.class);
//                listener.success(guid);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                listener.error();
//            }
//        }) {
//            @Override
//            public byte[] getBody() throws AuthFailureError {
//                return jsonFrage.getBytes();
//            }
//
//            @Override
//            public String getBodyContentType() {
//                return "application/json; charset=utf-8";
//            }
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Content-Type", "application/json; charset=utf-8");
//                return headers;
//            }
//        };
//        ApplicationController.getInstance().addToRequestQueue(request);
//    }

    public void getImage(String imageUrl, final ImageListener listener) {
        ImageRequest request = new ImageRequest(imageUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        listener.success(bitmap);
                    }
                }, 0, 0, null, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        listener.error();
                        Log.e("Fehler", "Fehler in RestDataClient.loadImage()");
                    }
                });
        ApplicationController.getInstance().addToRequestQueue(request);
    }

}
