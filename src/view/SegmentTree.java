
package view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
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
import javafx.scene.shape.Line;
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
    private Label sumLabel;
    boolean showLabel= true;
    
    private int input,data[],height,nodeValue[];
    SegmentNode[] node;
    Line line[];
    int lineInd=0;
    
    long timeDelay=1000000000,previousTime=0;
    AnimationTimer animation;
    Queue<Integer> nodeQueue= new LinkedList<>(),actionQueue= new LinkedList<>(),valueQueue= new LinkedList<>();
    private final int ORANGE=1,SET_VALUE=2,GREEN=3,ADD=4,LINE=5,AQUA=6;
    
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
                    timeDelay = Integer.parseInt( timeField.getText() );
                }catch(NumberFormatException e){}
            }
        });
        
        segmentPaneFooter.getChildren().addAll(backButton,timeField,setTimeDelay);
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
        TextField fromField = headerField("From Index", SCENE_WIDTH-225-210-5*2-200);
        TextField toField = headerField("To Index", SCENE_WIDTH-225-210-5);
        
        SegmentButton updateButton = headerButton("Update Tree", 15+200*2+5*2);
        updateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
               int ind,val;
               try{
                    showLabel=true;
                    ind = Integer.parseInt( indexField.getText() );
                    val = Integer.parseInt(updateField.getText());
                    update(1, 1, input, ind, val);
                }catch(NumberFormatException e){}
            }
        });
        
        SegmentButton SumButton = headerButton("Find Sum", SCENE_WIDTH-225);
        SumButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try{
                    showLabel=false;
                    int from = Integer.parseInt( fromField.getText() );
                    int to = Integer.parseInt(toField.getText());
                    int ret = query(1, 1, input, from, to);
                    sumLabel.setText("Segment Sum from "+Integer.toString(from)+" to "+Integer.toString(to)+"\n="+Integer.toString(ret)+"\n=");
                }catch(NumberFormatException e){}
            }
        });
        segmentPaneHeader.getChildren().addAll(indexField,updateButton,updateField,fromField,toField,SumButton);
        segmentPane.setTop(segmentPaneHeader);
    }
    
    int query(int ind, int lb, int ub, int qlb, int qub)
    {
        int ret=0;
        if(qlb<=lb && ub <= qub){ 
            ret = nodeValue[ind];
            actionColor(ind, AQUA);
            actionColor(ind, GREEN);
        }else if(qub < lb || qlb > ub){ ret = 0;}else
        {
            int mid = (ub+lb)/2;
            ret=0;
            actionColor(ind, ORANGE);
            ret += query(ind*2,lb, mid, qlb, qub);
            ret += query((ind*2)+1, mid+1, ub, qlb, qub);
            actionColor(ind, GREEN);
        }
        return ret;
    }
    
    private void update(int ind, int lb, int ub, int pos, int val)
    {
        if(pos < lb || pos > ub){}else if(lb==ub){
            if(lb == pos){
                actionColor(ind, ORANGE);
                nodeValue[ind] = val;
                actionSetValue(ind, val);
                actionColor(ind, GREEN);
            }
        }else {
            int mid = (lb+ub)/2;
            actionColor(ind, ORANGE);
            if(pos<=mid) update((ind*2),lb,mid,pos,val);
            else update((ind*2)+1,mid+1,ub,pos,val);
            nodeValue[ind] = nodeValue[(ind*2)] + nodeValue[(ind*2)+1];
            actionSetValue(ind, nodeValue[ind]);
            actionColor(ind, GREEN);
        }
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
    
    private int buildSubTree(int ind,int beg,int end,double begX,double endX,double parentX, double parentY,double blockHeight,double radius){
        if(beg<=end)
        {   
            double circleX = (begX+endX)/2;
            double circleY = (parentY+blockHeight*2);
        
            double lineX = parentX + blockHeight*0.5*Math.sin( Math.atan( (parentY-circleY)/(parentX-circleX) ) );
            double lineY = parentY + Math.abs( blockHeight*0.5*Math.cos( Math.atan( (parentY-circleY)/(parentX-circleX) ) ) );
        
            line[lineInd] = new Line(lineX, lineY, circleX, circleY);
            actionLine(lineInd++);
            
            node[ind]= new SegmentNode(centrePane, circleX, circleY, radius, 0, beg, end);
            actionAdd(ind);
        
            if(beg == end)
            {
                nodeValue[ind] = data[beg-1];
                node[ind].setValue(nodeValue[ind]);
                node[ind].changeToGreen();
            }else{
                int mid = (beg+end)/2;
                int value1 = buildSubTree(ind*2, beg, mid, begX, circleX, circleX, circleY, blockHeight,radius);
                int value2 = buildSubTree(ind*2+1, mid+1, end, circleX, endX, circleX, circleY, blockHeight,radius);
                nodeValue[ind] = value1+value2;
                
                actionSetValue(ind, nodeValue[ind]);
                actionColor(ind, GREEN);
            }
        }
        
        return nodeValue[ind];
    }
    
    private void actionSetValue(int ind,int value){
        nodeQueue.add(ind);
        valueQueue.add(value);
        actionQueue.add(SET_VALUE);
    }
    
    private void actionColor(int ind,int color){
        nodeQueue.add(ind);
        actionQueue.add(color);
    }
    
    private void actionAdd(int ind){
        nodeQueue.add(ind);
        actionQueue.add(ADD);
    }
    
    private void actionLine(int ind){
        nodeQueue.add(ind);
        actionQueue.add(LINE);
    }
    
    private void buildTree(){
        double CENTRE_WIDTH = SCENE_WIDTH;
        double CENTRE_HEIGHT = SCENE_HEIGHT-80-30;
        createBuildTreeHeader();
        centrePane = new AnchorPane();
        segmentPane.setCenter(centrePane);
        
        createLabel(CENTRE_WIDTH,CENTRE_HEIGHT);
        
        sumLabel = new Label("");
        sumLabel.setLayoutX(10);
        sumLabel.setLayoutY(10);
        centrePane.getChildren().add(sumLabel);
        showLabel=true;
        
        activateAnimation();
        line = new Line[input*4];
        
        height= (int)Math.ceil( (Math.log(input)/Math.log(2)) )+1;
        
        double blockHeight = (CENTRE_HEIGHT-30-5)/(height*2-1);
        double circleX = CENTRE_WIDTH/2;
        double circleY = 5+ blockHeight/2;
        double radius = Double.min(blockHeight/2, CENTRE_WIDTH/input);
        
        node = new SegmentNode[input*4];
        nodeValue = new int[input*4];
        
        
        node[1] = new SegmentNode(centrePane, circleX, circleY, radius, 0, 1, input);
        actionAdd(1);
        int mid = (1+input)/2;
        int value1 = buildSubTree(1*2, 1, mid, 5, circleX, circleX, circleY, blockHeight,radius);
        int value2 = buildSubTree(3, mid+1, input, circleX, CENTRE_WIDTH-5, circleX, circleY, blockHeight,radius);
        nodeValue[1] = value1 + value2;
        
        actionSetValue(1, nodeValue[1]);
        actionColor(1, GREEN);
    }
    
    private void activateAnimation(){
        animation = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if(now - previousTime>=timeDelay){
                    previousTime=now;
                    if(nodeQueue.size()!=0){
                        int nodeNo = nodeQueue.remove();
                        int action = actionQueue.remove();
                        if(action == GREEN){
                            node[nodeNo].changeToGreen();
                            if(showLabel) sumLabel.setText("Node of value "+Integer.toString(nodeValue[nodeNo])+" process completed");
                        }else if(action==ORANGE){
                            node[nodeNo].changeToOrange();
                            if(showLabel) sumLabel.setText("Node of Value "+Integer.toString(nodeValue[nodeNo])+" is in processing");
                        }else if(action==ADD){
                            node[nodeNo].addNode();
                        }else if(action == SET_VALUE){
                            node[nodeNo].setValue(valueQueue.remove());
                            if(showLabel) sumLabel.setText("Node value changed to "+Integer.toString(nodeValue[nodeNo])+" after process");
                        }else if(action == LINE){
                            centrePane.getChildren().add(line[nodeNo]);
                        }else if(action == AQUA){
                            node[nodeNo].changeToAqua();
                            String sum = sumLabel.getText();
                            if( sum.charAt( sum.length()-1 )!= '=' ){
                                sum+="+" + Integer.toString(nodeValue[nodeNo]);
                            }else sum+= Integer.toString(nodeValue[nodeNo]);
                            sumLabel.setText(sum);
                        }
                    }
                }
            }
        };
        animation.start();
    }
    
}
