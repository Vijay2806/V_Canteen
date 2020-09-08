package com.vijay.v_canteen.user;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vijay.v_canteen.R;
import com.vijay.v_canteen.util.SharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class User_order_page extends AppCompatActivity {
    Button order;
    TextView txt_name,txt_price,txt_quantity,txt_total;
    RadioButton cod,wallet;
    String uname,uid,iid,cid,iprice,icount,iname,total,dtime,otime,paid,fav;
    EditText time;
    Integer hour,oneminute,wallet_balance;
    TimePickerDialog.OnTimeSetListener mTimelistener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order_page);

        Intent get=getIntent();
        uname=get.getStringExtra("uname");
        uid= SharedPreference.get("userial");
        iid=get.getStringExtra("iid");
        cid=get.getStringExtra("cid");
        iprice=get.getStringExtra("iprice");
        icount=get.getStringExtra("icount");
        iname=get.getStringExtra("iname");
//        fav=get.getStringExtra("fav");

//        Log.i("vj",uid);
        Log.i("vijaynarayanan","favorite :"+fav);

        order=findViewById(R.id.uop_order);

        time=findViewById(R.id.uop_time);

        txt_name=findViewById(R.id.uop_iname);
        txt_price=findViewById(R.id.uop_price);
        txt_quantity=findViewById(R.id.uop_quantity);
        txt_total=findViewById(R.id.uop_total);

        cod=findViewById(R.id.uop_cod);
        wallet=findViewById(R.id.uop_wallet);

        txt_name.setText(iname);
        txt_price.setText(iprice);
        txt_quantity.setText(icount);
        Integer a=Integer.parseInt(iprice)*Integer.parseInt(icount);
        total=String.valueOf(a);
        txt_total.setText(total);

        time.setFocusable(false);

        Calendar cal=Calendar.getInstance();
        hour=cal.get(Calendar.HOUR_OF_DAY);
        oneminute=cal.get(Calendar.MINUTE);
        Log.i("vjnarayanan",String.valueOf(hour)+"   "+String.valueOf(oneminute));
        oneminute=oneminute+20;
        Log.i("vjnarayanan",String.valueOf(oneminute));
        if (oneminute>60){
            oneminute=oneminute-60;
            hour=hour+1;
            if (oneminute<10 && hour<10){
                time.setText("0"+String.valueOf(hour)+" : "+"0"+String.valueOf(oneminute));
            }
            else if (oneminute<10 && hour>9){
                time.setText(String.valueOf(hour)+" : "+"0"+String.valueOf(oneminute));
            }
            else if (oneminute>9 && hour<10){
                time.setText("0"+String.valueOf(hour)+" : "+String.valueOf(oneminute));
            }
            else {
                time.setText(String.valueOf(hour)+" : "+String.valueOf(oneminute));
            }
//            time.setText(String.valueOf(hour)+" : "+String.valueOf(minute));
        }
        else {
            if (oneminute<10 && hour<10){
                time.setText("0"+String.valueOf(hour)+" : "+"0"+String.valueOf(oneminute));
            }
            else if (oneminute<10 && hour>9){
                time.setText(String.valueOf(hour)+" : "+"0"+String.valueOf(oneminute));
            }
            else if (oneminute>9 && hour<10){
                time.setText("0"+String.valueOf(hour)+" : "+String.valueOf(oneminute));
            }
            else {
                time.setText(String.valueOf(hour)+" : "+String.valueOf(oneminute));
            }
//            time.setText(String.valueOf(hour)+" : "+String.valueOf(minute));
        }
