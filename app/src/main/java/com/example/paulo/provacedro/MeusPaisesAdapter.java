package com.example.paulo.provacedro;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paulo on 15/08/2016.
 */

public class MeusPaisesAdapter extends CursorAdapter {

    FloatingActionButton deletar;
    MeusPaisesFragment main;
    private List<String> counter = new ArrayList<String>();

    public MeusPaisesAdapter(Context context, Cursor cursor, int flags, MeusPaisesFragment main) {
        super(context, cursor, 0);
        this.main = main;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        view = super.getView(position, convertView, parent);

        return view;
    }

    //Metodo chamado para dar um inflate na nova view e retornar ela
    //nenhum dado e setado nesse momento
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.lista_item, parent, false);
    }

    //Esse é o metodo responsavel por setar os dados a view
    //como por exemplo detar o valor do TextView
    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {


        //Criação dos Elementos da View
        TextView callingCode = (TextView) view.findViewById(R.id.iso);
        TextView shortname = (TextView) view.findViewById(R.id.shortname);
        ImageView flag = (ImageView) view.findViewById(R.id.imageView);
        ImageView deletarUm = (ImageView) view.findViewById(R.id.deletarUm);
        final CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);

        final View viewCheck = view;

        LayoutInflater inflater = (LayoutInflater) main.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View x = main.getView();

        deletar = (FloatingActionButton) x.findViewById(R.id.fab_delete);

        //Aparace com o callingCode
        callingCode.setVisibility(View.VISIBLE);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    counter.add(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
                }else{
                    counter.remove(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
                }
            }
        });

        deletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < counter.size() ; i++)
                {
                    try {
                        DBManager dbManager = new DBManager(context);
                        dbManager.excluiVisitado(cursor.getString(cursor.getColumnIndexOrThrow("uid")),counter.get(i));

                        Toast.makeText(context,"País não visitado.", Toast.LENGTH_SHORT).show();

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                main.reload();
            }
        });

        //Deletar pelo icone de lixeira
        deletarUm.setVisibility(View.VISIBLE);
        deletarUm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Remover")
                        .setMessage("Deseja remover esse país dos visitados?")
                        .setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Performa a exclusao do banco
                                DBManager dbManager = new DBManager(context);
                                dbManager.excluiVisitado(cursor.getString(cursor.getColumnIndexOrThrow("uid")),cursor.getString(cursor.getColumnIndexOrThrow("id_pais")));
                                main.reload();
                            }
                        })
                        .setNegativeButton(R.string.nao, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Não faz nada
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });



        //Verifica quando o checkbox for cliclado

        shortname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkBox.isChecked()){
                    checkBox.setChecked(false);
                }else
                if(!checkBox.isChecked() && (checkBox.getVisibility() == View.VISIBLE)){
                    checkBox.setChecked(true);
                }else if ((!checkBox.isChecked()) && (checkBox.getVisibility() == View.INVISIBLE)){
                    editar(v,cursor);
                }

            }
        });




        /*Verifica quando o shortname for clicado por muito tempo
        * Se ele for clicado por muito tempo o checkbox é checado*
         */
        shortname.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if(checkBox.getVisibility() == View.INVISIBLE) {
                    checkBox.setVisibility(View.VISIBLE);
                }else if(checkBox.isChecked() ){
                    checkBox.setVisibility(View.INVISIBLE);
                }else if(!checkBox.isChecked() && (checkBox.getVisibility() == View.VISIBLE)){
                    checkBox.setVisibility(View.INVISIBLE);
                    checkBox.setChecked(false);
                }

                return false;
            }
        });



        //Extrai as propriedades do cursor
        Log.i("msg",cursor.toString());
       try {
           String data_callingCode = cursor.getString(cursor.getColumnIndexOrThrow("callingCode"));
           String data_shortname = cursor.getString(cursor.getColumnIndexOrThrow("shortname"));
           String data_imagem = cursor.getString(cursor.getColumnIndexOrThrow("_id"));

            //Popula os campos com as propriedades do cursor
            callingCode.setText(data_callingCode);

            shortname.setText(data_shortname);
            Picasso.with(view.getContext())
                   .load("http://sslapidev.mypush.com.br/world/countries/"+data_imagem+"/flag")
                    .into(flag);

       }catch (Exception e){
           e.printStackTrace();
       }



    }

    //metodo que adiciona os dados do pais a um novo objeto e envia para a nova activity
    public void editar(View v, Cursor cursor){
        Paises pais = new Paises();

        pais.setId(cursor.getString(cursor.getColumnIndexOrThrow("id_pais")));
        pais.setCallingcode(cursor.getString(cursor.getColumnIndexOrThrow("callingCode")));
        pais.setLongname(cursor.getString(cursor.getColumnIndexOrThrow("longname")));
        pais.setShortname(cursor.getString(cursor.getColumnIndexOrThrow("shortname")));

        pais.setFragment("meusPaises");
        Intent intent = new Intent(v.getContext(), DetalhesActivity.class);

        //adiciona o objeto pais a nova activty
        intent.putExtra("pais",pais);

        //abre a nova activity
        v.getContext().startActivity(intent);

    }


}
