package view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Heap {
    
    private final double SCENE_WIDTH = Screen.getPrimary().getVisualBounds().getWidth();
    private final double SCENE_HEIGHT = Screen.getPrimary().getVisualBounds().getHeight();
    
    private BorderPane heapPane;
    private AnchorPane centrePane;
    private HBox heapFooter;
    private Stage heapStage;
    private Scene heapScene;
    private Stage homeStage;
    
    private long timeDelay=1000_000_000;
    
    public Heap(){
        heapPane = new BorderPane();
        heapScene = new Scene(heapPane, SCENE_WIDTH, SCENE_HEIGHT);
        heapStage.setScene(heapScene);
        
        setFooter();
    }
    
    private void setFooter(){
        heapFooter = new HBox();
        heapFooter.setPrefHeight(30);
        //heapFooter.setStyle(FOOTER_STYLE);
        heapFooter.setPadding(new Insets(5,5,5,5));
        
        Button backButton = new Button("Back");
        
        /*try {
            backButton.setFont(Font.loadFont(new FileInputStream(Font_PATH), 13));
        } catch (FileNotFoundException e) {
            backButton.setFont(Font.font("Verdana", 13));
        }*/
        
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                heapStage.hide();
                homeStage.show();
            }
        });
        
        TextField timeField = new TextField();
        timeField.setPromptText("Set delay of animation(nano seconds)");
        timeField.setPrefWidth(300);
        
        Button setTimeDelay = new Button("Set");
        
        /*try {
            setTimeDelay.setFont(Font.loadFont(new FileInputStream(Font_PATH), 13));
        } catch (FileNotFoundException e) {
            setTimeDelay.setFont(Font.font("Verdana", 13));
            System.out.print("Not working");
        }*/
        
        setTimeDelay.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try{
                    timeDelay = Integer.parseInt( timeField.getText() );
                }catch(NumberFormatException e){}
            }
        });
        
        heapFooter.getChildren().addAll(backButton,timeField,setTimeDelay);
        heapPane.setBottom(heapFooter);
    }
    
}
