import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.ICF;
import org.chocosolver.solver.trace.Chatterbox;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.VF;
// API:   Constraint[] tsp(IntVar[] SUCCS, IntVar COST, int[][] COST_MATRIX)

public class tsp {
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


