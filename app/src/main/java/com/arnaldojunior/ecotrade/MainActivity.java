package com.arnaldojunior.ecotrade;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.arnaldojunior.ecotrade.model.Anuncio;
import com.arnaldojunior.ecotrade.model.SearchResponse;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String categoria;
    ImageButton botao;
    TextView viewCat;
    String url = "http://192.169.40.156:8080/EcoTradeServer/anuncio";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.requestProdutos();
    }

    public List<Produto> instanciarProdutos() {
        List<Produto> produtos = new ArrayList<Produto>();
        Produto p1 = new Produto("Iphone X", "Doação");
        Produto p2 = new Produto("SmartTv LG", "Venda");
        Produto p3 = new Produto("Nobreak", "Doação");
        produtos.add(p1);
        produtos.add(p2);
        produtos.add(p3);

        return produtos;
    }

    public void buscarListaPorCategoria(View view) {
        categoria = view.getContentDescription().toString();
        viewCat = (TextView) findViewById(R.id.textView2);
        viewCat.setText(categoria);
    }

    public void requestProdutos() {
        // A request for retrieving  a JSONObject.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        SearchResponse searchResponse = gson.fromJson(response.toString(), SearchResponse.class);
                        List<Anuncio> anuncios = searchResponse.getAnuncios();
                        ListView listView = (ListView) findViewById(R.id.listView);
                        AdapterProduto adapter = new AdapterProduto(anuncios, MainActivity.this);
                        listView.setAdapter(adapter);
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
}
