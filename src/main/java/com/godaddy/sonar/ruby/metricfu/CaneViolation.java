package com.godaddy.sonar.ruby.metricfu;

public abstract class CaneViolation {
    private String file;
    
    public CaneViolation(String file) {
        this.file = file;
    }
    
    public CaneViolation() {
    }
    
    public abstract String getKey();
    
    public String getFile() {
        return file;
    }
    
    public void setFile(String file) {
        this.file = file;
    }
    
    @Override
    public String toString() {
        return "file: " + file;
    }
}
