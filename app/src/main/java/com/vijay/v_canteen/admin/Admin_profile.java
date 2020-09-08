package com.vijay.v_canteen.admin;

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
import com.vijay.v_canteen.util.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Admin_profile extends AppCompatActivity {
    TextView name,id,phone,email;
    EditText edt_phone,edt_email;
    Button update,cancel;
    RelativeLayout r1,r2;
    String semail,sphone,sserial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);

        name=findViewById(R.id.ap_name);
        id=findViewById(R.id.ap_id);
        phone=findViewById(R.id.ap_phone);
        email=findViewById(R.id.ap_email);

        edt_email=findViewById(R.id.ap_edt_email);
        edt_phone=findViewById(R.id.ap_edt_phone);

        update=findViewById(R.id.ap_update);
        cancel=findViewById(R.id.ap_cancel);

        r1=findViewById(R.id.ap_r1);
        r2=findViewById(R.id.ap_r2);

        r2.setVisibility(View.GONE);
        r1.setVisibility(View.VISIBLE);

        name.setText(SharedPreference.get("aname"));
        id.setText(SharedPreference.get("aid"));
        phone.setText(SharedPreference.get("aphone"));
        email.setText(SharedPreference.get("aemail"));

        edt_phone.setText(SharedPreference.get("aphone"));
        edt_email.setText(SharedPreference.get("aemail"));

        sserial=SharedPreference.get("aserial");

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                r1.setVisibility(View.VISIBLE);
                r2.setVisibility(View.GONE);
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateemail() | !validatephone()){
                    return;
                }
                else {
                    semail=edt_email.getText().toString().trim();
                    sphone=edt_phone.getText().toString().trim();
                    updateprofile(semail,sphone,sserial);
                }
            }
        });

    }

    private void updateprofile(final String semail, final String sphone, final String sserial) {
        StringRequest request=new StringRequest(Request.Method.POST, getText(R.string.url) + "admin_update.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jobj=new JSONObject(response);
                    Log.i("vjnarayanan",response);
                    if (jobj.getString("status").equals("0")){
                        Toast.makeText(Admin_profile.this, jobj.getString("message"), Toast.LENGTH_SHORT).show();
                        r1.setVisibility(View.VISIBLE);
                        r2.setVisibility(View.GONE);
                        SharedPreference.removeKey("aemail");
                        SharedPreference.removeKey("aphone");
                        SharedPreference.save("aemail",semail);
                        SharedPreference.save("aphone",sphone);
                        phone.setText(sphone);
                        email.setText(semail);
                        edt_email.setText(semail);
                        edt_phone.setText(sphone);
                    }
                    else if (jobj.getString("status").equals("1")){
                        Toast.makeText(Admin_profile.this, jobj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(Admin_profile.this, jobj.getString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("id",sserial);
                params.put("phone",sphone);
                params.put("email",semail);
                return params;
            }
        };
        RequestQueue que= Volley.newRequestQueue(getApplicationContext());
        que.add(request);
    }

    private boolean validatephone() {
        if (edt_email.getText().toString().trim().equals("")){
            edt_phone.setError("Enter phone");
            return false;
        }
        else {
            return true;
        }
    }

    private boolean validateemail() {
        if (edt_email.getText().toString().trim().equals("")){
            edt_email.setError("Enter email");
            return false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(edt_email.getText().toString().trim()).matches()){
            edt_email.setError("Enter valid email");
            return false;
        }
        else {
            return true;
        }
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
                r1.setVisibility(View.GONE);
                r2.setVisibility(View.VISIBLE);
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        Intent back=new Intent(Admin_profile.this,Admin_home.class);
        startActivity(back);
        finish();
    }
}
