package com.github.sergesoldatenko.xta.Model;

import com.github.sergesoldatenko.xta.Model.Resources.HandleInUseException;
import com.github.sergesoldatenko.xta.Model.Resources.XtaFile;
import static com.github.sergesoldatenko.xta.Model.XtData.STATUS_EMPTY;
import static com.github.sergesoldatenko.xta.Model.XtData.STATUS_LOADING;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Project implements Runnable {
    private String projectName;
    private final String xtFilename;
    private XtData xtDataModel;
    private XtDataHeader xtDataHeaderModel;
    private XtFileParser xtFileParser;
    
    Project(File xtFile) throws FileNotFoundException, HandleInUseException {
        xtFilename = xtFile.getPath();
        int lastDotIndex = xtFilename.lastIndexOf('.');
        String xtaFilename = xtFilename;
        if (lastDotIndex > 0 && lastDotIndex > xtFilename.lastIndexOf(File.separatorChar)) {
            xtaFilename = xtFilename.substring(0, lastDotIndex);
        }
        projectName = xtaFilename;
        xtaFilename += ".xta";
        XtaFile xtaFile = XtaFile.getInstance();
        xtaFile.open(xtaFilename);
    }

    @Override
    public void run() {
        XtData xtData = getXtData();
        xtData.setXtFilename(xtFilename);
        xtData.setProjectName(projectName);
        xtData.setStatus(STATUS_EMPTY);
        xtData.init();
        try {
            startLoading();
        } catch (FileNotFoundException ex) {
            // to do:
            // process exception, show modal window with error and terminate thread
        } catch (IOException ex) {
            System.getLogger(Project.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            // to do:
            // show modal window with error and terminate thread
        }

        int i = 0;
        while (!Thread.currentThread().isInterrupted()) {
            System.out.println(i++);
            try {
                if (xtData.getStatus() == STATUS_LOADING) {
                    resumeLoading();
                    xtData.save();
                    // to do:
                    // notify view to add elements
                }
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                System.getLogger(Project.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            } catch (FileNotFoundException ex) {
                // to do:
                // process exception
            } catch (IOException ex) {
                System.getLogger(Project.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        }
    }

    private void startLoading() throws FileNotFoundException, IOException {
        if (getXtData().getStatus() == STATUS_EMPTY) {
            getParser().startLoading();
        }
    }
        
    private void resumeLoading() throws FileNotFoundException, IOException {
        getParser().resumeLoading();
        if (getXtData().getStatus() != STATUS_LOADING) {
            // Parser no needed anymore. Dereference for GC. 
            xtFileParser.close();
            xtFileParser = null;
        }
    }

    private XtData getXtData() {
        if (xtDataModel == null) {
            xtDataModel = new XtData(getXtDataHeader());
        }
        return xtDataModel;
    }
    
    private XtDataHeader getXtDataHeader() {
        if (xtDataHeaderModel == null) {
            xtDataHeaderModel = new XtDataHeader();
        }
        return xtDataHeaderModel;
    }
    
    private XtFileParser getParser() throws FileNotFoundException {
        if (this.xtFileParser == null) {
            this.xtFileParser = new XtFileParser(xtFilename, new XtDataPopulate(getXtDataHeader()));
        }
        
        return this.xtFileParser;
    }
}
