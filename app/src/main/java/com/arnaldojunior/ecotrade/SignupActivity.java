package com.arnaldojunior.ecotrade;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.arnaldojunior.ecotrade.databinding.ActivitySignupBinding;
import com.arnaldojunior.ecotrade.model.Usuario;
import com.arnaldojunior.ecotrade.util.Mask;
import com.arnaldojunior.ecotrade.util.NavigationModule;
import com.arnaldojunior.ecotrade.util.RequestQueueSingleton;
import com.arnaldojunior.ecotrade.util.SessionManager;
import com.arnaldojunior.ecotrade.util.TextInputValidator;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private ActivitySignupBinding binding;
    private String url;
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

        url = getResources().getString(R.string.webservice).concat("usuario");

        // Add masks to inputs
        EditText foneEditText = binding.cadastroContent.signupTelefoneInput.getEditText();
        foneEditText.addTextChangedListener(Mask.insert("(##)#####-####", foneEditText));

        EditText cpfEditText = binding.cadastroContent.signupCpfInput.getEditText();
        cpfEditText.addTextChangedListener(Mask.insert("###.###.###-##", cpfEditText));
    }

    public void sendForm(View view) {
        if (validateFields()) {
            Usuario usuario = new Usuario();

            usuario.setNome(binding.cadastroContent.signupNomeInput.getEditText().getText().toString());
            usuario.setCpf(binding.cadastroContent.signupCpfInput.getEditText().getText().toString());
            usuario.setTelefone(binding.cadastroContent.signupTelefoneInput.getEditText().getText().toString());
            usuario.setEmail(binding.cadastroContent.signupEmailInput.getEditText().getText().toString());
            usuario.setSenha(binding.cadastroContent.signupSenhaInput.getEditText().getText().toString());

            Map<String, String> postParam = new HashMap<String, String>();
            postParam.put("nome", usuario.getNome());
            postParam.put("cpf", Mask.unmask(usuario.getCpf()));
            postParam.put("telefone", usuario.getTelefone());
            postParam.put("email", usuario.getEmail());
            postParam.put("senha", usuario.getSenha());

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST, url, new JSONObject(postParam),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            gson = new Gson();
                            usuarioCadastrado = gson.fromJson(response.toString(), Usuario.class);
                            showConfirmationDialog();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Erro ao cadastrar!", Toast.LENGTH_SHORT).show();
                }
            });
            RequestQueueSingleton.getInstance(SignupActivity.this).addToRequestQueue(jsonObjectRequest);
        }
    }

    /**
     * Check if all inputs are valid.
     * @return a boolean
     */
    public boolean validateFields() {
        TextInputLayout nomeLayout = binding.cadastroContent.signupNomeInput;
        TextInputLayout cpfLayout = binding.cadastroContent.signupCpfInput;
        TextInputLayout foneLayout = binding.cadastroContent.signupTelefoneInput;
        TextInputLayout emailLayout = binding.cadastroContent.signupEmailInput;
        TextInputLayout senhaLayout = binding.cadastroContent.signupSenhaInput;
        boolean isAllValids = true;

        if (!TextInputValidator.validate(nomeLayout, false, false)) {
            isAllValids = false;
        }
        if (!TextInputValidator.validate(cpfLayout, false, false)) {
            isAllValids = false;
        }
        if (!TextInputValidator.validate(foneLayout, false, false)) {
            isAllValids = false;
        }
        if (!TextInputValidator.validate(emailLayout, true, false)) {
            isAllValids = false;
        }
        if (!TextInputValidator.validate(senhaLayout, false, true)) {
            isAllValids = false;
        }
        return isAllValids;
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
        NavigationModule.goToMainActivity(this);
    }
}