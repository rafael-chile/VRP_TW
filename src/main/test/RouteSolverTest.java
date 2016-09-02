//github pass123
import com.vrptw.constraints.RouteSolver;
import org.chocosolver.solver.Solver;

import java.util.ArrayList;
import java.util.List;


public class RouteSolverTest {

    /************* C O N S T A N T S **************/



    private static String testID = "vrp5-2_LS2_wed3.txt";
    private static int nbCustomers = 4;
    private static int nbVehicles = 8;
    private static int[] vCap = new int[] {400, 400, 600, 600, 600, 600, 600, 600};
    private static int[] vehicleID = new int[] {6001, 6002, 7001, 7002, 10001, 10002, 13001, 13002};
    private static int[] servTime =  new int[] {1800, 1800, 1800, 1800, 1800, 1800, 1800, 1800};
    private static int[] qty = new int[] {0,125,2100,100,890};
    private static int[][] costs = new int[][]
            {{    0,   16,   15,    7,   24},
                    {   16,    0,    0,   13,   11},
                    {   15,    0,    0,   12,   11},
                    {    7,   13,   12,    0,   21},
                    {   24,   11,   11,   21,    0}};
    private static int[][] travTime = new int[][]
            {{    0,  921,  915,  500, 1314},
                    {  934,    0,   53,  765,  687},
                    {  928,   53,    0,  757,  722},
                    {  554,  796,  789,    0, 1188},
                    { 1317,  684,  721, 1146,    0}};
    private static int[][] tWin = new int[][]
            {{21600, 86400},{25200, 72000},{21600, 46800},{46800, 64800},{28800, 86400}};
//sumV_Kg = 4400  sumKg  = 3215


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
