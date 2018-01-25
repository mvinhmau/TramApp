package com.example.marievinhmau.projettram;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class UpdateActivity extends AppCompatActivity {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String login, mdp, firstname, lastname;
    private int id;
    private EditText  lastNameEditText, firstNameEditText, mdpEditText;
    private TextView phoneText;
    private Button updateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        //On recupere les données de l'utilisateur
        pref = getApplicationContext().getSharedPreferences("SessionUser", 0);
        editor = pref.edit();
        login = pref.getString("login", null);
        mdp = pref.getString("mdp", null);
        lastname=pref.getString("lastName", null);
        firstname=pref.getString("firstName", null);
        id=pref.getInt("id", 0);

        phoneText=(TextView) findViewById(R.id.Phone);
        lastNameEditText=(EditText) findViewById(R.id.LastName);
        firstNameEditText= (EditText) findViewById(R.id.FirstName);
        mdpEditText= (EditText) findViewById(R.id.Mdp);

        phoneText.setText(login);
        lastNameEditText.setText(lastname);
        firstNameEditText.setText(firstname);

        addListener();



    }

    public void addListener(){
        updateBtn= (Button) findViewById(R.id.UpdateBtn);
        updateBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                String newLastName= lastNameEditText.getText().toString().trim();
                String newFirstName = firstNameEditText.getText().toString().trim();
                String newMdp = mdpEditText.getText().toString().trim();
                if (newMdp.length()==0)
                {
                    newMdp=mdp;
                }
                User u = new User (newLastName,newFirstName, login, newMdp);
                update(u);


            }
        });
    }

    public void update (User u)
    {
        UserDAO dao = new UserDAO(this);
        dao.open();
        dao.update(id,u);
        editor.clear();
        editor.commit();
        editor.putString("login", u.getPhone());
        editor.putString("mdp", u.getMdp());
        editor.putString("lastName", u.getLastName());
        editor.putString("firstName", u.getFirstName());
        editor.putInt("id", id);
        editor.commit();
        Intent intentRefresh = getIntent();
        finish();
        startActivity(intentRefresh);

    }
}
