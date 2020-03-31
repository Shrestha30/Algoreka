package model;

import java.io.FileInputStream;

import java.io.FileNotFoundException;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import javafx.scene.text.Font;

public class HomeButton extends Button {
    
    private final String Font_PATH = "src/model/homeResources/ethnocentric.ttf";
    private final String BUTTON_PRESSED_STYLE = "-fx-background-color: transparent; -fx-background-image: url('/model/homeResources/green_button_pressed.png');";
    private final String BUTTON_STYLE = "-fx-background-repeat: round;-fx-background-color: transparent; -fx-background-image: url('/model/homeResources/green_button.png');";
    
    public HomeButton(String text){
        setText(text);
        setHomeButtonFont();
        setStyle(BUTTON_STYLE);
        setPrefHeight(49);
        setPrefWidth(210);
        initializeHomrButtonListeners();
    }
    
    private void setHomeButtonFont(){
        try {
            setFont(Font.loadFont(new FileInputStream(Font_PATH), 20));
        } catch (FileNotFoundException e) {
            setFont(Font.font("Verdana", 15));
            System.out.print("Not working");
        }
    }
    
    private void setHomeButtonPressedStyle(){
        setStyle(BUTTON_PRESSED_STYLE);
        setPrefHeight(45);
        setLayoutY(getLayoutY()+5);
    }
    
    private void setHomeButtonReleasedStyle(){
        setStyle(BUTTON_STYLE);
        setPrefHeight(49);
        setLayoutY(getLayoutY()-5);
    }
    
    public void initializeHomrButtonListeners(){
        
        setOnMousePressed(new EventHandler<MouseEvent>(){
            public void handle(MouseEvent event){
                if(event.getButton().equals(MouseButton.PRIMARY))
                    setHomeButtonPressedStyle();
            }
        });
        
        setOnMouseReleased(new EventHandler<MouseEvent>(){
            public void handle(MouseEvent event){
                if(event.getButton().equals(MouseButton.PRIMARY))
                    setHomeButtonReleasedStyle();
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
