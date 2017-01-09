package com.godaddy.sonar.ruby.core;

import org.junit.Before;
import org.junit.Test;
import org.sonar.colorizer.KeywordsTokenizer;
import org.sonar.colorizer.RegexpTokenizer;
import org.sonar.colorizer.StringTokenizer;
import org.sonar.colorizer.Tokenizer;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class RubySourceCodeColorizerTest {
    private RubySourceCodeColorizer colorizer;

    @Before
    public void setUp() throws Exception {
        colorizer = new RubySourceCodeColorizer();
    }

    @Test
    public void testConstructor() {
        assertNotNull(colorizer);
    }

    @Test
    public void testGetTokenizer() {
        List<Tokenizer> tokenList = colorizer.getTokenizers();

        assertEquals(tokenList.get(0).getClass(), KeywordsTokenizer.class);
        assertEquals(tokenList.get(1).getClass(), KeywordsTokenizer.class);
        assertEquals(tokenList.get(2).getClass(), RegexpTokenizer.class);
        assertEquals(tokenList.get(3).getClass(), StringTokenizer.class);
    }
}
