package view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Queue;
import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
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
import javafx.util.Duration;
import model.HeapButton;
import model.HeapNode;

public class Heap {
    
    private final double SCENE_WIDTH = Screen.getPrimary().getVisualBounds().getWidth();
    private final double SCENE_HEIGHT = Screen.getPrimary().getVisualBounds().getHeight();
    private final double FOOTER_HEIGHT = 30;
    private final double HEADER_HEIGHT = 80;
    
    private final String FOOTER_STYLE="-fx-background-repeat: round;-fx-background-color: aqua;";
    private final String Font_PATH = "src/model/heapResources/Drifttype.ttf";
    
    private BorderPane heapPane;
    private AnchorPane centrePane;
    private AnchorPane heapHeader;
    private HBox heapFooter;
    private Stage heapStage;
    private Scene heapScene;
    private Stage homeStage;
    private HeapButton insertButton,deleteButton,sortButton;
    private Label statLabel,srtLabel;
    
    private int factor,cnt=0;
    private int[] data,fakeData;
    private HeapNode node[];
    private Line[] line;
    private double nx1,nx2,ny1,ny2;
    
    private long timeDelay=1000000000,previousTime=0;
    private AnimationTimer animation;
    private Queue<Integer> nodeQueue= new LinkedList<>(),actionQueue= new LinkedList<>(),valueQueue= new LinkedList<>();
    private final int ORANGE=1,GREEN=2,TRANSLATE=3,ENABLEBUTTON=4,SORT=5;
    
    public Heap(){
        heapPane = new BorderPane();
        heapScene = new Scene(heapPane, SCENE_WIDTH, SCENE_HEIGHT);
        heapStage = new Stage();
        heapStage.setScene(heapScene);
        
        setCentre();
        setFooter();
        
        data = new int[201];
        node = new HeapNode[201];
        line = new Line[201];
        fakeData = new int[201];
        
        statLabel = new Label();
        statLabel.setLayoutX(10);
        statLabel.setLayoutY(10);
        
        srtLabel = new Label("Sorted data:");
        statLabel.setLayoutX(10);
        statLabel.setLayoutY(SCENE_HEIGHT-60-HEADER_HEIGHT-FOOTER_HEIGHT);
    }
    
