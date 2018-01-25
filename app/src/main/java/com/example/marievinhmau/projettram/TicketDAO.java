package com.example.marievinhmau.projettram;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;



public class TicketDAO {

    private String[] allColumns = { MySQLiteHelper.TICKETS_ID,
            MySQLiteHelper.TICKETS_USER,
            MySQLiteHelper.TICKETS_NB};


    private SQLiteDatabase bdd;
    private MySQLiteHelper maBaseSQLite;



    public TicketDAO(Context context)
    {
        //On crée la BDD et sa table
        maBaseSQLite = new MySQLiteHelper(context, MySQLiteHelper.TABLE_TICKETS, null);
    }


    public void open(){
        //on ouvre la BDD en écriture
        bdd = maBaseSQLite.getWritableDatabase();
    }

    public void close(){
        //on ferme l'accès à la BDD
        bdd.close();
    }

    public SQLiteDatabase getBDD(){
        return bdd;
    }

    public long insert (Ticket t) {
        ContentValues value = new ContentValues();
        value.put(MySQLiteHelper.TICKETS_USER, t.getPhone_user());
        value.put(MySQLiteHelper.TICKETS_NB, t.getNb());

        return bdd.insert(MySQLiteHelper.TABLE_TICKETS, null, value);


    }


    public int delete(Ticket t) {
        long id = t.getId();
        System.out.println("User deleted with id: " + id);
        return bdd.delete(MySQLiteHelper.TABLE_TICKETS, MySQLiteHelper.TICKETS_ID
                + " = " + id, null);
    }

    public ArrayList<String> getAllTickets (String phone){
        phone=phone.trim();
        Cursor c = bdd.query(MySQLiteHelper.TABLE_TICKETS,
                null,
                MySQLiteHelper.TICKETS_USER + " like " + phone+"",
                null, null, null, null);

        return cursorAllTickets(c);
    }

    public Ticket getTicket (String phoneUser, int nb){
        phoneUser=phoneUser.trim();

        Cursor c = bdd.query(MySQLiteHelper.TABLE_TICKETS,allColumns,
                MySQLiteHelper.TICKETS_USER +" like "+phoneUser + " and "+ MySQLiteHelper.TICKETS_NB +" like "+nb,
                null, null, null, null);
        //Log.i("aa", "getTicket: "+c.getCount() );

        return cursorToOneTicket(c);
    }



    private Ticket cursorToOneTicket(Cursor c){
        Log.i("test", " "+c.getCount());
        if (c.getCount()==0)
        {
            return null;
        }
        c.moveToFirst();
        Ticket t = new Ticket();
        t.setId(c.getInt(MySQLiteHelper.TICKETS_ID_ID));
        t.setPhone_user(c.getString(MySQLiteHelper.TICKETS_USER_ID));
        t.setNb(c.getInt(MySQLiteHelper.TICKETS_NB_ID));
        c.close();
        return t;
    }

    public ArrayList<String> cursorAllTickets(Cursor c){
        //si aucun élément n'a été retourné dans la requête, on renvoie null
        ArrayList tickets = new ArrayList<>();
        int nb;
        int nbLignes =c.getCount();
        if (nbLignes == 0) {
            tickets.add("pas de voyages");
        }
            int i=0;
        c.moveToFirst();
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            nb=c.getInt(MySQLiteHelper.TICKETS_NB_ID);
            if (nb==1)
            {
                tickets.add(""+nb+" voyage");
            }
            else
            {
                tickets.add(""+nb+" voyages");
            }
        }

        c.close();


        return tickets;
    }


}


