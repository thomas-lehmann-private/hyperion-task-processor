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
package magic.system.hyperion.command;

import magic.system.hyperion.cli.CliCommand;
import magic.system.hyperion.cli.CliException;
import magic.system.hyperion.cli.CliOptionList;
import magic.system.hyperion.cli.CliResult;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Printing all third party dependencies to console.
 *
 * @author Thomas Lehmann
 */
public final class ThirdPartyCommandProcessor extends AbstractCommandProcessor {
    /**
     * Initialize with defined options and commands with its options and the parsed one.
     *
     * @param initGlobalOptions defined global options.
     * @param initCommands      defined commands with its options.
     * @param initParsedResult  parsed options and command given by the user.
     * @since 1.0.0
     */
    public ThirdPartyCommandProcessor(final CliOptionList initGlobalOptions,
                                      final List<CliCommand> initCommands,
                                      final CliResult initParsedResult) {
        super(initGlobalOptions, initCommands, initParsedResult);
    }

    @Override
    public void processCommand() throws CliException {
        try (var stream = getClass().getResourceAsStream("/dependencies.txt")) {
            final List<String> lines = List.of(new String(
                    stream.readAllBytes(), Charset.defaultCharset()).split("\n"));

            final var logger = LoggerFactory.getLogger("NO-TIMESTAMP");

            //CHECKSTYLE.OFF: MagicNumber: ok here
            for (var strLine : lines) {
                final var tokens = strLine.split(":");
                if (tokens.length >= 4) {
                    final var strGroupId = tokens[0].trim();
                    final var strArtifactId = tokens[1].trim();

                    final int iPos = tokens[3].indexOf(" -- ");
                    if (iPos >= 0) {
                        final var strVersion = tokens[3].substring(0, iPos).trim();
                        logger.info(String.format("group id: %s, artifact id: %s, version: %s",
                                strGroupId, strArtifactId, strVersion));
                    }
                }
            }
            //CHECKSTYLE.ON: MagicNumber:
        } catch (IOException e) {
            throw new CliException(e.getMessage());
        }
    }
}
