package jamesmcgarr.me.homeautomationandroid.utils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by user on 01/04/18.
 */

public class HttpUtils {

    //todo fill in the server url+port
    private static final String BASE_URL = "http://localhost:%s/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String port, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(String.format(getAbsoluteUrl(url), port), params, responseHandler);
    }

    public static void post(String port, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(String.format(getAbsoluteUrl(url), port), params, responseHandler);
    }

    public static void getByUrl(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(url, params, responseHandler);
    }

    public static void postByUrl(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(url, params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

}
