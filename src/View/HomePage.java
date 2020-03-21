package View;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import model.HomeButton;

public class HomePage {
    
    private static final int HEIGHT=700;
    private static final int WIDTH=1000;
    private AnchorPane homePane;
    private Scene homeScene;
    private Stage homeStage;
    
    public HomePage(){
        homePane = new AnchorPane();
        homeScene = new Scene(homePane,WIDTH,HEIGHT);
        homeStage = new Stage();
        homeStage.setScene(homeScene);
        addButton("Hello");
    }
    
    public Stage getHomeStage(){
        return homeStage;
    }
    
    private void addButton(String text){
        HomeButton wButton = new HomeButton(text);
        homePane.getChildren().add(wButton);
    }
}
