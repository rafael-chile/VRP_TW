package com.vrptw.dao;

import com.vrptw.entities.RouteCost;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public class RouteCostDao extends GenericDao {


    @SuppressWarnings(value = "unchecked")
    public List<RouteCost> getList(String idclient_from) throws SQLException {
        String sqlQuery = "SELECT * FROM route_costs WHERE idclient_from = '" + idclient_from +"'" ;
        List<RouteCost> dataList = null;
        try {
            dataList = this.read(RouteCost.class, sqlQuery );
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }

        //dataList.stream().forEach(System.out::println);
        return dataList ;
    }

}
