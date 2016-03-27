import com.vrptw.constraints.RouteSolver;
import org.chocosolver.solver.Solver;


public class RouteSolverTest {

    /************* C O N S T A N T S **************/

    private static int nbCustomers = 5;         /* NOT including depot client 0
                                        Customers: N = {1, 2,.., n}, 'n' different locations
                                        Every pair of locations (i, j) , where i, j ∈ N and i ≠ j,
                                        is associated with a travel time t[i][j] and a distance traveled d[i][j]  */

    private static int[] qty = new int[]       /* q[i] is the demand at point 'i'. Depot is denoted by 0. */
            {30, 20, 50, 10, 40};

    private static int nbVehicles = 2;         /* total vehicle fleet
                                        Fleet of vehicles: V = {1,...,m} with identical capacities.  */
    private static int[] vCap = new int[]          /*a[k] capacity of each vehicle.  */
            {50, 100};

    private static int max_cap_big = 1000;       /* Max vehicle capacity: Biggest truck (auxiliary variable) */
    private static int max_cap = 100;             /* Max vehicle capacity: Biggest truck  */
                                            /* Homogeneous and limited fleet that leave and return to the depot
                                            R[i] = {r[i](1),...,r[i](n[i])} denote the route for vehicle 'i',
                                            where r[i](j) is the index of the jth customer visited and n[i] is the nb. of customers in the route.
                                            r[i](n[i] + 1) = 0, every route finishes at the depot  */
    private static int vclCapacity = 100;          // a[k] is total vehicle capacity, of each vehicle k ∈ V

                                    /* Split deliveries: the demand of a customer may be fulfilled by more than one vehicle.
                                        This occurs in all cases where some demand exceeds the vehicle capacity */

    private static int[][] costs = new int[][]     /* costs in distance ALL to ALL customers */        //consumptions
            {   {0, 2223, 1272, 1931, 2047, 1597},
                    {2211, 0, 3226, 1202, 1138, 1755},
                    {1291, 3243, 0, 3077, 3190, 2740},
                    {1921, 1207, 3053, 0, 737, 1075},
                    {1981, 1083, 3117, 674, 0, 1132},
                    {1580, 1741, 2716, 1052, 1167, 0}};

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
