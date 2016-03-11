import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.ICF;
import org.chocosolver.solver.search.limits.FailCounter;
import org.chocosolver.solver.search.loop.lns.LNSFactory;
import org.chocosolver.solver.search.solution.Solution;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VF;


// API:   Constraint[] tsp(IntVar[] SUCCS, IntVar COST, int[][] COST_MATRIX)

public class tsp {
    public static void main(String [] arg){

        Solver solver = new Solver();
        IntVar[] VS = VF.enumeratedArray("VS", 4, 0, 4, solver);
        IntVar CO = VF.enumerated("CO", 0, 15, solver);
        int[][] costs = new int[][]{{0, 1, 3, 7},
                                    {1, 0, 1, 3},
                                    {3, 1, 0, 1},
                                    {7, 3, 1, 0}};
        solver.post(ICF.tsp(VS, CO, costs));
        solver.findAllSolutions();

        //Solution[] solutions = solver.findAllSolutions();
        //for (int i = 0; i < solutions.length; i++) {
        //    solutions[i].log();
        //    System.out.println();
        //}

        // Large Neighborhood Search
        //LNSFactory.rlns(solver, ivars, 30, 20140909L, new FailCounter(solver, 100));
        //solver.findOptimalSolution(ResolutionPolicy.MINIMIZE, objective);
        /*

        int num_sols = 0;
        Solution[] res = solver.findAllSolutions();
        for(int k = 0; k < res.length; k++) {
            num_sols++;
            Solution s = res[k];
            System.out.println("\nSolution #" + num_sols);

            System.out.print("x (decimal values):");
            for (int i = 0; i < m; i++) {
                System.out.print(s.getValue("x-"+i) + " ");
            }

            System.out.print("\nde Bruijn sequence: ");
            for(int i = 0; i < m; i++) {
                System.out.print(s.getValue("bin_code-"+i) + " ");
            }

            System.out.println("\nbinary:");
            for(int i = 0; i < m; i++) {
                for(int j = 0; j < n; j++) {
                    System.out.print(s.getValue("binary"+i+"-"+j) + " ");
                }
                System.out.println(": " + s.getValue("x-"+i));
            }

            p.log("\n");

            // check number of solutions
            p.log("num_solutions: " + num_solutions + " num_sols: " + num_sols);
            if (num_solutions > 0 && num_sols >= num_solutions) {
                break;
            }

        }*/


    }
}



/*
The solutions of the problem are :
• VS[0] = 2, VS[1] = 0, VS[2] = 3, VS[3] = 1, CO = 8
• VS[0] = 3, VS[1] = 0, VS[2] = 1, VS[3] = 2, CO = 10
• VS[0] = 1, VS[1] = 2, VS[2] = 3, VS[3] = 0, CO = 10
• VS[0] = 3, VS[1] = 2, VS[2] = 0, VS[3] = 1, CO = 14
• VS[0] = 1, VS[1] = 3, VS[2] = 0, VS[3] = 2, CO = 8
• VS[0] = 2, VS[1] = 3, VS[2] = 1, VS[3] = 0, CO = 14
 */