package com.example.paulo.provacedro;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class Perfil extends Fragment {

    private ImageView perfil_img;
    private TextView nome;
    private TextView email;
    private Profile profile;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        perfil_img = (ImageView) view.findViewById(R.id.foto_perfil);
        email = (TextView) view.findViewById(R.id.email_usuario);
        nome = (TextView) view.findViewById(R.id.nome_perfil);


        //Busca o usuario logado e seta na variavel profile
        Bundle bundle = getArguments();

        if(bundle != null){
            profile = (Profile) bundle.getParcelable(LoginFragment.PARCEL_KEY);
        }else{
            profile = Profile.getCurrentProfile();
        }

        //Chamada do Banco
        DBManager dbManager = new DBManager(getActivity());

        //Seta o Nome do Usuario
        nome.setText(profile.getName());

        //Seta o email do usuario buscando no Banco através do ID do usuario atual
        try {
            email.setText(dbManager.getUser(profile.getId()).get(2));
        }catch (Exception e){
            email.setText("Email não encontrado.");
            e.printStackTrace();
        }

        //Imagem do Usuario
        Picasso.with(getActivity())
                .load(profile.getProfilePictureUri(400,400).toString())
                .into(perfil_img);

        return view;


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();

    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

}
