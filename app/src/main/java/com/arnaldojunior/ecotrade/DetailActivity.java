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

        TextView nomeTV = (TextView) findViewById(R.id.detailNomeTV);
        nomeTV.setText(anuncio.getProduto().getNome());

        TextView descricaoTV = (TextView) findViewById(R.id.detailDescricaoTV);
        descricaoTV.setText(anuncio.getProduto().getDescricao().concat(
                " Equipamento com pouco uso, bem conservado. Apresenta umas pequenas avarias."+
                " Funciona normalmente."));

        TextView finalidadeTV = (TextView) findViewById(R.id.detailFinalidadeTV);
        finalidadeTV.setText(anuncio.getFinalidade());

        TextView valorTV = (TextView) findViewById(R.id.detailValorTV);
        valorTV.setText(anuncio.getValor() != null ? "R$ "+ anuncio.getValor() : "");

        TextView quandoTV = (TextView) findViewById(R.id.detailQuandoTV);
        quandoTV.setText("Publicado em 20/12/2019 às 18:45");

        TextView anuncianteTV = (TextView) findViewById(R.id.detailAnuncianteTV);
        anuncianteTV.setText("Arnaldo Junior");
    }
}
