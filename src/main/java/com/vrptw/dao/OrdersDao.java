package com.vrptw.dao;

import com.vrptw.entities.Location;
import com.vrptw.entities.Orders;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrdersDao extends GenericDao {

    public OrdersDao(){}

    @SuppressWarnings(value = "unchecked")
    public List<Orders> getList(String from, String to) throws SQLException {
        String sqlQuery = "SELECT DISTINCT idDocumento_final, encomenda, location_descarga, date_emissao, weight_kg, client FROM encomendas " +
                "WHERE date_emissao >= '" + from + "' AND date_emissao <= '" + to + "'";
        List<Orders> ordersList = null;
        try {
            ordersList = this.read(Orders.class, sqlQuery );

        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return ordersList;
    }

    public List<Location> getLocationList(String from, String to) throws SQLException {
        String sqlQuery =
                "SELECT * FROM locations WHERE idlocation IN (\n" +
                        "SELECT DISTINCT location_descarga FROM encomendas \n" +
                        "WHERE date_emissao >= \" " +from+ "\" AND date_emissao <=\" " +to+ "\" AND location_descarga IS NOT NULL)\n" +
                        "UNION SELECT * FROM borrego.locations where idLocation='depot'";

          /*    "SELECT * FROM locations WHERE idlocation IN (" +
                "SELECT DISTINCT location_descarga FROM encomendas " +
                "WHERE date_emissao >= '" + from + "' AND date_emissao <= '" + to + "')";*/
        List<Location> locationList = null;
        try {
            locationList = this.read(Location.class, sqlQuery );

        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return locationList;
    }

    public Location getDepotLocation() throws SQLException {
        String sqlQuery =
                "SELECT * FROM locations WHERE client = '0'";
        List<Location> locationList = null;
        try {
            locationList = this.read(Location.class, sqlQuery );

        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return locationList.get(0);
    }

}
