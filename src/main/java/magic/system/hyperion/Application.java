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
package magic.system.hyperion;

import magic.system.hyperion.cli.CliCommand;
import magic.system.hyperion.cli.CliException;
import magic.system.hyperion.cli.CliHelpPrinter;
import magic.system.hyperion.cli.CliOptionList;
import magic.system.hyperion.cli.CliParser;
import magic.system.hyperion.command.RunCommandProcessor;
import magic.system.hyperion.command.ServeCommandProcessor;
import magic.system.hyperion.command.ThirdPartyCommandProcessor;
import magic.system.hyperion.tools.Capabilities;
import magic.system.hyperion.tools.CapabilitiesPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Hyperion - the special task processing pipeline - the application.
 *
 * @author Thomas Lehmann
 */
public final class Application {
    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    /**
     * Logger name without timestamps.
     */
    private static final String NO_TIMESTAMP = "NO-TIMESTAMP";

    /**
     * Application properties (wrapper for application.properties).
     */
    private ApplicationProperties properties;

    /**
     * Defined global options for application.
     */
    private CliOptionList globalOptions;

    /**
     * Defined list of commands for application.
     */
    private List<CliCommand> commands;

    /**
     * Initialize application.
     */
    private Application() {
        // Nothing to do for the moment
    }

    /**
     * Running the application.
     *
     * @param args provided command line arguments.
     * @throws CliException when validation or the process has failed.
     */
    private void run(final String[] args) throws CliException {
        this.properties = new ApplicationProperties();
        this.globalOptions = ApplicationOptionsFunctions.defineGlobalOptions();
        this.commands = ApplicationOptionsFunctions.defineCommands();

        final var parser = CliParser.builder()
                .setGlobalOptions(this.globalOptions).setCommands(this.commands).build();
        final var result = parser.parse(args);

        if (result.getGlobalOptions().containsKey(ApplicationOptions.HELP.getLongName())) {
            printHelp();
        } else if (result.getCommandName().equals(ApplicationCommands.THIRD_PARTY.getCommand())) {
            new ThirdPartyCommandProcessor(
                    this.globalOptions, this.commands, result).processCommand();
        } else if (result.getCommandName().equals(ApplicationCommands.RUN.getCommand())) {
            logEnvironment();
            new RunCommandProcessor(this.globalOptions, this.commands, result).processCommand();
        } else if (result.getCommandName().equals(ApplicationCommands.CAPABILITIES.getCommand())) {
            final var printer = new CapabilitiesPrinter();
            printer.setGroovyVersion(this.properties.getGroovyVersion());
            printer.print(LoggerFactory.getLogger(NO_TIMESTAMP)::info);
        } else if (result.getCommandName().equals(ApplicationCommands.SERVE.getCommand())) {
            logEnvironment();
            new ServeCommandProcessor(this.globalOptions, this.commands, result).processCommand();
        }
    }

    /**
     * Logging of environment.
     */
    private void logEnvironment() {
        final String strFormat = "{}: {} ({})";
        LOGGER.info(strFormat, "Hyperion Version",
                this.properties.getProductVersion(),
                "git commit=" + this.properties.getGitCommitId());
        LOGGER.info(strFormat, "Operating System", Capabilities.getOperatingSystemName(),
                "arch=" + Capabilities.getOperatingSystemArchitecture());
        LOGGER.info(strFormat, "Host Name", Capabilities.getHostName(),
                "address=" + Capabilities.getHostAddress());
        LOGGER.info(strFormat, "Java", Capabilities.getJavaVersion(),
                "java class=" + Capabilities.getJavaClassVersion());
    }

    /**
     * Print the help.
     *
     * @throws CliException when validation has failed.
     */
    private void printHelp() throws CliException {
        final var helpPrinter = CliHelpPrinter.builder()
                .setExecution("java -jar "
                        + this.properties.getFinalName() + ".jar")
                .setProductVersion(this.properties.getProductVersion())
                .setBuildTimestamp(this.properties.getBuildTimestamp())
                .setAuthor(this.properties.getAuthor())
                .setGlobalOptions(this.globalOptions)
                .setCommands(this.commands)
                .build();
        helpPrinter.print(LoggerFactory.getLogger(NO_TIMESTAMP)::info);
    }

    /**
     * Initialize the application.
     *
     * @param args the command line arguments
     */
    public static void main(final String[] args) {
        final var application = new Application();
        try {
            application.run(args);
        } catch (CliException e) {
            LOGGER.error(e.getMessage(), e);
            System.exit(1);
        }
    }
}
