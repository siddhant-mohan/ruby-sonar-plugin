package com.godaddy.sonar.ruby.resources;

import com.godaddy.sonar.ruby.core.Ruby;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class RubyFileTest {
    protected Ruby ruby;

    @Before
    public void setUp() {
        ruby = new Ruby();
    }

    @After
    public void tearDown() {

    }

    @Test
    public void testRubyFile() {
        assertEquals("ruby", ruby.getKey());
        assertEquals("Ruby", ruby.getName());

        String[] expected = new String[]{".rb"};
        assertArrayEquals(expected, ruby.getFileSuffixes());
    }

}
