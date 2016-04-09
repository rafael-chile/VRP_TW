import com.vrptw.constraints.RouteSolver;
import org.chocosolver.solver.Solver;

import java.lang.Math;
import java.text.DecimalFormat;


public class polarCoords {


    public void GetPolar(double x, double y){
        DecimalFormat df = new DecimalFormat("#.#######");
        double centerX = -8.628569;
        double centerY = 39.213442;
        // Move the center to borrego
        x = x - centerX; y = y - centerY;
        // Cartesian to polar.
        double radius = Math.sqrt( x * x + y * y );
        double angleInDegrees = Math.toDegrees(Math.acos(x / radius));
        if(y < 0) angleInDegrees = 360 - angleInDegrees;
        System.out.println("x = " + df.format(x)+"   y = " + df.format(y)+ "   radius = " + df.format(radius)+"   angleInDegrees = " + angleInDegrees);
    }
    /**1. Cargar lista de clientes a procesar (la semana completa)
        2. Agregar a la case Client el atributo Angulo
        3. Procesar la lista para calcular el angulo a cada cliente: metodo "GetPolar"
        4. Ordenar una lista de elementos Client con respecto al angulo (CompareTo)
        5. Subdividir la lista Lista.size/
        var batchSize = lista.size() / 6;
        Mon = lista.sublist(0, batchSize)
        Tue = lista.sublist(batchSize+1, batchSize*2); */

    public static void main(String[] args) {
        //NE GPS = 39.214475, -8.626998
                /*x = 0,001571    y = 0,001033      radius = 0,0018802    angleInDegrees = 33.326638483753214*/
               // double x = -8.626998; double y = 39.214475;

        //NW GPS = 39.213749, -8.630076
               /*x = -0,001507       y = 0,000307     radius = 0,001538      angleInDegrees = 168.48549279902392*/
               // double x = -8.630076; double y = 39.213749;

        //SE GPS = 39.212413, -8.626664
                /* x = 0,001905    y = -0,001029   radius = 0,0021651    angleInDegrees = 28.376031709344314  */
                //double x = -8.626664; double y = 39.212413;

        //SW GPS = 39.212338, -8.630526
                /* x = -0,001957   y = -0,001104    radius = 0,0022469   angleInDegrees = 150.571404640278  */
                double x = -8.630526; double y = 39.212338;

        polarCoords test = new polarCoords();
        test.GetPolar(x, y);
    }
}