    private void setCentre(){
        centrePane = new AnchorPane();
        heapPane.setCenter(centrePane);
        
        HeapButton maxButton = new HeapButton("Max Heap", SCENE_WIDTH/3, SCENE_HEIGHT/6);
        HeapButton minButton = new HeapButton("Min Heap", SCENE_WIDTH/3, SCENE_HEIGHT/6);
        
        maxButton.setLayoutX(SCENE_WIDTH/9);
        maxButton.setLayoutY(5*(SCENE_HEIGHT-FOOTER_HEIGHT)/12);
        
        minButton.setLayoutX(SCENE_WIDTH*2/9+SCENE_WIDTH/3);
        minButton.setLayoutY(5*(SCENE_HEIGHT-FOOTER_HEIGHT)/12);
        
        maxButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                factor=1;
                initializeHeap();
            }
        });
        minButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                factor=-1;
                initializeHeap();
            }
        });
        
        centrePane.getChildren().addAll(maxButton,minButton);
    }
    
    private void initializeHeap(){
        centrePane = new AnchorPane();
        heapHeader = new AnchorPane();
        
        Image image = new Image("view/resources/heap/header.png",SCENE_WIDTH,HEADER_HEIGHT,false,true);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.ROUND, BackgroundRepeat.ROUND, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        heapHeader.setBackground(new Background(backgroundImage));
        heapHeader.setPrefWidth(SCENE_WIDTH);
        heapHeader.setPrefHeight(HEADER_HEIGHT);
        
        TextField insertField = new TextField();
        insertField.setPromptText("Number to be inserted");
        insertField.setPrefHeight(HEADER_HEIGHT*2/3);
        insertField.setPrefWidth(SCENE_WIDTH/6);
        insertField.setLayoutY(HEADER_HEIGHT/5);
        insertField.setLayoutX(SCENE_WIDTH/24);
        
        insertButton = new HeapButton("INSERT", SCENE_WIDTH/6, HEADER_HEIGHT*2/3);
        insertButton.setLayoutX(SCENE_WIDTH/24*2+SCENE_WIDTH/6);
        insertButton.setLayoutY(HEADER_HEIGHT/5);
        insertButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int val;
                try
                {
                    val = Integer.parseInt(insertField.getText());
                    insertIntoHeap(val);
                    insertButton.setDisable(true);
                    insertField.clear();
                }catch(NumberFormatException e){}
            }
        });
        
        deleteButton = new HeapButton("DELETE", SCENE_WIDTH/6, HEADER_HEIGHT*2/3);
        deleteButton.setLayoutX(SCENE_WIDTH/24*7+SCENE_WIDTH/6*3);
        deleteButton.setLayoutY(HEADER_HEIGHT/5);
        deleteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                deleteRootHeap();
                deleteButton.setDisable(true);
            }
        });
        
        sortButton = new HeapButton("Sort", SCENE_WIDTH/6, HEADER_HEIGHT*2/3);
        sortButton.setLayoutY(HEADER_HEIGHT/5);
        sortButton.setLayoutX(SCENE_WIDTH/24*6+SCENE_WIDTH/6*2);
        sortButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                sortButton.setDisable(true);
                addActionColor(100, SORT);
            }
        });
        
        heapHeader.getChildren().addAll(insertField,insertButton,deleteButton,sortButton);
        heapPane.setTop(heapHeader);
        heapPane.setCenter(centrePane);
        
        activateAnimation();
    }
    
    private void fakeBuildHeap(){
        centrePane = new AnchorPane();
        heapPane.setCenter(centrePane);
        int height = (int)Math.floor((Math.log(cnt)/Math.log(2)) )+1;
        double blockHeight = (SCENE_HEIGHT-FOOTER_HEIGHT-HEADER_HEIGHT-60-30)/(height*2-1);
        double radius = Math.min(blockHeight/2, SCENE_WIDTH/(2*Math.pow(2, height-1)+1));
        centrePane.getChildren().addAll(statLabel,srtLabel);
        if(cnt!=0)
        {
            node[1]=new HeapNode(radius, radius, radius, fakeData[1]);
            node[1].setRadius(radius);
            node[1].setLayoutX(SCENE_WIDTH/2-radius);
            node[1].setLayoutY(20);
            
            node[1].setCircleX(SCENE_WIDTH/2);
            node[1].setCircleY(20+radius);
            
            centrePane.getChildren().addAll(node[1]);
        }
        for(int i=2;i<=cnt;i++)
        {
            int nodeHeight = findHeight(i);
            double layoutX = SCENE_WIDTH/2/Math.pow(2, nodeHeight-1)+(i-Math.pow(2, nodeHeight-1))*SCENE_WIDTH/2/Math.pow(2, nodeHeight-2)-radius;
            double layoutY = 20+2*(nodeHeight-1)*blockHeight;
            
            node[i]= new HeapNode(layoutX, layoutY, radius, fakeData[i]);
            node[i].setRadius(radius);
            node[i].setLayoutX(layoutX);
            node[i].setLayoutY(layoutY);
            
            node[i].setCircleX(layoutX+radius);
            node[i].setCircleY(layoutY+radius);
            
            double lineX = node[i/2].getCircleX() + radius*Math.sin( Math.atan( (node[i/2].getCircleY()-node[i].getCircleY())/(node[i/2].getCircleX()-node[i].getCircleX()) ) );
            double lineY = node[i/2].getCircleY() + Math.abs( radius*Math.cos( Math.atan( (node[i/2].getCircleY()-node[i].getCircleY())/(node[i/2].getCircleX()-node[i].getCircleX()) ) ) );
            
            double endY = node[i].getCircleY() + radius*Math.sin( Math.atan( (node[i/2].getCircleY()-node[i].getCircleY())/(node[i/2].getCircleX()-node[i].getCircleX()) ) );
            double endX = node[i].getCircleX() + Math.abs( radius*Math.cos( Math.atan( (node[i/2].getCircleY()-node[i].getCircleY())/(node[i/2].getCircleX()-node[i].getCircleX()) ) ) );
            
            line[i] = new Line(lineX,lineY,endX,endY);
            centrePane.getChildren().addAll(line[i],node[i]);
        }
    }
    
    private void buildHeap(){
        centrePane = new AnchorPane();
        heapPane.setCenter(centrePane);
        int height = (int)Math.floor((Math.log(cnt)/Math.log(2)) )+1;
        double blockHeight = (SCENE_HEIGHT-FOOTER_HEIGHT-HEADER_HEIGHT-60-30)/(height*2-1);
        double radius = Math.min(blockHeight/2, SCENE_WIDTH/(2*Math.pow(2, height-1)+1));
        centrePane.getChildren().addAll(statLabel,srtLabel);
        if(cnt!=0)
        {
            node[1]=new HeapNode(radius, radius, radius, data[1]);
            node[1].setRadius(radius);
            node[1].setLayoutX(SCENE_WIDTH/2-radius);
            node[1].setLayoutY(20);
            
            node[1].setCircleX(SCENE_WIDTH/2);
            node[1].setCircleY(20+radius);
            
            centrePane.getChildren().addAll(node[1]);
        }
        for(int i=2;i<=cnt;i++)
        {
            int nodeHeight = findHeight(i);
            double layoutX = SCENE_WIDTH/2/Math.pow(2, nodeHeight-1)+(i-Math.pow(2, nodeHeight-1))*SCENE_WIDTH/2/Math.pow(2, nodeHeight-2)-radius;
            double layoutY = 20+2*(nodeHeight-1)*blockHeight;
            
            node[i]= new HeapNode(layoutX, layoutY, radius, data[i]);
            node[i].setRadius(radius);
            node[i].setLayoutX(layoutX);
            node[i].setLayoutY(layoutY);
            
            node[i].setCircleX(layoutX+radius);
            node[i].setCircleY(layoutY+radius);
            
            double lineX = node[i/2].getCircleX() + radius*Math.sin( Math.atan( (node[i/2].getCircleY()-node[i].getCircleY())/(node[i/2].getCircleX()-node[i].getCircleX()) ) );
            double lineY = node[i/2].getCircleY() + Math.abs( radius*Math.cos( Math.atan( (node[i/2].getCircleY()-node[i].getCircleY())/(node[i/2].getCircleX()-node[i].getCircleX()) ) ) );
            
            double endY = node[i].getCircleY() + radius*Math.sin( Math.atan( (node[i/2].getCircleY()-node[i].getCircleY())/(node[i/2].getCircleX()-node[i].getCircleX()) ) );
            double endX = node[i].getCircleX() + Math.abs( radius*Math.cos( Math.atan( (node[i/2].getCircleY()-node[i].getCircleY())/(node[i/2].getCircleX()-node[i].getCircleX()) ) ) );
            
            line[i] = new Line(lineX,lineY,endX,endY);
            
            centrePane.getChildren().addAll(line[i],node[i]);
        }
    }
    
    private int findHeight(int number){
        return (int)Math.floor((Math.log(number)/Math.log(2)) )+1;
    }
    
    private void insertIntoHeap(int x)
    {
      int child=1;
      if(cnt<200)
      {
        statLabel.setText("Updating Heap");
        data[++cnt] = x;
        fakeData[cnt] = x;
        node[cnt] = new HeapNode(100, 100, 100, x);
        buildHeap();
        child = cnt;
        int par = child/2;
        while(par>0)
        {
            if(factor*data[par]-factor*data[child]<0)
            {
                int tempData = data[par];
                data[par]=data[child];
                data[child]=tempData;
                addActionTranslate(child, par);
            }
            else
                break;
            child = par;
            par = par/2;
        }
      }else statLabel.setText("OverFlow!");
        addActionColor(1, ENABLEBUTTON);
    }
    
    private void deleteRootHeap()
    {
        if(cnt==0)
        {
            centrePane = new AnchorPane();
            statLabel.setText("UnderFlow!");
            centrePane.getChildren().add(srtLabel);
            heapPane.setCenter(centrePane);
        }else{
            int ret, ind = 1, lc, rc, mx;
            
            ret=data[ind];
            data[ind]=data[cnt];
            data[cnt]=ret;
            addActionTranslate(ind, cnt);
            
            ret = data[cnt--];
            srtLabel.setText( srtLabel.getText()+" "+Integer.toString(ret));
            lc = ind*2;
            rc = (ind*2)+1;

            while(lc-cnt<=0)
            {
                if(rc-cnt<=0)
                    if(factor==1)
                        mx = Integer.max(data[lc], data[rc]);
                    else mx = Integer.min(data[lc], data[rc]);
                else
                    mx = data[lc];
                if(factor*mx-factor*data[ind]>0)
                {
                    if(data[lc] == mx)
                    {
                        int temp=data[lc];
                        data[lc]=data[ind];
                        data[ind]=temp;
                        addActionTranslate(ind, lc);
                        ind = lc;
                    }else
                    {
                        int temp=data[rc];
                          data[rc]=data[ind];
                        data[ind]=temp;
                        addActionTranslate(ind, rc);
                        ind = rc;
                    }

                }else
                    break;
                lc = ind*2;
                rc = (ind*2)+1;
            }
        }
        addActionColor(2, ENABLEBUTTON);
    }
    
    public void segmentStart(Stage homeStage)
    {
        this.homeStage = homeStage;
        homeStage.hide();
        heapStage.show();
        heapStage.setMaximized(true);
        heapStage.setMinWidth(SCENE_WIDTH+16);
        System.out.println(heapPane.getWidth()+" "+heapPane.getHeight());
    }
    
    private void setFooter(){
        heapFooter = new HBox();
        heapFooter.setPrefHeight(30);
        heapFooter.setStyle(FOOTER_STYLE);
        heapFooter.setPadding(new Insets(5,5,5,5));
        
        Button backButton = new Button("Back");
        
        try {
            backButton.setFont(Font.loadFont(new FileInputStream(Font_PATH), 13));
        } catch (FileNotFoundException e) {
            backButton.setFont(Font.font("Verdana", 13));
        }
        
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
        
        heapFooter.getChildren().addAll(backButton,timeField,setTimeDelay);
        heapPane.setBottom(heapFooter);
    }
    
    private void addActionColor(int node,int color){
        nodeQueue.add(node);
        actionQueue.add(color);
    }
    
    private void addActionTranslate(int node1,int node2){
        nodeQueue.add(node1);
        actionQueue.add(TRANSLATE);
        valueQueue.add(node2);
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
                        }else if(action == ORANGE){
                            node[nodeNo].changeToOrange();
                        }else if(action == TRANSLATE){
                            int tempNodeNo = valueQueue.remove();
                            
                            TranslateTransition t1 = new TranslateTransition();
                            t1.setNode(node[nodeNo]);
                            t1.setDuration(Duration.seconds(timeDelay/1000000000));
                            t1.setToX(-node[nodeNo].getLayoutX()+node[tempNodeNo].getLayoutX());
                            t1.setToY(-node[nodeNo].getLayoutY()+node[tempNodeNo].getLayoutY());
                            
                            TranslateTransition t2 = new TranslateTransition();
                            t2.setNode(node[tempNodeNo]);
                            t2.setDuration(Duration.seconds(timeDelay/1000000000));
                            t2.setToX(-node[tempNodeNo].getLayoutX()+node[nodeNo].getLayoutX());
                            t2.setToY(-node[tempNodeNo].getLayoutY()+node[nodeNo].getLayoutY());
                            
                            node[nodeNo].changeToOrange();
                            node[tempNodeNo].changeToOrange();
                            
                            HeapNode tempNode = node[nodeNo];
                            node[nodeNo] = node[tempNodeNo];
                            node[tempNodeNo] = tempNode;
                            
                            int temp = fakeData[nodeNo];
                            fakeData[nodeNo]= fakeData[tempNodeNo];
                            fakeData[tempNodeNo] = temp;
                            
                            t2.setOnFinished(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    fakeBuildHeap();
                                    node[nodeNo].changeToGreen();
                                    node[tempNodeNo].changeToGreen();
                                }
                            });
                            
                            t1.play();
                            t2.play();
                        }else if(action==ENABLEBUTTON){
                            if(nodeNo==1){
                                insertButton.setDisable(false);
                                statLabel.setText(" ");
                            }else if(nodeNo==2)
                                deleteButton.setDisable(false);
                            else
                                sortButton.setDisable(false);
                        }else if(action==SORT){
                            if(cnt!=0){
                                deleteButton.setDisable(true);
                                deleteRootHeap();
                                addActionColor(100, SORT);
                            }else{
                                addActionColor(3, ENABLEBUTTON);
                            }
                        }
                    }
                }
            }
        };
        animation.start();
    }
    
}
