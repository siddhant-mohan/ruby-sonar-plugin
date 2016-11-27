package com.godaddy.sonar.ruby.metricfu;

public class SaikuroMethodComplexity {
    private int lines;
    private String name;
    private int complexity = -1;
    
    public SaikuroMethodComplexity(int lines, String name, int complexity) {
        this.name = name;
        this.lines = lines;
        this.complexity = complexity;
    }
    
    public SaikuroMethodComplexity() {
    }
    
    public int getLines() {
        return lines;
    }
    
    public void setLine(int line) {
        this.lines = line;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getComplexity() {
        return complexity;
    }
    
    public void setComplexity(int complexity) {
        this.complexity = complexity;
    }
    
    @Override
    public String toString() {
        return "lines: " + lines + " name: " + name + " complexity: " + complexity;
    }
}
