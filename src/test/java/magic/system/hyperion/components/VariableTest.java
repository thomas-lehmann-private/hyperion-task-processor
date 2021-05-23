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
package magic.system.hyperion.components;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testing class {@link Variable}.
 *
 * @author Thomas Lehmann
 */
@DisplayName("Testing class Variable")
@SuppressWarnings("checkstyle:multiplestringliterals")
public class VariableTest {
    /**
     * Testing regex applied to whole string.
     */
    @Test
    public void testRegexCompleteString() {
        final var variable = new Variable("test", "[A-Za-z]+", 0, false);
        assertTrue(variable.setValue("!!! Gandalf !!!"));
        assertEquals("test", variable.getName());
        assertEquals("Gandalf", variable.getValue());

        assertFalse(variable.setValue("!!! 12345 !!!"));
        assertEquals("Gandalf", variable.getValue());
    }

    /**
     * Testing regex applied to whole multiline string.
     */
    @Test
    public void testRegexCompleteMultiLineString() {
        final var variable = new Variable("test", ".*", 0, false);
        assertTrue(variable.setValue("Gandalf\nFrodo"));
        assertEquals("test", variable.getName());
        assertEquals("Gandalf\nFrodo", variable.getValue());
    }

    /**
     * Testing regex applied line by line for a string.
     */
    @Test
    public void testRegexLineByLine() {
        final var variable = new Variable("test", "[A-Za-z]+", 0, true);
        assertTrue(variable.setValue("!!! Gandalf !!!\n!!! Frodo !!!"));
        assertEquals("test", variable.getName());
        assertEquals("Gandalf\nFrodo", variable.getValue());
    }

    /**
     * Testing of {@link Variable#hashCode()}.
     *
     * @param bExpectedToBeEqual true when both variables should have equal has code.
     * @param variableA          one variable.
     * @param variableB          another variable.
     */
    @ParameterizedTest(name = "#{index} - equal?: {0}, variableA: {1}, variableB: {1}")
    @MethodSource("provideHashCodeEqualsTestData")
    void testHashCode(boolean bExpectedToBeEqual, final Variable variableA,
                      final Variable variableB) {
        if (bExpectedToBeEqual) {
            assertEquals(variableA.hashCode(), variableB.hashCode());
        } else {
            assertNotEquals(variableA.hashCode(), variableB.hashCode());
        }
    }

    /**
     * Testing of {@link Variable#equals(Object)}.
     *
     * @param bExpectedToBeEqual true when both variables should be equal
     * @param variableA          one variable.
     * @param variableB          another variable.
     */
    @ParameterizedTest(name = "#{index} - equal?: {0}, variableA: {1}, variableB: {1}")
    @MethodSource("provideHashCodeEqualsTestData")
    void testEquals(boolean bExpectedToBeEqual, final Variable variableA,
                    final Variable variableB) {
        if (bExpectedToBeEqual) {
            assertEquals(variableA, variableB);
        } else {
            assertNotEquals(variableA, variableB);
        }
    }

    /**
     * Test data for testing hashCode and equals.
     *
     * @return test data.
     */
    private static Stream<Arguments> provideHashCodeEqualsTestData() {
        return Stream.of(
                Arguments.of(true,
                        new Variable("test1", "[A-Za-z]+", 0, true),
                        new Variable("test1", "[A-Za-z]+", 0, true)),
                Arguments.of(false,
                        new Variable("test1", "[A-Za-z]+", 0, true),
                        new Variable("test2", "[A-Za-z]+", 0, true)),
                Arguments.of(false,
                        new Variable("test1", "[A-Za-z]+", 0, true),
                        new Variable("test1", "[A-Z]+", 0, true)),
                Arguments.of(false,
                        new Variable("test1", "[A-Za-z]+", 0, true),
                        new Variable("test1", "[A-Za-z]+", 1, true)),
                Arguments.of(false,
                        new Variable("test1", "[A-Za-z]+", 0, true),
                        new Variable("test1", "[A-Za-z]+", 0, false))
        );
    }
}
