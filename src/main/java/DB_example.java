import java.sql.*;
/**
 * Created by Usuario2 on 06/03/2016.
 */
public class DB_example {
    private static Connection conn;

    public static void main(String[] args)
    {
        try
        {
            PreparedStatement ps;
            String query = "SELECT * FROM vehicles";
            ps = conn.prepareCall(query);
            ps.execute();
            ResultSet rs = ps.getResultSet();

            while (rs.next())
            {
                String idVehicle = rs.getString("idVehicle");
                String licensePlate = rs.getString("license_plate");
                String maxWeight = rs.getString("max_weight");
                String description = rs.getString("description");
                String driverType = rs.getString("driver_type");

                // print the results
                System.out.format("%s, %s, %s, %s, %s\n", idVehicle, licensePlate, maxWeight, description, driverType);
            }
        }
        catch (Exception e)
        {
            System.err.println("Got an exception! ");
            System.err.println(e.getMessage());
        }
    }

}
