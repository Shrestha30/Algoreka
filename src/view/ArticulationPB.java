/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.beans.property.DoubleProperty;
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

/**
 *
 * @author USER
 */
public class ArticulationPB {
    private final double SCENE_WIDTH = Screen.getPrimary().getVisualBounds().getWidth()-1;
    private final double SCENE_HEIGHT = Screen.getPrimary().getVisualBounds().getHeight()-1;
    private final double footerHeightM = 30/705.0;
    private final double headerHeightM = 30/1366.0;
    
    private final String FOOTER_STYLE="-fx-background-repeat: round;-fx-background-color: aqua;";
    private final String Font_PATH = "src/model/heapResources/Drifttype.ttf";
    
    private BorderPane APane;
    private AnchorPane centrePane;
    private AnchorPane heapHeader;
    private HBox AFooter;
    private Stage AStage;
    private Scene AScene;
    private Stage homeStage;
    private long timeDelay;

    public ArticulationPB() {
        APane = new BorderPane();
        AScene = new Scene(APane, SCENE_WIDTH, SCENE_HEIGHT);
        AStage = new Stage();
        AStage.setScene(AScene);
        
        setFooter();
    }
    
    private void setFooter(){
        AFooter = new HBox();
        AFooter.prefHeightProperty().bind(APane.heightProperty().multiply(footerHeightM));
        AFooter.setStyle(FOOTER_STYLE);
        AFooter.setPadding(new Insets(5,5,5,5));
        
        Button backButton = new Button("Back");
        
        try {
            backButton.setFont(Font.loadFont(new FileInputStream(Font_PATH), 13));
        } catch (FileNotFoundException e) {
            backButton.setFont(Font.font("Verdana", 13));
        }
        
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                AStage.hide();
                homeStage.show();
            }
        });
        
        TextField timeField = new TextField();
        timeField.setPromptText("Set delay of animation(nano seconds)");
        timeField.setPrefWidth(300);
        
        Button setTimeDelay = new Button("Set");
        
        try {
            setTimeDelay.setFont(Font.loadFont(new FileInputStream(Font_PATH), 13));
        } catch (FileNotFoundException e) {
            setTimeDelay.setFont(Font.font("Verdana", 13));
            System.out.print("Not working");
        }
        
        setTimeDelay.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try{
                    timeDelay = (long)(1000000000*Double.parseDouble(timeField.getText())<1000000000?1000000000:1000000000*Double.parseDouble(timeField.getText()) );
                }catch(NumberFormatException e){}
            }
        });
        
        AFooter.getChildren().addAll(backButton,timeField,setTimeDelay);
        APane.setBottom(AFooter);
    }
    
}
