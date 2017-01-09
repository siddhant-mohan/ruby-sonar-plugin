package com.godaddy.sonar.ruby.parsers;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.StringUtils;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.io.File;
import java.io.IOException;

public final class CommentCountParser {

    private static final Logger LOG = Loggers.get(CommentCountParser.class);

    private CommentCountParser() {
    }

    public static int countLinesOfComment(File file) {
        int numComments = 0;
        LineIterator iterator = null;
        try {
            iterator = FileUtils.lineIterator(file);

            while (iterator.hasNext()) {
                String line = iterator.nextLine();
                if (StringUtils.startsWith(line.trim(), "#")) {
                    numComments++;
                }
            }
        } catch (IOException e) {
            LOG.error("Error determining comment count for file " + file, e);
        } finally {
            LineIterator.closeQuietly(iterator);
        }

        return numComments;
    }
}