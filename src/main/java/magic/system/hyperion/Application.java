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
import magic.system.hyperion.cli.CliOption;
import magic.system.hyperion.cli.CliOptionList;
import magic.system.hyperion.cli.CliParser;
import magic.system.hyperion.cli.OptionType;
import magic.system.hyperion.reader.DocumentReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

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
     * The key for final name of the jar (without extension).
     */
    private static final String PROPERTY_FINAL_NAME = "finalName";

    /**
     * The key for the product version (see pom.xml).
     */
    private static final String PROPERTY_PRODUCT_VERSION = "productVersion";

    /**
     * They key for the build timestamp.
     */
    private static final String PROPERTY_BUILD_TIMESTAMP = "buildTimestamp";

    /**
     * They key for the architect and developer of the project.
     */
    private static final String PROPERTY_AUTHOR = "author";

    /**
     * (G)lobal (o)ption (l)ong (n)ame for help.
     */
    private static final String GOLN_HELP = "help";

    /**
     * (G)lobal (o)ption (s)ong (n)ame for help.
     */
    private static final String GOSN_HELP = "h";

    /**
     * (G)lobal (o)ption (l)ong (n)ame for 3rd-party.
     */
    private static final String GOLN_3RD_PARTY = "third-party";

    /**
     * (G)lobal (o)ption (l)ong (n)ame for tag.
     */
    private static final String GOLN_TAG = "tag";

    /**
     * (G)lobal (o)ption (s)ong (n)ame for tag.
     */
    private static final String GOSN_TAG = "t";

    /**
     * Command run.
     */
    private static final String COMMAND_RUN = "run";

    /**
     * (C)ommand (o)ption (l)ong (n)ame for file.
     */
    private static final String COLN_FILE = "file";

    /**
     * Escape character.
     */
    private static final int ESCAPE = 27;

    /**
     * Application properties.
     */
    private Properties properties;

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
    void run(final String[] args) throws CliException {
        this.properties = getApplicationProperties();
        this.globalOptions = defineGlobalOptions();
        this.commands = defineCommands();

        final var parser = CliParser.builder()
                .setGlobalOptions(this.globalOptions).setCommands(this.commands).build();
        final var result = parser.parse(args);

        if (result.getGlobalOptions().containsKey(GOLN_HELP)) {
            printHelp();
        } else if (result.getGlobalOptions().containsKey(GOLN_3RD_PARTY)) {
            print3rdParty();
        } else if (result.getCommandName().equals(COMMAND_RUN)) {
            final List<String> tags = result.getGlobalOptions().containsKey(GOLN_TAG)
                    ? result.getGlobalOptions().get(GOLN_TAG): Collections.emptyList();
            processDocument(Paths.get(result.getCommandOptions().get(COLN_FILE).get(0)), tags);
        }
    }

    /**
     * Processing document.
     *
     * @param path path and filename of document.
     * @param tags the tags to be used to filter tasks.
     */
    private void processDocument(final Path path, final List<String> tags) {
        final var reader = new DocumentReader(path);
        final var document = reader.read();
        document.run(tags);
    }

    /**
     * Print the help.
     *
     * @throws CliException when validation has failed.
     */
    private void printHelp() throws CliException {
        final var helpPrinter = CliHelpPrinter.builder()
                .setExecution("java -jar "
                        + this.properties.getProperty(PROPERTY_FINAL_NAME) + ".jar")
                .setProductVersion(this.properties.getProperty(PROPERTY_PRODUCT_VERSION))
                .setBuildTimestamp(this.properties.getProperty(PROPERTY_BUILD_TIMESTAMP))
                .setAuthor(this.properties.getProperty(PROPERTY_AUTHOR))
                .setGlobalOptions(this.globalOptions)
                .setCommands(this.commands)
                .build();
        helpPrinter.print(LoggerFactory.getLogger("HELP")::info);
    }

    /**
     * Displaying 3rd party information.
     *
     * @throws CliException when reading of the dependencies has failed.
     */
    private void print3rdParty() throws CliException {
        try (var stream = getClass().getResourceAsStream("/dependencies.txt")) {
            final List<String> lines = List.of(new String(
                    stream.readAllBytes(), Charset.defaultCharset()).split("\n"));

            //CHECKSTYLE.OFF: MagicNumber: ok here
            for (var strLine: lines) {
                final var tokens = strLine.split(":");
                if (tokens.length >= 4) {
                    final var strGroupId = tokens[0].trim();
                    final var strArtifactId = tokens[1].trim();

                    int iPos = -1;
                    final var originalBytes = tokens[3].getBytes(Charset.defaultCharset());
                    for (int ix = 0; ix < originalBytes.length; ++ix) {
                        if (originalBytes[ix] == ESCAPE) {
                            iPos = ix;
                            break;
                        }
                    }

                    if (iPos >= 0) {
                        final var strVersion = tokens[3].substring(0, iPos).trim();
                        final var logger = LoggerFactory.getLogger("3RDPARTY");
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

    /**
     * Initialize the application.
     *
     * @param args the command line arguments
     */
    public static void main(final String[] args)  {
        final var application = new Application();
        try {
            application.run(args);
        } catch (CliException e) {
            LOGGER.error(e.getMessage(), e);
            System.exit(1);
        }
    }

    /**
     * Define the global options.
     *
     * @return list of global options
     * @throws CliException when validation of the definition has failed.
     */
    private static CliOptionList defineGlobalOptions() throws CliException {
        return CliOptionList.builder().add(
                CliOption.builder()
                        .setShortName(GOSN_HELP)
                        .setLongName(GOLN_HELP)
                        .setDescription("displaying this help")
                        .setType(OptionType.BOOLEAN)
                        .build()).add(
                CliOption.builder()
                        .setLongName(GOLN_3RD_PARTY)
                        .setDescription("displaying used 3rd party libraries")
                        .setType(OptionType.BOOLEAN)
                        .build()).add(
                CliOption.builder()
                        .setShortName(GOSN_TAG)
                        .setLongName(GOLN_TAG)
                        .setDescription("provide tag to filter tasks")
                        .setType(OptionType.STRING)
                        .setRepeatable(true)
                        .build()
        ).build();
    }

    /**
     * Define the list of commands.
     *
     * @return list of commands.
     * @throws CliException when validation of the definition has failed.
     */
    private static List<CliCommand> defineCommands() throws CliException {
        return List.of(CliCommand.builder()
                .setName(COMMAND_RUN)
                .setDescription("Running one document with tasks to be processed")
                .addOption(CliOption.builder()
                        .setShortName("f")
                        .setLongName(COLN_FILE)
                        .setDescription("Document with tasks to be processed")
                        .setRequired(true)
                        .setType(OptionType.PATH)
                        .build())
                .build()
        );
    }

    /**
     * Load and provide the application properties.
     *
     * @return application properties.
     */
    private static Properties getApplicationProperties() {
        final var properties = new Properties();

        try (var stream = Application.class.getResourceAsStream("/application.properties")) {
            properties.load(stream);
        } catch (IOException e) {
            // should never happen (the file should always be in the jar too)
            LOGGER.error(e.getMessage(), e);
        }

        return properties;
    }
}
