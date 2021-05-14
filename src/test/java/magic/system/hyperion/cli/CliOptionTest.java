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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Testing of class {@link CliOption}.
 *
 * @author Thomas Lehmann
 */
@DisplayName("Testing of CliOption class")
public class CliOptionTest {

    /**
     * Testing each field set.
     *
     * @throws CliException when any option is not correct.
     */
    @Test
    public void testSetEachField() throws CliException {
        //CHECKSTYLE.OFF: MultipleStringLiterals - ok here.
        final var option = CliOption.builder()
                .setLongName("file")
                .setShortName("f")
                .setRequired(true)
                .setRepeatable(true)
                .setDescription("path of document to process")
                .setType(OptionType.PATH)
                .build();

        assertEquals("file", option.getLongName());
        assertEquals("f", option.getShortName());
        assertEquals("path of document to process", option.getDescription());
        assertTrue(option.isRequired());
        assertTrue(option.isRepeatable());
        assertEquals(OptionType.PATH, option.getType());
        //CHECKSTYLE.ON: MultipleStringLiterals
    }

    /**
     * Testing for boolean option that cannot be repeated.
     */
    @Test
    public void testBooleanTypeCannotBeRepeated() {
        final var builder = CliOption.builder();
        builder
                .setLongName("validate")
                .setShortName("v")
                .setRequired(true)
                .setRepeatable(true)
                .setDescription("enable of validation")
                .setType(OptionType.BOOLEAN);

        final var throwable = assertThrows(CliException.class, builder::build);
        assertEquals(CliMessages.BOOLEAN_OPTION_NOT_REPEATABLE.getMessage(),
                throwable.getMessage());
    }

    /**
     * Testing validation of the long name.
     *
     * @param bExpectedToFail when true then expected to fail.
     * @param strValue value for long name.
     * @param strMessage hint for the case the assertion does fail.
     */
    @ParameterizedTest(
            name = "test long name - #{index} value={0}, expected to fail={1}, message={2}")
    @MethodSource("provideLongNameValidationTestData")
    public void testLongNameValidation(
            final boolean bExpectedToFail,
            final String strValue,
            final String strMessage) {
        // setup of builder
        final var builder = CliOption.builder();
        builder.setShortName("x");
        builder.setDescription("test");
        builder.setLongName(strValue);

        if (bExpectedToFail) {
            assertThrows(CliException.class, builder::build, strMessage);
        } else {
            assertDoesNotThrow(builder::build, strMessage);
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
                Arguments.of(true, null, "cannot be null"),
                Arguments.of(true, "FILE", "cannot be uppercase letters"),
                Arguments.of(true, "f", "too short"),
                Arguments.of(true, "abcdefghijklmnop", "too long"),
                Arguments.of(false, "abcdefghijklmno",
                        "maximum length for one subname"),
                Arguments.of(false, "config-file", "two valid sub names"),
                Arguments.of(false, "config-file-path", "three valid sub names"),
                Arguments.of(true, "the-config-file-path",
                        "limited to three sub names"),
                Arguments.of(true, "abcdefghijklmno-abcdefghijklmnop",
                        "second subname too long"),
                Arguments.of(true, "abcdefghijklmno-a",
                        "second subname too short"),
                Arguments.of(true,
                        "abcdefghijklmno-abcdefghijklmno-abcdefghijklmnop",
                        "third subname too long"),
                Arguments.of(true, "abcdefghijklmno-abcdefghijklmno-a",
                        "third subname too short"),
                Arguments.of(false, "http2", "allows number at the end"),
                Arguments.of(true, "2http", "name cannot start with numbers"),
                Arguments.of(false, "ht2tp", "allows numbers in name"),
                Arguments.of(true, "abc!?-_[](),;.",
                        "cannot be special characters")
        );
        //CHECKSTYLE.ON: MultipleStringLiterals
    }
}
