package com.example.marievinhmau.projettram;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

        //Database Version
        public static final int DATABASE_VERSION=9;

        //Database Name
        private static final String DATABASE_NAME = "users.db";

        //Table Users

        public static final String TABLE_USERS = "users";

        public static final String USERS_ID = "id";
        public static final int USERS_ID_ID = 0;

        public static final String USERS_LASTNAME = "lastname";
        public static final int USERS_LASTNAME_ID = 1;

        public static final String USERS_FIRSTNAME = "firstname";
        public static final int USERS_FIRSTNAME_ID = 2;

        public static final String USERS_PHONE = "phone";
        public static final int USERS_PHONE_ID = 3;

        public static final String USERS_MDP="mdp";
        public static final int USERS_MDP_ID = 4;

        //Table Tickets
        public static final String TABLE_TICKETS="tickets";

        public static final String TICKETS_ID="id";
        public static final int TICKETS_ID_ID=0;

        public static final String TICKETS_USER ="phone_user";
        public static final int TICKETS_USER_ID =1;

        public static final String TICKETS_NB="nb";
        public static final int TICKETS_NB_ID=2;

        // Creation table Users
        private static final String DATABASE_CREATE = "CREATE TABLE  "+ TABLE_USERS + "("
                + USERS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + USERS_LASTNAME + " text not null, "
                + USERS_FIRSTNAME +" text not null, "
                + USERS_PHONE +" text not null, "
                + USERS_MDP + " text not null );";

        // Creation table Tickets
        private static final String DATABASE_CREATE_TICKETS = "CREATE TABLE  "+ TABLE_TICKETS + "("
            + TICKETS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TICKETS_USER + " String not null, "
            + TICKETS_NB +" Integer not null);";

        //Constructeur MySqLiteHelper
        public MySQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
            super(context, name, factory, DATABASE_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE_TICKETS);
            Log.i("DBAdapter","Created table: "+DATABASE_CREATE);
            Log.i("DBAdapter","Created table: "+DATABASE_CREATE_TICKETS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(MySQLiteHelper.class.getName(),
                    "Upgrading database from version " + oldVersion + " to "
                            + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE " + TABLE_USERS+";");
            db.execSQL("DROP TABLE " + TABLE_TICKETS+";");
            onCreate(db);

        }
}

