 package org.wesnoth.wml.grammar;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(Parameterized.class)
public class GrammarTest {
    @Parameterized.Parameters (name = "{index}: ({1}) parseable={0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                /* Valid rules. */
                { true, "[mustlogin]\n[/mustlogin]\n", "mustlogin" },
                { true, "[hi]\nhi=hh\n[/hi]", "hi" },
                { true, "[parent_tag]\n" +
                        "     key1=value1\n" +
                        "     [child_tag]\n" +
                        "         key2=value2\n" +
                        "     [/child_tag]\n" +
                        " [/parent_tag]\n", "parent_tag" },
                { false, "# this is a comment", null },

        });
    }

    private final boolean testValid;
    private final String testString;
    private String rootTagName;

    public GrammarTest(boolean testValid, String testString, String rootTagName) {
        this.testValid = testValid;
        this.testString = testString;
        this.rootTagName = rootTagName;
    }

    @Test
    public void testRule() {
        ANTLRInputStream input = new ANTLRInputStream(this.testString);
        WMLLexer lexer = new WMLLexer(input);
        TokenStream tokens = new CommonTokenStream(lexer);

        WMLParser parser = new WMLParser(tokens);

        parser.removeErrorListeners();
        parser.setErrorHandler(new ExceptionThrowingErrorHandler());

        if (this.testValid) {
            WMLParser.DocumentContext element = parser.document();
            List<TerminalNode> tagName = element.tag().Name();
            assertThat(tagName.stream().map(node -> node.getSymbol().getText()).findFirst().get(), is(rootTagName));
            assertNull(element.exception);
        } else {
            try {
                WMLParser.DocumentContext element = parser.document();
                fail("Failed on \"" + this.testString + "\"");
            } catch (RuntimeException e) {
                // deliberately do nothing
            }
        }
    }
}