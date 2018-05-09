package com.angeloprogramador.stefanini;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListaCidadesPesquisaAdapter extends ArrayAdapter<ListaCidades> {
    public ListaCidadesPesquisaAdapter(Context context, ArrayList<ListaCidades> cidades) {
        super(context, 0, cidades);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Posição da View
        ListaCidades listaCidades = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_view_layout_cidades, parent, false);
        }
        // Encotrando e populando as partes da view
        TextView tvName = (TextView) convertView.findViewById(R.id.nomeCidade);
        tvName.setText(listaCidades.getNome());

        //Retorna a view
        return convertView;
    }

}
