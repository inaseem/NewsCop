package ali.naseem.newscop.utils;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Utils {
    private static final Utils ourInstance = new Utils();
    private static RequestQueue requestQueue;
    private static Gson gson;

    public static void initialize(Context context) {
        database = Room.databaseBuilder(context,
                AppDatabase.class, Constants.DB_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
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

    public static String getFormatted(String string) {

        //return String.format(Locale.getDefault(), "%s %s, %s", parts[2], month[Integer.parseInt(parts[1]) - 1], parts[0]);
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            Date date = inputFormat.parse(string);
            return getDifference(new Date(), date);
        } catch (Exception e) {
            return string;
        }
    }

    public static String getDifference(Date start, Date end) {
        long duration = start.getTime() - end.getTime();
        long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);
        long diffInDays = TimeUnit.MILLISECONDS.toDays(duration);
        StringBuilder diff = new StringBuilder();
        if (diffInDays > 0) {
            diff.append(diffInDays + " days ");
        } else {
            if (diffInHours > 0) {
                diff.append(diffInHours + " hours ");
            } else {
                if (diffInMinutes > 0) {
                    diff.append(diffInMinutes + " minutes ");
                } else {
                    if (diffInSeconds > 0) {
                        diff.append(diffInSeconds + " seconds ");
                    } else {
                        diff.append(" hours ");
                    }
                }
            }
        }
        return diff.append("ago").toString();
    }

}
