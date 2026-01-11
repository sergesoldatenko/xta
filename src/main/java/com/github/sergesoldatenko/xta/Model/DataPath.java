package com.github.sergesoldatenko.xta.Model;

import java.nio.ByteBuffer;

public class DataPath {
    private DataPathElement rootElement;
    
    public void setRootElement(DataPathElement element) {
        rootElement = element;
    }

    public DataPathElement getElementByLevel(int level) {
        DataPathElement element;
        int i;
        i = 1;
        element = rootElement;
        while (i < level) {
            if (element == null) {
                break;
            }
            element = element.getChild();
            i++;
        }
        
        return element;
    }
    
    public int getLength() {
        DataPathElement element;
        element = rootElement;
        int count;
        count = 0;
        while (element != null) {
            element = element.getChild();
            count++;
        }
        
        return count;
    }
    
    public byte[] getElements() {
        int elementPosition = 0;
        int length = getLength();
        byte[] activePathBytes = new byte[Long.BYTES * length];
        long elementId;

        DataPathElement element;
        element = rootElement;
        do {
            elementId = element.getId();
            for (int i = Long.BYTES - 1; i >= 0; i--) {
                activePathBytes[elementPosition+i] = (byte)(elementId & 0xFF);
                elementId >>= Byte.SIZE; // Shift right by 8 bits (Byte.SIZE)
            }
            elementPosition += Long.BYTES;
            element = element.getChild();
        } while (element != null);

        return activePathBytes;
    }

    public void setElements(byte[] activePathBytes) {
        int elementsCount = activePathBytes.length / Long.BYTES;
        long value;
        DataPathElement element;
        DataPathElement parentElement = new DataPathElement();

        for (int level = 1; level < elementsCount+1; level++) {
            value = 0;
            for (int i = 0; i < Long.BYTES; i++) {
                value <<= Byte.SIZE;
                value |= (activePathBytes[level+i] & 0xFF);
            }
            element = new DataPathElement(value);
            if (level > 1) {
                element.setParent(parentElement);
            } else {
                rootElement = element;
            }

            parentElement = element;
        }
    }
}
