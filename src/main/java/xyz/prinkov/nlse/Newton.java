package xyz.prinkov.nlse;

import org.ejml.simple.SimpleMatrix;
import org.mariuszgromada.math.mxparser.Function;

class Newton extends Method {
    public Newton() {
        title = "метод Ньютона";
    }

    @Override
    public double[][] compute(Vector x_0, Function[] nlse) {

        StringBuffer answer = new StringBuffer();

        Vector xprev;
        Vector xnext = new Vector(x_0);
        Jacobian jacob = new Jacobian(nlse);
        FuncMatrix F = new FuncMatrix(nlse);
        if(Jacobian.isSymbolComputing())
            if(jacob.symbolDerivative == null)
                return new double[5][5];
        do {
            xprev = new Vector(xnext);
            xnext = new Vector(
                    new SimpleMatrix(new double[][]{xprev.values}).transpose().
                    minus((jacob.evalJacobian(xprev).pseudoInverse()).
                    mult(F.calculate(xprev))).getMatrix().data);
            answer.append(xnext);
        } while(!xnext.plus((xprev).mult(-1)).isLess(eps));

        return getArray(answer.toString());
    }
}
