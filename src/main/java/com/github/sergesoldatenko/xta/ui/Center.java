package com.github.sergesoldatenko.xta.ui;

import com.github.sergesoldatenko.xta.App;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Center {
    
    private static final VBox vBox = new VBox();
    private static final TabPane tabPane = new TabPane();
    
    public static void createCenter() {
        HBox hBox = new HBox(vBox, tabPane);
        BorderPane root = (BorderPane)App.getRoot();
        root.setCenter(hBox);        
    }
    
    public static TabPane getTabPane() {
        return tabPane;
    } 
}
