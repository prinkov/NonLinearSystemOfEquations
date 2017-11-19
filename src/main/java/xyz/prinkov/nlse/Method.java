package xyz.prinkov.nlse;

import org.mariuszgromada.math.mxparser.Function;

public abstract class Method {
    public static double eps = 0.0001;
    public static double h = 0.05;
    public String title;
    public abstract double[][] compute(Vector x_0, Function[] nlse);
    public static  double[][] getArray(String answer) {
        String[] rows = answer.split(";");

        double[][] table = new double[rows.length][];
        for(int i = 0; i< rows.length; i++) {
            String[] row = rows[i].split(",");
            table[i] = new double[row.length];
            for(int j = 0; j < table[i].length; j++)
                table[i][j] = Double.parseDouble(row[j]);
        }
        return table;
    }
}
