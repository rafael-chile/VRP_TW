package com.vrptw.entities;

public class Clients {
    private String idClient;
    private String localidade;
    private String localidade_postal;

    public Clients(){}

    public Clients(String idClient, String localidade, String localidade_postal){
        this.idClient = idClient;
        this.localidade = localidade;
        this.localidade_postal = localidade_postal;
    }

    public String getLocalidade_postal() {
        return localidade_postal;
    }

    public void setLocalidade_postal(String localidade_postal) {
        this.localidade_postal = localidade_postal;
    }

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public String getLocalidade() {
        return localidade;
    }

    public void setLocalidade(String localidade) {
        this.localidade = localidade;
    }

    @Override
    public String toString() {
        return "Clients: {"
                + "idClient: " + idClient
                + ", localidade: " + localidade
                + ", localidade_postal: " + localidade_postal
                + "}";
    }
}
