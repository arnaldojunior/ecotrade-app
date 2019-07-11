package com.arnaldojunior.ecotrade.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class Anuncio implements Serializable {
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("categoria")
    @Expose
    private Categoria categoria;
    @SerializedName("cidade")
    @Expose
    private Cidade cidade;
    @SerializedName("finalidade")
    @Expose
    private String finalidade;
    @SerializedName("produto")
    @Expose
    private Produto produto;
    @SerializedName("endereco")
    @Expose
    private Endereco endereco;
    @SerializedName("quando")
    @Expose
    private Object quando;
    @SerializedName("valor")
    @Expose
    private String valor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public String getFinalidade() {
        return finalidade;
    }

    public void setFinalidade(String finalidade) {
        this.finalidade = finalidade;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Object getQuando() {
        return quando;
    }

    public void setQuando(Object quando) {
        this.quando = quando;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Anuncio anuncio = (Anuncio) o;
        return Objects.equals(id, anuncio.id) &&
                Objects.equals(categoria, anuncio.categoria) &&
                Objects.equals(cidade, anuncio.cidade) &&
                Objects.equals(finalidade, anuncio.finalidade) &&
                Objects.equals(produto, anuncio.produto) &&
                Objects.equals(quando, anuncio.quando) &&
                Objects.equals(valor, anuncio.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, categoria, cidade, finalidade, produto, quando, valor);
    }

    @Override
    public String toString() {
        return "Anuncio{" +
                "id='" + id + '\'' +
                ", categoria=" + categoria +
                ", cidade=" + cidade +
                ", finalidade='" + finalidade + '\'' +
                ", produto=" + produto +
                ", quando=" + quando +
                ", valor='" + valor + '\'' +
                '}';
    }
}