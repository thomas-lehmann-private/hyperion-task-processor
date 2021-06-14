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
 * A command is a name which can have individual options.
 *
 * @author Thomas Lehmann
 * @since 1.0.0
 */
public final class CliCommand {

    /**
     * Name of the command.
     */
    private final String strName;

    /**
     * Description of the command.
     */
    private final String strDescription;

    /**
     * List of options.
     */
    private final CliOptionList options;

    /**
     * Initialize command.
     *
     * @param builder builder with data.
     */
    private CliCommand(final Builder builder) {
        this.strName = builder.strName;
        this.strDescription = builder.strDescription;
        this.options = builder.options;
    }

    /**
     * Get name of the command.
     *
     * @return name.
     * @since 1.0.0
     */
    public String getName() {
        return this.strName;
    }

    /**
     * Get description of command.
     *
     * @return description.
     * @since 1.0.0
     */
    public String getDescription() {
        return this.strDescription;
    }

    /**
     * Get the list of options for the command.
     *
     * @return list of options.
     * @since 1.0.0
     */
    public List<CliOption> getOptions() {
        return this.options.getOptions();
    }

    /**
     * Trying to find option via short name or long name.
     *
     * @param strOption either short name or long name.
     * @return optional option.
     * @since 1.0.0
     */
    public Optional<CliOption> findOption(final String strOption) {
        return this.options.findOption(strOption);
    }

    /**
     * Create builder instance.
     *
     * @return builder instance.
     * @since 1.0.0
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for a command.
     */
    public static class Builder {

        /**
         * Name of the command.
         */
        private String strName;

        /**
         * Description of the command.
         */
        private String strDescription;

        /**
         * List of assigned options.
         */
        private CliOptionList options;

        /**
         * Builder for list of options.
         */
        private final CliOptionList.Builder optionsBuilder;

        /**
         * Initialize builder.
         */
        public Builder() {
            this.optionsBuilder = CliOptionList.builder();
        }

        /**
         * Change the name of the command.
         *
         * @param strValue new name.
         * @return builder itself to allow chaining.
         */
        public Builder setName(final String strValue) {
            this.strName = strValue;
            return this;
        }

        /**
         * Change the description of the command.
         *
         * @param strValue new name.
         * @return builder itself to allow chaining.
         * @since 1.0.0
         */
        public Builder setDescription(final String strValue) {
            this.strDescription = strValue;
            return this;
        }

        /**
         * Adding an option to the command.
         *
         * @param option new option.
         * @return builder itself to allow chaining.
         * @since 1.0.0
         */
        public Builder addOption(final CliOption option) {
            this.optionsBuilder.add(option);
            return this;
        }

        /**
         * Adding a list of options to the command.
         *
         * @param optionsToAdd list of options to add.
         * @return builder itself to allow chaining.
         * @since 1.0.0
         */
        public Builder addAllOptions(final List<CliOption> optionsToAdd) {
            this.optionsBuilder.addAll(optionsToAdd);
            return this;
        }

        /**
         * Create an instance of {@link CliCommand}.
         *
         * @return instance of {@link CliCommand}.
         * @throws CliException when the options are not valid.
         * @since 1.0.0
         */
        public CliCommand build() throws CliException {
            validate();
            // does also the validation of the option list
            this.options = this.optionsBuilder.build();
            return new CliCommand(this);
        }

        /**
         * Validating fields of an option.
         *
         * @throws CliException when validation has failed.
         */
        private void validate() throws CliException {
            if (this.strName == null || this.strName.isEmpty()) {
                throw new CliException(CliMessage.COMMAND_NAME_MISSING.getMessage());
            }

            if (this.strDescription == null || this.strDescription.isEmpty()) {
                throw new CliException(CliMessage.COMMAND_DESCRIPTION_MISSING.getMessage());
            }

            final var pattern = Pattern.compile("^[a-z]{2,15}$");
            final var matcher = pattern.matcher(this.strName);
            if (!matcher.find()) {
                throw new CliException(CliMessage.COMMAND_NAME_INVALID.getMessage());
            }
        }
    }
}
