package model;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;

public class AvlNode extends StackPane{
    
    private Circle circle;
    private Label label;
    private Label balanceFactor;
    private double circleX,circleY;
    private String text;
    private double radius;
    private Line line;
    
    private final String FONT_PATH = "src/model/heapResources/Drifttype.ttf";
    
    public AvlNode(double circleX, double circleY, double radius, int number,int bf){
        this.circleX = circleX;
        this.circleY = circleY;
        this.radius = radius;
        
        circle = new Circle(radius);
        circle.setFill(Color.LIME);
        
        String text = Integer.toString(number);
        if(text.length()==1)
            text=" "+text+" ";
        this.text = text;
        label = new Label(text);
        label.setFont(Font.font("Verdana", 2*radius/text.length()));
        
        balanceFactor = createBf(Integer.toString(bf));
        
        getChildren().addAll(circle,label,balanceFactor);
        setLayoutX(circleX-radius);
        setLayoutY(circleY-radius);
        
        line = new Line();
        line.endXProperty().bind(layoutXProperty().add(2*radius));
        line.endYProperty().bind(layoutYProperty());
        
        line.startXProperty().bind(layoutXProperty().add(radius));
        line.startYProperty().bind(layoutYProperty());
    }
    
    private Label createBf(String number){
        Label tempText = new Label("("+number+")");
        tempText.setFont(Font.font("Verdana", radius/5));
        
        tempText.setPrefWidth(radius*2);
        tempText.setPrefHeight(radius*2);
        tempText.setLayoutX(circleX+.5*radius);
        tempText.setLayoutY(circleY-radius);
        tempText.setAlignment(Pos.TOP_RIGHT);
        
        return tempText;
    }
    
    public void setBf(int number){
        balanceFactor.setText("("+Integer.toString(number)+")");
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
    
    public double getRadius(){
        return radius;
    }
    
    public void changeToGreen(){
        circle.setFill(Color.LIME);
    }
    
    public void changeToOrange(){
        circle.setFill(Color.ORANGE);
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public Line getLine() {
        return line;
    }

    /*public void setLine(Line line) {
        this.line = line;
    }*/
    
}
