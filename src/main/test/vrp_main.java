import org.chocosolver.solver.ResolutionPolicy;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.ICF;
import org.chocosolver.solver.constraints.IntConstraintFactory;
import org.chocosolver.solver.trace.Chatterbox;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VF;
import org.chocosolver.solver.variables.VariableFactory;
import org.chocosolver.util.tools.ArrayUtils;


public class vrp_main {

    /************* C O N S T A N T S **************/

    private int nbCustomers = 5         /* number of customers/vertices  */
            + 1;                        /*  + depot client 0
                                        Customers: N = {1, 2,.., n}, 'n' different locations
                                        Every pair of locations (i, j) , where i, j ∈ N and i ≠ j,
                                        is associated with a travel time t[i][j] and a distance traveled d[i][j]  */

    private int[] qty = new int[]       /* q[i] is the demand at point 'i'. Depot is denoted by 0. */
            {30, 20, 50, 10, 40};

    private int nbVehicles = 2;         /* total vehicle fleet
                                        Fleet of vehicles: V = {1,...,m} with identical capacities.  */
    private int[] vCap = new int[]          /*a[k] capacity of each vehicle.  */
            {50, 100};

    private int max_cap_big = 1000;       /* Max vehicle capacity: Biggest truck (auxiliary variable) */
    private int max_cap = 100;             /* Max vehicle capacity: Biggest truck  */
                                            /* Homogeneous and limited fleet that leave and return to the depot
                                            R[i] = {r[i](1),...,r[i](n[i])} denote the route for vehicle 'i',
                                            where r[i](j) is the index of the jth customer visited and n[i] is the nb. of customers in the route.
                                            r[i](n[i] + 1) = 0, every route finishes at the depot  */
    private int vclCapacity = 100;          // a[k] is total vehicle capacity, of each vehicle k ∈ V

                                    /* Split deliveries: the demand of a customer may be fulfilled by more than one vehicle.
                                        This occurs in all cases where some demand exceeds the vehicle capacity */

    private int[][] costs = new int[][]     /* costs in distance ALL to ALL customers */        //consumptions
            {   {0, 2223, 1272, 1931, 2047, 1597},
                    {2211, 0, 3226, 1202, 1138, 1755},
                    {1291, 3243, 0, 3077, 3190, 2740},
                    {1921, 1207, 3053, 0, 737, 1075},
                    {1981, 1083, 3117, 674, 0, 1132},
                    {1580, 1741, 2716, 1052, 1167, 0}};

    private int servTime = 10;                  /* time needed at the customer  */
    private int[][] travTime = new int[][]      /* costs in time ALL to ALL customers */
                         /*0  1  2  3  4  5*/
            {       /*0*/ {0, 2, 3, 4, 5, 6},
                    /*1*/ {2, 0, 2, 3, 4, 5},
                    /*2*/ {3, 2, 0, 2, 3, 4},
                    /*3*/ {4, 3, 2, 0, 2, 3},
                    /*4*/ {5, 4, 3, 2, 0, 2},
                    /*5*/ {6, 5, 4, 3, 2, 0}};

    private int[][] tWin = new int[][]          /* time windows for each client [earliest_i, latest_j]
    /*0*/{{0, 500}, /*1*/  {10, 500}, /*2*/  {11, 500}, /*3*/  {12, 500}, /*4*/  {13, 500}, /*5*/  {14, 500}};

    private int[][] M_ij;                       /* Constant for equation 6: M_ij = l_i + t_i - e_j    */

    /************* D E C I S I O N   V A R I A B L E S **************/

    private IntVar globalCost;
    private IntVar[] totalVehicleCosts;
    private IntVar[][] flowsEdges;
    private IntVar[] capacityUsed;
    private IntVar[][] serve;
    private BoolVar[][][] edges;
    private IntVar[][] servStart;           /* when service begins for each customer [i][k]*/
    private IntVar[][][] edgesM_ij;         /* auxiliary variable with the product edge * M_ij  */


