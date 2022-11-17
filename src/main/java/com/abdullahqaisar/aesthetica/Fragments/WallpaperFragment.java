package com.abdullahqaisar.aesthetica.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.TextView;

import com.abdullahqaisar.aesthetica.R;
import com.abdullahqaisar.aesthetica.WallpaperAdapter;
import com.abdullahqaisar.aesthetica.WallpaperModel;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WallpaperFragment extends Fragment {

    EditText et_searchWallpaper;
    RecyclerView recyclerView;
    WallpaperAdapter wallpaperAdapter;
    private ChipNavigationBar categoriesNavigationBar;
    List<WallpaperModel> wallpaperModelList;
    int pageNumber = 1;

    Boolean isScrolling = false;
    int currentItems, totalItems, scrollOutItems;
    String url = "https://api.pexels.com/v1/search?page="+pageNumber+"&per_page=80&query=aesthetic";
    public WallpaperFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_wallpaper, container, false);

        et_searchWallpaper = view.findViewById(R.id.et_searchWallpaper);
        recyclerView = view.findViewById(R.id.recyclerView);
        categoriesNavigationBar = view.findViewById(R.id.categoriesNavigation);
        categoriesNavigationBar.setItemSelected(R.id.aesthetic, true);
        wallpaperModelList = new ArrayList<>();
        wallpaperAdapter = new WallpaperAdapter(this.getContext(), wallpaperModelList);
        recyclerView.setAdapter(wallpaperAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);


        et_searchWallpaper.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String query = et_searchWallpaper.getText().toString().toLowerCase();
                    categoriesNavigationBar.setItemSelected(R.id.aesthetic, false);
                    categoriesNavigationBar.setItemSelected(R.id.nature, false);
                    categoriesNavigationBar.setItemSelected(R.id.retro, false);
                    categoriesNavigationBar.setItemSelected(R.id.abstractWallpaper, false);
                    searchWallpapers(query);
                    handled = true;
                }
                return handled;
            }
        });

        categoriesNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i) {
                    case R.id.aesthetic:
                        searchWallpapers("aesthetic");
                        break;
                    case R.id.nature:
                        searchWallpapers("nature");
                        break;
                    case R.id.retro:
                        searchWallpapers("retro");
                        break;
                    case R.id.abstractWallpaper:
                        searchWallpapers("abstract");
                        break;
                }
            }
        });


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currentItems = gridLayoutManager.getChildCount();
                totalItems = gridLayoutManager.getItemCount();
                scrollOutItems = gridLayoutManager.findFirstVisibleItemPosition();

                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
                    isScrolling = false;
                    fetchWallpaper();
                }
            }
        });
        fetchWallpaper();
        return view;
    }

    public void searchWallpapers(String searchWallpaper){
        url = "https://api.pexels.com/v1/search?page="+pageNumber+"&per_page=80&query="+searchWallpaper;
        wallpaperModelList.clear();
        fetchWallpaper();
    }

    public void fetchWallpaper() {

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("photos");

                            int length = jsonArray.length();
                            for (int i = 0; i < length; i++) {

                                JSONObject object = jsonArray.getJSONObject(i);
                                int id = object.getInt("id");

                                JSONObject objectImages = object.getJSONObject("src");
                                String originalUrl = objectImages.getString("original");
                                String mediumlUrl = objectImages.getString("medium");

                                WallpaperModel wallpaperModel = new WallpaperModel(id, originalUrl, mediumlUrl);
                                wallpaperModelList.add(wallpaperModel);
                            }

                            wallpaperAdapter.notifyDataSetChanged();
                            pageNumber++;

                        } catch (JSONException e) {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "563492ad6f91700001000001f5c6ae041d1a42d7b9c47433de6caf61");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(request);
    }



}