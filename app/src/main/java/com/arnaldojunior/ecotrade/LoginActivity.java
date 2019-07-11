package com.arnaldojunior.ecotrade;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private TextView signUpTV;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toolbar = findViewById(R.id.login_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        signUpTV = findViewById(R.id.loginCadastrarTV);
        signUpTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irParaCadastro();
            }
        });
    }

    public void irParaCadastro() {
        Intent intent = new Intent(this, CadastroActivity.class);
        startActivity(intent);
    }
}
