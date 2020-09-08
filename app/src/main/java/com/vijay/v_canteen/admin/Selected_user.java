package com.vijay.v_canteen.admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.vijay.v_canteen.util.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Selected_user extends AppCompatActivity {
    TextView name,id,phone,email,dob,gender;
    Button remove,block,unblock;
    String serial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_selected_user);

        name=findViewById(R.id.asu_name);
        id=findViewById(R.id.asu_id);
        phone=findViewById(R.id.asu_phone);
        email=findViewById(R.id.asu_email);
        dob=findViewById(R.id.asu_dob);
        gender=findViewById(R.id.asu_gender);

        remove=findViewById(R.id.asu_remove);
        block=findViewById(R.id.asu_block);
        unblock=findViewById(R.id.asu_unblock);

        serial=SharedPreference.get("auserial");

        name.setText(SharedPreference.get("aufirst")+" "+SharedPreference.get("aulast"));
        id.setText(SharedPreference.get("auid"));
        phone.setText(SharedPreference.get("auphone"));
        email.setText(SharedPreference.get("auemail"));
        dob.setText(SharedPreference.get("audob"));
        gender.setText(SharedPreference.get("augender"));

        if (SharedPreference.get("aublock").equals("0")){
            unblock.setVisibility(View.INVISIBLE);
        }
        else
        {
            block.setVisibility(View.INVISIBLE);
        }

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Selected_user.this)
                        .setMessage("Do you want to remove user")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removeuser(serial);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        })
                        .show();
            }
        });

        block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blockuser(serial);
            }
        });

        unblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unblockuser(serial);

            }
        });
    }

    private void removeuser(final String serial) {
        StringRequest request=new StringRequest(Request.Method.POST, getText(R.string.url) + "remove_user.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jobj=new JSONObject(response);
                    Log.i("vjnarayanan",response);
                    if (jobj.getString("status").equals("0")){
                        Toast.makeText(Selected_user.this, jobj.getString("message"), Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(Selected_user.this, Admin_manage_user.class);
                        startActivity(i);
                        finish();
                        removesharedp();
                    }
                    else if (jobj.getString("status").equals("1")){
                        Toast.makeText(Selected_user.this, jobj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(Selected_user.this, jobj.getString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("serial",serial);
                return params;
            }
        };
        RequestQueue que= Volley.newRequestQueue(getApplicationContext());
        que.add(request);
    }

    private void blockuser(final String serial) {
        StringRequest request=new StringRequest(Request.Method.POST, getText(R.string.url) + "block_user.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jobj=new JSONObject(response);
                    Log.i("vjnarayanan",response);
                    if (jobj.getString("status").equals("0")){
                        Toast.makeText(Selected_user.this, jobj.getString("message"), Toast.LENGTH_SHORT).show();
                        block.setVisibility(View.INVISIBLE);
                        unblock.setVisibility(View.VISIBLE);
                        SharedPreference.removeKey("uablock");
                        SharedPreference.save("uablock","1");
                    }
                    else if (jobj.getString("status").equals("1")){
                        Toast.makeText(Selected_user.this, jobj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(Selected_user.this, jobj.getString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("serial",serial);
                return params;
            }
        };
        RequestQueue que= Volley.newRequestQueue(getApplicationContext());
        que.add(request);
    }

    private void unblockuser(final String serial) {
        StringRequest request=new StringRequest(Request.Method.POST, getText(R.string.url) + "unblock_user.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jobj=new JSONObject(response);
                    Log.i("vjnarayanan",response);
                    if (jobj.getString("status").equals("0")){
                        Toast.makeText(Selected_user.this, jobj.getString("message"), Toast.LENGTH_SHORT).show();
                        unblock.setVisibility(View.INVISIBLE);
                        block.setVisibility(View.VISIBLE);
                        SharedPreference.removeKey("uablock");
                        SharedPreference.save("uablock","0");
                    }
                    else if (jobj.getString("status").equals("1")){
                        Toast.makeText(Selected_user.this, jobj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(Selected_user.this, jobj.getString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("serial",serial);
                return params;
            }
        };
        RequestQueue que= Volley.newRequestQueue(getApplicationContext());
        que.add(request);
    }

    @Override
    public void onBackPressed() {
        removesharedp();
        Intent back=new Intent(Selected_user.this,Admin_manage_user.class);
        startActivity(back);
        finish();
    }

    private void removesharedp() {
        SharedPreference.removeKey("auserial");
        SharedPreference.removeKey("aufirst");
        SharedPreference.removeKey("aulast");
        SharedPreference.removeKey("auemail");
        SharedPreference.removeKey("auphone");
        SharedPreference.removeKey("augender");
        SharedPreference.removeKey("audob");
        SharedPreference.removeKey("aublock");
        SharedPreference.removeKey("auphoto");
        SharedPreference.removeKey("auid");
    }

    @Override
    protected void onStop() {
        removesharedp();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        removesharedp();
        super.onDestroy();
    }
}
