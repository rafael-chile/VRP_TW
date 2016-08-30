//github pass123
import com.vrptw.constraints.RouteSolver;
import org.chocosolver.solver.Solver;

import java.util.ArrayList;
import java.util.List;


public class RouteSolverTest {

    /************* C O N S T A N T S **************/

    private static String testID = "vrp5_ mon_1.txt";
    private static int nbCustomers = 6;
    private static int nbVehicles = 8;
    private static int[] vCap = new int[] {400, 400, 600, 600, 400, 400, 10000, 10000};
    private static int[] vehicleID = new int[] {
            6001, 6002,
            9001, 9002,
            1101, 1102,
            1601, 1602};
    private static int[] servTime =  new int[] {1800, 1800, 1800, 1800, 1800, 1800, 2700, 2700};
    private static int[] qty = new int[] {0,100,100,100,100,100,100};   //{0,3150,4599,450,1210,4700,5000};
    private static int[][] costs = new int[][]
            {{    0,   66,   66,    5,    5,   64,   12},
                    {   67,    0,    3,   70,   70,    5,   69},
                    {   67,    3,    0,   70,   71,    5,   69},
                    {    5,   69,   70,    0,    1,   67,    7},
                    {    5,   69,   70,    1,    0,   67,    8},
                    {   65,    3,    4,   68,   69,    0,   63},
                    {   12,   68,   68,    7,    8,   66,    0}};
    private static int[][] travTime = new int[][]
            {{    0, 3296, 3315,  404,  474, 3184,  919},
                    { 3401,    0,  227, 3579, 3602,  370, 3526},
                    { 3391,  190,    0, 3568, 3591,  359, 3515},
                    {  399, 3474, 3493,    0,   71, 3362,  560},
                    {  477, 3554, 3571,  177,    0, 3441,  700},
                    { 3404,  354,  372, 3581, 3604,    0, 3635},
                    {  875, 3433, 3450,  523,  547, 3320,    0}};
    private static int[][] tWin = new int[][]
            {{21600, 86400},{28800, 86400},{21600, 46800},{28800, 86400},{28800, 86400},{50400, 72000},{28800, 86400}};
//sumV_Kg = 22800  sumKg  = 19109

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
