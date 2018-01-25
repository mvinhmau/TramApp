package com.example.marievinhmau.projettram;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MyTicketsActivity extends AppCompatActivity {

    private ListView myTickets;
    private ArrayList<String> billets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tickets);

        myTickets = (ListView) findViewById(R.id.MyTickets);
        billets=getTickets();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(MyTicketsActivity.this,
                android.R.layout.simple_list_item_1, billets);
        myTickets.setAdapter(adapter);

        if (!billets.get(0).equals("pas de voyages")) {

            addListener();
        }


    }

    public void addListener()
    {
        myTickets.setOnItemClickListener(
                new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int position, long id) {
                Intent intent = new Intent(MyTicketsActivity.this, FlashCodeActivity.class);
                startActivity(intent);

            }
        });
    }
    public ArrayList<String> getTickets()
    {
        TicketDAO dao = new TicketDAO(this);
        dao.open();
        ArrayList tickets=new ArrayList<>();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("SessionUser", 0);
        SharedPreferences.Editor editor = pref.edit();
        String phone= pref.getString("login", null);
        tickets = dao.getAllTickets(phone.trim());
        dao.close();
        return tickets;
    }



}
