package com.example.marievinhmau.projettram;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegistrationActivity extends AppCompatActivity {

    private EditText lastnameEditText;
    private EditText firstnameEditText;
    private EditText phoneEditText;
    private EditText mdpEditText;
    private EditText mdpTestEditText;
    private Button registerBtn;
    private TextView text;
    private UserDAO userDAO;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        lastnameEditText=(EditText) findViewById(R.id.LastName);
        firstnameEditText=(EditText) findViewById(R.id.FirstName);
        phoneEditText=(EditText) findViewById(R.id.Phone);
        mdpEditText=(EditText) findViewById(R.id.MdpConfirm);
        mdpTestEditText =(EditText) findViewById(R.id.Mdp);
        text =(TextView) findViewById(R.id.Erreur);

        userDAO =new UserDAO(this);
        addListener();



    }



    public void addListener()
    {
        registerBtn = (Button) findViewById(R.id.RegitrationBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //User u= createUser();

                //Verification des données
                String mdptest = mdpTestEditText.getText().toString().trim();
                String lastName= lastnameEditText.getText().toString().trim();
                String firstName= firstnameEditText.getText().toString().trim();
                String phone = phoneEditText.getText().toString().trim();
                String mdp= mdpEditText.getText().toString().trim();
                //text.setText("test" +phone +" "+lastName);
                boolean verif =verifData(phone, lastName, firstName, mdp, mdptest);

                if (verif)
                {

                    User user=new User(lastName, firstName, phone, mdp);

                    userDAO.open();
                    userDAO.insert(user);
                    User userFromBdd = userDAO.getUser(phone);
                    //text.setText("test : "+userFromBdd.toString());
                    if (userFromBdd!=null)
                    {
                        text.setText(" Le compte a été crée ");
                        int id =userFromBdd.getId();
                        session (phone, mdp, lastName, firstName, id);
                        Intent intent =new Intent (RegistrationActivity.this, MainActivity.class);
                        startActivity(intent);
                        //text.setText("persistance ");
                    }
                    userDAO.close();


                }
            }

        });
    }

    public boolean verifData (String phone, String lastName, String firstName, String mdp, String mdpTest)
    {
        boolean dataOk=false;
        String msg="";
        if ( (phone.length() == 0) || (lastName.length()==0) || (firstName.length()==00)
            ||(mdp.length()==0 ) || (mdpTest.length()==0))
        {
            msg+="Veuillez renseigner tous les champs \n";

        }
        else {
            userDAO.open();
            //on vérifie si le num n'existe pas
            User userTrouve = userDAO.getUser(phone);
            userDAO.close();
            if (userTrouve!= null)
            {
                msg+="Numero de téléphone déjà existant \n";
            }

            else {
                if (!mdp.equals(mdpTest)) {
                    msg += "Les deux mots de passe doivent être identiques \n";
                }
                if (phone.length() != 10) {
                    msg += "Numero de téléphone invalide \n";
                }
            }

        }
        if (msg.length()==0)
        {
            dataOk=true;
        }

        text.setText(" "+msg);
        //text.setText(""+dataOk);
        return dataOk;

    }

    public void session(String login, String mdp, String lastName, String firstName, int id)
    {
        SharedPreferences pref;
        // Editor for Shared preferences
        SharedPreferences.Editor editor;
        pref = this.getSharedPreferences("SessionUser", 0);
        editor = pref.edit();
        editor.putString("login", login);
        editor.putString("mdp", mdp);
        editor.putString("lastName", lastName);
        editor.putString("firstName", firstName);
        editor.putInt("id", id);
        editor.putBoolean("connected", true);
        editor.commit();
    }



}
