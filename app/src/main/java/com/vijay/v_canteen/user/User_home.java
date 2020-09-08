package com.vijay.v_canteen.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;
import com.vijay.v_canteen.R;
import com.vijay.v_canteen.fragment.Favorite;
import com.vijay.v_canteen.fragment.History;
import com.vijay.v_canteen.fragment.Home;
import com.vijay.v_canteen.util.SharedPreference;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import de.hdodenhof.circleimageview.CircleImageView;


public class User_home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private AppBarConfiguration mAppBarConfiguration;
    Toolbar t;
    NavigationView navigationView;
    DrawerLayout drawer;
    TextView header_name;
    CircleImageView header_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        SharedPreference.initialize(getApplicationContext());

        t= findViewById(R.id.toolbar);
        setSupportActionBar(t);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(
                this,drawer,t,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        View header=navigationView.getHeaderView(0);
        header_name=header.findViewById(R.id.nav_header_name);
        header_name.setText(SharedPreference.get("ufname")+" "+SharedPreference.get("ulname"));
        header_image=header.findViewById(R.id.nav_header_image);

        displayselectedscreen(R.id.nav_home);
        t.setTitle("V_Canteen");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        displayselectedscreen(menuItem.getItemId());
        return true;
    }

    private void displayselectedscreen(int itemId) {
        Fragment f=null;
        switch (itemId){
            case R.id.nav_home:
                f=new Home();
                t.setTitle("V_Canteen");
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_profile:
                Intent i=new Intent(User_home.this, User_profile.class);
                startActivity(i);
                finish();
                break;
            case R.id.nav_history:
                f=new History();
                t.setTitle("History");
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_favorite:
                f=new Favorite();
                t.setTitle("Favorite");
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_logout:
                new AlertDialog.Builder(User_home.this)
                        .setMessage("Are u really not hungry ")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                spclear();
                                Intent i=new Intent(User_home.this, MainActivity.class);
                                startActivity(i);
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        })
                        .show();
                break;
        }
        if (f!=null){
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container,f);
            ft.commit();
        }
    }

    private void spclear() {
        SharedPreference.removeKey("uid");
        SharedPreference.removeKey("ufname");
        SharedPreference.removeKey("ulname");
        SharedPreference.removeKey("uemail");
        SharedPreference.removeKey("userial");
        SharedPreference.removeKey("uphone");
        SharedPreference.removeKey("ublock");
        SharedPreference.removeKey("uimage");
        SharedPreference.removeKey("udob");
        SharedPreference.removeKey("ugender");
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (SharedPreference.get("uimage")!=null){
            Picasso.with(getApplicationContext()).load("http://192.168.0.103/API/V_canteen/photo/user/"+SharedPreference.get("uimage")).fit().into(header_image);
        }
        else {
//            Picasso.with(getApplicationContext()).load("http://192.168.0.103/API/V_canteen/photo/user/"+SharedPreference.get("uimage")).fit().into(header_image);
            Log.i("vjnarayanan","No image found for header");
        }
    }
}
