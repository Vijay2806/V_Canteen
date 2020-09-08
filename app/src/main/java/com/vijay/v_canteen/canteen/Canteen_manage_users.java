package com.vijay.v_canteen.canteen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vijay.v_canteen.R;
import com.vijay.v_canteen.adapter.Canteen_user_recycleradapter;
import com.vijay.v_canteen.adapter.User_recycleradapter;
import com.vijay.v_canteen.admin.Admin_manage_user;
import com.vijay.v_canteen.pojo.User_getter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Canteen_manage_users extends AppCompatActivity {
    RecyclerView recycler;
    ArrayList<User_getter> alusers;
    Canteen_user_recycleradapter recycleradapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canteen_manage_users);

        recycler=findViewById(R.id.cmu_recycler);
        recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recycler.setHasFixedSize(true);
        loaduser();

    }

    private void loaduser() {
        StringRequest request=new StringRequest(Request.Method.POST, getString(R.string.url)+"user_getter.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("vic",response);
                try {
                    JSONObject job=new JSONObject(response);
                    if(job.getString("status").equals("0")){
                        Log.i("vjnarayanan",response);
                        JSONArray jarr=job.getJSONArray("data");
                        alusers=new ArrayList<>();
                        for(int i=0;i<jarr.length();i++){
                            JSONObject jo=jarr.getJSONObject(i);
                            Log.i("vjnarayanan",jo.getInt("u_serial")+"   "+jo.getString("u_firstname")+"   "+jo.getString("u_lastname")+"   "+jo.getString("u_email")+"   "+jo.getString("u_phone")+"   "+jo.getString("u_password")+"   "+jo.getString("u_id")+"   "+jo.getString("u_gender")+"   "+jo.getString("u_photo")+"   "+jo.getString("u_dob")+"   "+jo.getString("u_block"));

                            User_getter user=new User_getter(jo.getInt("u_serial"),jo.getString("u_firstname"),jo.getString("u_lastname"),jo.getString("u_email"),jo.getString("u_phone"),jo.getString("u_password"),jo.getString("u_id"),jo.getString("u_gender"),jo.getString("u_photo"),jo.getString("u_dob"),jo.getString("u_block"));
                            alusers.add(user);
                        }
                        recycleradapter =new Canteen_user_recycleradapter(alusers,getApplicationContext());
                        recycler.setAdapter(recycleradapter);

                    }
                    else{
                        Toast.makeText(Canteen_manage_users.this, job.getString("message"), Toast.LENGTH_SHORT).show();
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
        Intent back=new Intent(Canteen_manage_users.this,Canteen_home.class);
        startActivity(back);
        finish();
    }
}
