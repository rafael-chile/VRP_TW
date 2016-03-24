import org.chocosolver.solver.ResolutionPolicy;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.ICF;
import org.chocosolver.solver.constraints.IntConstraintFactory;
import org.chocosolver.solver.search.loop.monitors.SearchMonitorFactory;
import org.chocosolver.solver.trace.Chatterbox;
import org.chocosolver.solver.variables.*;
import org.chocosolver.util.tools.ArrayUtils;


public class test {


    private int nbCustomers = 5         /* number of customers/vertices  */
            + 1;                        /*  + depot client 0
                                        Customers: N = {1, 2,.., n}, 'n' different locations
                                        Every pair of locations (i, j) , where i, j ∈ N and i ≠ j,
                                        is associated with a travel time t[i][j] and a distance traveled d[i][j]  */

    private int[] qty = new int[]       /* q[i] is the demand at point 'i'. Depot is denoted by 0. */
            {0, 30, 20, 50, 10, 40};

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

    private int[][] costs = new int[][]     /* costs in time ALL to ALL customers */        //consumptions
            {   {0, 2223, 1272, 1931, 2047, 1597},
                {2211, 0, 3226, 1202, 1138, 1755},
                {1291, 3243, 0, 3077, 3190, 2740},
                {1921, 1207, 3053, 0, 737, 1075},
                {1981, 1083, 3117, 674, 0, 1132},
                {1580, 1741, 2716, 1052, 1167, 0}};


    private IntVar globalCost;
    private IntVar[] totalVehicleCosts;
    private IntVar[] capacityUsed;
    private IntVar[][] serve;
    private IntVar[][][] aux2;
    private BoolVar[][][] edges;

