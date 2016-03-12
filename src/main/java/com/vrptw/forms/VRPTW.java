/*
 * Created by JFormDesigner on Thu Mar 10 15:31:22 GMT 2016
 */

package com.vrptw.forms;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

/**
 * @author Rafael Santana
 */
public class VRPTW extends JFrame {

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Rafael Santana
    private JTabbedPane tabbedPane1;
    private JPanel panel2;
    private JPanel panel3;
    private JScrollPane scrollPane1;
    private JTable table1;
    private JPanel panel1;
    private JScrollPane scrollPane2;
    private JTable table2;
    private JPanel panel4;
    private JScrollPane scrollPane3;
    private JTable table3;
    private JTabbedPane tabbedPane2;
    private JTabbedPane tabbedPane3;
    private JPanel selectDate;
    //private JTextField dateFrom;
    private JDatePickerImpl dateFrom;
    private JLabel fromDate;
    //private JTextField dateTo;
    private JDatePickerImpl dateTo;
    private JLabel toDate;
    private JButton searchDate;
    private JLabel numOrders;
    private JLabel numClients;
    private JLabel totalLocations;
    private JLabel numOrdersValue;
    private JLabel numClientsValue;
    private JLabel totalLocationsValue;
    private JLabel searchSummary;
    private JLabel totalArticles;
    private JLabel totalArticlesValue;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    public VRPTW() {
        initComponents();
    }

    /** Action for the Action button */
    private void sActionPerformed(ActionEvent e) {
        Date fromDate = (Date) dateFrom.getModel().getValue();
        Date toDate = (Date) dateTo.getModel().getValue();

        Form_Actions.LoadOrderdsIntoTable(fromDate, toDate, table1);
    }

    /** Action Event when clicked on Orders' table row */
    private void fieldSelectionMouseClicked(MouseEvent e) {
        int row = table1.getSelectedRow(); //table1.rowAtPoint(e.getPoint());
        int col = 2; // table1.columnAtPoint(e.getPoint());
        String idClient = table1.getValueAt(row, col).toString();

        Form_Actions.LoadClientIntoTable(idClient, table3);
    }

    public class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {

        private String datePattern = "yyyy-MM-dd";
        private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

        @Override
        public Object stringToValue(String text) throws ParseException {
            return dateFormatter.parseObject(text);
        }

        @Override
        public String valueToString(Object value) throws ParseException {
            if (value != null) {
                Calendar cal = (Calendar) value;
                return dateFormatter.format(cal.getTime());
            }

            return "";
        }

    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Rafael Santana
        tabbedPane1 = new JTabbedPane();
        panel2 = new JPanel();
        panel3 = new JPanel();
        scrollPane1 = new JScrollPane();

        String[] columnNames = {"IdDoc Final", "Encomenda", "Qtd Pedida","Unit Measure", "Date Emissao", "Client"};
        Object[][] data = { };

        table1 = new JTable(data, columnNames);

        selectDate = new JPanel();


        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");

        table1 = new JTable();
        panel1 = new JPanel();
        scrollPane2 = new JScrollPane();
        table2 = new JTable();
        panel4 = new JPanel();
        scrollPane3 = new JScrollPane();
        table3 = new JTable();
        tabbedPane2 = new JTabbedPane();
        tabbedPane3 = new JTabbedPane();
        selectDate = new JPanel();
        numOrders = new JLabel();
        numClients = new JLabel();
        totalLocations = new JLabel();
        numOrdersValue = new JLabel();
        numClientsValue = new JLabel();
        totalLocationsValue = new JLabel();
        searchSummary = new JLabel();
        totalArticles = new JLabel();
        totalArticlesValue = new JLabel();
        dateFrom = new JDatePickerImpl(new JDatePanelImpl(new UtilDateModel(), p), new DateLabelFormatter()); //new JTextField();
        fromDate = new JLabel();
        dateFrom.getModel().setSelected(true);
        dateTo = new JDatePickerImpl(new JDatePanelImpl(new UtilDateModel(), p), new DateLabelFormatter()); //new JTextField();
        toDate = new JLabel();
        dateTo.getModel().setSelected(true);

        searchDate = new JButton();

        //======== this ========
        setTitle("Borrego Leonor & Irm\u00e3o S.A.");
        Container contentPane = getContentPane();

