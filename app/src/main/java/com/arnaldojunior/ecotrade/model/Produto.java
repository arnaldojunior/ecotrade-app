package com.arnaldojunior.ecotrade.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class Produto implements Serializable {

    @SerializedName("descricao")
    @Expose
    private String descricao;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("nome")
    @Expose
    private String nome;

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produto produto = (Produto) o;
        return Objects.equals(descricao, produto.descricao) &&
                Objects.equals(id, produto.id) &&
                Objects.equals(nome, produto.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(descricao, id, nome);
    }

    @Override
    public String toString() {
        return "Produto{" +
                "descricao='" + descricao + '\'' +
                ", id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                '}';
    }
}