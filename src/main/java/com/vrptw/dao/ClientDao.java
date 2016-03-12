package com.vrptw.dao;

import com.vrptw.entities.Clients;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public class ClientDao extends GenericDao{

    public ClientDao() {}

    @SuppressWarnings(value = "unchecked")
    public List<Clients> getList(String idClient) throws SQLException {
        String sqlQuery = "SELECT * FROM clients WHERE idClient =" + idClient ;
        List<Clients> clientsList = null;
        try {
            clientsList = (new ClientDao()).runQuery(Clients.class, sqlQuery );
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return clientsList;
    }

}
