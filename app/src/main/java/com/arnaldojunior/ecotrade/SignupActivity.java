package com.arnaldojunior.ecotrade;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private ActivitySignupBinding binding;
    private String url;
    private Gson gson = new Gson();
    private Usuario usuario = new Usuario();
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);

        setSupportActionBar(binding.cadastroToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        url = getResources().getString(R.string.webservice).concat("usuario/");
        addMasksToInputs();

        session = new SessionManager(getApplicationContext());
        if (session.isLoggedIn()) {
            HashMap<String, String> credentials = session.getUserDetails();
            requestUserByEmail(credentials.get("email"));
        }
    }

    public void addMasksToInputs() {
        EditText foneEditText = binding.cadastroContent.signupTelefoneInput.getEditText();
        foneEditText.addTextChangedListener(Mask.insert("(##)#####-####", foneEditText));

        EditText cpfEditText = binding.cadastroContent.signupCpfInput.getEditText();
        cpfEditText.addTextChangedListener(Mask.insert("###.###.###-##", cpfEditText));
    }

    public void sendForm(View view) {
        View layoutView = findViewById(R.id.cadastro_content);
        if (TextInputValidator.validateAllInputs(layoutView)) {
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

            if (usuario.getId() != null) {
                postParam.put("id", usuario.getId().toString());
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST, url, new JSONObject(postParam),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            usuario = gson.fromJson(response.toString(), Usuario.class);
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
     * Shows a confirmation message to user by an AlertDialog.
     */
    public void showConfirmationDialog() {
        String message = session.isLoggedIn() ? "Atualização realizada" : "Cadastro realizado";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(message.concat(" com sucesso!"))
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

    public void requestUserByEmail(String email) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                0, url.concat("email/" + email), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        usuario = gson.fromJson(response.toString(), Usuario.class);
                        if (usuario.getId() != null) {
                            loadUserData();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Erro ao buscar usuário!", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueueSingleton.getInstance(SignupActivity.this).addToRequestQueue(jsonObjectRequest);
    }

    /**
     * Load user data on form.
     */
    public void loadUserData() {
        binding.cadastroContent.signupNomeInput.getEditText().setText(usuario.getNome());
        binding.cadastroContent.signupCpfInput.getEditText().setText(usuario.getCpf());
        binding.cadastroContent.signupTelefoneInput.getEditText().setText(usuario.getTelefone());
        binding.cadastroContent.signupEmailInput.getEditText().setText(usuario.getEmail());
        binding.cadastroContent.signupSenhaInput.getEditText().setText(usuario.getSenha());
        binding.cadastroContent.signupSendButton.setText("ATUALIZAR");
    }

    /**
     * Go to the MainActivity by passing the registered object.
     */
    public void goToMainActivity() {
        if (!session.isLoggedIn()) {
            session.createLoginSession(usuario.getNome(), usuario.getEmail());
        }
        NavigationModule.goToMainActivity(this);
    }
}