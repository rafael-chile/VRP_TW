//github pass123
package com.vrptw.constraints;
import org.chocosolver.solver.ResolutionPolicy;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.*;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.search.limits.FailCounter;
import org.chocosolver.solver.search.loop.lns.LNSFactory;
import org.chocosolver.solver.search.loop.monitors.IMonitorSolution;
import org.chocosolver.solver.search.loop.monitors.SMF;
import org.chocosolver.solver.search.loop.monitors.SearchMonitorFactory;
import org.chocosolver.solver.search.solution.BestSolutionsRecorder;
import org.chocosolver.solver.search.solution.Solution;
import org.chocosolver.solver.search.strategy.IntStrategyFactory;
import org.chocosolver.solver.search.strategy.strategy.AbstractStrategy;
import org.chocosolver.solver.trace.Chatterbox;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VF;
import org.chocosolver.solver.variables.VariableFactory;
import org.chocosolver.util.tools.ArrayUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader.*;
import java.util.ArrayList;
import java.util.List;

public class RouteSolver {

    /************* C L A S S  P A R A M E T E R S **************/

    private String testID;
    private int nbCustomers;
    private int[] qty;
    private int nbVehicles;
    private int totalDemand;
    private int totalVcap;
    private int[] vCap;
    private int[] vehicleID;
    private int max_cap_big;
    private int max_cap;
    private int vclCapacity;
    private int[][] costs;
    private int[] servTime;
    private int[][] breakTime;
    private int[][] travTime;
    private int[][] tWin;
    private int[][] M_ij;
    private int[][] stM_ij;
    private int[][] qtyMatrix;
    private double cost_km = 0.0;
    private String stg_bk = "";
    private String outputVRP = "";
    private String lines = "";
    public BestSolutionsRecorder objSolRecord;

    /************* D E C I S I O N   V A R I A B L E S **************/

    private IntVar globalCost;
    private IntVar basicFleetcost;
    private IntVar totalCapacityUsed;
    private IntVar totalDistance;
    private IntVar totalTTime;
    private IntVar[] totalVehicleDistance;
    private IntVar[] startDepotTime;
    private IntVar[] penaltyStart;
    private IntVar[] penaltyFinish;
    private IntVar[] penaltyExtraHours;
    private IntVar[] totalVehicleTTime;
    private IntVar[] totalVehicleKmEuros;
    //private IntVar[] totalVehiclesRequired;
    //private IntVar[] totalVehiclesNotRequired;
    //private IntVar[] maxDistance;
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
    //private IntVar x1;
    //private IntVar y1;


