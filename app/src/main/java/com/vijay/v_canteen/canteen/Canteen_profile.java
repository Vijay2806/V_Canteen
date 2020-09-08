package com.vijay.v_canteen.canteen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.vijay.v_canteen.R;
import com.vijay.v_canteen.admin.Admin_manage_canteen;
import com.vijay.v_canteen.admin.Admin_view_canteen;
import com.vijay.v_canteen.util.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Canteen_profile extends AppCompatActivity {
    ImageView image;
    EditText email,mobile,landline;
    TextView tname,temail,tmobile,tlandine,tid;
    String sname,sid,semail,smobile,sland;
    Button update,cancel,delete;
    RelativeLayout relativeLayout1,relativeLayout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canteen_profile);

        relativeLayout1=findViewById(R.id.cp_r1);
        relativeLayout2=findViewById(R.id.cp_r2);

        image=findViewById(R.id.cp_image);

        email=findViewById(R.id.cp_edt_email);
        mobile=findViewById(R.id.cp_edt_mobile);
        landline=findViewById(R.id.cp_edt_landline);

        update=findViewById(R.id.cp_update_canteen);
        cancel=findViewById(R.id.cp_cancel_canteen);

        relativeLayout2.setVisibility(View.GONE);

        tname=findViewById(R.id.cp_name);
        tid=findViewById(R.id.cp_id);
        temail=findViewById(R.id.cp_email);
        tmobile=findViewById(R.id.cp_phone);
        tlandine=findViewById(R.id.cp_landline);

        tname.setText(SharedPreference.get("cname"));
        tid.setText(SharedPreference.get("cid"));
        temail.setText(SharedPreference.get("cemail"));
        tmobile.setText(SharedPreference.get("cphone"));
        tlandine.setText(SharedPreference.get("clandline"));

        email.setText(SharedPreference.get("cemail"));
        mobile.setText(SharedPreference.get("cphone"));
        landline.setText(SharedPreference.get("clandline"));

        sid=SharedPreference.get("cid");

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
    }

    private void updatecanteen(final String semail, final String smobile, final String sland, final String sid) {
        StringRequest request=new StringRequest(Request.Method.POST, getText(R.string.url) + "update_canteen.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jobj=new JSONObject(response);
                    Log.i("vjnarayanan",response);
                    if (jobj.getString("status").equals("0")){
                        Toast.makeText(Canteen_profile.this, jobj.getString("message"), Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Canteen_profile.this, jobj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(Canteen_profile.this, jobj.getString("message"), Toast.LENGTH_SHORT).show();
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
        Intent intent1=new Intent(Canteen_profile.this, Canteen_home.class);
        startActivity(intent1);
        finish();
    }
}
