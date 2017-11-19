package xyz.prinkov.nlse;

import org.ejml.simple.SimpleMatrix;
import org.mariuszgromada.math.mxparser.Function;

public class Broiden extends Method {

    public Broiden() {
        title = "метод Бройдена";
    }

    @Override
    public double[][] compute(Vector x_0, Function[] nlse) {

        StringBuffer answer = new StringBuffer();

        Vector xprev;
        Vector xnext = new Vector(x_0);
        Jacobian jacob = new Jacobian(nlse);
        FuncMatrix F = new FuncMatrix(nlse);
        SimpleMatrix aprev;
        if(Jacobian.isSymbolComputing())
            if(jacob.symbolDerivative == null)
                return new double[5][5];
        SimpleMatrix anext = jacob.evalJacobian(xnext);

        while (true) {
            xprev = new Vector(xnext);
            aprev = new SimpleMatrix(anext);
            SimpleMatrix sk = aprev.pseudoInverse().mult(F.calculate(xprev)).scale(-1);
            xnext = new Vector(
                    new SimpleMatrix(new double[][]{xprev.values}).transpose().
                            plus(sk).getMatrix().data);
            if (sk.elementMaxAbs() < eps)
                return getArray(answer.toString());
            SimpleMatrix yk = F.calculate(xnext).minus(F.calculate(xprev));
            anext = aprev.plus(((yk.minus(aprev.mult(sk))).
                    mult(sk.transpose())).
                    scale(1.0 / (sk.transpose().
                            mult(sk).elementSum())));
            answer.append(xnext);
        }
    }
}
