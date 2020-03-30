package model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.text.Font;

public class SegmentLabel extends Label{
    
    private final String FONT_PATH = "model/segmentTreeResources/good_times.ttf";
    
    public SegmentLabel(double width,double height,int number){
        setText(Integer.toString(number));
        setPrefHeight(height);
        setPrefWidth(width);
        createBackground(width, height);
        createFont(height);
        setAlignment(Pos.CENTER);
    }
    
    private void createFont(double height){
        try {
            setFont(Font.loadFont(new FileInputStream(FONT_PATH), height/2));
        } catch (FileNotFoundException e) {
            setFont(Font.font("Verdana", height/2));
        }
    }
    
    private void createBackground(double width,double height){
        Image image = new Image("model/segmentTreeResources/label.png", width, height, false, true);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.ROUND, BackgroundRepeat.ROUND, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        setBackground(new Background(backgroundImage));
   }
    
}
