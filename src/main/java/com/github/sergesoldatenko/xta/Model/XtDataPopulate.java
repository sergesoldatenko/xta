package com.github.sergesoldatenko.xta.Model;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Active only when xt-File is parsed
 * 
 */
public class XtDataPopulate extends XtData {
    private List<XtDataRecord> dataRecords = new ArrayList<>();
    
    public XtDataPopulate(XtDataHeader xtDataHeader) {
        super(xtDataHeader);
    }
    
    @Override
    public void save() {
        // to do:
        // * save parsed records
        // * update header data if needed
        
        saveRecords();
        // xtDataHeader.save();
    }
    private void saveRecords() {
        //xtDataHeader.getXtRecordsNum();
        //xtDataHeader.getXtRecordsNumOrig();
        //XtDataRecord.RECORD_LENGTH;

        long activeRecordId = xtDataHeader.getActiveRecordId();
        long origActiveRecordId = xtDataHeader.getActiveRecordIdOrig();
        int bytesToWrite = (int)(activeRecordId - origActiveRecordId) * XtDataRecord.RECORD_LENGTH;
        ByteBuffer dataToSave;
        dataToSave = ByteBuffer.allocate(bytesToWrite);
        for (XtDataRecord record: dataRecords) {
            long incrementId = record.getIncrementId();
            if (incrementId > origActiveRecordId) {
                record.toByteBuffer(dataToSave);
            }
        }

        // to do: save data from buffer and clear ArrayList
        long filePosition = origActiveRecordId * XtDataRecord.RECORD_LENGTH
                + xtDataHeader.getHeaderSize();
        try {
            xtDataHeader.writeToXtaFile(dataToSave, filePosition);
        } catch (IOException ex) {
            System.getLogger(XtDataPopulate.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    public void addDataRecord(XtDataRecord dataRecord) {
        dataRecords.add(dataRecord);
    }
}
