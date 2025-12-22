package com.github.sergesoldatenko.xta.ui;

import com.github.sergesoldatenko.xta.App;
import com.github.sergesoldatenko.xta.Controller.MenuFileOpen;
import javafx.event.ActionEvent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;

public class MenuBuilder {
    public static void createMenuBar() {
        MenuBar menuBar = new MenuBar();

        // Create Menus
        Menu fileMenu = new Menu("File");
        Menu helpMenu = new Menu("Help");

        // Create MenuItems for File Menu
        MenuItem item1 = new MenuItem("Open");
        item1.setOnAction((ActionEvent event) -> {
            var fileOpenController = new MenuFileOpen();
            fileOpenController.execute(event);
        });

        MenuItem item2 = new MenuItem("Content 2");

        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(event -> System.exit(0));

        // Add MenuItems to the Menus
        fileMenu.getItems().addAll(item1, item2, exitItem);

        // Add Menus to the MenuBar
        menuBar.getMenus().addAll(fileMenu, helpMenu);

        BorderPane root = (BorderPane)App.getRoot();
        root.setTop(menuBar);
    } 
}
