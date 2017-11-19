package xyz.prinkov.nlse;

import org.mariuszgromada.math.mxparser.Function;

public class Test {

    public static void main(String[] args) {
        Function[] nlse = new Function[]{
                new Function("f(x,y,z) = 3*x^2+7*y*z"),
                new Function("f(x,y,z) = x^2+7*y*z"),
                new Function("f(x,y,z) = 2*cos(y)")
        };

        Jacobian jacob = new Jacobian(nlse);
        Vector x_0 = new Vector(new double[] {-6, 11, 2.5});
        System.out.println(jacob.evalJacobian(x_0));

        Jacobian.setSymbolComputing(true);
        System.out.println(jacob.evalJacobian(x_0));

        Jacobian.symbolDerivative = new Function[]{
                new Function("f(x,y,z) = 6*x"),
                new Function("f(x,y,z) = 7*z"),
                new Function("f(x,y,z) = 7*y"),
                new Function("f(x,y,z) = 2*x"),
                new Function("f(x,y,z) = 7*z"),
                new Function("f(x,y,z) = 7*y"),
                new Function("f(x,y,z) = 0"),
                new Function("f(x,y,z) = -2*sin(y)"),
                new Function("f(x,y,z) = 0"),
        };

        jacob.evalJacobian(x_0);
        System.out.println(jacob.evalJacobian(x_0));
    }

}
