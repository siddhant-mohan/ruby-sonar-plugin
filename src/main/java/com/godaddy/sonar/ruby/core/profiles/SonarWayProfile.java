package com.godaddy.sonar.ruby.core.profiles;

import org.sonar.api.profiles.ProfileDefinition;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.profiles.XMLProfileParser;
import org.sonar.api.utils.ValidationMessages;

public final class SonarWayProfile extends ProfileDefinition {

    private final XMLProfileParser parser;

    public SonarWayProfile(XMLProfileParser parser) {
        this.parser = parser;
    }

    @Override
    public RulesProfile createProfile(ValidationMessages messages) {
        RulesProfile profile = parser.parseResource(getClass().getClassLoader(), "ruby/profiles/sonar-way-profile.xml", messages);
        profile.setDefaultProfile(true);
        return profile;
    }
}
