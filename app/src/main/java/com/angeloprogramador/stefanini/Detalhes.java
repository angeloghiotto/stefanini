package com.angeloprogramador.stefanini;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

public class Detalhes extends AppCompatActivity {

    private DatabaseHelper helper;
    private Cursor existe;
    private Menu mMenu;
    private ListaCidades cidade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);

        //Para voltar a tela principal
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Recuperando informações passadas pela activity anterior
        Bundle b = getIntent().getExtras();
        String nome = b.getString("nome");
        Double lat = b.getDouble("lat");
        Double lon = b.getDouble("lon");

        //Criando um objeto ListaCidades
        cidade = new ListaCidades(nome,lon, lat);
        //Helper para comunicação com o SQLite
        helper = new DatabaseHelper(this);

        //Recuperando informações sobre a cidade escolhida
        WheaterAPIHelper infosTempo = new WheaterAPIHelper();
        infosTempo.execute(lat,lon);




    }

    //Criando o menu da view
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detalhes, menu);
        mMenu = menu;
        //Verificando a exitência dessa cidade como favorita
        existe = helper.getItemID(cidade.getNome());
        //Caso seja uma favoritada, preenche a estrela
        if(existe.getCount() > 0){
            mMenu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_star_black_24dp));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.addCidade:
                //Verificando a exitência dessa cidade como favorita
                existe = helper.getItemID(cidade.getNome());
                //Preenche a estrela de acordo com o resultado da query
                if(existe.getCount() == 0){
                    mMenu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_star_black_24dp));
                    helper.addData(cidade);
                }else{
                    mMenu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_star_border_black_24dp));
                    helper.deleteName(cidade);
                }
                return true;
        }
        return (super.onOptionsItemSelected(item));
    }


    public class WheaterAPIHelper extends AsyncTask<Double, String, JSONObject> {

        @Override
        protected JSONObject doInBackground(Double... doubles) {
            try {
                return getData(doubles[0],doubles[1]);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);

            //Referênciando os campos da View activity_detalhes
            TextView nomeCidade = findViewById(R.id.nomeCidade);
            TextView temperatura = findViewById(R.id.temperatura);
            TextView descricao = findViewById(R.id.descricao);
            TextView valorTempMax = findViewById(R.id.valorTempMax);
            TextView valorTempMin = findViewById(R.id.valorTempMin);
            ImageView icone = findViewById(R.id.icone);

            //Inputando os dados obtidos na API na view
            try {
                nomeCidade.setText(json.getString("name"));
                temperatura.setText(json.getJSONObject("main").getInt("temp") + " C°");
                descricao.setText(json.getJSONArray("weather").getJSONObject(0).getString("description"));
                valorTempMax.setText(json.getJSONObject("main").getInt("temp_max") + " C°");
                valorTempMin.setText(json.getJSONObject("main").getInt("temp_min") + " C°");

                //Utilizando a biblioteca picasso para facilitar a renderezação de imagens exeternas
                Picasso.get().load("http://openweathermap.org/img/w/"+json.getJSONArray("weather").getJSONObject(0).getString("icon")+".png").into(icone);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Iniciando o requerimento de JSON
        private JSONObject getData(Double lat, Double lon) throws IOException, JSONException {
            try {
                JSONObject json = readJsonFromUrl("http://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon+"&units=metric&lang=pt&APPID=593952bc20750862e0aabec29843a386");
                return json;
            } catch (JSONException e) {

                e.printStackTrace();
            }
            return null;
        }

        //Recebendo o JSON do request
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

        //Leitor de buffer
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
