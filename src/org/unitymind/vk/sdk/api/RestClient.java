package org.unitymind.vk.sdk.api;

import android.util.Log;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class RestClient {
    private static final String TAG = RestClient.class.getName();

    public static final String BASE_URL = "https://api.vk.com/method/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String apiMethod, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        String url = getAbsoluteUrl(apiMethod);
        Log.d(TAG, "url = " + url);
        Log.d(TAG, "params = " + params.toString());
        client.get(getAbsoluteUrl(apiMethod), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
