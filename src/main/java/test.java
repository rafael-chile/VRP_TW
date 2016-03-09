import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.IntConstraintFactory;
import org.chocosolver.solver.search.strategy.IntStrategyFactory;
import org.chocosolver.solver.trace.Chatterbox;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VariableFactory;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Usuario2 on 30/12/2015.
 */

public class test {
    public static void main(String [] arg){

        // 1. Create a Solver
        Solver solver = new Solver("my first problem");
        // 2. Create variables through the variable factory
        IntVar x = VariableFactory.bounded("X", 0, 5, solver);
        IntVar y = VariableFactory.bounded("Y", 0, 5, solver);
        // 3. Create and post constraints by using constraint factories
        solver.post(IntConstraintFactory.arithm(x, "+", y, "<", 5));
        solver.post(IntConstraintFactory.arithm(x, "+", y, ">", 3));
        // 4. Define the search strategy
        solver.set(IntStrategyFactory.lexico_LB(new IntVar[]{x, y}));
        // 5. Launch the resolution process
        solver.findSolution();
        //6. Print search statistics
        Chatterbox.printStatistics(solver);

        System.out.println("x:" + x);
        System.out.println("y:" + y);

     /*   private static Connection c;

        public static List<Vehicles> getVehiclesByPlate(String license_plate) throws SQLException {
            PreparedStatement ps;
            ps = c.prepareCall("SELECT FROM vehicles");
            ps.execute();getResultSet();
            List<Vehicles>
            ResultSet rs = ps.
        }*/
    }
}
