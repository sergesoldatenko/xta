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
    
    public ByteBuffer getElements() {
        DataPathElement element;
        ByteBuffer buffer;
        buffer = ByteBuffer.allocate(getLength() * Long.BYTES);
        buffer.position(0);
        element = rootElement;
        do {
            buffer.putLong(element.getId());
            element = element.getChild();
        } while (element != null);
        
        return buffer;
    }

    public void setElements(byte[] activePathBytes) {
        Long.
    }
}
