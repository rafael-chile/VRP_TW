//github pass123
import com.vrptw.constraints.RouteSolver;
import org.chocosolver.solver.Solver;

import java.util.ArrayList;
import java.util.List;


public class RouteSolverTest {

    /************* C O N S T A N T S **************/


    private static int nbCustomers = 22;
    private static int nbVehicles = 1;
    private static int servTime = 2700;
    private static int[] qty = new int[] {0,3900,15895,26550,19960,9000,200,75,7950,1290,3250,6000,300,250,2000,60,680,2220,100,100,20,20,25};
    private static int[] vCap = new int[] {100000};
    private static int[][] costs = new int[][]
            {{    0,   14,   23,   42,   16,   41,   40,   32,   19,   19,    6,   16,   42,   42,   20,   39,   29,    1,   39,   37,   16,   21,   32},
                    {   14,    0,   11,   30,   21,   26,   28,   20,   26,   26,   10,   22,   49,   49,   42,   27,   35,   15,   26,   48,   21,   41,   20},
                    {   23,   11,    0,   21,   33,   27,   19,   11,   35,   35,   19,   31,   58,   58,   31,   18,   45,   24,   27,   47,   33,   30,   11},
                    {   42,   30,   21,    0,   46,   42,    1,   10,   54,   54,   38,   50,   89,   89,   44,    4,   63,   43,   42,   74,   46,   43,   11},
                    {   16,   21,   33,   45,    0,   63,   43,   35,   43,   43,   23,   39,   64,   64,    4,   42,   53,   17,   61,   20,    0,    5,   35},
                    {   40,   27,   28,   42,   63,    0,   41,   39,   29,   29,   38,   33,   23,   23,   73,   39,   20,   51,    2,   57,   63,   74,   40},
                    {   40,   28,   19,    1,   44,   40,    0,    8,   52,   52,   36,   48,   87,   87,   42,    2,   61,   41,   40,   72,   44,   41,    9},
                    {   32,   20,   11,   10,   35,   37,    8,    0,   43,   43,   27,   40,   79,   79,   34,    7,   53,   33,   37,   64,   35,   33,    1},
                    {   19,   26,   35,   54,   44,   37,   52,   44,    0,    0,   17,    9,   24,   24,   44,   50,   11,   20,   35,   28,   44,   44,   44},
                    {   19,   26,   35,   54,   44,   37,   52,   44,    0,    0,   17,    9,   24,   24,   44,   50,   11,   20,   35,   28,   44,   44,   44},
                    {    6,   10,   19,   38,   23,   39,   36,   27,   17,   17,    0,   14,   40,   40,   27,   34,   27,    6,   37,   40,   23,   27,   28},
                    {   16,   22,   31,   50,   40,   33,   48,   40,    9,    9,   14,    0,   35,   35,   35,   47,   20,   16,   31,   19,   40,   35,   40},
                    {   42,   49,   58,   88,   65,   25,   86,   78,   25,   25,   41,   35,    0,    0,   76,   85,   15,   53,   23,   59,   65,   76,   78},
                    {   42,   49,   58,   88,   65,   25,   86,   78,   25,   25,   41,   35,    0,    0,   76,   85,   15,   53,   23,   59,   65,   76,   78},
                    {   20,   42,   31,   44,    4,   74,   42,   34,   44,   44,   27,   35,   75,   75,    0,   40,   55,   21,   72,   16,    4,    1,   34},
                    {   39,   27,   18,    4,   42,   38,    2,    7,   50,   50,   34,   47,   85,   85,   40,    0,   60,   39,   38,   71,   42,   40,    7},
                    {   29,   35,   44,   63,   54,   21,   61,   53,   11,   11,   27,   20,   14,   14,   55,   60,    0,   29,   19,   38,   54,   55,   53},
                    {    1,   15,   24,   43,   17,   51,   41,   33,   20,   20,    6,   16,   53,   53,   21,   39,   30,    0,   49,   38,   17,   21,   33},
                    {   40,   26,   27,   42,   62,    2,   40,   39,   29,   29,   38,   32,   23,   23,   73,   38,   19,   51,    0,   57,   62,   73,   39},
                    {   37,   48,   48,   73,   20,   57,   71,   63,   28,   28,   40,   19,   59,   59,   16,   70,   39,   38,   56,    0,   20,   17,   63},
                    {   16,   21,   33,   45,    0,   63,   43,   35,   43,   43,   23,   39,   64,   64,    4,   42,   53,   17,   61,   20,    0,    5,   35},
                    {   21,   41,   30,   43,    4,   74,   41,   33,   44,   44,   27,   35,   76,   76,    0,   39,   55,   21,   72,   17,    4,    0,   33},
                    {   32,   20,   11,   11,   36,   37,    9,    1,   44,   44,   28,   40,   79,   79,   34,    7,   53,   33,   37,   64,   36,   33,    0}};
    private static int[][] travTime = new int[][]
            {{    0,  941, 1428, 1918, 1291, 2256, 1859, 1375, 1290, 1290,  557, 1208, 2122, 2122, 1597, 1738, 1809,  104, 2112, 1606, 1291, 1687, 1417},
                    {  992,    0,  715, 1611, 1866, 2001, 1551, 1068, 1378, 1378,  570, 1296, 2209, 2209, 2063, 1430, 1897, 1047, 2004, 2233, 1866, 2080, 1109},
                    { 1452,  696,    0, 1230, 1748, 2064, 1171,  687, 1838, 1838, 1030, 1756, 2669, 2669, 1561, 1050, 2357, 1507, 2066, 2326, 1748, 1578,  728},
                    { 1916, 1588, 1234,    0, 2414, 2674,   62,  613, 2729, 2729, 1922, 2642, 3552, 3552, 2228,  274, 3248, 1965, 2677, 3019, 2414, 2244,  645},
                    { 1287, 1848, 1768, 2415,    0, 3153, 2355, 1872, 2284, 2284, 1824, 2200, 3019, 3019,  304, 2234, 2853, 1336, 3009, 1069,    0,  394, 1913},
                    { 2218, 2053, 2143, 2761, 3143,    0, 2701, 2324, 1605, 1605, 1886, 1650, 1288, 1288, 3237, 2580, 1159, 2289,  172, 2456, 3143, 3299, 2366},
                    { 1844, 1518, 1165,   61, 2342, 2605,    0,  543, 2660, 2660, 1851, 2577, 3482, 3482, 2158,  217, 3179, 1896, 2607, 2950, 2342, 2175,  576},
                    { 1375, 1046,  693,  605, 1873, 2161,  546,    0, 2188, 2188, 1380, 2104, 3010, 3010, 1686,  425, 2707, 1427, 2164, 2478, 1873, 1703,  104},
                    { 1278, 1335, 1822, 2718, 2347, 1772, 2659, 2175,    0,    0,  946,  594, 1382, 1382, 2083, 2537,  583, 1353, 1628, 1303, 2347, 2145, 2216},
                    { 1278, 1335, 1822, 2718, 2347, 1772, 2659, 2175,    0,    0,  946,  594, 1382, 1382, 2083, 2537,  583, 1353, 1628, 1303, 2347, 2145, 2216},
                    {  566,  538, 1024, 1920, 1820, 1911, 1860, 1377,  946,  946,    0,  864, 1777, 1777, 2126, 1739, 1465,  618, 1767, 1801, 1820, 2216, 1418},
                    { 1225, 1282, 1769, 2666, 2296, 1719, 2606, 2122,  633,  633,  893,    0, 1586, 1586, 2312, 2485, 1202, 1300, 1575, 1531, 2296, 2374, 2164},
                    { 2129, 2186, 2673, 3524, 3055, 1398, 3464, 2981, 1356, 1356, 1797, 1560,    0,    0, 3148, 3343,  889, 2197, 1254, 2368, 3055, 3210, 3022},
                    { 2129, 2186, 2673, 3524, 3055, 1398, 3464, 2981, 1356, 1356, 1797, 1560,    0,    0, 3148, 3343,  889, 2197, 1254, 2368, 3055, 3210, 3022},
                    { 1591, 2033, 1568, 2215,  301, 3275, 2155, 1672, 2060, 2060, 2128, 2269, 3141, 3141,    0, 2034, 2644, 1640, 3131,  764,  301,   90, 1713},
                    { 1720, 1391, 1038,  239, 2218, 2477,  180,  416, 2533, 2533, 1725, 2450, 3355, 3355, 2031,    0, 3052, 1769, 2480, 2823, 2218, 2048,  449},
                    { 1806, 1864, 2350, 3247, 2926, 1278, 3188, 2704,  584,  584, 1475, 1173,  916,  916, 2673, 3066,    0, 1882, 1135, 1893, 2926, 2735, 2745},
                    {  101,  993, 1480, 1974, 1347, 2328, 1915, 1431, 1372, 1372,  610, 1291, 2194, 2194, 1653, 1793, 1891,    0, 2184, 1661, 1347, 1743, 1472},
                    { 2166, 1963, 2053, 2670, 3091,  143, 2611, 2234, 1553, 1553, 1834, 1598, 1236, 1236, 3185, 2490, 1107, 2233,    0, 2405, 3091, 3247, 2275},
                    { 1593, 2185, 2348, 2969, 1081, 2509, 2910, 2426, 1295, 1295, 1796, 1506, 2376, 2376,  780, 2788, 1880, 1642, 2366,    0, 1081,  842, 2467},
                    { 1287, 1848, 1768, 2415,    0, 3153, 2355, 1872, 2284, 2284, 1824, 2200, 3019, 3019,  304, 2234, 2853, 1336, 3009, 1069,    0,  394, 1913},
                    { 1671, 2049, 1582, 2229,  378, 3344, 2170, 1686, 2130, 2130, 2208, 2331, 3210, 3210,   79, 2048, 2714, 1720, 3200,  834,  378,    0, 1727},
                    { 1405, 1079,  725,  647, 1903, 2193,  587,  103, 2220, 2220, 1412, 2138, 3042, 3042, 1719,  466, 2739, 1456, 2196, 2510, 1903, 1735,    0}};
    private static int[][] tWin = new int[][]
            {{28800, 86400},{28800, 86400},{28800, 86400},{28800, 86400},{28800, 86400},{28800, 86400},{28800, 86400},{28800, 86400},{28800, 86400},{28800, 86400},{28800, 86400},{28800, 86400},{28800, 86400},{28800, 86400},{28800, 86400},{28800, 86400},{28800, 86400},{28800, 86400},{28800, 86400},{28800, 86400},{28800, 86400},{28800, 86400},{28800, 86400}};

