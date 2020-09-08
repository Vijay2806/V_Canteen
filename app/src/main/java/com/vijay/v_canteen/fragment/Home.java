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
import com.vijay.v_canteen.adapter.User_viewcanteen_adapter;
import com.vijay.v_canteen.pojo.canteen_getter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Home extends Fragment {
    RecyclerView canteen;
    ArrayList<canteen_getter> alcanteen;
    User_viewcanteen_adapter recycleradapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@NonNull ViewGroup container,
                             @NonNull Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_home, container, false);

        canteen=v.findViewById(R.id.user_canteen_recycler_view);
        canteen.setLayoutManager(new LinearLayoutManager(getActivity()));
        canteen.setHasFixedSize(true);
        loadcanteen();
        return v;
    }

    private void loadcanteen() {
        StringRequest request=new StringRequest(Request.Method.POST, getString(R.string.url)+"get_canteen.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("vic",response);
                try {
                    JSONObject job=new JSONObject(response);
                    if(job.getString("status").equals("0")){
                        Log.i("vjnarayanan",response);
                        JSONArray jarr=job.getJSONArray("data");
                        alcanteen=new ArrayList<>();
                        for(int i=0;i<jarr.length();i++){
                            JSONObject jo=jarr.getJSONObject(i);
                            Log.i("vjnarayanan",jo.getInt("c_id")+"   "+jo.getString("c_name")+"   "+jo.getString("c_image")+"   "+jo.getString("c_phone")+"   "+jo.getString("c_email")+"   "+jo.getString("c_landline"));
                            canteen_getter canteenone=new canteen_getter(jo.getInt("c_id"),jo.getString("c_name"),jo.getString("c_image"),jo.getString("c_phone"),jo.getString("c_email"),jo.getString("c_landline"));
                            alcanteen.add(canteenone);
                        }
                        recycleradapter =new User_viewcanteen_adapter(alcanteen,getActivity());
                        canteen.setAdapter(recycleradapter);

                    }
                    else{
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
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                return params;
            }
        };
        RequestQueue queue= Volley.newRequestQueue(getActivity());
        queue.add(request);

    }
}