    /************* P R I N T   T H E   I N P U T   D A T A  **************/
    public void printInput(Solver solver) {

        System.out.println("nbCustomers = " + (nbCustomers - 1) + ";");
        System.out.println("nbVehicles = " + nbVehicles + ";");
        System.out.println("serviceTime = " + servTime + ";");
        System.out.print("vCapacity => ");
        for (int i = 0; i < nbVehicles; i++)
            System.out.print(" c" + i + " : " + vCap[i] + "; ");
        System.out.print("\nQtyRequired => ");
        for (int i = 0; i < nbCustomers-1; i++)
            System.out.print(" c" + (i+1) + " : " + qty[i] + "; ");
        String costD = "\n\n====== Cost Distance ======\n_|_0______1______2______3______4______5__\n";
        for (int i = 0; i < nbCustomers; i++) {
            String s = "";
            for (int j = 0; j < nbCustomers - 1; j++) {
                if(costs[i][j]==0) {
                    s += "0      ";
                } else {
                    s += costs[i][j] + "    ";}
            }
            costD +=i+"| "+ s + costs[i][nbCustomers - 1]+"\n";
        } System.out.println(costD);

        String costT = "====== Cost Time ======\n_|_0____1____2____3____4____5__\n";
        for (int i = 0; i < nbCustomers; i++) {
            String s = "";
            for (int j = 0; j < nbCustomers - 1; j++) {
                s += travTime[i][j] + "    ";
            }
            costT +=i+"| "+ s + travTime[i][nbCustomers - 1]+"\n";
        } System.out.println(costT);

        System.out.print("====== Time Windows ======\n________|_e__l_\n");
        for (int i = 0; i < nbCustomers; i++) {
            System.out.print("client"+i+" | ");
            for (int tw = 0; tw < 2; tw++) {
                System.out.print(tWin[i][tw] + " ");
            }System.out.print("\n");
        }System.out.print("\n");

        /** String mij = "====== Constant M[i][j] ======\n__|__0______1______2______3______4______5__\n";
         for (int i = 0; i < nbCustomers; i++) {
         mij += i+" | ";
         for (int j = 0; j < nbCustomers; j++) {
         mij +=M_ij[i][j] + "    ";}
         mij +="\n";
         } System.out.println(mij);*/
    }


    /************** F I N D   S O L U T I O N **************/

    public void findSol(Solver solver) {
        /*if(solver.findSolution()){     //print all solutions
            do{
                System.out.println(solver.getSolutionRecorder().getLastSolution().toString(solver));
            }while(solver.nextSolution());
        }*/

        //int numFailures = 200000;
        //SearchMonitorFactory.limitFail(solver, numFailures);

        //solver.findOptimalSolution(ResolutionPolicy.MINIMIZE, globalCost); /* OBJECTIVE FUNCTION * Minimize the total cost of the route */
        //long nbSol = solver.getMeasures().getSolutionCount();

        //solver.findAllSolutions();

        		/* Heuristic choices */
        /**  AbstractStrategy strat = IntStrategyFactory.minDom_LB(serve);
         solver.set(IntStrategyFactory.lastConflict(solver,strat));
         //		solver.set(strat);    */

        //   LNSFactory.rlns(solver, aux3, 30, 20140909L, new FailCounter(solver, 100));
        solver.findOptimalSolution(ResolutionPolicy.MINIMIZE, globalCost);


        //solver.findSolution();

        Chatterbox.printStatistics(solver);
        Chatterbox.showSolutions(solver);
        // System.out.println(solver.getSolutionRecorder().getLastSolution().toString(solver));
    }

