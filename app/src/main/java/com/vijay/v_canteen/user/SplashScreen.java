package com.vijay.v_canteen.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.vijay.v_canteen.R;
import com.vijay.v_canteen.admin.Admin_home;
import com.vijay.v_canteen.canteen.Canteen_home;
import com.vijay.v_canteen.util.SharedPreference;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        SharedPreference.initialize(getApplicationContext());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SharedPreference.contains("uid")){
                    Log.i("vjnarayanan",SharedPreference.get("ufname")+" "+SharedPreference.get("ulname"));
                    Intent user=new Intent(SplashScreen.this, User_home.class);
                    startActivity(user);
                    finish();

                }
                else if (SharedPreference.contains("aid")){
                    Log.i("vjnarayanan",SharedPreference.get("aid"));
                    Intent admin=new Intent(SplashScreen.this, Admin_home.class);
                    startActivity(admin);
                    finish();

                }
                else if (SharedPreference.contains("cid")){
                    Log.i("vjnarayanan",SharedPreference.get("cname"));
                    Intent canteen=new Intent(SplashScreen.this, Canteen_home.class);
                    startActivity(canteen);
                    finish();

                }
                else {
                    Log.i("vjnarayanan","Nobody's logged in");
                    Intent login=new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(login);
                    finish();

                }

            }
        },2000);


    }
}
