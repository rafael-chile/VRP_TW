package com.vrptw.constraints;

import org.chocosolver.solver.ResolutionPolicy;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.constraints.ICF;
import org.chocosolver.solver.constraints.IntConstraintFactory;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.search.loop.monitors.SearchMonitorFactory;
import org.chocosolver.solver.search.strategy.IntStrategyFactory;
import org.chocosolver.solver.search.strategy.strategy.AbstractStrategy;
import org.chocosolver.solver.trace.Chatterbox;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VF;
import org.chocosolver.solver.variables.VariableFactory;
import org.chocosolver.util.tools.ArrayUtils;

public class RouteSolver {

    /************* C L A S S  P A R A M E T E R S **************/

    private int nbCustomers;
    private int[] qty;
    private int nbVehicles;
    private int[] vCap;
    private int max_cap_big;
    private int max_cap;
    private int vclCapacity;
    private int[][] costs;
    private int servTime;
    private int[][] travTime;
    private int[][] tWin;
    private int[][] M_ij;
    private int[][] stM_ij;

    /************* D E C I S I O N   V A R I A B L E S **************/

    private IntVar globalCost;
    private IntVar[] totalVehicleDistance;
    private IntVar[][] flowsEdges;
    private IntVar[] capacityUsed;
    private IntVar[][] serve;
    private BoolVar[][][] edges;
    private BoolVar[][][] aux_edge;
    private IntVar[][] next;
    private IntVar[] size;
    private IntVar[][] servStart;           /* when service begins for each customer [i][k]*/
    private IntVar[][][] edgesM_ij;         /* auxiliary variable with the product edge * M_ij  */
    private IntVar x1;
    private IntVar y1;


    /** C O N S T R U C T O R */
    public RouteSolver(int nbCustomers, int[] qty, int nbVehicles, int[] vCap, int max_cap_big, int max_cap, int vclCapacity,
                int[][] costs, int servTime, int[][] travTime, int[][] tWin, int[][] M_ij, int[][] stM_ij){

        this.nbCustomers = nbCustomers+1;       // number of customers/vertices   + depot client 0

        this.qty = qty;                         // q[i] is the demand at point 'i'. Depot is denoted by 0.

        this.nbVehicles = nbVehicles ;          /* total vehicle fleet
                                                   Fleet of vehicles: V = {1,...,m} with identical capacities.  */

        this.vCap = vCap;                       // a[k] capacity of each vehicle.

        this.max_cap_big = max_cap_big;         // Max vehicle capacity: Biggest truck (auxiliary variable)
        this.max_cap = max_cap;                 // Max vehicle capacity: Biggest truck

        /* Homogeneous and limited fleet that leave and return to the depot
        R[i] = {r[i](1),...,r[i](n[i])} denote the route for vehicle 'i',
        where r[i](j) is the index of the jth customer visited and n[i] is the nb. of customers in the route.
        r[i](n[i] + 1) = 0, every route finishes at the depot  */
        this.vclCapacity = vclCapacity;         // a[k] is total vehicle capacity, of each vehicle k ∈ V

                                    /* Split deliveries: the demand of a customer may be fulfilled by more than one vehicle.
                                        This occurs in all cases where some demand exceeds the vehicle capacity */

        this.costs = costs;                     // costs in distance ALL to ALL customers */        //consumptions

        this.servTime = servTime;               // time needed at the customer  */
        this.travTime = travTime;               // costs in time ALL to ALL customers */

        this.tWin = tWin;                       // time windows for each client [earliest_i, latest_j]

        this.M_ij = M_ij;     //Constant Equation 6
        this.stM_ij = stM_ij;

    }

