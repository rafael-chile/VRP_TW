package com.vrptw.forms;

import com.vrptw.dao.ClientDao;
import com.vrptw.dao.OrderdsDao;
import com.vrptw.entities.Clients;
import com.vrptw.entities.Orders;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Form_Actions {

    private static final String orderdsTable_Header[] = new String[] { "Encomenda", "Date Emissao", "Client" };
    private static final String articlesTable_Header[] = new String[] { "id Doc Final", "id Article", "Desc Article",
            "quantity", "Qtd Pedida", "Unit Measure" };
    private static final String clientsTable_Header[] = new String[] { "idClient", "Localidade", "Localidade_postal" };

    public static void LoadOrderdsIntoTable(Date dateFrom, Date dateTo, JTable table1){
        if (dateFrom == null || dateTo == null){ return; }
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String fromDate = formatter.format(dateFrom);
            String toDate = formatter.format(dateTo);

            List<Orders> ordersList = (new OrderdsDao()).getList(fromDate, toDate);

            // ADD DATA INTO TABLE
            DefaultTableModel dtm = new DefaultTableModel(0, 0);

            // add header in table model
            dtm.setColumnIdentifiers(orderdsTable_Header);
            //set model into the table object
            table1.setModel(dtm);

            // add row dynamically into the table
            ordersList.stream().forEach(val -> dtm.addRow(new Object[] {
                    val.getEncomenda(), val.getDate_emissao(), val.getClient()
            }));


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void LoadArticlesIntoTable(String idEncomenda, JTable table){

        DefaultTableModel dtm = new DefaultTableModel(0, 0);
        dtm.setColumnIdentifiers(articlesTable_Header);
        table.setModel(dtm);

    }

    public static void LoadClientIntoTable(String idClient, JTable table){
        try {
            List<Clients> ordersList = (new ClientDao()).getList(idClient);

            DefaultTableModel dtm = new DefaultTableModel(0, 0);
            dtm.setColumnIdentifiers(clientsTable_Header);
            table.setModel(dtm);

            // add row dynamically into the table
            ordersList.stream().forEach(val -> dtm.addRow(new Object[] {
                    val.getIdClient(), val.getLocalidade(), val.getLocalidade_postal()
            }));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
