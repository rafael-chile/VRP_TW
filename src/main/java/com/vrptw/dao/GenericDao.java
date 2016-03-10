package com.vrptw.dao;

import java.sql.SQLException;
import java.util.List;

public abstract class GenericDao<T> {

    public abstract List<T> getList() throws SQLException;
    public abstract List<T> getList(String query) throws SQLException;

    public ConnectionManager connectionManager = new ConnectionManager();

    public GenericDao(){}

}