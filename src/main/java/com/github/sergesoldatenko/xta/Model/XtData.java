package com.github.sergesoldatenko.xta.Model;

import java.util.ArrayList;
import java.util.List;

public class XtData {
    public static final byte STATUS_EMPTY = 0;
    public static final String STATUS_EMPTY_LABEL = "Empty";
    public static final byte STATUS_LOADING = 1;
    public static final String STATUS_LOADING_LABEL = "Loading ...";
    public static final byte STATUS_LOADED = 2;
    public static final String STATUS_LOADED_LABEL = "Loaded";
    public static final byte STATUS_ERROR = 99;
    public static final String STATUS_ERROR_LABEL = "Error";

    private final XtDataHeader xtDataHeader;
    private List<XtDataRecord> dataRecords = new ArrayList<>();
    private XtDataHashInstruction xtDataHashInstruction;
    private XtDataHashParams xtDataHashParams;
    private XtDataHashFilename xtDataHashFilename;
    
    XtData(XtDataHeader xtDataHeader) {
        this.xtDataHeader = xtDataHeader;
    }
    
    public void save() {
        // to do:
        // * save parsed records
        // * update header data if needed
        
        saveRecords();
        // xtDataHeader.save();
    }
    private void saveRecords() {
        xtDataHeader.getXtRecordsNum();
        xtDataHeader.getXtRecordsNumOrig();
        //XtDataRecord.RECORD_LENGTH;
    }

    public void setStatus(byte status) {
        xtDataHeader.setStatus(status);
    }
    public byte getStatus() {
        return xtDataHeader.getStatus();
    }
    
    public void setXtFileOffset(long xtFileOffset) {
        xtDataHeader.setXtFileOffset(xtFileOffset);
    }
    
    public long getXtFileOffset(){
        return xtDataHeader.getXtFileOffset();
    }
    
    public void setXtRecordsNum(long xtRecords) {
        xtDataHeader.setXtRecordsNum(xtRecords);
    }

    public long getXtRecordsNum() {
        return xtDataHeader.getXtRecordsNum();
    }
    
    public void addDataRecord(XtDataRecord dataRecord) {
        // to do: update active record
        dataRecords.add(dataRecord);
    }

    public void setXtFilename(String xtFilename) {
        xtDataHeader.setXtFilename(xtFilename);
    }
    public String getXtFilename() {
        return xtDataHeader.getXtFilename();
    }

    public void setProjectName(String projectName) {
        xtDataHeader.setProjectName(projectName);
    }
    public String getProjectName() {
        return xtDataHeader.getProjectName();
    }

    public DataPath getActivePath() {
        return xtDataHeader.getActivePath();
    }
/*
    public void setActiveElement(DataPathElement element) {
        xtDataHeader.setActiveElement(element);
    }
    public DataPathElement getActiveElement() {
        return activePath
    }
*/
    public void setActiveRecordId(long id) {
        xtDataHeader.setActiveRecordId(id);
    }
    public long getActiveRecordId() {
        return xtDataHeader.getActiveRecordId();
    }

    public void init() {
        xtDataHeader.init();
    }

    public long setHashInstruction(String instruction) {
        return getDataHashInstruction().setRow(instruction);
    }

    public long setHashParams(String params) {
        return getDataHashParams().setRow(params);
    }

    public long setHashFilename(String filepath) {
        return getDataHashFilename().setRow(filepath);
    }
    
    public XtDataHashInstruction getDataHashInstruction() {
        if (xtDataHashInstruction == null) {
            xtDataHashInstruction = new XtDataHashInstruction();
        }
        return xtDataHashInstruction;
    }
    
    public XtDataHashParams getDataHashParams() {
        if (xtDataHashParams == null) {
            xtDataHashParams = new XtDataHashParams();
        }
        return xtDataHashParams;
    }
    
    public XtDataHashFilename getDataHashFilename() {
        if (xtDataHashFilename == null) {
            xtDataHashFilename = new XtDataHashFilename();
        }
        return xtDataHashFilename;
    }
}
