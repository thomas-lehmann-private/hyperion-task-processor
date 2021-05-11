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
import java.util.OptionalInt;
import java.util.function.Consumer;

/**
 * W * Help printer for global options and commands.
 */
public class CliHelpPrinter {
    /**
     * Option line format.
     */
    public static final String OPTION_FORMAT = "    %%-%ds %%-%ds - %%s%%s";

    /**
     * All global options.
     */
    private final CliOptionList globalOptions;

    /**
     * List of commands.
     */
    private final List<CliCommand> commands;

    /**
     * Initialize help printer with values from the builder.
     *
     * @param builder builder with the defined and validated options and commands.
     * @since 1.0.0
     */
    private CliHelpPrinter(final Builder builder) {
        this.globalOptions = builder.globalOptions;
        this.commands = builder.commands;
    }

    /**
     * Printing the options.
     *
     * @param consumer can be used for different consumer.
     * @since 1.0.0
     */
    void print(final Consumer<String> consumer) {
        printGlobalOptions(consumer);
        printCommands(consumer);
    }

    /**
     * Printing the global options.
     *
     * @param consumer can be used for different consumer.
     * @since 1.0.0
     */
    private void printGlobalOptions(final Consumer<String> consumer) {
        final OptionalInt iMaxLongNameLength = this.globalOptions.getOptions().stream()
                .map(CliHelpPrinter::getLongName).mapToInt(String::length).max();
        final OptionalInt iMaxShortNameLength = this.globalOptions.getOptions().stream()
                .map(CliHelpPrinter::getShortName).mapToInt(String::length).max();

        if (iMaxLongNameLength.isPresent()) {
            consumer.accept("Global options:");

            final var strFormat = String.format(OPTION_FORMAT,
                    iMaxShortNameLength.getAsInt(),
                    iMaxLongNameLength.getAsInt());

            for (final var option : this.globalOptions) {
                consumer.accept(String.format(strFormat,
                        getShortName(option),
                        getLongName(option),
                        option.getDescription(),
                        getAdditionalInformation(option)));
            }
        }
    }

    /**
     * Printing the commands and command options.
     *
     * @param consumer can be used for different consumer.
     * @since 1.0.0
     */
    private void printCommands(final Consumer<String> consumer) {
        if (!this.commands.isEmpty()) {
            if (!this.globalOptions.getOptions().isEmpty()) {
                consumer.accept("");
            }

            int iProcessesCommands = 0;

            for (final var command : this.commands) {
                final OptionalInt iMaxLongNameLength = command.getOptions().stream()
                        .map(CliHelpPrinter::getLongName).mapToInt(String::length).max();
                final OptionalInt iMaxShortNameLength = command.getOptions().stream()
                        .map(CliHelpPrinter::getShortName).mapToInt(String::length).max();

                // we do not need to process the command if there is no option
                if (!iMaxLongNameLength.isPresent()) {
                    continue;
                }

                if (iProcessesCommands > 0) {
                    consumer.accept("");
                }

                consumer.accept(String.format("Options for command '%s':", command.getName()));

                final var strFormat = String.format(OPTION_FORMAT,
                        iMaxShortNameLength.getAsInt(),
                        iMaxLongNameLength.getAsInt());

                for (final var option : command.getOptions()) {
                    consumer.accept(String.format(strFormat,
                            getShortName(option),
                            getLongName(option),
                            option.getDescription(),
                            getAdditionalInformation(option)));
                }

                ++iProcessesCommands;
            }
        }
    }

    /**
     * Get help representation of option short name.
     *
     * @param option the option (either global option or command option)
     * @return help representation of option short name.
     * @since 1.0.0
     */
    private static String getShortName(final CliOption option) {
        String strName = "";

        if (option.getShortName() != null && !option.getShortName().isEmpty()) {
            switch (option.getType()) {
                case STRING: {
                    strName = option.getShortName() + "<str>";
                    break;
                }
                case INTEGER: {
                    strName = option.getShortName() + "<int>";
                    break;
                }
                case DOUBLE: {
                    strName = option.getShortName() + "<float>";
                    break;
                }
                case PATH: {
                    strName = option.getShortName() + "<path>";
                    break;
                }
                default: {
                    strName = option.getShortName();
                }
            }

            strName = "-" + strName + ",";
        }

        return strName;
    }

    /**
     * Get help representation of option long name.
     *
     * @param option the option (either global option or command option)
     * @return help representation of option long name.
     * @since 1.0.0
     */
    private static String getLongName(final CliOption option) {
        final String strName;

        switch (option.getType()) {
            case STRING: {
                strName = option.getLongName() + "=<str>";
                break;
            }
            case INTEGER: {
                strName = option.getLongName() + "=<int>";
                break;
            }
            case DOUBLE: {
                strName = option.getLongName() + "=<float>";
                break;
            }
            case PATH: {
                strName = option.getLongName() + "=<path>";
                break;
            }
            default: {
                strName = option.getLongName();
            }
        }

        return "--" + strName;
    }

    /**
     * Get help representation of the additional information.
     *
     * @param option the option (either global option or command option)
     * @return help representation of the additional information.
     * @since 1.0.0
     */
    public static String getAdditionalInformation(final CliOption option) {
        String strInformation = "";

        if (option.isRequired()) {
            strInformation += "required";
        }

        if (option.isRepeatable()) {
            strInformation += strInformation.isEmpty() ? "repeatable" : ", repeatable";
        }

        return strInformation.isEmpty() ? "" : " [" + strInformation + "]";
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
     * Builder for instance of {@link CliHelpPrinter}.
     */
    public static class Builder extends AbstractFinalBuilder<CliHelpPrinter> {
        /**
         * Create instance of {@link CliHelpPrinter}.
         *
         * @return instance of {@link CliHelpPrinter}.
         * @throws CliException when validation has failed.
         * @since 1.0.0
         */
        @Override
        public CliHelpPrinter build() throws CliException {
            if (this.globalOptions == null) {
                this.globalOptions = CliOptionList.builder().build();
            }
            return new CliHelpPrinter(this);
        }
    }
}
