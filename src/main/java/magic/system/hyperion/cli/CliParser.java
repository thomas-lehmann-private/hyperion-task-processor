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
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Parser for command line arguments.
 *
 * @author Thomas Lehmann
 */
public class CliParser {
    /**
     * Pattern for a long name option of the form --name=value.
     */
    private static final Pattern LONG_NAME_PATTERN_WITH_ASSIGNMENT
            = Pattern.compile("^--([^=]*)=([^=]*)$");

    /**
     * Pattern for a long name option of the form --name either as boolean
     * or with a value as next argument.
     */
    private static final Pattern LONG_NAME_PATTERN_WITHOUT_ASSIGNMENT
            = Pattern.compile("^--([^=]*)$");

    /**
     * Value for boolean option.
     */
    private static final String TRUE = "true";

    /**
     * All global options.
     */
    private final CliOptionList globalOptions;

    /**
     * List of commands.
     */
    private final List<CliCommand> commands;

    /**
     * Result builder.
     */
    private final CliResult.Builder resultBuilder;

    /**
     * Optional command.
     */
    private Optional<CliCommand> command;

    /**
     * Initialize parser with values from the builder.
     *
     * @param builder builder with the defined and validated options and commands.
     * @since 1.0.0
     */
    private CliParser(final Builder builder) {
        this.globalOptions = builder.globalOptions;
        this.commands = builder.commands;
        this.resultBuilder = CliResult.builder();
        this.command = Optional.empty();
    }

    /**
     * Parsing command line arguments.
     *
     * @param arguments command line arguments.
     * @return result of parsing.
     * @throws CliException when parsing has failed.
     * @since 1.0.0
     */
    public CliResult parse(final String[] arguments) throws CliException {
        var iProcessedArguments = 0;
        var iPos = 0;
        while (iPos < arguments.length) {
            final String strArgument = arguments[iPos];
            if (parseLongOptionWithAssignment(strArgument) > 0) {
                ++iPos;
                continue;
            }

            final String strNextArgument = iPos + 1 < arguments.length ? arguments[iPos + 1] : "";
            iProcessedArguments = parseLongOptionWithoutAssignment(strArgument, strNextArgument);
            if (iProcessedArguments > 0) {
                iPos += iProcessedArguments;
                continue;
            }

            iProcessedArguments = parseShortOption(strArgument, strNextArgument);
            if (iProcessedArguments > 0) {
                iPos += iProcessedArguments;
                continue;
            }

            if (parseCommandName(strArgument) > 0) {
                ++iPos;
                continue;
            }

            throw new CliException(CliMessages.UNKNOWN_OPTION.getMessage());
        }
        validate();
        return this.resultBuilder.build();
    }

    /**
     * Trying to parse a command name.
     *
     * @param strArgument current argument to process.
     * @return number of arguments processed
     * @throws CliException when more than once command name is specified.
     * @since 1.0.0
     */
    private int parseCommandName(final String strArgument) throws CliException {
        var iProcessArguments = 0;
        final var optionalCommand = this.commands.stream()
                .filter(entry -> entry.getName().equals(strArgument)).findFirst();

        if (optionalCommand.isPresent()) {
            if (this.command.isPresent()) {
                throw new CliException(
                        CliMessages.MORE_THAN_ONE_COMMAND_NOT_ALLOWED.getMessage());
            } else {
                this.command = optionalCommand;
                this.resultBuilder.setCommandName(optionalCommand.get().getName());
                iProcessArguments = 1;
            }
        }

        return iProcessArguments;
    }

    /**
     * Parsing long option of the form --name=value.
     *
     * @param strArgument the name of the long name option.
     * @return number of arguments processed
     * @since 1.0.0
     */
    private int parseLongOptionWithAssignment(final String strArgument) {
        var iProcessArguments = 0;
        final var matcher = LONG_NAME_PATTERN_WITH_ASSIGNMENT.matcher(strArgument);
        if (matcher.find()) {
            if (this.command.isPresent()) {
                iProcessArguments = processOptionForCommand(
                        matcher.group(1), matcher.group(2), true);
            } else {
                iProcessArguments = processOptionForGlobal(
                        matcher.group(1), matcher.group(2), true);
            }
        }
        return iProcessArguments;
    }

