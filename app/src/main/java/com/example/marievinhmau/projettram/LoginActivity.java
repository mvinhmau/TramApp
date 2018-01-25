package com.example.marievinhmau.projettram;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.List;
import java.util.Random;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends  AppCompatActivity {

    private Button newUserBtn;
    private Button loginBtn;
    private EditText loginEditText;
    private EditText mdpEditText;
    private TextView text;
    private UserDAO userDAO;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent intent = getIntent();

        loginEditText= (EditText) findViewById(R.id.PhoneNb);
        mdpEditText= (EditText) findViewById(R.id.Mdp);
        text= (TextView) findViewById(R.id.ErreurLogin);
        userDAO= new UserDAO(this);

        this.addListener();

    }

    public void addListener()
    {
      newUserBtn = (Button) findViewById(R.id.NewUserBtn);
      newUserBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent =new Intent (LoginActivity.this, RegistrationActivity.class);
              startActivity(intent);
          }
      });

      loginBtn=(Button) findViewById(R.id.LoginBtn);
      loginBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              String login = loginEditText.getText().toString().trim();
              String mdp = mdpEditText.getText().toString().trim();
              text.setText(login + " - "+ mdp);
              userDAO.open();
              User user= userDAO.getUserLogin(login, mdp);
              if (user ==null )
              {
                  text.setText("numéro de téléphone et/ou mot de passe incorrect(s)");
              }
              else
              {
                  //text.setText("authentification OK");
                  session(login, mdp, user.getLastName(), user.getFirstName(), user.getId());
                  Intent intent =new Intent (LoginActivity.this, MainActivity.class);
                  startActivity(intent);
              }
              userDAO.close();



          }
      });
    }
    public void session(String login, String mdp, String lastName, String firstName, int id)
    {
        SharedPreferences pref;
        // Editor for Shared preferences
        SharedPreferences.Editor editor;
        //Toast.makeText(this,lastName+" "+firstName, Toast.LENGTH_LONG).show();
        pref = this.getSharedPreferences("SessionUser", 0);
        editor = pref.edit();
        editor.putString("login", login);
        editor.putString("mdp", mdp);
        editor.putString("lastName", lastName);
        editor.putString("firstName", firstName);
        editor.putInt("id", id);
        editor.commit();
    }


}





