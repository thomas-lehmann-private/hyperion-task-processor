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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Testing of class {@link CliParser}.
 */
@DisplayName("Testing CliParser class")
public class CliParserTest {
    /**
     * Testing global options.
     *
     * @param bExpectedToFail when true then expected to fail
     * @param arguments       list of arguments representing what a use might specify
     * @param globalOptions   list of allowed global options
     * @param expectedValues  expected values
     */
    @ParameterizedTest(name = "#{index}: expected fail: {0}, arguments: {1}, options: {2}")
    @MethodSource("provideGlobalOptionsOnlyTestData")
    public void testGlobalOptionsOnly(final boolean bExpectedToFail, final String[] arguments,
                                      final CliOptionList globalOptions,
                                      final List<List<String>> expectedValues) throws CliException {
        final var parser = CliParser.builder()
                .setGlobalOptions(globalOptions)
                .build();

        if (bExpectedToFail) {
            assertThrows(CliException.class, () -> parser.parse(arguments));
        } else {
            final var result = assertDoesNotThrow(() -> parser.parse(arguments));
            assertEquals(expectedValues.toString(), result.getGlobalOptions().values().toString());
        }
    }

    /**
     * Testing command options.
     *
     * @param bExpectedToFail when true then expected to fail
     * @param arguments       list of arguments representing what a use might specify
     * @param commands        list of allowed commands
     * @param expectedValues  expected values
     */
    @ParameterizedTest(name = "#{index}: expected fail: {0}, arguments: {1}, options: {2}")
    @MethodSource("provideCommandOptionsOnlyTestData")
    public void testCommandOptionsOnly(final boolean bExpectedToFail, final String[] arguments,
                             final List<CliCommand> commands,
                             final List<List<String>> expectedValues) throws CliException {
        final var builder = CliParser.builder();
        commands.forEach(builder::addCommand);
        final var parser = builder.build();

        if (bExpectedToFail) {
            assertThrows(CliException.class, () -> parser.parse(arguments));
        } else {
            final var result = assertDoesNotThrow(() -> parser.parse(arguments));
            assertEquals(expectedValues.toString(), result.getCommandOptions().values().toString());
        }
    }

    /**
     * Provide test data for
     * {@link CliParserTest#testGlobalOptionsOnly(boolean, String[], CliOptionList, List)}.
     *
     * @return test data.
     * @throws CliException when validation of options has failed.
     */
    @SuppressWarnings("unused")
    private static Stream<Arguments> provideGlobalOptionsOnlyTestData() throws CliException {
        //CHECKSTYLE.OFF: MultipleStringLiterals: ok here.
        return Stream.of(
                // normal option which is boolean (so no value is defined)
                Arguments.of(false, List.of("--help").toArray(String[]::new),
                        CliOptionList.builder().add(CliOption.builder()
                                .setType(OptionType.BOOLEAN).setShortName("h")
                                .setLongName("help").setDescription("help").build()).build(),
                        List.of(List.of("true"))),
                // option is not known
                Arguments.of(true, List.of("--unknown").toArray(String[]::new),
                        CliOptionList.builder().add(CliOption.builder()
                                .setType(OptionType.BOOLEAN).setShortName("h")
                                .setLongName("help").setDescription("help").build()).build(),
                        List.of()),
                // missing option because one has been defined as required
                Arguments.of(true, new String[0], CliOptionList.builder()
                                .add(CliOption.builder()
                                        .setShortName("m").setLongName("mode").setRequired(true)
                                        .setDescription("mode").build()).build(),
                        List.of()),
                // option with assignment as one parameter
                Arguments.of(false, List.of("--repeat=3").toArray(String[]::new),
                        CliOptionList.builder().add(CliOption.builder()
                                .setType(OptionType.INTEGER).setShortName("r")
                                .setLongName("repeat")
                                .setDescription("repeat").build()).build(),
                        List.of(List.of("3"))),
                // option without assignment using two arguments
                Arguments.of(false, List.of("--repeat", "3").toArray(String[]::new),
                        CliOptionList.builder().add(CliOption.builder()
                                .setType(OptionType.INTEGER).setShortName("r")
                                .setLongName("repeat")
                                .setDescription("repeat").build()).build(),
                        List.of(List.of("3"))),
                // option (by default) is configured as not repeatable
                Arguments.of(true, List.of("--repeat=3", "--repeat=3").toArray(String[]::new),
                        CliOptionList.builder().add(CliOption.builder()
                                .setType(OptionType.INTEGER)
                                .setLongName("repeat")
                                .setDescription("repeat").build()).build(),
                        List.of()),
                // option is configured as repeatable
                Arguments.of(false, List.of("--value=3", "--value=4").toArray(String[]::new),
                        CliOptionList.builder().add(CliOption.builder()
                                .setType(OptionType.INTEGER)
                                .setRepeatable(true)
                                .setLongName("value")
                                .setDescription("value").build()).build(),
                        List.of(List.of("3", "4"))),
                // option is configured as repeatable; main focus are the short options
                Arguments.of(false, List.of("-v", "3", "--value=4", "-v5").toArray(String[]::new),
                        CliOptionList.builder().add(CliOption.builder()
                                .setType(OptionType.INTEGER)
                                .setRepeatable(true)
                                .setLongName("value").setShortName("v")
                                .setDescription("value").build()).build(),
                        List.of(List.of("3", "4", "5")))
        );
        //CHECKSTYLE.ON: MultipleStringLiterals
    }

