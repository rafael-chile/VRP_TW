import org.chocosolver.samples.AbstractProblem;
import org.chocosolver.solver.ResolutionPolicy;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.ICF;
import org.chocosolver.solver.search.loop.monitors.IMonitorSolution;
import org.chocosolver.solver.search.strategy.IntStrategyFactory;
import org.chocosolver.solver.search.strategy.strategy.AbstractStrategy;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VariableFactory;
import org.chocosolver.util.tools.ArrayUtils;

import java.util.Random;

/**
 *
 * <h3>Path Constraint Problem</h3>
 *
 * <h2>The Problem</h2> The path problem a simple problem where we are trying to find the best path in a directed graph.
 * A path is a succession of node starting from the source node to the destination node. Each node has to be connected
 * to the previous and the next node by a a directed edge in the graph. The path can't admit any cycle </br>
 *
 * <h2>The Model</h2> This model will specify all the constraints to make a coherent path in the graph. The graph is both
 * represented by a BoolVar matrix and an array of IntVar (successor representation). For instance, if the boolean (i, j)
 * of the matrix is equals to true, that means that the path contains this edge starting from the node of id i to the node
 * of id j. If the edge doesn't exists in the graph the the boolean value is equals to the Choco constants FALSE.
 * Else the edge can be used or not, so its representation is a choco boolean variable which can be instantiated to
 * true : edge is in the path or to false : the edge isn't in the path.
 *
 * <h2>The Example</h2> We are presenting this problem through an little example of Mario's day. </br> Mario is an
 * Italian Plumber and is work is mainly to find gold in the plumbing of all the houses of the neighborhood. Mario is
 * moving in the city using his kart that has a specified amount of fuel. Mario starts his day of work from his personal
 * house and always end to his friend Luigi's house to have the supper. The problem here is to plan the best path for
 * Mario in order to earn the more money with the amount of fuel of his kart ! </p> (Version 1.3) We are making the
 * analogy of this problem to the knapsack problem. In fact we want to found a set of edges that form a path where Mario
 * can find the more gold and respects the fuel limit constraint. The analogy is the following :
 * <ul>
 * <li>The weight is the consumption to go through the edge</li>
 * <li>The energy is the gold that we can earn on the house at the end of the edge</li>
 * </ul>
 *
 * @author Amaury Ollagnier, Jean-Guillaume Fages
 * @since 21/05/2013
 */
@SuppressWarnings("unchecked")
public class MarioKart extends AbstractProblem {

    // CONSTANTS

    /** The number of house in the neighborhood of Mario : Size of the graph : Number of nodes */
    private static int SITES_NUMBER = 6;
    /** The Mario's house id. Random generation if equals to Integer.MAX_VALUE */
    private static int ID_DEPOT_START = 0;//Integer.MAX_VALUE;
    /** The Luigi's house id. Random generation if equals to Integer.MAX_VALUE */
    private static int ID_DEPOT_FINISH= 0;//Integer.MAX_VALUE;
    /** The amount of fuel of the kart in mini-litres */
    private static int FUEL = 100;
    /** The kart of mario */
    private static KART VEHICLE_1 = KART.ECOLO;

    // INSTANCES VARIABLES

    /** The dimension of the graph i.e. the number of nodes in the graph */
    private int n;
    /** The source node id */
    private int s;
    /** The destination node id */
    private int t;
    /** The matrix of consumptions the keeps the number of mini-litres needed to go to one house to another */
    private int[][] consumptions;

    /**
     * The boolean matrix represents all the edges in the graph, and if the boolean (i, j) of the matrix is equals to
     * true, that means that the path contains this edge starting from the node of id i to the node of id j. If the edge
     * doesn't exists in the graph the the boolean value is equals to the Choco constants FALSE. Else the edge can be
     * used or not, so it representation is a choco boolean variable which can be instantiated to true : edge is in the
     * path or to false : the edge isn't in the path.
     */
    private BoolVar[][] edges;

