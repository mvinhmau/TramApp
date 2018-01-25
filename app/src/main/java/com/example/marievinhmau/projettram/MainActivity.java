package com.example.marievinhmau.projettram;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.view.View.OnClickListener;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    //private ImageButton loginBtn ;
    private Button itinaryBtn ;
    private Button timetableBtn;
    private Button mapBtn;
    private Menu menu;
    private String login;
    private String mdp;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addListener();

        TextView text = (TextView) findViewById(R.id.Test);
        // Si utilistaeur connecté on récupères son login et mdp
        pref = getApplicationContext().getSharedPreferences("SessionUser", 0);
        editor = pref.edit();
        login = pref.getString("login", null);
        mdp = pref.getString("mdp", null);
       /* if (login!=null && mdp!=null)
        {
            text.setText(login +" - " + mdp);
            editor.clear();
            editor.commit();

        }*/

    }

   public void addListener()
    {
        //ItinaryActivity
        itinaryBtn =(Button) findViewById(R.id.itinaryBtn);
        itinaryBtn.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ItinaryActivity.class);
                startActivity(intent);
            }
        });

        //LoginActivity

       /* loginBtn= (ImageButton) findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });*/

        //MapActivity
        mapBtn= (Button) findViewById(R.id.mapBtn);
        mapBtn.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });

        //TimetableActivity
        timetableBtn =(Button) findViewById(R.id.timetableSearchBtn);
        timetableBtn= (Button) findViewById(R.id.timetableSearchBtn);
        timetableBtn.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TimetableActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (login!=null && mdp!=null)
        {
            inflater.inflate(R.menu.menu_logout, menu);
        }
        else {
            inflater.inflate(R.menu.menu_login, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.login:
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                return true;
            case R.id.logout:
                logout();
                return true;
            case R.id.shop:
                intent = new Intent(MainActivity.this, ShopActivity.class);
                startActivity(intent);
                return true;
            case R.id.update:
                intent = new Intent(MainActivity.this, UpdateActivity.class);
                startActivity(intent);
                return true;
            case R.id.billets:
                intent = new Intent(MainActivity.this, MyTicketsActivity.class);
                startActivity(intent);
                return true;


        }

        return super.onOptionsItemSelected(item);
    }

    public void logout()
    {
        //on supprime les ressouces stockées
        editor.clear();
        editor.commit();
        //on rafraichit l'activité
        Intent intentRefresh = getIntent();
        finish();
        startActivity(intentRefresh);
    }



}

