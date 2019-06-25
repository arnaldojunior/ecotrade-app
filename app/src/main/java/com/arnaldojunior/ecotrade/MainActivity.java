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
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.arnaldojunior.ecotrade.model.Anuncio;
import com.arnaldojunior.ecotrade.model.SearchResponse;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String categoria;
    private ImageButton botao;
    private TextView categoriaTextView;
    private String url = "http://192.169.40.156:8080/EcoTradeServer/anuncio";
    private ListView anunciosListView;
    private SearchResponse searchResponse;
    private List<Anuncio> anuncios;
    private AdapterProduto adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //this.requestProdutos();
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
        categoria = view.getContentDescription().toString();
        categoriaTextView = (TextView) findViewById(R.id.textView2);
        categoriaTextView.setText(categoria);
    }

    public void requestProdutos() {
        // A request for retrieving a JSONObject.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        searchResponse = gson.fromJson(response.toString(), SearchResponse.class);
                        anuncios = searchResponse.getAnuncios();
                        anunciosListView = (ListView) findViewById(R.id.listView);
                        adapter = new AdapterProduto(anuncios, MainActivity.this);
                        anunciosListView.setAdapter(adapter);

                        anunciosListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                abrirAnuncio(view, position);
                            }
                        });
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("ERRO: "+ error);
                    }
                });

        // Access the RequestQueue through my singleton class.
        RequestQueueSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    /**
     * Inicializa outra activity passando um objeto Anuncio como parâmetro.
     * @param view
     * @param position
     */
    public void abrirAnuncio(View view, int position) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("Anuncio", anuncios.get(position));
        startActivity(intent);
    }

    public void acessarConta(MenuItem item) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void addAnuncio(MenuItem item) {

    }
}