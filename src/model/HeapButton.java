package model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;


public class HeapButton extends Button{
    
    private final String FONT_PATH = "src/model/heapResources/Drifttype.ttf";
    private final String BUTTON_PRESSED_STYLE = "-fx-background-repeat: round; -fx-background-color: transparent; -fx-background-image: url('/model/heapResources/button_pressed.png'); -fx-background-size: contain;";
    private final String BUTTON_STYLE = "-fx-background-repeat: round; -fx-background-color: transparent; -fx-background-image: url('/model/heapResources/button.png'); -fx-background-size: contain;";
    
    private double width,height;
    private String text;
    
    public HeapButton(String text,double width,double height){
        this.text = text;
        this.width = width;
        this.height = height;
        setText(text);
        setHeapButtonFont();
        setStyle(BUTTON_STYLE);
        setPrefHeight(height);
        setPrefWidth(width);
        initializeHeapButtonListeners();
    }
    
    private void setHeapButtonFont(){
        try {
            setFont(Font.loadFont(new FileInputStream(FONT_PATH), Integer.min( (int)width/text.length(),(int)height/2 ) ));
        } catch (FileNotFoundException e) {
            setFont(Font.font("Verdana", width/text.length()));
            System.out.print("Not working");
        }
    }
    
    private void setHeapButtonPressedStyle(){
        setStyle(BUTTON_PRESSED_STYLE);
        setPrefHeight(height-5);
    }
    
    private void setHeapButtonReleasedStyle(){
        setStyle(BUTTON_STYLE);
        setPrefHeight(height);
    }
    
    public void initializeHeapButtonListeners(){
        
        setOnMousePressed(new EventHandler<MouseEvent>(){
            public void handle(MouseEvent event){
                if(event.getButton().equals(MouseButton.PRIMARY))
                    setHeapButtonPressedStyle();
            }
        });
        
        setOnMouseReleased(new EventHandler<MouseEvent>(){
            public void handle(MouseEvent event){
                if(event.getButton().equals(MouseButton.PRIMARY))
                    setHeapButtonReleasedStyle();
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

}