    /**
     * Provide test data for testing commands.
     *
     * @return list of test data for
     *         {@link CliParserTest#testCommandOptionsOnly(boolean, String[], List, List)}.
     * @throws CliException when validation has failed.
     */
    @SuppressWarnings("unused")
    private static Stream<Arguments> provideCommandOptionsOnlyTestData() throws CliException {
        //CHECKSTYLE.OFF: MultipleStringLiterals: ok here.
        return Stream.of(
                // just a command - no options
                Arguments.of(false, List.of("run").toArray(String[]::new),
                        List.of(CliCommand.builder().setName("run").build()),
                        List.of()),
                // a command and one boolean option
                Arguments.of(false, List.of("run", "--help").toArray(String[]::new),
                        List.of(CliCommand.builder()
                                .setName("run")
                                .addOption(CliOption.builder()
                                        .setLongName("help")
                                        .setDescription("help")
                                        .setType(OptionType.BOOLEAN).build())
                                .build()),
                        List.of(List.of("true"))),
                // a command and one unknown option
                Arguments.of(true, List.of("run", "--unknown").toArray(String[]::new),
                        List.of(CliCommand.builder()
                                .setName("run")
                                .addOption(CliOption.builder()
                                        .setLongName("help")
                                        .setDescription("help")
                                        .setType(OptionType.BOOLEAN).build())
                                .build()),
                        List.of()),
                // a command with one option with assignment
                Arguments.of(false, List.of("run", "--retry=3").toArray(String[]::new),
                        List.of(CliCommand.builder()
                                .setName("run")
                                .addOption(CliOption.builder()
                                        .setLongName("retry")
                                        .setDescription("retry")
                                        .setType(OptionType.INTEGER).build())
                                .build()),
                        List.of(List.of("3"))),
                // a command with short options
                Arguments.of(false, List.of(
                        "run", "--tag=abc", "-tdef", "-t", "ghi").toArray(String[]::new),
                        List.of(CliCommand.builder()
                                .setName("run")
                                .addOption(CliOption.builder()
                                        .setLongName("tag").setShortName("t")
                                        .setDescription("tag")
                                        .setType(OptionType.INTEGER).build())
                                .build()),
                        List.of(List.of("abc", "def", "ghi")))
        );
        //CHECKSTYLE.ON: MultipleStringLiterals
    }
}
