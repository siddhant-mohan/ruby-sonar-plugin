package com.godaddy.sonar.ruby.metricfu;

import org.sonar.api.rule.Severity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RoodiProblem {
    
    private String file;
    private int line = 0;
    private String problem;
    
    public static enum RoodiCheck {
        AbcMetricMethodCheck,
        AssignmentInConditionalCheck,
        CaseMissingElseCheck,
        ClassLineCountCheck,
        ClassNameCheck,
        ClassVariableCheck,
        ControlCouplingCheck,
        CoreMethodOverrideCheck,
        CyclomaticComplexityBlockCheck,
        CyclomaticComplexityMethodCheck,
        EmptyRescueBodyCheck,
        ForLoopCheck,
        MethodLineCountCheck,
        MethodNameCheck,
        ModuleLineCountCheck,
        ModuleNameCheck,
        NpathComplexityMethodCheck,
        ParameterNumberCheck,
    }
    
    ;
    
    
    private static final Map<Pattern, RoodiCheck> messageToKeyMap;
    private static final Map<RoodiCheck, String> keyToSeverityMap;
    
    static {
        HashMap<Pattern, RoodiCheck> mapPatternToCheck = new HashMap<Pattern, RoodiCheck>();
        mapPatternToCheck.put(Pattern.compile("^Found = in conditional\\.", Pattern.CASE_INSENSITIVE), RoodiCheck.AssignmentInConditionalCheck);
        mapPatternToCheck.put(Pattern.compile("^Case statement is missing an else clause\\.", Pattern.CASE_INSENSITIVE), RoodiCheck.CaseMissingElseCheck);
        mapPatternToCheck.put(Pattern.compile("^Class \"[^\"]+\" has \\d+ lines\\.", Pattern.CASE_INSENSITIVE), RoodiCheck.ClassLineCountCheck);
        mapPatternToCheck.put(Pattern.compile("^Class name \"[^\"]+\" should match pattern ", Pattern.CASE_INSENSITIVE), RoodiCheck.ClassNameCheck);
        mapPatternToCheck.put(Pattern.compile("^Don't use class variables ", Pattern.CASE_INSENSITIVE), RoodiCheck.ClassVariableCheck);
        mapPatternToCheck.put(Pattern.compile("^Method \"[^\"]+\" uses the argument \"[^\"]+\" for internal control\\.", Pattern.CASE_INSENSITIVE), RoodiCheck.ControlCouplingCheck);
        mapPatternToCheck.put(Pattern.compile("^Class overrides method '[^']+'\\.", Pattern.CASE_INSENSITIVE), RoodiCheck.CoreMethodOverrideCheck);
        mapPatternToCheck.put(Pattern.compile("^Block cyclomatic complexity is \\d+\\.", Pattern.CASE_INSENSITIVE), RoodiCheck.CyclomaticComplexityBlockCheck);
        mapPatternToCheck.put(Pattern.compile("^Method name \"[^\"]+\" cyclomatic complexity is \\d+\\.", Pattern.CASE_INSENSITIVE), RoodiCheck.CyclomaticComplexityMethodCheck);
        mapPatternToCheck.put(Pattern.compile("^Rescue block should not be empty\\.", Pattern.CASE_INSENSITIVE), RoodiCheck.EmptyRescueBodyCheck);
        mapPatternToCheck.put(Pattern.compile("^Don't use 'for' loops\\.", Pattern.CASE_INSENSITIVE), RoodiCheck.ForLoopCheck);
        mapPatternToCheck.put(Pattern.compile("^Method \"[^\"]+\" has \\d+ lines\\.", Pattern.CASE_INSENSITIVE), RoodiCheck.MethodLineCountCheck);
        mapPatternToCheck.put(Pattern.compile("^Method name \"[^\"]+\" should match pattern ", Pattern.CASE_INSENSITIVE), RoodiCheck.MethodNameCheck);
        mapPatternToCheck.put(Pattern.compile("^Module \"[^\"]+\" has \\d+ lines\\.", Pattern.CASE_INSENSITIVE), RoodiCheck.ModuleLineCountCheck);
        mapPatternToCheck.put(Pattern.compile("^Module name \"[^\"]+\" should match pattern ", Pattern.CASE_INSENSITIVE), RoodiCheck.ModuleNameCheck);
        mapPatternToCheck.put(Pattern.compile("^Method name \"[^\"]+\" n-path complexity is ", Pattern.CASE_INSENSITIVE), RoodiCheck.NpathComplexityMethodCheck);
        mapPatternToCheck.put(Pattern.compile("^Method name \"[^\"]+\" has \\d+ parameters\\.", Pattern.CASE_INSENSITIVE), RoodiCheck.ParameterNumberCheck);
        mapPatternToCheck.put(Pattern.compile("^Method name \"[^\"]+\" has an ABC metric score of ", Pattern.CASE_INSENSITIVE), RoodiCheck.AbcMetricMethodCheck);
        messageToKeyMap = Collections.unmodifiableMap(mapPatternToCheck);
        
        HashMap<RoodiCheck, String> mapKeyToSeverity = new HashMap<RoodiCheck, String>();
        mapKeyToSeverity.put(RoodiCheck.AbcMetricMethodCheck, Severity.MAJOR);
        mapKeyToSeverity.put(RoodiCheck.AssignmentInConditionalCheck, Severity.CRITICAL);
        mapKeyToSeverity.put(RoodiCheck.CaseMissingElseCheck, Severity.MINOR);
        mapKeyToSeverity.put(RoodiCheck.ClassLineCountCheck, Severity.MINOR);
        mapKeyToSeverity.put(RoodiCheck.ClassNameCheck, Severity.MINOR);
        mapKeyToSeverity.put(RoodiCheck.ClassVariableCheck, Severity.MINOR);
        mapKeyToSeverity.put(RoodiCheck.ControlCouplingCheck, Severity.MAJOR);
        mapKeyToSeverity.put(RoodiCheck.CoreMethodOverrideCheck, Severity.MAJOR);
        mapKeyToSeverity.put(RoodiCheck.CyclomaticComplexityBlockCheck, Severity.MAJOR);
        mapKeyToSeverity.put(RoodiCheck.CyclomaticComplexityMethodCheck, Severity.MAJOR);
        mapKeyToSeverity.put(RoodiCheck.EmptyRescueBodyCheck, Severity.MAJOR);
        mapKeyToSeverity.put(RoodiCheck.ForLoopCheck, Severity.MINOR);
        mapKeyToSeverity.put(RoodiCheck.MethodLineCountCheck, Severity.MINOR);
        mapKeyToSeverity.put(RoodiCheck.MethodNameCheck, Severity.MINOR);
        mapKeyToSeverity.put(RoodiCheck.ModuleLineCountCheck, Severity.MINOR);
        mapKeyToSeverity.put(RoodiCheck.ModuleNameCheck, Severity.MINOR);
        mapKeyToSeverity.put(RoodiCheck.NpathComplexityMethodCheck, Severity.MAJOR);
        mapKeyToSeverity.put(RoodiCheck.ParameterNumberCheck, Severity.MINOR);
        keyToSeverityMap = Collections.unmodifiableMap(mapKeyToSeverity);
    }
    
    
    public RoodiProblem(String file, int line, String problem) {
        this.file = file;
        this.line = line;
        this.problem = problem;
    }
    
    public RoodiProblem() {
    }
    
    public String getFile() {
        return file;
    }
    
    public void setFile(String file) {
        this.file = file;
    }
    
    public int getLine() {
        return line;
    }
    
    public void setLine(int line) {
        this.line = line;
    }
    
    public String getProblem() {
        return problem;
    }
    
    public void setProblem(String problem) {
        this.problem = problem;
    }
    
    @Override
    public String toString() {
        return "file: " + file + " lines: " + line + " problem: " + problem;
    }
    
    public static RoodiCheck messageToKey(String message) {
        for (Pattern p : messageToKeyMap.keySet()) {
            if (p.matcher(message).find()) {
                return messageToKeyMap.get(p);
            }
        }
        return null;
    }
    
    public static String toSeverity(RoodiCheck check) {
        if (keyToSeverityMap.containsKey(check)) {
            return keyToSeverityMap.get(check);
        }
        return Severity.BLOCKER; // Make sure we catch this case.
    }
    
    public static String toSeverity(String check) {
        try {
            return toSeverity(RoodiCheck.valueOf(check));
        } catch (Exception e) {
            return Severity.BLOCKER; // Make sure we catch this case.
        }
    }
}
