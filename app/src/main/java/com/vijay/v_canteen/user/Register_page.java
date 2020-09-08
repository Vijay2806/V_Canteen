package com.vijay.v_canteen.user;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.vijay.v_canteen.R;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Register_page extends AppCompatActivity {
    TextInputLayout first_name_tly,last_name_tly,email_tly,mobile_no_tly,icard_number_tly,password_tly,confirm_password_tly,dob_tly;
    EditText first_name,last_name,e_mail,mobile_no,icasrd_no,password,confirm_password,dob_txt;
    Button signup;
    RadioButton male,female;
    CheckBox terms_check_box;
    TextView termsa_and_conditions,login_redirect;
    String firstname,lastname,email,phone,userpassword,idnum,gender,dob,currentdate;
    DatePickerDialog.OnDateSetListener mDatesetlistner;
    IntentIntegrator qrScan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        first_name_tly=findViewById(R.id.fnametly);
        last_name_tly=findViewById(R.id.lnametly);
        dob_tly=findViewById(R.id.dobtxtly);
        email_tly=findViewById(R.id.emailtxtly);
        mobile_no_tly=findViewById(R.id.mobilenotxtly);
        icard_number_tly=findViewById(R.id.idnotxtly);
        password_tly=findViewById(R.id.passwordnotxtly);
        confirm_password_tly=findViewById(R.id.confirmpasswordnotxtly);

        first_name=findViewById(R.id.firstnametxt);
        last_name=findViewById(R.id.lastnametxt);
        dob_txt=findViewById(R.id.dobtxt);
        e_mail=findViewById(R.id.emailtxt);
        mobile_no=findViewById(R.id.mobilenotxt);
        icasrd_no=findViewById(R.id.idnotxt);
        password=findViewById(R.id.passwordtxt);
        confirm_password=findViewById(R.id.confirmpasswordtxt);

        signup=findViewById(R.id.registerbutton);

        male=findViewById(R.id.malerbtn);
        female=findViewById(R.id.femalerbtn);

        terms_check_box = findViewById(R.id.termscheckbox);

        termsa_and_conditions=findViewById(R.id.terms_condition_text);
        login_redirect=findViewById(R.id.register_login_txt);

        dob_txt.setFocusable(false);
        icasrd_no.setFocusable(false);

        qrScan = new IntentIntegrator(Register_page.this);

        icasrd_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(Register_page.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                integrator.setPrompt("Scan a barcode");
                //integrator.setCameraId(0);  // Use a specific camera of the device
                integrator.setBeepEnabled(true);
                integrator.setOrientationLocked(false);
                integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();
            }
        });

        dob_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal=Calendar.getInstance();
                Integer year=cal.get(Calendar.YEAR);
                Integer month=cal.get(Calendar.MONTH);
                Integer day=cal.get(Calendar.DAY_OF_MONTH);
                currentdate=String.valueOf(day)+" / "+String.valueOf(month)+" / "+String.valueOf(year);

                DatePickerDialog dialog=new DatePickerDialog(
                        Register_page.this,
                        android.R.style.Theme_Holo_Light_Dialog,
                        mDatesetlistner,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDatesetlistner=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                Log.i("vjnarayanan",year+"/"+month+"/"+dayOfMonth);
                dob_txt.setText(dayOfMonth+" / "+month+" / "+year);
            }
        };

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validatefirstname() | !validatelastname() | !validateemail() | !validatemobile() | !validatepassword() | !validateconfirmpassword() | !validateicardno() | !validategender() | !validatedob(currentdate)){
                    return;
                }
                else{
                    if (!validatetemrs()){
                        return;
                    }
                    else {
                        firstname=first_name.getText().toString().trim();
                        lastname=last_name.getText().toString().trim();
                        email=e_mail.getText().toString().trim();
                        phone=mobile_no.getText().toString().trim();
                        userpassword=password.getText().toString().trim();
                        idnum=icasrd_no.getText().toString().trim();
                        dob=dob_txt.getText().toString().trim();
                        if(male.isChecked()){
                            gender="male";
                        }
                        else{
                            gender="female";
                        }
                        registeruser(firstname,lastname,email,phone,userpassword,idnum,gender,dob);
                    }
                }
            }
        });

        login_redirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Register_page.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private boolean validatedob(String currentdate) {
        if (dob_txt.getText().toString().trim().equals("")){
            dob_tly.setError("Select DOB");
            return false;
        }
        else if (dob_txt.getText().toString().trim().equals(currentdate)){
            dob_tly.setError("Invalid DOB");
            return false;
        }
        else {
            dob_tly.setError(null);
            return true;
        }
    }

    private void registeruser(final String firstname, final String lastname, final String email, final String phone, final String userpassword, final String idnum, final String gender, final String dob) {
        StringRequest request=new StringRequest(Request.Method.POST, getText(R.string.url) + "vcanteenregister.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jobj=new JSONObject(response);
                    Log.i("vjnarayanan",response);
                    if (jobj.getString("status").equals("0")){
                        Toast.makeText(Register_page.this, jobj.getString("message"), Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(Register_page.this,MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                    else if (jobj.getString("status").equals("1")){
                        Toast.makeText(Register_page.this, jobj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(Register_page.this, jobj.getString("message"), Toast.LENGTH_SHORT).show();
                        clear();
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
                params.put("firstname",firstname);
                params.put("lastname",lastname);
                params.put("email",email);
                params.put("phone",phone);
                params.put("password",userpassword);
                params.put("gender",gender);
                params.put("userid",idnum);
                params.put("dob",dob);
                return params;
            }
        };
        RequestQueue que= Volley.newRequestQueue(getApplicationContext());
        que.add(request);
    }

    private void clear() {
        e_mail.setText("");
        mobile_no.setText("");
        first_name.setText("");
        last_name.setText("");
        icasrd_no.setText("");
        password.setText("");
        confirm_password.setText("");
        dob_txt.setText("");
    }

    private boolean validategender() {
        if(!male.isChecked() && !female.isChecked()){
            Toast.makeText(this, "Select gender", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            return true;
        }
    }

    private boolean validatetemrs() {
        if(!terms_check_box.isChecked()){
            Toast.makeText(this, "Accept terms and conditions...!!!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else {
            return true;
        }
    }

    private boolean validateicardno() {
        if(icasrd_no.getText().toString().trim().equals("")){
            icard_number_tly.setError("Enter ID number");
            return false;
        }
        else if(icasrd_no.getText().toString().trim().length()!=10){
            icard_number_tly.setError("Invalid ID number");
            return false;
        }
        else{
            icard_number_tly.setError(null);
            return true;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {

            if (result.getContents() == null) {
                Toast.makeText(Register_page.this, "Result Not Found", Toast.LENGTH_LONG).show();
                Log.i("vjnarayanan","Result Not Found");
            } else {
                try {
                    JSONObject obj = new JSONObject(result.getContents());
                    Log.i("vjnarayanan", String.valueOf(obj));


                } catch (JSONException e) {
                    e.printStackTrace();
                    icasrd_no.setText(result.getContents());
                    Log.i("vjnarayanan", result.getContents());
                    //Toast.makeText(getActivity(), result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private boolean validateconfirmpassword() {
        if(confirm_password.getText().toString().trim().equals("")){
            confirm_password_tly.setError("Enter confirm password");
            return false;
        }
        else if(!confirm_password.getText().toString().trim().equals(password.getText().toString().trim())){
            confirm_password_tly.setError("Password does not match");
            return false;
        }
        else{
            confirm_password_tly.setError(null);
            return true;

        }
    }

    private boolean validatepassword() {
        if(password.getText().toString().trim().equals("")){
            password_tly.setError("Enter password");
            return false;
        }
        else if (password.getText().toString().trim().length()<8){
            password_tly.setError("8 or more characters required");
            return false;
        }
        else if(!confirm_password.getText().toString().trim().equals(password.getText().toString().trim())){
            password_tly.setError("Password does not match");
            return false;
        }
        else{
            password_tly.setError(null);
            return true;

        }
    }

    private boolean validatemobile() {
        if(mobile_no.getText().toString().trim().equals("")){
            mobile_no_tly.setError("Enter mobile");
            return false;
        }
        else{
            mobile_no_tly.setError(null);
            return true;

        }
    }

    private boolean validateemail() {
        if(e_mail.getText().toString().trim().equals("")){
            email_tly.setError("Enter E-mail");
            return false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(e_mail.getText().toString().trim()).matches()){
            email_tly.setError("Enter a valid E-mail");
            return false;
        }
        else{
            email_tly.setError(null);
            return true;

        }
    }

    private boolean validatelastname() {
        if(last_name.getText().toString().trim().equals("")){
            last_name_tly.setError("Enter last name");
            return false;
        }
        else{
            last_name_tly.setError(null);
            return true;

        }
    }

    private boolean validatefirstname() {
        if(first_name.getText().toString().trim().equals("")){
            first_name_tly.setError("Enter first name");
            return false;
        }
        else{
            first_name_tly.setError(null);
            return true;

        }
    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(Register_page.this,MainActivity.class);
        startActivity(i);
        finish();
    }

}
