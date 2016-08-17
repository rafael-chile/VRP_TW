//github pass123
import com.vrptw.constraints.RouteSolver;
import org.chocosolver.solver.Solver;

import java.util.ArrayList;
import java.util.List;


public class RouteSolverTest {

    /************* C O N S T A N T S **************/


    private static String testID = "ThirdSol_1.txt";
    private static int nbCustomers = 9;
    private static int nbVehicles = 8;
    private static int[] vCap = new int[] {400, 400, 600, 1000, 7500, 10000, 15000, 15000};
    private static int[] vehicleID = new int[] {14, 15, 7, 12, 2, 16, 3, 4};
    private static int[] servTime =  new int[] {1800, 1800, 1800, 1800, 1800, 2700, 2700, 2700};
    private static int[] qty = new int[] {0,1210,4700,42,1243,3208,450,3150,4599,100};
    private static int[][] costs = new int[][]
            {{    0,   18,   11,   32,   84,   85,   14,   13,   82,   26},
                    {   19,    0,   30,   50,   66,   66,    5,   33,   64,   12},
                    {   11,   30,    0,   36,   86,   87,   28,    3,   84,   19},
                    {   32,   51,   37,    0,  141,  142,   47,   34,  139,   74},
                    {   85,   67,   87,  143,    0,    3,   70,   90,    5,   69},
                    {   85,   67,   87,  143,    3,    0,   71,   90,    5,   69},
                    {   15,    5,   27,   47,   69,   70,    0,   29,   67,    8},
                    {   13,   33,    3,   33,   89,   89,   30,    0,   87,   22},
                    {   83,   65,   81,  137,    3,    4,   69,   84,    0,   63},
                    {   24,   12,   19,   75,   68,   68,    8,   22,   66,    0}};
    private static int[][] travTime = new int[][]
            {{    0, 1292,  870, 2026, 4469, 4487, 1114, 1039, 4356, 1539},
                    { 1357,    0, 1643, 3254, 3296, 3315,  474, 1905, 3184,  919},
                    { 1012, 1717,    0, 2597, 4505, 4524, 1563,  247, 4393, 1218},
                    { 2017, 3293, 2467,    0, 6343, 6360, 3115, 2302, 6230, 3055},
                    { 4617, 3401, 4526, 6581,    0,  227, 3602, 4787,  370, 3526},
                    { 4606, 3391, 4516, 6575,  190,    0, 3591, 4777,  359, 3515},
                    { 1205,  477, 1492, 3102, 3554, 3571,    0, 1753, 3441,  700},
                    { 1018, 1907,  165, 2349, 4696, 4714, 1754,    0, 4584, 1409},
                    { 4619, 3404, 4635, 6691,  354,  372, 3604, 4897,    0, 3635},
                    { 1515,  875, 1132, 3192, 3433, 3450,  547, 1398, 3320,    0}};
    private static int[][] tWin = new int[][]
            {{28800, 72000},{28800, 86400},{50400, 72000},{28800, 57600},{28800, 86400},{28800, 86400},{28800, 86400},{28800, 86400},{21600, 46800},{25200, 46800}};


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
