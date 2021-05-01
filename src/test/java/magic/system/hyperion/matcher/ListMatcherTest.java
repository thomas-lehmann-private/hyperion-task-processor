/*
 * The MIT License
 *
 * Copyright 2021 Thomas Lehmann.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package magic.system.hyperion.matcher;

import java.util.List;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Testing of class {@link ListMatcher}.
 *
 * @author Thomas Lehmann
 */
public class ListMatcherTest {

    /**
     * Test value.
     */
    private static final String TWO = "two";

    /**
     * Test value.
     */
    private static final String ONE = "one";

    /**
     * Test value.
     */
    private static final String THREE = "three";

    /**
     * Test value.
     */
    private static final String FOUR = "four";

    /**
     * Name test data element.
     */
    private static final String EXPECTED_TO_FAIL = "expected to fail";

    /**
     * Name test data element.
     */
    private static final String DATA = "data";

    /**
     * Testing default without conditions.
     */
    @Test
    public void testDefault() {
        final var matcher = new ListMatcher<String>();
        assertTrue(matcher.matches(List.of(ONE, TWO, THREE, FOUR)));
    }

    /**
     * Testing {@link ListMatcher#requireOnce(java.lang.Object)}.
     */
    @Test
    public void testRequiredOnce() {
        final var matcher = new ListMatcher<String>();

        matcher.requireOnce(THREE);
        assertFalse(matcher.matches(List.of(ONE, TWO)), "missing 'three'");
        assertTrue(matcher.matches(List.of(ONE, TWO, THREE)),
                "on time 'three' should be fine");
        assertTrue(matcher.matches(List.of(THREE, THREE)),
                "two times 'three' should also fine");

        matcher.requireOnce(FOUR);
        assertFalse(matcher.matches(List.of(THREE, THREE)), "missing 'four'");
        assertTrue(matcher.matches(List.of(THREE, THREE, FOUR)),
                "one time 'four' should be fine");
        assertTrue(matcher.matches(List.of(THREE, THREE, FOUR, FOUR)),
                "two times time 'four' should also be fine");
    }

    @ParameterizedTest(name = "{displayName} with [{arguments}]")
    @MethodSource("provideRequireCountTestData")
    public void testRequireCount(final List<String> listData, final boolean bExpectedToFail) {
        final var matcher = new ListMatcher<String>();
        //CHECKSTYLE.OFF: MagicNumber - ok here
        matcher.requireCount(THREE, 3);
        //CHECKSTYLE.ON: MagicNumber

        if (bExpectedToFail) {
            assertFalse(matcher.matches(listData));
        } else {
            assertTrue(matcher.matches(listData));
        }
    }

    private static Stream<Arguments> provideRequireCountTestData() {
        return Stream.of(
                Arguments.of(Named.of(DATA, List.of(THREE)),
                        Named.of(EXPECTED_TO_FAIL, true)),
                Arguments.of(Named.of(DATA, List.of(THREE, THREE)),
                        Named.of(EXPECTED_TO_FAIL, true)),
                Arguments.of(Named.of(DATA, List.of(THREE, THREE, THREE)),
                        Named.of(EXPECTED_TO_FAIL, false)),
                Arguments.of(Named.of(DATA, List.of(THREE, THREE, THREE, THREE)),
                        Named.of(EXPECTED_TO_FAIL, false))
        );
    }
}
