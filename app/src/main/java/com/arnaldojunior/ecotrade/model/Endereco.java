package com.arnaldojunior.ecotrade.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class Endereco implements Serializable {
    @SerializedName("logradouro")
    @Expose
    private String logradouro;
    @SerializedName("bairro")
    @Expose
    private String bairro;
    @SerializedName("cep")
    @Expose
    private String cep;
    @SerializedName("numero")
    @Expose
    private int numero;

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Endereco endereco = (Endereco) o;
        return numero == endereco.numero &&
                Objects.equals(logradouro, endereco.logradouro) &&
                Objects.equals(bairro, endereco.bairro) &&
                Objects.equals(cep, endereco.cep);
    }

    @Override
    public int hashCode() {
        return Objects.hash(logradouro, bairro, cep, numero);
    }

    @Override
    public String toString() {
        return "Endereco{" +
                "logradouro='" + logradouro + '\'' +
                ", bairro='" + bairro + '\'' +
                ", cep='" + cep + '\'' +
                ", numero=" + numero +
                '}';
    }
}
