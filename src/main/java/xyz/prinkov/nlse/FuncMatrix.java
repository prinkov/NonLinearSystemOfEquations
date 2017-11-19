package xyz.prinkov.nlse;

import org.ejml.simple.SimpleMatrix;
import org.mariuszgromada.math.mxparser.Function;

public class FuncMatrix {
    private Function[] functions;
    public FuncMatrix(Function[] functions) {
        this.functions = functions;
    }



    public SimpleMatrix calculate(Vector vals) {
        SimpleMatrix matrix = new SimpleMatrix(functions.length,
                1);
        for(int i = 0; i < functions.length; i++)
                matrix.set(i, 0, functions[i].calculate(vals.getValues()));

        return matrix;
    }

}
