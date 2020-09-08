package com.vijay.v_canteen.user;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.vijay.v_canteen.R;

public class User_blocked extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_blocked);
    }

    @Override
    public void onBackPressed() {
        Intent back=new Intent(User_blocked.this,MainActivity.class);
        startActivity(back);
        finish();
    }
}
