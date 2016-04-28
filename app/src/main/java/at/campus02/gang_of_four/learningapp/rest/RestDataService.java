package at.campus02.gang_of_four.learningapp.rest;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import at.campus02.gang_of_four.learningapp.at.campus02.gang_of_four.leaningapp.model.Frage;

/**
 * Created by Jakob on 21.04.2016.
 */
public class RestDataService {
    private static final String baseUrl = "http://campus02learningapp.azurewebsites.net/api/";
    private static RequestQueue requestQueue = null;
    Gson gson = new Gson();

    public RestDataService(Context context){
        requestQueue = Volley.newRequestQueue(context);
    }

    public void getKategorien(){
        String url = baseUrl + "kategorie";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse( JSONArray response) {
                try {
                    Log.i("Rest", response.get(0).toString());
                } catch (JSONException e) {
                    Log.e("ERROR", "Json error in getKategorien." + e.getLocalizedMessage());
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", "Error in getKategorien!");

            }
        } );
        requestQueue.add(request);
    }

    public void createFrage(){

    }

    public void getFrage(String id){

    }

    public void getFragen(){
        String url = baseUrl + "fragen";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse( JSONArray response) {
                try {
                    Frage frage = gson.fromJson(response.getJSONObject(0).toString(), Frage.class);
                    Log.i("Frage", frage.getFragetext());
                } catch (JSONException e) {
                    Log.e("ERROR", "Error in getFragen Json.");
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", "Error in getFragen!");

            }
        } );
        requestQueue.add(request);
    }


}
