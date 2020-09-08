package com.vijay.v_canteen.canteen;

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

public class Canteen_home extends AppCompatActivity {
    LinearLayout addnewitem,viewitem,manageusers,checkstats,reviewandfeedback,canteenprofile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canteen_home);
        SharedPreference.initialize(getApplicationContext());

        addnewitem=findViewById(R.id.add_new_item);
        viewitem=findViewById(R.id.view_item);
        manageusers=findViewById(R.id.canteen_manage_users);
        checkstats=findViewById(R.id.canteen_check_stats);
        reviewandfeedback=findViewById(R.id.canteen_check_review_feedback);
        canteenprofile=findViewById(R.id.canteen_profile);

        addnewitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a =new Intent(Canteen_home.this,Canteen_add_item.class);
                startActivity(a);
                finish();
            }
        });

        viewitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent b=new Intent(Canteen_home.this,Canteen_item_recyclerview.class);
                startActivity(b);
                finish();
            }
        });

        manageusers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent c=new Intent(Canteen_home.this,Canteen_manage_users.class);
                startActivity(c);
                finish();

            }
        });


        checkstats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent f=new Intent(Canteen_home.this,Canteen_check_stats.class);
                startActivity(f);
                finish();

            }
        });

        reviewandfeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent g=new Intent(Canteen_home.this,Canteen_check_reviews.class);
                startActivity(g);
                finish();

            }
        });

        canteenprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent h=new Intent(Canteen_home.this,Canteen_profile.class);
                startActivity(h);
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
            case R.id.menulogout:
                new AlertDialog.Builder(Canteen_home.this)
                        .setMessage("Do you want to logout")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removeSharespreference();
                                Intent logout=new Intent(Canteen_home.this,Canteen_Login.class);
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

    private void removeSharespreference() {
        SharedPreference.removeKey("cid");
        SharedPreference.removeKey("cname");
        SharedPreference.removeKey("cimage");
        SharedPreference.removeKey("cphone");
        SharedPreference.removeKey("cemail");
        SharedPreference.removeKey("clandline");


    }
}
