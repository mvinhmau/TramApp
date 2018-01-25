package com.example.marievinhmau.projettram;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ShopActivity extends AppCompatActivity {

    private Spinner spinnerNbBillets;
    private Button buyBtn;
    private TicketDAO ticketdao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        creationSpinner();
        addListener();

    }

    private void creationSpinner(){
        //Liste déroulante des heures
        spinnerNbBillets = (Spinner) findViewById(R.id.nbBillets);
        ArrayList<Integer> nb = new ArrayList<>();
        for (int i=1; i<=10; i++)
        {
            nb.add(i);
        }

        ArrayAdapter adapterNbBillets = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                nb
        );
        adapterNbBillets.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNbBillets.setAdapter(adapterNbBillets);
    }

    public void addListener()
    {
        buyBtn = (Button) findViewById(R.id.buyBtn);
        buyBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                String msg = "Retrouvez votre achat dans l'onglet \"Mes Billets \"";
                buy(msg);
            }
        });
    }
    public void buy(String msg)
    {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("SessionUser", 0);
        SharedPreferences.Editor editor = pref.edit();
        String phone = pref.getString("login", null);
        int nb = Integer.parseInt(spinnerNbBillets.getSelectedItem().toString());
        Ticket t = new Ticket(phone,nb );
        //Toast.makeText(this, ""+ t.getPhone_user() +" "+ t.getNb(), Toast.LENGTH_LONG).show();
        ticketdao = new TicketDAO(this);
        ticketdao.open();
        ticketdao.insert(t);
        //Toast.makeText(this, t.getPhone_user(), Toast.LENGTH_LONG).show();


        Ticket ticketBdd = ticketdao.getTicket(t.getPhone_user(), t.getNb());
        //Toast.makeText(this, " "+ ticketBdd, Toast.LENGTH_LONG).show();

        if (ticketBdd!=null) {
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(this, "Erreur lors de l'achat", Toast.LENGTH_LONG).show();
        }
        ticketdao.close();
    }
}
