package com.shevaalex.android.rickmortydatabase.source.network;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.shevaalex.android.rickmortydatabase.BuildConfig;
import com.shevaalex.android.rickmortydatabase.R;
import org.json.JSONObject;

public class NetworkDataParsing {
    private static final String LOG_TAG = NetworkDataParsing.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static volatile NetworkDataParsing sInstance;
    private final VolleySingleton volleyInstance;
    private final Context context;

    private NetworkDataParsing (Context context) {
        if (sInstance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
        volleyInstance = VolleySingleton.getInstance(context.getApplicationContext());
        this.context = context.getApplicationContext();
    }

    public static synchronized NetworkDataParsing getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = new NetworkDataParsing(context);
                }
            }
        }
        return sInstance;
    }

    // makes a call to get the number of pages containing Characters, and passes it to RmRepo
    public void getVolleyResponse(final VolleyCallback callback, final String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(), response -> {
                    if (response != null) {
                        callback.getJsonDataResponse(response);
                    }
                }, error -> {
                    handleVolleyErrors(error);
                    error.printStackTrace();
                });
        volleyInstance.addToRequestQueue(jsonObjectRequest);
    }

    private void handleVolleyErrors (VolleyError error) {
        String errorMessage = "";
        Resources res = context.getResources();
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            //This indicates that the request has either time out or there is no connection
            errorMessage = res.getString(R.string.timeout_or_no_connection);
        } else if (error instanceof AuthFailureError) {
            // Error indicating that there was an Authentication Failure while performing the request
            errorMessage = res.getString(R.string.auth_fail);
        } else if (error instanceof ServerError) {
            //Indicates that the server responded with a error response
            errorMessage = res.getString(R.string.server_error);
        } else if (error instanceof NetworkError) {
            //Indicates that there was network error while performing the request
            errorMessage = res.getString(R.string.network_error);
        } else if (error instanceof ParseError) {
            // Indicates that the server response could not be parsed
            errorMessage = res.getString(R.string.parse_error);
        }
        if (BuildConfig.DEBUG) {
            Log.d(LOG_TAG, errorMessage);
        }
    }

    //cancels Volley requests after database sync / check
    //and invalidates last url's cache to prevent Volley returning cached requests without network connection
    public void cancelVolleyRequests (String urlKey) {
        volleyInstance.cancelRequests(urlKey);
    }

}