package com.github.sergesoldatenko.xta.Model;

import java.io.IOException;
import java.nio.ByteBuffer;

public class XtDataRecordRepository {
    private final XtDataHeader dataHeader;

    XtDataRecordRepository(XtDataHeader dataHeader) {
        this.dataHeader = dataHeader;
    }

    public XtDataRecord loadById(long id) throws Exception {
        XtDataRecord record;
        //= new XtDataRecord();
        long filePosition = (id - 1) * XtDataRecord.RECORD_LENGTH
            + dataHeader.getHeaderSize();
        ByteBuffer buffer = ByteBuffer.allocate(XtDataRecord.RECORD_LENGTH);
        try {
            int bytesFetched = dataHeader.readFromXtaFile(buffer, filePosition);
            if (bytesFetched != XtDataRecord.RECORD_LENGTH) {
                throw new IOException("Failed to load a xta record");
            }
            record = new XtDataRecord();
            importFromByteBuffer(record, buffer);
        } catch (IOException ex) {
            System.getLogger(XtDataRecordRepository.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            // to do: replace Exception type with custom one
            throw new Exception("record not found");
        }

        return record;
    }

    public void importFromByteBuffer(XtDataRecord record, ByteBuffer buffer) {
        record.setIncrementId(buffer.getLong());
        record.setParent(buffer.getLong());
        record.setMemoryUsed(buffer.getInt());
        record.setInstructionOffset(buffer.getLong());
        record.setParamsOffset(buffer.getLong());
        record.setFilepathOffset(buffer.getLong());
        record.setLineNum(buffer.getInt());
        record.setLevel(buffer.getShort());
        record.setExecTime(buffer.getFloat());
    }

    public void exportToByteBuffer(XtDataRecord record, ByteBuffer buffer) {
        buffer.putLong(record.getIncrementId());
        buffer.putLong(record.getParent());
        buffer.putInt(record.getMemoryUsed());
        buffer.putLong(record.getInstructionOffset());
        buffer.putLong(record.getParamsOffset());
        buffer.putLong(record.getFilepathOffset());
        buffer.putInt(record.getLineNum());
        buffer.putShort(record.getLevel());
        buffer.putFloat(record.getExecTime());
    }
}
