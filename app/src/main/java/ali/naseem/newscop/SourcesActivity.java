package ali.naseem.newscop;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ali.naseem.newscop.adapters.SourcesAdapter;
import ali.naseem.newscop.adapters.TopicAdapter;
import ali.naseem.newscop.models.Topics;
import ali.naseem.newscop.models.sources.Source;
import ali.naseem.newscop.models.sources.SourceApi;
import ali.naseem.newscop.utils.Aid;
import ali.naseem.newscop.utils.ApiFactory;
import ali.naseem.newscop.utils.Constants;
import ali.naseem.newscop.utils.Utils;

public class SourcesActivity extends AppCompatActivity implements LocationListener {

    private List<Source> sources = new ArrayList<>();
    private List<Topics> topics = new ArrayList<>();
    private SourcesAdapter adapter;
    private TopicAdapter topicAdapter;
    private View topicsWarning, sourcesWarning, proceed;
    private TextView addSources, addTopics;
    private LocationManager locationManager;
    private Dialog dialog;
    private RecyclerView recyclerView;
    private View progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sources);
        addSources = findViewById(R.id.addSources);
        addTopics = findViewById(R.id.addTopics);
        proceed = findViewById(R.id.proceed);
        topicsWarning = findViewById(R.id.topicsWarning);
        sourcesWarning = findViewById(R.id.sourcesWarning);
        recyclerView = findViewById(R.id.sourcesRecyclerView);
        progressBar = findViewById(R.id.progressBar);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(false);
        recyclerView.setNestedScrollingEnabled(false);
        adapter = new SourcesAdapter(sources, this);
        recyclerView.setAdapter(adapter);

        RecyclerView recyclerView1 = findViewById(R.id.topicsRecyclerView);
        GridLayoutManager layoutManager1 = new GridLayoutManager(this, 2);
        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        recyclerView1.setHasFixedSize(false);
        recyclerView1.setNestedScrollingEnabled(false);
        if (Utils.getInstance().getDatabase().topicsDao().getAll().size() > 0) {
            topicsWarning.setVisibility(View.GONE);
        }
        if (Utils.getInstance().getDatabase().sourcesDao().getAll().size() > 0) {
            sourcesWarning.setVisibility(View.GONE);
        }
        topics.addAll(Utils.getInstance().getDatabase().topicsDao().getAll());
        topicAdapter = new TopicAdapter(topics, this);
        recyclerView1.setAdapter(topicAdapter);
        Intent intent = getIntent();
        String start = intent.getStringExtra(Constants.START);
        if (Utils.getInstance().isFirstTime() || start != null) {
            addSources.setVisibility(View.GONE);
            proceed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent1 = new Intent(SourcesActivity.this, MainActivity.class);
                    startActivity(intent1);
                    finish();
                }
            });
            addTopics.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addTopic();
                }
            });
            Utils.getInstance().setFirstTime(false);
        } else {
//            proceed.setVisibility(View.GONE);
            startActivity(new Intent(SourcesActivity.this, MainActivity.class));
            finish();
        }
        loadSources();
    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                showDialog();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
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
                        sources.clear();
                        sources.addAll(sourceApi.getSources());
                        adapter.notifyDataSetChanged();
                        Aid.crossfade(recyclerView, progressBar);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        Utils.getInstance().addRequest(stringRequest);
    }

    public void showDialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("News Cop wants to access you location.");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Allow",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });

        builder1.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        } else {
            getLocation();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses.size() > 0) {
                Toast.makeText(this, addresses.get(0).getCountryCode(), Toast.LENGTH_SHORT).show();
                Utils.getInstance().setLocation(addresses.get(0).getCountryCode().toLowerCase());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private void addTopic() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.topic_add, (ViewGroup) addSources.getRootView(), false);
        final EditText input = view.findViewById(R.id.editText);
        View add = view.findViewById(R.id.add);
        View cancel = view.findViewById(R.id.cancel);
        builder.setView(view);
        builder.setCancelable(true);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = input.getText().toString().trim();
                if (text.length() > 1) {
                    Topics topics = new Topics(text);
                    int id = (int) Utils.getInstance().getDatabase().topicsDao().insert(topics);
                    topics.setId(id);
                    SourcesActivity.this.topics.add(topics);
                    topicAdapter.notifyDataSetChanged();
                }
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }
}
