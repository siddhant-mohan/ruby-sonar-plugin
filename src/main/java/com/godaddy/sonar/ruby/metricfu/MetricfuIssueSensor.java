package com.godaddy.sonar.ruby.metricfu;

import java.io.IOException;
import java.util.List;

import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.component.ResourcePerspectives;
import org.sonar.api.issue.Issuable;
import org.sonar.api.issue.Issuable.IssueBuilder;
import org.sonar.api.issue.Issue;
import org.sonar.api.resources.Project;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.rule.Severity;

import com.godaddy.sonar.ruby.RubyPlugin;
import com.godaddy.sonar.ruby.core.Ruby;
import com.godaddy.sonar.ruby.metricfu.RoodiProblem.RoodiCheck;
import com.google.common.collect.Lists;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

public class MetricfuIssueSensor implements Sensor
{
    private static final Logger LOG = Loggers.get(MetricfuIssueSensor.class);

    private static final Integer NO_LINE_NUMBER = -1;

    private MetricfuYamlParser metricfuYamlParser;
    private FileSystem fileSystem;
    private final ResourcePerspectives perspectives;

    public MetricfuIssueSensor(FileSystem fileSystem, MetricfuYamlParser metricfuYamlParser, ResourcePerspectives perspectives) {
        this.fileSystem = fileSystem;
        this.metricfuYamlParser = metricfuYamlParser;
        this.perspectives = perspectives;
    }

    public boolean shouldExecuteOnProject(Project project) {
        return fileSystem.hasFiles(fileSystem.predicates().hasLanguage("ruby"));
    }

    public void analyse(Project project, SensorContext context) {
        for (InputFile file : Lists.newArrayList(fileSystem.inputFiles(fileSystem.predicates().hasLanguage(Ruby.KEY)))) {
            LOG.debug("analyzing issues in the file: " + file.absolutePath());
            try {
                analyzeFile(file, context);
            } catch (IOException e) {
                LOG.error("Can not analyze the file " + file.absolutePath() + " for issues");
            }
        }
    }

    private void analyzeFile(InputFile file, SensorContext sensorContext) throws IOException {
        List<ReekSmell> smells = metricfuYamlParser.parseReek(file.relativePath());
        for (ReekSmell smell : smells) {
            addIssue(file, RubyPlugin.KEY_REPOSITORY_REEK, smell.getType(), ReekSmell.toSeverity(smell.getType()), smell.getMessage());
        }

        List<RoodiProblem> problems = metricfuYamlParser.parseRoodi(file.relativePath());
        for (RoodiProblem problem : problems) {
            RoodiCheck check = RoodiProblem.messageToKey(problem.getProblem());
            addIssue(file, problem.getLine(), RubyPlugin.KEY_REPOSITORY_ROODI, check.toString(), RoodiProblem.toSeverity(check), problem.getProblem());
        }

        List<CaneViolation> violations = metricfuYamlParser.parseCane(file.relativePath());
        for (CaneViolation violation : violations) {
            if (violation instanceof CaneCommentViolation) {
                CaneCommentViolation c = (CaneCommentViolation)violation;
                addIssue(file, c.getLine(), RubyPlugin.KEY_REPOSITORY_CANE, c.getKey(), Severity.MINOR,
                    "Class ' " + c.getClassName() + "' requires explanatory comments on preceding line.");
            } else if (violation instanceof CaneComplexityViolation) {
                CaneComplexityViolation c = (CaneComplexityViolation)violation;
                addIssue(file, NO_LINE_NUMBER, RubyPlugin.KEY_REPOSITORY_CANE, c.getKey(), Severity.MAJOR,
                    "Method '" + c.getMethod() + "' has ABC complexity of " + c.getComplexity() + ".");
            } else if (violation instanceof CaneLineStyleViolation) {
                CaneLineStyleViolation c = (CaneLineStyleViolation)violation;
                addIssue(file, c.getLine(), RubyPlugin.KEY_REPOSITORY_CANE, c.getKey(), Severity.MINOR, c.getDescription() + ".");
            }
        }
    }

    public void addIssue(InputFile file, Integer line, String repo, String key, String severity, String message) {
		try {

    		Issuable issuable = perspectives.as(Issuable.class, file);
    		if (issuable != null) {
        		IssueBuilder bld = issuable.newIssueBuilder();
        		bld.ruleKey(RuleKey.of(repo, key));
    		    bld.message(message);
    		    bld.severity(severity);
        		if (line != NO_LINE_NUMBER) {
        			bld = bld.line(line);
        		}
        		Issue issue = bld.build();
    			if (!issuable.addIssue(issue)) {
    				LOG.error("Failed to register issue.\nIssue Object : " + issue.toString());
    			}
    		} else {
    		    LOG.warn("Unable to create issuable.");
    		}
		} catch(Exception e) {
			LOG.error("Error in create issue object: " + e.getMessage(), e);
		}
    }

    public void addIssue(InputFile file, String repo, String key, String severity, String message) {
    	addIssue(file, NO_LINE_NUMBER, repo, key, severity, message);
    }
}
