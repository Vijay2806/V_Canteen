package com.vijay.v_canteen.admin;

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
import com.vijay.v_canteen.adapter.Admin_canteen_recycleradapter;
import com.vijay.v_canteen.pojo.canteen_getter;
import com.vijay.v_canteen.util.SharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Admin_manage_canteen extends AppCompatActivity {
    RecyclerView canteen;
    Button addbtn;
    ArrayList<canteen_getter> alcanteen;
    Admin_canteen_recycleradapter recycleradapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_canteen);

        SharedPreference.initialize(getApplication());

        canteen=findViewById(R.id.cantee_recycler_view);

        addbtn=findViewById(R.id.add_new_canteen_two);

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add=new Intent(Admin_manage_canteen.this,Admin_add_canteen.class);
                startActivity(add);
                finish();
            }
        });
        loadcanteen();
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
                        canteen.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        canteen.setHasFixedSize(true);
                        alcanteen=new ArrayList<>();
                        for(int i=0;i<jarr.length();i++){
                            JSONObject jo=jarr.getJSONObject(i);
                            Log.i("vjnarayanan",jo.getInt("c_id")+"   "+jo.getString("c_name")+"   "+jo.getString("c_image")+"   "+jo.getString("c_phone")+"   "+jo.getString("c_email")+"   "+jo.getString("c_landline"));

                            canteen_getter canteenone=new canteen_getter(jo.getInt("c_id"),jo.getString("c_name"),jo.getString("c_image"),jo.getString("c_phone"),jo.getString("c_email"),jo.getString("c_landline"));
                            alcanteen.add(canteenone);
                        }
                        recycleradapter =new Admin_canteen_recycleradapter(alcanteen,getApplicationContext());
                        canteen.setAdapter(recycleradapter);

                    }
                    else{
                        Toast.makeText(Admin_manage_canteen.this, job.getString("message"), Toast.LENGTH_SHORT).show();
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
        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(Admin_manage_canteen.this,Admin_home.class);
        startActivity(intent);
        finish();
    }
}

