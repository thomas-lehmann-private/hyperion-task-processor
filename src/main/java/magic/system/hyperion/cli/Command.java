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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toSet;

/**
 * A command is name which can have individual options.
 *
 * @author Thomas Lehmann
 */
public final class Command {

    /**
     * Name of the command.
     */
    private final String strName;

    /**
     * List of assigned options.
     */
    private final List<Option> options;

    /**
     * Initialize command.
     *
     * @param builder builder with data.
     */
    private Command(final Builder builder) {
        this.strName = builder.strName;
        this.options = new ArrayList<>(builder.options);
    }

    /**
     * Get name of the command.
     *
     * @return name.
     */
    public String getName() {
        return this.strName;
    }

    /**
     * Get the list of options for the command.
     *
     * @return list of options.
     */
    public List<Option> getOptions() {
        return Collections.unmodifiableList(this.options);
    }

    /**
     * Create builder instance.
     *
     * @return builder instance.
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
         * List of assigned options.
         */
        private final List<Option> options;

        /**
         * Initialize builder.
         */
        public Builder() {
            this.options = new ArrayList<>();
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
         * Adding an option to the command.
         *
         * @param option new option.
         * @return builder itself to allow chaining.
         */
        public Builder addOption(final Option option) {
            this.options.add(option);
            return this;
        }

        /**
         * Create an instance of {@link Command}.
         *
         * @return instance of {@link Command}.
         * @throws CliException when the options are not valid.
         */
        public Command build() throws CliException {
            validate();
            return new Command(this);
        }

        /**
         * Validating fields of an option.
         *
         * @throws CliException when validation has failed.
         */
        private void validate() throws CliException {
            if (this.strName == null || this.strName.isEmpty()) {
                throw new CliException(CliMessages.COMMAND_NAME_MISSING.getMessage());
            }

            validateOptionDescription();
            validateOptionLongNames();
            validateOptionShortNames();
        }

        /**
         * Checking for duplicate option descriptions.
         *
         * @throws CliException when there are duplicate option descriptions.
         */
        private void validateOptionDescription() throws CliException {
            final var descriptions = this.options.stream().map(
                    Option::getDescription).collect(toSet());
            if (descriptions.size() != this.options.size()) {
                throw new CliException(CliMessages.OPTION_DESCRIPTION_MORE_THAN_ONCE.getMessage());
            }
        }

        /**
         * Checking for duplicate option long names.
         *
         * @throws CliException when there are duplicate option long names.
         */
        private void validateOptionLongNames() throws CliException {
            final var pattern = Pattern.compile("^[a-z]{2,10}$");
            final var matcher = pattern.matcher(this.strName);
            if (!matcher.find()) {
                throw new CliException(CliMessages.COMMAND_NAME_INVALID.getMessage());
            }

            final var longNames = this.options.stream().map(Option::getLongName).collect(
                    toSet());
            if (longNames.size() != this.options.size()) {
                throw new CliException(CliMessages.OPTION_LONG_NAME_MORE_THAN_ONCE.getMessage());
            }
        }

        /**
         * Checking for duplicate option short names (exception the ones that do
         * not have one.
         *
         * @throws CliException when there are duplicate option short names.
         */
        private void validateOptionShortNames() throws CliException {
            final Predicate<String> isNull = entry -> entry == null;
            final Predicate<String> isNotNull = entry -> entry != null;

            final var iIgnorableShortNames = this.options.stream()
                    .map(Option::getShortName).filter(isNull.or(String::isEmpty)).count();
            final var shortNames = this.options.stream()
                    .map(Option::getShortName).filter(
                            isNotNull.and(not(String::isEmpty))).collect(toSet());

            if (shortNames.size() != (this.options.size() - iIgnorableShortNames)) {
                throw new CliException(CliMessages.OPTION_SHORT_NAME_MORE_THAN_ONCE.getMessage());
            }
        }
    }
}