    /**********************************************/

    private static int[][] breakTime;
    private static int[][] M_ij;                       /* Constant for equation 6: M_ij = l_i + t_ij - e_j    */
    private static int[][] stM_ij;                       /* Constant for equation 6: M_ij = l_i + t_ij - e_j    */


    /**-------------INSTANCE END--------------*/

    private static int max_cap_big = 1000;       /* Max vehicle capacity: Biggest truck (auxiliary variable) */
    private static int max_cap = 100;             /* Max vehicle capacity: Biggest truck  */
                                            /* Homogeneous and limited fleet that leave and return to the depot
                                            R[i] = {r[i](1),...,r[i](n[i])} denote the route for vehicle 'i',
                                            where r[i](j) is the index of the jth customer visited and n[i] is the nb. of customers in the route.
                                            r[i](n[i] + 1) = 0, every route finishes at the depot  */
    private static int vclCapacity = 100;          // a[k] is total vehicle capacity, of each vehicle k ∈ V

                                    /* Split deliveries: the demand of a customer may be fulfilled by more than one vehicle.
                                        This occurs in all cases where some demand exceeds the vehicle capacity */

    private static RouteSolver testWithLocalTestData(){
        return new RouteSolver(nbCustomers, qty, nbVehicles, vCap, max_cap_big,  max_cap, vclCapacity,
                costs, servTime, travTime, breakTime, tWin, M_ij, stM_ij);
    }

    private static RouteSolver testWithDBTestData(){
        // TODO: get data from db and set it into the parameters; then do : return new RouteSolver(...);
        /*
        RouteCostDao rcDao = new RouteCostDao();
        rcDao.getDistanceCostMatrix(...);
        nbCustomers = ;
        qty = ;
        nbVehicles = ;
        vCap = ;
        max_cap_big = ;
        max_cap = ;
        vclCapacity = ;
        costs = ;
        servTime = ;
        travTime = ;
        tWin = ;
        M_ij = ;
        stM_ij = ;
        */
        return new RouteSolver(nbCustomers, qty, nbVehicles, vCap, max_cap_big,  max_cap, vclCapacity,
                costs, servTime, travTime, breakTime, tWin, M_ij, stM_ij);
    }

    /** Run  M A I N  A P P  F O R T E S T */

    public static void main(String [] arg){

        RouteSolver rs = RouteSolverTest.testWithLocalTestData();
        // 1. Create a Solver
        Solver solver = new Solver();

        // 2. Constraints
        rs.addRouteSolvingConstraints(solver);

        // 3. Print Input
        rs.printInput(solver);

        // 4. Find Solution
        rs.findSol(solver);

        // 5. Print Output
        rs.printOutput(solver);

    }
}
