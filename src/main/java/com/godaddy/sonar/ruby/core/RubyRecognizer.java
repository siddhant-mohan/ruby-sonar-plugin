package com.godaddy.sonar.ruby.core;

import org.sonar.squidbridge.recognizer.CodeRecognizer;

public class RubyRecognizer extends CodeRecognizer {
    private static final double MAX_RECOGNIZE_PERCENT = 0.95;

    public RubyRecognizer() {
        super(MAX_RECOGNIZE_PERCENT, new RubyFootPrint());
    }
}