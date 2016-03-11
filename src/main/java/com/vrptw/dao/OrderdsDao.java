package com.vrptw.dao;

import com.vrptw.entities.Orders;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public class OrderdsDao extends GenericDao {

    public OrderdsDao(){}

    @SuppressWarnings(value = "unchecked")
    public List<Orders> getList(String from, String to) throws SQLException {
        String sqlQuery = "SELECT * FROM encomendas WHERE date_emissao >= '" + from + "' and date_emissao <= '" + to + "'";
        List<Orders> ordersList = null;
        try {
            ordersList = (new OrderdsDao()).runQuery(Orders.class, sqlQuery );

        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return ordersList;
    }
}
