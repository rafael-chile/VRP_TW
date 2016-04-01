import java.lang.Math;


public class polarCoords {
    public static void main(String[] args) {

    double rads, degs, tanA, coTanA;

    // Obtain angle in degrees from user
    degs = 120d;
    // Convert degrees to radian
    rads = Math.toRadians(degs);

    // Calculate tangent
    tanA = Math.tan(rads);
    System.out.println("Tangent = " + tanA);

    // Calculate cotangent
    coTanA = 1.0 / Math.tan(rads);
    System.out.println("Cotangent = " + coTanA);

    // Calculate arc-tangent
    rads = Math.atan(tanA);
    degs = Math.toDegrees(rads);
    System.out.println("Arc tangent: " + degs);

    // Calculate arc-cotangent
    rads = Math.atan(1 / coTanA);
    degs = Math.toDegrees(rads);
    System.out.println("Arc cotangent: " + degs);


    double Eps = 23.440;
    double RA = 312.5175;
    double EpsRad = Math.toRadians(Eps);
    double RARad = Math.toRadians(RA);
    double tmp1 = Math.tan(EpsRad);
    double tmp2 = Math.sin(RARad);
    double result = Math.atan(tmp1 * tmp2);

    System.out.println("in radians: " + result); //-0.30931302106018527
    System.out.println("in degrees: " + Math.toDegrees(result));//-17.722330655189765

    }
}
