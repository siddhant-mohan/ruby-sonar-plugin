package com.godaddy.sonar.ruby.core.profiles;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.profiles.XMLProfileParser;
import org.sonar.api.rules.RuleFinder;
import org.sonar.api.utils.ValidationMessages;

import static org.junit.Assert.assertNotNull;

public class SonarWayProfileTest {
    private IMocksControl mocksControl;
    private XMLProfileParser parser;
    private RuleFinder ruleFinder;
    private SonarWayProfile profile;
    private ValidationMessages messages;

    @Before
    public void setup() {
        mocksControl = EasyMock.createControl();
        ruleFinder = mocksControl.createMock(RuleFinder.class);
        parser = new XMLProfileParser(ruleFinder);
        messages = ValidationMessages.create();

        profile = new SonarWayProfile(parser);
    }

    @Test
    public void testConstructor() {
        assertNotNull(profile);
    }

    @Test
    public void testCreateProfile() {
//       RulesProfile rulesProfile = profile.createProfile(messages);
//       assertNotNull(rulesProfile);
//       assertEquals("Sonar Way", rulesProfile.getName());
//       assertEquals("ruby", rulesProfile.getLanguage());
    }

}
