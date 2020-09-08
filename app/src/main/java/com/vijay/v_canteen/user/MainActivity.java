package com.vijay.v_canteen.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
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
import com.vijay.v_canteen.R;
import com.vijay.v_canteen.admin.Admin_Login;
import com.vijay.v_canteen.canteen.Canteen_Login;
import com.vijay.v_canteen.util.SharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    TextView registerredirect;
    TextInputEditText userid,userpass;
    Button loginbtn;
    String u_loginid,u_password;
    CircleImageView acc_change;

    TextInputLayout useridtly,userpasstly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userid=findViewById(R.id.useridtxt);
        userpass=findViewById(R.id.userpasswordtxt);

        useridtly=findViewById(R.id.useridtxtly);
        userpasstly=findViewById(R.id.userpasswordtxtly);

        loginbtn=findViewById(R.id.loginbutton);

        acc_change=findViewById(R.id.useraccountchange_img);

        acc_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu pop=new PopupMenu(getApplicationContext(),acc_change);
                pop.getMenuInflater().inflate(R.menu.u_account_change,pop.getMenu());
                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.a_acc :
                                Intent a=new Intent(MainActivity.this, Admin_Login.class);
                                startActivity(a);
                                finish();
                                return true;
                            case R.id.c_acc :
                                Intent c=new Intent(MainActivity.this, Canteen_Login.class);
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
                if(!useridcheck() | !userpasscheck()){
                    return;
                }
                else{
                    u_loginid=userid.getText().toString().trim();
                    u_password=userpass.getText().toString().trim();
                    userlogin(u_loginid,u_password);
                }

            }
        });

        registerredirect=findViewById(R.id.registerpagetxt);

        registerredirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this, Register_page.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void userlogin(final String u_loginid, final String u_password) {
        StringRequest request=new StringRequest(Request.Method.POST, getString(R.string.url) + "vcanteenlogin.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jobj=new JSONObject(response);
                    if(jobj.getString("status").equals("0")){
                        Toast.makeText(MainActivity.this, jobj.getString("message"), Toast.LENGTH_SHORT).show();
                        JSONObject ob=jobj.getJSONObject("data");
                        if (ob.getString("u_block").equals("1")){
                            Intent i=new Intent(MainActivity.this,User_blocked.class);
                            startActivity(i);
                            finish();
                        }
                        else {
                            SharedPreference.save("uid", ob.getString("u_id"));
                            SharedPreference.save("ufname", ob.getString("u_firstname"));
                            SharedPreference.save("ulname", ob.getString("u_lastname"));
                            SharedPreference.save("uemail", ob.getString("u_email"));
                            SharedPreference.save("userial", ob.getString("u_serial"));
                            SharedPreference.save("uphone", ob.getString("u_phone"));
                            SharedPreference.save("ublock", ob.getString("u_block"));
                            SharedPreference.save("uimage", ob.getString("u_photo"));
                            SharedPreference.save("udob", ob.getString("u_dob"));
                            SharedPreference.save("ugender", ob.getString("u_gender"));

                            Intent intent = new Intent(MainActivity.this, User_home.class);
                            startActivity(intent);
                            finish();
                        }



                    }
                    else if(jobj.getString("status").equals("1")){
                        Toast.makeText(MainActivity.this, jobj.getString("message"), Toast.LENGTH_SHORT).show();
                        clearpassword();

                    }
                    else if (jobj.getString("status").equals("2")){
                        Toast.makeText(MainActivity.this, jobj.getString("message"), Toast.LENGTH_SHORT).show();
                        clear();

                    }
                    else {
                        Toast.makeText(MainActivity.this, jobj.getString("message"), Toast.LENGTH_SHORT).show();

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
                params.put("loginid",u_loginid);
                params.put("password",u_password);
                return params;
            }
        };

        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

    private void clear() {
        userpass.setText("");
        userid.setText("");
    }

    private void clearpassword() {
        userpass.setText("");
    }

    private boolean userpasscheck() {
        if (userpass.getText().toString().trim().equals("")){
            userpasstly.setError("Enter password");
            return false;
        }
        else{
            userpasstly.setError(null);
            return true;
        }

    }

    private boolean useridcheck() {
        if (userid.getText().toString().trim().equals("")){
            useridtly.setError("Enter id");
            return false;
        }
        else{
            useridtly.setError(null);
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}