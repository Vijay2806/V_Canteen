package com.vijay.v_canteen.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.vijay.v_canteen.R;
import com.vijay.v_canteen.user.MainActivity;
import com.vijay.v_canteen.user.Register_page;
import com.vijay.v_canteen.util.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Admin_view_canteen extends AppCompatActivity {

    ImageView image;
    EditText email,mobile,landline;
    TextView tname,temail,tmobile,tlandine,tid;
    String sname,sid,semail,smobile,sland;
    Button update,cancel,delete;
    RelativeLayout relativeLayout1,relativeLayout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_canteen);

        SharedPreference.initialize(getApplication());

        relativeLayout1=findViewById(R.id.avc_r1);
        relativeLayout2=findViewById(R.id.avc_r2);

        image=findViewById(R.id.avc_image);

        email=findViewById(R.id.avc_edt_email);
        mobile=findViewById(R.id.avc_edt_mobile);
        landline=findViewById(R.id.avc_edt_landline);

        update=findViewById(R.id.ac_update_canteen);
        cancel=findViewById(R.id.ac_cancel_canteen);
        delete=findViewById(R.id.avc_delete_btn);

        relativeLayout2.setVisibility(View.GONE);

        tname=findViewById(R.id.avc_name);
        tid=findViewById(R.id.avc_id);
        temail=findViewById(R.id.avc_email);
        tmobile=findViewById(R.id.avc_phone);
        tlandine=findViewById(R.id.avc_landline);

        tname.setText(SharedPreference.get("name"));
        tid.setText(SharedPreference.get("id"));
        temail.setText(SharedPreference.get("email"));
        tmobile.setText(SharedPreference.get("phone"));
        tlandine.setText(SharedPreference.get("landline"));

        email.setText(SharedPreference.get("email"));
        mobile.setText(SharedPreference.get("phone"));
        landline.setText(SharedPreference.get("landline"));

        sid=SharedPreference.get("id");

        Picasso.with(getApplicationContext()).load("http://192.168.0.103/API/V_canteen/photo/canteen/"+SharedPreference.get("image")).fit().into(image);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateemail() | !validatemobile() | !validateland()){
                    return;
                }
                else {
                    semail=email.getText().toString().trim();
                    smobile=mobile.getText().toString().trim();
                    sland=landline.getText().toString().trim();
                    updatecanteen(semail,smobile,sland,sid);
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableedit();

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Admin_view_canteen.this)
                        .setMessage("Do you want to remove canteen")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deletecanteen(sid);
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




    }

    private void updatecanteen(final String semail, final String smobile, final String sland, final String sid) {
        StringRequest request=new StringRequest(Request.Method.POST, getText(R.string.url) + "update_canteen.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jobj=new JSONObject(response);
                    Log.i("vjnarayanan",response);
                    if (jobj.getString("status").equals("0")){
                        Toast.makeText(Admin_view_canteen.this, jobj.getString("message"), Toast.LENGTH_SHORT).show();
                        disableedit();
                        SharedPreference.removeKey("email");
                        SharedPreference.removeKey("phone");
                        SharedPreference.removeKey("landline");
                        SharedPreference.save("phone",smobile);
                        SharedPreference.save("email",semail);
                        SharedPreference.save("landline",sland);
                        temail.setText(semail);
                        tmobile.setText(smobile);
                        tlandine.setText(sland);
                    }
                    else if (jobj.getString("status").equals("1")){
                        Toast.makeText(Admin_view_canteen.this, jobj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(Admin_view_canteen.this, jobj.getString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("email",semail);
                params.put("mobile",smobile);
                params.put("land",sland);
                params.put("id",sid);
                return params;
            }
        };
        RequestQueue que= Volley.newRequestQueue(getApplicationContext());
        que.add(request);
    }

    private boolean validateland() {
        if (landline.getText().toString().trim().equals("")){
            landline.setError("Enter landline ext");
            return false;
        }
        else {
            return true;
        }
    }

    private boolean validatemobile() {
        if (mobile.getText().toString().trim().equals("")){
            mobile.setError("Enter mobile no");
            return false;
        }
        else {
            return true;
        }
    }

    private boolean validateemail() {
        if (email.getText().toString().trim().equals("")){
            email.setError("Enter email");
            return false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()){
            email.setError("Invalid Email");
            return false;
        }
        else {
            return true;
        }
    }

    private void disableedit() {
        relativeLayout1.setVisibility(View.VISIBLE);
        relativeLayout2.setVisibility(View.GONE);
    }

    private void deletecanteen(final String sid) {
        StringRequest request=new StringRequest(Request.Method.POST, getText(R.string.url) + "delete_canteen.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jobj=new JSONObject(response);
                    Log.i("vjnarayanan",response);
                    if (jobj.getString("status").equals("0")){
                        Toast.makeText(Admin_view_canteen.this, jobj.getString("message"), Toast.LENGTH_SHORT).show();
                        Intent back=new Intent(Admin_view_canteen.this,Admin_manage_canteen.class);
                        startActivity(back);
                        finish();
                    }
                    else if (jobj.getString("status").equals("1")){
                        Toast.makeText(Admin_view_canteen.this, jobj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(Admin_view_canteen.this, jobj.getString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("id",sid);
                return params;
            }
        };
        RequestQueue que= Volley.newRequestQueue(getApplicationContext());
        que.add(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_profile,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.edit_profile_option:
                enableedit();
                return true;
        }
        return false;
    }

    private void enableedit() {
        relativeLayout2.setVisibility(View.VISIBLE);
        relativeLayout1.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        removesp();
        Intent intent1=new Intent(Admin_view_canteen.this,Admin_manage_canteen.class);
        startActivity(intent1);
        finish();
    }

    private void removesp() {
        SharedPreference.removeKey("id");
        SharedPreference.removeKey("name");
        SharedPreference.removeKey("email");
        SharedPreference.removeKey("phone");
        SharedPreference.removeKey("landline");
        SharedPreference.removeKey("image");
    }

    @Override
    protected void onStop() {
        removesp();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        removesp();
        super.onDestroy();
    }
}
