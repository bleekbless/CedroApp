package com.example.paulo.provacedro;

import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DetalhesActivity extends AppCompatActivity {


    Toolbar toolbar;
    TextView tv_shortname;
    TextView tv_longname;
    TextView tv_callingcode;
    ImageView tv_imagem;
    CheckBox checkBox;
    EditText dataVisitada;

    //Edit
    EditText edit_shortname;
    EditText edit_callingCode;
    EditText edit_longname;

    Button salvar;
    Button editar;
    Profile profile;

    String shortname;
    String longname;
    String callingcode;
    String id;
    String fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);

        Bundle bundle = savedInstanceState;

        if(bundle != null){

            profile = (Profile) bundle.getParcelable(LoginFragment.PARCEL_KEY);

        }else{
            profile = Profile.getCurrentProfile();
        }

        Intent i = getIntent();

        final Paises pais = (Paises) i.getSerializableExtra("pais");

        if(pais != null){
            shortname = pais.getShortname();
            longname = pais.getLongname();
            callingcode = pais.getCallingcode();
            id = pais.getId();
            fragment = pais.getFragment();
        }

        //Mascara para a data visitada

        //associacao dos id aos elementos
        tv_imagem = (ImageView) findViewById(R.id.edit_imagem_pais);
        tv_longname = (TextView) findViewById(R.id.edit_longname_pais);
        tv_shortname = (TextView) findViewById(R.id.edit_shortname_pais);
        tv_callingcode = (TextView) findViewById(R.id.edit_callingcode_pais);
        salvar = (Button) findViewById(R.id.edit_button_salvar);
        editar = (Button) findViewById(R.id.edit_button_editar);
        checkBox = (CheckBox) findViewById(R.id.edit_visitado);

        if(fragment=="meusPaises"){
            editar.setVisibility(View.VISIBLE);
        }

        //Edit
        edit_shortname = (EditText) findViewById(R.id.edit_field_shortname);
        edit_longname = (EditText) findViewById(R.id.edit_field_longname);
        edit_callingCode = (EditText) findViewById(R.id.edit_field_callingCode);


        dataVisitada = (EditText) findViewById(R.id.edit_data_visitada);
        dataVisitada.addTextChangedListener(Mask.insert("##/##/####", dataVisitada));


        final DBManager dbManager = new DBManager(this);

        //checa se existe algum registro no banco em que o pais foi visitado pelo usuario atual
        checkBox.setChecked(dbManager.checaVisitado(profile.getId(),pais.getId()));

        if(checkBox.isChecked()){
            dataVisitada.setEnabled(true);
        }else{
            dataVisitada.setEnabled(false);
        }

        if (dbManager.checaVisitado(profile.getId(), pais.getId())){

            dataVisitada.setText(dbManager.buscaData(profile.getId(),pais.getId()));
        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    dataVisitada.setEnabled(true);
                    if(!dbManager.checaVisitado(profile.getId(),pais.getId())) {
                        salvar.setText("Salvar");
                    }
                }else if (!isChecked){
                    dataVisitada.setEnabled(false);
                    if(dbManager.checaVisitado(profile.getId(),pais.getId())) {
                        salvar.setText("Excluir");
                    }
                }
            }
        });



        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edit_longname.setVisibility(View.VISIBLE);
                edit_shortname.setVisibility(View.VISIBLE);
                edit_callingCode.setVisibility(View.VISIBLE);

                edit_longname.setText(longname);
                edit_shortname.setText(shortname);
                edit_callingCode.setText(callingcode);


            }
        });

        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DBManager dbManager = new DBManager(v.getContext());

                if (checkBox.isChecked()) {
                    if(dataVisitada.getText().toString().trim().length() > 0) {

                        if(edit_longname.getVisibility() == View.VISIBLE){
                            pais.setShortname(edit_shortname.getText().toString());
                            pais.setCallingcode(edit_callingCode.getText().toString());
                            pais.setLongname(edit_longname.getText().toString());

                            dbManager.updatePais(pais,profile.getId(),dataVisitada.getText().toString());
                        }else{
                            dbManager.inserePais(pais, profile.getId(), dataVisitada.getText().toString());
                        }

                        Toast.makeText(DetalhesActivity.this, "Salvo", Toast.LENGTH_SHORT).show();


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                DetalhesActivity.this.finish();
                            }
                        }, 1000);

                    }else{
                        Toast.makeText(DetalhesActivity.this, "Digite a data em que visitou.", Toast.LENGTH_SHORT).show();
                    }


                }else if (!checkBox.isChecked()){
                    if(dbManager.checaVisitado(profile.getId(), pais.getId())) {
                        dbManager.excluiVisitado(profile.getId(), pais.getId());
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                DetalhesActivity.this.finish();

                            }
                        }, 1000);
                    }else {
                        Toast.makeText(DetalhesActivity.this, "Confirme que visitou o país.", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        //cria a toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Detalhes");
        setSupportActionBar(toolbar);

        //cria a setinha para voltar
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        tv_callingcode.setText("Calling Code: " + callingcode);
        tv_shortname.setText(shortname);
        tv_longname.setText(longname);
        Picasso.with(this).load("http://sslapidev.mypush.com.br/world/countries/"+id+"/flag").into(tv_imagem);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //para activity anterior
        if(item.getItemId() == android.R.id.home){
            finish();//fecha a activity atual e retorna a anterior se existir
        }

        return super.onOptionsItemSelected(item);
    }
}
