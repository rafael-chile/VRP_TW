package com.vrptw.entities;

public class Article {
    private String documento_final;
    private String idArtigo;
    private String description_artigo;
    private int quantity;
    private String qtd_pedida;
    private String unit_measure;
    private String encomenda;

    public Article(){}

    public String getDocumento_final() {
        return documento_final;
    }

    public void setDocumento_final(String documento_final) {
        this.documento_final = documento_final;
    }

    public String getIdArtigo() {
        return idArtigo;
    }

    public void setIdArtigo(String idArtigo) {
        this.idArtigo = idArtigo;
    }

    public String getDescription_artigo() {
        return description_artigo;
    }

    public void setDescription_artigo(String description_artigo) {
        this.description_artigo = description_artigo;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getQtd_pedida() {
        return qtd_pedida;
    }

    public void setQtd_pedida(String qtd_pedida) {
        this.qtd_pedida = qtd_pedida;
    }

    public String getUnit_measure() {
        return unit_measure;
    }

    public void setUnit_measure(String unit_measure) {
        this.unit_measure = unit_measure;
    }

    public String getEncomenda() {
        return encomenda;
    }

    public void setEncomenda(String encomenda) {
        this.encomenda = encomenda;
    }

    @Override
    public String toString() {
        return "Article: {"
                + "documento_final: " + documento_final
                + ", idArtigo: " + idArtigo
                + ", description_artigo: " + description_artigo
                + ", quantity: " + quantity
                + ", qtd_pedida: " + qtd_pedida
                + ", unit_measure: " + unit_measure
                + ", encomenda: " + encomenda
                + "}";
    }
}
