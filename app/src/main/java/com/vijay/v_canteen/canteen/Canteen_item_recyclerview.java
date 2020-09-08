package com.vijay.v_canteen.canteen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vijay.v_canteen.R;
import com.vijay.v_canteen.adapter.Item_recycleradapter;
import com.vijay.v_canteen.pojo.Item_getter;
import com.vijay.v_canteen.util.SharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Canteen_item_recyclerview extends AppCompatActivity {
    Button additem;
    RecyclerView rview;
    ArrayList<Item_getter> alitem;
    Item_recycleradapter recycleradapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canteen_view_item);

        additem=findViewById(R.id.acvi_add);
        rview=findViewById(R.id.acvi_recycler);

        String cid= SharedPreference.get("cid");
        Log.i("vjnarayanan",SharedPreference.get("cid"));
        
        loaddata(cid);

        additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Canteen_item_recyclerview.this,Canteen_add_item.class);
                startActivity(i);
                finish();
            }
        });


    }

    private void loaddata(final String cid) {
        StringRequest request=new StringRequest(Request.Method.POST, getString(R.string.url)+"get_item.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("vjnarayan",response);
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
                            Log.i("vjnarayanan",jo.getInt("i_id")+jo.getString("i_name")+jo.getString("i_type")+jo.getString("i_price")+jo.getString("i_quantity")+ jo.getString("c_id"));

                            Item_getter item=new Item_getter(jo.getInt("i_id"),jo.getString("i_name"),jo.getString("i_type"),jo.getString("i_price"),jo.getString("i_quantity"),jo.getString("i_image"),jo.getString("c_id"));
                            alitem.add(item);
                        }
                        recycleradapter =new Item_recycleradapter(alitem,getApplicationContext());
                        rview.setAdapter(recycleradapter);

                    }
                    else{
                        Toast.makeText(Canteen_item_recyclerview.this, job.getString("message"), Toast.LENGTH_SHORT).show();
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
        Intent back= new Intent(Canteen_item_recyclerview.this,Canteen_home.class);
        startActivity(back);
        finish();
    }
}
