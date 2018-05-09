package com.angeloprogramador.stefanini;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class PesquisaCidades extends AppCompatActivity {

    private ArrayList<ListaCidades> listaCidades = new ArrayList<ListaCidades>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisa_cidades);

        //Para voltar a tela principal
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //INICIO - Pegando json estático que está na pasta RAW e transformando em Gson
        String cityJson = inputStreamToString(getResources().openRawResource(R.raw.city_list));
        JsonModel listaCidadesJson = new Gson().fromJson(cityJson, JsonModel.class);



        //Adicionando as cidades no objeto ListaCidades
        int tamanhoLista = listaCidadesJson.getData().size();
        for (int i = 0; i < tamanhoLista; i++) {
            listaCidades.add(new ListaCidades(
                    listaCidadesJson.getData().get(i).name,
                    listaCidadesJson.getData().get(i).coord.lon,
                    listaCidadesJson.getData().get(i).coord.lat
            ));
        }

        // CRIANDO O ADAPTER
        ListaCidadesPesquisaAdapter adapter = new ListaCidadesPesquisaAdapter(this, listaCidades);

        // ADICIONANDO O ADAPTER NO LIST VIEW
        ListView listViewCidades = findViewById(R.id.listaCidades);
        listViewCidades.setAdapter(adapter);

        listViewCidades.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //Criando o intent para mostrar detalhes da cidade corretamente
                ListaCidades cidade = listaCidades.get(position);
                Intent intent = new Intent(getApplicationContext(), Detalhes.class);
                Bundle b = new Bundle();
                b.putString("nome", cidade.getNome());
                b.putDouble("lat", cidade.getLat());
                b.putDouble("lon", cidade.getLon());
                intent.putExtras(b);
                startActivity(intent);
            }
        });

    }
    // Lendo Json
    public String inputStreamToString(InputStream inputStream) {
        try {
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, bytes.length);
            String json = new String(bytes);
            return json;
        } catch (IOException e) {
            return null;
        }
    }
}
