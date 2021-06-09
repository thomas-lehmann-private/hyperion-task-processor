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
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Testing of class {@link CliHelpPrinter}.
 */
@DisplayName("Testing class CliHelpPrinter")
@TestMethodOrder(value = MethodOrderer.Random.class)
public class CliHelpPrinterTest {
    /**
     * Testing of header part for help.
     *
     * @param bExpectedToFail when true then expected to fail.
     * @param strExecution string that explains how to call the application.
     * @param strVersion the version of the application.
     * @param strBuildTimestamp the build timestamp when the application were built.
     * @param strAuthor the author of the application.
     * @param expectedLines the expected help output (lines).
     */
    @ParameterizedTest(name="#{index} - expected fail: {0}, data: {1}, {2}, {3}, {4}, {5}")
    @MethodSource("provideHeaderOnlyTestData")
    public void testHeaderOnly(final boolean bExpectedToFail, final String strExecution,
                               final String strVersion, final String strBuildTimestamp,
                               final String strAuthor, final List<String> expectedLines) {
        final var builder = CliHelpPrinter.builder()
                .setExecution(strExecution)
                .setProductVersion(strVersion)
                .setAuthor(strAuthor)
                .setBuildTimestamp(strBuildTimestamp);

        if (bExpectedToFail) {
            assertThrows(CliException.class, builder::build);
        } else {
            final var helpPrinter = assertDoesNotThrow(builder::build);
            final List<String> lines = new ArrayList<>();
            helpPrinter.print(lines::add);
            assertEquals(expectedLines.toString(), lines.toString());
        }
    }

    /**
     * Testing global options.
     *
     * @throws CliException when validation has failed.
     */
    @Test
    public void testGlobalOptionsOnly() throws CliException {
        //CHECKSTYLE.OFF: MultipleStringLiterals: ok here
        final var globalOptions = CliOptionList.builder().add(
                CliOption.builder()
                        .setShortName("v").setLongName("verbose").setType(OptionType.INTEGER)
                        .setDescription("verbosity level").build()).
                add(CliOption.builder()
                        .setLongName("simulate").setType(OptionType.BOOLEAN)
                        .setDescription("simulate tasks only").build()).
                add(CliOption.builder()
                        .setShortName("t").setLongName("tag").setRepeatable(true)
                        .setDescription("filtering by this tag").build()).
                add(CliOption.builder()
                        .setShortName("f").setLongName("file")
                        .setRequired(true).setRepeatable(true).setType(OptionType.PATH)
                        .setDescription("document to process").build())
                .build();

        final var helpPrinter = CliHelpPrinter.builder()
                .setExecution("java -jar demo.jar")
                .setProductVersion("1.0.0")
                .setGlobalOptions(globalOptions).build();

        final List<String> lines = new ArrayList<>();
        helpPrinter.print(lines::add);

        int ix = lines.indexOf("Global options:") + 1;
        assertEquals("    -v<int>,  --verbose=<int> - verbosity level", lines.get(ix++));
        assertEquals("              --simulate      - simulate tasks only", lines.get(ix++));
        assertEquals("    -t<str>,  --tag=<str>     - filtering by this tag [repeatable]",
                lines.get(ix++));
        assertEquals("    -f<path>, --file=<path>   - document to process [required, repeatable]",
                lines.get(ix));
        //CHECKSTYLE.ON: MultipleStringLiterals
    }

    /**
     * Testing one command.
     *
     * @throws CliException when validation has failed.
     */
    @Test
    public void testOneCommandOnly() throws CliException {
        //CHECKSTYLE.OFF: MultipleStringLiterals: ok here
        final var options = List.of(
                CliOption.builder()
                        .setShortName("v").setLongName("verbose").setType(OptionType.INTEGER)
                        .setDescription("verbosity level").build(),
                CliOption.builder()
                        .setLongName("simulate").setType(OptionType.BOOLEAN)
                        .setDescription("simulate tasks only").build(),
                CliOption.builder()
                        .setShortName("t").setLongName("tag").setRepeatable(true)
                        .setDescription("filtering by this tag").build(),
                CliOption.builder()
                        .setShortName("f").setLongName("file")
                        .setRequired(true).setRepeatable(true).setType(OptionType.PATH)
                        .setDescription("document to process").build());

        final var command = CliCommand.builder()
                .setName("run").setDescription("run tasks")
                .addAllOptions(options).build();

        final var helpPrinter = CliHelpPrinter.builder()
                .setExecution("java -jar demo.jar")
                .setProductVersion("1.0.0")
                .addCommand(command).build();

        final List<String> lines = new ArrayList<>();
        helpPrinter.print(lines::add);

        int ix = lines.indexOf("List of available commands:") + 1;
        assertEquals("    run - run tasks", lines.get(ix++));
        assertEquals("", lines.get(ix++));
        assertEquals("Options for command 'run':", lines.get(ix++));
        assertEquals("    -v<int>,  --verbose=<int> - verbosity level", lines.get(ix++));
        assertEquals("              --simulate      - simulate tasks only", lines.get(ix++));
        assertEquals("    -t<str>,  --tag=<str>     - filtering by this tag [repeatable]",
                lines.get(ix++));
        assertEquals("    -f<path>, --file=<path>   - document to process [required, repeatable]",
                lines.get(ix));
        //CHECKSTYLE.ON: MultipleStringLiterals
    }

    /**
     * Provide test data for header only test.
     *
     * @return list of usecases.
     */
    private static Stream<Arguments> provideHeaderOnlyTestData() {
        // expected to fail, execution, version, timestamp, author
        //CHECKSTYLE.OFF: MultipleStringLiterals: ok here.
        return Stream.of(
                Arguments.of(false, "java -jar demo.jar", "1.0.0", null, null,
                        List.of("java -jar demo.jar [global options] [command [command options]]",
                                "    version: 1.0.0")),
                Arguments.of(true, "java -jar demo.jar", null, null, null, List.of()),
                Arguments.of(true, null, "1.0.0", null, null, List.of()),
                Arguments.of(false, "java -jar demo.jar", "1.0.0", null, "Some author",
                        List.of("java -jar demo.jar [global options] [command [command options]]",
                                "    version: 1.0.0", "    author: Some author")),
                Arguments.of(false, "java -jar demo.jar", "1.0.0", "some timestamp", "Some author",
                        List.of("java -jar demo.jar [global options] [command [command options]]",
                                "    version: 1.0.0, build timestamp: some timestamp",
                                "    author: Some author"))
        );
        //CHECKSTYLE.ON: MultipleStringLiterals
    }
}
