/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algoreka;

import view.HomePage;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Algoreka extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        HomePage homePage = new HomePage();
        primaryStage = homePage.getHomeStage();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
