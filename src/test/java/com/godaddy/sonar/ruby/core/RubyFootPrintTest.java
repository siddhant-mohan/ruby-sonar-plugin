package com.godaddy.sonar.ruby.core;

import org.junit.Test;
import org.sonar.squidbridge.recognizer.Detector;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RubyFootPrintTest {
    @Test
    public void testGetDetectors() {
        RubyFootPrint footPrint = new RubyFootPrint();
        Set<Detector> detector = footPrint.getDetectors();
        assertNotNull(detector);
        assertEquals(2, detector.size());
    }

}
