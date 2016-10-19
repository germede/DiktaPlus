package singularfactory.app.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by Óscar Adae Rodríguez on 11/05/15-10:51
 */
public class Volley {

    public static final String TAG = Volley.class.getSimpleName();

    public static int SOCKET_TIMEOUT  = 15000;  //15 seconds

    private static Volley mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mContext;

    private Volley(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(20);

            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
    }

    public static synchronized Volley getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Volley(context);
        }
        return mInstance;
    }

//    public RequestQueue volleyGetRequestQueue() {
//        if (mRequestQueue == null) {
////            mRequestQueue = Volley.newRequestQueue(AppCommon.getInstance().getApplicationContext(), new OkHttpStack());
//            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
//        }
//
//        return mRequestQueue;
//    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
//            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext(), new OkHttpStack());
            mRequestQueue = com.android.volley.toolbox.Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public void volleySetDefaultPolicyRetry(StringRequest request, String tagRequest) {
        RetryPolicy policy = new DefaultRetryPolicy(SOCKET_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);

        //Set the default tag if tag is empty
        request.setTag(TextUtils.isEmpty(tagRequest) ? TAG : tagRequest);
        getRequestQueue().add(request);
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
//        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
//        getRequestQueue().add(req);

        RetryPolicy policy = new DefaultRetryPolicy(SOCKET_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        req.setRetryPolicy(policy);

        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }
}