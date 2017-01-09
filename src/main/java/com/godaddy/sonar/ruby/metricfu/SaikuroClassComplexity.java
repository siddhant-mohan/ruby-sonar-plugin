package com.godaddy.sonar.ruby.metricfu;

import java.util.ArrayList;
import java.util.List;

public class SaikuroClassComplexity {
    private String file;
    private String name;
    private int lines;
    private int complexity = -1;
    List<SaikuroMethodComplexity> methods = new ArrayList<SaikuroMethodComplexity>();

    public SaikuroClassComplexity(String file, int lines, String method, int complexity) {
        this.file = file;
        this.lines = lines;
        this.complexity = complexity;
    }

    public SaikuroClassComplexity() {
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLines() {
        return lines;
    }

    public void setLines(int lines) {
        this.lines = lines;
    }

    public int getComplexity() {
        return complexity;
    }

    public void setComplexity(int complexity) {
        this.complexity = complexity;
    }

    public List<SaikuroMethodComplexity> getMethods() {
        return methods;
    }

    public void setMethods(List<SaikuroMethodComplexity> methods) {
        this.methods = methods;
    }

    @Override
    public String toString() {
        return "file: " + file + " line: " + lines + " complexity: " + complexity + " methods: " + methods;
    }
}