    /************* P R I N T   T H E   I N P U T   D A T A  **************/
    public void printInput(Solver solver) {

        System.out.println("\nnbCustomers = " + (nbCustomers - 1) + ";");
        System.out.println("nbVehicles = " + nbVehicles + ";");
        System.out.println("serviceTime = " + servTime + ";");
        System.out.print("vCapacity => ");
        for (int i = 0; i < nbVehicles; i++)
            System.out.print(" Vcap" + i + " : " + vCap[i] + "; ");
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

        System.out.print("====== Time Windows ======\n________|_e__l_\n");
        for (int i = 0; i < nbCustomers; i++) {
            System.out.print("client"+i+" | ");
            for (int tw = 0; tw < 2; tw++) {
                System.out.print(tWin[i][tw] + " ");
            }System.out.print("\n");
        }System.out.print("\n");

        String costT = "====== Cost Time ======\n_|_0____1____2____3____4____5__\n";
        for (int i = 0; i < nbCustomers; i++) {
            String s = "";
            for (int j = 0; j < nbCustomers - 1; j++) {
                s += travTime[i][j] + "    ";
            }
            costT +=i+"| "+ s + travTime[i][nbCustomers - 1]+"\n";
        } System.out.println(costT);


        String mij = "====== Constant M[i][j] (latest_i + Costij - earliest_j) ======\n__|__0______1______2______3______4______5__\n";
        for (int i = 0; i < nbCustomers; i++) {
            mij += i+" | ";
            for (int j = 0; j < nbCustomers; j++) {
                mij +=M_ij[i][j] + "    ";}
            mij +="\n";
        } System.out.println(mij);

        String stmij = "====== stM[i][j] (Servtime + Costij - Mij) ======\n__|__0______1______2______3______4______5__\n";
        for (int i = 0; i < nbCustomers; i++) {
            stmij += i+" | ";
            for (int j = 0; j < nbCustomers; j++) {
                stmij +=stM_ij[i][j] + "    ";}
            stmij +="\n";
        } System.out.println(stmij);

    }


    /************** F I N D   S O L U T I O N **************/
    public void findSol(Solver solver) {
        /*if(solver.findSolution()){     //print all solutions
            do{
                System.out.println(solver.getSolutionRecorder().getLastSolution().toString(solver));
            }while(solver.nextSolution());
        }*/

        //solver.findOptimalSolution(ResolutionPolicy.MINIMIZE, globalCost); /* OBJECTIVE FUNCTION * Minimize the total cost of the route */
        //long nbSol = solver.getMeasures().getSolutionCount();

        //solver.findAllSolutions();

		/* Heuristic choices */
        IntVar[] vars = ArrayUtils.append(ArrayUtils.flatten(next), ArrayUtils.flatten(servStart), new IntVar[]{x1, y1});
        AbstractStrategy strat = IntStrategyFactory.minDom_LB(vars);
        solver.set(IntStrategyFactory.lastConflict(solver,strat));

        //solver.set(IntStrategyFactory.minDom_LB((ArrayUtils.flatten(next))));
        //solver.set(IntStrategyFactory.minDom_LB(new IntVar[]{x1, y1}));

       //int numFailures = 200000;
       //SearchMonitorFactory.limitFail(solver, numFailures);
        //solver.findSolution();
        solver.findOptimalSolution(ResolutionPolicy.MINIMIZE, globalCost);

        Chatterbox.printStatistics(solver);
        Chatterbox.showSolutions(solver);
//        System.out.println(solver.getSolutionRecorder().getLastSolution().toString(solver));
    }

