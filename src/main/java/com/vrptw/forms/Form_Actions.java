package com.vrptw.forms;

import com.vrptw.dao.ArticleDao;
import com.vrptw.dao.ClientDao;
import com.vrptw.dao.OrderdsDao;
import com.vrptw.entities.Article;
import com.vrptw.entities.Client;
import com.vrptw.entities.Orders;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Form_Actions {

    private static final String orderdsTable_Header[] = new String[] { "Encomenda", "Date Emissao", "Client" };
    private static final String articlesTable_Header[] = new String[] { "id Doc Final", "id Article", "Desc Article",
            "quantity", "Qtd Pedida", "Unit Measure", "Encomenda" };
    private static final String clientsTable_Header[] = new String[] { "id Client", "Localidade Postal",
            "TW From", "TW To", "Loc Lat","Loc Lon"};

    public static int LoadOrderdsIntoTable(Date dateFrom, Date dateTo, JTable table1){
        if (dateFrom == null || dateTo == null){ return 0; }
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String fromDate = formatter.format(dateFrom);
            String toDate = formatter.format(dateTo);

            List<Orders> ordersList = (new OrderdsDao()).getList(fromDate, toDate);
            Set<String> clientIdList = new HashSet<>();
            ordersList.stream().forEach(val->clientIdList.add(val.getClient()));

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

            return clientIdList.size();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void LoadArticlesIntoTable(String idEncomenda, JTable table){
        try {
            List<Article> ordersList = (new ArticleDao()).getList(idEncomenda);

            DefaultTableModel dtm = new DefaultTableModel(0, 0);
            dtm.setColumnIdentifiers(articlesTable_Header);
            table.setModel(dtm);

            // add row dynamically into the table
            ordersList.stream().forEach(val -> dtm.addRow(new Object[] {
                    val.getDocumento_final(), val.getIdArtigo(), val.getDescription_artigo(), val.getQuantity(),
                    val.getQtd_pedida(), val.getUnit_measure(), val.getEncomenda()
            }));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void LoadClientIntoTable(String idClient, JTable table){
        try {
            List<Client> ordersList = (new ClientDao()).getList(idClient);

            DefaultTableModel dtm = new DefaultTableModel(0, 0);
            dtm.setColumnIdentifiers(clientsTable_Header);
            table.setModel(dtm);

            // add row dynamically into the table
            ordersList.stream().forEach(val -> dtm.addRow(new Object[] {
                    val.getIdClient(), val.getLocalidade_postal(), val.getTw_from(), val.getTw_to(),
                    val.getLocation_lat(), val.getLocation_lng()
            }));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
