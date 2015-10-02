package org.wesnoth.tools;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class WiresharkConverterCLITest {

    private TestAppender appender;

    @Before
    public void setup() {
        appender = new TestAppender();
        Logger.getRootLogger().addAppender(appender);

    }

    @After
    public void tearDown() {

        Logger.getRootLogger().removeAppender(appender);
    }

    @Test
    public void testWiresharkConverter() {


        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        String wiresharkValue = "00:00:00:25:1f:8b:08:00:00:00:00:00:00:ff:8b:ce:ca:cf:cc:8b:cf:c9:4f:4a:aa:8c:e5:8a:d6:47:e6:01:00:44:c8:fa:49:1b:00:00:00";

        WiresharkConverterCLI.main(wiresharkValue);

        final List<LoggingEvent> log = appender.getLog();
        assertThat(log.get(0).getMessage(), is("Packetsize: 37"));
        assertThat(log.get(1).getMessage(), is("Content:"));
        assertThat(log.get(2).getMessage(), is("[join_lobby]\n[/join_lobby]\n"));

    }

    class TestAppender extends AppenderSkeleton {
        private final List<LoggingEvent> log = new ArrayList<>();

        @Override
        public boolean requiresLayout() {
            return false;
        }

        @Override
        protected void append(final LoggingEvent loggingEvent) {
            log.add(loggingEvent);
        }

        @Override
        public void close() {
        }

        public List<LoggingEvent> getLog() {
            return new ArrayList<>(log);
        }
    }
}