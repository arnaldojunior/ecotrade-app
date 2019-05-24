package com.arnaldojunior.ecotrade;

import java.util.Date;

public class Produto {
    private int id;
    private String descricao;
    private Date data;
    private String local;
    private String finalidade; //doação ou venda
    private double valor;

    public Produto (String descricao, String finalidade) {
        this.descricao = descricao;
        this.finalidade = finalidade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getFinalidade() {
        return finalidade;
    }

    public void setFinalidade(String finalidade) {
        this.finalidade = finalidade;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
