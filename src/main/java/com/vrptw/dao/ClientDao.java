package com.vrptw.dao;

import com.vrptw.entities.Client;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ClientDao extends GenericDao{

    public ClientDao() {}

    @SuppressWarnings(value = "unchecked")
    public List<Client> getList(String idClient) throws SQLException {

        String sqlQuery = "SELECT C.idClient, C.localidade_postal, C.tw_from, C.tw_to, L.location_lat, L.location_lng " +
                " FROM clients AS C LEFT JOIN locations AS L " +
                " ON C.idClient = L.client AND L.main_location = 'TRUE'" +
                " WHERE C.idClient = '" + idClient +"'" ;

        List<Client> dataList = null;
        try {
            dataList = this.read(Client.class, sqlQuery );
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        //dataList.stream().forEach(System.out::println);
        return dataList ;
    }

    @SuppressWarnings(value = "unchecked")
    public List<Client> getListIDsBetweenDates(Date from, Date until) throws SQLException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String fromDate = formatter.format(from);
        String toDate = formatter.format(until);

        /*String sqlQuery = "SELECT clients.idClient  FROM clients " +
                "INNER JOIN encomendas ON encomendas.client = clients.idClient " +
                " AND encomendas.date_emissao >= \""+fromDate+"\" AND encomendas.date_emissao <= \""+toDate+"\" " +
                "group by clients.idClient ;" ;*/
        String sqlQuery = "SELECT C.idClient, C.localidade_postal, C.tw_from, C.tw_to, L.location_lat, L.location_lng " +
                "FROM clients AS C " +
                "LEFT JOIN locations AS L " +
                "  ON C.idClient = L.client AND L.main_location = 'TRUE' " +
                "WHERE C.idClient IN (SELECT clients.idClient  FROM clients " +
                "  INNER JOIN encomendas ON encomendas.client = clients.idClient " +
                "             AND encomendas.date_emissao >= \""+fromDate+"\" AND encomendas.date_emissao <= \""+toDate+"\"" +
                "group by clients.idClient );" ;

        List<Client> dataList = null;
        try {
            dataList = this.read(Client.class, sqlQuery );
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        //dataList.stream().forEach(System.out::println);
        return dataList ;
    }

}
