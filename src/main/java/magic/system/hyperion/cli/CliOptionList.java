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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toSet;

/**
 * Wrapper for list of options and the related validation.
 *
 * @author Thomas Lehmann
 * @since 1.0.0
 */
public class CliOptionList implements Iterable<CliOption> {
    /**
     * List of assigned options.
     */
    private final List<CliOption> options;

    /**
     * Initialize option list with option list from builder.
     *
     * @param builder builder containing the option list.
     */
    private CliOptionList(final Builder builder) {
        this.options = builder.options;
    }

    /**
     * Get options.
     *
     * @return options.
     */
    public List<CliOption> getOptions() {
        return Collections.unmodifiableList(this.options);
    }

    /**
     * Trying to find option via short name or long name.
     *
     * @param strOption either short name or long name.
     * @return optional option.
     */
    public Optional<CliOption> findOption(final String strOption) {
        Optional<CliOption> optionalOption = Optional.empty();

        if (strOption != null && !strOption.isEmpty()) {
            optionalOption = this.options.stream().filter(
                    option -> strOption.equals(option.getLongName())
                            || strOption.equals(option.getShortName())).findFirst();
        }
        return optionalOption;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append(this.options).build();
    }

    @Override
    public Iterator<CliOption> iterator() {
        return this.options.iterator();
    }

    /**
     * Provide builder for this class.
     *
     * @return Builder for this class.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder of a {@link CliOptionList}.
     */
    public static class Builder {
        /**
         * List of assigned options.
         */
        private final List<CliOption> options;

        /**
         * Initialize builder.
         */
        public Builder() {
            this.options = new ArrayList<>();
        }

        /**
         * Add a new option.
         *
         * @param option new option.
         * @return builder itself to allow chaining.
         */
        public Builder add(final CliOption option) {
            this.options.add(option);
            return this;
        }

        /**
         * Create instance of {@link CliOptionList}.
         *
         * @return instance of {@link CliOptionList}.
         * @throws CliException when validation has failed.
         */
        public CliOptionList build() throws CliException {
            validateOptionDescription();
            validateOptionLongNames();
            validateOptionShortNames();
            return new CliOptionList(this);
        }

        /**
         * Checking for duplicate option descriptions.
         *
         * @throws CliException when there are duplicate option descriptions.
         */
        private void validateOptionDescription() throws CliException {
            final var descriptions = this.options.stream().map(
                    CliOption::getDescription).collect(toSet());
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
            final var longNames = this.options.stream().map(CliOption::getLongName).collect(
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
                    .map(CliOption::getShortName).filter(isNull.or(String::isEmpty)).count();
            final var shortNames = this.options.stream()
                    .map(CliOption::getShortName).filter(
                            isNotNull.and(not(String::isEmpty))).collect(toSet());

            if (shortNames.size() != this.options.size() - iIgnorableShortNames) {
                throw new CliException(CliMessages.OPTION_SHORT_NAME_MORE_THAN_ONCE.getMessage());
            }
        }
    }
}
