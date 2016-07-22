package com.vrptw.entities;

import java.util.Date;

public class Orders {

    private String idDocumento_final;
    private String encomenda;
    private String qtd_pedida;
    private String unit_measure;
    Date date_emissao;
    private String location_descarga;
    private String weight_kg;
    private String client;

    public Orders(){}

    public Orders(String idDocumento_final, String encomenda, String qtd_pedida, String unit_measure, Date date_emissao, String location_descarga, String weight_kg, String client){
        this.idDocumento_final = idDocumento_final;
        this.encomenda = encomenda;
        this.qtd_pedida = qtd_pedida;
        this.unit_measure = unit_measure;
        this.date_emissao = date_emissao;
        this.location_descarga = location_descarga;
        this.weight_kg = weight_kg;
        this.client = client;
    }


    public String getIdDocumento_final() {
        return idDocumento_final;
    }

    public void setIdDocumento_final(String idDocumento_final) {
        this.idDocumento_final = idDocumento_final;
    }

    public String getEncomenda() {
        return encomenda;
    }

    public void setEncomenda(String encomenda) {
        this.encomenda = encomenda;
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

    public Date getDate_emissao() {
        return date_emissao;
    }

    public void setDate_emissao(Date date_emissao) {
        this.date_emissao = date_emissao;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getLocation_descarga() { return location_descarga; }

    public void setLocation_descarga(String location_descarga) { this.location_descarga = location_descarga; }

    public String getWeight_kg() { return weight_kg; }

    public void setWeight_kg(String weight_kg) { this.weight_kg = weight_kg;  }


    @Override
    public String toString() {
        return "Order: {"
                + "idDocumento_final: " + idDocumento_final
                + ", encomenda: " + encomenda
                + ", qtd_pedida: " + qtd_pedida
                + ", unit_measure: " + unit_measure
                + ", date_emissao: " + date_emissao
                + ", location_descarga: " + location_descarga
                + ", weight_kg: " + weight_kg
                + ", client: " + client
                + "}";
    }

}
