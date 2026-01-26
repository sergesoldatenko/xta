package com.github.sergesoldatenko.xta.Model;

import java.nio.ByteBuffer;

public class XtDataRecord {
    private short level;            // 2 bytes
    private int memory;             // 4 bytes
    private int lineNum;            // 4 bytes
    private float execTime;         // 4 bytes
    private long instructionOffset; // 8 bytes
    private long paramsOffset;      // 8 bytes
    private long filepathOffset;    // 8 bytes
    private long parent;            // 8 bytes
    private long incrementId;       // 8 bytes
    
    private static final int POSITION_INCREMENT_ID = 0;
    private static final int POSITION_PARENT = POSITION_INCREMENT_ID + Long.BYTES;
    private static final int POSITION_MEMORY_USED = POSITION_PARENT + Long.BYTES;
    private static final int POSITION_INSTRUCTION_OFFSET = POSITION_MEMORY_USED + Integer.BYTES;
    private static final int POSITION_PARAMS_OFFSET = POSITION_INSTRUCTION_OFFSET + Long.BYTES;
    private static final int POSITION_FILEPATH_OFFSET = POSITION_PARAMS_OFFSET + Long.BYTES;
    private static final int POSITION_LINENUM = POSITION_FILEPATH_OFFSET + Long.BYTES;
    private static final int POSITION_LEVEL = POSITION_LINENUM + Integer.BYTES;
    private static final int POSITION_EXEC_TIME = POSITION_LEVEL + Short.BYTES;
    public static final int RECORD_LENGTH = POSITION_EXEC_TIME + Float.BYTES;
    
    public void setIncrementId(long num) {
        this.incrementId = num;
    }
    public long getIncrementId() {
        return this.incrementId;
    }
    
    public void setExecTime(float execTime) {
        this.execTime = execTime;
    }
    public float getExecTime() {
        return this.execTime;
    }

    public void setMemoryUsed(int memory) {
        this.memory = memory;
    }
    public int getMemoryUsed() {
        return this.memory;
    }

    public void setInstructionOffset(long instructionOffset) {
        this.instructionOffset = instructionOffset;
    }
    public long getInstructionOffset() {
        return this.instructionOffset;
    }

    public void setParamsOffset(long paramsOffset) {
        this.paramsOffset = paramsOffset;
    }
    public long getParamsOffset() {
        return this.paramsOffset;
    }

    public void setFilepathOffset(long filepathOffset) {
        this.filepathOffset = filepathOffset;
    }
    public long getFilepathOffset() {
        return this.filepathOffset;
    }

    public void setLineNum(int lineNum) {
        this.lineNum = lineNum;
    }
    public int getLineNum() {
        return this.lineNum;
    }

    public void setLevel(short level) {
        this.level = level;
    }

    public short getLevel() {
        return this.level;
    }
    
    public void setParent(long id) {
        parent = id;
    }
    public long getParent() {
        return parent;
    }
}
