package com.godaddy.sonar.ruby.metricfu;

public class CaneLineStyleViolation extends CaneViolation {
    private int line;
    private String description;
    private String key = "UnknownViolation";
    
    public CaneLineStyleViolation(String file, int line, String description) {
        super(file);
        setLine(line);
        setDescription(description);
    }
    
    public CaneLineStyleViolation() {
    }
    
    public String getKey() {
        return key;
    }
    
    public int getLine() {
        return line;
    }
    
    public void setLine(int line) {
        this.line = line;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
        
        if (description.contains("tabs")) {
            key = "LineStyleTabsViolation";
        } else if (description.contains("whitespace")) {
            key = "LineStyleWhitespaceViolation";
        } else if (description.contains("characters")) {
            key = "LineStyleLengthViolation";
        }
    }
    
    @Override
    public String toString() {
        return "file: " + getFile() + " line: " + line + " description: " + description;
    }
}
