package com.godaddy.sonar.ruby.metricfu;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.godaddy.sonar.ruby.RubyPlugin;
import com.godaddy.sonar.ruby.core.Ruby;
import org.apache.commons.configuration.Configuration;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.fs.*;
import org.sonar.api.batch.fs.internal.DefaultFilePredicates;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.batch.fs.internal.DefaultInputDir;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.config.Settings;
import org.sonar.api.resources.Project;
import org.sonar.api.scan.filesystem.PathResolver;

import com.godaddy.sonar.ruby.core.LanguageRuby;


public class MetricfuComplexitySensorTest
{
	

	private PathResolver pathResolver;
	private SensorContext sensorContext;
	private MetricfuYamlParser metricfuYamlParser;
	private MetricfuComplexitySensor metricfuComplexitySensor;
	private Configuration config;
	private Project project;

	private Settings settings;
	private DefaultFileSystem fs;

	@Before
	public void setUp() throws Exception
	{
		
		pathResolver = EasyMock.createMock(PathResolver.class);
		fs = EasyMock.createMock(DefaultFileSystem.class);
		
		metricfuYamlParser = EasyMock.createMock(MetricfuYamlParser.class);
		settings = new Settings();
		settings.appendProperty(RubyPlugin.METRICFU_COMPLEXITY_METRIC_PROPERTY, "saikuro");

		metricfuComplexitySensor = new MetricfuComplexitySensor(settings, fs, metricfuYamlParser);
		config = EasyMock.createMock(Configuration.class);
		expect(config.getString("sonar.language", "java")).andStubReturn("ruby");

		project = new Project("test project");
		project.setLanguage(LanguageRuby.INSTANCE);
//		project.setConfiguration(config);

	}

	@Test
	@Ignore
	public void testConstructor()
	{
		assertNotNull(metricfuComplexitySensor);
	}

	@Test
	@Ignore
	public void analyseShouldNotFailWithCorrectParameters() throws IOException
	{
		// initialize file list return value
		List<InputFile> sourceFiles= new ArrayList<InputFile>();
		DefaultInputFile testFile = new DefaultInputFile("test project", "lib/some_path/foo_bar.rb");
		
		// set the file module path to the local working directory in order
		// to use file().getName() method and add file to the list
		Path modulePath = FileSystems.getDefault().getPath(".");
		testFile.setModuleBaseDir(modulePath);
		sourceFiles.add(testFile);
		
		// create a file system predicate factory and set the predicates method as well as the input files method
		FilePredicates predicates = new DefaultFileSystem(new File("src")).predicates();
		expect(fs.predicates()).andReturn(predicates);
		expect(fs.inputFiles(EasyMock.isA(FilePredicate.class))).andReturn(sourceFiles);
		
		// replay the file system and run the analysis
		EasyMock.replay(fs);
  		metricfuComplexitySensor.analyse(project, sensorContext);
		EasyMock.verify();
	}
}