    /**
     * The next value table. The next variable of a node is the id of the next node in the path + an offset. If the node
     * isn't used, the next value is equals to the current node id + the offset
     */
    private IntVar[] next;

    /** Integer Variable which represents the overall size of the path founded */
    private IntVar size;

    /** The consumption of the Mario's Kart in the path */
    private IntVar fuelConsumed;

    // METHODS

    @Override
    public void createSolver() {
        solver = new Solver("Paths Finder");
    }

    @Override
    public void buildModel() {
        data();
        variables();
        constraints();
        strengthenFiltering();
    }

    @Override
    public void configureSearch() {
		/* Listeners */
        solver.plugMonitor(new IMonitorSolution() {
            private static final long serialVersionUID = 1L;
            @Override
            public void onSolution() {
                prettyOut();
            }
        });
		/* Heuristic choices */
        AbstractStrategy strat = IntStrategyFactory.minDom_LB(next);
        solver.set(IntStrategyFactory.lastConflict(solver,strat));
//		solver.set(strat);
    }

    @Override
    public void solve() {
        solver.findOptimalSolution(ResolutionPolicy.MAXIMIZE, fuelConsumed);
        printInputData();
    }

    @Override
    public void prettyOut() {
		/* log out the solution of the problem founded */
        System.out.println((int) ((size.getValue() + 0d) / (SITES_NUMBER + 0d) * 100) + " % of houses visited");
        System.out.println((int) ((fuelConsumed.getValue() + 0d) / (FUEL + 0d) * 100) + " % of fuel burned");
    }

    private void printInputData(){
        System.out.println("nbSites = "+ SITES_NUMBER  + ";");
        System.out.println("MarioHouse = "+ID_DEPOT_START+";");
        System.out.println("LuigiHouse = "+ID_DEPOT_FINISH+";");
        System.out.println("fuelMax = "+FUEL+";");
        String conso = "conso = [";
        for(int i=0;i<SITES_NUMBER ;i++){
            String s = "|";
            for(int j=0;j<SITES_NUMBER -1;j++){
                s+=this.consumptions[i][j]+",";
            }
            conso += s+this.consumptions[i][SITES_NUMBER -1];
        }
        conso+="|];";
        System.out.println(conso);

    }

    /** Creation of the problem instance */
    private void data() {
		/* Data of the town */
        int[][] distances = //TSP_Utils.generateRandomCosts(HOUSE_NUMBER, SEED, CITY_SIZE);
                // This matrix of distances takes as if locations are in a square distribution separated by 1 meter frm each other
                new int[][]{ /* B1,B2,B3,B4,B5,B6, */
                        /*A1*/ {0, 1, 2, 3, 4, 5},
                        /*A2*/ {1, 0, 1, 2, 3, 4},
                        /*A3*/ {2, 1, 0, 1, 2, 3},
                        /*A4*/ {3, 2, 1, 0, 1, 2},
                        /*A5*/ {4, 3, 2, 1, 0, 1},
                        /*A6*/ {5, 4, 3, 2, 1, 0},
                };
        consumptions = computeConsumptions(distances);

		/* Mario and Luigi houses */
        /*Random rd = new Random(SEED);
        if (ID_DEPOT_START == Integer.MAX_VALUE)
            ID_DEPOT_START = rd.nextInt(HOUSE_NUMBER - 1);
        if (ID_DEPOT_FINISH == Integer.MAX_VALUE) {
            ID_DEPOT_FINISH = rd.nextInt(HOUSE_NUMBER - 1);
            while (ID_DEPOT_FINISH == ID_DEPOT_START)
                ID_DEPOT_FINISH = rd.nextInt(HOUSE_NUMBER - 1);
        }*/

		/* Force some values */
        consumptions[ID_DEPOT_FINISH][ID_DEPOT_START] = 0;

		/* The basics variables of the graph */
        this.n = SITES_NUMBER ;
        this.s = ID_DEPOT_START;
        this.t = ID_DEPOT_FINISH;
    }

