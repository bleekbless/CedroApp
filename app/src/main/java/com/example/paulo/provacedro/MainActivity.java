package com.example.paulo.provacedro;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.paulo.provacedro.R;
import com.example.paulo.provacedro.DownloadCompleto.download_complete;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.FacebookCallback;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.squareup.picasso.*;



public class MainActivity extends AppCompatActivity {



    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    FragmentTransaction fragmentTransaction;
    NavigationView navigationView;

    public ListView list;
    public ArrayList<Paises> countries = new ArrayList<Paises>();
    public ListAdapter adapter;
    public TextView email_header;
    public ImageView imagem_header;
    public TextView nome;
    private Profile profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Pega a view do navigation drawer para atualizar com a imagem o nome e o email do usuario
        View view = navigationView.inflateHeaderView(R.layout.navigation_drawer_header);

        imagem_header = (ImageView) view.findViewById(R.id.img_profile);
        nome = (TextView) view.findViewById(R.id.nomeUsuario);
        email_header = (TextView) view.findViewById(R.id.email_header);

        if(savedInstanceState != null){
            profile = (Profile) savedInstanceState.getParcelable(LoginFragment.PARCEL_KEY);
        }else{
            profile = Profile.getCurrentProfile();
        }


        if(profile != null) {
            nome.setText(profile.getName());
            Picasso.with(this)
                    .load(profile.getProfilePictureUri(400, 400).toString())
                    .into(imagem_header);


            //Adiciona o usuario dentro do banco
            DBManager dbManager = new DBManager(this);



            if(dbManager.getUser(profile.getId()) == null) {

                //insere os dados do usuario do facebook
                dbManager.addUser(profile.getId().toString()
                        , profile.getName().toString()
                        , "email"
                        ,profile.getProfilePictureUri(400,400).toString()
                );
            }

            email_header.setText(dbManager.getUser(profile.getId()).get(2));


        }

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);



        fragmentTransaction = (FragmentTransaction) getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_container, new LoginFragment());
        //drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        fragmentTransaction.commit();

        getSupportActionBar().setTitle(R.string.app_name);




        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.paises_id:
                        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container, new PaisesFragment());
                        fragmentTransaction.addToBackStack("detail");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Países");
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.perfil_id:
                        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container, new Perfil());
                        fragmentTransaction.addToBackStack("profile");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Perfil");
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.meus_paises_id:
                        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container, new MeusPaisesFragment());
                        fragmentTransaction.addToBackStack("mpaises");
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Meus Países");
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.logout_button_id:
                        LoginManager.getInstance().logOut();
                        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container, new LoginFragment());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Login");
                        drawerLayout.closeDrawers();
                        break;
                }
                return false;
            }
        });

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed() {


        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack();
        if (fragmentManager.getBackStackEntryCount() > 0){
            Log.i("MainActivity", "popping backstack");
            fragmentManager.popBackStack();
        }else{
            Log.i("MainActivity", "nothing on backstack, calling super");
            super.onBackPressed();
        }

    }

}
