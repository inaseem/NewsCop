package ali.naseem.newscop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import ali.naseem.newscop.models.everything.EverythingApi;
import ali.naseem.newscop.models.headlines.HeadlinesApi;
import ali.naseem.newscop.models.sources.SourceApi;
import ali.naseem.newscop.utils.Constants;
import ali.naseem.newscop.utils.Utils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadEverything();
        loadHeadlines();
        loadSources();
    }

    private void loadEverything() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://newsapi.org/v2/top-headlines?country=us&apiKey=f7f076926fbf4dbf8857183a0717b0cb", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(Constants.TAG, response);
                try {
                    EverythingApi everythingApi = Utils.getInstance().getGson().fromJson(response, EverythingApi.class);
                    if (everythingApi.getStatus().equalsIgnoreCase("ok")) {
                        Utils.getInstance().getDatabase().articlesDao().deleteAll();
                        Utils.getInstance().getDatabase().articlesDao().insertAll(everythingApi.getArticles());
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
                        Utils.getInstance().getDatabase().sourcesDao().deleteAll();
                        Utils.getInstance().getDatabase().sourcesDao().insertAll(sourceApi.getSources());
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

    private void loadHeadlines() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://newsapi.org/v2/top-headlines?country=us&apiKey=f7f076926fbf4dbf8857183a0717b0cb", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(Constants.TAG, response);
                try {
                    HeadlinesApi headlinesApi = Utils.getInstance().getGson().fromJson(response, HeadlinesApi.class);
                    if (headlinesApi.getStatus().equalsIgnoreCase("ok")) {
                        Utils.getInstance().getDatabase().headlinesDao().deleteAll();
                        Utils.getInstance().getDatabase().headlinesDao().insertAll(headlinesApi.getArticles());
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
