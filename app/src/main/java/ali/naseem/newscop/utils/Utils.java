package ali.naseem.newscop.utils;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

public class Utils {
    private static final Utils ourInstance = new Utils();
    private static RequestQueue requestQueue;
    private static Gson gson;

    public static void initialize(Context context) {
        database = Room.databaseBuilder(context,
                AppDatabase.class, Constants.DB_NAME).allowMainThreadQueries().build();
        requestQueue = Volley.newRequestQueue(context);
        gson = new Gson();
    }

    public Gson getGson() {
        return gson;
    }

    public void addRequest(Request request) {
        request.setTag(Constants.TAG);
        request.setShouldCache(false);
        request.setRetryPolicy(new DefaultRetryPolicy(Constants.TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        requestQueue.getCache().clear();
        requestQueue.add(request);
    }

    public static Utils getInstance() {
        return ourInstance;
    }

    private static AppDatabase database;

    private Utils() {
    }

    public AppDatabase getDatabase() {
        return database;
    }
}
