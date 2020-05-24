package model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;

public class TestNode {
    
    private int key;  
    private TestNode left;  
    private TestNode right;
    private Line tLine,lLine,rLine;
    private Circle circle;
    private Label bfLabel,keyLabel;
    
    public TestNode(int key, int bf,double circleX,double circleY,double radius){
        this.key = key;
        left=null;
        right=null;
        height=1;
        circle = new Circle(radius);
        
        keyLabel = createText(Integer.toString(key));
        bfLabel = createRange("("+Integer.toString(bf)+")");
        
        tLine = new Line();
        lLine = new Line();
        rLine = new Line();
        
        circle.setCenterX(circleX);
        circle.setCenterY(circleY);
        circle.setFill(Color.AQUA);
        createCircleListeners();
    }
    
    private void createCircleListeners(){
        circle.layoutXProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                bfLabel.setLayoutX(circle.getCenterX()+circle.getRadius()/2);
                keyLabel.setLayoutX(circle.getCenterX()-circle.getRadius());
                tLine.setStartX(circle.getCenterX()+circle.getRadius());
                lLine.setEndX(circle.getCenterX()+circle.getRadius());
            }
        });
        circle.layoutYProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                bfLabel.setLayoutY(circle.getCenterY()-2*circle.getRadius());
                keyLabel.setLayoutY(circle.getCenterY()-circle.getRadius());
                tLine.setStartY(circle.getCenterY()+circle.getRadius());
                lLine.setEndY(circle.getCenterY()-circle.getRadius());
            }
        });
        
        circle.centerXProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                bfLabel.setLayoutX(circle.getCenterX()+circle.getRadius()/2);
                keyLabel.setLayoutX(circle.getCenterX()-circle.getRadius());
                tLine.setStartX(circle.getCenterX());
                lLine.setEndX(newValue.doubleValue());
                rLine.setEndX(newValue.doubleValue());
                
                //System.out.println(enLine.getStartX()+" "+stLine.getStartX());
            }
        });
        circle.centerYProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                bfLabel.setLayoutY(circle.getCenterY()-circle.getRadius());
                keyLabel.setLayoutY(circle.getCenterY()-circle.getRadius());
                tLine.setStartY(newValue.doubleValue()-circle.getRadius());
                lLine.setEndY(newValue.doubleValue()+circle.getRadius());
                rLine.setEndY(newValue.doubleValue()+circle.getRadius());
            }
        });
    }
    
    private Label createRange(String number){
        Label tempText = new Label(number);
        tempText.setFont(Font.font("Verdana", 2*circle.getRadius()/number.length()));
        
        tempText.setPrefWidth(circle.getRadius()*2);
        tempText.setPrefHeight(circle.getRadius()*2);
        
        tempText.setAlignment(Pos.CENTER);
        
        return tempText;
    }
    
    private Label createText(String number){
        if(number.length()==1)
            number=" "+number+" ";
        Label tempText = new Label(number);
        
        tempText.setFont(Font.font("Verdana", 2*circle.getRadius()/number.length()));
        
        tempText.setPrefWidth(circle.getRadius()*2);
        tempText.setPrefHeight(circle.getRadius()*2);
        
        tempText.setAlignment(Pos.CENTER);
        
        return tempText;
    }
    
    public void setRadius(double radius){
        circle.setRadius(radius);
        
        keyLabel.setFont(Font.font("Verdana", 2*circle.getRadius()/keyLabel.getText().length()));
        bfLabel.setFont(Font.font("Verdana", 2*circle.getRadius()/bfLabel.getText().length()));
        
        keyLabel.setPrefWidth(circle.getRadius()*2);
        keyLabel.setPrefHeight(circle.getRadius()*2);
        
        bfLabel.setPrefWidth(circle.getRadius()*2);
        bfLabel.setPrefHeight(circle.getRadius()*2);
    }

    public Circle getCircle() {
        return circle;
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
    }

    public Label getBfLabel() {
        return bfLabel;
    }

    public void setBfLabel(Label bfLabel) {
        this.bfLabel = bfLabel;
    }

    public Label getKeyLabel() {
        return keyLabel;
    }

    public void setKeyLabel(Label keyLabel) {
        this.keyLabel = keyLabel;
    }
    
    public Line getTLine() {
        return tLine;
    }

    public void setTLine(Line line) {
        this.tLine = line;
    }
    
    public Line getLLine() {
        return lLine;
    }

    public void setLLine(Line line) {
        this.lLine = line;
    }
    
    public void setRLine(Line line) {
        this.rLine = line;
    }
    
    public Line getRLine() {
        return rLine;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public TestNode getLeft() {
        return left;
    }

    public void setLeft(TestNode left) {
        this.left = left;
    }

    public TestNode getRight() {
        return right;
    }

    public void setRight(TestNode right) {
        this.right = right;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
    private int height;
    
    public void setBf(int bf){
        bfLabel.setText("("+Integer.toString(bf)+")");
    }
    
}
