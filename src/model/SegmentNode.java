package model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

public class SegmentNode {
    
    private final String Font_PATH = "src/model/segmentTreeResources/good_times.ttf";
    
    private Circle circle;
    private Label text;
    private Label range;
    private double circleX;
    private double circleY;
    private double radius;
    private AnchorPane pane;
    
    public SegmentNode(AnchorPane pane,double circleX,double circleY,double radius,int number,int start,int end){
        circle = new Circle(circleX, circleY, radius);
        this.circleX = circleX;
        this.circleY = circleY;
        this.radius = radius;
        this.pane = pane;
        changeToOrange();
        
        text = createText(Integer.toString(number));
        range = createRange("("+Integer.toString(start)+"-"+Integer.toString(end)+")");
    }
    
    private Label createText(String number){
        if(number.length()==1)
            number=" "+number+" ";
        Label tempText = new Label(number);
        try {
            tempText.setFont(Font.loadFont(new FileInputStream(Font_PATH), 2*radius/number.length()));
        } catch (FileNotFoundException e) {
            tempText.setFont(Font.font("Verdana", 2*radius/number.length()));
            System.out.print("Not working");
        }
        
        tempText.setPrefWidth(radius*2);
        tempText.setPrefHeight(radius*2);
        tempText.setLayoutX(circleX-radius);
        tempText.setLayoutY(circleY-radius);
        tempText.setAlignment(Pos.CENTER);
        
        return tempText;
    }
    
    private Label createRange(String number){
        Label tempText = new Label(number);
        //try {
            //tempText.setFont(Font.loadFont(new FileInputStream(Font_PATH), 2*radius/number.length()));
        //} catch (FileNotFoundException e) {
            tempText.setFont(Font.font("Verdana", 2*radius/number.length()));
        //}
        
        tempText.setPrefWidth(radius*2);
        tempText.setPrefHeight(radius*2);
        tempText.setLayoutX(circleX+.5*radius);
        tempText.setLayoutY(circleY-1.5*radius);
        tempText.setAlignment(Pos.CENTER);
        
        return tempText;
    }
    
    public void changeToOrange(){
        circle.setFill(Color.ORANGE);
    }
    
    public void changeToGreen(){
        circle.setFill(Color.LIGHTGREEN);
    }
    
    public void changeToAqua(){
        circle.setFill(Color.AQUA);
    }
    
    public void setValue(int no){
        String number = Integer.toString(no);
        if(number.length()==1)
            number=" "+number+" ";
        text.setText(number);
        try {
            text.setFont(Font.loadFont(new FileInputStream(Font_PATH), 2*radius/number.length()));
        } catch (FileNotFoundException e) {
            text.setFont(Font.font("Verdana", 2*radius/number.length()));
            System.out.print("Not working");
        }
    }
    
    public void addNode(){
        pane.getChildren().addAll(circle,text,range);
    }
    
}
