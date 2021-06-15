package view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.DriverManager;
import java.util.LinkedList;
import java.util.Queue;
import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.AnimatorAvlNode;
import model.AvlNode;
import model.AvlTheoryNode;
import model.HeapButton;
import model.TestAnimator;
import model.TestNode;

public class AVLtree {
    
    private final double SCENE_WIDTH = Screen.getPrimary().getVisualBounds().getWidth();
    private final double SCENE_HEIGHT = Screen.getPrimary().getVisualBounds().getHeight();
    private final double FOOTER_HEIGHT = 30;
    private final double HEADER_HEIGHT = 80;
    private final double CENTRE_HEIGHT = SCENE_HEIGHT-FOOTER_HEIGHT-HEADER_HEIGHT;
    
    private final String FOOTER_STYLE="-fx-background-repeat: round;-fx-background-color: aqua;";
    private final String Font_PATH = "src/model/heapResources/Drifttype.ttf";
    
    private BorderPane avlPane;
    private HBox avlFooter;
    private AnchorPane avlHeader;
    private AnchorPane centrePane;
    private Scene avlScene;
    private Stage avlStage;
    private Stage homeStage;
    
    private TextField insertField;
    private TextField deleteField;
    private HeapButton insertButton;
    private HeapButton deleteButton;
    private Button traverseButton;
    private ChoiceBox<String> traverseBox;
    
    private double radius,blockHeight;
    private int treeHeight,cnt;
    private TestNode root,tempNode;
    private TestAnimator tempAnimator;
    
    private long timeDelay=2000000000,previousTime=0;
    private AnimationTimer animation;
    private Queue<Integer> nodeQueue= new LinkedList<>(),
            actionQueue= new LinkedList<>(),valueQueue= new LinkedList<>();
    private Queue<Double> doubleQueue = new LinkedList<>();
    private final int ORANGE=1,GREEN=2,TRANSLATE=3,ENABLEBUTTON=4,SORT=5,MOVE=6,
            BUILD=7;
    
    
    public AVLtree(){
        avlPane = new BorderPane();
        avlStage = new Stage();
        avlScene = new Scene(avlPane, SCENE_WIDTH, SCENE_HEIGHT);
        avlStage.setScene(avlScene);
        
        setFooter();
        setHeader();
    }
    
    private void setFooter(){
        avlFooter = new HBox();
        avlFooter.setPrefHeight(30);
        avlFooter.setStyle(FOOTER_STYLE);
        avlFooter.setPadding(new Insets(5,5,5,5));
        
        Button backButton = new Button("Back");
        
        try {
            backButton.setFont(Font.loadFont(new FileInputStream(Font_PATH), 13));
        } catch (FileNotFoundException e) {
            backButton.setFont(Font.font("Verdana", 13));
        }
        
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                avlStage.hide();
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
        
        avlFooter.getChildren().addAll(backButton,timeField,setTimeDelay);
        avlPane.setBottom(avlFooter);
    }
    
