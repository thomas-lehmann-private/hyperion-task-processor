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
package magic.system.hyperion.cli;

import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Testing of class {@link Option}.
 *
 * @author Thomas Lehmann
 */
@DisplayName("Testing of Option class")
public class OptionTest {

    /**
     * Testing each field set.
     */
    @Test
    public void testSetEachField() throws CliException {
        //CHECKSTYLE.OFF: MultipleStringLiterals - ok here.
        final var option = Option.builder().setLongName("file").setShortName("f").setRequired(
                true).setRepeatable(
                        true).setDescription("path of document to process").build();
        assertEquals("file", option.getLongName());
        assertEquals("f", option.getShortName());
        assertEquals("path of document to process", option.getDescription());
        assertEquals(true, option.isRequired());
        assertEquals(true, option.isRepeatable());
        //CHECKSTYLE.ON: MultipleStringLiterals
    }

    /**
     * Testing validation of the long name.
     *
     * @param strValue value for long name.
     * @param bExpectedToFail when true then expected to fail.
     */
    @ParameterizedTest(name = "test long name - #{index} value={0}, expected to fail={1}")
    @MethodSource("provideLongNameValidationTestData")
    public void testLongNameValidation(final String strValue,
            final boolean bExpectedToFail) {
        final var builder = Option.builder();
        builder.setShortName("x");
        builder.setDescription("test");
        builder.setLongName(strValue);

        if (bExpectedToFail) {
            assertThrows(CliException.class, () -> builder.build());
        } else {
            assertDoesNotThrow(() -> builder.build());
        }
    }

    /**
     * Test data for long name validation.
     *
     * @return test data.
     */
    private static Stream<Arguments> provideLongNameValidationTestData() {
        //CHECKSTYLE.OFF: MultipleStringLiterals - ok here.
        return Stream.of(
                Arguments.of("FOO", true),
                Arguments.of("FOOFOOFOOFOOFOOFOO", true),
                Arguments.of("FOOFOOFOOFOOFOO", true),
                Arguments.of("foofoofoofoofoo", false),
                Arguments.of("fo", false),
                Arguments.of("f", true),
                Arguments.of("f-f", true),
                Arguments.of("f-fo", true),
                Arguments.of("fo-f", true),
                Arguments.of("fo-fo", false),
                Arguments.of("foofoofooo-foofoofooo", false),
                Arguments.of("foofoofoofo-foofoofoof", true),
                Arguments.of("foofoofoof-foofoofoofo", true)
        );
        //CHECKSTYLE.ON: MultipleStringLiterals
    }
}
