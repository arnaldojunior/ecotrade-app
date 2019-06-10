package com.arnaldojunior.ecotrade;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.arnaldojunior.ecotrade.model.Anuncio;

import org.w3c.dom.Text;

import java.util.List;

public class AdapterProduto extends BaseAdapter {
    private final List<Anuncio> anuncios;
    private final Activity activity;

    public AdapterProduto (List<Anuncio> anuncios, Activity activity) {
        this.anuncios = anuncios;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return anuncios.size();
    }

    @Override
    public Object getItem(int i) {
        return anuncios.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = activity.getLayoutInflater().inflate(R.layout.listview_personalizada, viewGroup, false);

        Anuncio anuncio = anuncios.get(i);

        ImageView imagem = (ImageView) view.findViewById(R.id.listview_personalizada_imagem);
        TextView descricao = (TextView) view.findViewById(R.id.listview_personalizada_descricao);
        TextView finalidade = (TextView) view.findViewById(R.id.listview_personalizada_finalidade);
        TextView quando = (TextView) view.findViewById(R.id.textview_quando);
        TextView local = (TextView) view.findViewById(R.id.textview_local);

        imagem.setImageResource(R.drawable.smart);
        descricao.setText(anuncio.getProduto().getNome());
        finalidade.setText(anuncio.getFinalidade());
        quando.setText(anuncio.getLogradouro());
        local.setText(anuncio.getCidade().getLocalizacao());

        return view;
    }
}
