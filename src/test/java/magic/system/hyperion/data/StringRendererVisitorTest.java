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
package magic.system.hyperion.data;

import magic.system.hyperion.components.Model;
import magic.system.hyperion.components.TaskParameters;
import magic.system.hyperion.components.Variable;
import magic.system.hyperion.components.WithParameters;
import magic.system.hyperion.generics.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testing of class {@link StringRendererVisitor}.
 */
@DisplayName("Testing StringRendererVisitor class")
@TestMethodOrder(value = MethodOrderer.Random.class)
public class StringRendererVisitorTest {
    /**
     * Test key.
     */
    private static final String KEY = "test";

    /**
     * Test value of a variable.
     */
    private static final String VARIABLE_VALUE = "variable value";

    /**
     * Test value of a model attribute.
     */
    private static final String MODEL_VALUE = "model value";

    /**
     * Test value of a matrix attribute.
     */
    private static final String MATRIX_VALUE = "matrix value";

    /**
     * Test value of a with entry.
     */
    private static final String WITH_VALUE = "with value";

    /**
     * Test value for with entry index.
     */
    private static final int WITH_INDEX = 2;

    /**
     * Testing templating for a single string.
     *
     * @param strInitialValue  initial value with expression.
     * @param strExpectedValue expected rendered string.
     */
    @ParameterizedTest(name = "#{index} - initial value={0}, expected value={1}")
    @MethodSource("getRenderStringData")
    public void testRenderString(final String strInitialValue, final String strExpectedValue) {
        final var stringValue = StringValue.of(strInitialValue);
        stringValue.accept(new StringRendererVisitor(getDefaultTaskParameters()));
        assertEquals(strExpectedValue, stringValue.getValue());
    }

    /**
     * Testing templating for a list of values.
     *
     * @param strInitialValue  initial value with expression.
     * @param strExpectedValue expected rendered string.
     */
    @ParameterizedTest(name = "#{index} - initial value={0}, expected value={1}")
    @MethodSource("getRenderStringData")
    public void testRenderListOfValues(final String strInitialValue,
                                       final String strExpectedValue) {
        final var listOfValues
                = ListOfValues.of(StringValue.of(strInitialValue), StringValue.of(strInitialValue));
        listOfValues.accept(new StringRendererVisitor(getDefaultTaskParameters()));
        assertEquals(strExpectedValue, listOfValues.get(0).toString());
        assertEquals(strExpectedValue, listOfValues.get(1).toString());
    }

    /**
     * Testing templating for attribute map.
     *
     * @param strInitialValue  initial value with expression.
     * @param strExpectedValue expected rendered string.
     */
    @ParameterizedTest(name = "#{index} - initial value={0}, expected value={1}")
    @MethodSource("getRenderStringData")
    public void testRenderAttributeMap(final String strInitialValue,
                                       final String strExpectedValue) {
        final var strKey1 = UUID.randomUUID().toString();
        final var strKey2 = UUID.randomUUID().toString();
        final var strKey3 = UUID.randomUUID().toString();
        final var strKey4 = UUID.randomUUID().toString();

        final var attributeMap = AttributeMap.of(
                Pair.of(strKey1, StringValue.of(strInitialValue)),
                Pair.of(strKey2, ListOfValues.of(StringValue.of(strInitialValue))),
                Pair.of(strKey3, AttributeMap.of(
                        Pair.of(strKey4, StringValue.of(strInitialValue)))));

        attributeMap.accept(new StringRendererVisitor(getDefaultTaskParameters()));
        assertEquals(strExpectedValue, attributeMap.getString(strKey1));
        assertEquals(strExpectedValue, attributeMap.getList(strKey2).get(0).toString());
        assertEquals(strExpectedValue, attributeMap.getMap(strKey3).getString(strKey4));
    }

    /**
     * Providing test data.
     *
     * @return test data.
     */
    private static Stream<Arguments> getRenderStringData() {
        return Stream.of(
                Arguments.of("{{ variables.test.value }}", VARIABLE_VALUE),
                Arguments.of("{{ model.attributes.test }}", MODEL_VALUE),
                Arguments.of("{{ model.attributes.test.value }}", MODEL_VALUE),
                Arguments.of("{{ matrix.test }}", MATRIX_VALUE),
                Arguments.of("{{ with.value }}", WITH_VALUE),
                Arguments.of("{{ with.index }}", String.valueOf(WITH_INDEX))
        );
    }

    /**
     * Provide a simple setup for some concrete task parameters.
     *
     * @return simple task parameters.
     */
    private static TaskParameters getDefaultTaskParameters() {
        final var variable = new Variable();
        variable.setValue(VARIABLE_VALUE);

        return TaskParameters.of(
                Model.of(Pair.of(KEY, StringValue.of(MODEL_VALUE))),
                Map.of(KEY, MATRIX_VALUE),
                Map.of(KEY, variable),
                WithParameters.of(WITH_INDEX, StringValue.of(WITH_VALUE)));
    }
}


