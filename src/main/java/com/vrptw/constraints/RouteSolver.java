//github pass123
package com.vrptw.constraints;
import org.chocosolver.solver.ResolutionPolicy;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.constraints.ICF;
import org.chocosolver.solver.constraints.IntConstraintFactory;
import org.chocosolver.solver.constraints.LogicalConstraintFactory;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.search.loop.monitors.IMonitorSolution;
import org.chocosolver.solver.search.loop.monitors.SearchMonitorFactory;
import org.chocosolver.solver.search.solution.Solution;
import org.chocosolver.solver.search.strategy.IntStrategyFactory;
import org.chocosolver.solver.search.strategy.strategy.AbstractStrategy;
import org.chocosolver.solver.trace.Chatterbox;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VF;
import org.chocosolver.solver.variables.VariableFactory;
import org.chocosolver.util.tools.ArrayUtils;

import java.util.List;


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
    private int[][] breakTime;
    private int[][] travTime;
    private int[][] tWin;
    private int[][] M_ij;
    private int[][] stM_ij;
    private int[][] qtyMatrix;
    private double cost_km = 0.0;
    private String stg_bk = "";

    /************* D E C I S I O N   V A R I A B L E S **************/

    private IntVar globalCost;
    private IntVar[] totalVehicleDistance;
    private IntVar[] totalVehiclesRequired;
    private IntVar[] maxDistance;
    private IntVar[] totalTravTime;
    private IntVar[][] flowsEdges;
    private IntVar[] capacityUsed;
    private IntVar[] capacityUsed_100;
    private IntVar[][] serve;
    private IntVar[][] serve_qty;
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
                int[][] costs, int servTime, int[][] travTime, int[][] breakTime, int[][] tWin, int[][] M_ij, int[][] stM_ij){

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
        this.breakTime = breakTime;               // costs in time ALL to ALL customers */

        this.tWin = tWin;                       // time windows for each client [earliest_i, latest_j]

        this.M_ij = M_ij;     //Constant Equation 6
        this.stM_ij = stM_ij;

    }

    /************* P R I N T   T H E   I N P U T   D A T A  **************/
    public void printInput(Solver solver) {

        System.out.print("nbCustomers = " + (nbCustomers - 1) + ";");
        System.out.println("nbVehicles = " + nbVehicles + ";");
        System.out.println("serviceTime = " + servTime + ";");
        System.out.print("vCapacity => ");
        for (int i = 0; i < nbVehicles; i++)
            System.out.print(" Vcap" + i + " : " + vCap[i] + "; ");
        System.out.print("\nQtyRequired => ");
        for (int i = 0; i < nbCustomers; i++)
            System.out.print(" c" + (i) + " : " + qty[i] + "; ");
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

        System.out.print("\n====== Break Time (required) ======\n");
        System.out.print("  |0　1　2 3 4 5 6 7 8 9 10 11 12 13 14 15 \n");
        for (int i = 0; i < nbCustomers; i++) {
            if(i<10) System.out.print(" ");
            System.out.print(i+"|");
            for (int j = 0; j < nbCustomers; j++)
                if(breakTime[i][j]==0){
                    System.out.print("□ ");
                }else {
                    System.out.print("■ ");
                }
            System.out.print("\n");
        }System.out.print("\n");

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
        IntVar[] vars = ArrayUtils.append(  ArrayUtils.flatten(next),
                                            ArrayUtils.flatten(servStart),
                                            //ArrayUtils.flatten(serve_qty),
                                            //ArrayUtils.flatten(serve),
                                            ArrayUtils.flatten(edgesM_ij),
                                            capacityUsed, capacityUsed_100, totalVehicleDistance,
                                            new IntVar[]{x1, y1});
        AbstractStrategy strat = IntStrategyFactory.minDom_LB(vars);
        solver.set(IntStrategyFactory.lastConflict(solver,strat));

        solver.plugMonitor((IMonitorSolution) () -> {
            System.out.println("\n░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░");
            Chatterbox.showStatistics(solver);
            System.out.println(solver.getSolutionRecorder().getLastSolution().toString(solver));
            //System.out.println(solver.getSolutionRecorder().getLastSolution().toString(solver).);
            System.out.print("====== Demand per customer ====>");
            int sum2 = 0;
            for (int i = 0; i < nbCustomers; i++) {
                System.out.print(" | client "+i+"="+qty[i]);
                sum2 = sum2 + qty[i];
            }System.out.print(" ===> "+sum2+"\n");

            for (int k = 0; k < nbVehicles; k++) {
                System.out.println("=== " + capacityUsed[k] + " from " + vCap[k]);  }
            System.out.print("totalVehicleDistance => ");
            for (int k = 0; k < nbVehicles; k++){
                System.out.print(totalVehicleDistance[k]+"  ");
            }System.out.print("\ntotalVehiclesRequired => ");
            for (int k = 0; k < nbVehicles; k++){
                System.out.print(totalVehiclesRequired[k]+"   ");
            }System.out.print("\n" + globalCost + "\n");
        });

        //solver.findAllSolutions();

        int numFailures = 200000;
        SearchMonitorFactory.limitFail(solver, numFailures);
        solver.findSolution();

        solver.findOptimalSolution(ResolutionPolicy.MINIMIZE, globalCost);

       /* List<Solution> monday = solver.getSolutionRecorder().getSolutions();
        System.out.println("Monday Solution "+monday.size()+" solutions : ");
        for(Solution s:monday){
            System.out.println("totalTravTime = "+s.getIntVal(totalTravTime[1])+" and globalCost = "+s.getIntVal(globalCost));
        }*/

        Chatterbox.printStatistics(solver);
        Chatterbox.showSolutions(solver);
        System.out.println(solver.getSolutionRecorder().getLastSolution().toString(solver));
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
            System.out.println("=== " + capacityUsed[k] + " from " + vCap[k]);
            System.out.println("=== " + capacityUsed_100[k]);
            int sum = 0;
            for (int i = 0; i < nbCustomers; i++) {     //2
                for (int j = 0; j < nbCustomers; j++)
                    if (edges[k][i][j].contains(1)&&(i!=j)){
                        System.out.print(edges[k][i][j] + "cost(distance) = " + costs[i][j]);
                        sum = sum + costs[i][j];
                        System.out.print("\n"); }
            }
            if(vCap[0]>7500){ cost_km = sum * 1.5; }
            else {cost_km = sum * 0.6; }
            System.out.print("manual sum = "+sum+"\ncost km = "+cost_km+"\n\n");
        }
        System.out.print("\n====== Global Cost ======\n");
        System.out.print("totalVehicleDistance => ");
        for (int k = 0; k < nbVehicles; k++){
            System.out.print(totalVehicleDistance[k]+"  ");
        }System.out.print("\ntotalVehiclesRequired => ");
        for (int k = 0; k < nbVehicles; k++){
            System.out.print(totalVehiclesRequired[k]+"   ");
        }System.out.print("\n" + globalCost + "\n");

        System.out.print("\n====== Serve ======\n");
        System.out.print("_|0__1\n");
        for (int i = 0; i < nbCustomers; i++) {
            if(i<10) System.out.print(" ");
                System.out.print(i+"|");
            for (int k = 0; k < nbVehicles; k++) {
                System.out.print(serve[i][k] + "  ");
            }System.out.print("\n");
        }

        System.out.print("\n====== Serve_Qty ======\n");
        System.out.print("_|0____1____2\n");
        for (int i = 0; i < nbCustomers; i++) {
            if(i<10) System.out.print(" ");
            System.out.print(i+"|");
            for (int k = 0; k < nbVehicles; k++) {
                if(serve_qty[i][k].getValue()==0) {System.out.print("0     ");}
                else {System.out.print(serve_qty[i][k].getValue()+ "  "); }
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

        System.out.print("\n====== qtyMatrix ======\n");
        System.out.print("_|0__1\n");
        for (int i = 0; i < nbCustomers; i++) {
            System.out.print(" " + i+"|");
            for (int j = 0; j < nbCustomers; j++) {
                System.out.print(qtyMatrix[i][j] + "  ");
            }System.out.print("\n");
        }

        System.out.print("====== servStart [k][i] ======\n");
        System.out.print("_|______0______1______2_______3________4_______5\n");
        for (int k = 0; k < nbVehicles; k++) {
            System.out.print(k + " | ");
            for (int i = 0; i < nbCustomers; i++) {
                if(serve[i][k].getValue()>0)
                System.out.print(servStart[k][i] + "  ");
            }System.out.print("\n");
        }

        System.out.print("====== totalTravTime [k] ======\n");
        for (int k = 0; k < nbVehicles; k++) {
            System.out.println(totalTravTime[k] + "   ");
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
            }System.out.print("Break times 45 min:");

            for (int i = 0; i < nbCustomers; i++) {
                for (int j = 0; j < nbCustomers; j++) {
                    if(breakTime[i][j]==1 && aux_edge[k][i][j].getValue()==1 ){
                        stg_bk += "\nVehicle " + k + ": from [ " + i + " ] to [ " + j + " ]";
                    }
                }
            } if (stg_bk.length()>1) {
                System.out.print(stg_bk);
                }else {System.out.println(" -- None -- \n");}

        }

        System.out.print("\n====== summary TSP ======\n");

        System.out.println("1-vehicleCap	2-capacityUsed	3-distance_KM	4-travelTime	5-break times	6-global Cost 7-cost/km");
        System.out.println(vCap[0]);
        System.out.println(capacityUsed[0].getValue());
        System.out.println(totalVehicleDistance[0].getValue());
        System.out.println(totalTravTime[0].getValue());
        if (stg_bk.length()>1) {System.out.print(stg_bk);}else {System.out.println("0");}
        System.out.println(globalCost.getValue());
        System.out.println(cost_km);
    }


    /************** C O N S T R A I N T S  **************/
    public void addRouteSolvingConstraints(Solver solver) {

        /* Total Demand vs. Total Vehicle Capacity */
        System.out.print("\n====== Total Demand vs. Total Vehicle Capacity ======\n");
        int totalDemand = 0, totalVcap = 0;
        for (int i = 0; i < nbCustomers; i++) totalDemand = totalDemand + qty[i];
        for (int k = 0; k < nbVehicles; k++) totalVcap = totalVcap + vCap[k];
        System.out.println("totalDemand = " + totalDemand +" | totalVcap ="+totalVcap);

        int diffTotals = totalVcap - totalDemand;
        System.out.println("difference Totals = " + diffTotals);

        if(diffTotals < 0) {
            System.out.println("original nbVehicles = " + nbVehicles +" (+ 1 new vehicle)");
            nbVehicles = nbVehicles + 1;
            int[] vCap_new = {Math.abs(diffTotals)};      // absolute value of the excess
            vCap = ArrayUtils.append(vCap, vCap_new);
        }

        /* Try catch constraints*/
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

        /* represents the distance cost per vehicle and total number of vehicles. The total cost for the objective function */
        totalVehicleDistance = VF.boundedArray("distance", nbVehicles, 0, VF.MAX_INT_BOUND, solver);  //fuelConsumed
        totalVehiclesRequired = VF.boundedArray("cost_num_vehicles", nbVehicles, 0, VF.MAX_INT_BOUND, solver);  //to minimize num Vehicles Costs
        globalCost = VF.bounded("global_cost", 0, VF.MAX_INT_BOUND, solver);

        /* represents the Total_Weight in the knapsack constraint. We dont have any restriction according to the distance  */
        maxDistance = VF.boundedArray("maxDistance", nbVehicles, 0, VF.MAX_INT_BOUND, solver);  //fuelConsumed

        /* represents the capacity used per vehicle in the path */
        capacityUsed = new IntVar[nbVehicles];                          // goldFound
        for (int k = 0; k < nbVehicles; k++)
            capacityUsed[k] = VariableFactory.bounded("vehicle " + k + " capacityUsed", 0, vCap[k], solver);

        /* The next value table. The next variable of a node is the id of the next node in the path + an offset */
        next = VariableFactory.enumeratedMatrix("next", nbVehicles, nbCustomers, 0, nbCustomers - 1, solver);

		/* Integer Variable which represents the overall size of the path founded */
        size = VariableFactory.boundedArray("size", nbVehicles, 0, nbCustomers, solver);


        /** CONSTRAINT (3): the total demand of each customer will be fulfilled */
        /* true if client 'i' is served by vehicle vclK, false otherwise */
        serve = VF.boundedMatrix("serve", nbCustomers, nbVehicles, 0, 100, solver);
        solver.post(ICF.sum(ArrayUtils.flattenSubMatrix(0, 1, 0, nbVehicles, serve), "=", VF.fixed(0, solver))); // node 0 is not served by any vehicle
        for (int i = 1; i < nbCustomers; i++) {
            Constraint const3 = ICF.sum(ArrayUtils.flattenSubMatrix(i, 1, 0, nbVehicles, serve), "=", VariableFactory.fixed(100, solver));  // VariableFactory.fixed(1, solver));
            solver.post(const3);
            try {
                solver.propagate();
            } catch (ContradictionException e) {
                System.out.println("Constraint (3) = ContradictionException " + e);
                solver.getEngine().flush();
                solver.unpost(const3);
            }
        }

        /** CONSTRAINT (4): the vehicle capacity will not be exceed */
        /* The scalar constraint to compute global consumption of the kart to perform the path */
        // qty = {5, 2, 5, 10, 8};   // vCap = {50, 100};
        capacityUsed_100 = new IntVar[nbVehicles];
        for (int k = 0; k < nbVehicles; k++) {
            Constraint const4;
            if(diffTotals < 0)  /* When we add a new vehicle to fulfill the demands, all vehicles have to be completely full (=100%)*/
                 { const4 = ICF.arithm(capacityUsed[k],"=", vCap[k]); } //
            else { const4 = ICF.scalar(ArrayUtils.flattenSubMatrix(0, nbCustomers, k, 1, serve), qty, "<=", VF.fixed((vCap[k] * 100), solver)); }  //capacityUsed_100[k]));

            solver.post(const4);
            try { solver.propagate();}
            catch (ContradictionException e) {
                solver.getEngine().flush();
                solver.unpost(const4);
            }
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
                for (int j = 0; j < nbCustomers; j++) {
                    Constraint const5;
                    if (i == j) {   const5 = ICF.times(edges[k][i][j], 0, aux_edge[k][i][j]) ;
                        } else  {   const5 = ICF.times(edges[k][i][j], 1, aux_edge[k][i][j]);  }
                    solver.post(const5);
                    try {
                        solver.propagate();
                    } catch (ContradictionException e) {
                        System.out.println("Constraint (5) = ContradictionException " + e);
                        solver.getEngine().flush();
                        solver.unpost(const5);
                    }
                }

        for (int k = 0; k < nbVehicles; k++)                  //  we apply the 5th constraint with the new copy (aux_edges)
            for (int i = 0; i < nbCustomers; i++) {
                Constraint d = ICF.arithm(serve[i][k], ">", 0);
                Constraint f = ICF.sum(ArrayUtils.flattenSubMatrix(0, nbCustomers, i, 1, aux_edge[k]), VF.fixed(1, solver));  //serve[i][k]));
                LogicalConstraintFactory.ifThen(d, f);
                try {
                    solver.propagate();
                } catch (ContradictionException e) {
                    System.out.println("Constraint (5.2)= ContradictionException " + e);
                }
            }

        /**The European Community (EC) social legislation */
        /** CONSTRAINT (9): A 45 minutes break after 4.5 hours of driving */
        /* We make a copy of the travTime table and we add manually the resting time
        *  in distances with more than 16200 seconds driving*/
        breakTime = new int[nbCustomers][nbCustomers];
        for (int i = 0; i < nbCustomers; i++) {
            for (int j = 0; j < nbCustomers; j++) {
                if (travTime[i][j] >= 16200) {    // 4.5 hours driving (16200)
                    breakTime[i][j] = 1;
                    travTime[i][j] += 2700;       // 45 min rest
                } else {
                    breakTime[i][j] = 0;
                }
            }
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

        /**The European Community (EC) social legislation */
        /** CONSTRAINT (8): A daily driving period of no more than 9 hours */
        totalTravTime = VF.boundedArray("totalTravTime", nbVehicles, 0, 86400, solver);  //  24 hours * 3600 seconds = 86400
        for (int k = 0; k < nbVehicles; k++) {
            solver.post(ICF.scalar(ArrayUtils.flatten(edges[k]),ArrayUtils.flatten(travTime),"=",totalTravTime[k]));
            Constraint const8 = ICF.arithm(totalTravTime[k], "<=", 32400);                 //   9 hours * 3600 seconds = 32400
            solver.post(const8);
            try {
                solver.propagate();
            } catch (ContradictionException e) {
                System.out.println("Constraint (8) = ContradictionException " + e);
                solver.getEngine().flush();
                solver.unpost(const8);
            }
        }

        /** OBJECTIVE FUNCTION */
        /* Calculate total costs for each vehicle: The scalar constraint to compute global cost performed in all paths */
        for (int k = 0; k < nbVehicles; k++) {
            //consumptions, fuelConsumed
            solver.post(ICF.scalar(ArrayUtils.flatten(edges[k]), ArrayUtils.flatten(costs), totalVehicleDistance[k]));
            // penalize the introduction of new vehicles
            Constraint a = ICF.sum(ArrayUtils.flatten(aux_edge[k]), ">", VF.fixed(0, solver));
            Constraint b = ICF.times(VF.fixed(vCap[k], solver), 5, totalVehiclesRequired[k]);  // (vCap * 5) big number to penalize
            Constraint c = ICF.times(VF.fixed(vCap[k], solver), 0, totalVehiclesRequired[k]);
            LogicalConstraintFactory.ifThenElse(a, b, c);
        }

        // Compute global cost
        solver.post(ICF.sum(ArrayUtils.append(totalVehiclesRequired, totalVehicleDistance), globalCost));

        /** POST CIRCUIT CONSTRAINT */
        /* The scalar constraint to compute the amount of capacity that each vehicle perform in the path. With our model if a
		 * node isn't used then his next value is equals to his id. Then the boolean edges[k][i][i] is equals to true */

        /*A new table is created to have store the product of (Serve and Qty * 100)  */
        serve_qty = VF.boundedMatrix("serve_qty", nbCustomers, nbVehicles, 0, VariableFactory.MAX_INT_BOUND, solver);
        for (int k = 0; k < nbVehicles; k++)
            for (int i = 0; i < nbCustomers; i++)
                solver.post(ICF.times(serve[i][k], qty[i], serve_qty[i][k]));

        for (int k = 0; k < nbVehicles; k++){   //Capacity_used = Capacity_used100 / 100
            capacityUsed_100[k] = VariableFactory.bounded("vehicle " + k + " capacityUsed_100", 0, (vCap[k]*100), solver);
            solver.post(ICF.times(capacityUsed[k], 100, capacityUsed_100[k]));
            solver.post(ICF.sum(ArrayUtils.flattenSubMatrix(0, nbCustomers, k, 1, serve_qty), capacityUsed_100[k]));}

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

        /** STRONGER FILTERING */
		/* DISTANCE RELATED FILTERING * identifies the min/max distance involved by visiting each node  */
        IntVar[][] distanceAt = new IntVar[nbVehicles][nbCustomers-1];
        for(int k=0;k<nbVehicles;k++) {
            for (int i = 0; i < nbCustomers - 1; i++) {
                distanceAt[k][i] = VariableFactory.bounded("distanceAt" +k +"_"+ i, 0, 99999, solver);
                solver.post(ICF.element(distanceAt[k][i], costs[i], next[k][i], 0, "none"));
            }
        }

        /** Knapsack FILTERING */
		/* This problem can be seen has a knapsack problem where are trying to found the set of edges that contains the
		* more golds and respects the fuel limit constraint. The analogy is the following : the weight is the
		* consumption to go through the edge and the energy is the gold that we can earn */
        qtyMatrix = new int[nbCustomers][nbCustomers];
        for (int i = 0; i < qtyMatrix.length; i++)
            for (int j = 0; j < qtyMatrix.length; j++) {
                qtyMatrix[i][j] = (i == j) ? 0 : qty[i];
                System.out.println(qtyMatrix[i][j]);
            }
        for (int k = 0; k < nbVehicles; k++)
            solver.post(ICF.knapsack(ArrayUtils.flatten(edges[k]), maxDistance[k], capacityUsed[k],
                    ArrayUtils.flatten(costs), ArrayUtils.flatten(qtyMatrix)));
    }


    /************** C O N S T R A I N T S  **************/
    public void backUpConstraints(Solver solver) {


        /** CONSTRAINT (1): each vehicle will leave the depot and arrive at a determined customer */
        /**for (int k = 0; k < nbVehicles; k++){

         Constraint const1 = ICF.sum(ArrayUtils.flattenSubMatrix(0, 1, 1, nbCustomers - 1, edges[k]), "=", VariableFactory.fixed(1, solver));
         solver.post(const1);
         try {
         solver.propagate();
         } catch (ContradictionException e) {
         System.out.println("Constraint (1) = ContradictionException " + e);
         solver.getEngine().flush();
         solver.unpost(const1);
         }
         }*/

        /** CONSTRAINT (2): entrance and exit flows, guarantees that each vehicle will leave a customer and arrive to the depot
         flowsEdges = new IntVar[nbVehicles][nbCustomers];
         //     for (int k = 0; k < nbVehicles; k++)
         //     flowsEdges[k] = VariableFactory.enumeratedArray("flowsEdges" + k, nbCustomers, 0, nbCustomers, solver);

         //IntVar[] aux1 = VariableFactory.enumeratedArray("aux", nbCustomers, 0, nbCustomers, solver);

         for (int k = 0; k < nbVehicles; k++) {
         flowsEdges[k] = VariableFactory.enumeratedArray("flowsEdges" + k, nbCustomers, 0, nbCustomers, solver);
         for (int p = 0; p < nbCustomers; p++) {
         solver.post(ICF.sum(ArrayUtils.flattenSubMatrix(0, nbCustomers, p, 1, edges[k]), "=",  flowsEdges[k][p]));
         solver.post(ICF.sum(ArrayUtils.flattenSubMatrix(p, 1, 0, nbCustomers, edges[k]), "=",  flowsEdges[k][p]));
         solver.post(ICF.arithm(edges[k][p][p],"=", VariableFactory.fixed(false, solver)));   // Edge from 'i' to 'j' when 'i'='j' is equal to 0
         }
         }*/
    }
}