//        Log.i("vjnarayanan","After :"+String.valueOf(hour)+":"+String.valueOf(minute));

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog=new TimePickerDialog(
                        User_order_page.this,android.R.style.Theme_Holo_Light_Dialog,
                        mTimelistener,hour,oneminute,true
                );
                dialog.show();
            }
        });

        mTimelistener=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (hourOfDay<hour && minute<oneminute){
                    Toast.makeText(User_order_page.this, "Delivery time is invalid", Toast.LENGTH_SHORT).show();
                }
                else if (hourOfDay==hour && minute<oneminute){
                    Toast.makeText(User_order_page.this, "Delivery time is invalid", Toast.LENGTH_SHORT).show();
                }
                else if (hourOfDay<hour && minute==oneminute){
                    Toast.makeText(User_order_page.this, "Delivery time is invalid", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (minute<10 && hourOfDay<10){
                        time.setText("0"+String.valueOf(hourOfDay)+" : "+"0"+String.valueOf(minute));
                    }
                    else if (minute<10 && hourOfDay>9){
                        time.setText(String.valueOf(hourOfDay)+" : "+"0"+String.valueOf(minute));
                    }
                    else if (minute>9 && hourOfDay<10){
                        time.setText("0"+String.valueOf(hourOfDay)+" : "+String.valueOf(minute));
                    }
                    else {
                        time.setText(String.valueOf(hourOfDay)+" : "+String.valueOf(minute));
                    }
                }
            }
        };

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validatepayment()){
                    return;
                }
                else{
                    Calendar cal=Calendar.getInstance();
                    int chour=cal.get(Calendar.HOUR_OF_DAY);
                    int cminute=cal.get(Calendar.MINUTE);
                    Log.i("vjnarayanan",String.valueOf(chour)+"   "+String.valueOf(cminute));
                    Log.i("vjnarayanan",String.valueOf(oneminute));
                    if (oneminute<10 && hour<10){
                        otime="0"+String.valueOf(hour)+" : "+"0"+String.valueOf(oneminute);
                    }
                    else if (oneminute<10 && hour>9){
                        otime=String.valueOf(hour)+" : "+"0"+String.valueOf(oneminute);
                    }
                    else if (oneminute>9 && hour<10){
                        otime="0"+String.valueOf(hour)+" : "+String.valueOf(oneminute);
                    }
                    else {
                        otime=String.valueOf(hour)+" : "+String.valueOf(oneminute);
                    }
                    dtime=time.getText().toString().trim();
                    new AlertDialog.Builder(User_order_page.this)
                            .setMessage("Confirm Order")
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    return;
                                }
                            })
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (cod.isChecked()){
                                        paid="0";
                                        Log.i("vj",uid+"  "+cid+"  "+iid+"  "+iname+"  "+iprice+"  "+icount+"  "+total+"  "+otime+"  "+dtime+"  "+uname+"  "+paid);
                                        addorder(uid,cid,iid,iname,iprice,icount,total,otime,dtime,uname,paid);
                                    }
                                    else {
                                        getwallet(uid);
                                        Log.i("vj",uid+"  "+cid+"  "+iid+"  "+iname+"  "+iprice+"  "+icount+"  "+total+"  "+otime+"  "+dtime+"  "+uname+"  "+paid);
                                    }
                                }
                            })
                            .show();
                }
            }
        });

    }

    private void getwallet(final String uid) {
        StringRequest request=new StringRequest(Request.Method.POST, getString(R.string.url)+"get_wallet.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("vj",response);
                try {
                    JSONObject job=new JSONObject(response);
                    if(job.getString("status").equals("0")){
                        Log.i("vjnarayanan",response);
                        JSONArray jarr=job.getJSONArray("data");
                        for(int i=0;i<jarr.length();i++){
                            JSONObject jo=jarr.getJSONObject(i);
                            Log.i("vjnarayanan","wallet balance :"+jo.get("u_wallet"));
                            wallet_balance=Integer.parseInt(jo.get("u_wallet").toString());
//                           Toast.makeText(User_order_page.this, wallet_balance.toString(), Toast.LENGTH_SHORT).show();
                            if (wallet_balance<Integer.parseInt(total)){
                                Toast.makeText(User_order_page.this, "Not enough wallet balance", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            else {
                                paid="1";
                                wallet_balance=wallet_balance-Integer.parseInt(total);
                                addorder(uid,cid,iid,iname,iprice,icount,total,otime,dtime,uname,paid);
                                Log.i("vj","After Deduction :"+wallet_balance.toString());
                            }
                        }
                    }
                    else{
                        Toast.makeText(User_order_page.this, job.getString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("id",uid);
                return params;
            }
        };
        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

    private void addorder(final String uid, final String cid, final String iid, final String iname, final String iprice, final String icount, final String total, final String otime, final String dtime, final String uname, final String paid) {
        StringRequest request=new StringRequest(Request.Method.POST, getText(R.string.url) + "add_order.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jobj=new JSONObject(response);
                    Log.i("vjnarayanan",response);
                    if (jobj.getString("status").equals("0")){
                        Toast.makeText(User_order_page.this, jobj.getString("message"), Toast.LENGTH_SHORT).show();
                        if (paid=="0") {
                            Intent i = new Intent(User_order_page.this, User_home.class);
                            startActivity(i);
                            finish();
                        }
                        else {
                            duductwallet(uid,wallet_balance);
                            Intent i = new Intent(User_order_page.this, User_home.class);
                            startActivity(i);
                            finish();
                        }
                    }
                    else if (jobj.getString("status").equals("1")){
                        Toast.makeText(User_order_page.this, jobj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(User_order_page.this, jobj.getString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("iid",iid);
                params.put("uid",uid);
                params.put("cid",cid);
                params.put("otime",otime);
                params.put("dtime",dtime);
                params.put("iname",iname);
                params.put("iprice",iprice);
                params.put("iquantity",icount);
                params.put("ototal",total);
                params.put("paid",paid);
                params.put("uname",uname);
                return params;
            }
        };
        RequestQueue que= Volley.newRequestQueue(getApplicationContext());
        que.add(request);
    }

    private void duductwallet(final String uid, final Integer wallet_balance) {
        StringRequest request=new StringRequest(Request.Method.POST, getString(R.string.url)+"deduct_wallet.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("vic",response);
                try {
                    JSONObject job=new JSONObject(response);
                    if(job.getString("status").equals("0")) {
                        Log.i("vjnarayanan", response);
                        JSONArray jarr = job.getJSONArray("data");
                    }
                    else{
                        Toast.makeText(User_order_page.this, job.getString("message"), Toast.LENGTH_SHORT).show();
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
                params.put("id",uid);
                params.put("balance",wallet_balance.toString());
                return params;
            }
        };
        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

    private boolean validatepayment() {
        if (!cod.isChecked() && !wallet.isChecked()){
            Toast.makeText(this, "Select a payment method", Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(User_order_page.this)
                .setMessage("Do you want to cancel order")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent back=new Intent(User_order_page.this,User_home.class);
                        startActivity(back);
                        finish();
                    }
                })
                .show();

    }
}
