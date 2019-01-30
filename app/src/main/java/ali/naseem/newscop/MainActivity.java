package ali.naseem.newscop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import ali.naseem.newscop.fragments.TopFive;
import ali.naseem.newscop.models.everything.EverythingApi;
import ali.naseem.newscop.models.sources.SourceApi;
import ali.naseem.newscop.utils.ApiFactory;
import ali.naseem.newscop.utils.AppDatabase;
import ali.naseem.newscop.utils.Constants;
import ali.naseem.newscop.utils.Utils;

public class MainActivity extends AppCompatActivity {

    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = Utils.getInstance().getDatabase();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.headlinesFrame, TopFive.newInstance())
                .commit();
        loadEverything();
        //loadSources();
    }

    private void loadEverything() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ApiFactory.EVERYTHING, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(Constants.TAG, response);
                try {
                    EverythingApi everythingApi = Utils.getInstance().getGson().fromJson(response, EverythingApi.class);
                    if (everythingApi.getStatus().equalsIgnoreCase("ok")) {
                        database.articlesDao().deleteAll();
                        database.articlesDao().insertAll(everythingApi.getArticles());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Some Error Occurred" + error, Toast.LENGTH_SHORT).show();
            }
        });
        Utils.getInstance().addRequest(stringRequest);
    }

    private void loadSources() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ApiFactory.SOURCES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(Constants.TAG, response);
                try {
                    SourceApi sourceApi = Utils.getInstance().getGson().fromJson(response, SourceApi.class);
                    if (sourceApi.getStatus().equalsIgnoreCase("ok")) {
                        database.sourcesDao().deleteAll();
                        database.sourcesDao().insertAll(sourceApi.getSources());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Some Error Occurred" + error, Toast.LENGTH_SHORT).show();
            }
        });
        Utils.getInstance().addRequest(stringRequest);
    }
}
