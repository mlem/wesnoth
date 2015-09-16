package org.wesnoth.network.compression;

import org.hamcrest.*;

import java.io.*;

public class StreamMatcher {
    static Matcher<InputStream> sameBytes(final InputStream expected) {
        return new TypeSafeMatcher<InputStream>() {
            @Override
            public void describeTo(Description description) {
                description.appendText(failure);
            }

            String failure = "";

            public boolean matchesSafely(InputStream underTest) {
                BufferedInputStream actualStream;
                if (underTest instanceof BufferedInputStream) {
                    actualStream = (BufferedInputStream) underTest;
                } else {
                    actualStream = new BufferedInputStream(underTest);
                }
                BufferedInputStream expectedStream;
                if (expected instanceof BufferedInputStream) {
                    expectedStream = (BufferedInputStream) expected;
                } else {
                    expectedStream = new BufferedInputStream(expected);
                }

                BufferedReader expectedReader = new BufferedReader(new InputStreamReader(expectedStream));
                BufferedReader actual = new BufferedReader(new InputStreamReader(actualStream));

                String line;
                try {
                    while ((line = expectedReader.readLine()) != null) {
                        Matcher<?> equalsMatcher = CoreMatchers.equalTo(line);
                        String actualLine = actual.readLine();
                        if (!equalsMatcher.matches(actualLine)) {
                            StringDescription description = new StringDescription(new StringBuffer(actualLine));
                            equalsMatcher.describeTo(description);
                            equalsMatcher.describeMismatch(line, new StringDescription(new StringBuffer(line)));
                            this.describeMismatch(line, new StringDescription(new StringBuffer(line)));
                            failure = description.toString();
                            return false;
                        }
                    }
                } catch (IOException e) {
                    failure = e.getMessage();
                    e.printStackTrace();
                    return false;
                }
                return true;
            }

        };
    }
}
