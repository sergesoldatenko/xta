package com.github.sergesoldatenko.xta.Model;

import static com.github.sergesoldatenko.xta.Model.XtData.STATUS_EMPTY;
import static com.github.sergesoldatenko.xta.Model.XtData.STATUS_LOADED;
import static com.github.sergesoldatenko.xta.Model.XtData.STATUS_LOADING;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * A class caller is responsible to invoke close() method before this object is dereferenced
 */
public class XtFileParser {
    private final XtData xtData;
    private final RandomAccessFile xtFile;
    private final int CHUNK_LENGTH = 1000;
    
    XtFileParser(String xtFilename, XtData xtData) throws FileNotFoundException {
        this.xtFile = new RandomAccessFile(xtFilename, "r");
        this.xtData = xtData;
    }
    
    public void startLoading() throws IOException {
        if (xtData.getStatus() != STATUS_EMPTY) {
            return;
        }
        String line;
        line = xtFile.readLine();
        // to do:
        // add different xt formats parsing in future
        if ("TRACE START".equals(line.substring(0, 11))) {
            xtData.setXtRecordsNum(0);
            xtData.setXtFileOffset(
                xtFile.getFilePointer()
            );
            xtData.setStatus(STATUS_LOADING);
        } else {
            // to do:
            // throw exception to show modal window with notification that format isnot recognized
        }
    }

    public void resumeLoading() throws IOException {
        String line;
        xtFile.seek(xtData.getXtFileOffset());
        int chunkNum = 0;
        //long xtRecordsNum = xtData.getXtRecordsNum();
        while (chunkNum < CHUNK_LENGTH) {
            line = xtFile.readLine();
            if (line == null || line.substring(0, 9).equals("TRACE END")) {
                xtData.setStatus(STATUS_LOADED);
                break;
            } else {
                // to do: set increment id for each record
                XtDataRecord record = parseLine(line);
                DataPath activePath = xtData.getActivePath();
                DataPathElement element = new DataPathElement(record.getIncrementId());
                if (record.getLevel() > 1) {
                    DataPathElement parentElement = activePath.getElementByLevel(record.getLevel() - 1);
                    parentElement.setChild(element);
                    record.setParent(parentElement.getId());
                } else { // top level element
                    activePath.setRootElement(element);
                }
                //record.setAsActive();

                xtData.addDataRecord(record);
                chunkNum++;
            }
        }
            
        xtData.setXtFileOffset(
            xtFile.getFilePointer()
        );
            
        xtData.setXtRecordsNum(
            xtData.getXtRecordsNum() + chunkNum
        );
    }

    private XtDataRecord parseLine(String line) {
        // fields:
        // 0 - exec_time
        // 1 - memory
        // 2 - instruction
        // 3 - params
        // 4 - file
        // 5 - line
        char c;
        XtDataRecord xtDataRecord = new XtDataRecord();
        short fieldStartPos = 0;
        short nestedGapCount = 0;
        byte switchCase = 0;
        for (short i = 0; i < line.length(); i++) {
            c = line.charAt(i);
            switch (switchCase) {
                case 0: // ltrim leading spaces
                    if (c == 0x20) {
                        break;
                    } else {
                        // first field found
                        fieldStartPos = i;
                        switchCase++;
                    }
                case 1: // processing "exec_time" field
                    if (c == 0x20) {
                        float execTime;
                        try {        
                            execTime = Float.parseFloat(line.substring(fieldStartPos, i));
                        } catch (NumberFormatException ex) {
                            execTime = 0;
                        }
                        xtDataRecord.setExecTime(execTime);
                        switchCase++;
                    }
                    break;
                case 2: // find "memory" field
                    if (c == 0x20) {
                        break;
                    } else {
                        fieldStartPos = i;
                        switchCase++;
                    }
                case 3: // processing "memory" field
                    if (c == 0x20) {
                        int memory;
                        try {
                            memory = Integer.parseInt(line.substring(fieldStartPos, i));
                        } catch (NumberFormatException ex) {
                            memory = 0;
                        }
                        // to do: add check to make memory field optional
                        // exclude for now
                        // xtDataRecord.setMemoryUsed(memory);
                        switchCase++;
                    }
                    break;
                case 4: // find "instruction" field
                    if (c == 0x20) {
                        nestedGapCount++;
                    } else {
                        if (
                                c == '-'
                                && line.charAt(i+1) == '>'
                                && line.charAt(i+2) == 0x20
                        ) {
                            // found instruction field
                            i +=3;
                            xtDataRecord.setLevel((short) ((nestedGapCount - 3) >>> 1));
                            switchCase++;
                            fieldStartPos = i;
                        }
                    }
                    break;
                case 5: // processing "instruction" field
                    if (c == '(') {
                        // Save instruction
                        String instruction = line.substring(fieldStartPos, i);
                        xtDataRecord.setInstructionOffset(
                                xtData.setHashInstruction(instruction)
                                //XtDataHashInstruction.setRow(instruction)
                        );
                        switchCase++;
                    }
                    break;
                case 6: // process parameter
                    if (c == ')') {
                        // no param
                        //parsedLine[fieldIndex] = null;
                    } else {
                        fieldStartPos = i;
                        try {
                            i = findParamEndIndex(line, fieldStartPos);
                            String params = line.substring(fieldStartPos, i);
                            xtDataRecord.setParamsOffset(
                                    xtData.setHashParams(params)
                                    //XtDataHashParams.setRow(params)
                            );
                        } catch (Exception ex) {
                            //parsedLine[fieldIndex] = null;
                        }
                    }
                    switchCase++;
                    break;
                case 7: // find "file" field
                    if (c == 0x20) {
                        break;
                    } else {
                        fieldStartPos = i;
                        switchCase++;
                    }
                case 8: // find end of "file" field
                    if (c == ':') {
                        String filepath = line.substring(fieldStartPos, i);
                        xtDataRecord.setFilepathOffset(
                                xtData.setHashFilename(filepath)
                                //XtDataHashFilename.setRow(filepath)
                        );
                        switchCase++;
                    }
                    break;
                case 9:
                    //String lineNum = line.substring(i);
                    int lineNum;
                    try {
                        lineNum = Integer.parseInt(line.substring(i));
                    } catch (NumberFormatException ex) {
                        lineNum = -1;
                    }
                    xtDataRecord.setLineNum(lineNum);
            }
        }
        
        return xtDataRecord;
    }

    private short findParamEndIndex(String line, short fieldStartPos) throws Exception {
        char c;
        short pos = (short)line.length();
        while (fieldStartPos < pos) {
            pos--;
            c = line.charAt(pos);
            if (c == ')') {
                return pos;
            }
        }

        throw new Exception("Param not found.");
    }
    
    public void close() {
        try {
            xtFile.close();
        } catch (IOException ex) {
            System.getLogger(XtFileParser.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
}
