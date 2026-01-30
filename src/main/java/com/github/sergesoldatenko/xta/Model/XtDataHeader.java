package com.github.sergesoldatenko.xta.Model;

import com.github.sergesoldatenko.xta.Model.Resources.XtaFile;
import static com.github.sergesoldatenko.xta.Model.XtData.STATUS_EMPTY;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class XtDataHeader {

    private String xtaVersionSignature = "XTA1"; // default XTA1

    // header data
    private byte status;
    private long xtFileOffset;
    private long xtRecords;
    // private int xtRecordLength = 0; - no need for now. Maybe used when added fields length dynamic management.
    private String xtFilename;
    private String projectName;
    private long activeRecordId = 0;
    private DataPath activePath = new DataPath();
    
    // copy of loaded header data
    private byte orig_status;
    private long orig_xtFileOffset;
    private long orig_xtRecords;
    // private int orig_xtRecordLength; - no need for now. Maybe used when added fields length dynamic management.
    private String orig_xtFilename;
    private String orig_projectName;
    private long orig_activeRecordId;
    
    private final int HEADER_SIZE = 4096;
    private final int FIXED_VARIABLES_SIZE = 31;
    private final int POSITION_HEADER_LENGTH = 4;
    private final int POSITION_METADATA_LENGTH = 6;
    private final int POSITION_STATUS = 8;
    private final int POSITION_XT_FILE_OFFSET = 9;
    private final int POSITION_XT_RECORDS = 13;
    private final int POSITION_ACTIVE_RECORD = 17;
    private final int POSITION_METADATA = 25;
    private ByteBuffer header;

    public String getVersion() {
        return xtaVersionSignature;
    }
    
    public void setXtFilename(String xtFilename) {
        this.xtFilename = xtFilename;
    }
    
    public String getXtFilename() {
        return this.xtFilename;
    }
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    
    public String getProjectName() {
        return this.projectName;
    }
    
    public void setStatus(byte status) {
        this.status = status;
    }
    
    public byte getStatus() {
        return this.status;
    }
    
    public void setXtFileOffset(long xtFileOffset) {
        this.xtFileOffset = xtFileOffset;
    }
    
    public long getXtFileOffset(){
        return this.xtFileOffset;
    }
    
    public void setXtRecordsNum(long xtRecords) {
        this.xtRecords = xtRecords;
    }

    public long getXtRecordsNum() {
        return this.xtRecords;
    }

    public long getXtRecordsNumOrig() {
        return this.orig_xtRecords;
    }

    public void setActiveRecordId(long id) {
        activeRecordId = id;
    }
    public long getActiveRecordId() {
        return activeRecordId;
    }
    public long getActiveRecordIdOrig() {
        return orig_activeRecordId;
    }

    /*
    public int getRecordLength() {
        return this.xtRecordLength;
    }
    public void setRecordLength(int recordLength) {
        this.xtRecordLength = recordLength;
    }*/
    
    // -----------------
    public String getMetadata() {
        return xtFilename + '\0' + projectName + '\0';
    }
    
    private void loadXtaHeader() {
        XtaFile xtaFile = XtaFile.getInstance();
        boolean headerLoaded = false;
        ByteBuffer miniHeader = ByteBuffer.allocate(6);
        byte[] bytes;
        try {
            int bytesRead = xtaFile.read(miniHeader);
            if (bytesRead == 6) {
                bytes = new byte[4];
                miniHeader.position(0);
                miniHeader.get(bytes);
                // xtaSignature not used for now. Probably will be used in future versions.
                String xtaSignature = new String(bytes, StandardCharsets.UTF_8);
                int headerLength = miniHeader.getChar();
                header = ByteBuffer.allocate(headerLength);
                bytesRead = xtaFile.read(header, 0);
                if (bytesRead == headerLength) {
                    headerLoaded = true;
                }
            }
        } catch (IOException ex) {
            System.getLogger(XtDataHeader.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
        if (!headerLoaded) {
            createXtaHeader();
        } else {
            // fill object properties from loaded header
            status = header.get(POSITION_STATUS);
            orig_status = status;
            xtFileOffset = header.getLong(POSITION_XT_FILE_OFFSET);
            orig_xtFileOffset = xtFileOffset;
            xtRecords = header.getLong(POSITION_XT_RECORDS);
            orig_xtRecords = xtRecords;
            int metadataLength = header.getChar(POSITION_METADATA_LENGTH);
            byte[] metadata = new byte[metadataLength];
            header.position(POSITION_METADATA);
            header.get(metadata, 0, metadataLength);
         
            //header.get(POSITION_METADATA, metadata, 0, metadataLength);
            // Parse Metadata
            int xtFilenameLength = indexOfByte(metadata, (byte) 0x00, 0);
            if (xtFilenameLength > 0) {
                xtFilename = new String(metadata, 0, xtFilenameLength);
                orig_xtFilename = xtFilename;
                int projectNameLength = indexOfByte(metadata, (byte) 0x00, xtFilenameLength + 1) - xtFilenameLength;
                if (projectNameLength > 0) {
                    projectName = new String(metadata, xtFilenameLength + 1, projectNameLength);
                    orig_projectName = projectName;
                }
            }

            activeRecordId = header.getLong(POSITION_ACTIVE_RECORD);
            orig_activeRecordId = activeRecordId;
            loadDataPath();
        }
    }

    private void createXtaHeader() {
        XtaFile xtaFile = XtaFile.getInstance();
        header = generateXtaHeader();
        try {
            xtaFile.write(header, 0);
        } catch (IOException ex) {
            System.getLogger(XtDataHeader.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    private ByteBuffer generateXtaHeader() {
        String metadata = getMetadata();
        int headerLength = metadata.length() + FIXED_VARIABLES_SIZE;
        //ByteBuffer header = ByteBuffer.allocate(headerLength);
        ByteBuffer header = ByteBuffer.allocate(HEADER_SIZE);
        header.put("XTA1".getBytes());
        header.putChar((char)headerLength);
        header.putChar((char)metadata.length());
        header.put(status);
        header.putLong(xtFileOffset);
        header.putLong(xtRecords);
        header.putLong(activeRecordId);
        header.put(metadata.getBytes());
        header.position(0);
        
        return header;
    }

    public int getHeaderSize() {
        return HEADER_SIZE;
    }

    public void init() {
        if (status == STATUS_EMPTY) {
            loadXtaHeader();
        }
    }
    
    public void save() {
        if (origDataChanged()) {
            // to do: update header

            // to do: update active path on header saving?
            //activePath.getElements();
        }
    }

    public DataPath getActivePath() {
        return activePath;
    }
    
    private boolean origDataChanged() {
        return !(
            orig_status == status
            && orig_xtFileOffset == xtFileOffset
            && orig_xtRecords == xtRecords
            && orig_xtFilename.equals(xtFilename)
            && orig_projectName.equals(projectName)
        );
    }

    private int indexOfByte(byte[] array, byte bytecode) {
        return indexOfByte(array, bytecode, 0);
    }
    
    private int indexOfByte(byte[] array, byte bytecode, int startFrom) {
        for (int i = startFrom; i < array.length; i++) {
            if (array[i] == bytecode) {
                return i;
            }
        }
        return -1;
    }

    private void loadDataPath() {
        DataPathRepository dataPathRepo = new DataPathRepository(this);
        activePath = dataPathRepo.loadById(activeRecordId);
    }
}
