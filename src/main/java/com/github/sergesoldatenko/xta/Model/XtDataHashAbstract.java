package com.github.sergesoldatenko.xta.Model;

import java.util.HashMap;

public class XtDataHashAbstract {
    private HashMap<String, Long> rows  = new HashMap<>();
    private long lastPosition = 0;
    
    public long setRow(String row) {
        Long position = rows.get(row);
        if (position == null) {
            rows.put(row, lastPosition);
            position = lastPosition;
            lastPosition += row.length() + 1;
        }
        
        return position;
    }
}
