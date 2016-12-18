package com.godaddy.sonar.ruby.metricfu;

public class CaneComplexityViolation extends CaneViolation {
    private String method;
    private int complexity;

    public CaneComplexityViolation(String file, String method, int complexity) {
        super(file);
        this.method = method;
        this.complexity = complexity;
    }

    public CaneComplexityViolation() {
    }

    public String getKey() {
        return "ComplexityViolation";
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getComplexity() {
        return complexity;
    }

    public void setComplexity(int complexity) {
        this.complexity = complexity;
    }

    @Override
    public String toString() {
        return "file: " + getFile() + " line: " + complexity + " method: " + method;
    }
}
