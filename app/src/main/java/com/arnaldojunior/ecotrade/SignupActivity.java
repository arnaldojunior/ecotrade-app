package com.arnaldojunior.ecotrade;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.arnaldojunior.ecotrade.databinding.ActivitySignupBinding;
import com.arnaldojunior.ecotrade.model.Usuario;
import com.arnaldojunior.ecotrade.util.RequestQueueSingleton;
import com.arnaldojunior.ecotrade.util.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private ActivitySignupBinding binding;
    private static final String URL = "http://192.168.0.15:8080/EcoTradeServer/rest/usuario";
    private Gson gson;
    private Usuario usuarioCadastrado;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);

        Toolbar toolbar = findViewById(R.id.cadastro_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MaterialButton botaoEntrar = findViewById(R.id.entrar_button);
        botaoEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Usuario usuario = new Usuario();

                usuario.setNome(binding.cadastroContent.cadastroNomeInput.getEditText().getText().toString());
                usuario.setCpf(binding.cadastroContent.cadastroCpfInput.getEditText().getText().toString());
                usuario.setTelefone(binding.cadastroContent.cadastroTelefoneInput.getEditText().getText().toString());
                usuario.setEmail(binding.cadastroContent.cadastroTelefoneInput.getEditText().getText().toString());
                usuario.setSenha(binding.cadastroContent.cadastroSenhaInput.getEditText().getText().toString());

                Map<String, String> postParam = new HashMap<String, String>();
                postParam.put("nome", usuario.getNome());
                postParam.put("cpf", usuario.getCpf());
                postParam.put("telefone", usuario.getTelefone());
                postParam.put("email", usuario.getEmail());
                postParam.put("senha", usuario.getSenha());

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.POST, URL, new JSONObject(postParam),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                System.out.println("Sucesso ao enviar objeto: "+ response);
                                gson = new Gson();
                                usuarioCadastrado = gson.fromJson(response.toString(), Usuario.class);
                                showConfirmationDialog();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                System.out.println("Erro ao enviar objeto Json: "+ error);
                            }
                        });
                RequestQueueSingleton.getInstance(SignupActivity.this).addToRequestQueue(jsonObjectRequest);
            }
        });
    }

    /**
     * Shows a confirmation message to user by an AlertDialog.
     */
    public void showConfirmationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cadastro realizado com sucesso!")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        goToMainActivity();
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        goToMainActivity();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Go to the MainActivity by passing the registered object.
     */
    public void goToMainActivity() {
        session = new SessionManager(getApplicationContext());
        session.createLoginSession(usuarioCadastrado.getNome(), usuarioCadastrado.getEmail());
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}