package com.godaddy.sonar.ruby.duplications;

import com.godaddy.sonar.ruby.core.Ruby;
import net.sourceforge.pmd.cpd.SourceCode;
import net.sourceforge.pmd.cpd.TokenEntry;
import net.sourceforge.pmd.cpd.Tokenizer;
import net.sourceforge.pmd.cpd.Tokens;
import org.jruby.CompatVersion;
import org.jruby.lexer.yacc.LexerSource;
import org.jruby.lexer.yacc.RubyYaccLexer;
import org.jruby.lexer.yacc.RubyYaccLexer.LexState;
import org.jruby.parser.DefaultRubyParser;
import org.jruby.parser.ParserConfiguration;
import org.jruby.parser.ParserSupport;
import org.jruby.parser.RubyParserResult;
import org.sonar.api.batch.AbstractCpdMapping;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.resources.Language;
import org.sonar.api.resources.Project;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RubyCPDMapping extends AbstractCpdMapping {
    Ruby ruby;
    FileSystem fileSystem;

    public RubyCPDMapping(Ruby ruby, Project project, FileSystem fileSystem) {
        this.ruby = ruby;
        this.fileSystem = fileSystem;
    }

    @Override
    public Language getLanguage() {
        return ruby;
    }

    @Override
    public Tokenizer getTokenizer() {
        return new RubyCPDTokenizer();
    }

    class RubyCPDTokenizer implements Tokenizer {

        @Override
        public void tokenize(SourceCode source, Tokens cpdTokens) throws IOException {
            String fileName = source.getFileName();
            List<String> l = new ArrayList<String>();
            ParserConfiguration c = new ParserConfiguration(org.jruby.Ruby.getGlobalRuntime(), 0, false, CompatVersion.BOTH);
            LexerSource s = LexerSource.getSource(source.getFileName(), new FileInputStream(new File(source.getFileName())), l, c);
            ParserSupport ps = new ParserSupport();
            ps.setConfiguration(c);
            ps.pushLocalScope();
            ps.setResult(new RubyParserResult());
            RubyYaccLexer lex = new RubyYaccLexer();
            lex.reset();
            lex.setSource(s);
            lex.setParserSupport(ps);
            lex.setEncoding(RubyYaccLexer.UTF8_ENCODING);
            lex.setState(LexState.EXPR_BEG);
            while (lex.nextToken() > 0) {
                cpdTokens.add(new TokenEntry(DefaultRubyParser.yyName(lex.token()), fileName, lex.getPosition().getStartLine()));
            }
        }
    }

    public static void main(String[] args) {
        RubyCPDMapping m = new RubyCPDMapping(new Ruby(), null, null);
        Tokens t = new Tokens();
        Tokenizer toker = m.getTokenizer();

        SourceCode s = new SourceCode(new SourceCode.FileCodeLoader(new File("/home/gallen/work/ruby-sonar-plugin-sync/src/test/resources/test-data/hello_world.rb"), "utf-8"));
        try {
            toker.tokenize(s, t);
            Iterator<TokenEntry> i = t.iterator();
            while (i.hasNext()) {
                TokenEntry e = i.next();
                System.out.println(e.getBeginLine() + ": " + e.getValue() + ", " + e.getTokenSrcID());
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
