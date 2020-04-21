package model;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

public class HeapNode extends StackPane{
    
    private Circle circle;
    private Label label;
    private double circleX,circleY;
    private String text;
    
    private final String FONT_PATH = "src/model/heapResources/Drifttype.ttf";
    
    public HeapNode(double circleX, double circleY, double radius, int number){
        this.circleX = circleX;
        this.circleY = circleY;
        
        circle = new Circle(radius);
        circle.setFill(Color.LIME);
        
        String text = Integer.toString(number);
        if(text.length()==1)
            text=" "+text+" ";
        this.text = text;
        label = new Label(text);
        label.setFont(Font.font("Verdana", 2*radius/text.length()));
        
        getChildren().addAll(circle,label);
        setLayoutX(circleX-radius);
        setLayoutY(circleY-radius);
    }
    
    public double getCircleX(){
        return circleX;
    }
    
    public double getCircleY(){
        return circleY;
    }
    
    public void setCircleX(double circleX){
        this.circleX = circleX;
    }
    
    public void setCircleY(double circleY){
        this.circleY = circleY;
    }
    
    public void setRadius(double radius){
        circle.setRadius(radius);
        label.setFont(Font.font("Verdana", 2*radius/text.length()));
        
    }
    
    public void changeToGreen(){
        circle.setFill(Color.LIME);
    }
    
    public void changeToOrange(){
        circle.setFill(Color.ORANGE);
    }
    
}
