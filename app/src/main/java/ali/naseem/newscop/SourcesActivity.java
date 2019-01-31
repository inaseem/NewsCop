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
import android.widget.Button;
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
import ali.naseem.newscop.models.Topics;
import ali.naseem.newscop.models.sources.Source;
import ali.naseem.newscop.models.sources.SourceApi;
import ali.naseem.newscop.utils.ApiFactory;
import ali.naseem.newscop.utils.Constants;
import ali.naseem.newscop.utils.Utils;

public class SourcesActivity extends AppCompatActivity implements LocationListener {

    private List<Source> sources = new ArrayList<>();
    private SourcesAdapter adapter;
    private View topicsWarning, sourcesWarning, proceed;
    private TextView addSources, addTopics;
    private LocationManager locationManager;
    private Dialog dialog;

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
//            addTopics.setVisibility(View.GONE);
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
        } else {
            proceed.setVisibility(View.GONE);
        }
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
        builder.setTitle("Title");
        View view = LayoutInflater.from(this).inflate(R.layout.topic_add, (ViewGroup) addSources.getRootView(), false);
        final EditText input = view.findViewById(R.id.editText);
        Button add = view.findViewById(R.id.add);
        Button cancel = view.findViewById(R.id.cancel);
        builder.setView(LayoutInflater.from(this).inflate(R.layout.topic_add, (ViewGroup) addSources.getRootView(), false));
        builder.setCancelable(true);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = input.getText().toString().trim();
                if (text.length() > 1) {
                    Topics topics = new Topics(text);
                    Utils.getInstance().getDatabase().topicsDao().insertAll(topics);
                }
                dialog.cancel();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
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
