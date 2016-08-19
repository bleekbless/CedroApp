package com.example.paulo.provacedro;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.paulo.provacedro.DownloadCompleto.download_complete;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class PaisesFragment extends Fragment implements download_complete{

    public ListView list;
    public ArrayList<Paises> countries = new ArrayList<Paises>();
    public ListAdapter adapter;


    public PaisesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;

        view = (View) inflater.inflate(R.layout.fragment_paises, container, false);

        list = (ListView) view.findViewById(R.id.lista);
        adapter = new ListAdapter(this);
        list.setAdapter(adapter);
        view.bringToFront();


        DownloadCompleto downloadCompleto = new DownloadCompleto((download_complete) this);
        downloadCompleto.download_data_from_link("http://sslapidev.mypush.com.br/world/countries/active");

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    public void get_data(String data)
    {

        try {
            JSONArray data_array=new JSONArray(data);

            for (int i = 0 ; i < data_array.length() ; i++)
            {
                JSONObject obj = new JSONObject(data_array.get(i).toString());

                Paises add = new Paises();
                add.id = obj.getString("id");
                add.iso = obj.getString("iso");
                add.longname = obj.getString("longname");
                add.culture = obj.getString("culture");
                add.status = obj.getString("status");
                add.callingcode = obj.getString("callingCode");
                add.shortname = obj.getString("shortname");

                countries.add(add);

            }

            adapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //Devido a um problema com o notifyOnDataChange criei esse metodo para atualizar o fragment
    public void reload(){

        FragmentTransaction fragmentTransaction = ((MainActivity)getActivity()).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_container, new PaisesFragment());
        fragmentTransaction.commit();
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
