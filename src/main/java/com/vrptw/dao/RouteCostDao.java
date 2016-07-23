package com.vrptw.dao;

import com.vrptw.entities.RouteCost;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public class RouteCostDao extends GenericDao {


    @SuppressWarnings(value = "unchecked")
    public List<RouteCost> getListByClient(String idclient_from) throws SQLException {
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
    public List<RouteCost> getListByClientOrderedby (String idclient_from, String orderBy) throws SQLException {
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
        return getCostMatrixByClientList(idclientLst, COST_TYPE.DISTANCE);
    }
    public double[][] getTimeCostMatrix(List<String> idclientLst) throws SQLException {
        return getCostMatrixByClientList(idclientLst, COST_TYPE.TIME);
    }

    enum COST_TYPE{ DISTANCE, TIME }

    @SuppressWarnings(value = "unchecked")
    private double[][] getCostMatrixByClientList(List<String> idclientLst, COST_TYPE costType) throws SQLException {
        double[][] costMatrix = new double[idclientLst.size()][idclientLst.size()];
        idclientLst.sort(String::compareTo);

        for(int i=0; i<idclientLst.size(); i++){
            List<RouteCost> rcList = this.getListByClientOrderedby(idclientLst.get(i), "idclient_to");
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


/* By Location */
    @SuppressWarnings(value = "unchecked")
    public List<RouteCost> getListByLocation(String idlocation_from) throws SQLException {
        String sqlQuery = "SELECT * FROM route_costs WHERE idlocation_from = '" + idlocation_from +"'" ;
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
    public List<RouteCost> getListByLocationOrderedby(String idlocation_from, String orderBy) throws SQLException {
        String sqlQuery = "SELECT * FROM route_costs WHERE idlocation_from = '" + idlocation_from +"' order by " + orderBy + ";" ;
        List<RouteCost> dataList = null;
        try {
            dataList = this.read(RouteCost.class, sqlQuery );
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }

        //dataList.stream().forEach(System.out::println);
        return dataList ;
    }

    public double[][] getDistanceCostMatrixByLocation(List<String> idlocationLst) throws SQLException {
        return getCostMatrixByLocationList(idlocationLst, COST_TYPE.DISTANCE);
    }
    public double[][] getTimeCostMatrixByLocation(List<String> idlocationLst) throws SQLException {
        return getCostMatrixByLocationList(idlocationLst, COST_TYPE.TIME);
    }

    @SuppressWarnings(value = "unchecked")
    private double[][] getCostMatrixByLocationList(List<String> idLocationLst, COST_TYPE costType) throws SQLException {
        double[][] costMatrix = new double[idLocationLst.size()][idLocationLst.size()];
        idLocationLst.sort(String::compareTo);

        for(int i=0; i<idLocationLst.size(); i++){
            List<RouteCost> rcList = this.getListByLocationOrderedby(idLocationLst.get(i), "idlocation_to");
            for(int j=0; j<idLocationLst.size(); j++){
                boolean found = false;
                for(RouteCost rc : rcList) {
                    if( rc.getIdlocation_to().equals(idLocationLst.get(j)) ){ //search for the id
                        costMatrix[i][j] = (costType==COST_TYPE.DISTANCE) ? rc.getDistance_cost() : rc.getTime_cost();
                        found = true;
                        break;
                    }
                }
                if(!found){
                    costMatrix[i][j] = -0.0;
                    System.out.println(" A "+costType.name()+" INPUT WAS NOT FOUND FOR ID_FROM:" + idLocationLst.get(i) + ", AND ID_TO:" + idLocationLst.get(j));
                }
            }
        }

        return costMatrix;
    }

}