    private void setHeader(){
        avlHeader = new AnchorPane();
         
        Image image = new Image("view/resources/avl/header.png",SCENE_WIDTH,HEADER_HEIGHT,false,true);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.ROUND, BackgroundRepeat.ROUND, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        avlHeader.setBackground(new Background(backgroundImage));
        avlHeader.setPrefWidth(SCENE_WIDTH);
        avlHeader.setPrefHeight(HEADER_HEIGHT);
        
        insertField = new TextField();
        insertField.setPromptText("Number to be inserted");
        insertField.setPrefHeight(HEADER_HEIGHT*2/3);
        insertField.setPrefWidth(SCENE_WIDTH/6);
        insertField.setLayoutX(SCENE_WIDTH/36);
        insertField.setLayoutY(HEADER_HEIGHT/6);
        
        insertButton = new HeapButton("Insert", SCENE_WIDTH/6, HEADER_HEIGHT*2/3);
        insertButton.setLayoutX(SCENE_WIDTH*2/36+SCENE_WIDTH/6);
        insertButton.setLayoutY(HEADER_HEIGHT/6);
        insertButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try{
                    int i;
                    i = Integer.parseInt(insertField.getText());
                    insertIni(i);
                }catch(NumberFormatException e){}
            }
        });
        
        deleteField = new TextField();
        deleteField.setPromptText("Number to be deleted");
        deleteField.setPrefHeight(HEADER_HEIGHT*2/3);
        deleteField.setPrefWidth(SCENE_WIDTH/6);
        deleteField.setLayoutX(SCENE_WIDTH*3/36 + SCENE_WIDTH*2/6);
        deleteField.setLayoutY(HEADER_HEIGHT/6);
        
        deleteButton = new HeapButton("Delete", SCENE_WIDTH/6, HEADER_HEIGHT*2/3);
        deleteButton.setLayoutX(SCENE_WIDTH*4/36+SCENE_WIDTH*3/6);
        deleteButton.setLayoutY(HEADER_HEIGHT/6);
        
        traverseBox = new ChoiceBox<>();
        traverseBox.setPrefWidth(SCENE_WIDTH/6);
        traverseBox.setPrefHeight(HEADER_HEIGHT/3);
        traverseBox.getItems().addAll("Inorder","Preorder","Postorder");
        traverseBox.setLayoutX(SCENE_WIDTH*5/36+SCENE_WIDTH*4/6);
        traverseBox.setLayoutY(HEADER_HEIGHT/6);
        traverseBox.setValue("Inorder");
        
        traverseButton = new Button("Traverse");
        traverseButton.setPrefWidth(SCENE_WIDTH/6);
        traverseButton.setPrefHeight(HEADER_HEIGHT/3);
        traverseButton.setLayoutX(SCENE_WIDTH*5/36+SCENE_WIDTH*4/6);
        traverseButton.setLayoutY(HEADER_HEIGHT*1/6+HEADER_HEIGHT/3);
        
        avlHeader.getChildren().addAll(insertField,insertButton,deleteField,
                deleteButton,traverseBox,traverseButton);
        
        avlPane.setTop(avlHeader);
    }
    
    private void insertIni(int i){
        buildTree();
        root = insert(root, i);
        addActonBuild();
    }
    
    private void buildTree(){
        centrePane = new AnchorPane();
        avlPane.setCenter(centrePane);
        
        treeHeight = (int)(1.44*Math.floor((Math.log(cnt)/Math.log(2)) )+2);
        blockHeight = (CENTRE_HEIGHT-30-30)/(treeHeight*2-1);
        radius = Math.min(blockHeight/2, SCENE_WIDTH/(2*Math.pow(2, treeHeight-1)+1));
        
        addTestNode(root,0,SCENE_WIDTH,1,false,null,1);
    }
    
    private void addTestNode(TestNode node,double st,double end, int height,
            boolean isLine, TestNode preVisNode,int pos){
        if(node != null){
            node.setlCoor(st);
            node.setrCoor(end);
            
            node.setRadius(radius);
            node.getCircle().setCenterX( (st+end)/2 );
            node.getCircle().setCenterY(30+ blockHeight/2 + (height-1)*blockHeight*2);
            node.setyCoor(node.getCircle().getCenterY());
            
            if(isLine){
                if(pos==1){
                    preVisNode.setRLine(node.getTLine());
                }else{
                    preVisNode.setLLine(node.getTLine());
                }
                centrePane.getChildren().add(node.getTLine());
                preVisNode.getCircle().setCenterX(
                    preVisNode.getCircle().getCenterX()+1);
                preVisNode.getCircle().setCenterY(
                    preVisNode.getCircle().getCenterY()+1);
            }
            
            centrePane.getChildren().addAll(node.getCircle(),node.getBfLabel(),node.getKeyLabel());
            node.getCircle().setCenterX( (st+end)/2+1 );
            node.getCircle().setCenterY( node.getCircle().getCenterY()+1 );
            addTestNode(node.getLeft(), st, (st+end)/2, height+1, true, node,-1);
            addTestNode(node.getRight(), (st+end)/2, end, height+1, true, node,1);
        }
    }
    
    private void addNode(AvlTheoryNode node,double st,double end, int height,
            boolean isLine, AvlNode preVisNode){
        if(node != null){
            AvlNode visNode = node.getNode();
            visNode.setRadius(radius);
            visNode.setPrefSize(2*radius, 2*radius);
            visNode.setCircleY(30+radius + (height-1)*blockHeight*2 );
            visNode.setCircleX( (st+end)/2 - radius );
            visNode.setLayoutY(30 + (height-1)*blockHeight*2);
            visNode.setLayoutX((st+end)/2);
            
            if(isLine){
                visNode.getLine().startXProperty().bind(preVisNode.layoutXProperty().add(preVisNode.getRadius()));
                visNode.getLine().startYProperty().bind(preVisNode.layoutYProperty().add(2*preVisNode.getRadius()));
                centrePane.getChildren().add(visNode.getLine());
            }
            
            centrePane.getChildren().add(visNode);
            addNode(node.getLeft(), st, (st+end)/2, height+1, true, visNode);
            addNode(node.getRight(), (st+end)/2, end, height+1, true, visNode);
        }
    }
    
    public void avlStart(Stage homeStage){
        this.homeStage = homeStage;
        homeStage.hide();
        avlStage.show();
        avlStage.setMaximized(true);
        avlStage.setMinWidth(SCENE_WIDTH+16);
        activateAnimation();
    }
    
    int calcHeight(TestNode N)  
    {
        int ret=0;
        if (N != null)    
            ret = N.getHeight();
        return ret;
    }
    
    int max(int a, int b)  
    {  
        return (a > b)? a : b;  
    }
    
    TestNode rightRotate(TestNode y){  
        TestNode x = y.getLeft();  
        TestNode T2 = x.getRight();  
  
        // Perform rotation  
        x.setRight(y);  
        y.setLeft(T2);  
  
        // Update heights  
        y.setHeight( max( calcHeight(y.getLeft()), 
                    calcHeight(y.getRight()) ) + 1);  
        x.setHeight( max( calcHeight(x.getLeft()), 
                    calcHeight(x.getRight())) + 1);  
  
        // Return new root  
        return x;  
    }
    
    TestNode leftRotate(TestNode x){  
        TestNode y = x.getRight();  
        TestNode T2 = y.getLeft();  
  
        // Perform rotation  
        y.setLeft(x);  
        x.setRight(T2);  
  
        // Update heights  
        x.setHeight( max(calcHeight(x.getLeft()),     
                    calcHeight(x.getRight())) + 1);  
        y.setHeight( max(calcHeight(y.getLeft()),  
                    calcHeight(y.getRight())) + 1);  
  
        // Return new root  
        return y;  
    }
    
    private int getBalance(TestNode N)  
    {
        int ret;
        if (N == null)  
            ret = 0;  
        else{
            ret = calcHeight(N.getLeft()) - calcHeight(N.getRight());
            N.setBf(ret);
        }
        return ret;
    }
    
    private TestNode insert(TestNode node, int key){  
        /* 1. Perform the normal BST insertion */
        if (node == null)  
        {
            cnt++;
            TestNode newNode = new TestNode(key, 0, radius, radius, radius);
            return(newNode);
        }  
  
        if (key < node.getKey()){
            addActionMove(key, node, node.getLeft().getCircle().getCenterX(),
                node.getLeft().getCircle().getCenterY());

            node.setLeft(insert(node.getLeft(), key));
        }
        else if (key > node.getKey()){
            if(node.getRight()!=null){
                addActionMove(key, node, node.getRight().getCircle().getCenterX(),
                            node.getRight().getCircle().getCenterY());
            }
            node.setRight(insert(node.getRight(), key));   
        }else // Equal keys are not allowed in BST  
            return node;  
  
        /* 2. Update height of this ancestor node */
        node.setHeight(1 + max(calcHeight(node.getLeft()),  
                        calcHeight(node.getRight())));  
  
            /* 3. Get the balance factor of this ancestor  
            node to check whether this node became  
            unbalanced */
        int balance = getBalance(node);  
  
        // If this node becomes unbalanced, then  
        // there are 4 cases  
  
        // Left Left Case  
        if (balance > 1 && key < node.getLeft().getKey())  
            return rightRotate(node);  
  
        // Right Right Case  
        if (balance < -1 && key > node.getRight().getKey())  
            return leftRotate(node);  
  
        // Left Right Case  
        if (balance > 1 && key > node.getLeft().getKey())  
        {  
            node.setLeft(leftRotate(node.getLeft()));  
            return rightRotate(node);  
        }  
  
        // Right Left Case  
        if (balance < -1 && key < node.getRight().getKey())  
        {  
            node.setRight(rightRotate(node.getRight()));  
            return leftRotate(node);  
        }  
  
        /* return the (unchanged) node pointer */
        return node;  
    }
    
    private void addActionMove(int key,TestNode fromNode,double centerX,double centerY){
        nodeQueue.add(key);
        actionQueue.add(MOVE);
        doubleQueue.add(fromNode.getCircle().getCenterX());
        doubleQueue.add(fromNode.getCircle().getCenterY());
        doubleQueue.add(centerX);
        doubleQueue.add(centerY);
    }
    
    private void addActonBuild(){
        nodeQueue.add(0);
        actionQueue.add(BUILD);
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
                        
                        if(tempNode!=null) centrePane.getChildren().
                                removeAll(tempNode.getCircle(),tempNode.getKeyLabel());
                        if(tempAnimator!=null){
                            tempAnimator.stop();
                            tempAnimator = null;
                        }
                        
                        if(action == GREEN){
                            //node[nodeNo].changeToGreen();
                        }else if(action == ORANGE){
                            //node[nodeNo].changeToOrange();
                        }else if(action == BUILD){
                            buildTree();
                        }else if(action==MOVE){
                            tempNode = new TestNode(nodeNo, 0, radius, radius, radius);
                            tempAnimator = new TestAnimator(doubleQueue.remove(),
                                    doubleQueue.remove(), doubleQueue.remove(),
                                    doubleQueue.remove(), tempNode, timeDelay/1000000000);
                            tempNode.getCircle().setFill(Color.rgb(152, 22, 208, 0.48));
                            centrePane.getChildren().addAll(tempNode.getCircle(),tempNode.getKeyLabel());
                            tempAnimator.start();
                        }
                    }
                }
            }
        };
        animation.start();
    } 
    
}
