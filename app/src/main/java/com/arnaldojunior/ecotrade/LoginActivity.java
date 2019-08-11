package com.arnaldojunior.ecotrade;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.arnaldojunior.ecotrade.databinding.ActivityLoginBinding;
import com.arnaldojunior.ecotrade.model.Usuario;
import com.arnaldojunior.ecotrade.util.RequestQueueSingleton;
import com.arnaldojunior.ecotrade.util.SessionManager;
import com.arnaldojunior.ecotrade.util.TextInputValidator;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.view.View;
import android.widget.Toast;

import org.json.JSONObject;

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
    }

    public void doLogin(View view) {
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

    /**
     * Check if all inputs are valid.
     * @return a boolean
     */
    public boolean validateFields() {
        TextInputLayout emailLayout = binding.loginContent.loginEmailInput;
        TextInputLayout senhaLayout = binding.loginContent.loginSenhaInput;
        boolean isAllValids = true;

        if (!TextInputValidator.validate(emailLayout, true, false)) {
            isAllValids = false;
        }
        if (!TextInputValidator.validate(senhaLayout, false, true)) {
            isAllValids = false;
        }
        return isAllValids;
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

    public void goToSignupActivity(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
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
