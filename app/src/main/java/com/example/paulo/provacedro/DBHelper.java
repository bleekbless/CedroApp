package com.example.paulo.provacedro;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Paulo on 15/08/2016.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static String DB_NAME = "cedroapp";
    private static int DB_VERSION = 1;

    private static String TABLE_USERS =
            "CREATE TABLE users("+
                    "_id TEXT PRIMARY KEY,"+
                    "nome TEXT,"+
                    "email TEXT,"+
                    "imagem TEXT"+
                    ");";
    private static String TABLE_PAISES =
            "CREATE TABLE pais("+
                    "_id TEXT PRIMARY KEY,"+
                    "shortname TEXT,"+
                    "longname TEXT,"+
                    "callingCode TEXT"+
                ");";
    private static String TABLE_USERS_PAISES =
            "CREATE TABLE user_pais( " +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    "id_user TEXT NOT NULL, "+
                    "id_pais TEXT NOT NULL, " +
                    "dataVisitada TEXT, " +
                    "FOREIGN KEY (id_user) REFERENCES users(_id), " +
                    "FOREIGN KEY (id_pais) REFERENCES pais(_id) " +
                    ");";


    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_USERS);
        db.execSQL(TABLE_PAISES);
        db.execSQL(TABLE_USERS_PAISES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versaoAntiga, int versaoAtual) {


    }
}
