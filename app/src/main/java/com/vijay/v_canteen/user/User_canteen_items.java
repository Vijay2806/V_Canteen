package com.vijay.v_canteen.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vijay.v_canteen.R;
import com.vijay.v_canteen.adapter.Item_recycleradapter;
import com.vijay.v_canteen.adapter.User_item_recycleradapter;
import com.vijay.v_canteen.canteen.Canteen_add_item;
import com.vijay.v_canteen.canteen.Canteen_home;
import com.vijay.v_canteen.canteen.Canteen_item_recyclerview;
import com.vijay.v_canteen.pojo.Item_getter;
import com.vijay.v_canteen.util.SharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User_canteen_items extends AppCompatActivity {
    RecyclerView rview;
    ArrayList<Item_getter> alitem;
    User_item_recycleradapter recycleradapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_canteen_items);
//
        Log.i("vjnarayanan", SharedPreference.get("canteenid"));

        rview=findViewById(R.id.uci_recycler);
        Intent next=getIntent();
        String cid=next.getStringExtra("cid");
        Log.i("vj",cid);

        loaddata(cid);

    }

    private void loaddata(final String cid) {
        StringRequest request=new StringRequest(Request.Method.POST, getString(R.string.url)+"user_selected_canteen_item.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("vic",response);
                try {
                    JSONObject job=new JSONObject(response);
                    if(job.getString("status").equals("0")){
                        Log.i("vjnarayanan",response);
                        JSONArray jarr=job.getJSONArray("data");
                        rview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        rview.setHasFixedSize(true);
                        alitem=new ArrayList<>();
                        for(int i=0;i<jarr.length();i++){
                            JSONObject jo=jarr.getJSONObject(i);
                            Log.i("vjnarayanan",jo.getInt("i_id")+jo.getString("i_name")+jo.getString("i_type")+jo.getString("i_price")+jo.getString("i_quantity")+jo.getString("c_id"));

                            Item_getter item=new Item_getter(jo.getInt("i_id"),jo.getString("i_name"),jo.getString("i_type"),jo.getString("i_price"),jo.getString("i_quantity"),jo.getString("i_image"),jo.getString("c_id"));
                            alitem.add(item);
                        }
                        recycleradapter =new User_item_recycleradapter(alitem,getApplicationContext());
                        rview.setAdapter(recycleradapter);

                    }
                    else{
                        Toast.makeText(User_canteen_items.this, job.getString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("id",cid);
                return params;
            }
        };
        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

    @Override
    public void onBackPressed() {
        SharedPreference.removeKey("canteenid");
        Intent back= new Intent(User_canteen_items.this, User_home.class);
        startActivity(back);
        finish();
    }
}
