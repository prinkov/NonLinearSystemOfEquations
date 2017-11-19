package xyz.prinkov.nlse;

import org.ejml.simple.SimpleMatrix;
import org.mariuszgromada.math.mxparser.Function;

public class Jacobian {
    private Function[] functions;
    public static String title = "Численный ";
    public static  Function[] symbolDerivative;

    private static boolean symbolComputing = false;

    public static boolean isSymbolComputing() {
        return symbolComputing;
    }

    public static void setSymbolComputing(boolean val) {
        if(val)
            title = "Аналитический ";
        else
            title = "Численный ";
        symbolComputing = val;

    }

    public Jacobian(Function[] functions) {
        this.functions = functions;
    }

    public SimpleMatrix evalJacobian(Vector vals) {
        SimpleMatrix matrix = new SimpleMatrix(functions.length,
                functions[0].getArgumentsNumber());
        int argNum = functions[0].getArgumentsNumber();
        for(int i = 0; i < functions.length; i++)
            for(int j = 0; j < argNum; j++) {
                if(symbolComputing) {
                    if(symbolDerivative == null)
                        matrix.set(i, j,0);
                    else {
                        matrix.set(i, j, symbolDerivative[i * argNum + j].calculate(vals.getValues()));
                    }
                } else {
                    matrix.set(i, j, derivative(functions[i], vals, j));
                }
        }

        return matrix;
    }


    private double derivative(Function f, Vector doth, int i) {
        Vector prev = new Vector(doth);
        Vector next = new Vector(doth);
        prev = prev.add(i, -Method.h);
        next= next.add(i, Method.h);

        return (f.calculate(next.getValues()) -
                f.calculate(prev.getValues())) /
                (2.0 * Method.h);
    }

}
