package com.arnaldojunior.ecotrade;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.arnaldojunior.ecotrade.databinding.ActivityCadastroBinding;
import com.google.android.material.button.MaterialButton;

public class CadastroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCadastroBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_cadastro);

        Toolbar toolbar = findViewById(R.id.cadastro_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MaterialButton botaoEntrar = findViewById(R.id.entrar_button);
        botaoEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
