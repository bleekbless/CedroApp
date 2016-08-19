package com.example.paulo.provacedro;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Paulo on 15/08/2016.
 */

public class DBManager {

    private DBHelper dbHelper = null;

    public DBManager(Context context){
        if (dbHelper == null){
            dbHelper = new DBHelper(context);
        }
    }
    //metodo para inserir novo usuario
    public void addUser(String id, String nome, String email, String imagem){
        String sql = "INSERT INTO users (_id, nome, email, imagem) VALUES ('"+id+"','"+nome+"','"+email+"','"+imagem+"');";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(sql);
    }

    //metodo para alterar o email do usuario
    public void updateEmail(String id, String email){
        String sql = "UPDATE users SET email = '" + email + "' WHERE _id = '"+id+"'";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(sql);
    }

    public void updateImagem(String id, String imagem){
        String sql = "UPDATE users SET imagem = '" + imagem + "' WHERE _id = '"+id+"'";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(sql);
    }

    //metodo para buscar os dados do usuario
    public ArrayList<String> getUser (String id){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String sql = "SELECT * FROM users WHERE _id = "+id;
        Cursor cursor = db.rawQuery(sql, null);

        ArrayList<String> user = null;

        if(cursor != null && cursor.moveToFirst()){
            user = new ArrayList<String>();
            do {
                user.add(cursor.getString(0));
                user.add(cursor.getString(1));
                user.add(cursor.getString(2));
            }while (cursor.moveToNext());
        }
        return user;
    }


    public void inserePais(Paises pais, String iduser, String dataVisitada){

        String sql = "INSERT OR REPLACE INTO pais (_id, shortname, longname, callingCode) VALUES ('"+pais.getId()+"','"+pais.getShortname()+"','"+pais.getLongname()+"','"+pais.getCallingcode()+"'); ";
        String sql2 = "INSERT OR REPLACE INTO user_pais (id_user, id_pais, dataVisitada) VALUES ('"+iduser+"','"+pais.getId()+"','"+ dataVisitada +"');";

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.execSQL(sql);
        db.execSQL(sql2);

    }

    public void updatePais(Paises pais, String iduser, String dataVisitada){

        String sql = "INSERT OR REPLACE INTO pais (_id, shortname, longname, callingCode) VALUES ('"+pais.getId()+"','"+pais.getShortname()+"','"+pais.getLongname()+"','"+pais.getCallingcode()+"'); ";

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.execSQL(sql);

    }

    public Cursor buscaPaisesVisitados(String id){

        String sql = "SELECT p._id, u._id as uid, up.id_pais, p.shortname, p.longname, p.callingCode FROM user_pais as up " +
              "JOIN pais AS p ON up.id_pais = p._id " +
                "JOIN users AS u ON up.id_user = u._id " +
                "WHERE u._id = '"+ id + "';";

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(sql,null);


        return  cursor;

    }

    public void excluiVisitado(String id, String idPais){

        String sql = "DELETE FROM user_pais WHERE id_pais = '"+idPais+"' AND id_user = '" + id +"';";

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.execSQL(sql);
    }

    public boolean checaVisitado(String id, String idPais){

        String sql = "SELECT * FROM user_pais WHERE id_pais = '"+ idPais +"' AND id_user = '" + id +"';";

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(sql,null);

        if (cursor.moveToFirst()){
            return true;
        }else
            return false;
    }

    public String buscaData(String id, String idPais){
        String sql = "SELECT _id, dataVisitada FROM user_pais WHERE id_pais = '"+ idPais +"' AND id_user = '" + id +"';";

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(sql,null);

        if(cursor.moveToFirst()){

            return cursor.getString(cursor.getColumnIndexOrThrow("dataVisitada"));

        }else {
            return null;
        }
    }
}