        //======== tabbedPane1 ========
        {
            tabbedPane1.setBorder(null);

            //======== panel2 ========
            {

                // JFormDesigner evaluation mark
                panel2.setBorder(new javax.swing.border.CompoundBorder(
                        new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
                                "", javax.swing.border.TitledBorder.CENTER,
                                javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
                                java.awt.Color.red), panel2.getBorder())); panel2.addPropertyChangeListener(
                    e -> {
                        if("border".equals(e.getPropertyName()))
                            throw new RuntimeException();
                    }
            );


                //======== panel3 ========
                {
                    panel3.setBorder(new TitledBorder("Orders"));

                    //======== scrollPane1 ========
                    {

                        //---- table1 ----
                        table1.setRowSelectionAllowed(true);
                        //table1.setCellSelectionEnabled(true);
                        table1.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                fieldSelectionMouseClicked(e);
                            }
                        });
                        scrollPane1.setViewportView(table1);
                    }

                    GroupLayout panel3Layout = new GroupLayout(panel3);
                    panel3.setLayout(panel3Layout);
                    panel3Layout.setHorizontalGroup(
                            panel3Layout.createParallelGroup()
                                    .addGroup(panel3Layout.createSequentialGroup()
                                            .addContainerGap()
                                            .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 1071, Short.MAX_VALUE)
                                            .addContainerGap())
                    );
                    panel3Layout.setVerticalGroup(
                            panel3Layout.createParallelGroup()
                                    .addGroup(panel3Layout.createSequentialGroup()
                                            .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
                                            .addContainerGap())
                    );
                }

                //======== panel1 ========
                {
                    panel1.setBorder(new TitledBorder("Articles (Selected Order Details)"));

                    //======== scrollPane2 ========
                    {
                        scrollPane2.setViewportView(table2);
                    }

                    GroupLayout panel1Layout = new GroupLayout(panel1);
                    panel1.setLayout(panel1Layout);
                    panel1Layout.setHorizontalGroup(
                            panel1Layout.createParallelGroup()
                                    .addGroup(panel1Layout.createSequentialGroup()
                                            .addContainerGap()
                                            .addComponent(scrollPane2, GroupLayout.DEFAULT_SIZE, 1071, Short.MAX_VALUE)
                                            .addContainerGap())
                    );
                    panel1Layout.setVerticalGroup(
                            panel1Layout.createParallelGroup()
                                    .addGroup(GroupLayout.Alignment.TRAILING, panel1Layout.createSequentialGroup()
                                            .addComponent(scrollPane2, GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
                                            .addContainerGap())
                    );
                }

                //======== panel4 ========
                {
                    panel4.setBorder(new TitledBorder("Client (Selected Order Details)"));

                    //======== scrollPane3 ========
                    {
                        scrollPane3.setViewportView(table3);
                    }

                    GroupLayout panel4Layout = new GroupLayout(panel4);
                    panel4.setLayout(panel4Layout);
                    panel4Layout.setHorizontalGroup(
                            panel4Layout.createParallelGroup()
                                    .addGroup(GroupLayout.Alignment.TRAILING, panel4Layout.createSequentialGroup()
                                            .addContainerGap()
                                            .addComponent(scrollPane3, GroupLayout.DEFAULT_SIZE, 1071, Short.MAX_VALUE)
                                            .addContainerGap())
                    );
                    panel4Layout.setVerticalGroup(
                            panel4Layout.createParallelGroup()
                                    .addGroup(panel4Layout.createSequentialGroup()
                                            .addComponent(scrollPane3, GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
                                            .addContainerGap())
                    );
                }

                GroupLayout panel2Layout = new GroupLayout(panel2);
                panel2.setLayout(panel2Layout);
                panel2Layout.setHorizontalGroup(
                        panel2Layout.createParallelGroup()
                                .addGroup(panel2Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(panel2Layout.createParallelGroup()
                                                .addComponent(panel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(panel1, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(panel4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addContainerGap())
                );
                panel2Layout.setVerticalGroup(
                        panel2Layout.createParallelGroup()
                                .addGroup(panel2Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(panel3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(panel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(panel4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addContainerGap())
                );
            }
            tabbedPane1.addTab("Search Results", panel2);
            tabbedPane1.addTab("Solution", tabbedPane2);
            tabbedPane1.addTab("Text", tabbedPane3);
        }

        //======== selectDate ========
        {
            selectDate.setBorder(new TitledBorder("Search by Date"));

            //---- fromDate ----
            fromDate.setText("From:");

            //---- toDate ----
            toDate.setText("To:");

            //---- searchDate ----
            searchDate.setText("Search");
            searchDate.addActionListener(this::sActionPerformed);

            //---- numOrders ----
            numOrders.setText("Num. Orders:");

            //---- numClients ----
            numClients.setText("Num.Clients:");

            //---- totalLocations ----
            totalLocations.setText("Total Locations:");

            //---- numOrdersValue ----
            numOrdersValue.setBackground(Color.lightGray);
            numOrdersValue.setBorder(null);
            numOrdersValue.setText("0");
            numOrdersValue.setHorizontalAlignment(SwingConstants.LEFT);
            numOrdersValue.setForeground(Color.gray);

            //---- numClientsValue ----
            numClientsValue.setBackground(Color.lightGray);
            numClientsValue.setBorder(null);
            numClientsValue.setText("0");
            numClientsValue.setHorizontalAlignment(SwingConstants.LEFT);
            numClientsValue.setForeground(Color.gray);

            //---- totalLocationsValue ----
            totalLocationsValue.setBackground(Color.lightGray);
            totalLocationsValue.setBorder(null);
            totalLocationsValue.setText("0");
            totalLocationsValue.setHorizontalAlignment(SwingConstants.LEFT);
            totalLocationsValue.setForeground(Color.gray);

            //---- searchSummary ----
            searchSummary.setText("Search Summary:");

            //---- totalArticles ----
            totalArticles.setText("Total Articles:");

            //---- totalArticlesValue ----
            totalArticlesValue.setBackground(Color.lightGray);
            totalArticlesValue.setBorder(null);
            totalArticlesValue.setText("0");
            totalArticlesValue.setHorizontalAlignment(SwingConstants.LEFT);
            totalArticlesValue.setForeground(Color.gray);

            GroupLayout selectDateLayout = new GroupLayout(selectDate);
            selectDate.setLayout(selectDateLayout);
            selectDateLayout.setHorizontalGroup(
                    selectDateLayout.createParallelGroup()
                            .addGroup(GroupLayout.Alignment.TRAILING, selectDateLayout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(fromDate)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(dateFrom, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(toDate)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(dateTo, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(searchDate, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(selectDateLayout.createParallelGroup()
                                            .addGroup(selectDateLayout.createSequentialGroup()
                                                    .addComponent(numOrders)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(numOrdersValue, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(numClients)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(numClientsValue, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addComponent(totalArticles)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(totalArticlesValue, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(totalLocations)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(totalLocationsValue, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE))
                                            .addComponent(searchSummary))
                                    .addContainerGap())
            );
            selectDateLayout.setVerticalGroup(
                    selectDateLayout.createParallelGroup()
                            .addGroup(selectDateLayout.createSequentialGroup()
                                    .addGroup(selectDateLayout.createParallelGroup()
                                            .addGroup(selectDateLayout.createSequentialGroup()
                                                    .addGap(10, 10, 10)
                                                    .addComponent(searchSummary)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addGroup(selectDateLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                            .addComponent(numOrders)
                                                            .addComponent(totalLocations)
                                                            .addComponent(totalLocationsValue)
                                                            .addComponent(numClients)
                                                            .addComponent(numClientsValue)
                                                            .addComponent(totalArticles)
                                                            .addComponent(numOrdersValue)
                                                            .addComponent(totalArticlesValue)))
                                            .addGroup(selectDateLayout.createSequentialGroup()
                                                    .addGap(20, 20, 20)
                                                    .addGroup(selectDateLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                            .addComponent(fromDate)
                                                            .addComponent(dateFrom, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(toDate)
                                                            .addComponent(dateTo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(searchDate))))
                                    .addContainerGap(18, Short.MAX_VALUE))
            );
        }

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(contentPaneLayout.createParallelGroup()
                                        .addComponent(selectDate, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(tabbedPane1, GroupLayout.Alignment.TRAILING))
                                .addContainerGap())
        );
        contentPaneLayout.setVerticalGroup(
                contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(selectDate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tabbedPane1, GroupLayout.PREFERRED_SIZE, 643, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }


}