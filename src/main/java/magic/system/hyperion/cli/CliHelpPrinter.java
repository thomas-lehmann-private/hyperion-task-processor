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
     * Option, line format.
     */
    public static final String OPTION_FORMAT = "    %%-%ds %%-%ds - %%s%%s";

    /**
     * Command, line format.
     */
    public static final String COMMAND_FORMAT = "    %%-%ds - %%s";

    /**
     * Optional. Might be a person or an organisation.
     */
    private final String strAuthor;

    /**
     * Usually the version taken from the build (pom.xml or whatever build
     * system you use).
     */
    private final String strProductVersion;

    /**
     * Timestamp when the project has been built.
     */
    private final String strBuildTimestamp;

    /**
     * The way to execute the application.
     */
    private final String strExecution;

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
        this.strAuthor = builder.strAuthor;
        this.strProductVersion = builder.strProductVersion;
        this.strBuildTimestamp = builder.strBuildTimestamp;
        this.strExecution = builder.strExecution;
        this.globalOptions = builder.globalOptions;
        this.commands = builder.commands;
    }

    /**
     * Printing the options.
     *
     * @param consumer can be used for different consumer.
     * @since 1.0.0
     */
    public void print(final Consumer<String> consumer) {
        printHeader(consumer);
        printGlobalOptions(consumer);
        printCommandList(consumer);
        printCommands(consumer);
    }

    /**
     * Printing how to call the application, version information,
     * build timestamp (when available) and author (when available).
     *
     * @param consumer can be used for different consumer.
     * @since 1.0.0
     */
    private void printHeader(final Consumer<String> consumer) {
        consumer.accept(this.strExecution + " [global options] [command [command options]]");
        String strLine = "    version: " + this.strProductVersion;
        if (this.strBuildTimestamp != null && !this.strBuildTimestamp.isEmpty()) {
            strLine += ", build timestamp: " + this.strBuildTimestamp;
        }
        consumer.accept(strLine);
        if (this.strAuthor != null && !this.strAuthor.isEmpty()) {
            consumer.accept("    author: " + this.strAuthor);
        }
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
            consumer.accept("");
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
     * Printing list of commands.
     *
     * @param consumer can be used for different consumer.
     * @since 1.0.0
     */
    private void printCommandList(final Consumer<String> consumer) {
        final OptionalInt iMaxNameLength = this.commands.stream()
                .map(CliCommand::getName).mapToInt(String::length).max();

        if (iMaxNameLength.isPresent()) {
            consumer.accept("");

            final var strFormat = String.format(COMMAND_FORMAT,
                    iMaxNameLength.getAsInt());

            consumer.accept("List of available commands:");
            for (final var command : this.commands) {
                consumer.accept(String.format(strFormat,
                        command.getName(), command.getDescription()));
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
            consumer.accept("");
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
         * Optional. Might be a person or an organisation.
         */
        private String strAuthor;

        /**
         * Required. Usually the version taken from the build (pom.xml or whatever build
         * system you use).
         */
        private String strProductVersion;

        /**
         * Optional. Timestamp when the project has been built.
         */
        private String strBuildTimestamp;

        /**
         * Required. The way to execute the application.
         */
        private String strExecution;

        /**
         * Change author.
         *
         * @param strValue new value for author.
         * @return builder itself to allow chaining.
         * @apiNote use it before options and commands to avoid casting.
         * @since 1.0.0
         */
        public Builder setAuthor(final String strValue) {
            this.strAuthor = strValue;
            return this;
        }

        /**
         * Change product version.
         *
         * @param strValue new value for product version.
         * @return builder itself to allow chaining.
         * @apiNote use it before options and commands to avoid casting.
         * @since 1.0.0
         */
        public Builder setProductVersion(final String strValue) {
            this.strProductVersion = strValue;
            return this;
        }

        /**
         * Change build timestamp.
         *
         * @param strValue new value for build timestamp.
         * @return builder itself to allow chaining.
         * @apiNote use it before options and commands to avoid casting.
         * @since 1.0.0
         */
        public Builder setBuildTimestamp(final String strValue) {
            this.strBuildTimestamp = strValue;
            return this;
        }

        /**
         * Change execution.
         *
         * @param strValue new value for execution.
         * @return builder itself to allow chaining.
         * @apiNote use it before options and commands to avoid casting.
         * @since 1.0.0
         */
        public Builder setExecution(final String strValue) {
            this.strExecution = strValue;
            return this;
        }

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
            validate();
            return new CliHelpPrinter(this);
        }

        /**
         * Validation fields.
         *
         * @throws CliException when validation has failed.
         */
        private void validate() throws CliException {
            if (this.strExecution == null || this.strExecution.isEmpty()) {
                throw new CliException(CliMessage.HELP_EXECUTION_INFORMATION_MISSING.getMessage());
            }

            if (this.strProductVersion == null || this.strProductVersion.isEmpty()) {
                throw new CliException(CliMessage.HELP_PRODUCT_VERSION_MISSING.getMessage());
            }
        }
    }
}