    /** Creation of CP variables */
    private void variables() {
		/* Choco variables */
        fuelConsumed = VariableFactory.bounded("Fuel Consumption", 0, FUEL, solver);
		/* Initialisation of the boolean matrix */
        edges = VariableFactory.boolMatrix("edges", n, n, solver);
		/* Initialisation of all the next value for each house */
        next = VariableFactory.enumeratedArray("next", n, 0, n - 1, solver);
		/* Initialisation of the size variable */
        size = VariableFactory.bounded("size", 2, n, solver);
    }

    /** Post all the constraints of the problem */
    private void constraints() {
		/* The scalar constraint to compute global consumption of the kart to perform the path */
        solver.post(ICF.scalar(ArrayUtils.flatten(edges), ArrayUtils.flatten(consumptions), fuelConsumed));

		/* The scalar constraint to compute the amount of gold founded by Mario in the path. With our model if a
		 * node isn't used then his next value is equals to his id. Then the boolean edges[i][i] is equals to true */
        BoolVar[] used = new BoolVar[n];
        for (int i = 0; i < used.length; i++)
            used[i] = edges[i][i].not();
       // solver.post(ICF.scalar(used, gold, goldFound));

		/* The subcircuit constraint. This forces all the next value to form a circuit which the overall size is equals
		 * to the size variable. This constraint check if the path contains any sub circles. */
        solver.post(ICF.subcircuit(next, 0, size));

		/* The path has to end on the t node. This constraint doesn't create a path, but a circle or a circuit. So we
		 * force the edge (t,s) then all the other node of the circuit will form a starting from s and ending at t */
        solver.post(ICF.arithm(next[t], "=", s));

		/* The boolean channeling constraint. Enforce the relation between the next values and the edges values in the
		 * graph boolean variable matrix */
        for (int i = 0; i < n; i++){
            solver.post(ICF.boolean_channeling(edges[i], next[i],0));
        }
    }

    /** Adds more constraints to get a stronger filtering */
    private void strengthenFiltering(){
		/* FUEL RELATED FILTERING:
		 * identifies the min/max fuel consumption involved by visiting each house */
        IntVar[] fuelHouse = new IntVar[SITES_NUMBER ];
        for(int i=0; i<SITES_NUMBER ;i++){
            fuelHouse[i] = VariableFactory.enumerated("fuelHouse",0,FUEL,solver);
            solver.post(ICF.element(fuelHouse[i],consumptions[i],next[i],0,"none"));
        }
        solver.post(ICF.sum(fuelHouse,fuelConsumed));
    }

    // SOME USEFUL METHODS

    /**
     * Compute the matrix of consumption from the matrix of distance regarding the kart of mario
     * @param distances matrix of int
     * @return a matrix of consumption
     */
    private static int[][] computeConsumptions(int[][] distances) {
        int[][] conso = new int[distances.length][distances.length];
        for (int i = 0; i < conso.length; i++)
            for (int j = 0; j < conso.length; j++)
                conso[i][j] = (int) (distances[i][j] * VEHICLE_1.getConsoMiniLitreByMeter());
        return conso;
    }


    // LAUNCHER

    /**
     * The main to execute
     * @param args arguments
     */
    public static void main(String[] args) {
        new MarioKart().execute(args);
    }

    // TOOLS FOR THE RESOLUTION

    /** The possibles type of kart */
    private enum KART {
        TRUNK(10),
        NORMAL(5),
        ECOLO(2);

        /** The consumption of the kart in litre / 100km */
        private double conso;

        /**
         * @param conso the consumption of the kart in litre / 100km
         * Return Build a kart with his specify conso
         */
        KART(double conso) {
            this.conso = conso;
        }

        /** @return the consumption in mini litre by meter */
        public double getConsoMiniLitreByMeter() {
            return conso / 1d;
        }
    }
}