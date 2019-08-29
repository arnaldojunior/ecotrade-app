package com.arnaldojunior.ecotrade;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.arnaldojunior.ecotrade.databinding.ActivityNewAdBinding;
import com.arnaldojunior.ecotrade.databinding.ContentNewAdBinding;
import com.arnaldojunior.ecotrade.model.Address;
import com.arnaldojunior.ecotrade.model.Anuncio;
import com.arnaldojunior.ecotrade.model.Usuario;
import com.arnaldojunior.ecotrade.util.Mask;
import com.arnaldojunior.ecotrade.util.NavigationModule;
import com.arnaldojunior.ecotrade.util.RequestQueueSingleton;
import com.arnaldojunior.ecotrade.util.SessionManager;
import com.arnaldojunior.ecotrade.util.TextInputValidator;
import com.arnaldojunior.ecotrade.view.CategoryFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class NewAdActivity extends AppCompatActivity implements CategoryFragment.OnListFragmentInteractionListener {

    private ActivityNewAdBinding binding;
    private SessionManager session;
    private CategoryFragment fragment;
    private TextInputLayout cepTextInput;
    private TextInputLayout valorTextInput;
    private EditText logradouroEditText;
    private TextView localizacaoTextView;
    private MaterialButton doarButton;
    private MaterialButton venderButton;
    private Gson gson = new Gson();
    private Anuncio anuncio = new Anuncio();
    private ContentNewAdBinding layout;
    private Address address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_ad);
        layout = binding.newAdContent;

        cepTextInput = layout.cepTextInput;
        localizacaoTextView = layout.localizacaoTextView;
        logradouroEditText = layout.logradouroTextInput.getEditText();
        doarButton = layout.finalidadeDoar;
        venderButton = layout.finalidadeVender;
        valorTextInput = layout.newAdValor;

        setSupportActionBar(binding.newAdToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setInputListeners();

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> credentials = session.getUserDetails();
        requestUserByEmail(credentials.get("email"));
    }

    public void setInputListeners() {
        layout.newAdTituloInput.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    TextInputValidator.validate(layout.newAdTituloInput);
                }
            }
        });

        doarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectButton((MaterialButton) v);
                deselectButton(venderButton);
                if (valorTextInput.getVisibility() == View.VISIBLE) {
                    valorTextInput.setVisibility(View.INVISIBLE);
                }
            }
        });

        venderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectButton((MaterialButton) v);
                deselectButton(doarButton);
                valorTextInput.setVisibility(View.VISIBLE);
            }
        });

        layout.newAdCategoria.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategorySelectionFragment();
            }
        });

        final EditText cepEditText = cepTextInput.getEditText();
        cepEditText.addTextChangedListener(Mask.insert("##.###-###", cepEditText));

        cepEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    String cep = Mask.unmask(cepEditText.getText().toString());
                    System.out.println("CEP: "+ cep);
                    if (TextInputValidator.isCEPValid(cep)) {
                        requestAddress(cep);
                        return true;
                    } else {
                        cepTextInput.setError("");
                        if (logradouroEditText.getText().length() > 0) {
                            logradouroEditText.setText("");
                        }
                        if (localizacaoTextView.getVisibility() == View.VISIBLE) {
                            localizacaoTextView.setVisibility(View.GONE);
                        }
                    }
                }
                return false;
            }
        });
    }

    public void requestAddress(String cep) {
        String url = getResources().getString(R.string.viacep_ws);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                0, url.concat(cep).concat("/json/"), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        address = gson.fromJson(response.toString(), Address.class);
                        System.out.println("ADDRESS: "+ address.toString());
                        if (address.getUf() == null) {
                            cepTextInput.setError("CEP não encontrado!");
                            localizacaoTextView.setVisibility(View.GONE);
                            logradouroEditText.setText("");
                        } else {
                            cepTextInput.setError("");
                            logradouroEditText.setText(address.getLogradouro());
                            localizacaoTextView.setText(address.getLocalidade()+" - "+address.getUf());
                            localizacaoTextView.setVisibility(View.VISIBLE);
                        }
                    }
                }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    cepTextInput.setError("Erro ao buscar CEP!");
                }
            });
        RequestQueueSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    /**
     * Open a list fragment to user select one category.
     */
    public void openCategorySelectionFragment() {
        fragment = new CategoryFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.new_ad_contraint_layout, fragment);
        transaction.commit();
    }

    @Override
    public void onListFragmentInteraction(String item) {
        layout.newAdCategoria.getEditText().setText(item.toString());
        TextInputValidator.validate(layout.newAdCategoria);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.remove(fragment);
        transaction.commit();
    }

    /**
     * Build a json object and send it to webservice.
     * @param view
     */
    public void sendForm(View view) {
        if (isFormValid()) {
            anuncio.getProduto().setNome(layout.newAdTituloInput.getEditText().getText().toString().trim());
            anuncio.getProduto().setDescricao(layout.newAdDescricaoInput.getEditText().getText().toString().trim());
            anuncio.setFinalidade(getSelectedButton(doarButton, venderButton).getText().toString());
            if (isButtonSelected(venderButton)) {
                anuncio.setValor(valorTextInput.getEditText().getText().toString());
            }
            anuncio.getCategoria().setNome(layout.newAdCategoria.getEditText().getText().toString());

            String cep = Mask.unmask(cepTextInput.getEditText().getText().toString());
            address.setCep(Mask.unmask(address.getCep()));
            // if current address is valid
            if (!TextInputValidator.isEmpty(cepTextInput.getEditText())
                && cep.equalsIgnoreCase(address.getCep())) {
                anuncio.getEndereco().setCep(address.getCep());
                anuncio.getEndereco().setLogradouro(address.getLogradouro());
                anuncio.getEndereco().setBairro(address.getBairro());
                anuncio.getCidade().getUf().setUf(address.getUf());
                anuncio.getCidade().setNome(address.getLocalidade());
            }

            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(gson.toJson(anuncio));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String url = getResources().getString(R.string.webservice).concat("anuncio");
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST, url, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            showConfirmationDialog();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Erro ao cadastrar!", Toast.LENGTH_SHORT).show();
                }
            });
            RequestQueueSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
        }
    }

    /**
     * Check the validation of required fields.
     * @return whether form is valid or not.
     */
    public boolean isFormValid() {
        boolean noErrors = true;
        if (!TextInputValidator.validate(layout.newAdTituloInput))
            noErrors = false;

        if (!TextInputValidator.validate(layout.newAdCategoria))
            noErrors = false;

        if (!isButtonSelected(doarButton) && !isButtonSelected(venderButton)) {
            noErrors = false;
            Toast.makeText(getApplicationContext(), "Escolha uma finalidade!", Toast.LENGTH_SHORT).show();
        } else {
            if (isButtonSelected(venderButton)) {
                if (TextInputValidator.isEmpty(valorTextInput.getEditText())) {
                    noErrors = false;
                    valorTextInput.setError("Campo obrigatório!");
                } else {
                    valorTextInput.setError("");
                }
            }
        }
        if (TextInputValidator.validate(cepTextInput)) {
            String cep = Mask.unmask(cepTextInput.getEditText().getText().toString());
            if (!TextInputValidator.isCEPValid(cep)) {
                cepTextInput.setError("CEP inválido!");
                noErrors = false;
            } else {
                cepTextInput.setError("");
            }
        }
        return noErrors;
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
                        NavigationModule.goToMainActivity(getApplicationContext());
                    }})
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        NavigationModule.goToMainActivity(getApplicationContext());
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void selectButton(MaterialButton button) {
        button.setBackgroundColor(getResources().getColor(R.color.button_option));
        button.setTextColor(getResources().getColor(R.color.white));
        valorTextInput.setError("");
    }

    public void deselectButton(MaterialButton button) {
        button.setBackgroundColor(getResources().getColor(R.color.white));
        button.setTextColor(getResources().getColor(R.color.button_option));
    }

    public boolean isButtonSelected(MaterialButton button) {
        return button.getCurrentTextColor() == getResources().getColor(R.color.button_option)
                ? false : true;
    }

    public MaterialButton getSelectedButton(MaterialButton button1, MaterialButton button2) {
        return isButtonSelected(button1) ? button1 : button2;
    }

    public void requestUserByEmail(String email) {
        String url = getResources().getString(R.string.webservice).concat("usuario/");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                0, url.concat("email/" + email), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Usuario usuario = gson.fromJson(response.toString(), Usuario.class);
                        anuncio.setUsuario(usuario);
                        System.out.println("USUÁRIO: "+ anuncio.getUsuario().getNome());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Erro ao buscar usuário!", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueueSingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
}
