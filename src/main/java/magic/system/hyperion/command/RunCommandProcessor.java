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

import magic.system.hyperion.ApplicationOptions;
import magic.system.hyperion.cli.CliCommand;
import magic.system.hyperion.cli.CliException;
import magic.system.hyperion.cli.CliOptionList;
import magic.system.hyperion.cli.CliResult;
import magic.system.hyperion.components.DocumentParameters;
import magic.system.hyperion.reader.DocumentReader;
import magic.system.hyperion.tools.FileUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

/**
 * Implementing the run command (processing a document with tasks).
 *
 * @author Thomas Lehmann
 */
public final class RunCommandProcessor extends AbstractCommandProcessor {
    /**
     * Initialize with defined options and commands with its options and the parsed one.
     *
     * @param initGlobalOptions defined global options.
     * @param initCommands      defined commands with its options.
     * @param initParsedResult  parsed options and command given by the user.
     * @since 1.0.0
     */
    public RunCommandProcessor(final CliOptionList initGlobalOptions,
                               final List<CliCommand> initCommands,
                               final CliResult initParsedResult) {
        super(initGlobalOptions, initCommands, initParsedResult);
    }

    @Override
    public void processCommand() throws CliException {
        handleTemporaryPathOption();

        final List<String> tags = this.parsedResult.getGlobalOptions().getOrDefault(
                ApplicationOptions.TAG.getLongName(), Collections.emptyList());
        final Path pathDocument = Paths.get(this.parsedResult.getCommandOptions().get(
                ApplicationOptions.RUN_FILE.getLongName()).get(0));
        final int iTimeoutTaskGroup = Integer.parseInt(
                this.parsedResult.getGlobalOptions().getOrDefault(
                        ApplicationOptions.TIMEOUT_TASKGROUP.getLongName(),
                        List.of(this.globalOptions.findOption(
                                ApplicationOptions.TIMEOUT_TASKGROUP.getLongName())
                                .get().getDefault())).get(0));

        processDocument(pathDocument, DocumentParameters.of(tags, iTimeoutTaskGroup));
    }

    /**
     * Processing one YAML document for given path.
     *
     * @param path       path and filename of document.
     * @param parameters the document parameters.
     */
    private void processDocument(final Path path, final DocumentParameters parameters) {
        final var reader = new DocumentReader();
        final var document = reader.read(path);
        document.run(parameters);
    }

    /**
     * When the temporary path option is defined and the path does exist and is directory
     * then it is set.
     *
     * @throws CliException when path does not exit or is not a directory.
     */
    private void handleTemporaryPathOption() throws CliException {
        // evaluating temporary path option
        final var temporaryPaths = this.parsedResult.getCommandOptions().getOrDefault(
                ApplicationOptions.TEMPORARY_PATH.getLongName(), Collections.emptyList());
        if (!temporaryPaths.isEmpty()) {
            final var temporaryPath = Paths.get(temporaryPaths.get(0));
            if (Files.exists(temporaryPath) && Files.isDirectory(temporaryPath)) {
                FileUtils.setTemporaryPath(temporaryPath);
            } else {
                throw new CliException("Temporary path does not exist or is not a directory!");
            }
        }
    }
}
