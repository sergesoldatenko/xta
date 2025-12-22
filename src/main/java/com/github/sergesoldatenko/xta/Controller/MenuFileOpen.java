package com.github.sergesoldatenko.xta.Controller;

import static com.github.sergesoldatenko.xta.Model.ProjectManager.createProject;
import com.github.sergesoldatenko.xta.Model.ProjectProperties;
import static com.github.sergesoldatenko.xta.ui.Center.getTabPane;
import java.io.File;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.PopupWindow;

public class MenuFileOpen {
    public void execute(ActionEvent event) {
        MenuItem sourceMenuItem = (MenuItem) event.getSource();
        PopupWindow parentPopup = sourceMenuItem.getParentPopup();
        Stage stage = (Stage) parentPopup.getOwnerWindow();
                
        FileChooser fileChooser = new FileChooser();
        ProjectProperties properties = new ProjectProperties();
        String lastDirectory = properties.getLastDirectory();
        if (lastDirectory != null) {
            fileChooser.setInitialDirectory(new File(lastDirectory));
        }
        
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("xt Files", "*.xt"),
            //new FileChooser.ExtensionFilter("xt Files", "*.xta"),
            new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            File parentFile = selectedFile.getParentFile();
            String parentFileStr = parentFile.toString();
            properties.setLastDirectory(parentFileStr);
            createProject(selectedFile);
            TabPane tabPane = getTabPane();
            Tab tab1 = new Tab("Tab 1");
            tab1.setContent(new Label("Content for Tab 1"));
            tabPane.getTabs().add(tab1);
            // to do:
            // XtFile = XtRepository.open(selectedFile);
            // create tab and bind with XtFile
        }
    }
}
