package com.vrptw.dao;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public abstract class GenericDao<T> {

    public ConnectionManager connectionManager = new ConnectionManager();

    public GenericDao(){}

    @SuppressWarnings(value = "unchecked")
    public List<T> read(Class nClass, String query) throws SQLException, IllegalAccessException,
            InstantiationException, NoSuchMethodException, InvocationTargetException {

        List<T> objList = new ArrayList<>();
        Connection con = this.connectionManager.connect();

        // Execute a query
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        // Extract data from result set
        while (rs != null && rs.next()) {
            if(nClass.equals(String.class)) {  // if is a String
                objList.add((T)rs.getObject(1));
            } else{
                // create a new instance of class to insert data from db
                Object classInstance = nClass.newInstance();
                Method[] methods = nClass.getDeclaredMethods();

                for(Field f : nClass.getDeclaredFields()) {

                    String setterName = Character.toUpperCase(f.getName().charAt(0)) + f.getName().substring(1);
                    //System.out.println("METHOD NAME:"+methodName);
                    for(Method m : methods) {
                        String methodName = "set" + setterName;
                        if(m.getName().equals(methodName)) {
                            Method method = nClass.getDeclaredMethod(methodName, m.getParameterTypes());

                            //retrieve the data
                            try {
                                Object rsField = rs.getObject(f.getName());
                                if (rsField != null){ method.invoke(classInstance, rsField);}
                            }catch (SQLException e){ /* do nothing */}
                        }
                    }
                }

                objList.add((T) classInstance);
            }

            // System.out.println("Result added: " + longitude);
        }

        //finally block used to close resources
        try {
            // Clean-up environment
            this.closeAllConnections(rs, stmt);
        } catch (SQLException ignored) {}// nothing we can do

        return objList;

    }

    public int runInsertQuery( String query) throws SQLException, IllegalAccessException,
            InstantiationException, NoSuchMethodException, InvocationTargetException {

        List<T> objList = new ArrayList<>();
        Connection con = this.connectionManager.connect();

        // Execute a query
        Statement stmt = con.createStatement();
        int result = stmt.executeUpdate(query);

        //finally block used to close resources
        try {
            // Clean-up environment
            this.closeAllConnections(null, stmt);
        } catch (SQLException ignored) {}// nothing we can do

        return result;
    }

    private void closeAllConnections(ResultSet rs, Statement stmt) throws SQLException {
        if (stmt != null) { stmt.close(); }
        if (rs != null ){ rs.close(); }
        connectionManager.disconnect(); // don't disconnect using "con.close()" form
    }
}