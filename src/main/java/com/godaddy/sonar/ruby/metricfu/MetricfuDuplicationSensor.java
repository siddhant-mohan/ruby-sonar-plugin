package com.godaddy.sonar.ruby.metricfu;

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

import java.util.ArrayList;

public class MetricfuDuplicationSensor implements Sensor {
    private static final Logger LOG = Loggers.get(MetricfuDuplicationSensor.class);
    private FileSystem fileSystem;
    private MetricfuYamlParser metricfuYamlParser;

    public MetricfuDuplicationSensor(FileSystem fileSystem, MetricfuYamlParser metricfuYamlParser) {
        this.fileSystem = fileSystem;
        this.metricfuYamlParser = metricfuYamlParser;
    }

    public boolean shouldExecuteOnProject(Project project) {
        return fileSystem.hasFiles(fileSystem.predicates().hasLanguage(Ruby.KEY));
    }

    public void analyse(Project project, SensorContext context) {
        saveDuplication(context);
//		try {
//		    List<FlayReason> duplications = metricfuYamlParser.parseFlay();
//		    for (FlayReason duplication : duplications) {
//                NewDuplication nd = context.newDuplication();
//		        for (int i = 0; i < duplication.getMatches().size(); i++) {
//		            FlayReason.Match match = duplication.getMatches().get(i);
//                    InputFile inputFile = fileSystem.inputFile(fileSystem.predicates().hasRelativePath(match.getFile()));
//
//		            if (inputFile != null) {
//	                    if (i == 0) {
//	                        LOG.debug("Adding duplication origin: " + inputFile.relativePath() + ", line " + match.getStartLine());
//	                        nd.originBlock(inputFile, match.getStartLine(), match.getStartLine()+match.getLines()-1);
//	                    } else {
//                            LOG.debug("  adding duplicated by: " + inputFile.relativePath() + ", line " + match.getStartLine());
//	                        nd.isDuplicatedBy(inputFile, match.getStartLine(), match.getStartLine()+match.getLines()-1);
//	                    }
//		            } else {
//		                LOG.warn("Unable to find input file for " + match.getFile());
//		            }
//		        }
//		        nd.save();
//		    }

//			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//			DocumentBuilder builder = factory.newDocumentBuilder();
//
//			Document doc = builder.newDocument();
//			Element root = doc.createElement("duplications");
//			doc.appendChild(root);
//
//			HashMap<String, Double> duplicated_blocks = new HashMap<String, Double>();
//			HashMap<String, Double> duplicated_lines = new HashMap<String, Double>();
//			HashMap<String, Document> duplicated_xml = new HashMap<String, Document>();
//
//			List<FlayReason> duplications = metricfuYamlParser.parseFlay();
//			for (FlayReason duplication : duplications) {
//				Element group = doc.createElement("g");
//				for (FlayReason.Match match : duplication.getMatches()) {
//				    InputFile inputFile = fileSystem.inputFile(fileSystem.predicates().hasRelativePath(match.getFile()));
//				    if (inputFile != null) {
//    					String key = project.getKey() + ":" + inputFile.relativePath();
//    					if (duplicated_blocks.containsKey(key)) {
//    						duplicated_blocks.put(key, duplicated_blocks.get(key)+1);
//    					} else {
//    						duplicated_blocks.put(key, 1.0);
//    					}
//
//    					if (duplicated_lines.containsKey(key)) {
//    						duplicated_lines.put(key, duplicated_lines.get(key)+match.getLines());
//    					} else {
//    						duplicated_lines.put(key, match.getLines() * 1.0);
//    					}
//
//    					Element block = doc.createElement("b");
//    					block.setAttribute("r", key);
//    					block.setAttribute("s", match.getStartLine().toString());
//    					block.setAttribute("l", match.getLines().toString());
//    					group.appendChild(block);
//				    } else {
//				        LOG.warn("Unable to find input file for " + match.getFile());
//				    }
//				}
//
//				// Now that we have the group XML, add it to each file.
//				HashSet<String> already_added = new HashSet<String>();
//				for (FlayReason.Match match : duplication.getMatches()) {
//                    InputFile inputFile = fileSystem.inputFile(fileSystem.predicates().hasRelativePath(match.getFile()));
//                    if (inputFile != null) {
//    					String key = project.getKey() + ":" + inputFile.relativePath();
//    					if (!duplicated_xml.containsKey(key)) {
//    						Document d = builder.newDocument();
//    						Element r = d.createElement("duplications");
//    						d.appendChild(r);
//    						duplicated_xml.put(key, d);
//    					}
//
//    					// If we have duplications in the same file, only add them once.
//    					if (!already_added.contains(key)) {
//    						Document d = duplicated_xml.get(key);
//    						d.getFirstChild().appendChild(d.importNode(group, true));
//    						already_added.add(key);
//    					}
//                    }
//				}
//			}
//
//			TransformerFactory tf = TransformerFactory.newInstance();
//			Transformer transformer = tf.newTransformer();
//			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
//			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
//
//			for (InputFile inputFile : inputFiles) {
//				String key = project.getKey() + ":" + inputFile.relativePath();
//				if (duplicated_blocks.containsKey(key)) {
//					context.saveMeasure(inputFile, CoreMetrics.DUPLICATED_FILES, 1.0);
//					context.saveMeasure(inputFile, CoreMetrics.DUPLICATED_BLOCKS, duplicated_blocks.get(key));
//					context.saveMeasure(inputFile, CoreMetrics.DUPLICATED_LINES, duplicated_lines.get(key));
//				} else {
//					context.saveMeasure(inputFile, CoreMetrics.DUPLICATED_FILES, 0.0);
//				}
//
//				if (duplicated_xml.containsKey(key)) {
//					StringWriter writer = new StringWriter();
//					transformer.transform(new DOMSource(duplicated_xml.get(key)), new StreamResult(writer));
//					context.saveMeasure(inputFile, new Measure<String>(CoreMetrics.DUPLICATED_LINES, writer.getBuffer().toString()));
//				}
//			}
//
//		} catch (Exception e) {
//			LOG.error("Exception raised while processing duplications.", e);
//		}
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
