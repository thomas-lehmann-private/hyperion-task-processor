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

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testing of class {@link CliHelpPrinter}.
 */
public class CliHelpPrinterTest {
    /**
     * Testing global options.
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
                .setGlobalOptions(globalOptions).build();

        final List<String> lines = new ArrayList<>();
        helpPrinter.print(lines::add);

        int ix = 0;
        assertEquals("Global options:", lines.get(ix++));
        assertEquals("    -v<int>,  --verbose=<int> - verbosity level", lines.get(ix++));
        assertEquals("              --simulate      - simulate tasks only", lines.get(ix++));
        assertEquals("    -t<str>,  --tag=<str>     - filtering by this tag [repeatable]",
                lines.get(ix++));
        assertEquals("    -f<path>, --file=<path>   - document to process [required, repeatable]",
                lines.get(ix++));
        //CHECKSTYLE.ON: MultipleStringLiterals
    }

    /**
     * Testing one command.
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

        final var command = CliCommand.builder().setName("run")
                .addAllOptions(options).build();

        final var helpPrinter = CliHelpPrinter.builder()
                .addCommand(command).build();

        final List<String> lines = new ArrayList<>();
        helpPrinter.print(lines::add);

        int ix = 0;
        assertEquals("Options for command 'run':", lines.get(ix++));
        assertEquals("    -v<int>,  --verbose=<int> - verbosity level", lines.get(ix++));
        assertEquals("              --simulate      - simulate tasks only", lines.get(ix++));
        assertEquals("    -t<str>,  --tag=<str>     - filtering by this tag [repeatable]",
                lines.get(ix++));
        assertEquals("    -f<path>, --file=<path>   - document to process [required, repeatable]",
                lines.get(ix++));
        //CHECKSTYLE.ON: MultipleStringLiterals
    }
}
