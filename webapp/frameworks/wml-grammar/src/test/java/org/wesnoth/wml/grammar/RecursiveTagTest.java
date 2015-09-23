package org.wesnoth.wml.grammar;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class RecursiveTagTest {

    @Test
    public void testRule() {
        ANTLRInputStream input = new ANTLRInputStream("[parent_tag]\n" +
                "     key1=value1\n" +
                "     [child_tag]\n" +
                "         key2=value2\n" +
                "     [/child_tag]\n" +
                " [/parent_tag]");
        WMLLexer lexer = new WMLLexer(input);
        TokenStream tokens = new CommonTokenStream(lexer);

        WMLParser parser = new WMLParser(tokens);

        parser.removeErrorListeners();
        parser.setErrorHandler(new ExceptionThrowingErrorHandler());

        WMLParser.DocumentContext element = parser.document();
        WMLParser.TagContext parentTag = element.tag().get(0);
        assertThat(parentTag.Name().stream().map(node -> node.getSymbol().getText()).findFirst().get(), is("parent_tag"));
        assertThat(parentTag.content().chardata().get(0).TEXT().getText(), is("\n     key1=value1\n     "));
        List<WMLParser.TagContext> childTag = parentTag.content().tag();
        assertThat(childTag.get(0).Name().stream().map(node -> node.getSymbol().getText()).findFirst().get(), is("child_tag"));
        assertThat(childTag.get(0).content().chardata().get(0).TEXT().getText(), is("\n         key2=value2\n     "));
        assertNull(element.exception);
    }
}
