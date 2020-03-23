package view;

import java.util.ArrayList;
import java.util.List;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import model.HomeButton;

public class HomePage {
    
    private final static int OBUTTON_START_X=150;
    private final static int OBUTTON_START_Y=150;
    private static final int HEIGHT=539;
    private static final int WIDTH=960;
    private AnchorPane homePane;
    private Scene homeScene;
    private Stage homeStage;
    
    List<HomeButton> optionButtons;
    
    public HomePage(){
        optionButtons= new ArrayList();
        
        homePane = new AnchorPane();
        homeScene = new Scene(homePane,WIDTH,HEIGHT);
        homeStage = new Stage();
        homeStage.setScene(homeScene);
        homeStage.setMinWidth(WIDTH+20);
        homeStage.setMinHeight(HEIGHT+40);
        
        addButton();
        
        createBackgroundImg();
        addWelcomeLogo();
    }
    
    public Stage getHomeStage(){
        return homeStage;
    }
    
    private void addOptionButton(HomeButton button){
        optionButtons.add(button);
        button.setLayoutX(OBUTTON_START_X+ 250*(optionButtons.size()/5));
        button.setLayoutY(OBUTTON_START_Y+( (optionButtons.size()-1)%4 )*100);
        homePane.getChildren().add(button);
    }
    
    private void addButton(){
        createAVLTreeButton();
        createSegmentTreeButton();
        createHeapButton();
        createArticulationPointButton();
        createArticulationBridgeButton();
    }
    
    private void createAVLTreeButton(){
        HomeButton button= new HomeButton("AVL Tree");
        addOptionButton(button);
    }
    
    private void createSegmentTreeButton(){
        HomeButton button= new HomeButton("Segment Tree");
        addOptionButton(button);
    }
    
    private void createHeapButton(){
        HomeButton button= new HomeButton("Heap");
        addOptionButton(button);
    }
    
    private void createArticulationPointButton(){
        HomeButton button= new HomeButton("Articulation Point");
        addOptionButton(button);
    }
    
    private void createArticulationBridgeButton(){
        HomeButton button= new HomeButton("Articulation Bridge");
        addOptionButton(button);
    }
    
    private void createBackgroundImg(){
        Image backgroundImage = new Image("view/resources/background1.png", WIDTH, HEIGHT, false, true);
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.ROUND, BackgroundRepeat.ROUND, BackgroundPosition.DEFAULT, null);
        homePane.setBackground(new Background(background));
    }
    
    private void addWelcomeLogo(){
        ImageView logo= new ImageView("view/resources/welcome_logo.png");
        logo.setLayoutX(200);
        logo.setLayoutY(0);
        
        logo.setOnMouseEntered(new EventHandler<MouseEvent>(){
            public void handle(MouseEvent event){
                logo.setEffect(new DropShadow());
            }
        });
        
        logo.setOnMouseExited(new EventHandler<MouseEvent>(){
            public void handle(MouseEvent event){
                logo.setEffect(null);
            }
        });
        
        homePane.getChildren().add(logo);
    }
}
