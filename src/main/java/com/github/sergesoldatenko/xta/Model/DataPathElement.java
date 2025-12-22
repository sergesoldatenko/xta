package com.github.sergesoldatenko.xta.Model;

public class DataPathElement {
    private long elementId;
    private DataPathElement parent;
    private DataPathElement child;
    
    public void setId(long id) {
        elementId = id;
    }
    public long getId() {
        return elementId;
    }
    
    public void setChild(DataPathElement element) {
        child = element;
    }
    public DataPathElement getChild() {
        return child;
    }
    
    public void setParent(DataPathElement element) {
        parent = element;
    }
    public DataPathElement getParent() {
        return parent;
    }
}
