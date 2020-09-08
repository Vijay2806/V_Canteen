package com.vijay.v_canteen.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.vijay.v_canteen.canteen.Canteen_Login;
import com.vijay.v_canteen.user.MainActivity;
import com.vijay.v_canteen.R;
import com.vijay.v_canteen.util.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Admin_Login extends AppCompatActivity {
    CircleImageView acc_change;
    TextInputEditText adminid,adminpass;
    Button loginbtn;
    String a_loginid,a_password;
    TextInputLayout adminidtly,adminpasstly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__login);

        SharedPreference.initialize(getApplicationContext());

        acc_change=findViewById(R.id.adminaccountchange_img);

        adminid=findViewById(R.id.aidtxt);
        adminpass=findViewById(R.id.apasswordtxt);

        adminidtly=findViewById(R.id.aidtxtly);
        adminpasstly=findViewById(R.id.apasswordtxtly);

        loginbtn=findViewById(R.id.aloginbutton);

        acc_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu pop=new PopupMenu(getApplicationContext(),acc_change);
                pop.getMenuInflater().inflate(R.menu.a_account_change,pop.getMenu());
                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.u_acc :
                                Intent u=new Intent(Admin_Login.this, MainActivity.class);
                                startActivity(u);
                                finish();
                                return true;
                            case R.id.c_acc :
                                Intent c=new Intent(Admin_Login.this, Canteen_Login.class);
                                startActivity(c);
                                finish();
                                return true;
                            default:
                                break;
                        }
                        return false;
                    }
                });
                pop.show();
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!adminidcheck() | !adminpasscheck()){
                    return;
                }
                else{
                    a_loginid=adminid.getText().toString().trim();
                    a_password=adminpass.getText().toString().trim();
                    adminlogin(a_loginid,a_password);
                }

            }
        });
    }

    private void adminlogin(final String a_loginid, final String a_password) {
        StringRequest request=new StringRequest(Request.Method.POST, getString(R.string.url) + "admin_login.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jobj=new JSONObject(response);
                    if(jobj.getString("status").equals("0")){
                        JSONObject ob=jobj.getJSONObject("data");
                        savesharedpreference(ob);
                        Intent intent=new Intent(Admin_Login.this,Admin_home.class);
                        startActivity(intent);
                        finish();

                    }
                    else if(jobj.getString("status").equals("1")){
                        Toast.makeText(Admin_Login.this, jobj.getString("message"), Toast.LENGTH_SHORT).show();
                        clearpassword();

                    }
                    else if (jobj.getString("status").equals("2")){
                        Toast.makeText(Admin_Login.this, jobj.getString("message"), Toast.LENGTH_SHORT).show();
                        clear();

                    }
                    else {
                        Toast.makeText(Admin_Login.this, jobj.getString("message"), Toast.LENGTH_SHORT).show();

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
                params.put("loginid",a_loginid);
                params.put("password",a_password);
                return params;
            }
        };

        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

    private void savesharedpreference(JSONObject ob) {
        try {
            SharedPreference.save("aid",ob.getString("a_id"));
            SharedPreference.save("aserial",ob.getString("a_serial"));
            SharedPreference.save("aname",ob.getString("a_name"));
            SharedPreference.save("aemail",ob.getString("a_email"));
            SharedPreference.save("aphone",ob.getString("a_phone"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void clear() {
        adminpass.setText("");
        adminid.setText("");
    }

    private void clearpassword() {
        adminpass.setText("");
    }

    private boolean adminpasscheck() {
        if (adminpass.getText().toString().trim().equals("")){
            adminpasstly.setError("Enter name");
            return false;
        }
        else{
            adminpasstly.setError(null);
            return true;
        }

    }

    private boolean adminidcheck() {
        if (adminid.getText().toString().trim().equals("")){
            adminidtly.setError("Enter password");
            return false;
        }
        else{
            adminidtly.setError(null);
            return true;
        }
    }
}
