package com.github.sergesoldatenko.xta.Model;

import com.github.sergesoldatenko.xta.Model.Resources.HandleInUseException;
import java.io.File;
import java.io.FileNotFoundException;
//import java.util.LinkedHashMap;
//import java.util.Map;

public class ProjectManager {
    //private static final Map<String, Project> projects = new LinkedHashMap();
    private static String projectName = "";
    private static Project projectObj;
    private static Thread thread;
    
    public static boolean createProject(File xtFile) {
        // to do:
        // create project from xt file
        String name = xtFile.getPath();
        int lastDotIndex = name.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex > name.lastIndexOf(File.separatorChar)) {
            name = name.substring(0, lastDotIndex);
        }
        //if (projects.containsKey(name)) {
        if (projectName.equals(name)) {
            // to do: 
            // show notification that project already opened
            return false;
        }
        if (!projectName.isEmpty()) {
            closeProject();
        }
        //projects.put(name, new Project());
        projectName = name;
        try {
            projectObj = new Project(xtFile);
        } catch (FileNotFoundException | HandleInUseException ex) {
            // to do:
            // show notification about exception. 
            // Maybe suggest to create a file manually and set permissions. TBD
            System.getLogger(ProjectManager.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            return false;
        }
        thread = new Thread(projectObj);
        thread.start();        
        
        return true;
    }
    
    public static boolean openProject(File projectFile) {
        // to do:
        // Open project from xta file
        return true;
    }
    public static void closeProject() {
        // to do:
        // close current project and prompt to save changes
        projectName = "";
        projectObj = null;
        if (thread != null) {
            thread.interrupt();
            try {
                thread.join();
            } catch (InterruptedException ex) {
                System.getLogger(ProjectManager.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }
    }
}


