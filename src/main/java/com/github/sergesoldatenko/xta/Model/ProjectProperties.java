package com.github.sergesoldatenko.xta.Model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class ProjectProperties {
    private String propertyFilename = "xta.properties";
    private Properties properties = new Properties();
    private boolean isLoaded = false;
    
    public void loadProperties() {
        if (isLoaded) {
            return;
        }
        try (InputStream input = new FileInputStream(propertyFilename)) {
            properties.load(input);
            isLoaded = true;
        } catch (IOException ex) {
            ex.printStackTrace();
        }    
    }
    
    public void saveProperties() {
        try (OutputStream output = new FileOutputStream(propertyFilename)) {
            properties.store(output, "This is a new properties file");

        } catch (IOException io) {
            io.printStackTrace();
        }
    }
    
    public String getLastDirectory() {
        if (!isLoaded) {
            loadProperties();
        }
        return properties.getProperty("last_directory");
    }
    
    public void setLastDirectory(String path) {
        if (!path.equals(properties.getProperty("last_directory"))) {
            properties.setProperty("last_directory", path);
            saveProperties();
        }
    }
}
