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
import magic.system.hyperion.server.IServer;
import magic.system.hyperion.server.creator.IServerCreator;
import magic.system.hyperion.tools.Factory;

import java.util.List;

/**
 * Running the serve command.
 *
 * @author Thomas Lehmann
 */
public class ServeCommandProcessor extends AbstractCommandProcessor {
    /**
     * Default port of server application.
     */
    public static final int DEFAULT_PORT = 8000;

    /**
     * Initialize with defined options and commands with its options and the parsed one.
     *
     * @param initGlobalOptions defined global options.
     * @param initCommands      defined commands with its options.
     * @param initParsedResult  parsed options and command given by the user.
     * @since 1.0.0
     */
    public ServeCommandProcessor(CliOptionList initGlobalOptions, List<CliCommand> initCommands,
                                 CliResult initParsedResult) {
        super(initGlobalOptions, initCommands, initParsedResult);
    }

    @Override
    public void processCommand() throws CliException {
        final var serveCommand = this.commands.stream().filter(
                command -> command.getName().equals(this.parsedResult.getCommandName())).findAny();

        int iDefaultPort = DEFAULT_PORT;

        if (serveCommand.isPresent()) {
            final var portOption = serveCommand.get()
                    .findOption(ApplicationOptions.PORT.getLongName());
            if (portOption.isPresent()) {
                iDefaultPort = Integer.parseInt(portOption.get().getDefault());
            }
        }

        final int iPort = Integer.parseInt(
                this.parsedResult.getCommandOptions().getOrDefault(
                        ApplicationOptions.PORT.getLongName(),
                        List.of(String.valueOf(iDefaultPort))).get(0));

        final var server = new Factory<IServer>(IServerCreator.class).create("default");
        server.start(iPort);
    }
}
