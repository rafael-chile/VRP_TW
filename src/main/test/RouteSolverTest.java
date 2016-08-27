//github pass123
import com.vrptw.constraints.RouteSolver;
import org.chocosolver.solver.Solver;

import java.util.ArrayList;
import java.util.List;


public class RouteSolverTest {

    /************* C O N S T A N T S **************/


    private static String testID = "vrp3_ LS1_4-2.txt";
    private static int nbCustomers = 6;
    private static int nbVehicles = 5;
    private static int[] vCap = new int[] {27000, 15000, 15000, 600, 400};
    private static int[] vehicleID = new int[] {1, 3, 4, 7, 15};
    private static int[] servTime =  new int[] {2700, 2700, 2700, 1800, 1800};
    private static int[] qty = new int[] {0,890,40,40,7080,4690,4450};
    private static int[][] costs = new int[][]
            {{    0,   26,   78,   26,    9,    8,   24},
                    {   26,    0,   53,    0,   35,   27,   33},
                    {   78,   53,    0,   53,   89,  102,   87},
                    {   26,    0,   53,    0,   35,   27,   33},
                    {    9,   35,   89,   35,    0,   14,   15},
                    {    8,   27,  103,   27,   14,    0,   28},
                    {   24,   33,   87,   33,   15,   28,    0}};
    private static int[][] travTime = new int[][]
                {{    0, 2144, 5277, 2143,  565,  547, 1314},
                    { 2133,    0, 3301,    1, 1751, 2185, 1724},
                    { 5630, 3646,    0, 3645, 5319, 5794, 5292},
                    { 2132,    1, 3300,    0, 1752, 2185, 1725},
                    {  577, 1765, 4982, 1766,    0,  723,  799},
                    {  562, 2206, 5451, 2205,  735,    0, 1484},
                    { 1317, 1730, 4947, 1731,  785, 1464,    0}};
    private static int[][] tWin = new int[][]
            {{21600, 86400},{28800, 86400},{28800, 86400},{28800, 86400},{28800, 86400},{50400, 75600},{28800, 86400}};
//sumV_Kg = 58000  sumKg  = 17190


    /**********************************************/

    private static int[][] breakTime;
    private static int[][] M_ij;                       /* Constant for equation 6: M_ij = l_i + t_ij - e_j    */
    private static int[][] stM_ij;                       /* Constant for equation 6: M_ij = l_i + t_ij - e_j    */


    /**-------------INSTANCE END--------------*/

    private static int max_cap_big = 1000;       /* Max vehicle capacity: Biggest truck (auxiliary variable) */
    private static int max_cap = 100;             /* Max vehicle capacity: Biggest truck  */
                                            /* Homogeneous and limited fleet that leave and return to the depot
                                            R[i] = {r[i](1),...,r[i](n[i])} denote the route for vehicle 'i',
                                            where r[i](j) is the index of the jth customer visited and n[i] is the nb. of customers in the route.
                                            r[i](n[i] + 1) = 0, every route finishes at the depot  */
    private static int vclCapacity = 100;          // a[k] is total vehicle capacity, of each vehicle k ∈ V

                                    /* Split deliveries: the demand of a customer may be fulfilled by more than one vehicle.
                                        This occurs in all cases where some demand exceeds the vehicle capacity */

    private static RouteSolver testWithLocalTestData(){
        return new RouteSolver(testID, nbCustomers, qty, nbVehicles, vCap, vehicleID, max_cap_big,  max_cap, vclCapacity,
                costs, servTime, travTime, breakTime, tWin, M_ij, stM_ij);
    }

    private static RouteSolver testWithDBTestData(){
        // TODO: get data from db and set it into the parameters; then do : return new RouteSolver(...);
        /*
        RouteCostDao rcDao = new RouteCostDao();
        rcDao.getDistanceCostMatrix(...);
        testID = ;
        nbCustomers = ;
        qty = ;
        nbVehicles = ;
        vCap = ;
        vehicleID = ;
        max_cap_big = ;
        max_cap = ;
        vclCapacity = ;
        costs = ;
        servTime = ;
        travTime = ;
        tWin = ;
        M_ij = ;
        stM_ij = ;
        */
        return new RouteSolver(testID, nbCustomers, qty, nbVehicles, vCap, vehicleID, max_cap_big,  max_cap, vclCapacity,
                costs, servTime, travTime, breakTime, tWin, M_ij, stM_ij);
    }

    /** Run  M A I N  A P P  F O R T E S T */

    public static void main(String [] arg){

        RouteSolver rs = RouteSolverTest.testWithLocalTestData();
        // 1. Create a Solver
        Solver solver = new Solver();

        // 2. Constraints
        rs.addRouteSolvingConstraints(solver);

        // 3. Print Input
        rs.printInput(solver);

        // 4. Find Solution
        rs.findSol(solver);

        // 5. Print Output
        rs.printOutput(solver);


    }
}
