package com.arnaldojunior.ecotrade.model;

import java.util.Objects;

public class Address {
    private String bairro;
    private String cep;
    private String logradouro;
    private String localidade;
    private String uf;
    private String complemento;

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

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getLocalidade() {
        return localidade;
    }

    public void setLocalidade(String localidade) {
        this.localidade = localidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(bairro, address.bairro) &&
                Objects.equals(cep, address.cep) &&
                Objects.equals(logradouro, address.logradouro) &&
                Objects.equals(localidade, address.localidade) &&
                Objects.equals(uf, address.uf) &&
                Objects.equals(complemento, address.complemento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bairro, cep, logradouro, localidade, uf, complemento);
    }

    @Override
    public String toString() {
        return "Address{" +
                "bairro='" + bairro + '\'' +
                ", cep='" + cep + '\'' +
                ", logradouro='" + logradouro + '\'' +
                ", localidade='" + localidade + '\'' +
                ", uf='" + uf + '\'' +
                ", complemento='" + complemento + '\'' +
                '}';
    }
}