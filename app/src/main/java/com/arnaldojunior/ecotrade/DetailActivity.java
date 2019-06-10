package com.arnaldojunior.ecotrade;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.arnaldojunior.ecotrade.model.Anuncio;

public class DetailActivity extends AppCompatActivity {

    private Anuncio anuncio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        anuncio = (Anuncio) intent.getSerializableExtra("Anuncio");
        TextView tv = (TextView) findViewById(R.id.produtoTextView);
        tv.setText(anuncio.getProduto().getNome());
    }
}