    public void printSolver(Solver solver) {

        /*if(solver.findSolution()){
            do{
                System.out.println(solver.getSolutionRecorder().getLastSolution().toString(solver));
            }while(solver.nextSolution());
        }*/
        //solver.findSolution();
        /*int numFailures = 200000;
        SearchMonitorFactory.limitFail(solver, numFailures);
        solver.findOptimalSolution(ResolutionPolicy.MINIMIZE, globalCost);*/
        solver.findSolution();
        //long nbSol = solver.getMeasures().getSolutionCount();

        Chatterbox.printStatistics(solver);
//        System.out.println(solver.getSolutionRecorder().getLastSolution().toString(solver));


        System.out.print("\n====== Edges ======\n");
        for (int k = 0; k < nbVehicles; k++) {
            System.out.print(k+"|0_1_2_3_4_5 \n");
            for (int i = 0; i < nbCustomers; i++) {
                System.out.print(i+"|");
                for (int j = 0; j < nbCustomers; j++)
                    System.out.print(edges[k][i][j].getValue() + " ");
                System.out.print("\n");
            }System.out.print("\n");
        }


        System.out.print("====== Serve ======\n");
        System.out.print("_|0_1\n");
        for (int i = 0; i < nbCustomers; i++) {
            System.out.print(i+"|");
            for (int k = 0; k < nbVehicles; k++) {
                System.out.print(serve[i][k].getValue() + " ");
            }System.out.print("\n");
        }

      /*for (int j = 0; j < aux2.length; j++) {
            System.out.print("Aux "+j+" = \n");
            for (int k = 0; k < aux2[j].length; k++) {     //2
                for (int i = 0; i < aux2[j][0].length; i++) {
                    System.out.print(aux2[j][k][i] + " ");
                }
                System.out.print("\n");
            }
        }*/

        System.out.print("\n====== Demand per customer ======\n");
        int sum2 = 0;
        for (int i = 1; i < nbCustomers; i++) {
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
    }

    public void test_sum(Solver solver) {
        IntVar[] VS = VariableFactory.enumeratedArray("VS", 4, new int[]{1, 3, 5, 7, 11, 13, 15}, solver);
        solver.post(ICF.sum(VS, "=", VariableFactory.fixed(30, solver)));
    }

    public void const1_test(Solver solver) {
        int nbCustomers = 5;
        IntVar[][] edges;
        edges = VariableFactory.enumeratedMatrix("edges", nbCustomers, nbCustomers, new int[]{0,1}, solver);
        solver.post(ICF.sum(ArrayUtils.flattenSubMatrix(0, 1, 1, nbCustomers-1, edges), "=", VariableFactory.fixed(1, solver)));
        //starts in [3,0] and the length in x=1 and in y=4
        solver.post(ICF.sum(ArrayUtils.flattenSubMatrix(3, 1, 0, nbCustomers, edges), "=", VariableFactory.fixed(4, solver)));
    }

    public void const1(Solver solver) {

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

        /** 4. C O N S T R A I N T S   */
        // Constraint (1): each vehicle will leave the depot and arrive at a determined customer
        for (int k = 0; k < nbVehicles; k++)
            solver.post(ICF.sum(ArrayUtils.flattenSubMatrix(0, 1, 1, nbCustomers - 1, edges[k]), "=", VariableFactory.fixed(1, solver)));

        // Constraint (2.1): entrance and exit flows, guarantees that each vehicle will leave a customer and arrive to the depot
        IntVar[] aux1 = VariableFactory.enumeratedArray("aux", nbCustomers, 0, nbCustomers, solver);
        for (int k = 0; k < nbVehicles; k++) {
            for (int p = 0; p < nbCustomers; p++) {
                solver.post(ICF.sum(ArrayUtils.flattenSubMatrix(0, nbCustomers, p, 1, edges[k]), "=", aux1[p]));
                solver.post(ICF.sum(ArrayUtils.flattenSubMatrix(p, 1, 0, nbCustomers, edges[k]), "=", aux1[p]));
                solver.post(ICF.arithm(edges[k][p][p],"=", VariableFactory.fixed(false, solver)));   // Edge from 'i' to 'j' when 'i'='j' is equal to 0
            }
        }

        // Constraint (3): the total demand of each customer will be fulfilled
            /* true if client 'i' is served by vehicle vclK, false otherwise */
        serve = VF.enumeratedMatrix("serve", nbCustomers, nbVehicles, 0, 1, solver);
        for (int i = 0; i < nbCustomers; i++) {
            solver.post(ICF.sum(ArrayUtils.flattenSubMatrix(i, 1, 0, nbVehicles, serve), "=", VariableFactory.fixed(1, solver)));
        }

        //Constraint (4): the vehicle capacity will not be exceed
        /* The scalar constraint to compute global consumption of the kart to perform the path   */
        // qty = {5, 2, 5, 10, 8};
        // vCap = {50, 100};
        capacityUsed = new IntVar[nbVehicles];
        for (int k = 0; k < nbVehicles; k++) {
            capacityUsed[k] = VariableFactory.enumerated("vehicle" + k + " capacityUsed", 0, vCap[k], solver);
            solver.post(ICF.scalar(ArrayUtils.flattenSubMatrix(0, nbCustomers, k, 1, serve), qty, "<=", capacityUsed[k]));
        }

        //Constraint (5): the demand of each customer will only be fulfilled if a determined vehicle goes by that place
        //(We can notice that, adding to constraint (5) the sum of all vehicles and combining to equation (3) we have the constraint ____
        //which guarantees that each vertex will be visited at least once by at least one vehicle)

        for (int i = 0; i < nbCustomers; i++) {
            for (int k = 0; k < nbVehicles; k++) {
                solver.post(ICF.sum(ArrayUtils.flattenSubMatrix(0, nbCustomers, i, 1, edges[k]), ">=", serve[i][k]));
            }
        }

        aux2 = new IntVar[nbCustomers][nbVehicles][nbCustomers];    //  one matrix for each visited client j with all vehicles
        for (int i = 0; i < nbCustomers; i++) {
            for (int k = 0; k < nbVehicles; k++) {
                aux2[i][k] = ArrayUtils.flattenSubMatrix(0, nbCustomers, i, 1, edges[k]);
            }
        }
        for (int j = 0; j < nbCustomers; j++) {
                solver.post(ICF.sum(ArrayUtils.flatten(aux2[j]), ">=", VariableFactory.fixed(1, solver)));
        }

        // Equation (6):minimum time for beginning the service of customer j in a determined route
        // also guarantees that there will be no sub tours. The constant M_ij  is a large enough number

        int servTime = 10;
        int[][] travTime = new int[][]     /* costs in time ALL to ALL customers */        //consumptions
                         /*0  1  2  3  4  5*/
                {   /*0*/ {0, 2, 2, 2, 2, 2},
                    /*1*/ {2, 0, 2, 2, 2, 2},
                    /*2*/ {2, 2, 0, 2, 2, 2},
                    /*3*/ {2, 2, 2, 0, 2, 2},
                    /*4*/ {2, 2, 2, 2, 0, 2},
                    /*5*/ {2, 2, 2, 2, 2, 0}};

        int[][] M_ij = new int[nbCustomers][nbCustomers];

        int[][] tWin = new int[][] // [earliest_i, latest_j]
                /*0*/ {{0, 500}, /*1*/  {0, 500}, /*2*/  {0, 500}, /*3*/  {0, 500}, /*4*/  {0, 500}, /*5*/  {0, 500}};

        IntVar[][] servStart;
        servStart = VF.enumeratedMatrix("servStart", nbCustomers, nbVehicles, 0, 1000, solver);
        IntVar[][][] edgesM_ij = new IntVar[nbVehicles][nbCustomers][nbCustomers];
        for (int k = 0; k < nbVehicles; k++)
            edgesM_ij[k] = VF.enumeratedMatrix("edgeM_ij" + k, nbCustomers, nbCustomers, 0, 1000, solver);

        for (int k = 0; k < nbVehicles; k++) {
            for (int i = 0; i < nbCustomers; i++) {
                for (int j = 0; j < nbCustomers; j++) {
                    M_ij[i][j] = tWin[i][1] + travTime[i][j] - tWin[j][0];
                    int stM_ij = servTime + travTime[i][j] - M_ij[i][j];                   // s_i + t_ij - M_ij (servTime[i] when you have different values per client)
                    solver.post(ICF.times(edges[k][i][j], M_ij[i][j], edgesM_ij[k][i][j]));  //  edges*Mij
                    solver.post(ICF.sum(new IntVar[]{VF.fixed(stM_ij,solver),servStart[i][k],edgesM_ij[k][i][j]}, "<=", servStart[j][k]));
                }
            }
        }
        // Constraint(7): all customers will be served within their time windows
        for (int k = 0; k < nbVehicles; k++) {
            for (int i = 0; i < nbCustomers; i++) {
                solver.post(ICF.arithm(servStart[i][k],">=",tWin[i][0]));
                solver.post(ICF.arithm(servStart[i][k],"<=",tWin[i][1]));
            }
        }

        // Equation (8): the decision variables y[k][i] and b[k][i] are positive.
                /**done in declaration*/

        // Equation (9): the decision variables x[k][i][j] is binary.
                /**done in declaration*/


    }

    public static void main(String [] arg){
        // 1. Create a Solver
        Solver solver = new Solver();

        // 1. Test
        test t = new test();
        t.const1(solver);

        // 1. Execute Solver
        t.printSolver(solver);

    }


}
