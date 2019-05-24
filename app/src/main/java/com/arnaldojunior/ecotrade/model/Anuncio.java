package com.arnaldojunior.ecotrade.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Anuncio {

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

}