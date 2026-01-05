package com.github.sergesoldatenko.xta;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import static com.github.sergesoldatenko.xta.ui.MenuBuilder.createMenuBar;
import static com.github.sergesoldatenko.xta.ui.Center.createCenter;
import javafx.scene.Parent;

/**
 * JavaFX App
 */
public class App extends Application {
    
    private static Scene scene;
    
    @Override
    public void start(Stage stage) {
        //MenuBar menuBar = MenuBuilder.createMenuBar();
        
        BorderPane root = new BorderPane(); // StackPane(label);
        //root.setTop(menuBar);
        scene = new Scene(root, 640, 480);
        stage.setScene(scene);
        createMenuBar();
        createCenter();
        stage.show();
    }

    static void setRoot(Parent parent) {
        scene.setRoot(parent);
    }
    
    public static Parent getRoot() {
        return scene.getRoot();
    }
    
    public static void main(String[] args) {
        launch();
    }
}