    /************** P R I N T   T H E   O U T P U T   D A T A  **************/
    public void printOutput(Solver solver) {

        System.out.print("\n====== Demand per customer ======\n");
        int sum2 = 0;
        for (int i = 0; i < nbCustomers-1; i++) {
            System.out.print(" | client "+i+"="+qty[i]);
            sum2 = sum2 + qty[i];
        }System.out.print(" ===> "+sum2+"\n");

        for (int k = 0; k < nbVehicles; k++) {
            System.out.print("\n=== "+ capacityUsed[k]+ " from "+ vCap[k]+"\n" );
            int sum = 0;
            for (int i = 0; i < nbCustomers; i++) {     //2
                for (int j = 0; j < nbCustomers; j++)
                    if (edges[k][i][j].contains(1)){
                        System.out.print(edges[k][i][j] + " cost(time) = " + costs[i][j]);
                        sum = sum + costs[i][j];
                        System.out.print("\n"); }
            }System.out.print("manual sum = "+sum+"\n");
        }
        for (int k = 0; k < nbVehicles; k++){
            System.out.print(totalVehicleCosts[k]+"  ");
        }
        System.out.print(" "+globalCost+"\n");

        System.out.print("====== Serve ======\n");
        System.out.print("_|0__1\n");
        for (int i = 0; i < nbCustomers; i++) {
            System.out.print(i+"|");
            for (int k = 0; k < nbVehicles; k++) {
                if(serve[i][k].getValue()==0){
                    System.out.print("□ ");
                }else {
                    System.out.print("■ ");
                }
            }System.out.print("\n");
        }

        System.out.print("\n====== Edges ======\n");
        for (int k = 0; k < nbVehicles; k++) {
            System.out.print(k+"|0　1　2 3 4　5 \n");
            for (int i = 0; i < nbCustomers; i++) {
                System.out.print(i+"|");
                for (int j = 0; j < nbCustomers; j++)
                    if(edges[k][i][j].getValue()==0){
                        System.out.print("□ ");
                    }else {
                        System.out.print("■ ");
                    }
                System.out.print("\n");
            }System.out.print("\n");
        }
    }

