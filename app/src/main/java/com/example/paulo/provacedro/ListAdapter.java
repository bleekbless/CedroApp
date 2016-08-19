package com.example.paulo.provacedro;

import com.facebook.Profile;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Paulo on 12/08/2016.
 */

    public class ListAdapter extends BaseAdapter {

        PaisesFragment main;
        private Context context;

        private Profile profile;

        public ListAdapter(Context context){
            this.context = context;
        }


        ListAdapter(PaisesFragment main)
        {
            this.main = main;
        }

        @Override
        public int getCount() {
            return  main.countries.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        static class ViewHolderItem {
            TextView shortname;
            ImageView flag;
            ImageView visitado;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent){
            ViewHolderItem holder = new ViewHolderItem();

            Bundle bundle = main.getArguments();

            if(bundle != null){
                profile = (Profile) bundle.getParcelable(LoginFragment.PARCEL_KEY);
            }else{
                profile = Profile.getCurrentProfile();
            }

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) main.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.lista_item, null);

                holder.shortname = (TextView) convertView.findViewById(R.id.shortname);
                holder.flag = (ImageView) convertView.findViewById(R.id.imageView);
                holder.visitado = (ImageView) convertView.findViewById(R.id.iconVisitado);

                convertView.setTag(holder);
            }
            else
            {
                holder = (ViewHolderItem) convertView.getTag();
            }



            //verifica quando a bandeira for clicada e abre a nova activity para salvar o pais
            holder.flag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    editar(v,position);

                }
            });
            final ViewHolderItem finalHolder = holder;
            //verifica quando a o nome for clicada e abre a nova activity para salvar o pais
            holder.shortname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        editar(v,position);

                }
            });


            DBManager dbManager = new DBManager(main.getContext());

            if(dbManager.checaVisitado(profile.getId(), this.main.countries.get(position).id)){
                holder.visitado.setVisibility(View.VISIBLE);
            }else{
                holder.visitado.setVisibility(View.INVISIBLE);
            }


            holder.shortname.setText(this.main.countries.get(position).shortname);
            Picasso.with(main.getContext()).load("http://sslapidev.mypush.com.br/world/countries/"+this.main.countries.get(position).id+"/flag").into(holder.flag);

            return convertView;
        }

    //metodo que adiciona os dados do pais a um novo objeto e envia para a nova activity
        public void editar(View v, int position){
            Paises pais = new Paises();

            pais.setId(main.countries.get(position).id);
            pais.setCallingcode(main.countries.get(position).callingcode);
            pais.setCulture(main.countries.get(position).culture);
            pais.setIso(main.countries.get(position).iso);
            pais.setLongname(main.countries.get(position).longname);
            pais.setShortname(main.countries.get(position).shortname);
            pais.setStatus(main.countries.get(position).status);
            pais.setFragment("paises");



            Intent intent = new Intent(v.getContext(), DetalhesActivity.class);

            //adiciona o objeto pais a nova activty
            intent.putExtra("pais",pais);

            //abre a nova activity
            v.getContext().startActivity(intent);

        }
}