package com.vijay.v_canteen.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vijay.v_canteen.R;
import com.vijay.v_canteen.adapter.User_item_recycleradapter;
import com.vijay.v_canteen.adapter.User_viewcanteen_adapter;
import com.vijay.v_canteen.pojo.Item_getter;
import com.vijay.v_canteen.pojo.canteen_getter;
import com.vijay.v_canteen.user.User_canteen_items;
import com.vijay.v_canteen.util.SharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Favorite extends Fragment {
    RecyclerView favorites;
    ArrayList<Item_getter> alitem;
    User_item_recycleradapter recycleradapter;
    String uid;
    TextView fav_txt;

    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @NonNull ViewGroup container,
                              @NonNull Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_favorite, container, false);

        fav_txt=v.findViewById(R.id.fav_txt);

        favorites=v.findViewById(R.id.favorite_recycler);
        favorites.setLayoutManager(new LinearLayoutManager(getActivity()));
        favorites.setHasFixedSize(true);
        uid= SharedPreference.get("userial");
        fav_txt.setVisibility(View.GONE);
        loadfavorites(uid);
        return v;
    }

    private void loadfavorites(String uid) {
        StringRequest request = new StringRequest(Request.Method.POST, getString(R.string.url) + "get_favorite_item.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("vic", response);
                try {
                    JSONObject job = new JSONObject(response);
                    if (job.getString("status").equals("0")) {
                        Log.i("vjnarayanan", response);
                        JSONArray jarr = job.getJSONArray("data");
                        alitem = new ArrayList<>();
                        for (int i = 0; i < jarr.length(); i++) {
                            JSONObject jo = jarr.getJSONObject(i);
                            Log.i("vjnarayanan", jo.getInt("i_id") + jo.getString("i_name") + jo.getString("i_type") + jo.getString("i_price") + jo.getString("i_quantity") + jo.getString("c_id"));

                            Item_getter item = new Item_getter(jo.getInt("i_id"), jo.getString("i_name"), jo.getString("i_type"), jo.getString("i_price"), jo.getString("i_quantity"), jo.getString("i_image"), jo.getString("c_id"));
                            alitem.add(item);
                        }
                        recycleradapter = new User_item_recycleradapter(alitem, getActivity());
                        favorites.setAdapter(recycleradapter);

                    } else {
                        Toast.makeText(getActivity(), job.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", Favorite.this.uid);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }
}
