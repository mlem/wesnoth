package org.wesnoth.wml.grammar;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.misc.NotNull;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class WmlParserListenerTest {

    Logger LOGGER = LoggerFactory.getLogger(WmlParserListenerTest.class);

    @Test
    public void test() {
        MyWMLParserBaseListener wmlParserBaseListener = new MyWMLParserBaseListener();

        ANTLRInputStream input = new ANTLRInputStream("[parent_tag]\n" +
                "     key1=value1\n" +
                "     [child_tag]\n" +
                "         key2=value2\n" +
                "     [/child_tag]\n" +
                " [/parent_tag]");
        WMLLexer lexer = new WMLLexer(input);
        TokenStream tokens = new CommonTokenStream(lexer);

        WMLParser parser = new WMLParser(tokens);

        parser.addParseListener(wmlParserBaseListener);

        parser.removeErrorListeners();
        parser.setErrorHandler(new ExceptionThrowingErrorHandler());

        WMLParser.DocumentContext element = parser.document();

        WMLTag parent = wmlParserBaseListener.getRootTag();
        assertThat(parent.name, is("parent_tag"));
        assertThat(parent.attributes, is("\n     key1=value1\n     "));


    }

    public class WMLTag {

        String name;
        WMLTag parent;
        List<WMLTag> subTags = new ArrayList<>();
        String attributes;

        public WMLTag(String name) {
            this.name = name;
        }

        public void addTag(WMLTag current) {
            subTags.add(current);
            current.parent = this;
        }
    }

    private class MyWMLParserBaseListener extends WMLParserBaseListener {

        private WMLTag root;

        private Stack<WMLTag> stack = new Stack<>();


        @Override
        public void enterTag(@NotNull WMLParser.TagContext ctx) {
            super.enterTag(ctx);
            LOGGER.info("Entering TAG on depth " + ctx.depth());
            WMLTag current = new WMLTag("no_name");

            if (stack.size() == 0) {
                root = current;
            } else {
                root.addTag(current);
            }
            stack.push(current);


        }

        @Override
        public void exitTag(@NotNull WMLParser.TagContext ctx) {
            super.exitTag(ctx);
            LOGGER.info(ctx.toStringTree());
            WMLTag current = stack.pop();
            current.name = ctx.Name().stream()
                    .map(node -> node.getSymbol().getText())
                    .findFirst().orElse("no_name");
            LOGGER.info("Exiting TAG with name " + current.name);
        }

        @Override
        public void enterChardata(@NotNull WMLParser.ChardataContext ctx) {
            super.enterChardata(ctx);
            LOGGER.info("enterChardata");
        }

        @Override
        public void exitChardata(@NotNull WMLParser.ChardataContext ctx) {
            super.exitChardata(ctx);
            String text = ctx.getText();
            if (!text.trim().isEmpty()) {
                stack.peek().attributes = text;
                LOGGER.info("attributes found: " + text);
            }
            LOGGER.info("exitChardata");
        }

        @Override
        public void enterContent(@NotNull WMLParser.ContentContext ctx) {
            super.enterContent(ctx);
            LOGGER.info("enterContent");
        }

        @Override
        public void exitContent(@NotNull WMLParser.ContentContext ctx) {
            super.exitContent(ctx);
            LOGGER.info("exitContent");
        }

        public WMLTag getRootTag() {
            return root;
        }
    }
}
