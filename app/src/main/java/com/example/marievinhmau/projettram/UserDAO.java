package com.example.marievinhmau.projettram;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import android.database.sqlite.SQLiteDatabase;



public class UserDAO {

    /*public static final String TABLE_NAME = "users";
    public static final String ID = "id";
    public static final int NUM_ID=0;
    public static final String LASTNAME = "lastname";
    public static final int NUM_LASTNAME=1;
    public static final String FIRSTNAME = "firstname";
    public static final int NUM_FIRSTNAME=2;
    public static final String PHONE = "phone";
    public static final int NUM_PHONE=3;
    public static final String MDP="mdp";
    public static final int NUM_MDP=4;*/

    private String[] allColumns =
            {
                MySQLiteHelper.USERS_ID,
                MySQLiteHelper.USERS_LASTNAME,
                MySQLiteHelper.USERS_FIRSTNAME,
                MySQLiteHelper.USERS_PHONE,
                MySQLiteHelper.USERS_MDP
            };

    private SQLiteDatabase bdd;
    private MySQLiteHelper maBaseSQLite;



    public UserDAO(Context context)
    {
        //On crée la BDD et sa table
        maBaseSQLite = new MySQLiteHelper(context, MySQLiteHelper.TABLE_USERS, null);
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

    public long insert (User u) {
        //Création d'un ContentValues (fonctionne comme une HashMap)
        ContentValues value = new ContentValues();
        //on lui ajoute une valeur associée à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur

        value.put(MySQLiteHelper.USERS_LASTNAME, u.getLastName());
        value.put(MySQLiteHelper.USERS_FIRSTNAME, u.getFirstName());
        value.put(MySQLiteHelper.USERS_PHONE, u.getPhone());
        value.put(MySQLiteHelper.USERS_MDP, u.getMdp());
        return bdd.insert(MySQLiteHelper.TABLE_USERS, null, value);


    }


    public int delete(User u) {
        long id = u.getId();
        System.out.println("User deleted with id: " + id);
        return bdd.delete(MySQLiteHelper.TABLE_USERS, MySQLiteHelper.USERS_ID
                    + " = " + id, null);
    }


    public int update(int id , User u) {
        ContentValues value = new ContentValues();
        value.put(MySQLiteHelper.USERS_LASTNAME, u.getLastName());
        value.put(MySQLiteHelper.USERS_FIRSTNAME, u.getFirstName());
        value.put(MySQLiteHelper.USERS_PHONE, u.getPhone());
        value.put(MySQLiteHelper.USERS_MDP, u.getMdp());
        return bdd.update(MySQLiteHelper.TABLE_USERS,
                value,
                MySQLiteHelper.USERS_ID  + " = "+id,
                null);
        //, new String[] {String.valueOf(u.getId())}

    }


    public User getUser(String phone){

        Cursor c = bdd.query(MySQLiteHelper.TABLE_USERS,
                allColumns,
                MySQLiteHelper.USERS_PHONE + " like\"" + phone+"\"",
                null, null, null, null);
        return cursorToUser(c);
    }

    public User getUserLogin(String phone, String mdp)
    {
        Cursor c = bdd.query(MySQLiteHelper.TABLE_USERS, allColumns,
                MySQLiteHelper.USERS_PHONE + " like\"" + phone+"\" and "+  MySQLiteHelper.USERS_MDP +" like\"" + mdp+"\"",
                null, null, null, null);
        return cursorToUser(c);
    }
    private User cursorToUser(Cursor c){
        //si aucun élément n'a été retourné dans la requête, on renvoie null
        if (c.getCount() == 0)
            return null;

        //Sinon on se place sur le premier élément
        c.moveToFirst();
        User user = new User();
        //on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
        user.setId(c.getInt(MySQLiteHelper.USERS_ID_ID));
        user.setLastName(c.getString(MySQLiteHelper.USERS_LASTNAME_ID));
        user.setFirstName(c.getString(MySQLiteHelper.USERS_FIRSTNAME_ID));
        user.setPhone(c.getString(MySQLiteHelper.USERS_PHONE_ID));
        user.setMdp(c.getString(MySQLiteHelper.USERS_MDP_ID));

        //On ferme le cursor
        c.close();


        return user;
    }




}
