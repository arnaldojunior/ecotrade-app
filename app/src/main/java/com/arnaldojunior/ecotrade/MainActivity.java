package com.arnaldojunior.ecotrade;

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
import com.arnaldojunior.ecotrade.databinding.ActivityMainBinding;
import com.arnaldojunior.ecotrade.model.Anuncio;
import com.arnaldojunior.ecotrade.model.SearchResponse;
import com.arnaldojunior.ecotrade.util.AdapterProduto;
import com.arnaldojunior.ecotrade.util.NavigationModule;
import com.arnaldojunior.ecotrade.util.RequestQueueSingleton;
import com.arnaldojunior.ecotrade.util.SessionManager;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String categoriaSelecionada = "geral";
    private ImageButton botao;
    private String url;
    private ListView anunciosListView;
    private SearchResponse searchResponse;
    private List<Anuncio> anuncios;
    private AdapterProduto adapter;
    private ActivityMainBinding binding;
    private SessionManager session;
    private HashMap<String, String> usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.content.setCategoria(categoriaSelecionada);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        session = new SessionManager(getApplicationContext());

        url = getResources().getString(R.string.webservice).concat("anuncio/");
        // Requests a general products list.
        this.requestProdutos(url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        if (session.isLoggedIn()) {
            usuario = session.getUserDetails();
            System.out.println("SEJA BEM-VINDO: "+ usuario);
            inflater.inflate(R.menu.user_menu, menu);
        } else {
            inflater.inflate(R.menu.main_menu, menu);
        }
        return true;
    }

    /**
     * Fetches all ads by the selected category and show it to user.
     * @param view
     */
    public void buscarListaPorCategoria(View view) {
        binding.content.categoriaTV.setText(view.getContentDescription().toString());
        this.requestProdutos(url.concat(view.getContentDescription().toString()));
    }

    public void requestProdutos(String url) {
        // A request for retrieving a JSONArray.
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
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
                                    goToAdActivity(view, position);
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

    public void goToAdActivity(View view, int position) {
        NavigationModule.goToAdActivity(this, anuncios.get(position));
    }

    public void goToLoginActivity(MenuItem item) {
        NavigationModule.goToLoginActivity(this);
    }

    public void goToSignupActivity(MenuItem item) {
        NavigationModule.goToSignupActivity(this);
    }

    public void logout(MenuItem item) {
        session.logoutUser();
        finish();
        startActivity(getIntent());
    }
}