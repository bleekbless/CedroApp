package com.example.paulo.provacedro;


import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class MeusPaisesFragment extends Fragment {

    Profile profile;
    ListView lista_paises;
    TextView nenhum_pais_visitado;
    Cursor cursor;


    public MeusPaisesFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view;

        view = (View) inflater.inflate(R.layout.fragment_meus_paises, container, false);

        nenhum_pais_visitado = (TextView) view.findViewById(R.id.title_visitados);

        //Set dos ids dos elementos da View
        lista_paises = (ListView) view.findViewById(R.id.minha_lista);

        //Instanciação do Manager do SQLite
        DBManager dbManager = new DBManager(view.getContext());

        //Pegar os Dados do Usuario salvo na sessão
        Bundle bundle = getArguments();

        if(bundle != null){
            profile = (Profile) bundle.getParcelable(LoginFragment.PARCEL_KEY);
        }else{
            profile = Profile.getCurrentProfile();
        }

        //Pega os paises já visitado pelo usuario
        try {
            cursor = dbManager.buscaPaisesVisitados(profile.getId());

            if(!cursor.moveToFirst()){
                nenhum_pais_visitado.setText("Nenhum país visitado");
            }

        }catch (Exception e){
            e.printStackTrace();
        }


        //Execução do adapter para popular a listView
            final MeusPaisesAdapter meusPaisesAdapter = new MeusPaisesAdapter(view.getContext(), cursor, 0, this);


            //Popula a listview
            lista_paises.setAdapter(meusPaisesAdapter);


        return view;
    }

    //Devido a um problema com o notifyOnDataChange criei esse metodo para atualizar o fragment
    public void reload(){

        FragmentTransaction fragmentTransaction = ((MainActivity)getActivity()).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_container, new MeusPaisesFragment());
        fragmentTransaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        //Mostra a action Bar
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();

    }

    @Override
    public void onStop() {
        super.onStop();
        //Mostra a action Bar
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

}
