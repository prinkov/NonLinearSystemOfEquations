package xyz.prinkov.nlse;

import javafx.application.Application;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.ejml.data.DenseMatrix64F;
import org.ejml.simple.SimpleMatrix;
import org.mariuszgromada.math.mxparser.Function;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Method[] methods = new Method[2];

        methods[0] = new Newton();
        methods[1] = new Broiden();

        Button startBtn = new Button("Посчитать!");
        Button enterJacobian = new Button("Ввести якобиан");
        Button addBtn = new Button("+");
        Button removeBtn = new Button("-");

        Font mainFont = Font.font("Monaco", FontWeight.BOLD, 16);
        startBtn.setFont(mainFont);
        addBtn.setFont(mainFont);
        enterJacobian.setFont(mainFont);
        removeBtn.setFont(mainFont);


        ArrayList<MyTextField> tFlds = new ArrayList<>();
        tFlds.add(new MyTextField());
        tFlds.get(tFlds.size()-1).setText("f(x,y,z) = 3*x^2+7*y*z");
        tFlds.add(new MyTextField());
        tFlds.get(tFlds.size()-1).setText("f(x,y,z) = x^2+7*y*z");
        tFlds.add(new MyTextField());
        tFlds.get(tFlds.size()-1).setText("f(x,y,z) = 2*cos(y)");

        ScrollPane paneForFields = new ScrollPane();
        HBox hbForSc = new HBox();
        Text bracket = new Text("{");
        bracket.setFont(Font.font("Areal", FontWeight.LIGHT, 180));
        hbForSc.setFillHeight(true);
        paneForFields.setFitToHeight(true);
        MyTextField epsMethod = new MyTextField();
        MyTextField stepDiff = new MyTextField();
        MyTextField doth = new MyTextField();
        epsMethod.setText("0.001");
        stepDiff.setText("0.05");
        hbForSc.getChildren().addAll(bracket, paneForFields);
        hbForSc.setAlignment(Pos.CENTER);
        paneForFields.setFitToWidth(true);
        VBox boxForFields = new VBox();
        boxForFields.getChildren().addAll(tFlds);
        boxForFields.setSpacing(5);
        boxForFields.setAlignment(Pos.CENTER);
        paneForFields.setContent(boxForFields);
        ProgressBar indic = new ProgressBar();
        VBox vbox = new VBox();

        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(5,5,5,5));
        vbox.getChildren().addAll(hbForSc);
        HBox boxForAddBtn = new HBox();
        boxForAddBtn.setAlignment(Pos.CENTER_RIGHT);
        boxForAddBtn.setSpacing(5);
        boxForAddBtn.getChildren().addAll(enterJacobian, removeBtn, addBtn);
        epsMethod.setMaxWidth(100);
        epsMethod.setPrefWidth(100);
        epsMethod.setMinWidth(100);
        stepDiff.setMaxWidth(100);
        stepDiff.setPrefWidth(100);
        stepDiff.setMinWidth(100);
        doth.setMaxWidth(100);
        doth.setPrefWidth(100);
        doth.setMinWidth(100);
        doth.setText("x,y,z");

        HBox epses = new HBox();

        epses.setAlignment(Pos.CENTER);
        epses.setSpacing(1);

        epses.getChildren().addAll(new MyLabel("x_0 = "), doth,
                new MyLabel("Eps метода:"), epsMethod,
                new MyLabel("Шаг дифф.:"), stepDiff);
        indic.setVisible(false);
        vbox.getChildren().addAll(boxForAddBtn, epses, indic, startBtn);


        addBtn.setOnMouseClicked(e-> {
            tFlds.add(new MyTextField());
            boxForFields.getChildren().add(tFlds.get(tFlds.size()-1));
            System.out.println(tFlds.size());

        });
        System.out.println(tFlds.size());
        removeBtn.setOnMouseClicked(e -> {
            boxForFields.getChildren().remove(tFlds.get(tFlds.size()-1));
            tFlds.remove(tFlds.size()-1);
            System.out.println(tFlds.size());

        });

        startBtn.setOnMouseClicked(e-> {
            TabPane tabs = new TabPane();
            Scene sceneDialog = new Scene(tabs);

            Service<Void> service = new Service<Void>() {
                @Override
                protected Task<Void> createTask() {
                    return new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            String[] tempDoth = doth.getText().split(",");
                            double[] nDoth = new double[tempDoth.length];
                            for(int r = 0; r < tempDoth.length; r++)
                                nDoth[r] = Double.parseDouble(tempDoth[r]);
                            Method.eps = Double.parseDouble(epsMethod.getText());
                            Method.h = Double.parseDouble(stepDiff.getText());

                            Function[] nlse =new Function[tFlds.size()];

                            for(int y = 0; y < tFlds.size(); y++)
                                nlse[y] = new Function(tFlds.get(y).getText().toString());
                            ArrayList<Image> timg = new ArrayList<>();
                            Vector x_0 = new Vector(nDoth);
                            for (Function f : nlse) {
                                String strFormula = (f.getFunctionExpressionString()).replaceAll("\\*", "");
                                strFormula += "=0";
                                TeXFormula texFormula = new TeXFormula(strFormula);

                                timg.add(SwingFXUtils.toFXImage((BufferedImage) texFormula.createBufferedImage(TeXConstants.STYLE_DISPLAY, 40,
                                        java.awt.Color.BLACK, java.awt.Color.WHITE), null));
                            }

                            for(int i = 0; i < methods.length * 2; i++) {
                                ArrayList<ImageView> formula = new ArrayList<>();
                                for (Image img : timg) {
                                    formula.add(new ImageView(img));
                                }
                                VBox v = new VBox();
                                v.setBackground(new Background(new BackgroundFill(Color.WHITE,
                                        CornerRadii.EMPTY, Insets.EMPTY)));
                                v.setSpacing(20);
                                v.setAlignment(Pos.CENTER);
                                double[][] answer;
                                answer = methods[i % 2].compute(x_0, nlse);

                                HBox pt = new HBox();
                                VBox p = new VBox();
                                p.setAlignment(Pos.CENTER);
                                pt.setAlignment(Pos.CENTER);
                                p.getChildren().addAll(formula);
                                Text brack = new Text("{");
                                brack.setFont(Font.font("Areal", FontWeight.LIGHT, 75
                                        * formula.size()));
                                pt.getChildren().addAll(brack, p);

                                v.getChildren().addAll(pt,
                                        getTable(nlse, answer));
                                ScrollPane sc = new ScrollPane();
                                sc.setContent(v);
                                sc.setFitToWidth(true);
                                sc.setFitToHeight(true);
                                tabs.setPrefWidth(1200);
                                tabs.getTabs().add(new Tab(Jacobian.title + methods[i%2].title, sc));
                                if(i == 1)
                                    Jacobian.setSymbolComputing(true);
                            }
                            Jacobian.setSymbolComputing(false);
                            return null;
                        }
                    };
                }
            };
            service.start();
            service.setOnRunning(e2 -> {
                indic.setMaxHeight(100);
                indic.setVisible(true);
                stage.sizeToScene();

            });
            service.setOnSucceeded(val->{
                Stage dialog = new Stage();
                dialog.setScene(sceneDialog);
                dialog.initOwner(stage);
                dialog.initModality(Modality.APPLICATION_MODAL);
                indic.setVisible(false);
                dialog.sizeToScene();
                dialog.showAndWait();
                stage.sizeToScene();

            });
        });

        enterJacobian.setOnMouseClicked(e -> {
            Stage oStage = new Stage();
            VBox vPane = new VBox();
            vPane.setSpacing(15);
            ScrollPane scPane = new ScrollPane();
            GridPane outputMatrix = new GridPane();
            outputMatrix.setAlignment(Pos.CENTER);
            outputMatrix.setPadding(new Insets(                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 10));
            outputMatrix.setVgap(10);
            outputMatrix.setHgap(10);
            Function f1 =  new Function(tFlds.get(0).getText().toString());
            int NUM_ROW =tFlds.size();
            int NUM_COL= f1.getArgumentsNumber();

            TextField[][] input = new TextField[NUM_ROW][NUM_COL];
            String ftext = f1.getFunctionName() + "(";
            for(int k = 0; k < f1.getArgumentsNumber(); k++)
                if(k == f1.getArgumentsNumber()-1)
                    ftext += f1.getArgument(k).getArgumentName();
                else
                    ftext += f1.getArgument(k).getArgumentName()+",";
            ftext += ")=";
            for(int i = 0; i < NUM_ROW; i++)
                for(int j = 0; j < NUM_COL; j++) {
                    input[i][j] = new TextField(ftext);
                    outputMatrix.add(input[i][j], j, i);
                }
            scPane.setContent(outputMatrix);
            vPane.setAlignment(Pos.CENTER);
            Label txt = new Label("Аналиитически заданный якобиан");
            txt.setFont(new Font("Monaco", 30));
            Button closeBtn = new Button("Сохранить");
            closeBtn.setFont(new Font("Monaco", 30));
            closeBtn.setOnMouseClicked(event -> {
                Jacobian.symbolDerivative = new Function[NUM_COL*NUM_ROW];
                for(int i = 0; i < NUM_ROW; i++)
                    for(int j = 0; j < NUM_COL; j++) {
                        Jacobian.symbolDerivative[i*NUM_COL + j] =
                                new Function(input[i][j].getText());
                        System.out.println(input[i][j].getText());
                    }
                oStage.close();
            });
            HBox localHBox = new HBox(scPane);
            localHBox.setAlignment(Pos.CENTER);
            vPane.getChildren().addAll(txt, localHBox, closeBtn);

            Scene sc = new Scene(vPane, 800, 500);
            oStage.setScene(sc);
            oStage.show();

        });

        vbox.setMinWidth(600);
        vbox.setMinHeight(300);
        vbox.setPadding(new Insets(10,10,10,10));
        Scene scene = new Scene(vbox);
        stage.setTitle("Методы оптимизации нулевого порядка");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    public static Pane getTable(Function[] f, double[][] vals) {
        Font mainFont = Font.font("Monaco", FontWeight.BOLD, 16);
        GridPane gp = new GridPane();
        gp.setAlignment(Pos.CENTER);
        gp.setBackground(new Background(new BackgroundFill(Color.WHITE,
                CornerRadii.EMPTY, Insets.EMPTY)));
        gp.setGridLinesVisible(true);

        Label iterN = new MyLabel("Номер итерации");

        gp.add(iterN, 0, 0);

        for(int i = 0; i < vals.length; i++)
            gp.add(new MyLabel((i+1)+""),
                    0, i+1);

        for(int i = 0; i < f[0].getArgumentsNumber(); i++) {
            TeXFormula texFormula = new TeXFormula(f[0].getArgument(i).getArgumentName());
            Image timg = SwingFXUtils.toFXImage((BufferedImage)texFormula.createBufferedImage(TeXConstants.STYLE_DISPLAY,
                    20,
                    java.awt.Color.BLACK, java.awt.Color.WHITE), null);
            ImageView formula = new ImageView(timg);
            Label lbl = new MyLabel("");
            lbl.setGraphic(formula);
            gp.add(lbl, (i + 1), 0);
        }

        Label lbl = new MyLabel("f");

        gp.add(lbl, f[0].getArgumentsNumber() + 1, 0);

        for(int i = 0; i < vals.length; i++) {
            for (int j = 0; j < vals[0].length; j++) {
                Label curLbl = new MyLabel(vals[i][j] + "");
                gp.add(curLbl, (j + 1), i + 1);
            }
            String fVal = "(";
            for(int k = 0; k < f.length; k ++)
                if(k == f.length - 1)
                    fVal += (Math.ceil(f[k].calculate(vals[i]) * 100000) / 100000.0);
                else
                    fVal += (Math.ceil(f[k].calculate(vals[i]) * 100000) / 100000.0)+"; ";
            fVal += ")";
            gp.add(new MyLabel(fVal), vals[0].length+1, i + 1);

        }
        gp.setVgap(0);
        gp.setHgap(0);
        return gp;



    }
    static class MyLabel extends Label {
        public MyLabel(String t) {
            super(t);
            Font mainFont = Font.font("Monaco", FontWeight.BOLD, 16);

            setAlignment(Pos.CENTER);
            setFont(mainFont);
            setPadding(new Insets(10,10,10,10));
        }
    }

    static class MyTextField extends TextField {
        public MyTextField() {
            super();
            setMinWidth(250);
            Font mainFont = Font.font("Monaco", FontWeight.BOLD, 16);
            setFont(mainFont);

            setText("f(x,y,z) = 2*cos(y)");

            setPromptText("f(x,y) = x+y");
            setFocusTraversable(false);
        }
    }

}