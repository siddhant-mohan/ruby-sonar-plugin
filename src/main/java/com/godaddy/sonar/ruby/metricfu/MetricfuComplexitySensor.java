package com.godaddy.sonar.ruby.metricfu;

import java.util.List;

import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.config.Settings;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.PersistenceMode;
import org.sonar.api.measures.RangeDistributionBuilder;
import org.sonar.api.resources.Project;

import com.godaddy.sonar.ruby.RubyPlugin;
import com.godaddy.sonar.ruby.core.Ruby;
import com.google.common.collect.Lists;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

public class MetricfuComplexitySensor implements Sensor
{
    private static final Logger LOG = Loggers.get(MetricfuComplexitySensor.class);

    private static final Number[] FILES_DISTRIB_BOTTOM_LIMITS = { 0, 5, 10, 20, 30, 60, 90 };
    private static final Number[] FUNCTIONS_DISTRIB_BOTTOM_LIMITS = { 1, 2, 4, 6, 8, 10, 12, 20, 30 };
    private static final String COMPLEXITY_SAIKURO = "saikuro";
    private static final String COMPLEXITY_CANE = "cane";

    private MetricfuYamlParser metricfuYamlParser;
    private Settings settings;
    private FileSystem fileSystem;

    public MetricfuComplexitySensor(Settings settings, FileSystem fileSystem, MetricfuYamlParser metricfuYamlParser) {
        this.settings = settings;
        this.fileSystem = fileSystem;
        this.metricfuYamlParser = metricfuYamlParser;
    }

    public boolean shouldExecuteOnProject(Project project) {
        return fileSystem.hasFiles(fileSystem.predicates().hasLanguage(Ruby.KEY));
    }

    public void analyse(Project project, SensorContext context) {
        String complexityType = settings.getString(RubyPlugin.METRICFU_COMPLEXITY_METRIC_PROPERTY);

        if (!complexityType.equalsIgnoreCase(COMPLEXITY_CANE) && !complexityType.equalsIgnoreCase(COMPLEXITY_SAIKURO)) {
            LOG.warn("Unknown/unsupported complexity type '" + complexityType + ", forcing complexity to " + COMPLEXITY_SAIKURO + ".");
            complexityType = COMPLEXITY_SAIKURO;
        }
        LOG.info("MetricfuComplexitySensor: using " + complexityType + " complexity.");
    
        FilePredicate predicate = fileSystem.predicates().hasLanguage(Ruby.KEY);
        List<InputFile> sourceFiles = Lists.newArrayList(fileSystem.inputFiles(predicate));
        for (InputFile inputFile : sourceFiles) {
            LOG.debug("Analyzing functions for classes in the file: " + inputFile.file().getName());
            analyzeFile(inputFile, context, complexityType);
        }
    }

    private void analyzeFile(InputFile inputFile, SensorContext sensorContext, String complexityType) {
        RangeDistributionBuilder fileDistribution = new RangeDistributionBuilder(CoreMetrics.FILE_COMPLEXITY_DISTRIBUTION, FILES_DISTRIB_BOTTOM_LIMITS);
        RangeDistributionBuilder functionDistribution = new RangeDistributionBuilder(CoreMetrics.FUNCTION_COMPLEXITY_DISTRIBUTION, FUNCTIONS_DISTRIB_BOTTOM_LIMITS);
        int fileComplexity = 0;
        int numMethods = 0;
        if (complexityType.equalsIgnoreCase(COMPLEXITY_CANE)) {
            for (CaneViolation v : metricfuYamlParser.parseCane(inputFile.relativePath())) {
                if (v instanceof CaneComplexityViolation) {
                    CaneComplexityViolation c = (CaneComplexityViolation) v;
                    fileComplexity += c.getComplexity();
                    numMethods++;
                    functionDistribution.add(Double.valueOf(c.getComplexity()));
                }
            }
        } else {
            for (SaikuroClassComplexity c : metricfuYamlParser.parseSaikuro(inputFile.relativePath())) {
                for (SaikuroMethodComplexity m : c.getMethods()) {
                    fileComplexity += m.getComplexity();
                    numMethods++;
                    functionDistribution.add(Double.valueOf(m.getComplexity()));
                }
            }
        }

        LOG.error("NUMBER OF METHODS = " + numMethods);
        if (numMethods > 0) {
            LOG.error("SETTING COMPLEXITY METRICS, fileComplexity = " + fileComplexity);
            fileDistribution.add(Double.valueOf(fileComplexity));
            Measure m = sensorContext.saveMeasure(inputFile, fileDistribution.build().setPersistenceMode(PersistenceMode.MEMORY));
            if (m == null) {
                LOG.error("SAVING OF METRIC #1 FAILED!");
            }
            m = sensorContext.saveMeasure(inputFile, CoreMetrics.FUNCTION_COMPLEXITY, Double.valueOf(fileComplexity) / numMethods);
            if (m == null) {
                LOG.error("SAVING OF METRIC #2 FAILED!");
            }
            m = sensorContext.saveMeasure(inputFile, functionDistribution.build().setPersistenceMode(PersistenceMode.MEMORY));
            if (m == null) {
                LOG.error("SAVING OF METRIC #3 FAILED!");
            }
        }
    }
}
