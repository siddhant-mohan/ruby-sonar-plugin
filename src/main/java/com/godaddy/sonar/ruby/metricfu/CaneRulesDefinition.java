package com.godaddy.sonar.ruby.metricfu;

import com.godaddy.sonar.ruby.RubyPlugin;
import com.godaddy.sonar.ruby.core.Ruby;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinitionXmlLoader;

public class CaneRulesDefinition implements RulesDefinition {
    private final RulesDefinitionXmlLoader xmlLoader;
    
    public CaneRulesDefinition(RulesDefinitionXmlLoader xmlLoader) {
        this.xmlLoader = xmlLoader;
    }
    
    @Override
    public void define(Context context) {
        NewRepository repository = context.createRepository(RubyPlugin.KEY_REPOSITORY_CANE, Ruby.KEY).setName(RubyPlugin.NAME_REPOSITORY_CANE);
        xmlLoader.load(repository, getClass().getResourceAsStream("/com/godaddy/sonar/ruby/metricfu/CaneRulesRepository.xml"), "UTF-8");
        repository.done();
    }
}
