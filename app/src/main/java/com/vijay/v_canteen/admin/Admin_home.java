package com.vijay.v_canteen.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.vijay.v_canteen.R;
import com.vijay.v_canteen.util.SharedPreference;

public class Admin_home extends AppCompatActivity {
    LinearLayout addcanteenbtn,managecanteen,manageuser,adminprofile,getcanteenstats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        addcanteenbtn=findViewById(R.id.add_canteen_btn);
        managecanteen=findViewById(R.id.view_canteen_btn);
        manageuser=findViewById(R.id.manage_user_btn);
        adminprofile=findViewById(R.id.admin_profile_btn);
        getcanteenstats=findViewById(R.id.get_stats_btn);

        addcanteenbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a=new Intent(Admin_home.this,Admin_add_canteen.class);
                startActivity(a);
                finish();

            }
        });

        managecanteen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent b=new Intent(Admin_home.this,Admin_manage_canteen.class);
                startActivity(b);
                finish();

            }
        });

        manageuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent c=new Intent(Admin_home.this,Admin_manage_user.class);
                startActivity(c);
                finish();

            }
        });

        adminprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent d=new Intent(Admin_home.this,Admin_profile.class);
                startActivity(d);
                finish();

            }
        });

        getcanteenstats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent e=new Intent(Admin_home.this,Admin_check_canteen_stats.class);
                startActivity(e);
                finish();

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menulogout :
                new AlertDialog.Builder(Admin_home.this)
                        .setTitle("Warning")
                        .setMessage("Do you want to logout")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removesp();
                                Intent logout=new Intent(Admin_home.this,Admin_Login.class);
                                startActivity(logout);
                                finish();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
                return true;

        }
        return false;
    }

    private void removesp() {
        SharedPreference.removeKey("aid");
        SharedPreference.removeKey("aserial");
        SharedPreference.removeKey("aname");
        SharedPreference.removeKey("aphone");
        SharedPreference.removeKey("aemail");


    }
}
