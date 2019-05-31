package com.arnaldojunior.ecotrade;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
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

    String categoria;
    ImageButton botao;
    TextView categoriaTextView;
    String url = "http://192.168.0.207:8080/EcoTradeServer/anuncio";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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