package com.arnaldojunior.ecotrade;

import android.os.Bundle;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.arnaldojunior.ecotrade.databinding.ActivityLoginBinding;
import com.arnaldojunior.ecotrade.model.Usuario;
import com.arnaldojunior.ecotrade.util.NavigationModule;
import com.arnaldojunior.ecotrade.util.RequestQueueSingleton;
import com.arnaldojunior.ecotrade.util.SessionManager;
import com.arnaldojunior.ecotrade.util.TextInputValidator;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private String url;
    private SessionManager session;
    private Usuario usuarioBuscado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        setSupportActionBar(binding.loginToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        url = getResources().getString(R.string.webservice).concat("usuario/email/");

        // Clear the error once more than 8 characters are typed.
        final TextInputLayout senhaTextInput = binding.loginContent.loginSenhaInput;
        senhaTextInput.getEditText().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                TextInputValidator.validate(senhaTextInput);
                return false;
            }
        });
    }

    public void doLogin(View view) {
        View layoutView = findViewById(R.id.login_content);
        if (TextInputValidator.validateAllInputs(layoutView)) {

            String email = binding.loginContent.loginEmailInput.getEditText().getText().toString().trim();

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    0, url.concat(email), null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Gson gson = new Gson();
                            usuarioBuscado = gson.fromJson(response.toString(), Usuario.class);
                            // if the password is correct.
                            if (checkLogin()) {
                                goToMainActivity();
                            } else {
                                showToast("Login incorreto!");
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            showToast("Usuário não encontrado!");
                        }
                    });
            RequestQueueSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
        }
    }

    /**
     * Check if the password is correct.
     * @return a boolean telling if the password for that user is correct.
     */
    public boolean checkLogin() {
        return binding.loginContent.loginSenhaInput.getEditText().getText().toString()
                .equalsIgnoreCase(usuarioBuscado.getSenha()) ? true : false;
    }

    public void showToast(CharSequence message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void goToSignupActivity(View view) {
        NavigationModule.goToSignupActivity(this);
    }

    public void goToMainActivity() {
        session = new SessionManager(getApplicationContext());
        session.createLoginSession(usuarioBuscado.getNome(), usuarioBuscado.getEmail());
        NavigationModule.goToMainActivity(this);
    }
}