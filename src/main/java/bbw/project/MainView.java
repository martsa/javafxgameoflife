package bbw.project;

import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;

public class MainView extends VBox {
    private Button stepButton;
    private Canvas canvas;          //place where we can draw anthing on it .. graphics
    private Simulation simulation; // get simulation
    private Affine affine;



    //constructor
    public MainView() {

        this.stepButton= new Button("Simulate Button ");
        this.stepButton.setOnAction(actionEvent->{             //activate step button with redrawing the canvas
         simulation.step();
         draw();
        });
        this.canvas=new Canvas(400,400);

        //creating a mouse press handler with calling function handlerDraw
        this.canvas.setOnMousePressed(this::handleDraw);

        this.getChildren().addAll(this.stepButton,this.canvas); //add to the viewbox
        this.affine=new Affine();
        this.affine.appendScale(400 / 10f, 400/10f); // fill the canvas with our simulation.
        this.simulation=new Simulation(10,10);  //create a simulation on canvas


    }
    // get coordinates of mouse pressed in canvas
    private void handleDraw(MouseEvent mouseEvent) {
        double mouseX= mouseEvent.getX();
        double mouseY=mouseEvent.getY();



        try {
            Point2D simCoord= this.affine.inverseTransform(mouseX, mouseY);//it will take screen coordinate & convert to simulation coordinate
            int simX=(int) simCoord.getX();
            int simY=(int) simCoord.getY();
            System.out.println(simX +"," +simY);
            this.simulation.setAlive(simX,simY);       //creating cell alive that was mouse clicked
            draw();                                    //redraw it to see changes
        }catch (NonInvertibleTransformException e) {
            System.out.println("Transform couldnt be performed ");

        }
    }

    //draw on canvas using draw function.
    public void draw(){

        GraphicsContext g= this.canvas.getGraphicsContext2D();  //Get graphics context from canvas
        g.setTransform(this.affine);            //every thing should scaled up to the size of canvas size
        g.setFill(Color.LIGHTGRAY);
        g.fillRect(0,0,450,450);
        g.setFill(Color.BLACK);   // change alive color to black

        //starting drawing board

        for (int x = 0; x < this.simulation.width; x++) {
            for (int y = 0; y < this.simulation.height; y++) {
                if(this.simulation.getstate(x,y)==1) {     //if alive draw rectangle
                    g.fillRect(x, y, 1, 1);
                }
            }

        }
        g.setStroke(Color.GRAY);
        g.setLineWidth(0.05);

        //draw grid line  both x as well as y co oridinates
        for (int x = 0; x <= this.simulation.width; x++) {
            g.strokeLine(x,0,x,10);

        }

        for (int y = 0; y < this.simulation.height; y++) {
            g.strokeLine(0, y, 10, y);
        }

        }


}
