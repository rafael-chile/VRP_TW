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
    private JPanel selectDate;
    //private JTextField dateFrom;
    private JDatePickerImpl dateFrom;
    private JLabel fromDate;
    //private JTextField dateTo;
    private JDatePickerImpl dateTo;
    private JLabel toDate;
    private JButton searchDate;
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
        int col = 5; // table1.columnAtPoint(e.getPoint());
        String idClient = table1.getValueAt(row, col).toString();
        System.out.println("Selected row with IdClient:"+ idClient);

        // TODO: use the value selection to load order data into whatever is required in a second jtable/jtext object
        //Form_Actions.LoadFacturasIntoTable(idClient, table2);
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
        dateFrom = new JDatePickerImpl(new JDatePanelImpl(new UtilDateModel(), p), new DateLabelFormatter()); //new JTextField();
        dateFrom.getModel().setSelected(true);
        fromDate = new JLabel();

        dateTo = new JDatePickerImpl(new JDatePanelImpl(new UtilDateModel(), p), new DateLabelFormatter()); //new JTextField();
        dateTo.getModel().setSelected(true);
        toDate = new JLabel();

        searchDate = new JButton();

        //======== this ========
        setTitle("VRP with TW");
        Container contentPane = getContentPane();

        //======== tabbedPane1 ========
        {
            tabbedPane1.setBorder(null);

            //======== panel2 ========
            {

                // JFormDesigner evaluation mark
                panel2.setBorder(new javax.swing.border.CompoundBorder(
                    new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
                        "By Rafael Mejia", javax.swing.border.TitledBorder.CENTER,
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
                                .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 555, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(92, Short.MAX_VALUE))
                    );
                    panel3Layout.setVerticalGroup(
                        panel3Layout.createParallelGroup()
                            .addGroup(panel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(scrollPane1, GroupLayout.PREFERRED_SIZE, 124, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    );
                }

                GroupLayout panel2Layout = new GroupLayout(panel2);
                panel2.setLayout(panel2Layout);
                panel2Layout.setHorizontalGroup(
                    panel2Layout.createParallelGroup()
                        .addGroup(panel2Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(panel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addContainerGap())
                );
                panel2Layout.setVerticalGroup(
                    panel2Layout.createParallelGroup()
                        .addGroup(panel2Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(panel3, GroupLayout.PREFERRED_SIZE, 155, GroupLayout.PREFERRED_SIZE)
                            .addContainerGap(392, Short.MAX_VALUE))
                );
            }
            tabbedPane1.addTab("Result", panel2);
        }

        //======== selectDate ========
        {
            selectDate.setBorder(new TitledBorder("Select Date"));

            //---- fromDate ----
            fromDate.setText("From:");

            //---- toDate ----
            toDate.setText("To:");

            //---- searchDate ----
            searchDate.setText("Search");
            searchDate.addActionListener(this::sActionPerformed);

            GroupLayout selectDateLayout = new GroupLayout(selectDate);
            selectDate.setLayout(selectDateLayout);
            selectDateLayout.setHorizontalGroup(
                selectDateLayout.createParallelGroup()
                    .addGroup(selectDateLayout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addComponent(fromDate)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dateFrom, GroupLayout.PREFERRED_SIZE, 174, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(toDate)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dateTo, GroupLayout.PREFERRED_SIZE, 174, GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(searchDate, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            selectDateLayout.setVerticalGroup(
                selectDateLayout.createParallelGroup()
                    .addGroup(selectDateLayout.createSequentialGroup()
                        .addGroup(selectDateLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(searchDate)
                            .addComponent(fromDate)
                            .addComponent(toDate)
                            .addComponent(dateFrom, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(dateTo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 15, Short.MAX_VALUE))
            );
        }

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addComponent(tabbedPane1)
                        .addComponent(selectDate, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap())
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addGap(14, 14, 14)
                    .addComponent(selectDate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(tabbedPane1)
                    .addContainerGap())
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }


}