    /**
     * Parsing long option of the form --name.
     * When the type is boolean no further argument is required.
     *
     * @param strArgument     the name of the long name option.
     * @param strNextArgument the optional value for the option.
     * @return number of arguments processed
     * @since 1.0.0
     */
    private int parseLongOptionWithoutAssignment(final String strArgument,
                                                 final String strNextArgument) {
        var iProcessArguments = 0;
        final var matcher = LONG_NAME_PATTERN_WITHOUT_ASSIGNMENT.matcher(strArgument);
        if (matcher.find()) {
            if (this.command.isPresent()) {
                iProcessArguments = processOptionForCommand(
                        matcher.group(1), strNextArgument, false);
            } else {
                iProcessArguments = processOptionForGlobal(
                        matcher.group(1), strNextArgument, false);
            }
        }
        return iProcessArguments;
    }

    /**
     * Parsing short option.
     *
     * @param strArgument argument of form -char.
     * @param strNextArgument optional next parameter after that option.
     * @return number of arguments processed
     * @since 1.0.0
     */
    private int parseShortOption(final String strArgument, final String strNextArgument) {
        var iProcessedArguments = 0;

        if (strArgument.startsWith("-")) {
            final boolean bWithAssignment = strArgument.length() > 2;
            final String strShortOption = strArgument.substring(1, 2);
            final String strFinalNextArgument
                    = bWithAssignment? strArgument.substring(2): strNextArgument;

            if (this.command.isPresent()) {
                iProcessedArguments = processOptionForCommand(
                        strShortOption, strFinalNextArgument, bWithAssignment);
            } else {
                iProcessedArguments = processOptionForGlobal(
                        strShortOption, strFinalNextArgument, bWithAssignment);
            }
        }

        return iProcessedArguments;
    }

    /**
     * Process either short option or long option for command.
     *
     * @param strName         name of the option
     * @param strNextArgument next argument after that option
     * @param bWithAssignment when true just one argument is processed/consumed.
     * @return number of arguments processed
     * @since 1.0.0
     */
    private int processOptionForCommand(final String strName, final String strNextArgument,
                                        final boolean bWithAssignment) {
        var iProcessArguments = 0;
        final var optionalOption = this.command.get().findOption(strName);
        if (optionalOption.isPresent()) {
            if (optionalOption.get().getType().equals(OptionType.BOOLEAN)) {
                this.resultBuilder.addCommandOption(
                        optionalOption.get().getLongName(), TRUE);
                iProcessArguments = 1;
            } else {
                this.resultBuilder.addCommandOption(
                        optionalOption.get().getLongName(), strNextArgument);
                iProcessArguments = bWithAssignment ? 1 : 2;
            }
        }
        return iProcessArguments;
    }

    /**
     * Process long option for global option.
     *
     * @param strName         name of the option
     * @param strNextArgument next argument after that option
     * @param bWithAssignment when true just one argument is processed/consumed.
     * @return number of arguments processed
     * @since 1.0.0
     */
    private int processOptionForGlobal(final String strName, final String strNextArgument,
                                       final boolean bWithAssignment) {
        var iProcessArguments = 0;
        final var optionalOption = this.globalOptions.findOption(strName);
        if (optionalOption.isPresent()) {
            if (optionalOption.get().getType().equals(OptionType.BOOLEAN)) {
                this.resultBuilder.addGlobalOption(
                        optionalOption.get().getLongName(), TRUE);
                iProcessArguments = 1;
            } else {
                this.resultBuilder.addGlobalOption(
                        optionalOption.get().getLongName(), strNextArgument);
                iProcessArguments = bWithAssignment ? 1 : 2;
            }
        }
        return iProcessArguments;
    }

    /**
     * Validating parsed options.
     *
     * @throws CliException when validation has failed.
     */
    private void validate() throws CliException {
        for (final var option : this.globalOptions) {
            if (option.isRequired()
                    && !this.resultBuilder.hasGlobalOption(option.getLongName())) {
                throw new CliException(CliMessages.REQUIRED_OPTION_MISSING.getMessage());
            }

            if (!option.isRepeatable()
                    && this.resultBuilder.countGlobalOption(option.getLongName()) > 1) {
                throw new CliException(CliMessages.OPTION_NOT_REPEATABLE.getMessage());
            }
        }
    }

    /**
     * Creating the builder.
     *
     * @return Builder for this class.
     * @since 1.0.0
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Creating an instance of {@link CliParser}.
     */
    public static class Builder extends AbstractFinalBuilder<CliParser> {
        /**
         * Create instance of {@link CliParser}.
         *
         * @return instance of {@link CliParser}.
         * @throws CliException when validation has failed.
         * @since 1.0.0
         */
        @Override
        public CliParser build() throws CliException {
            if (this.globalOptions == null) {
                this.globalOptions = CliOptionList.builder().build();
            }
            return new CliParser(this);
        }
    }
}
