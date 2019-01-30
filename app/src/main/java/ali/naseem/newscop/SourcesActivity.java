package ali.naseem.newscop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.List;

import ali.naseem.newscop.adapters.SourcesAdapter;
import ali.naseem.newscop.models.sources.Source;
import ali.naseem.newscop.models.sources.SourceApi;
import ali.naseem.newscop.utils.ApiFactory;
import ali.naseem.newscop.utils.Constants;
import ali.naseem.newscop.utils.Utils;

public class SourcesActivity extends AppCompatActivity {

    private List<Source> sources = new ArrayList<>();
    private SourcesAdapter adapter;
    private View topicsWarning, sourcesWarning, proceed;
    private TextView addSources, addTopics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sources);
        addSources = findViewById(R.id.addSources);
        addTopics = findViewById(R.id.addTopics);
        proceed = findViewById(R.id.proceed);
        RecyclerView recyclerView = findViewById(R.id.topicsRecyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(false);
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new SourcesAdapter(sources, this);
        recyclerView.setAdapter(adapter);
        loadSources();
        Intent intent = getIntent();
        if (intent.getStringExtra(Constants.START) == null) {
            addSources.setVisibility(View.GONE);
            addTopics.setVisibility(View.GONE);
            proceed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent1 = new Intent(SourcesActivity.this, MainActivity.class);
                    startActivity(intent1);
                    finish();
                }
            });
        } else {
            proceed.setVisibility(View.GONE);
        }
    }

    private void loadSources() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ApiFactory.SOURCES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(Constants.TAG, response);
                try {
                    SourceApi sourceApi = Utils.getInstance().getGson().fromJson(response, SourceApi.class);
                    if (sourceApi.getStatus().equalsIgnoreCase("ok")) {
//                        Utils.getInstance().getDatabase().sourcesDao().deleteAll();
                        sources.clear();
                        sources.addAll(sourceApi.getSources());
                        adapter.notifyDataSetChanged();
//                        Aid.crossfade(recyclerView, progressBar);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getContext(), "Some Error Occurred" + error, Toast.LENGTH_SHORT).show();
            }
        });
        Utils.getInstance().addRequest(stringRequest);
    }
}
