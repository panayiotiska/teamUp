package com.kappa.teamup.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kappa.teamup.models.User;

import java.util.HashMap;
import java.util.Map;

public class NetworkManager
{
    private final String TAG = "NetworkManager";
    private final String API_URL = "https://tertian-hats.000webhostapp.com/connect/";

    private static NetworkManager instance = null;

    private Context context;

    public RequestQueue requestQueue;

    private NetworkManager(Context context)
    {
        this.context = context;

        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized NetworkManager getInstance(Context context)
    {
        if(null == instance)
        {
            instance = new NetworkManager(context);
        }
        return instance;
    }

    public static synchronized NetworkManager getInstance()
    {
        if(null == instance)
        {
            throw new IllegalStateException(NetworkManager.class.getSimpleName() + " is not initialized, call getInstance(...) first");
        }
        return instance;
    }

    public void registerUser(final User user, final NetworkManagerListener<String> listener)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API_URL + "register.php", new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {

                listener.getResult(response);
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                if(error instanceof TimeoutError || error instanceof NoConnectionError)
                {
                    listener.getError("Δεν υπάρχει πρόσβαση στο διαδύκτιο.");
                }
                else
                {
                    listener.getError("Παρουσιάστηκε κάποιο σφάλμα. Παρακαλούμε δοκιμάστε αργότερα.");
                }

                Log.d(TAG, error.getMessage());
            }
        })
        {
            protected Map<String, String> getParams()
            {
                Map map = new HashMap();
                map.put("user_uid", user.getUserUID());
                map.put("user_name", user.getUserName());
                map.put("device_token", user.getDeviceToken());
                return map;
            }
        };

        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }
}
