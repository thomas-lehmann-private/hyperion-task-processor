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

import magic.system.hyperion.data.StringValue;
import magic.system.hyperion.generics.Pair;
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
 * Testing class {@link Model}.
 */
@DisplayName("Testing Model")
@SuppressWarnings("checkstyle:multiplestringliterals")
public class ModelTest {
    /**
     * Testing default with no data.
     */
    @Test
    public void testDefault() {
        final var model = new Model();
        assertTrue(model.isEmpty());
        assertTrue(model.getData().isEmpty());
        assertEquals(0, model.getData().size());
    }

    /**
     * Testing {@link Model#of(Pair[])} function.
     */
    @Test
    public void testOf() {
        final var model = Model.of(
                Pair.of("key1", StringValue.of("value1")),
                Pair.of("key2", StringValue.of("value2")));

        assertFalse(model.isEmpty());
        assertFalse(model.getData().isEmpty());
        assertEquals(2, model.getData().size());
        assertEquals("value1", model.getData().getString("key1"));
        assertEquals("value2", model.getData().getString("key2"));
    }

    /**
     * Testing {@link Model#hashCode()}.
     *
     * @param bExpectedToBeEqual when true both hashCode should be equal.
     * @param modelA one model.
     * @param modelB another model.
     */
    @ParameterizedTest(name = "#{index} - equals?: {0}, modelA: {1}, modelB: {2}")
    @MethodSource("provideEqualsHasCodeTestData")
    public void testHashCode(final boolean bExpectedToBeEqual, final Model modelA,
                             final Model modelB) {
        if (bExpectedToBeEqual) {
            assertEquals(modelA.hashCode(), modelB.hashCode());
        } else {
            assertNotEquals(modelA.hashCode(), modelB.hashCode());
        }
    }

    /**
     * Testing {@link Model#equals(Object)}.
     *
     * @param bExpectedToBeEqual when true both models should be equal.
     * @param modelA one model.
     * @param modelB another model.
     */
    @ParameterizedTest(name = "#{index} - equals?: {0}, modelA: {1}, modelB: {2}")
    @MethodSource("provideEqualsHasCodeTestData")
    public void testEquals(final boolean bExpectedToBeEqual, final Model modelA,
                           final Model modelB) {
        if (bExpectedToBeEqual) {
            assertEquals(modelA, modelB);
        } else {
            assertNotEquals(modelA, modelB);
        }
    }

    /**
     * Provide tests data for testing "equals" and "hashCode".
     *
     * @return list of data.
     */
    private static Stream<Arguments> provideEqualsHasCodeTestData() {
        return Stream.of(
                Arguments.of(true, new Model(), new Model()),
                Arguments.of(true, Model.of(), Model.of()),
                Arguments.of(false, Model.of(),
                        Model.of(Pair.of("key1", StringValue.of("value1")))),
                Arguments.of(true, Model.of(Pair.of("key1", StringValue.of("value1"))),
                        Model.of(Pair.of("key1", StringValue.of("value1"))))
        );
    }
}
