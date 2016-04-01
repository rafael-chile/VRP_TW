import com.vrptw.constraints.RouteSolver;
import org.chocosolver.solver.Solver;


public class RouteSolverTest {

    /************* C O N S T A N T S **************/

    private static int[] qty = new int[]       /* q[i] is the demand at point 'i'. Depot is denoted by 0. */
            {0, 5, 20, 50, 10, 40, 30, 100, 50, 10, 40, 30, 20, 50, 10};

    private static int nbVehicles = 3;         /* total vehicle fleet
                                        Fleet of vehicles: V = {1,...,m} with identical capacities.  */
    private static int[] vCap = new int[]          /*a[k] capacity of each vehicle.  */
            {60, 100, 1100};


    private static int nbCustomers = 14;  /* NOT including depot client 0
                                          /* Customers: N = {1, 2,.., n}, 'n' different locations
                                            Every pair of locations (i, j) , where i, j ∈ N and i ≠ j,
                                            is associated with a travel time t[i][j] and a distance traveled d[i][j]  */

    private static int[][] costs = new int[][]     /* costs in distance ALL to ALL customers */        //consumptions

            {{0,462,282,635,227,180,468,331,713,277,653,31,812,870,214},
                    {0,0,988,378,195,305,693,374,415,228,499,835,853,771,446},
                    {282,988,0,612,172,774,705,791,959,187,742,721,543,395,613},
                    {635,378,612,0,973,604,520,339,118,556,335,479,967,599,67},
                    {227,195,172,973,0,130,525,348,726,572,235,272,218,145,525},
                    {180,305,774,604,130,0,432,787,259,423,865,134,561,297,526},
                    {468,693,705,520,525,432,0,515,581,425,234,483,846,364,455},
                    {331,374,791,339,348,787,515,0,186,46,613,886,549,411,159},
                    {713,415,959,118,726,259,581,186,0,300,783,322,548,39,282},
                    {277,228,187,556,572,423,425,46,300,0,403,784,40,161,398},
                    {653,499,742,335,235,865,234,613,783,403,0,107,22,766,3},
                    {31,835,721,479,272,134,483,886,322,784,107,0,679,209,416},
                    {812,853,543,967,218,561,846,549,548,40,22,679,0,824,985},
                    {870,771,395,599,145,297,364,411,39,161,766,209,824,0,255},
                    {214,446,613,67,525,526,455,159,282,398,3,416,985,255,0}};



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

private static int servTime = 10;                  /* time needed at the customer  */
    private static int[][] travTime = new int[][]      /* costs in time ALL to ALL customers */
                         /*0  1  2  3  4  5*/
            {       /*0*/ {0, 2, 3, 4, 5, 6},
                    /*1*/ {2, 0, 2, 3, 4, 5},
                    /*2*/ {3, 2, 0, 2, 3, 4},
                    /*3*/ {4, 3, 2, 0, 2, 3},
                    /*4*/ {5, 4, 3, 2, 0, 2},
                    /*5*/ {6, 5, 4, 3, 2, 0}};

    private static int[][] tWin = new int[][]          /* time windows for each client [earliest_i, latest_j]
    /*0*/{{0, 500}, /*1*/  {10, 500}, /*2*/  {11, 500}, /*3*/  {12, 500}, /*4*/  {13, 500}, /*5*/  {14, 500}};

    private static int[][] M_ij;                       /* Constant for equation 6: M_ij = l_i + t_i - e_j    */


    private static RouteSolver testWithLocalTestData(){
        return new RouteSolver(nbCustomers, qty, nbVehicles, vCap, max_cap_big,  max_cap, vclCapacity,
                costs, servTime, travTime, tWin, M_ij);
    }

    private static RouteSolver testWithDBTestData(){
        // TODO: get data from db and set it into the parameters; then do : return new RouteSolver(...);
        /*
        RouteCostDao rcDao = new RouteCostDao();
        rcDao.getDistanceCostMatrix(...);
        nbCustomers = ;
        qty = ;
        nbVehicles = ;
        vCap = ;
        max_cap_big = ;
        max_cap = ;
        vclCapacity = ;
        costs = ;
        servTime = ;
        travTime = ;
        tWin = ;
        M_ij = ;
        */
        return new RouteSolver(nbCustomers, qty, nbVehicles, vCap, max_cap_big,  max_cap, vclCapacity,
                costs, servTime, travTime, tWin, M_ij);
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