    /************** P R I N T   T H E   O U T P U T   D A T A  **************/
    public void printOutput(Solver solver) {

        System.out.print("\n====== Demand per customer ======\n");
        int sum2 = 0;
        for (int i = 0; i < nbCustomers; i++) {
            System.out.print(" | client "+i+"="+qty[i]);
            sum2 = sum2 + qty[i];
        }System.out.print(" ===> "+sum2+"\n");

        for (int k = 0; k < nbVehicles; k++) {
            System.out.print("\n=== "+ capacityUsed[k]+ " from "+ vCap[k]+"\n" );
            int sum = 0;
            for (int i = 0; i < nbCustomers; i++) {     //2
                for (int j = 0; j < nbCustomers; j++)
                    if (edges[k][i][j].contains(1)&&(i!=j)){
                        System.out.print(edges[k][i][j] + " cost(distance) = " + costs[i][j]);
                        sum = sum + costs[i][j];
                        System.out.print("\n"); }
            }System.out.print("manual sum = "+sum+"\n");
        }
        for (int k = 0; k < nbVehicles; k++){
            System.out.print(totalVehicleDistance[k]+"  ");
        }
        System.out.print(" "+globalCost+"\n");

        System.out.print("====== Serve ======\n");
        System.out.print("_|0__1\n");
        for (int i = 0; i < nbCustomers; i++) {
            if(i<10) System.out.print(" ");
                System.out.print(i+"|");
            for (int k = 0; k < nbVehicles; k++) {
                if(serve[i][k].getValue()==0){
                    System.out.print("□ ");
                }else {
                    System.out.print("■ ");
                }
            }System.out.print("\n");
        }

        System.out.print("\n====== edgesM_ij ======\n");
        for (int k = 0; k < nbVehicles; k++) {
            System.out.print(k+" |0______1______2______3______4______5______6    7    8 9 10 11 12 13 14 15 \n");
            for (int i = 0; i < nbCustomers; i++) {
                if(i<10) System.out.print(" ");
                System.out.print(i+"|");
                for (int j = 0; j < nbCustomers; j++){
                    if(edgesM_ij[k][i][j].getValue()==0) {System.out.print("0  ");}
                    else {System.out.print(edgesM_ij[k][i][j].getValue()+ ""); }
                    System.out.print("    ");
                }System.out.print("\n");
            }System.out.print("\n");
        }

        System.out.print("====== servStart [k][i] ======\n");
        System.out.print("_|______0______1______2_______3________4_______5\n");
        for (int k = 0; k < nbVehicles; k++) {
            System.out.print(k + " | ");
            for (int i = 0; i < nbCustomers; i++) {
                System.out.print(servStart[k][i] + "  ");
            }System.out.print("\n");
        }

/**
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
*/
        System.out.print("\n====== Aux Edges ======\n");
        for (int k = 0; k < nbVehicles; k++) {
            System.out.print(k+" |0　1　2 3 4 5 6 7 8 9 10 11 12 13 14 15 \n");
            for (int i = 0; i < nbCustomers; i++) {
                if(i<10) System.out.print(" ");
                System.out.print(i+"|");
                for (int j = 0; j < nbCustomers; j++)
                    if(aux_edge[k][i][j].getValue()==0){
                        System.out.print("□ ");
                    }else {
                        System.out.print("■ ");
                    }
                System.out.print("\n");
            }System.out.print("\n");
        }
        System.out.println(servStart[0][0] +" "+ VF.fixed(stM_ij[0][1],solver) +" "+ edgesM_ij[0][0][1] +" "+ servStart[0][1]);
    }

