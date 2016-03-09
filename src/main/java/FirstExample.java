//STEP 1. Import required packages

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FirstExample {
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://127.0.0.1/borrego";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "pass123";

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        try{
            //STEP 2: Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            //STEP 3: Open a connection
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            //STEP 4: Execute a query
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT * FROM vehicles";
            ResultSet rs = stmt.executeQuery(sql);

            //STEP 5: Extract data from result set
            while (rs.next()){
                String idVehicle = rs.getString("idVehicle");
                String licensePlate = rs.getString("license_plate");
                String maxWeight = rs.getString("max_weight");
                String description = rs.getString("description");
                String driverType = rs.getString("driver_type");

                // print the results
                System.out.format("%s, %s, %s, %s, %s\n", idVehicle, licensePlate, maxWeight, description, driverType);

                List<Vehicule> vehiculeLst = new ArrayList<>();
                vehiculeLst.add(new Vehicule(idVehicle, licensePlate)) //todo agregar lo que falta

            }
            //STEP 6: Clean-up environment
            rs.close();
            stmt.close();
            conn.close();
        }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
            }// nothing we can do
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }//end try
        System.out.println("Goodbye!");
    }//end main
}//end FirstExample