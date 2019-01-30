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
import ali.naseem.newscop.models.headlines.HeadlinesApi;
import ali.naseem.newscop.models.sources.SourceApi;
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
        loadSources();
    }

    private void loadEverything() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://newsapi.org/v2/everything?q=bitcoin&apiKey=f7f076926fbf4dbf8857183a0717b0cb", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(Constants.TAG, response);
                try {
                    EverythingApi everythingApi = Utils.getInstance().getGson().fromJson(response, EverythingApi.class);
                    if (everythingApi.getStatus().equalsIgnoreCase("ok")) {
                        database.articlesDao().deleteAll();
                        database.articlesDao().insertAll(everythingApi.getArticles());
//                        Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://newsapi.org/v2/sources?apiKey=f7f076926fbf4dbf8857183a0717b0cb", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(Constants.TAG, response);
                try {
                    SourceApi sourceApi = Utils.getInstance().getGson().fromJson(response, SourceApi.class);
                    if (sourceApi.getStatus().equalsIgnoreCase("ok")) {
                        database.sourcesDao().deleteAll();
                        database.sourcesDao().insertAll(sourceApi.getSources());
//                        Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
