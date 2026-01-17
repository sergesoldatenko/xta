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
    private long incrementId;
    
    private final int POSITION_PARENT = 0;
    private final int POSITION_MEMORY_USED = 8;
    private final int POSITION_INSTRUCTION_OFFSET = 12;
    private final int POSITION_PARAMS_OFFSET = 20;
    private final int POSITION_FILEPATH_OFFSET = 28;
    private final int POSITION_LINENUM = 36;
    private final int POSITION_LEVEL = 40;
    private final int POSITION_EXEC_TIME = 42;
    public static final int RECORD_LENGTH = 46;
    
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

    public void toByteBuffer(ByteBuffer buffer) {
        buffer.putLong(parent);
        buffer.putInt(memory);
        buffer.putLong(instructionOffset);
        buffer.putLong(paramsOffset);
        buffer.putLong(filepathOffset);
        buffer.putInt(lineNum);
        buffer.putShort(level);
        buffer.putFloat(execTime);
    }
}
