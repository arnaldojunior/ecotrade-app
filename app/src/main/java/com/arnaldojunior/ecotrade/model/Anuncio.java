package com.arnaldojunior.ecotrade.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class Anuncio implements Serializable {

    @SerializedName("bairro")
    @Expose
    private String bairro;
    @SerializedName("categoria")
    @Expose
    private Categoria categoria;
    @SerializedName("cep")
    @Expose
    private String cep;
    @SerializedName("cidade")
    @Expose
    private Cidade cidade;
    @SerializedName("finalidade")
    @Expose
    private String finalidade;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("logradouro")
    @Expose
    private String logradouro;
    @SerializedName("produto")
    @Expose
    private Produto produto;
    @SerializedName("quando")
    @Expose
    private Object quando;
    @SerializedName("valor")
    @Expose
    private String valor;

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
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
        return finalidade.equals(anuncio.finalidade) &&
                id.equals(anuncio.id) &&
                valor.equals(anuncio.valor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(finalidade, id, valor);
    }

    @Override
    public String toString() {
        return "Anuncio{" +
                "bairro='" + bairro + '\'' +
                ", categoria=" + categoria +
                ", cep='" + cep + '\'' +
                ", cidade=" + cidade +
                ", finalidade='" + finalidade + '\'' +
                ", id='" + id + '\'' +
                ", logradouro='" + logradouro + '\'' +
                ", produto=" + produto +
                ", quando=" + quando +
                ", valor='" + valor + '\'' +
                '}';
    }
}