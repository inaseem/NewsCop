package ali.naseem.newscop.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.List;

import ali.naseem.newscop.R;
import ali.naseem.newscop.adapters.HeadlinesAdapter;
import ali.naseem.newscop.models.headlines.Article;
import ali.naseem.newscop.models.headlines.HeadlinesApi;
import ali.naseem.newscop.utils.Aid;
import ali.naseem.newscop.utils.Constants;
import ali.naseem.newscop.utils.Utils;

public class TopFive extends Fragment {

    private RecyclerView recyclerView;
    private View progressBar;
    private HeadlinesAdapter adapter;
    private List<Article> items = new ArrayList<>();

    public TopFive() {
        // Required empty public constructor
    }

    public static TopFive newInstance() {
        TopFive fragment = new TopFive();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_top_five, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), layoutManager.getOrientation()));
        recyclerView.setHasFixedSize(false);
        recyclerView.setNestedScrollingEnabled(false);
        if (items.size() == 0) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        adapter = new HeadlinesAdapter(items, getContext());
        recyclerView.setAdapter(adapter);
        loadHeadlines();
        return view;
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
                        items.clear();
                        items.addAll(Utils.getInstance().getDatabase().headlinesDao().getAll());
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
                Toast.makeText(getContext(), "Some Error Occurred" + error, Toast.LENGTH_SHORT).show();
            }
        });
        Utils.getInstance().addRequest(stringRequest);
    }

}
