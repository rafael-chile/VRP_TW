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

    @SuppressWarnings(value = "unchecked")
    public List<RouteCost> getListorderedBy(String idclient_from, String orderBy) throws SQLException {
        String sqlQuery = "SELECT * FROM route_costs WHERE idclient_from = '" + idclient_from +"' order by " + orderBy + ";" ;
        List<RouteCost> dataList = null;
        try {
            dataList = this.read(RouteCost.class, sqlQuery );
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }

        //dataList.stream().forEach(System.out::println);
        return dataList ;
    }

    public double[][] getDistanceCostMatrix(List<String> idclientLst) throws SQLException {
        return getCostMatrix(idclientLst, COST_TYPE.DISTANCE);
    }
    public double[][] getTimeCostMatrix(List<String> idclientLst) throws SQLException {
        return getCostMatrix(idclientLst, COST_TYPE.TIME);
    }

    enum COST_TYPE{ DISTANCE, TIME }

    @SuppressWarnings(value = "unchecked")
    private double[][] getCostMatrix(List<String> idclientLst, COST_TYPE costType) throws SQLException {
        double[][] costMatrix = new double[idclientLst.size()][idclientLst.size()];
        idclientLst.sort(String::compareTo);

        for(int i=0; i<idclientLst.size(); i++){
            List<RouteCost> rcList = this.getListorderedBy(idclientLst.get(i), "idclient_to");
            for(int j=0; j<idclientLst.size(); j++){
                boolean found = false;
                for(RouteCost rc : rcList) {
                    if( rc.getIdclient_to().equals(idclientLst.get(j)) ){ //search for the id
                        costMatrix[i][j] = (costType==COST_TYPE.DISTANCE) ? rc.getDistance_cost() : rc.getDistance_cost();
                        found = true;
                        break;
                    }
                }
                if(!found){
                    costMatrix[i][j] = -0.0;
                    System.out.println(" A "+costType.name()+" INPUT WAS NOT FOUND FOR ID_FROM:" + idclientLst.get(i) + ", AND ID_TO:" + idclientLst.get(j));
                }
            }
        }

        return costMatrix;
    }

}
