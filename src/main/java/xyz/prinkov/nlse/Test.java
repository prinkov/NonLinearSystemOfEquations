package xyz.prinkov.nlse;

import org.mariuszgromada.math.mxparser.Function;

import java.util.Arrays;

public class Test {

    public static void main(String[] args) {
        Function[] nlse = new Function[]{
                new Function("f(x_0,x_1,x_2) = (x_0-2)^2+(x_1-1)^2+(x_2-3)^2-50^2"),
                new Function("f(x_0,x_1,x_2) = 2*x_0 + x_1 + 3*x_2+5"),
                new Function("f(x_0,x_1,x_2) = 3*x_0 + 5*x_1+3*x_2+2")
        };

        Jacobian jacob = new Jacobian(nlse);
        Vector x_0 = new Vector(new double[] {-6, 11, 2.5});

//        Jacobian.setSymbolComputing(true);

        Jacobian.symbolDerivative = new Function[]{
                new Function("f(x_0,x_1,x_2)= 2*(x_0-2)"),
                new Function("f(x_0,x_1,x_2)= 2*(x_1-1)"),
                new Function("f(x_0,x_1,x_2)= 2*(x_2-3)"),
                new Function("f(x_0,x_1,x_2)=2"),
                new Function("f(x_0,x_1,x_2)=1"),
                new Function("f(x_0,x_1,x_2)=3"),
                new Function("f(x_0,x_1,x_2)=3"),
                new Function("f(x_0,x_1,x_2)=5"),
                new Function("f(x_0,x_1,x_2)=3"),
        };

        Newton br = new Newton();
        double[][] answer = br.compute(new Vector(new double[]{2, -0.5, 2}), nlse);
        System.out.println(Arrays.toString(answer[answer.length-1]));
    }

}
