package com.vrptw.forms;

import com.vrptw.dao.OrderdsDao;
import com.vrptw.entities.Orders;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Form_Actions {

    private static final String orderdsTable_Header[] = new String[] {
            "IdDoc Final", "Encomenda", "Qtd Pedida","Unit Measure", "Date Emissao", "Client" };

    public static void LoadOrderdsIntoTable(Date dateFrom, Date dateTo, JTable table1){
        if (dateFrom == null || dateTo == null){ return; }
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String fromDate = formatter.format(dateFrom);
            String toDate = formatter.format(dateTo);

            List<Orders> ordersList = (new OrderdsDao()).getList(fromDate, toDate);
            //l.stream().forEach(System.out::println);

            // ADD DATA INTO TABLE
            DefaultTableModel dtm = new DefaultTableModel(0, 0);

            // add header in table model
            dtm.setColumnIdentifiers(orderdsTable_Header);
            //set model into the table object
            table1.setModel(dtm);

            // add row dynamically into the table
            ordersList.stream().forEach(val -> dtm.addRow(new Object[] {
                    val.getIdDocumento_final(), val.getEncomenda(), val.getQtd_pedida(),
                    val.getUnit_measure(), val.getDate_emissao(), val.getClient()
            }));


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
