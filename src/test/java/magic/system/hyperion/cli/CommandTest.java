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

import java.util.List;
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
 * Testing of class {@link Command}.
 *
 * @author Thomas Lehmann
 */
@DisplayName("Testing Command class")
public class CommandTest {

    /**
     * Testing default usage.
     *
     * @throws CliException when a command/option is not set correctly.
     */
    @Test
    public void testDefaultUsage() throws CliException {
        //CHECKSTYLE.OFF: MultipleStringLiterals - ok here.
        final var command = Command.builder().setName("run")
                .addOption(Option.builder()
                        .setLongName("file")
                        .setShortName("f")
                        .setDescription("document to process")
                        .setRequired(true)
                        .setRepeatable(true)
                        .setType(OptionType.PATH)
                        .build())
                .addOption(Option.builder()
                        .setLongName("help")
                        .setShortName("h")
                        .setDescription("this help")
                        .build())
                .build();

        assertEquals("run", command.getName());
        assertEquals(2, command.getOptions().size());
        // just a probe (the other details are tested via another test class).
        assertEquals("file", command.getOptions().get(0).getLongName());
        assertEquals("help", command.getOptions().get(1).getLongName());
        //CHECKSTYLE.ON: MultipleStringLiterals
    }

    /**
     * Testing for duplicate long names.
     *
     * @throws CliException when the option build has failed (should not happen here).
     */
    @Test
    public void testDuplicateOptionLongName() throws CliException {
        final var builder = Command.builder();
        //CHECKSTYLE.OFF: MultipleStringLiterals - ok here.
        builder.setName("run");
        for (int ix = 0; ix < 2; ++ix) {
            builder.addOption(Option.builder()
                    .setLongName("file")
                    .setShortName(List.of("a", "b").get(ix))
                    .setDescription("document to process " + ix).build());
        }
        //CHECKSTYLE.ON: MultipleStringLiterals

        final var throwable = assertThrows(CliException.class, builder::build);
        assertEquals(CliMessages.OPTION_LONG_NAME_MORE_THAN_ONCE.getMessage(),
                throwable.getMessage());
    }

    /**
     * Testing for duplicate short names.
     *
     * @param bExpectedToFail when true then expected to fail.
     * @param shortNames list of short names to use for test.
     * @throws CliException when the option build has failed (should not happen here).
     */
    @ParameterizedTest(name = "option short names - #{index}: expected fail={0}, data={1}")
    @MethodSource("provideOptionShortNamesTestData")
    public void testDuplicateOptionShortName(final boolean bExpectedToFail,
                                             final List<String> shortNames) throws CliException {
        final var builder = Command.builder();
        //CHECKSTYLE.OFF: MultipleStringLiterals - ok here.
        builder.setName("run");
        for (int ix = 0; ix < shortNames.size(); ++ix) {
            builder.addOption(Option.builder()
                    .setLongName("file" + ix)
                    .setShortName(shortNames.get(ix).equals("null")? null: shortNames.get(ix))
                    .setDescription("document to process " + ix).build());
        }
        //CHECKSTYLE.ON: MultipleStringLiterals

        if (bExpectedToFail) {
            final var throwable = assertThrows(CliException.class, builder::build);
            assertEquals(CliMessages.OPTION_SHORT_NAME_MORE_THAN_ONCE.getMessage(),
                    throwable.getMessage());
        } else {
            assertDoesNotThrow(builder::build);
        }
    }

    /**
     * Testing validation for duplication descriptions.
     *
     * @throws CliException when the option build has failed (should not happen here).
     */
    @Test
    public void testDuplicateOptionDescription() throws CliException {
        final var builder = Command.builder();
        //CHECKSTYLE.OFF: MultipleStringLiterals - ok here.
        builder.setName("run");
        for (int ix = 0; ix < 2; ++ix) {
            builder.addOption(Option.builder()
                    .setLongName("file" + ix)
                    .setShortName(List.of("a", "b").get(ix))
                    .setDescription("document to process ").build());
        }
        //CHECKSTYLE.ON: MultipleStringLiterals

        final var throwable = assertThrows(CliException.class, builder::build);
        assertEquals(CliMessages.OPTION_DESCRIPTION_MORE_THAN_ONCE.getMessage(),
                throwable.getMessage());
    }

    /**
     * Testing command name to work correctly.
     *
     * @param strName name of the command.
     * @param bExpectedToFail when true then expected to fail.
     */
    @ParameterizedTest(name = "test invalid name - #{index}: name={0}")
    @MethodSource("provideCommandNameTestData")
    public void testCommandName(final String strName, final boolean bExpectedToFail) {
        if (bExpectedToFail) {
            assertThrows(CliException.class,
                    () -> Command.builder().setName(strName).build());
        } else {
            assertDoesNotThrow(() -> Command.builder().setName(strName).build());
        }
    }

    /**
     * Provide list of test data for command names.
     *
     * @return list of test data.
     */
    @SuppressWarnings("unused")
    private static Stream<Arguments> provideCommandNameTestData() {
        return Stream.of(
                Arguments.of("run", false),
                Arguments.of("ru", false),
                Arguments.of("r", true),
                Arguments.of("RUN", true),
                Arguments.of("runrunrunr", false),
                Arguments.of("runrunrunru", true)
        );
    }

    /**
     * Provide list of test data for checking duplicate option short names.
     *
     * @return list of test data.
     */
    @SuppressWarnings("unused")
    private static Stream<Arguments> provideOptionShortNamesTestData() {
        //CHECKSTYLE.OFF: MultipleStringLiterals - ok here.
        return Stream.of(
                Arguments.of(false, List.of("a", "b", "c", "")),
                Arguments.of(false, List.of("a", "b", "c", "null")),
                Arguments.of(false, List.of("a", "b", "", "c", "null", "d")),
                Arguments.of(true, List.of("a", "b", "", "c", "null", "d", "a"))
        );
        //CHECKSTYLE.ON: MultipleStringLiterals
    }
}
