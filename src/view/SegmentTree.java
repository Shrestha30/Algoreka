
package view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import model.SegmentButton;
import model.SegmentLabel;
import model.SegmentNode;

public class SegmentTree {
    
    private final double SCENE_WIDTH=Screen.getPrimary().getVisualBounds().getWidth();
    private final double SCENE_HEIGHT=Screen.getPrimary().getVisualBounds().getHeight()-30;
    
    private final String FOOTER_STYLE="-fx-background-repeat: round;-fx-background-color: aqua;";
    
    private final String Font_PATH = "src/model/segmentTreeResources/good_times.ttf";
    
    private Stage segmentStage;
    private BorderPane segmentPane;
    private HBox segmentPaneFooter;
    private AnchorPane segmentPaneHeader;
    private AnchorPane centrePane;
    private Scene segmentScene;
    
    private Stage homeStage;
    
    private int input,data[];
    
    public SegmentTree()
    {
        segmentPane = new BorderPane();
        segmentScene = new Scene(segmentPane, SCENE_WIDTH, SCENE_HEIGHT);
        segmentStage = new Stage();
        segmentStage.setScene(segmentScene);
        
        createSegmentPaneFooter();
        createSegmentPaneHeader();
    }
    
    private void createSegmentPaneFooter(){
        segmentPaneFooter = new HBox();
        segmentPaneFooter.setPrefHeight(30);
        segmentPaneFooter.setStyle(FOOTER_STYLE);
        segmentPaneFooter.setPadding(new Insets(5,5,5,5));
        
        Button backButton = new Button("Back");
        
        try {
            backButton.setFont(Font.loadFont(new FileInputStream(Font_PATH), 13));
        } catch (FileNotFoundException e) {
            backButton.setFont(Font.font("Verdana", 13));
            System.out.print("Not working");
        }
        
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                segmentStage.hide();
                homeStage.show();
            }
        });
        
        segmentPaneFooter.getChildren().add(backButton);
        segmentPane.setBottom(segmentPaneFooter);
    }
    
    private void createSegmentPaneHeader(){
        segmentPaneHeader = new AnchorPane();
        Image headerImage= new Image("view/resources/segmenttree/header.png",SCENE_WIDTH,150,false,true);
        BackgroundImage headerBackgroundImage = new BackgroundImage(headerImage, BackgroundRepeat.ROUND, BackgroundRepeat.ROUND, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        segmentPaneHeader.setBackground(new Background(headerBackgroundImage));
        
        segmentPaneHeader.setPrefHeight(150);
        segmentPaneHeader.setPrefWidth(SCENE_WIDTH);
        
        TextField inputNo = new TextField();
        inputNo.setPromptText("Number of Inputs");
        inputNo.setPrefHeight(40);
        inputNo.setPrefWidth(200);
        inputNo.setLayoutY(50);
        inputNo.setLayoutX(40);
        
        SegmentButton button = new SegmentButton("Take  Inputs");
        button.setHeight(40);
        button.setLayoutX(200+40+20);
        button.setLayoutY(45.5);
        
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try{
                    input = Integer.parseInt( inputNo.getText() );
                    createInputGUI();
                }catch(NumberFormatException e){
                    segmentPane.setCenter(new Label("Please enter a number"));
                }
            }
        });
        
        segmentPaneHeader.getChildren().addAll(inputNo,button);
        segmentPane.setTop(segmentPaneHeader);
    }
    
    public void segmentStart(Stage homeStage)
    {
        this.homeStage = homeStage;
        homeStage.hide();
        segmentStage.show();
        segmentStage.setMaximized(true);
    }
    
    private void createInputGUI(){
        AnchorPane pane = new AnchorPane();
        
        final double PANE_WIDTH = SCENE_WIDTH;
        final double PANE_HEIGHT = SCENE_HEIGHT-30-150;
        final double FIELDX=15;
        final double FIELDY=15;
        
        pane.setPrefHeight(PANE_HEIGHT);
        pane.setPrefWidth(PANE_HEIGHT);
        
        data = new int[input];
        SegmentButton button = new SegmentButton("Build  Tree");
        button.setLayoutX( (PANE_WIDTH-button.getPrefWidth())/2 );
        button.setLayoutY(PANE_HEIGHT-15-button.getPrefHeight());
        
        int fieldNum = (int)Math.ceil( Math.sqrt(input) );
        TextField[] field = new TextField[input];
        final double FIELD_WIDTH = (PANE_WIDTH-30)/fieldNum;
        final double FIELD_HEIGHT= (PANE_HEIGHT-15-button.getPrefHeight()-30)/fieldNum;
        for(int i=0;i<input;i++)
        {
            field[i]=new TextField();
            field[i].setPromptText(Integer.toString(i+1));
            field[i].setPrefWidth(FIELD_WIDTH);
            field[i].setPrefHeight(FIELD_HEIGHT);
            field[i].setLayoutX( FIELDX+FIELD_WIDTH*(i/(fieldNum)) );
            field[i].setLayoutY(FIELDY+ (i%(fieldNum))*FIELD_HEIGHT );
            try {
                field[i].setFont(Font.loadFont(new FileInputStream(Font_PATH), FIELD_HEIGHT/2));
            } catch (FileNotFoundException e) {
                field[i].setFont(Font.font("Verdana", FIELD_HEIGHT/2));
                System.out.print("Not working");
            }
            pane.getChildren().add(field[i]);
        }
        
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                boolean next=true;
                for(int i=0;i<input;i++){
                    try{
                    data[i] = Integer.parseInt( field[i].getText() );
                    }catch(NumberFormatException e){
                        next=false;
                        segmentPane.setCenter(new Label("One of the inputs was not Valid\nAll inputs must be integers"));
                        break;
                    }
                }
                if(next)
                    buildTree();
            }
        });
              
        pane.getChildren().add(button);
        segmentPane.setCenter(pane);
    }
    
    private void createBuildTreeHeader(){
        segmentPaneHeader = new AnchorPane();
        Image headerImage= new Image("view/resources/segmenttree/header.png",SCENE_WIDTH,80,false,true);
        BackgroundImage headerBackgroundImage = new BackgroundImage(headerImage, BackgroundRepeat.ROUND, BackgroundRepeat.ROUND, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        segmentPaneHeader.setBackground(new Background(headerBackgroundImage));
        
        segmentPaneHeader.setPrefHeight(80);
        segmentPaneHeader.setPrefWidth(SCENE_WIDTH);
        
        TextField indexField = headerField("Index", 15);
        TextField updateField = headerField("Value to be Updated", 15+200+5);
        TextField fromField = headerField("From", SCENE_WIDTH-225-210-5*2-200);
        TextField toField = headerField("To", SCENE_WIDTH-225-210-5);
        
        SegmentButton updateButton = headerButton("Build", 15+200*2+5*2);
        SegmentButton SumButton = headerButton("Find Sum", SCENE_WIDTH-225);
        
        segmentPaneHeader.getChildren().addAll(indexField,updateButton,updateField,fromField,toField,SumButton);
        segmentPane.setTop(segmentPaneHeader);
    }
    
    private TextField headerField(String text,double x){
        TextField inputNo = new TextField();
        inputNo.setPromptText(text);
        inputNo.setPrefHeight(40);
        inputNo.setPrefWidth(200);
        inputNo.setLayoutY(20);
        inputNo.setLayoutX(x);
        return inputNo;
    }
    
    private SegmentButton headerButton(String text,double x){
        SegmentButton button = new SegmentButton(text);
        button.setLayoutX(x);
        button.setLayoutY(15.5);
        return button;
    }
    
    private void createLabel(double width,double height){
        SegmentLabel label[]=new SegmentLabel[input];
        for(int i=0;i<input;i++)
        {
            label[i]=new SegmentLabel(width/input, 20, data[i]);
            label[i].setLayoutY(height-5-20);
            label[i].setLayoutX(5+i*width/input);
            centrePane.getChildren().add(label[i]);
        }
    }
    
    private void buildTree(){
        double CENTRE_WIDTH = SCENE_WIDTH;
        double CENTRE_HEIGHT = SCENE_HEIGHT-80-30;
        createBuildTreeHeader();
        centrePane = new AnchorPane();
        segmentPane.setCenter(centrePane);
        
        createLabel(CENTRE_WIDTH,CENTRE_HEIGHT);
        SegmentNode node = new SegmentNode(centrePane,300,300,100,55);
        //a delay of 4 sec
        node.setValue(555);
        node.changeToGreen();
    }
    
}
