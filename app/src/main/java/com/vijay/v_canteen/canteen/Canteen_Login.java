package com.vijay.v_canteen.canteen;

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
import com.vijay.v_canteen.user.MainActivity;
import com.vijay.v_canteen.R;
import com.vijay.v_canteen.admin.Admin_Login;
import com.vijay.v_canteen.util.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Canteen_Login extends AppCompatActivity {
    CircleImageView acc_change;
    TextInputEditText canteenid,canteenpass;
    Button loginbtn;
    String c_loginid,c_password;
    TextInputLayout canteenidtly,canteenpasstly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canteen__login);

        acc_change=findViewById(R.id.canteenaccountchange_img);

        canteenid=findViewById(R.id.cidtxt);
        canteenpass=findViewById(R.id.cpasswordtxt);

        canteenidtly=findViewById(R.id.cidtxtly);
        canteenpasstly=findViewById(R.id.cpasswordtxtly);

        loginbtn=findViewById(R.id.cloginbutton);

        acc_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu pop=new PopupMenu(getApplicationContext(),acc_change);
                pop.getMenuInflater().inflate(R.menu.c_account_change,pop.getMenu());
                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.a_acc :
                                Intent a=new Intent(Canteen_Login.this, Admin_Login.class);
                                startActivity(a);
                                finish();
                                return true;
                            case R.id.u_acc :
                                Intent u=new Intent(Canteen_Login.this, MainActivity.class);
                                startActivity(u);
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
                if(!canteenidcheck() | ! canteenpasscheck()){
                    return;
                }
                else{
                    c_loginid=canteenid.getText().toString().trim();
                    c_password=canteenpass.getText().toString().trim();
                    canteenlogin(c_loginid,c_password);
                }

            }
        });
    }

    private boolean canteenpasscheck() {
        if (canteenpass.getText().toString().trim().equals("")){
            canteenpasstly.setError("Enter name");
            return false;
        }
        else{
            canteenpasstly.setError(null);
            return true;
        }

    }

    private boolean canteenidcheck() {
        if (canteenid.getText().toString().trim().equals("")){
            canteenidtly.setError("Enter password");
            return false;
        }
        else{
            canteenidtly.setError(null);
            return true;
        }
    }

    private void canteenlogin(final String c_loginid, final String c_password) {
        StringRequest request=new StringRequest(Request.Method.POST, getString(R.string.url) + "canteen_login.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jobj=new JSONObject(response);
                    if(jobj.getString("status").equals("0")) {
                        JSONObject job=jobj.getJSONObject("data");
                        savesharedpreference(job);
                        Intent intent = new Intent(Canteen_Login.this, Canteen_home.class);
                        startActivity(intent);
                        finish();
                    }

                    else if(jobj.getString("status").equals("1")){
                        Toast.makeText(Canteen_Login.this, jobj.getString("message"), Toast.LENGTH_SHORT).show();
                        clearpassword();

                    }
                    else if (jobj.getString("status").equals("2")){
                        Toast.makeText(Canteen_Login.this, jobj.getString("message"), Toast.LENGTH_SHORT).show();
                        clear();

                    }
                    else {
                        Toast.makeText(Canteen_Login.this, jobj.getString("message"), Toast.LENGTH_SHORT).show();

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
                params.put("loginid",c_loginid);
                params.put("password",c_password);
                return params;
            }
        };

        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

    private void savesharedpreference(JSONObject job) {
        try {
            SharedPreference.save("cid",job.getString("c_id"));
            SharedPreference.save("cname",job.getString("c_name"));
            SharedPreference.save("cimage",job.getString("c_image"));
            SharedPreference.save("cphone",job.getString("c_phone"));
            SharedPreference.save("cemail",job.getString("c_email"));
            SharedPreference.save("clandline",job.getString("c_landline"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void clear() {
        canteenpass.setText("");
        canteenid.setText("");
    }

    private void clearpassword() {
        canteenpass.setText("");
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
