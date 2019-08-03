package com.arnaldojunior.ecotrade;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.arnaldojunior.ecotrade.databinding.ActivityLoginBinding;
import com.arnaldojunior.ecotrade.model.Usuario;
import com.arnaldojunior.ecotrade.util.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private static final String URL = "http://192.168.0.15:8080/EcoTradeServer/rest/usuario/email/";
    private SessionManager session;
    private Usuario usuarioBuscado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.login_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        binding.loginContent.loginCadastrarTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCadastroActivity();
            }
        });

        binding.loginContent.loginEntrarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin();
            }
        });
    }

    public void doLogin() {
        if (validateFields()) {

            String email = binding.loginContent.loginEmailInput.getEditText().getText().toString();

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    0, URL.concat(email), null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.out.println("Usuário encontrado: "+ response);
                            Gson gson = new Gson();
                            usuarioBuscado = gson.fromJson(response.toString(), Usuario.class);

                            if (checkLogin()) {
                                goToMainActivity();
                            } else {
                                Context context = getApplicationContext();
                                CharSequence text = "Login Incorreto!";
                                int duration = Toast.LENGTH_SHORT;

                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("Erro ao buscar usuário: "+ error);
                    Toast toast = Toast.makeText(getApplicationContext(), "Usuário não encontrado!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
            RequestQueueSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
        }
    }

    public boolean validateFields() {
        if (binding.loginContent.loginEmailInput.getEditText().getText() != null
            && binding.loginContent.loginSenhaInput.getEditText().getText() != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Check if the password is correct.
     * @return
     */
    public boolean checkLogin() {
        System.out.println("SENHA INPUT: "+ binding.loginContent.loginSenhaInput.getEditText().getText().toString());
        System.out.println("SENHA USUÁRIO: "+ usuarioBuscado.getSenha());
        return binding.loginContent.loginSenhaInput.getEditText().getText().toString()
                .equalsIgnoreCase(usuarioBuscado.getSenha()) ? true : false;
    }

    public void goToCadastroActivity() {
        Intent intent = new Intent(this, CadastroActivity.class);
        startActivity(intent);
    }

    public void goToMainActivity() {
        session = new SessionManager(getApplicationContext());
        session.createLoginSession(usuarioBuscado.getNome(), usuarioBuscado.getEmail());
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
