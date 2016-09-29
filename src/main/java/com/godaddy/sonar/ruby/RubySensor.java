package com.godaddy.sonar.ruby;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.config.Settings;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.resources.Project;
import org.sonar.squidbridge.measures.Metric;
import org.sonar.squidbridge.text.Source;

import com.godaddy.sonar.ruby.core.Ruby;
import com.godaddy.sonar.ruby.core.RubyRecognizer;
import com.godaddy.sonar.ruby.parsers.CommentCountParser;
import com.google.common.collect.Lists;

public class RubySensor implements Sensor {
    private FileSystem fileSystem;

    public RubySensor(Settings settings, FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    @Override
    public boolean shouldExecuteOnProject(Project project) {
        // This sensor is executed only when there are Ruby files
        return fileSystem.hasFiles(fileSystem.predicates().hasLanguage(Ruby.KEY));
    }

    public void analyse(Project project, SensorContext context) {
        computeBaseMetrics(context, project);
    }

    protected void computeBaseMetrics(SensorContext sensorContext, Project project) {
        Reader reader = null;
        List<InputFile> inputFiles = Lists.newArrayList(fileSystem.inputFiles(fileSystem.predicates().hasLanguage(Ruby.KEY)));

        for (InputFile inputFile : inputFiles) {
            try {
                reader = new StringReader(FileUtils.readFileToString(inputFile.file(), fileSystem.encoding().name()));
                Source source = new Source(reader, new RubyRecognizer());
                sensorContext.saveMeasure(inputFile, CoreMetrics.NCLOC, (double) source.getMeasure(Metric.LINES_OF_CODE));

                int numCommentLines = CommentCountParser.countLinesOfComment(inputFile.file());
                sensorContext.saveMeasure(inputFile, CoreMetrics.COMMENT_LINES, (double) numCommentLines);
                sensorContext.saveMeasure(inputFile, CoreMetrics.FILES, 1.0);
                sensorContext.saveMeasure(inputFile, CoreMetrics.CLASSES, 1.0);
            } catch (Exception e) {
                throw new IllegalStateException("Error computing base metrics for project.", e);
            } finally {
                IOUtils.closeQuietly(reader);
            }
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
