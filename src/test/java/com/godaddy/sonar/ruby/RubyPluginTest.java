package com.godaddy.sonar.ruby;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.godaddy.sonar.ruby.core.Ruby;
import com.godaddy.sonar.ruby.core.RubySourceCodeColorizer;
import com.godaddy.sonar.ruby.core.profiles.SonarWayProfile;
import com.godaddy.sonar.ruby.metricfu.CaneRulesDefinition;
import com.godaddy.sonar.ruby.metricfu.MetricfuComplexitySensor;
import com.godaddy.sonar.ruby.metricfu.MetricfuDuplicationSensor;
import com.godaddy.sonar.ruby.metricfu.MetricfuIssueSensor;
import com.godaddy.sonar.ruby.metricfu.ReekRulesDefinition;
import com.godaddy.sonar.ruby.metricfu.RoodiRulesDefinition;
import com.godaddy.sonar.ruby.simplecovrcov.SimpleCovRcovJsonParserImpl;
import com.godaddy.sonar.ruby.simplecovrcov.SimpleCovRcovSensor;

public class RubyPluginTest {

	@Test
	public void testGetExtensions() {
		// FIXME add back code duplication metrics once it's fixed
		RubyPlugin plugin = new RubyPlugin();
		List<Object> extensions = plugin.getExtensions();
		assertTrue(extensions.size() > 0);
		assertTrue(extensions.contains(Ruby.class));
		assertTrue(extensions.contains(SimpleCovRcovSensor.class));
		assertTrue(extensions.contains(SimpleCovRcovJsonParserImpl.class));
		assertTrue(extensions.contains(RubySourceCodeColorizer.class));
		assertTrue(extensions.contains(RubySensor.class));
		assertTrue(extensions.contains(MetricfuComplexitySensor.class));
        assertTrue(extensions.contains(SonarWayProfile.class));
        // assertTrue(extensions.contains(MetricfuDuplicationSensor.class));
        assertTrue(extensions.contains(MetricfuIssueSensor.class));
        assertTrue(extensions.contains(CaneRulesDefinition.class));
        assertTrue(extensions.contains(ReekRulesDefinition.class));
        assertTrue(extensions.contains(RoodiRulesDefinition.class));
	}
}
