package com.arnaldojunior.ecotrade;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.arnaldojunior.ecotrade.databinding.ActivityMainBinding;
import com.arnaldojunior.ecotrade.model.Anuncio;
import com.arnaldojunior.ecotrade.model.SearchResponse;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String categoriaSelecionada = "geral";
    private ImageButton botao;
    private String url = "http://192.168.0.15:8080/EcoTradeServer/rest/anuncio";
    private ListView anunciosListView;
    private SearchResponse searchResponse;
    private List<Anuncio> anuncios;
    private AdapterProduto adapter;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.content.setCategoria(categoriaSelecionada);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        this.requestProdutos();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * Informa a categoria selecionada.
     * @param view
     */
    public void buscarListaPorCategoria(View view) {
        binding.content.categoriaTV.setText(view.getContentDescription().toString());
    }

    public void requestProdutos() {
        // A request for retrieving a JSONObject.
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url,
                null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        System.out.println("SUCESSO: "+ response.toString());

                        try {
                            Gson gson = new Gson();
                            Anuncio[] anunciosArray = gson.fromJson(response.toString(), Anuncio[].class);
                            anuncios = Arrays.asList(anunciosArray);
                            anunciosListView = (ListView) findViewById(R.id.listView);
                            adapter = new AdapterProduto(anuncios, MainActivity.this);
                            anunciosListView.setAdapter(adapter);

                            anunciosListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    irParaAnuncio(view, position);
                                }
                            });
                        } catch (Exception je) {
                            je.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("ERRO: "+ error);
                    }
                });

        // Access the RequestQueue through my singleton class.
        RequestQueueSingleton.getInstance(this).addToRequestQueue(jsonArrayRequest);
    }

    /**
     * Inicializa outra activity passando um objeto Anuncio como parâmetro.
     * @param view
     * @param position
     */
    public void irParaAnuncio(View view, int position) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("Anuncio", anuncios.get(position));
        startActivity(intent);
    }

    public void irParaLogin(MenuItem item) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void irParaCadastro(MenuItem item) {
        Intent intent = new Intent(this, CadastroActivity.class);
        startActivity(intent);
    }

    public void addAnuncio(MenuItem item) {

    }
}