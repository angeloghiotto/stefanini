package com.angeloprogramador.stefanini;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListaCidadesMainAdapter extends ArrayAdapter<ListaCidades> {
    public ListaCidadesMainAdapter(Context context, ArrayList<ListaCidades> cidades) {
        super(context, 0, cidades);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ListaCidades listaCidades = getItem(position);


        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_layout_cidades_main, parent, false);
        }
        // Encontrando e populando as pártes certas da view
        TextView nomeCidade = (TextView) convertView.findViewById(R.id.nomeCidadeMain);
        nomeCidade.setText(listaCidades.getNome());

        TextView descricaoMain = (TextView) convertView.findViewById(R.id.descricaoMain);
        descricaoMain.setText(listaCidades.getClima());

        TextView tempMain = (TextView) convertView.findViewById(R.id.tempMain);
        tempMain.setText(listaCidades.getTemperatura());

        //Retorna a view
        return convertView;
    }

}