    /************** C O N S T R A I N T S  **************/
    public void constraints(Solver solver) {

        /* represents all paths all vehicles
        for each vehicle, 1 boolean matrix. x[k][i][j] True if vehicle vclK visits the client 'j' directly after 'i', False otherwise */
        edges = new BoolVar[nbVehicles][nbCustomers][nbCustomers];
        for (int k = 0; k < nbVehicles; k++)
            edges[k] = VariableFactory.boolMatrix("truck_" + k, nbCustomers, nbCustomers, solver);

        /* Calculate total costs for each vehicle: The scalar constraint to compute global cost performed in all paths */
        totalVehicleCosts = VF.enumeratedArray("cost", nbVehicles, 0, 99999, solver);
        for (int k = 0; k < nbVehicles; k++)
            solver.post(ICF.scalar(ArrayUtils.flatten(edges[k]),ArrayUtils.flatten(costs),totalVehicleCosts[k]));
        globalCost = VF.enumerated("global_cost", 0, 999999, solver);
        solver.post(IntConstraintFactory.sum(totalVehicleCosts, globalCost)); // compute global cost

        /** Constraint (1): each vehicle will leave the depot and arrive at a determined customer */
        for (int k = 0; k < nbVehicles; k++)
            solver.post(ICF.sum(ArrayUtils.flattenSubMatrix(0, 1, 1, nbCustomers-1, edges[k]), "=", VariableFactory.fixed(1, solver)));


         /** Constraint (2): entrance and exit flows, guarantees that each vehicle will leave a customer and arrive to the depot*/

        flowsEdges = new IntVar[nbVehicles][nbCustomers];
        //     for (int k = 0; k < nbVehicles; k++)
        //     flowsEdges[k] = VariableFactory.enumeratedArray("flowsEdges" + k, nbCustomers, 0, nbCustomers, solver);

        //IntVar[] aux1 = VariableFactory.enumeratedArray("aux", nbCustomers, 0, nbCustomers, solver);

        for (int k = 0; k < nbVehicles; k++)
            flowsEdges[k] = VariableFactory.enumeratedArray("flowsEdges" + k, nbCustomers, 0, nbCustomers, solver);


            for (int p = 0; p < nbCustomers; p++) {
                for (int k = 0; k < nbVehicles; k++) {
                solver.post(ICF.sum(ArrayUtils.flattenSubMatrix(0, nbCustomers, p, 1, edges[0]), "=",  flowsEdges[k][p]));
                solver.post(ICF.sum(ArrayUtils.flattenSubMatrix(p, 1, 0, nbCustomers, edges[0]), "=",  flowsEdges[k][p]));
                solver.post(ICF.arithm(edges[k][p][p],"=", VariableFactory.fixed(false, solver)));   // Edge from 'i' to 'j' when 'i'='j' is equal to 0
            }
        }

        /** Constraint (3): the total demand of each customer will be fulfilled */
            /* true if client 'i' is served by vehicle vclK, false otherwise */
        serve = VF.enumeratedMatrix("serve", nbCustomers, nbVehicles, 0, 1, solver);
        solver.post(ICF.sum(ArrayUtils.flattenSubMatrix(0, 1, 0, nbVehicles, serve), "=", VariableFactory.fixed(nbVehicles, solver))); // node 0 is not served by any vehicle
        for (int i = 1; i < nbCustomers; i++) {
            solver.post(ICF.sum(ArrayUtils.flattenSubMatrix(i, 1, 0, nbVehicles, serve), "=", VariableFactory.fixed(1, solver)));
        }

        /** Constraint (4): the vehicle capacity will not be exceed */
        /* The scalar constraint to compute global consumption of the kart to perform the path */
        // qty = {5, 2, 5, 10, 8};
        // vCap = {50, 100};
        capacityUsed = new IntVar[nbVehicles];
        for (int k = 0; k < nbVehicles; k++) {
            capacityUsed[k] = VariableFactory.enumerated("vehicle" + k + " capacityUsed", 0, vCap[k], solver);
            solver.post(ICF.scalar(ArrayUtils.flattenSubMatrix(1, nbCustomers-1, k, 1, serve), qty, "<=", capacityUsed[k]));
        }

        /** Constraint (5): the demand of each customer will only be fulfilled if a determined vehicle goes by that place */
        //(We can notice that, adding to constraint (5) the sum of all vehicles and combining to equation (3) we have the constraint ____
        //which guarantees that each vertex will be visited at least once by at least one vehicle)
        for (int i = 0; i < nbCustomers; i++) {
            for (int k = 0; k < nbVehicles; k++){
                solver.post(ICF.sum(ArrayUtils.flattenSubMatrix(0, nbCustomers, i, 1, edges[k]), ">=", serve[i][k]));
            }
        }

        /** Equation (6):minimum time for beginning the service of customer j in a determined route
         // also guarantees that there will be no sub tours. The constant M_ij  is a large enough number

         M_ij = new int[nbCustomers][nbCustomers];
         for (int i = 0; i < nbCustomers; i++)
         for (int j = 0; j < nbCustomers; j++)
         M_ij[i][j] = tWin[i][1] + travTime[i][j] - tWin[j][0];

         servStart = VF.enumeratedMatrix("servStart", nbCustomers, nbVehicles, 0, 1000, solver);
         edgesM_ij = new IntVar[nbVehicles][nbCustomers][nbCustomers];
         for (int k = 0; k < nbVehicles; k++)
         edgesM_ij[k] = VF.enumeratedMatrix("edgeM_ij" + k, nbCustomers, nbCustomers, 0, 1000, solver);

         for (int k = 0; k < nbVehicles; k++) {
         for (int i = 0; i < nbCustomers; i++) {
         for (int j = 0; j < nbCustomers; j++) {
         int stM_ij = servTime + travTime[i][j] - M_ij[i][j];                   // s_i + t_ij - M_ij (servTime[i] when you have different values per client)
         solver.post(ICF.times(edges[k][i][j], M_ij[i][j], edgesM_ij[k][i][j]));  //  edges*Mij
         solver.post(ICF.sum(new IntVar[]{VF.fixed(stM_ij,solver),servStart[i][k],edgesM_ij[k][i][j]}, "<=", servStart[j][k]));
         }
         }
         }*/

        /** Constraint(7): all customers will be served within their time windows
         for (int k = 0; k < nbVehicles; k++) {
         for (int i = 0; i < nbCustomers; i++) {
         solver.post(ICF.arithm(servStart[i][k],">=",tWin[i][0]));
         solver.post(ICF.arithm(servStart[i][k],"<=",tWin[i][1]));
         }
         }*/

        // Equation (8): the decision variables y[k][i] and b[k][i] are positive.
        /**done in declaration*/

        // Equation (9): the decision variables x[k][i][j] is binary.
        /**done in declaration*/
    }

    public static void main(String [] arg){

        // 1. Create a Solver
        Solver solver = new Solver();
        vrp_main vrp = new vrp_main();

        // 2. Constraints
        vrp.constraints(solver);

        // 3. Print Input
        vrp.printInput(solver);

        // 4. Find Solution
        vrp.findSol(solver);

        // 5. Print Output
        vrp.printOutput(solver);

    }
}
