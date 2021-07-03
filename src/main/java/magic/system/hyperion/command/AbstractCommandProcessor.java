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

import java.util.List;

/**
 * Command processor enable modularizing commands in the application.
 *
 * @author Thomas Lehmann
 */
public abstract class AbstractCommandProcessor {
    /**
     * Defined global options.
     */
    protected final CliOptionList globalOptions;

    /**
     * Defined commands with its options.
     */
    protected final List<CliCommand> commands;

    /**
     * Parsed options and command given by the user.
     */
    protected final CliResult parsedResult;

    /**
     * Initialize with defined options and commands with its options and the parsed one.
     *
     * @param initGlobalOptions defined global options.
     * @param initCommands defined commands with its options.
     * @param initParsedResult parsed options and command given by the user.
     * @since 1.0.0
     */
    public AbstractCommandProcessor(final CliOptionList initGlobalOptions,
                        final List<CliCommand> initCommands,
                        final CliResult initParsedResult) {
        this.globalOptions = initGlobalOptions;
        this.commands = initCommands;
        this.parsedResult = initParsedResult;

        // also that is really wanted (contract) it should never happen.
        // However it's to silence the spotbugs analyse about not read fields since not
        // every command process does require all fields.
        assert this.globalOptions != null;
        assert this.commands != null;
        assert this.parsedResult != null;
    }

    /**
     * Intention is to enforce implementation of the concrete command in the derived class.
     *
     * @throws CliException when processing of command has failed.
     * @since 1.0.0
     */
    public abstract void processCommand() throws CliException;
}
