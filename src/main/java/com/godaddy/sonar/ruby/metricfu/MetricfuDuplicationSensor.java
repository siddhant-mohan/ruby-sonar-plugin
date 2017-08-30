package com.godaddy.sonar.ruby.metricfu;

import java.io.File;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import com.godaddy.sonar.ruby.core.Ruby;
import com.google.common.collect.Lists;
import org.sonar.api.batch.Sensor;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.cpd.NewCpdTokens;
import org.sonar.api.resources.Project;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import java.util.List;
import java.util.ArrayList;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Measure;
import org.sonar.api.scan.filesystem.FileQuery;
import org.sonar.api.scan.filesystem.ModuleFileSystem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import com.godaddy.sonar.ruby.core.Ruby;
import com.godaddy.sonar.ruby.core.RubyFile;

public class MetricfuDuplicationSensor implements Sensor {
    private static final Logger LOG = Loggers.get(MetricfuDuplicationSensor.class);
    private FileSystem fileSystem;
    private MetricfuYamlParser metricfuYamlParser;
    private ModuleFileSystem moduleFileSystem;

    public MetricfuDuplicationSensor(FileSystem fileSystem, MetricfuYamlParser metricfuYamlParser) {
        this.fileSystem = fileSystem;
        this.metricfuYamlParser = metricfuYamlParser;
    }

    public boolean shouldExecuteOnProject(Project project) {
        return fileSystem.hasFiles(fileSystem.predicates().hasLanguage(Ruby.KEY));
    }

    public void analyse(Project project, SensorContext context) {
        // saveDuplication(context);
		// List<File> sourceDirs = moduleFileSystem.sourceDirs();
        // List<File> rubyFilesInProject = moduleFileSystem.files(FileQuery.onSource().onLanguage(project.getLanguageKey()));
        List<File> rubyFilesInProject = Lists.newArrayList(fileSystem.files(fileSystem.predicates().hasLanguage(Ruby.KEY)));
        ArrayList<InputFile> inputFiles = Lists.newArrayList(fileSystem.inputFiles(fileSystem.predicates().hasLanguage(Ruby.KEY)));

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document doc = builder.newDocument();
            Element root = doc.createElement("duplications");
            doc.appendChild(root);

            HashMap<String, Double> duplicated_blocks = new HashMap<String, Double>();
            HashMap<String, Double> duplicated_lines = new HashMap<String, Double>();
            HashMap<String, Document> duplicated_xml = new HashMap<String, Document>();

            List<FlayReason> duplications = metricfuYamlParser.parseFlay();        
            for (FlayReason duplication : duplications) {
                Element group = doc.createElement("g");
                for (FlayReason.Match match : duplication.getMatches()) {
                    File file = new File(fileSystem.baseDir(), match.getFile());
                    RubyFile resource = new RubyFile(file, inputFiles); 
                    String key = project.getKey() + ":" + resource.getKey();
                    if (duplicated_blocks.containsKey(key)) {
                        duplicated_blocks.put(key, duplicated_blocks.get(key)+1);
                    } else {
                        duplicated_blocks.put(key, 1.0);
                    }

                    if (duplicated_lines.containsKey(key)) {
                        duplicated_lines.put(key, duplicated_lines.get(key)+match.getLines());
                    } else {
                        duplicated_lines.put(key, match.getLines() * 1.0);
                    } 
                    
                    Element block = doc.createElement("b");
                    block.setAttribute("r", key);
                    block.setAttribute("s", match.getStartLine().toString());
                    block.setAttribute("l", match.getLines().toString());  
                    group.appendChild(block);                   
                }
                
                // Now that we have the group XML, add it to each file.
                for (FlayReason.Match match : duplication.getMatches()) {
                    File file = new File(fileSystem.baseDir(), match.getFile());
                    RubyFile resource = new RubyFile(file, inputFiles);             
                    String key = project.getKey() + ":" + resource.getKey();
                    if (!duplicated_xml.containsKey(key)) {
                        Document d = builder.newDocument();
                        Element r = d.createElement("duplications");
                        d.appendChild(r);
                        duplicated_xml.put(key, d);
                    } 
                    Document d = duplicated_xml.get(key);
                    d.getFirstChild().appendChild(d.importNode(group, true));
                }
            }
            
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            
            for (File file : rubyFilesInProject) {
                RubyFile resource = new RubyFile(file, inputFiles);             
                String key = project.getKey() + ":" + resource.getKey();
                if (duplicated_blocks.containsKey(key)) {
                    context.saveMeasure(resource, CoreMetrics.DUPLICATED_FILES, 1.0);
                    context.saveMeasure(resource, CoreMetrics.DUPLICATED_BLOCKS, duplicated_blocks.get(key));
                    context.saveMeasure(resource, CoreMetrics.DUPLICATED_LINES, duplicated_lines.get(key));
                } else {
                    context.saveMeasure(resource, CoreMetrics.DUPLICATED_FILES, 0.0);                   
                }
                
                if (duplicated_xml.containsKey(key)) {
                    StringWriter writer = new StringWriter();
                    transformer.transform(new DOMSource(duplicated_xml.get(key)), new StreamResult(writer));
                    context.saveMeasure(resource, new Measure(CoreMetrics.DUPLICATIONS_DATA, writer.getBuffer().toString()));   
                }
            }
            
        } catch (Exception e) {
            // Log.error("Exception raised while processing duplications.", e);
        }
    }

    private void saveDuplication(SensorContext sensorContext) {

        LOG.info("saveDuplication");

        ArrayList<InputFile> inputFiles = Lists.newArrayList(fileSystem.inputFiles(fileSystem.predicates().hasLanguage(Ruby.KEY)));

        for (InputFile inputFile : inputFiles) {
            if (inputFile.lines() > 2) {

                // FIXME: add duplication support once out of beta
                NewCpdTokens nd = sensorContext.newCpdTokens();

                nd.onFile(inputFile);
                // nd.addToken(inputFile.newRange(0, 0, inputFile.lines(), 0));
                // nd.originBlock(inputFile, 1, inputFile.lines() / 2 - 1);
                // LOG.info("originBlock {} 1:{}", inputFile.relativePath(), inputFile.lines() / 2 - 1);
                // nd.isDuplicatedBy(inputFile, inputFile.lines() / 2, inputFile.lines() - 1);
                // String s = Integer.toString(inputFile.lines() / 2);
                // String e = Integer.toString(inputFile.lines() - 1);
                // LOG.info("isDuplicatedBy " + inputFile.relativePath() + " : " + s + " : " + e);
                //
                nd.save();
            }
        }
    }
}
