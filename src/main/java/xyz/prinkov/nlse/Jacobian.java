package xyz.prinkov.nlse;

import org.ejml.simple.SimpleMatrix;
import org.mariuszgromada.math.mxparser.Function;

public class Jacobian {
    private Function[] functions;
    public Jacobian(Function[] functions) {
        this.functions = functions;
    }

    public SimpleMatrix evalJacobian(Vector vals) {
        SimpleMatrix matrix = new SimpleMatrix(functions.length,
                functions[0].getArgumentsNumber());

        for(int i = 0; i < functions.length; i++)
            for(int j = 0; j < functions[0].getArgumentsNumber(); j++) {
                matrix.set(i, j, derivative(functions[i], vals, j));
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
