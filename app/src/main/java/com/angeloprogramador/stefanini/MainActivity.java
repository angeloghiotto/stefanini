package com.angeloprogramador.stefanini;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper mDatabaseHelper;
    private ArrayList<ListaCidades> listaCidadesSalvas = new ArrayList<ListaCidades>();
    private ListView listViewCidades;
    private ListaCidadesMainAdapter adapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Pegando os dados do SQLite
        mDatabaseHelper = new DatabaseHelper(this);
        Cursor data = mDatabaseHelper.getData();
        context = this;
        listaCidadesSalvas.clear();
        while (data.moveToNext()) {
            listaCidadesSalvas.add(new ListaCidades(
                    data.getString(1),
                    data.getDouble(2),
                    data.getDouble(3)
            ));
        }

        // CRIANDO O ADAPTER
        adapter = new ListaCidadesMainAdapter(this, listaCidadesSalvas);

        // ADICIONANDO O ADAPTER NO LIST VIEW
        listViewCidades = findViewById(R.id.listaCidadesSalvas);
        listViewCidades.setAdapter(adapter);
        listViewCidades.setEmptyView(findViewById(R.id.vazio));

        listViewCidades.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                ListaCidades cidade = listaCidadesSalvas.get(position);

                Intent intent = new Intent(context, Detalhes.class);
                Bundle b = new Bundle();
                b.putString("nome", cidade.getNome()); //Your id
                b.putDouble("lat", cidade.getLat()); //Your id
                b.putDouble("lon", cidade.getLon()); //Your id
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);
            }
        });

        WheaterAPIHelper infosTempo = new WheaterAPIHelper();
        //Executando chamadas para verificar os dados das cidades salvas
        infosTempo.execute(listaCidadesSalvas);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.pesquisaCidade:
                //Chamada da telah de pesquisa
                Intent intent = new Intent(this, PesquisaCidades.class);
                startActivity(intent);
                return (true);
        }
        return (super.onOptionsItemSelected(item));
    }

    //Synk Task para busca na API
    public class WheaterAPIHelper extends AsyncTask<ArrayList<ListaCidades>, String, JSONObject[]> {

        @Override
        protected JSONObject[] doInBackground(ArrayList<ListaCidades>... listaCidades) {
            try {
                return getData(listaCidades[0]);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject[] json) {
            super.onPostExecute(json);
            //Preenchendo a lista toda de uma vez, com dados retornados da API
            for(int i = 0; i < json.length; i++) {
                try {
                    listaCidadesSalvas.get(i).setClima(json[i].getJSONArray("weather").getJSONObject(0).getString("description"));
                    listaCidadesSalvas.get(i).setTemperatura(json[i].getJSONObject("main").getInt("temp") + " C°");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //Para atualizar a lista
            adapter.notifyDataSetChanged();
        }

        private JSONObject[] getData(ArrayList<ListaCidades> listaCidades) throws IOException, JSONException {
            JSONObject json[] = new JSONObject[listaCidades.size()];
            for (int i = 0; i < listaCidades.size(); i++) {
                try {
                    json[i] = readJsonFromUrl("http://api.openweathermap.org/data/2.5/weather?lat=" + listaCidades.get(i).getLat() + "&lon=" +  listaCidades.get(i).getLon() + "&units=metric&lang=pt&APPID=593952bc20750862e0aabec29843a386");
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
            return json;
        }

        public JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
            InputStream is = new URL(url).openStream();
            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                String jsonText = readAll(rd);
                JSONObject json = new JSONObject(jsonText);
                return json;
            } finally {
                is.close();
            }
        }

        private String readAll(Reader rd) throws IOException {
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            return sb.toString();
        }

    }
}
