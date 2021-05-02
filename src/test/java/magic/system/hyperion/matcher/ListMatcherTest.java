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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Testing of class {@link ListMatcher}.
 *
 * @author Thomas Lehmann
 */
@DisplayName("Testing ListMatcher class")
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
    @DisplayName("Testing require at least once")
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

    /**
     * Testing requiring minimum count.
     *
     * @param strValue the value to require
     * @param iCount the exact count required
     * @param listData the list to verify
     * @param bExpectedToFail when true then expected to fail other expected to
     * succeed.
     */
    @DisplayName("require minimum count")
    @ParameterizedTest(
            name = "testRequireCount - #{index} value={0}, count={1}, data={2}, expected fail={3}")
    @MethodSource("provideRequireCountTestData")
    public void testRequireCount(final String strValue, final int iCount,
            final List<String> listData, final boolean bExpectedToFail) {
        final var matcher = new ListMatcher<String>();
        matcher.requireCount(strValue, iCount);
        if (bExpectedToFail) {
            assertFalse(matcher.matches(listData));
        } else {
            assertTrue(matcher.matches(listData));
        }
    }

    /**
     * Testing requiring exact count.
     *
     * @param strValue the value to require
     * @param iCount the exact count required
     * @param listData the list to verify
     * @param bExpectedToFail when true then expected to fail other expected to
     * succeed. W
     */
    @ParameterizedTest(
            name = "Test exact count - #{index} value={0}, count={1}, data={2}, expected fail={3}")
    @MethodSource("provideRequireExactCountTestData")
    public void testRequireExactCount(final String strValue, final int iCount,
            final List<String> listData, final boolean bExpectedToFail) {
        final var matcher = new ListMatcher<String>();
        matcher.requireExactCount(strValue, iCount);

        if (bExpectedToFail) {
            assertFalse(matcher.matches(listData));
        } else {
            assertTrue(matcher.matches(listData));
        }
    }

    @DisplayName("testing for allowed values")
    @ParameterizedTest(name = "Test allow - #{index} values={0}, data={1}, expected fail={2}")
    @MethodSource("provideAllowTestData")
    public void testAllow(final List<String> values, final List<String> listData,
            final boolean bExpectedToFail) {
        final var matcher = new ListMatcher<String>();
        values.forEach(value -> matcher.allow(value));

        if (bExpectedToFail) {
            assertFalse(matcher.matches(listData));
        } else {
            assertTrue(matcher.matches(listData));
        }
    }

    /**
     * Test data for require minimum count test.
     * <ul>
     * <li>too little
     * <li>correct
     * <li>correct
     * </ul>
     *
     * @return list of use cases.
     */
    private static Stream<Arguments> provideRequireCountTestData() {
        //CHECKSTYLE.OFF: MagicNumber - ok here
        return Stream.of(
                Arguments.of(THREE, 3, List.of(THREE, THREE), true),
                Arguments.of(THREE, 3, List.of(THREE, THREE, THREE),
                        false),
                Arguments.of(THREE, 3, List.of(THREE, THREE, THREE, THREE),
                        false)
        );
        //CHECKSTYLE.ON: MagicNumber
    }

    /**
     * Test data for require exact count test.
     * <ul>
     * <li>too little
     * <li>correct
     * <li>too much
     * </ul>
     *
     * @return list of use cases.
     */
    private static Stream<Arguments> provideRequireExactCountTestData() {
        //CHECKSTYLE.OFF: MagicNumber - ok here
        return Stream.of(
                Arguments.of(THREE, 3, List.of(THREE, THREE), true),
                Arguments.of(THREE, 3, List.of(THREE, THREE, THREE),
                        false),
                Arguments.of(THREE, 3, List.of(THREE, THREE, THREE, THREE),
                        true)
        );
        //CHECKSTYLE.ON: MagicNumber
    }

    /**
     * Test data for allowing values.
     * <ul>
     * <li> not allowed values
     * <li> allowed and not allowed values mixed
     * <li> one allowed value
     * <li> allowed values, but not all allowed values are given
     * <li> allowed values, all allowed values are given
     * </ul>
     *
     * @return list of use cases.
     */
    private static Stream<Arguments> provideAllowTestData() {
        return Stream.of(
                Arguments.of(List.of(THREE), List.of(ONE, TWO), true),
                Arguments.of(List.of(THREE), List.of(ONE, TWO, THREE), true),
                Arguments.of(List.of(THREE), List.of(THREE), false),
                Arguments.of(List.of(THREE, FOUR), List.of(THREE), false),
                Arguments.of(List.of(THREE, FOUR), List.of(THREE, FOUR), false)
        );
    }
}