    /** C O N S T R U C T O R */
    public RouteSolver(String testID, int nbCustomers, int[] qty, int nbVehicles, int[] vCap, int[] vehicleID, int max_cap_big, int max_cap, int vclCapacity,
                int[][] costs, int[] servTime, int[][] travTime, int[][] breakTime, int[][] tWin, int[][] M_ij, int[][] stM_ij){

        this.testID = testID;

        this.nbCustomers = nbCustomers+1;       // number of customers/vertices   + depot client 0

        this.qty = qty;                         // q[i] is the demand at point 'i'. Depot is denoted by 0.

        this.nbVehicles = nbVehicles ;          /* total vehicle fleet
                                                   Fleet of vehicles: V = {1,...,m} with identical capacities.  */

        this.vCap = vCap;                       // a[k] capacity of each vehicle.

        this.vehicleID = vehicleID;             // vehicle's id.

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

        System.out.println("nbCustomers = " + (nbCustomers - 1) + ";");
        System.out.println("nbVehicles = " + nbVehicles + ";");
        System.out.print("serviceTime = > ");
        for (int i = 0; i < nbVehicles; i++)
            System.out.print(" serviceTime" + i + " : " + servTime[i] + "; ");
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

        IntVar[] varsNext = ArrayUtils.append(  ArrayUtils.flatten(next));

        IntVar[] vars = ArrayUtils.append(  ArrayUtils.flatten(next),
                                            ArrayUtils.flatten(servStart),
                                            //ArrayUtils.flatten(serve_qty),
                                            ArrayUtils.flatten(serve),
                                            ArrayUtils.flatten(edgesM_ij),
                                            capacityUsed, capacityUsed_100, totalVehicleDistance, startDepotTime, totalVehicleTTime, penaltyFinish, penaltyStart, penaltyExtraHours, totalVehicleKmEuros,
                                            new IntVar[]{totalCapacityUsed, totalDistance, totalTTime, globalCost});

        IntVar[] ivars1 = solver.retrieveIntVars();

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
                System.out.print(totalVehicleDistance[k]+"  "+totalVehicleKmEuros[k]+ " euros");
                }
            System.out.println("\n"+globalCost);
        });

        //solver.findAllSolutions();

        /**int numFailures = 200000;
        SearchMonitorFactory.limitFail(solver, numFailures);
        solver.findSolution();*/

        objSolRecord = null;
        objSolRecord = new BestSolutionsRecorder(globalCost);

        //Chatterbox.showDecisions(solver);
        //SMF.limitSolution(solver, 3);

        /**LNSFactory.pglns(solver, ivars, 30, 10, 200, 0, new FailCounter(solver, 100));
        SMF.limitTime(solver, "15m"); // because PGLNS is not complete (due to Fast Restarts), we add a time limit
        solver.findOptimalSolution(ResolutionPolicy.MINIMIZE, objective);*/

        /** - Partial solution based on ivars
         *  - If no solution are found within 100 fails
         *  - then restart is forced
         *  - Every 30 calls to this neighborhood: the number of fixed variables is used 20140909L (random seed)*/

        //SMF.limitSolution(solver, 1);

        //IntVar[] ivars = solver.retrieveIntVars();
        SMF.limitTime(solver, "15m");
        //SMF.limitSolution(solver, 1);
        //LNSFactory.pglns(solver, ivars, 30, 10, 200, 0, new FailCounter(solver, 100));
        //LNSFactory.rlns(solver, vars, 30, 20140909L, new FailCounter(solver, 1000));
        //pglns(Solver solver, IntVar[] vars, int fgmtSize, int listSize, int level, long seed, ACounter frcounter)
        //LNSFactory.pglns(solver, ivars, 30, 100, 2000, 20140909L, new FailCounter(solver, 1000));
        solver.findOptimalSolution(ResolutionPolicy.MINIMIZE, globalCost);

       /* List<Solution> monday = solver.getSolutionRecorder().getSolutions();
        System.out.println("Monday Solution "+monday.size()+" solutions : ");
        for(Solution s:monday){
            System.out.println("totalTravTime = "+s.getIntVal(totalTravTime[1])+" and globalCost = "+s.getIntVal(globalCost));
        }*/
        Chatterbox.showSolutions(solver);

        //String loggerFile = System.getProperty("user.dir")+"/src/main/java/com/routecostloader/app/logs/rc-log.%u.%g.log";

      //System.out.println(solver.getSolutionRecorder().getLastSolution().toString(solver));
      //System.out.println("objSolRecord" + objSolRecord.toString());

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
            System.out.print("manual sum = "+sum+"\ncost_km = "+cost_km+"\n\n");
        }
        System.out.print("\n====== Global Cost ======\n");
        System.out.print("totalVehicleDistance (not taken) => ");
        for (int k = 0; k < nbVehicles; k++){
            System.out.print(totalVehicleDistance[k]+"  ");
        }
        System.out.print("\ntotalVehicleKmEuros => ");
        for (int k = 0; k < nbVehicles; k++){
            System.out.print(totalVehicleKmEuros[k]+"  ");
        }
        System.out.print("\ntotalVehicleTTime => ");
        for (int k = 0; k < nbVehicles; k++) {
            System.out.print(totalVehicleTTime[k] + "  ");
        }

        System.out.print("\n" + totalCapacityUsed + "\n");
        System.out.print("\n" + globalCost + "\n");

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

      /**  System.out.print("\n====== qtyMatrix ======\n");
        System.out.print("_|0__1\n");
        for (int i = 0; i < nbCustomers; i++) {
            System.out.print(" " + i+"|");
            for (int j = 0; j < nbCustomers; j++) {
                System.out.print(qtyMatrix[i][j] + "  ");
            }System.out.print("\n");
        }*/

        System.out.print("====== servStart [k][i] ======\n");
        System.out.print("_|______0______1______2_______3________4_______5\n");
        for (int k = 0; k < nbVehicles; k++) {
            System.out.print(k + " | ");
            System.out.print(servStart[k][0] + "  ");
            for (int i = 1; i < nbCustomers; i++) {
                if(serve[i][k].getValue()>0)
                System.out.print(servStart[k][i] + "  ");
            }System.out.print("\n");
        }

        System.out.print("\nstartDepotTime => \n");
        for (int k = 0; k < nbVehicles; k++){
            System.out.print(startDepotTime[k]+"  ");
        }

        System.out.print("\npenaltyStart => \n");
        for (int k = 0; k < nbVehicles; k++){
            System.out.print(penaltyStart[k]+"  ");
        }
        System.out.print("\npenaltyFinish => \n");
        for (int k = 0; k < nbVehicles; k++){
            System.out.print(penaltyFinish[k]+"  ");
        }

        System.out.print("\npenaltyExtraHours => \n");
        for (int k = 0; k < nbVehicles; k++){
            System.out.print(penaltyExtraHours[k]+"  ");
        }

        System.out.print("\n\n====== totalTravTime [k] ======\n");
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


        System.out.print("\n====== summary VRP ======\n");

        System.out.println("1-Num Vehicles  2-Locations  3-Total_Qty  4-vehicleCap  5-totalCapacityUsed  \n" +
                "6-distance_KM  7-travelTime  8-breaktimes  9-cost/km  finnot_used_cost  \n" +
                "11-TravelTime_cost  12-penalty_per_V  13-global_cost\n\n");
        System.out.print("nbVehicles = \t"); System.out.println(nbVehicles);
        System.out.print("nbCustomers = ");System.out.println(nbCustomers-1);
        System.out.print("totalDemand = ");System.out.println(totalDemand);
        System.out.print("totalVcap = ");System.out.println(totalVcap);
        System.out.print("totalCapacityUsed = ");System.out.println(totalCapacityUsed.getValue());
        System.out.print("totalDistance = ");System.out.println(totalDistance.getValue());
        System.out.print("totalTTime = ");System.out.println(totalTTime.getValue());
        if (stg_bk.length()>1) {System.out.print(stg_bk);}else {System.out.println("breaktimes = "+"0");}

        double KE_cost= 0, PS_cost= 0, PF_cost= 0, EH_cost= 0, global_cost, basic_cost;
        for (int k = 0; k < nbVehicles; k++) {
            KE_cost += totalVehicleKmEuros[k].getValue()/100.0;
            PS_cost += penaltyStart[k].getValue()/100.0;
            PF_cost += penaltyFinish[k].getValue()/100.0;
            EH_cost += penaltyExtraHours[k].getValue()/100.0;
        }   global_cost = (globalCost.getValue())/100.0;
            basic_cost = (basicFleetcost.getValue())/100.0;

        System.out.print("totalVehicleKmEuros = ");System.out.println(KE_cost);
        System.out.print("basicFleetcost = ");System.out.println(basic_cost);
        System.out.print("penaltyStart = " + PS_cost);System.out.println(PS_cost);
        System.out.print("penaltyFinish = ");System.out.println(PF_cost);
        System.out.print("penaltyExtraHours = ");System.out.println(EH_cost);
        System.out.print("global cost = " + global_cost);
                //  http://choco-solver.org/apidocs/org/chocosolver/solver/search/measure/IMeasures.html
                //Variables	 //Constraints
                // isOptimal?
                System.out.println("\n\n" + solver.getMeasures().isObjectiveOptimal());
        // Resolution time
        System.out.println(solver.getMeasures().getTimeCount());
        // Nodes
        System.out.println(solver.getMeasures().getNodeCount());
        // Backtracks
        System.out.println(solver.getMeasures().getBackTrackCount());
        // Fails
        System.out.println(solver.getMeasures().getFailCount());
        // Solutions
        System.out.println(solver.getMeasures().getSolutionCount()+"\n\n\n\n");


        outputVRP = "\n" + nbVehicles + "\t" + (nbCustomers - 1) + "\t" +  totalDemand + "\t" +  totalVcap + "\t" + totalCapacityUsed.getValue() + "\t" +  totalDistance.getValue() + "\t" +  totalTTime.getValue();
        if (stg_bk.length()>1) {outputVRP += "\t" + stg_bk;} else {outputVRP += "\t" + "0";}
        outputVRP += "\t" + KE_cost + "\t" + basic_cost + "\t" + PS_cost + "\t" + PF_cost + "\t" + EH_cost + "\t" + global_cost;
        outputVRP += "\t\t\t" + solver.getMeasures().isObjectiveOptimal() + "\t" + solver.getMeasures().getTimeCount()+ "\t" +  solver.getMeasures().getNodeCount()+ "\t" +  solver.getMeasures().getBackTrackCount() + "\t" +  solver.getMeasures().getFailCount() + "\t" +  solver.getMeasures().getSolutionCount()+"\n\n";
        outputVRP += solver.getMeasures().toString()+"\n\n";
        for (int k = 0; k < nbVehicles; k++) {
            outputVRP += "\n" + vehicleID[k] + " \t= " + capacityUsed[k].getValue() + " from " + vCap[k];  }

        /**
        System.out.print("\n" + nbVehicles + "\t" + (nbCustomers - 1) + "\t" +  totalDemand + "\t" +  totalVcap + "\t" + totalCapacityUsed.getValue() + "\t" +  totalDistance.getValue() + "\t" +  totalTTime.getValue());
        if (stg_bk.length()>1) {System.out.print("\t" + stg_bk);}else {System.out.print("\t" + "0");}
        System.out.print("\t" + KE_cost + "\t" + basic_cost + "\t" + PS_cost + "\t" + PF_cost + "\t" + EH_cost + "\t" + global_cost);
        System.out.print("\t\t\t" + solver.getMeasures().isObjectiveOptimal() + "\t" + solver.getMeasures().getTimeCount()+ "\t" +  solver.getMeasures().getNodeCount()+ "\t" +  solver.getMeasures().getBackTrackCount() + "\t" +  solver.getMeasures().getFailCount() + "\t" +  solver.getMeasures().getSolutionCount()+"\n\n");
        Solver output
        Chatterbox.printStatistics(solver);
        for (int k = 0; k < nbVehicles; k++) {
        System.out.print("\n" + vehicleID[k] + " \t= " + capacityUsed[k].getValue() + " from " + vCap[k]);  }   */

        try {
            lines = solver.getSolutionRecorder().getLastSolution().toString(solver);
            lines += "\n\n\n";
            lines += outputVRP;
            BufferedWriter writer = new BufferedWriter(new FileWriter(testID));
            writer.write(lines);
            writer.close();}
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    /************** C O N S T R A I N T S  **************/
    public void addRouteSolvingConstraints(Solver solver) {

        /* Total Demand vs. Total Vehicle Capacity */
        System.out.print("\n====== Total Demand vs. Total Vehicle Capacity ======\n");
        totalDemand = 0; totalVcap = 0;
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
            servTime = ArrayUtils.append(servTime, new int[] {2250});
        }

        /** CREATION OF CP VARIABLES */
        /* Initialisation of the boolean matrix - represents all paths all vehicles
        for each vehicle, 1 boolean matrix. x[k][i][j] True if vehicle vclK visits the client 'j' directly after 'i', False otherwise */
        edges = new BoolVar[nbVehicles][nbCustomers][nbCustomers];
        for (int k = 0; k < nbVehicles; k++)
            edges[k] = VariableFactory.boolMatrix("truck_" + k, nbCustomers, nbCustomers, solver);

        /* represents the distance cost per vehicle and total number of vehicles. The total cost for the objective function */
        totalVehicleDistance = VF.boundedArray("distance", nbVehicles, 0, VF.MAX_INT_BOUND, solver);  //  fuelConsumed
        totalVehicleTTime = VF.boundedArray("travelTime", nbVehicles, 0, VF.MAX_INT_BOUND, solver);  //  totalVehicleTravelTime
        totalVehicleKmEuros = VF.boundedArray("km_euros", nbVehicles, 0, VF.MAX_INT_BOUND, solver);  //  distance/euros
        //totalVehiclesRequired = VF.boundedArray("cost_num_vehicles", nbVehicles, 0, VF.MAX_INT_BOUND, solver);  //to minimize num Vehicles Costs
        //totalVehiclesNotRequired = VF.boundedArray("cost_notUsed_vehicles", nbVehicles, 0, 250, solver);  //to minimize num Vehicles Costs
        totalCapacityUsed = VF.bounded("totalCapUsed", 0, VF.MAX_INT_BOUND, solver);
        totalDistance = VF.bounded("totalDistance", 0, VF.MAX_INT_BOUND, solver);
        totalTTime = VF.bounded("totalTTime", 0, VF.MAX_INT_BOUND, solver);
        globalCost = VF.bounded("global_cost", 0, VF.MAX_INT_BOUND, solver);
        penaltyStart = VF.boundedArray("penaltyStart", nbVehicles, 0, 200, solver);
        penaltyFinish = VF.boundedArray("penaltyFinish", nbVehicles, 0, 400, solver);
        penaltyExtraHours = VF.boundedArray("penaltyExtraHours", nbVehicles, 0, 900, solver);
        basicFleetcost = VF.bounded("basicFleetcost", 0, VF.MAX_INT_BOUND, solver);


        /* represents the Total_Weight in the knapsack constraint. We dont have any restriction according to the distance  */
        //maxDistance = VF.boundedArray("maxDistance", nbVehicles, 0, VF.MAX_INT_BOUND, solver);  //fuelConsumed

        /* represents the capacity used per vehicle in the path */
        capacityUsed = new IntVar[nbVehicles];                          // goldFound
        for (int k = 0; k < nbVehicles; k++)
            capacityUsed[k] = VariableFactory.bounded("vehicle " + k + " capacityUsed", 0, vCap[k], solver);
        solver.post(ICF.sum(capacityUsed, "=", totalCapacityUsed));         //totalCapacityUsed

        /* The next value table. The next variable of a node is the id of the next node in the path + an offset */
        next = VariableFactory.enumeratedMatrix("next", nbVehicles, nbCustomers, 0, nbCustomers - 1, solver);

		/* Integer Variable which represents the overall size of the path founded */
        size = VariableFactory.boundedArray("size", nbVehicles, 0, nbCustomers, solver);


        /** CONSTRAINT (3): the total demand of each customer will be fulfilled */
        /* true if client 'i' is served by vehicle vclK, false otherwise */
        serve = VF.boundedMatrix("serve", nbCustomers, nbVehicles, 0, 100, solver);
        solver.post(ICF.sum(ArrayUtils.flattenSubMatrix(0, 1, 0, nbVehicles, serve), "=", VF.fixed(0, solver))); // node 0 is not served by any vehicle


        /** CONSTRAINT 3.1 to force Vehicle-Client pairs  */
        //solver.post(ICF.arithm(serve[1][5],"=",100));

       // solver.post(ICF.arithm(serve[1][4], "=", 100));
        //solver.post(ICF.arithm(serve[2][4], "=", 100));
       // solver.post(ICF.arithm(serve[3][1], "=", 100));
       // solver.post(ICF.arithm(serve[8][4], "=", 100));
        //solver.post(ICF.arithm(serve[9][1], "=", 100));


        /** Fin Constraint 3.1   */


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


        /** Equation (6):minimum time for beginning the service of customer j in a determined route */
        /* also guarantees that there will be no sub tours. The constant M_ij  is a large enough number */
        M_ij = new int[nbCustomers][nbCustomers];
        stM_ij = new int[nbCustomers][nbCustomers];  // stM_ij (servTime + cost_ij - Mij)
        for (int i = 0; i < nbCustomers; i++)
            for (int j = 0; j < nbCustomers; j++)
                if(i!=j){
                    M_ij[i][j] = tWin[i][1] + travTime[i][j] - tWin[j][0];
                    stM_ij[i][j] = 2250 + travTime[i][j] - M_ij[i][j];   //s_i + t_ij - M_ij (servTime[i] when you have different values per client)
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
                for (int j = 0; j < nbCustomers; j++) {
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

        startDepotTime = VF.boundedArray("startDepotTime", nbVehicles, 0, tWin[0][1], solver);  //  Vehicles starting time

        /** CONSTRAINT (7.1): guarantees that all the vehicles start the service during the Company working hours*/
        for (int k = 0; k < nbVehicles; k++) {
            for (int i = 1; i < nbCustomers; i++) {
                //startDepotTime[k] + travTime[0][i] <= servStart[k][i]
                Constraint a7 = ICF.arithm(capacityUsed[k], ">", 0);
                Constraint b7 = ICF.arithm(startDepotTime[k], ">=", tWin[0][0]);
                LCF.ifThen(a7, b7);

                Constraint c7 = ICF.arithm(capacityUsed[k], ">", 0);
                Constraint d7 = ICF.arithm(servStart[k][i], "-", startDepotTime[k], ">=", travTime[0][i]);
                Constraint f7 = ICF.arithm(startDepotTime[k], "=", 0);
                LCF.ifThenElse(c7, d7, f7);
            }
        }

        /** CONSTRAINT (7.2): guarantees that all the vehicles finish the service during the Company working hours*/
        for (int k = 0; k < nbVehicles; k++) {
            solver.post(ICF.arithm(servStart[k][0], "<=", tWin[0][1]));
        }

        /**The European Community (EC) social legislation */
        /** CONSTRAINT (8): A daily driving period of no more than 9 hours */
        totalTravTime = VF.boundedArray("totalTravTime", nbVehicles, 0, 86400, solver);  //  24 hours * 3600 seconds = 86400
        Constraint const8;
        for (int k = 0; k < nbVehicles; k++) {
            solver.post(ICF.scalar(ArrayUtils.flatten(edges[k]),ArrayUtils.flatten(travTime),"=",totalTravTime[k]));
            const8 = ICF.arithm(totalTravTime[k], "<=", 43200);                 //   12 hours * 3600 seconds = 43200
            solver.post(const8);
            try {
                solver.propagate();
            } catch (ContradictionException e) {
                System.out.println("Constraint (8) = ContradictionException " + e);
                solver.getEngine().flush();
                solver.unpost(const8);
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


        /** OBJECTIVE FUNCTION */
        /* Calculate total costs for each vehicle: The scalar constraint to compute global cost performed in all paths */
        /* Cost in Euros  times  10 to avoid rational numbers */

           // costs in distance
            Constraint const_distance;
        for (int k = 0; k < nbVehicles; k++) {
                const_distance = ICF.scalar(ArrayUtils.flatten(edges[k]), ArrayUtils.flatten(costs), totalVehicleDistance[k]);
            solver.post(const_distance);
            try { solver.propagate(); }
            catch (ContradictionException e) {
                System.out.println("Objective Function - const_totalvd = ContradictionException " + e);
                solver.getEngine().flush(); solver.unpost(const_distance); }

            // vehicle cost/km     small 0.60€/km    and    large: 1.50€/km
            if(vCap[k] >= 7500)  solver.post(ICF.times(totalVehicleDistance[k], 15, totalVehicleKmEuros[k]));
            else                solver.post(ICF.times(totalVehicleDistance[k], 6, totalVehicleKmEuros[k]));

            // plus a fixed cost per vehicle not used/day    small 10€/km    and    large: 25€/km
            solver.post(ICF.arithm(basicFleetcost,"=", 2350));  //

            /** Penalty for starting before 8am    10€/hour  */
            Constraint c1 = IntConstraintFactory.arithm(startDepotTime[k],">=",21600 );
            Constraint c2 = IntConstraintFactory.arithm(startDepotTime[k],"<",25200);
            Constraint c3 = IntConstraintFactory.arithm(startDepotTime[k],">=",25200);
            Constraint c4 = IntConstraintFactory.arithm(startDepotTime[k],"<",28800 );
            Constraint c1c2 = LogicalConstraintFactory.and(c1,c2);
            Constraint c3c4 = LogicalConstraintFactory.and(c3,c4);
            Constraint then1 = ICF.arithm(penaltyStart[k], "=", 200);
            Constraint then2 = ICF.arithm(penaltyStart[k], "=", 100);
            LogicalConstraintFactory.ifThen(c1c2, then1);
            LogicalConstraintFactory.ifThen(c3c4, then2);

            /**  Penalty for starting finishing 8pm    10€/hour  */
            Constraint c5 = IntConstraintFactory.arithm(startDepotTime[k],">",72000 );
            Constraint c6 = IntConstraintFactory.arithm(startDepotTime[k],"<=",75600);
            Constraint c7 = IntConstraintFactory.arithm(startDepotTime[k],">",75600);
            Constraint c8 = IntConstraintFactory.arithm(startDepotTime[k],"<=",79200 );
            Constraint c9 = IntConstraintFactory.arithm(startDepotTime[k],">",79200 );
            Constraint c10 = IntConstraintFactory.arithm(startDepotTime[k],"<=",82800);
            Constraint c11 = IntConstraintFactory.arithm(startDepotTime[k],">",82800);
            Constraint c12 = IntConstraintFactory.arithm(startDepotTime[k],"<=",86400 );

            Constraint c5c6 = LogicalConstraintFactory.and(c5,c6);
            Constraint c7c8 = LogicalConstraintFactory.and(c7,c8);
            Constraint c9c10 = LogicalConstraintFactory.and(c9,c10);
            Constraint c11c12 = LogicalConstraintFactory.and(c11,c12);

            Constraint then3 = ICF.arithm(penaltyFinish[k], "=", 100);
            Constraint then4 = ICF.arithm(penaltyFinish[k], "=", 200);
            Constraint then5 = ICF.arithm(penaltyFinish[k], "=", 300);
            Constraint then6 = ICF.arithm(penaltyFinish[k], "=", 400);

            LogicalConstraintFactory.ifThen(c5c6, then3);
            LogicalConstraintFactory.ifThen(c7c8, then4);
            LogicalConstraintFactory.ifThen(c9c10, then5);
            LogicalConstraintFactory.ifThen(c11c12, then6);

            /** Penalty for driving more than 9 hours (max 12h)  30€/hour  */
            Constraint c13 = IntConstraintFactory.arithm(totalTravTime[k],">",32400 );      // 9h
            Constraint c14 = IntConstraintFactory.arithm(totalTravTime[k],"<=",36000);      // 10h
            Constraint c15 = IntConstraintFactory.arithm(totalTravTime[k],">",36000);
            Constraint c16 = IntConstraintFactory.arithm(totalTravTime[k],"<=",39600 );     // 11h
            Constraint c17 = IntConstraintFactory.arithm(totalTravTime[k],">",39600 );
            Constraint c18 = IntConstraintFactory.arithm(totalTravTime[k],"<=",43200);      // 12h

            Constraint c13c14 = LogicalConstraintFactory.and(c13,c14);
            Constraint c15c16 = LogicalConstraintFactory.and(c15,c16);
            Constraint c17c18 = LogicalConstraintFactory.and(c17,c18);

            Constraint then7 = ICF.arithm(penaltyExtraHours[k], "=", 300);
            Constraint then8 = ICF.arithm(penaltyExtraHours[k], "=", 600);
            Constraint then9 = ICF.arithm(penaltyExtraHours[k], "=", 900);

            LogicalConstraintFactory.ifThen(c13c14, then7);
            LogicalConstraintFactory.ifThen(c15c16, then8);
            LogicalConstraintFactory.ifThen(c17c18, then9);


            /**Constraint used_a = ICF.sum(ArrayUtils.flatten(aux_edge[k]), ">", VF.fixed(0, solver));
            Constraint used_b = ICF.times(VF.fixed(10, solver), 0, totalVehiclesNotRequired[k]);
            int notUsed;
            if(vCap[k] > 7500)  {notUsed = 250;}   // (vCap * 5) big number to penalize
            else                {notUsed = 100;}
            Constraint used_c = ICF.times(VF.fixed(10, solver), notUsed, totalVehiclesNotRequired[k]);
            LogicalConstraintFactory.ifThenElse(used_a, used_b, used_c);

            // costs in travelTime~
            Constraint const_travelTime;
            const_travelTime = ICF.scalar(ArrayUtils.flatten(edges[k]), ArrayUtils.flatten(travTime), totalVehicleTTime[k]);
                solver.post(const_travelTime);
                try { solver.propagate(); }
                catch (ContradictionException e) {
                    System.out.println("Objective Function - const_travelTime = ContradictionException " + e);
                    solver.getEngine().flush(); solver.unpost(const_travelTime); }

            // penalize the introduction of new vehicles
            Constraint a = ICF.sum(ArrayUtils.flatten(aux_edge[k]), ">", VF.fixed(0, solver));
            Constraint b = ICF.times(VF.fixed(vCap[k], solver), 3, totalVehiclesRequired[k]);  // (vCap * 5) big number to penalize
            Constraint c = ICF.times(VF.fixed(vCap[k], solver), 0, totalVehiclesRequired[k]);
            LogicalConstraintFactory.ifThenElse(a, b, c);   */

        }

        // Compute global cost
        solver.post(ICF.sum(totalVehicleDistance, "=", totalDistance));         //totalDistance
        solver.post(ICF.sum(totalTravTime, "=", totalTTime));         //totalTTime

        solver.post(ICF.arithm(basicFleetcost,"=", 2350));
        IntVar[] finalCosts =ArrayUtils.append(totalVehicleKmEuros, penaltyStart, penaltyFinish,  penaltyExtraHours, new IntVar[]{basicFleetcost});
        solver.post(ICF.sum(finalCosts, "=", globalCost));

        /**

                solver.post(ICF.sum(ArrayUtils.append(totalVehicleKmEuros, new IntVar[]{basicFleetcost})));
        solver.post(IntConstraintFactory.scalar(ArrayUtils.append(open, costPerStore), coeffs, totCost));

        Constraint sum(IntVar[] VARS, IntVar SUM)

        _args = ArrayUtils.append(_args, new String[]{"-l", "SILENT"});*/



        IntVar[] vars = ArrayUtils.append(  ArrayUtils.flatten(next),
                ArrayUtils.flatten(servStart),
                //ArrayUtils.flatten(serve_qty),
                ArrayUtils.flatten(serve),
                ArrayUtils.flatten(edgesM_ij),
                capacityUsed, capacityUsed_100, totalVehicleDistance, totalVehicleTTime, totalVehicleKmEuros,
                new IntVar[]{totalCapacityUsed, totalDistance, totalTTime, globalCost});



        /** POST CIRCUIT CONSTRAINT */
        /* The scalar constraint to compute the amount of capacity that each vehicle perform in the path. With our model if a
		 * node isn't used then his next value is equals to his id. Then the boolean edges[k][i][i] is equals to true */

        /*A new table is created to have store the product of (Serve and Qty * 100)  */
        Constraint const_circuit1;
        serve_qty = VF.boundedMatrix("serve_qty", nbCustomers, nbVehicles, 0, VariableFactory.MAX_INT_BOUND, solver);
        for (int k = 0; k < nbVehicles; k++)
            for (int i = 0; i < nbCustomers; i++){
                const_circuit1 = ICF.times(serve[i][k], qty[i], serve_qty[i][k]);
                solver.post(const_circuit1);
                try {
                    solver.propagate();
                } catch (ContradictionException e) {
                    System.out.println("Serve and Qty  = ContradictionException " + e);
                    solver.getEngine().flush();
                    solver.unpost(const_circuit1);
                }
            }

        for (int k = 0; k < nbVehicles; k++){   //Capacity_used = Capacity_used100 / 100
            capacityUsed_100[k] = VariableFactory.bounded("vehicle " + k + " capacityUsed_100", 0, (vCap[k]*100), solver);
            solver.post(ICF.times(capacityUsed[k], 100, capacityUsed_100[k]));
            solver.post(ICF.sum(ArrayUtils.flattenSubMatrix(0, nbCustomers, k, 1, serve_qty), capacityUsed_100[k]));}

        /* The sub-circuit constraint. This forces all the next value to form a circuit which the overall size is equals
		 * to the size variable. This constraint check if the path contains any sub circles. */
        Constraint const_circuit;
        for (int k = 0; k < nbVehicles; k++){
            const_circuit = ICF.subcircuit(next[k], 0, size[k]);
            solver.post(const_circuit);
            try {
                solver.propagate();
            } catch (ContradictionException e) {
                System.out.println("POST CIRCUIT CONSTRAINT = ContradictionException " + e);
                solver.getEngine().flush();
                solver.unpost(const_circuit);
            }
        }

		/* The boolean channeling constraint. Enforce the relation between the next values and the edges values in the
		 * graph boolean variable matrix */
        Constraint const_channel;
        for (int k = 0; k < nbVehicles; k++){
            for (int i = 0; i < nbCustomers; i++){
                const_channel = ICF.boolean_channeling(edges[k][i], next[k][i], 0);
                solver.post(const_channel);
                try {
                    solver.propagate();
                } catch (ContradictionException e) {
                    System.out.println("boolean channeling constraint = ContradictionException " + e);
                    solver.getEngine().flush();
                    solver.unpost(const_channel);
                }
            }
        }

        /** STRONGER FILTERING */
		/* DISTANCE RELATED FILTERING * identifies the min/max distance involved by visiting each node  */

        Constraint const_stronger;
        IntVar[][] distanceAt = new IntVar[nbVehicles][nbCustomers-1];
        for(int k=0;k<nbVehicles;k++) {
            for (int i = 0; i < nbCustomers - 1; i++) {
                distanceAt[k][i] = VariableFactory.bounded("distanceAt" +k +"_"+ i, 0, VF.MAX_INT_BOUND, solver);
                const_stronger = ICF.element(distanceAt[k][i], costs[i], next[k][i], 0, "none");
                solver.post(const_stronger);
                try {
                    solver.propagate();
                } catch (ContradictionException e) {
                    System.out.println("STRONGER FILTERING = ContradictionException " + e);
                    solver.getEngine().flush();
                    solver.unpost(const_stronger);
                }
            }
        }
    }


    /************** C O N S T R A I N T S  **************/
    public void backUpConstraints(Solver solver) {


        /** Knapsack FILTERING
         /* This problem can be seen has a knapsack problem where are trying to found the set of edges that contains the
         * more golds and respects the fuel limit constraint. The analogy is the following : the weight is the
         * consumption to go through the edge and the energy is the gold that we can earn */
       /** qtyMatrix = new int[nbCustomers][nbCustomers];
        for (int i = 0; i < qtyMatrix.length; i++)
            for (int j = 0; j < qtyMatrix.length; j++) {
                qtyMatrix[i][j] = (i == j) ? 0 : qty[i];
                //System.out.println(qtyMatrix[i][j]);
            }
        for (int k = 0; k < nbVehicles; k++)
            solver.post(ICF.knapsack(ArrayUtils.flatten(edges[k]), maxDistance[k], capacityUsed[k],
                    ArrayUtils.flatten(costs), ArrayUtils.flatten(qtyMatrix)));   */


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

        /**
        /* Try catch constraints
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
        //	e.printStackTrace();
            solver.getEngine().flush();
            solver.unpost(test1);
            // busChoosen = null;
        }*/


        /**
         for(int i=0; i<idStrLst.size(); i++){
         switch (idStrLst.get(i)) {
         case "100017":  idStrLst.set(i,"100577"); System.out.printf("100017 -> 100577\n");break;
         case "100086":  idStrLst.set(i,"100101"); System.out.printf("100086 -> 100101\n");break;
         case "100102":  idStrLst.set(i,"207771"); System.out.printf("100102 -> 207771\n");break;
         case "100103":  idStrLst.set(i,"100175"); System.out.printf("100103 -> 100175\n");break;
         case "100166":  idStrLst.set(i,"100290"); System.out.printf("100166 -> 100290\n");break;
         case "100198":  idStrLst.set(i,"100290"); System.out.printf("100198 -> 100290\n");break;
         case "100273":  idStrLst.set(i,"100260"); System.out.printf("100273 -> 100260\n");break;
         case "100296":  idStrLst.set(i,"200033"); System.out.printf("100296 -> 200033\n");break;
         case "100374":  idStrLst.set(i,"100373"); System.out.printf("100374 -> 100373\n");break;
         case "100409":  idStrLst.set(i,"100375"); System.out.printf("100409 -> 100375\n");break;
         case "100413":  idStrLst.set(i,"100373"); System.out.printf("100413 -> 100373\n");break;
         case "100422":  idStrLst.set(i,"100149"); System.out.printf("100422 -> 100149\n");break;
         case "100499":  idStrLst.set(i,"100425"); System.out.printf("100499 -> 100425\n");break;
         case "100537":  idStrLst.set(i,"100581"); System.out.printf("100537 -> 100581\n");break;
         case "100562":  idStrLst.set(i,"100016"); System.out.printf("100562 -> 100016\n");break;
         case "100588":  idStrLst.set(i,"100248"); System.out.printf("100588 -> 100248\n");break;
         case "100604":  idStrLst.set(i,"200033"); System.out.printf("100604 -> 200033\n");break;
         case "100615":  idStrLst.set(i,"100016"); System.out.printf("100615 -> 100016\n");break;
         case "100619":  idStrLst.set(i,"100373"); System.out.printf("100619 -> 100373\n");break;
         case "100625":  idStrLst.set(i,"100245"); System.out.printf("100625 -> 100245\n");break;
         case "100630":  idStrLst.set(i,"100098"); System.out.printf("100630 -> 100098\n");break;
         case "100632":  idStrLst.set(i,"100290"); System.out.printf("100632 -> 100290\n");break;
         case "100633":  idStrLst.set(i,"100294"); System.out.printf("100633 -> 100294\n");break;
         case "100634":  idStrLst.set(i,"100290"); System.out.printf("100634 -> 100290\n");break;
         case "100635":  idStrLst.set(i,"100290"); System.out.printf("100635 -> 100290\n");break;
         case "100636":  idStrLst.set(i,"100259"); System.out.printf("100636 -> 100259\n");break;
         case "100656":  idStrLst.set(i,"100294"); System.out.printf("100656 -> 100294\n");break;
         case "100676":  idStrLst.set(i,"100674"); System.out.printf("100676 -> 100674\n");break;
         case "100677":  idStrLst.set(i,"100373"); System.out.printf("100677 -> 100373\n");break;
         case "100679":  idStrLst.set(i,"207775"); System.out.printf("100679 -> 207775\n");break;
         case "100694":  idStrLst.set(i,"100692"); System.out.printf("100694 -> 100692\n");break;
         case "100732":  idStrLst.set(i,"207786"); System.out.printf("100732 -> 207786\n");break;
         case "100733":  idStrLst.set(i,"100770"); System.out.printf("100733 -> 100770\n");break;
         case "100739":  idStrLst.set(i,"100577"); System.out.printf("100739 -> 100577\n");break;
         case "100756":  idStrLst.set(i,"100674"); System.out.printf("100756 -> 100674\n");break;
         default: System.out.printf(idStrLst.get(i)+"\n"); break;
         }
         } */

    }
}