    /************** C O N S T R A I N T S  **************/
    public void addRouteSolvingConstraints(Solver solver) {

        x1 = VF.bounded("x1", 39, 40, solver);
        y1 = VF.bounded("y1", 59, 60, solver);
        // solver.post(ICF.arithm(x1,"+", VF.fixed(20,solver),"=",8));
        IntVar[] vars1 = new IntVar[]{x1, VF.fixed(20,solver)};
        Constraint test1 = ICF.sum(vars1, "<=", y1);
        solver.post(test1);
        //solver.set(IntStrategyFactory.minDom_LB(new IntVar[]{x1, y1}));
        try {
            solver.propagate();
        } catch (ContradictionException e) {
//						e.printStackTrace();
            solver.getEngine().flush();
            solver.unpost(test1);
           // busChoosen = null;
        }

        /** CREATION OF CP VARIABLES */
        /* Initialisation of the boolean matrix - represents all paths all vehicles
        for each vehicle, 1 boolean matrix. x[k][i][j] True if vehicle vclK visits the client 'j' directly after 'i', False otherwise */
        edges = new BoolVar[nbVehicles][nbCustomers][nbCustomers];
        for (int k = 0; k < nbVehicles; k++)
            edges[k] = VariableFactory.boolMatrix("truck_" + k, nbCustomers, nbCustomers, solver);

        /* represents the cost per vehicle and the total cost for the objective function */
        totalVehicleDistance = VF.boundedArray("distance", nbVehicles, 0, VF.MAX_INT_BOUND, solver);  //fuelConsumed
        globalCost = VF.bounded("global_cost", 0, VF.MAX_INT_BOUND, solver);

        /* represents the capacity used per vehicle in the path */
        capacityUsed = new IntVar[nbVehicles];                          // goldFound
        for (int k = 0; k < nbVehicles; k++)
            capacityUsed[k] = VariableFactory.bounded("vehicle " + k + " capacityUsed", 0, vCap[k], solver);

        /* The next value table. The next variable of a node is the id of the next node in the path + an offset */
        next = VariableFactory.enumeratedMatrix("next", nbVehicles, nbCustomers, 0, nbCustomers - 1, solver);

		/* Integer Variable which represents the overall size of the path founded */
        size = VariableFactory.boundedArray("size", nbVehicles, 2, nbCustomers, solver);

        /** OBJECTIVE FUNCTION */
        /* Calculate total costs for each vehicle: The scalar constraint to compute global cost performed in all paths */
        for (int k = 0; k < nbVehicles; k++)
            solver.post(ICF.scalar(ArrayUtils.flatten(edges[k]), ArrayUtils.flatten(costs), totalVehicleDistance[k]));  //consumptions, fuelConsumed
        solver.post(ICF.sum(totalVehicleDistance, globalCost)); // compute global cost

        /** CONSTRAINT (1): each vehicle will leave the depot and arrive at a determined customer */
        for (int k = 0; k < nbVehicles; k++)
            solver.post(ICF.sum(ArrayUtils.flattenSubMatrix(0, 1, 1, nbCustomers - 1, edges[k]), "=", VariableFactory.fixed(1, solver)));

        /** CONSTRAINT (3): the total demand of each customer will be fulfilled */
        /* true if client 'i' is served by vehicle vclK, false otherwise */
        serve = VF.enumeratedMatrix("serve", nbCustomers, nbVehicles, 0, 1, solver);
        solver.post(ICF.sum(ArrayUtils.flattenSubMatrix(0, 1, 0, nbVehicles, serve), "=", VariableFactory.fixed(nbVehicles, solver))); // node 0 is not served by any vehicle
        for (int i = 1; i < nbCustomers; i++)
            solver.post(ICF.sum(ArrayUtils.flattenSubMatrix(i, 1, 0, nbVehicles, serve), "=", VariableFactory.fixed(1, solver)));

        /** CONSTRAINT (4): the vehicle capacity will not be exceed */
        /* The scalar constraint to compute global consumption of the kart to perform the path */
        // qty = {5, 2, 5, 10, 8};   // vCap = {50, 100};
        for (int k = 0; k < nbVehicles; k++) {
            solver.post(ICF.scalar(ArrayUtils.flattenSubMatrix(0, nbCustomers, k, 1, serve), qty, "<=", capacityUsed[k]));
        }

        /** CONSTRAINT (5): the demand of each customer will only be fulfilled if a determined vehicle goes by that place*/
        /* (We can notice that, adding to constraint (5) the sum of all vehicles and combining to equation (3) we have the constraint ____
        //which guarantees that each vertex will be visited at least once by at least one vehicle)*/
        /* Subcircuit brings the not used nodes in with the active cel [i][i], we use a boolean aux_edge[][] to get rid of this */
        aux_edge = new BoolVar[nbVehicles][nbCustomers][nbCustomers];   // initialize aux_edges
        for (int k = 0; k < nbVehicles; k++)
            aux_edge[k] = VariableFactory.boolMatrix("aux_edge" + k, nbCustomers, nbCustomers, solver);

        for (int k = 0; k < nbVehicles; k++)                    //  aux_edges = edges (except [i][i])
            for (int i = 0; i < nbCustomers; i++)
                for (int j = 0; j < nbCustomers; j++)
                    if(i==j){ solver.post(ICF.times(edges[k][i][j], 0, aux_edge[k][i][j])); }
                    else { solver.post(ICF.times(edges[k][i][j], 1, aux_edge[k][i][j])); }

        for (int k = 0; k < nbVehicles; k++){                   //  we apply the 5th constraint with the new copy (aux_edges)
            for (int i = 1; i < nbCustomers; i++)
                solver.post(ICF.sum(ArrayUtils.flattenSubMatrix(0, nbCustomers, i, 1, aux_edge[k]), ">=", serve[i][k])); }

        /** POST CIRCUIT CONSTRAINT */
        /* The scalar constraint to compute the amount of capacity that each vehicle perform in the path. With our model if a
		 * node isn't used then his next value is equals to his id. Then the boolean edges[k][i][i] is equals to true */
        BoolVar[][] used = new BoolVar[nbVehicles][nbCustomers];
        for (int k = 0; k < nbVehicles; k++){
            for (int i = 0; i < nbCustomers; i++)
                used[k][i] = edges[k][i][i].not();  //k
            solver.post(ICF.scalar(used[k], qty, capacityUsed[k]));   }    //gold, goldFound));

        /* The sub-circuit constraint. This forces all the next value to form a circuit which the overall size is equals
		 * to the size variable. This constraint check if the path contains any sub circles. */
        for (int k = 0; k < nbVehicles; k++)
          solver.post(ICF.subcircuit(next[k], 0, size[k]));

		/* The boolean channeling constraint. Enforce the relation between the next values and the edges values in the
		 * graph boolean variable matrix */
        for (int k = 0; k < nbVehicles; k++){
            for (int i = 0; i < nbCustomers; i++)
                solver.post(ICF.boolean_channeling(edges[k][i], next[k][i], 0));
        }

        /** Equation (6):minimum time for beginning the service of customer j in a determined route */
        /* also guarantees that there will be no sub tours. The constant M_ij  is a large enough number */
        M_ij = new int[nbCustomers][nbCustomers];
        stM_ij = new int[nbCustomers][nbCustomers];  // stM_ij (servTime + cost_ij - Mij)
        for (int i = 0; i < nbCustomers; i++)
            for (int j = 0; j < nbCustomers; j++)
                if(i!=j){
                    M_ij[i][j] = tWin[i][1] + travTime[i][j] - tWin[j][0];
                    stM_ij[i][j] = servTime + travTime[i][j] - M_ij[i][j];   //s_i + t_ij - M_ij (servTime[i] when you have different values per client)
                }

        edgesM_ij = new IntVar[nbVehicles][nbCustomers][nbCustomers];
        for (int k = 0; k < nbVehicles; k++) {
            edgesM_ij[k] = VF.boundedMatrix("edgeM_ij" + k, nbCustomers, nbCustomers, 0, VariableFactory.MAX_INT_BOUND, solver);
            for (int i = 0; i < nbCustomers; i++) {
                for (int j = 0; j < nbCustomers; j++) {
                    if(i!=j){ solver.post(ICF.times(edges[k][i][j], M_ij[i][j], edgesM_ij[k][i][j])); }
                    else { solver.post(ICF.times(edges[k][i][j], 0, edgesM_ij[k][i][j])); }
                }
            }
        }

        servStart = VF.boundedMatrix("servStart", nbVehicles, nbCustomers, 0, VariableFactory.MAX_INT_BOUND, solver);
        for (int k = 0; k < nbVehicles; k++) {
            System.out.println("\n");
            for (int i = 1; i < nbCustomers; i++) {
                for (int j = 1; j < nbCustomers; j++) {
                        solver.post(ICF.sum(new IntVar[]{servStart[k][i], VF.fixed(stM_ij[i][j], solver), edgesM_ij[k][i][j]}, "<=", servStart[k][j])); //k
                }
            }
        }

        /** CONSTRAINT (7): guarantees that all the customers will be served within their Time Windows*/
        /* earilest_i <= b_ki <= latest_i */
        for (int k = 0; k < nbVehicles; k++) {
            for (int i = 1; i < nbCustomers; i++) {
                solver.post(ICF.arithm(servStart[k][i], ">=", tWin[i][0]));
                solver.post(ICF.arithm(servStart[k][i], "<=", tWin[i][1]));
            }
        }


        /** STRONGER FILTERING */
		/* DISTANCE RELATED FILTERING * identifies the min/max distance involved by visiting each node  */
            IntVar[][] distanceAt = new IntVar[nbVehicles][nbCustomers-1];
            for(int k=0;k<nbVehicles;k++) {
                for (int i = 0; i < nbCustomers - 1; i++) {
                    distanceAt[k][i] = VariableFactory.bounded("distanceAt" +k +"_"+ i, 0, 99999, solver);
                    solver.post(ICF.element(distanceAt[k][i], costs[i], next[k][i], 0, "none"));
                }
            }

		/* CAPACITY RELATED FILTERING
		* We are trying to find the set of edges that serve the more goods and respects the fuel limit constraint.
		* The analogy: the weight is the costs to go through the edge and the energy is the goods that we can serve
            int[][] goldMatrix = new int[n][n];
            for (int i = 0; i < goldMatrix.length; i++)
                for (int j = 0; j < goldMatrix.length; j++)
                    goldMatrix[i][j] = (i == j) ? 0 : gold[i];
            solver.post(ICF.knapsack(ArrayUtils.flatten(edges), VariableFactory.fixed(FUEL, solver),
                    goldFound, ArrayUtils.flatten(consumptions), ArrayUtils.flatten(goldMatrix)));

            //Constraint knapsack(IntVar[] OCCURRENCES, IntVar TOTAL_WEIGHT,
            //                       IntVar TOTAL_ENERGY,   int[] WEIGHT,     int[] ENERGY)

        */


        /** CONSTRAINT (2): entrance and exit flows, guarantees that each vehicle will leave a customer and arrive to the depot
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
        }*/
    }
}
