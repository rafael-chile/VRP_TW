import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.ICF;
import org.chocosolver.solver.trace.Chatterbox;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VF;
import org.chocosolver.solver.variables.VariableFactory;
import org.chocosolver.util.tools.ArrayUtils;
// API:   Constraint[] tsp(IntVar[] SUCCS, IntVar COST, int[][] COST_MATRIX)

public class tsp {

    public void test_sum(Solver solver) {
        IntVar[] VS = VariableFactory.enumeratedArray("VS", 4, new int[]{1, 3, 5, 7, 11, 13, 15}, solver);
        solver.post(ICF.sum(VS, "=", VariableFactory.fixed(30, solver)));
    }

    public void const1_test(Solver solver) {
        int nbCustomers = 5;
        IntVar[][] edges;
        edges = VariableFactory.enumeratedMatrix("edges", nbCustomers, nbCustomers, new int[]{0,1}, solver);
        solver.post(ICF.sum(ArrayUtils.flattenSubMatrix(0, 1, 1, nbCustomers - 1, edges), "=", VariableFactory.fixed(1, solver)));
        //starts in [3,0] and the length in x=1 and in y=4
        solver.post(ICF.sum(ArrayUtils.flattenSubMatrix(3, 1, 0, nbCustomers, edges), "=", VariableFactory.fixed(4, solver)));
    }
    public static void main(String [] arg){

        Solver solver = new Solver();
        IntVar[] VS = VF.enumeratedArray("VS", 4, 0, 4, solver);  // la variable puede tomar del 0 al 4
        IntVar CO = VF.enumerated("CO", 0, 15, solver);   // 15 is the maximum cost accepted
        int[][] costs = new int[][]{{0, 1, 3, 7},
                                    {1, 0, 1, 3},
                                    {3, 1, 0, 1},
                                    {7, 3, 1, 0}};
        solver.post(ICF.tsp(VS, CO, costs));
        System.out.println(solver);

        Chatterbox.printStatistics(solver);

        if(solver.findSolution()){
            do{
                System.out.println(solver.getSolutionRecorder().getLastSolution().toString(solver));
            }while(solver.nextSolution());
        }
    }
}


