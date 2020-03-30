package model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;

public class SegmentButton extends Button{
    
    private final String Font_PATH = "src/model/segmentTreeResources/good_times.ttf";
    private final String BUTTON_PRESSED_STYLE = "-fx-background-color: transparent; -fx-background-image: url('/model/segmentTreeResources/yellow_button02.png'); -fx-background-repeat: round";
    private final String BUTTON_STYLE = "-fx-background-repeat: round;-fx-background-color: transparent; -fx-background-image: url('/model/segmentTreeResources/yellow_button01.png');";
    
    private int height = 49;
    
    public SegmentButton(String text){
        setText(text);
        setSegmentButtonFont();
        setStyle(BUTTON_STYLE);
        setPrefHeight(height);
        setPrefWidth(210);
        initializeSegmentButtonListeners();
    }
    
    private void setSegmentButtonFont(){
        try {
            setFont(Font.loadFont(new FileInputStream(Font_PATH), 20));
        } catch (FileNotFoundException e) {
            setFont(Font.font("Verdana", 15));
            System.out.print("Not working");
        }
    }
    
    private void setSegmentButtonPressedStyle(){
        setStyle(BUTTON_PRESSED_STYLE);
        setPrefHeight(height);
        setLayoutY(getLayoutY()+5);
    }
    
    private void setSegmentButtonReleasedStyle(){
        setStyle(BUTTON_STYLE);
        setPrefHeight(height);
        setLayoutY(getLayoutY()-5);
    }
    
    public void initializeSegmentButtonListeners(){
        
        setOnMousePressed(new EventHandler<MouseEvent>(){
            public void handle(MouseEvent event){
                if(event.getButton().equals(MouseButton.PRIMARY))
                    setSegmentButtonPressedStyle();
            }
        });
        
        setOnMouseReleased(new EventHandler<MouseEvent>(){
            public void handle(MouseEvent event){
                if(event.getButton().equals(MouseButton.PRIMARY))
                    setSegmentButtonReleasedStyle();
            }
        });
        
        setOnMouseEntered(new EventHandler<MouseEvent>(){
            public void handle(MouseEvent event){
                setEffect(new DropShadow());
            }
        });
        
        setOnMouseExited(new EventHandler<MouseEvent>(){
            public void handle(MouseEvent event){
                setEffect(null);
            }
        });
        
    }
    
    public void setHeight(int x){
        if(x>49)
        {
            height = x;
            setPrefHeight(height);
        }
    }
    
}
