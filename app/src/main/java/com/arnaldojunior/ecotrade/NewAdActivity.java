package com.arnaldojunior.ecotrade;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.arnaldojunior.ecotrade.model.Image;
import com.arnaldojunior.ecotrade.model.Usuario;
import com.arnaldojunior.ecotrade.util.Mask;
import com.arnaldojunior.ecotrade.util.NavigationModule;
import com.arnaldojunior.ecotrade.util.PermissionManager;
import com.arnaldojunior.ecotrade.util.RequestQueueSingleton;
import com.arnaldojunior.ecotrade.util.SessionManager;
import com.arnaldojunior.ecotrade.util.TextInputValidator;
import com.arnaldojunior.ecotrade.view.CategoryFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
    private ImageView newImage;
    private String Document_img1="";
    private boolean permissionsGranted = false;
    private static final int PERMISSIONS_READ_EXTERNAL_STORAGE = 3;

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

        addNewImageView(); // add new image select button
        setInputListeners();

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> credentials = session.getUserDetails();
        requestUserByEmail(credentials.get("email"));
    }

    /**
     * Add a new image select button for user to select.
     */
    public void addNewImageView() {
        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(240, 240));
        imageView.setImageResource(R.drawable.plus);
        layout.newAdImageContainer.addView(imageView);

        int lastChildIndex = layout.newAdImageContainer.getChildCount();
        newImage = (ImageView) layout.newAdImageContainer.getChildAt(lastChildIndex - 1);
        addImageOnClickListener(newImage);
    }

    public void addImageOnClickListener(ImageView imageView) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String permission = Manifest.permission.READ_EXTERNAL_STORAGE;
                if (PermissionManager.hasPermission(getApplicationContext(), permission)) {
                    selectImage();
                } else {
                    Toast.makeText(getApplicationContext(), "Permissões requiridas!",
                            Toast.LENGTH_SHORT).show();
                    PermissionManager.requestPermissions(NewAdActivity.this,
                            new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                            PERMISSIONS_READ_EXTERNAL_STORAGE);
                }
            }
        });
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

            System.out.println("JSON: "+ jsonObject);

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

    /**
     * Calls a options select image dialog.
     */
    private void selectImage() {
        final CharSequence[] options = { "Câmera", "Galeria","Cancelar" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Adicionar Foto");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Câmera"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                }
                else if (options[item].equals("Galeria"))
                {
                    Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                else if (options[item].equals("Cancelar")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    selectImage();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
                    bitmap = getResizedBitmap(bitmap, 400);
                    newImage.setImageBitmap(bitmap);
                    BitMapToString(bitmap);
                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                thumbnail = getResizedBitmap(thumbnail, 400);
                Log.w("Path of image:", picturePath+"");
                newImage.setImageBitmap(thumbnail);

                // Convert image to String and add it to anuncio object.
                Image image = new Image(BitMapToString(thumbnail));
                anuncio.getImages().add(image);
            }
            addNewImageView(); // add new image select button
        }
    }

    public String BitMapToString(Bitmap userImage1) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        userImage1.compress(Bitmap.CompressFormat.PNG, 60, baos);
        byte[] b = baos.toByteArray();
        Document_img1 = Base64.encodeToString(b, Base64.DEFAULT);
        return Document_img1;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}
