package com.vijay.v_canteen.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.vijay.v_canteen.util.SharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class User_selected_item extends AppCompatActivity {
    String iid,name,type,price,quantity,cid,count,uid,uname,fav_status,image;
    TextView edt_name,edt_type,edt_price,edt_quantity;
    Button add,order,plus,minus;
    CircleImageView fav,unfav;
    EditText edt_count;
    Integer count2;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_selected_item);

        imageView=findViewById(R.id.usi_image);

        uname=SharedPreference.get("ufname")+" "+SharedPreference.get("ulname");
        uid=SharedPreference.get("userial");
//        cid= SharedPreference.get("canteenid");
        Intent i=getIntent();
        iid=i.getStringExtra("id");
        name=i.getStringExtra("name");
        type=i.getStringExtra("type");
        price=i.getStringExtra("price");
        quantity=i.getStringExtra("quantity");
        cid=i.getStringExtra("cid");
        image=i.getStringExtra("image");
        cid= SharedPreference.get("canteenid");
        fav_status=i.getStringExtra("fav");
        Log.i("vjnarayanan",iid+" "+name+" "+type+" "+price+" "+quantity+" "+cid);
        Log.i("vijaynarayanan","favorite status :"+fav_status);

        edt_name=findViewById(R.id.usi_name);
        edt_price=findViewById(R.id.usi_price);
        edt_type=findViewById(R.id.usi_type);
        edt_quantity=findViewById(R.id.usi_quantity);

        edt_count=findViewById(R.id.usi_count);

        edt_price.setText(price);
        edt_quantity.setText(quantity);
        edt_name.setText(name);
        edt_type.setText(type);

        add=findViewById(R.id.usi_add);
        order=findViewById(R.id.usi_order);
        minus=findViewById(R.id.usi_minus);
        plus=findViewById(R.id.usi_plus);

        fav=findViewById(R.id.usi_fav);
        unfav=findViewById(R.id.usi_unfav);

        fav.setVisibility(View.GONE);

        Picasso.with(getApplicationContext()).load("http://192.168.0.103/API/V_canteen/photo/item/"+image).fit().into(imageView);

        edt_count.setFocusable(false);
        count2=Integer.parseInt(edt_count.getText().toString());
        Log.i("vjnarayanan",String.valueOf(count2));

        if (fav_status.equals("null")){
            fav.setVisibility(View.GONE);
            unfav.setVisibility(View.VISIBLE);
        }
        else if (fav_status.equals("0")){
            fav.setVisibility(View.GONE);
            unfav.setVisibility(View.VISIBLE);
        }
        else if (fav_status.equals("1")){
            fav.setVisibility(View.VISIBLE);
            unfav.setVisibility(View.GONE);
        }

        unfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fav_status.equals("null")){
                    addnewfavorite(uid,iid);
                    fav_status="0";
                }
                else if (fav_status.equals("0")){
                    addfavorite(uid,iid);
                }
            }
        });

        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removefavorite(uid,iid);
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a=Integer.parseInt(edt_count.getText().toString());
                if(a>1){
                    a=a-1;
                    edt_count.setText(String.valueOf(a));
                }
                else {
                    return;
                }
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a=Integer.parseInt(edt_count.getText().toString());
                if (a<6){
                    a=a+1;
                    edt_count.setText(String.valueOf(a));
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addcart();
            }
        });

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count=edt_count.getText().toString();
                Log.i("vjnarayanan","item count :"+count);
                Intent iorder=new Intent(User_selected_item.this,User_order_page.class);
                iorder.putExtra("uid",uid);
                iorder.putExtra("iid",iid);
                iorder.putExtra("cid",cid);
                iorder.putExtra("iname",name);
                iorder.putExtra("iprice",price);
                iorder.putExtra("icount",count);
                iorder.putExtra("uname",uname);
                startActivity(iorder);
                finish();
            }
        });
    }

    private void addnewfavorite(final String uid, final String iid) {
        StringRequest request=new StringRequest(Request.Method.POST, getString(R.string.url)+"add_new_favorite.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("vjnarayanan",response);
                try {
                    JSONObject job=new JSONObject(response);
                    if(job.getString("status").equals("0")){
                        Log.i("vjnarayanan",response);
                            fav.setVisibility(View.VISIBLE);
                            unfav.setVisibility(View.GONE);
                    }
                    else{
                        Toast.makeText(User_selected_item.this, job.getString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("uid",uid);
                params.put("iid",iid);
                return params;
            }
        };
        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

    private void removefavorite(final String uid, final String iid) {
        StringRequest request=new StringRequest(Request.Method.POST, getString(R.string.url)+"remove_fav.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject job=new JSONObject(response);
                    if(job.getString("status").equals("0")){
                        Log.i("wadu",response);
                            unfav.setVisibility(View.VISIBLE);
                            fav.setVisibility(View.GONE);
                    }
                    else{
                        Toast.makeText(User_selected_item.this, job.getString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("uid",uid);
                params.put("iid",iid);
                return params;
            }
        };
        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

    private void addfavorite(final String uid, final String iid) {
        StringRequest request=new StringRequest(Request.Method.POST, getString(R.string.url)+"add_fav.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject job=new JSONObject(response);
                    if(job.getString("status").equals("0")){
                        Log.i("vjnarayanan",response);
                            fav.setVisibility(View.VISIBLE);
                            unfav.setVisibility(View.GONE);
                    }
                    else{
                        Toast.makeText(User_selected_item.this, job.getString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("uid",uid);
                params.put("iid",iid);
                return params;
            }
        };
        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

    private void addcart() {
        StringRequest request=new StringRequest(Request.Method.POST, getString(R.string.url)+"add_cart.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("vjnarayanan",response);
                try {
                    JSONObject job=new JSONObject(response);
                    if(job.getString("status").equals("0")){
                        Log.i("vjnarayanan",response);
                        JSONArray jarr=job.getJSONArray("data");
                        for(int i=0;i<jarr.length();i++){
                            JSONObject jo=jarr.getJSONObject(i);
                            Log.i("vjnarayanan",jo.getInt("i_id")+jo.getString("i_name")+jo.getString("i_type")+jo.getString("i_price")+jo.getString("i_quantity")+ jo.getString("c_id"));
                        }

                    }
                    else{
                        Toast.makeText(User_selected_item.this, job.getString("message"), Toast.LENGTH_SHORT).show();
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
}
