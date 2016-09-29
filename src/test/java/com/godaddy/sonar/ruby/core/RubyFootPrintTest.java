package com.godaddy.sonar.ruby.core;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;
import org.sonar.squidbridge.recognizer.Detector;

public class RubyFootPrintTest {
	@Test
	public void testGetDetectors() {
		RubyFootPrint footPrint = new RubyFootPrint();
		Set<Detector> detector = footPrint.getDetectors();
		assertNotNull(detector);
		assertEquals(2, detector.size());
	}

}
