package com.vijay.v_canteen.user;

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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vijay.v_canteen.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class User_profile extends AppCompatActivity {
    CircleImageView profilepic;
    EditText name,fname,lname,phone,email,userid,dob;
    Button updatebtn,cancelbtn;
    String ufname,ulname,uphone,uemail,udob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        profilepic=findViewById(R.id.profilepic_img);

        name=findViewById(R.id.profile_name_edt);
        fname=findViewById(R.id.profile_fname_edt);
        lname=findViewById(R.id.profile_lname_edt);
        phone=findViewById(R.id.profile_phone_edt);
        email=findViewById(R.id.profile_email_edt);
        userid=findViewById(R.id.profile_user_id_edt);
        dob=findViewById(R.id.profile_dob_edt);

        updatebtn=findViewById(R.id.update_profile_button);
        cancelbtn=findViewById(R.id.cancel_profile_btn);

        disableedit();

        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!fnamevalidate() | !lnamevalidate() | !phonevalidate() | !emailvalidate() | !dobvalidate()){
                    Toast.makeText(User_profile.this, "Fill all details", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    ufname=fname.getText().toString().trim();
                    ulname=lname.getText().toString().trim();
                    uphone=phone.getText().toString().trim();
                    uemail=email.getText().toString().trim();
                    udob=dob.getText().toString().trim();

                    updateprofile(ufname,ulname,uphone,uemail,udob);

                }

            }
        });

        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableedit();
            }
        });




    }

    private void updateprofile(final String ufname, final String ulname, final String uphone, final String uemail, final String udob) {

        StringRequest request=new StringRequest(Request.Method.POST, getString(R.string.url) + "profile_update.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jobj=new JSONObject(response);
                    if (jobj.getString("status").equals("0")){
                        Log.i("vjnarayanan",jobj.getString("message"));
                        Toast.makeText(User_profile.this, jobj.getString("message"), Toast.LENGTH_SHORT).show();
                        disableedit();
                    }
                    else if (jobj.getString("status").equals("1")){
                        Log.i("vjnarayanan",jobj.getString("message"));
                        Toast.makeText(User_profile.this, jobj.getString("message"), Toast.LENGTH_SHORT).show();
                        clear();
                    }
                    else
                        Log.i("vjnarayanan",jobj.getString("message"));
                        Toast.makeText(User_profile.this, jobj.getString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("fname",ufname);
                params.put("lname",ulname);
                params.put("phone",uphone);
                return params;
            }
        };

        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

    private void clear() {
        phone.setText("");
        email.setText("");
    }

    private boolean dobvalidate() {
        if (dob.getText().toString().trim().equals("")){
            return false;
        }
        else
            return true;
    }

    private boolean emailvalidate() {
        if (email.getText().toString().trim().equals("")){
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString().trim()).matches()){
            email.setError("Invalid email");
            return false;
        }
        else
            return true;
    }

    private boolean phonevalidate() {
        if (phone.getText().toString().trim().equals("")){
            return false;
        }
        else
            return true;
    }

    private boolean lnamevalidate() {
        if (lname.getText().toString().trim().equals("")){
            return false;
        }
        else
            return true;
    }

    private boolean fnamevalidate() {
        if (fname.getText().toString().trim().equals("")){
            return false;
        }
        else
            return true;
    }

    private void disableedit() {
        name.setFocusable(false);
        name.setVisibility(View.VISIBLE);
        fname.setVisibility(View.INVISIBLE);
        lname.setVisibility(View.INVISIBLE);
        fname.setFocusable(false);
        lname.setFocusable(false);
        phone.setFocusable(false);
        email.setFocusable(false);
        userid.setFocusable(false);
        dob.setFocusable(false);

        updatebtn.setFocusable(false);
        cancelbtn.setFocusable(false);
        updatebtn.setVisibility(View.INVISIBLE);
        cancelbtn.setVisibility(View.INVISIBLE);
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
        name.setVisibility(View.INVISIBLE );
        fname.setFocusableInTouchMode(true);
        fname.setFocusable(true);
        fname.setVisibility(View.VISIBLE);
        lname.setFocusableInTouchMode(true);
        lname.setFocusable(true);
        lname.setVisibility(View.VISIBLE);
        email.setFocusableInTouchMode(true);
        email.setFocusable(true);



        updatebtn.setFocusable(true);
        updatebtn.setVisibility(View.VISIBLE);
        cancelbtn.setFocusable(true);
        cancelbtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        Intent home=new Intent(User_profile.this, User_home.class);
        startActivity(home);
        finish();
    }
}
