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

import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Testing of class {@link MatrixParameters}.
 *
 * @author Thomas Lehmann
 */
@DisplayName("Testing of MatrixParameters")
@SuppressWarnings("checkstyle:multiplestringliterals")
public class MatrixParametersTest {
    /**
     * Testing default usage.
     */
    @Test
    public void testDefaultUsage() {
        final var matrixParameters = new MatrixParameters("test");
        final var parameters = Map.of("key1", "value1", "key2", "value2");
        matrixParameters.setParameters(parameters);

        assertEquals("test", matrixParameters.getTitle());
        assertEquals(parameters.size(), matrixParameters.getParameters().size());
        assertEquals("value1", matrixParameters.getParameters().get("key1"));
        assertEquals("value2", matrixParameters.getParameters().get("key2"));
    }

    /**
     * Testing {@link MatrixParameters#hashCode()}.
     *
     * @param bExpectedToBeEqual when true them expected to be equals.
     * @param paramsA one instance of {@link MatrixParameters}.
     * @param paramsB another instance of {@link MatrixParameters}.
     */
    @ParameterizedTest(name = "#{index} - equals?: {0}, paramsA={1}, paramsB{2}")
    @MethodSource("provideHashCodeEqualsTestData")
    public void testHashCode(final boolean bExpectedToBeEqual, final MatrixParameters paramsA,
                             final MatrixParameters paramsB) {
        if (bExpectedToBeEqual) {
            assertEquals(paramsA.hashCode(), paramsB.hashCode());
        } else {
            assertNotEquals(paramsA.hashCode(), paramsB.hashCode());
        }
    }

    /**
     * Testing {@link MatrixParameters#equals(Object)}.
     *
     * @param bExpectedToBeEqual when true them expected to be equals.
     * @param paramsA one instance of {@link MatrixParameters}.
     * @param paramsB another instance of {@link MatrixParameters}.
     */
    @ParameterizedTest(name = "#{index} - equals?: {0}, paramsA={1}, paramsB{2}")
    @MethodSource("provideHashCodeEqualsTestData")
    public void testEquals(final boolean bExpectedToBeEqual, final MatrixParameters paramsA,
                             final MatrixParameters paramsB) {
        if (bExpectedToBeEqual) {
            assertEquals(paramsA, paramsB);
        } else {
            assertNotEquals(paramsA, paramsB);
        }
    }

    /**
     * Provide test data for testing hashCode and equals methods.
     *
     * @return test data.
     */
    private static Stream<Arguments> provideHashCodeEqualsTestData() {
        return Stream.of(
            Arguments.of(true,
                    combine(new MatrixParameters("test1"), Map.of("key1", "value1")),
                    combine(new MatrixParameters("test1"), Map.of("key1", "value1"))),
            Arguments.of(false,
                    combine(new MatrixParameters("test1"), Map.of("key1", "value1")),
                    combine(new MatrixParameters("test2"), Map.of("key1", "value1"))),
                Arguments.of(false,
                        combine(new MatrixParameters("test1"), Map.of("key1", "value1")),
                        combine(new MatrixParameters("test1"), Map.of()))
        );
    }

    /**
     * Helper function {@link MatrixParametersTest#provideHashCodeEqualsTestData()}.
     *
     * @param matrixParameters instance of {@link MatrixParameters}.
     * @param parameters map of key/value to be set into the matrix parameters.
     * @return changed matrix parameters.
     */
    private static MatrixParameters combine(final MatrixParameters matrixParameters,
                                            final Map<String, String> parameters) {
        matrixParameters.setParameters(parameters);
        return matrixParameters;
    }
}
