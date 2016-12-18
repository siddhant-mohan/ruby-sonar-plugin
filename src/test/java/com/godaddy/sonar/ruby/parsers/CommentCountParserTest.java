package com.godaddy.sonar.ruby.parsers;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class CommentCountParserTest {
    public static String INPUT_SOURCE_FILE = "src/test/resources/test-data/hello_world.rb";
    
    @Test
    public void testCountLinesOfComment() {
        int comments = CommentCountParser.countLinesOfComment(new File(INPUT_SOURCE_FILE));
        assertEquals(comments, 4);
    }
}
