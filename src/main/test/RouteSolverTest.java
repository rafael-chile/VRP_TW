//github pass123
import com.vrptw.constraints.RouteSolver;
import org.chocosolver.solver.Solver;


public class RouteSolverTest {

    /************* C O N S T A N T S **************/

    /************* 16 vehicles 10 Clients **************/

    private static int[] qty = new int[]       /* q[i] is the demand at point 'i'. Depot is denoted by 0. */
            {0, 5, 20, 50, 10, 40, 30, 100, 50, 10, 40, 100};

    private static int nbVehicles = 1;         /* total vehicle fleet
                                        Fleet of vehicles: V = {1,...,m} with identical capacities.  */
    private static int[] vCap = new int[]          /*a[k] capacity of each vehicle.  */
           // {27000, 7500, 15000, 15000, 1150, 400, 600, 600, 600, 600, 400, 1000, 600, 400, 400, 10000};

            {27000};


    private static int nbCustomers = 11;  /* NOT including depot client 0
                                          /* Customers: N = {1, 2,.., n}, 'n' different locations
                                            Every pair of locations (i, j) , where i, j ∈ N and i ≠ j,
                                            is associated with a travel time t[i][j] and a distance traveled d[i][j]  */

    private static int[][] costs = new int[][]     /* costs in distance ALL to ALL customers */        //consumptions

    {{0,462,282,635,227,180,468,331,713,277,653,31},
        {0,0,988,378,195,305,693,374,415,228,499,835},
        {282,988,0,612,172,774,705,791,959,187,742,721},
        {635,378,612,0,973,604,520,339,118,556,335,479},
        {227,195,172,973,0,130,525,348,726,572,235,272},
        {180,305,774,604,130,0,432,787,259,423,865,134},
        {468,693,705,520,525,432,0,515,581,425,234,483},
        {331,374,791,339,348,787,515,0,186,46,613,886,},
        {713,415,959,118,726,259,581,186,0,300,783,322},
        {277,228,187,556,572,423,425,46,300,0,403,784,},
        {653,499,742,335,235,865,234,613,783,403,0,107},
        {31,835,721,479,272,134,483,886,322,784,107,0}};


                   /* With respect to Time Window  */

    private static int servTime = 15;                  /* time needed at the customer  */

    private static int[][] travTime = new int[][]      /* costs in time ALL to ALL customers */
                  /*0 1  2  3  4  5  6  7   8 9  10 11 12 13 14*/
            { /*0*/ {0,46,22,65,27,10,48,31,73,27,53,31},
                    {10,0,98,38,95,25,63,74,15,28,49,83},
                    {22,98,0,12,72,77,70,71,99,87,72,71},
                    {65,38,62,0,93,64,50,39,18,56,35,47},
                    {27,95,72,73,0,10,25,48,76,52,25,72},
                    {10,35,74,04,10,0,42,77,29,43,85,14},
                    {46,93,75,20,25,42,0,55,51,45,24,43},
                    {31,74,91,39,48,77,55,0,86,4,61,86},
                    {73,45,59,18,76,29,51,16,0,30,73,32},
                    {27,28,87,56,52,23,45,46,30,0,43,74},
                    {63,49,72,35,25,85,24,63,73,43,0,10},
                    {3,83,71,79,22,14,83,86,32,74,17,0}};

    private static int[][] tWin = new int[][]          /* time windows for each client [earliest_i, latest_j]  */

            { /*0*/  {0, 500}, /*1*/  {10, 400}, /*2*/  {10, 500}, /*3*/  {40, 500}, /*4*/  {25, 400}, /*5*/  {150, 300},
    /*6*/  {0, 500}, /*7*/  {10, 400}, /*8*/  {10, 500}, /*9*/  {40, 500}, /*10*/ {25, 400}, /*11*/  {150, 300}
            };

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
        return new RouteSolver(nbCustomers, qty, nbVehicles, vCap, max_cap_big,  max_cap, vclCapacity,
                costs, servTime, travTime, tWin, M_ij, stM_ij);
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
        stM_ij = ;
        */
        return new RouteSolver(nbCustomers, qty, nbVehicles, vCap, max_cap_big,  max_cap, vclCapacity,
                costs, servTime, travTime, tWin, M_ij, stM_ij);
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
