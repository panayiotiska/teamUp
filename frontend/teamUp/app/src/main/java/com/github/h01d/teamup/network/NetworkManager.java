package com.github.h01d.teamup.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NetworkManager
{
    private final String TAG = "NetworkManager";

    private static NetworkManager instance = null;
    private RequestQueue requestQueue;

    private NetworkManager(Context context)
    {
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

    /**
     * getData will handle all GET Requests to the API
     *
     * @param URL      the API url
     * @param listener the listener to make easier retuning data
     */

    public void getData(String URL, final NetworkManagerListener listener)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(response);

                    if(jsonObject.getJSONObject("result").getString("status").equals("success"))
                    {
                        listener.getResult(jsonObject.getJSONArray("payload"));
                    }
                    else
                    {
                        listener.getError(jsonObject.getJSONObject("status").getString("error"), 1);
                    }

                }
                catch(JSONException e)
                {
                    e.printStackTrace();

                    listener.getError(e.getMessage(), 1);
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                if(error instanceof TimeoutError || error instanceof NoConnectionError)
                {
                    listener.getError("Δεν υπάρχει πρόσβαση στο διαδίκτυο.", 0);
                }
                else
                {
                    listener.getError("Παρουσιάστηκε ένα άγνωστο σφάλμα.\nΠαρακαλούμε δοκιμάστε αργότερα.", 1);
                }

                if((error.getMessage() != null))
                {
                    Log.d(TAG, error.getMessage());
                }
            }
        })
        {
            @Override
            public Map<String, String> getHeaders()
            {
                Map<String, String> params = new HashMap<>();
                params.put("auth-token", "rafAuthToken");

                return params;
            }
        };

        stringRequest.setShouldCache(false); // Prevent caching response on LTE networks
        requestQueue.add(stringRequest);
    }

    /**
     * postData will handle all POST Requests to the API
     * @param URL the API url
     * @param jsonObject the JSON object with the data
     * @param listener the listener to make easier retuning data
     */

    public void postData(String URL, JSONObject jsonObject, final NetworkManagerListener listener)
    {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(final JSONObject response)
            {
                try
                {
                    if(response.getJSONObject("result").getString("status").equals("success"))
                    {
                        if(!response.isNull("payload"))
                        {
                            listener.getResult(response.getJSONArray("payload"));
                        }
                        else
                        {
                            listener.getResult(new JSONArray());
                        }
                    }
                    else
                    {
                        listener.getError(response.getJSONObject("result").getString("error"), 1);
                    }

                }
                catch(JSONException e)
                {
                    e.printStackTrace();

                    listener.getError(e.getMessage(), 1);
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                if(error instanceof TimeoutError || error instanceof NoConnectionError)
                {
                    listener.getError("Δεν υπάρχει πρόσβαση στο διαδίκτυο.", 0);
                }
                else
                {
                    listener.getError("Παρουσιάστηκε ένα άγνωστο σφάλμα.\nΠαρακαλούμε δοκιμάστε αργότερα.", 1);
                }

                if((error.getMessage() != null))
                {
                    Log.d(TAG, error.getMessage());
                }
            }
        })
        {
            @Override
            public Map<String, String> getHeaders()
            {
                Map<String, String> params = new HashMap<>();
                params.put("auth-token", "rafAuthToken");

                return params;
            }
        };

        jsObjRequest.setShouldCache(false); // Prevent caching response on LTE networks
        requestQueue.add(jsObjRequest);
    }
}
