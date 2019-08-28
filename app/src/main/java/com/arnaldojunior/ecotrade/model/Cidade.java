package com.arnaldojunior.ecotrade.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class Cidade implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("nome")
    @Expose
    private String nome;
    @SerializedName("uf")
    @Expose
    private Uf uf = new Uf();

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

    public Uf getUf() {
        return uf;
    }

    public void setUf(Uf uf) {
        this.uf = uf;
    }

    public String getLocalizacao() {
        return nome.concat("/").concat(uf.getUf());

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cidade cidade = (Cidade) o;
        return Objects.equals(id, cidade.id) &&
                Objects.equals(nome, cidade.nome) &&
                Objects.equals(uf, cidade.uf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, uf);
    }

    @Override
    public String toString() {
        return "Cidade{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", uf=" + uf +
                '}';
    }
}