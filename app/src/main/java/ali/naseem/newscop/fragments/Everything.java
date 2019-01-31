package ali.naseem.newscop.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.List;

import ali.naseem.newscop.R;
import ali.naseem.newscop.adapters.ArticlesAdapter;
import ali.naseem.newscop.models.Topics;
import ali.naseem.newscop.models.everything.Article;
import ali.naseem.newscop.models.everything.EverythingApi;
import ali.naseem.newscop.models.sources.Source;
import ali.naseem.newscop.utils.Aid;
import ali.naseem.newscop.utils.ApiFactory;
import ali.naseem.newscop.utils.Constants;
import ali.naseem.newscop.utils.Utils;

public class Everything extends Fragment {

    private RecyclerView recyclerView;
    private View progressBar;
    private ArticlesAdapter adapter;
    private List<Article> items = new ArrayList<>();
    private int page = 1;
    boolean canLoad = true;

    public Everything() {
        // Required empty public constructor
    }

    public static Everything newInstance() {
        Everything fragment = new Everything();
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
        final View view = inflater.inflate(R.layout.fragment_everything, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), layoutManager.getOrientation()));
        recyclerView.setNestedScrollingEnabled(true);
        items.addAll(Utils.getInstance().getDatabase().articlesDao().getAll());
        if (items.size() == 0) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        adapter = new ArticlesAdapter(items, getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        loadEverything();
        return view;
    }

    public void loadMore() {
        if (canLoad)
            load();
    }

    private void load() {
        List<Source> savedSources = Utils.getInstance().getDatabase().sourcesDao().getAll();
        StringBuilder sources = new StringBuilder();
        if (savedSources.size() > 0) {
            sources.append("&sources=");
            for (Source source : savedSources) {
                if (source.getId() != null)
                    sources.append(source.getId()).append(",");
                else
                    sources.append(source.getName()).append(",");
            }
            sources.deleteCharAt(sources.length() - 1);
        } else {
            sources.append("&country=").append(Utils.getInstance().getLocation() == null ? "in" : Utils.getInstance().getLocation());
        }
        List<Topics> topics = Utils.getInstance().getDatabase().topicsDao().getAll();
        if (topics.size() > 0) {
            sources.append("&q=");
            for (Topics topic : topics) {
                sources.append(topic.getTopicName().toLowerCase()).append(",");
            }
            sources.deleteCharAt(sources.length() - 1);
            sources.append("&page=").append(++page);
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ApiFactory.EVERYTHING + sources.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(Constants.TAG, response);
                try {
                    EverythingApi everythingApi = Utils.getInstance().getGson().fromJson(response, EverythingApi.class);
                    if (everythingApi.getStatus().equalsIgnoreCase("ok")) {
                        if (everythingApi.getArticles().size() <= everythingApi.getTotalResults()) {
                            canLoad = false;
                        }
                        Utils.getInstance().getDatabase().articlesDao().insertAll(everythingApi.getArticles());
                        items.addAll(Utils.getInstance().getDatabase().articlesDao().getAll());
                        adapter.notifyDataSetChanged();
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

    private void loadEverything() {
        List<Source> savedSources = Utils.getInstance().getDatabase().sourcesDao().getAll();
        StringBuilder sources = new StringBuilder();
        if (savedSources.size() > 0) {
            sources.append("&sources=");
            for (Source source : savedSources) {
                if (source.getId() != null)
                    sources.append(source.getId()).append(",");
                else
                    sources.append(source.getName()).append(",");
            }
            sources.deleteCharAt(sources.length() - 1);
        } else {
            sources.append("&country=").append(Utils.getInstance().getLocation() == null ? "in" : Utils.getInstance().getLocation());
        }
        List<Topics> topics = Utils.getInstance().getDatabase().topicsDao().getAll();
        if (topics.size() > 0) {
            sources.append("&q=");
            for (Topics topic : topics) {
                sources.append(topic.getTopicName().toLowerCase()).append(",");
            }
            sources.deleteCharAt(sources.length() - 1);
            sources.append("&page=").append(page);
        }
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ApiFactory.EVERYTHING + sources.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(Constants.TAG, response);
                try {
                    EverythingApi everythingApi = Utils.getInstance().getGson().fromJson(response, EverythingApi.class);
                    if (everythingApi.getStatus().equalsIgnoreCase("ok")) {
                        if (everythingApi.getArticles().size() <= everythingApi.getTotalResults()) {
                            canLoad = false;
                        }
                        Utils.getInstance().getDatabase().articlesDao().deleteAll();
                        Utils.getInstance().getDatabase().articlesDao().insertAll(everythingApi.getArticles());
                        items.clear();
                        items.addAll(Utils.getInstance().getDatabase().articlesDao().getAll());
                        adapter.notifyDataSetChanged();
                        Aid.crossfade(recyclerView, progressBar);
                    } else {
                        Log.d(Constants.TAG, response);
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
