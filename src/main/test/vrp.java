
import org.chocosolver.samples.*;
import org.chocosolver.solver.ResolutionPolicy;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.constraints.ICF;
import org.chocosolver.solver.constraints.IntConstraintFactory;
import org.chocosolver.solver.constraints.LogicalConstraintFactory;
import org.chocosolver.solver.search.strategy.IntStrategyFactory;
import org.chocosolver.solver.search.strategy.strategy.AbstractStrategy;
import org.chocosolver.solver.trace.Chatterbox;
import org.chocosolver.solver.variables.BoolVar;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VF;
import org.chocosolver.solver.variables.VariableFactory;
import org.chocosolver.util.tools.ArrayUtils;

import java.util.Arrays;
import java.util.Random;

import static org.chocosolver.solver.constraints.extension.TuplesFactory.allDifferent;
import static org.chocosolver.solver.variables.VariableFactory.bool;

public class vrp {

    public static void main(String [] arg){

        /** 1. S O L V E R  */
        Solver solver = new Solver("VRP Solver");

        /** 2. C O N S T A N T S  */
        int nbCustomers = 5;        /* number of customers/vertices
                                        Customers: N = {1, 2,.., n}, 'n' different locations
                                        Every pair of locations (i, j) , where i, j ∈ N and i ≠ j,
                                        is associated with a travel time t[i][j] and a distance traveled d[i][j]
                                        q[i] is the demand at point 'i'. Depot is denoted by 0. */

        int nbVehicles = 2;         /* total vehicle fleet
                                        Fleet of vehicles: V = {1,...,m} with identical capacities.
                                        Homogeneous and limited fleet that leave and return to the depot
                                        R[i] = {r[i](1),...,r[i](n[i])} denote the route for vehicle 'i',
                                        where r[i](j) is the index of the jth customer visited and n[i] is the nb. of customers in the route.
                                        r[i](n[i] + 1) = 0, every route finishes at the depot  */
        int vclCapacity = 100;          // a[k] is total vehicle capacity, of each vehicle k ∈ V

                                    /* Split deliveries: the demand of a customer may be fulfilled by more than one vehicle.
                                        This occurs in all cases where some demand exceeds the vehicle capacity */

        int[][] costs = new int[][]     /* costs in time ALL to ALL customers */        //consumptions
                {   {0, 2223, 1272, 1931, 2047, 1597},
                        {2211, 0, 3226, 1202, 1138, 1755},
                        {1291, 3243, 0, 3077, 3190, 2740},
                        {1921, 1207, 3053, 0, 737, 1075},
                        {1981, 1083, 3117, 674, 0, 1132},
                        {1580, 1741, 2716, 1052, 1167, 0},};

        /** 3. D E C I S I O N   V A R I A B L E S (Variable factory)  */
        /* represents all paths all vehicles
            for each vehicle, 1 boolean matrix. x[k][i][j] True if vehicle vclK visits the client 'j' directly after 'i', False otherwise */
        IntVar[][][] edges = new IntVar[nbVehicles][nbCustomers][nbCustomers];
        for (int i = 0; i < nbVehicles; i++) {
            edges[i] = VariableFactory.enumeratedMatrix("truck_" + i, nbCustomers, nbCustomers, new int[]{0,1}, solver);
        }

        /* b[k][i] is the moment(time seconds) at which service begins at customer 'i'(1,..,n) by vehicle 'k'(1,..,m)  */
        IntVar[][] startTime = VF.enumeratedMatrix("startTime", nbVehicles, nbCustomers, 0, 32400, solver);      // in seconds 0 - 9 hours

        /* true if client 'i' is served by vehicle vclK, false otherwise */
        IntVar[][] serve = VF.enumeratedMatrix("serve", nbCustomers, nbCustomers, new int[]{0, 1},  solver);

        /* y[k][i] is the fraction of customer’s demand i delivered by vehicle 'k'  */
        IntVar[][] fraction = VF.enumeratedMatrix("fraction", nbCustomers, nbCustomers, new int[]{0, 1},  solver);

        /** 4. C O N S T R A I N T S   */
        // Constraint (1): each vehicle will leave the depot and arrive at a determined customer
        for (int k = 0; k < nbVehicles; k++) {
            solver.post(ICF.sum(ArrayUtils.flattenSubMatrix(0, 0, 1, nbCustomers, edges[k]), "=", VariableFactory.fixed(1, solver)));
        }

        // Constraint (2): entrance and exit flows, guarantees that each vehicle will leave a customer and arrive to the depot
        IntVar[] aux = VariableFactory.enumeratedArray("aux", nbCustomers, 0, nbCustomers, solver);
        for (int k = 0; k < nbVehicles; k++) {
            for (int p = 0; p < nbCustomers; p++) {
                System.out.println("p=" + p + ", aux=" + aux[p]+ ",k=" + k + ",edge" + edges[k]);
                System.out.println(ArrayUtils.flattenSubMatrix(0, nbCustomers-1, 3, 4, edges[k]));
                //solver.post(ICF.sum(ArrayUtils.flattenSubMatrix(0, nbCustomers, p, p, edges[k]), "=", aux[p]));
                //solver.post(ICF.sum(ArrayUtils.flattenSubMatrix(p, p, 0, nbCustomers-1, edges[k]), "=", aux[p]));
            }
        }

        // Constraint (3): the total demand of each customer will be fulfilled
        for (int k = 0; k < nbVehicles; k++) {
            for (int i = 0; i < nbCustomers; i++) {
                solver.post(IntConstraintFactory.arithm(serve[k][i], "=", 1));
            }
        }

        // Constraint (4): the vehicle capacity will not be exceed

        // Constraint (5): the demand of each customer will only be fulfilled if a determined vehicle goes by that place
            // (We can notice that, adding to constraint (5) the sum of all vehicles and combining to equation (3) we have the constraint ____
            // which guarantees that each vertex will be visited at least once by at least one vehicle)

        // Equation (6):minimum time for beginning the service of customer j in a determined route
            // also guarantees that there will be no sub tours. The constant M_ij  is a large enough number

        // Constraint(7): all customers will be served within their time windows

        // Equation (8): the decision variables y[k][i] and b[k][i] are positive.

        // Equation (9): the decision variables x[k][i][j] is binary.

        /** 5. S E A R C H   S T R A T E G Y   */
        //solver.set(IntStrategyFactory.lexico_LB(new IntVar[]{x, y}));
        //AbstractStrategy strat = IntStrategyFactory.minDom_LB(next);
        //solver.set(IntStrategyFactory.lastConflict(solver,strat));

        /** 6. L A U N C H   S O L V E R  -  O B J E C T I V E   F U N C T I O N
         * Minimize the total cost of the route         */
        //solver.findOptimalSolution(ResolutionPolicy.MINIMIZE, totCost);
        solver.findSolution();

        /** 7. P R I N T   S T A T I S T I C S   */
        Chatterbox.printStatistics(solver);
        Chatterbox.showSolutions(solver);
    }

}
