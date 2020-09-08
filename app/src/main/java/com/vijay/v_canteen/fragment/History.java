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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vijay.v_canteen.R;
import com.vijay.v_canteen.adapter.User_history_adapter;
import com.vijay.v_canteen.pojo.History_getter;
import com.vijay.v_canteen.util.SharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class History extends Fragment {
    RecyclerView history;
    ArrayList<History_getter> allhistory;
    User_history_adapter recycleradapter;
    String uid;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container,
                             @NonNull Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_history, container, false);

        history=v.findViewById(R.id.history_recycler);
        history.setLayoutManager(new LinearLayoutManager(getActivity()));
        history.setHasFixedSize(true);
        uid= SharedPreference.get("userial");
        loadhistory(uid);
        return v;
    }

    private void loadhistory(String uid) {
        StringRequest request = new StringRequest(Request.Method.POST, getString(R.string.url) + "get_history.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("vic", response);
                try {
                    JSONObject job = new JSONObject(response);
                    if (job.getString("status").equals("0")) {
                        Log.i("vjnarayanan", response);
                        JSONArray jarr = job.getJSONArray("data");
                        allhistory = new ArrayList<>();
                        for (int i = 0; i < jarr.length(); i++) {
                            JSONObject jo = jarr.getJSONObject(i);
                            Log.i("vjnarayanan", jo.getInt("i_count") +"  "+ jo.getString("i_name") +"  "+ jo.getString("o_total") +"  "+ jo.getString("o_status"));

                            History_getter histories = new History_getter(jo.getString("i_count"), jo.getString("i_name"), jo.getString("o_total"), jo.getString("o_status"));
                            allhistory.add(histories);
                        }
                        recycleradapter = new User_history_adapter(allhistory, getActivity());
                        history.setAdapter(recycleradapter);

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
                params.put("id", History.this.uid);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }
}
