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

import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Command line option class.
 *
 * @author Thomas Lehmann
 * @since 1.0.0
 */
public class CliOption {

    /**
     * Maximum description length.
     */
    private static final int MAX_DESCRIPTION_LENGTH = 40;

    /**
     * Maximum number of subnames.
     */
    private static final int MAX_SUBNAMES = 3;

    /**
     * Long name of option.
     */
    private final String strLongName;

    /**
     * Short name of option (one char).
     */
    private final String strShortName;

    /**
     * Description of option (help).
     */
    private final String strDescription;

    /**
     * When true then option is required (default: false).
     */
    private final boolean bRequired;

    /**
     * When true then option is repeatable (default: false).
     */
    private final boolean bRepeatable;

    /**
     * Type of the option (default: STRING).
     */
    private final OptionType type;

    /**
     * Initialize option instance.
     *
     * @param builder builder with defined values.
     */
    private CliOption(final Builder builder) {
        this.strLongName = builder.strLongName;
        this.strShortName = builder.strShortName;
        this.strDescription = builder.strDescription;
        this.bRequired = builder.bRequired;
        this.bRepeatable = builder.bRepeatable;
        this.type = builder.type;
    }

    /**
     * Get long name of option.
     *
     * @return long name.
     */
    public String getLongName() {
        return this.strLongName;
    }

    /**
     * Get short name of option (one char).
     *
     * @return short name (one char).
     */
    public String getShortName() {
        return this.strShortName;
    }

    /**
     * Get description of option.
     *
     * @return description.
     */
    public String getDescription() {
        return this.strDescription;
    }

    /**
     * Check whether option is required.
     *
     * @return true when required, false when optional.
     */
    public boolean isRequired() {
        return this.bRequired;
    }

    /**
     * Check whether option is repeatable.
     *
     * @return true when repeatable, false when it can be used once only.
     */
    public boolean isRepeatable() {
        return this.bRepeatable;
    }

    /**
     * Type of the option.
     *
     * @return type.
     */
    public OptionType getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("long name", this.strLongName)
                .append("short name", this.strShortName)
                .append("description", this.strDescription)
                .append("repeatable", this.bRepeatable)
                .append("required", this.bRequired)
                .build();
    }

    /**
     * Provider a new builder.
     *
     * @return builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for an option instance.
     */
    public static class Builder {

        /**
         * Long name of option.
         */
        private String strLongName;

        /**
         * Short name of option (one char).
         */
        private String strShortName;

        /**
         * Description of option (help).
         */
        private String strDescription;

        /**
         * When true then option is required (default: false).
         */
        private boolean bRequired;

        /**
         * When true then option is repeatable (default: false).
         */
        private boolean bRepeatable;

        /**
         * Type of the option.
         */
        private OptionType type = OptionType.STRING;

        /**
         * Changing long name of option.
         *
         * @param strValue new value.
         * @return builder itself to allow chaining.
         */
        public Builder setLongName(final String strValue) {
            this.strLongName = strValue;
            return this;
        }

        /**
         * Changing short name of option.
         *
         * @param strValue new value.
         * @return builder itself to allow chaining.
         */
        public Builder setShortName(final String strValue) {
            this.strShortName = strValue;
            return this;
        }

        /**
         * Changing description of option.
         *
         * @param strValue new value.
         * @return builder itself to allow chaining.
         */
        public Builder setDescription(final String strValue) {
            this.strDescription = strValue;
            return this;
        }

        /**
         * Changing required state of option.
         *
         * @param bValue new value.
         * @return builder itself to allow chaining.
         */
        public Builder setRequired(final boolean bValue) {
            this.bRequired = bValue;
            return this;
        }

        /**
         * Changing repeatable state of option.
         *
         * @param bValue new value.
         * @return builder itself to allow chaining.
         */
        public Builder setRepeatable(final boolean bValue) {
            this.bRepeatable = bValue;
            return this;
        }

        /**
         * Changing type of option.
         *
         * @param value new value.
         * @return builder itself to allow chaining.
         */
        public Builder setType(final OptionType value) {
            this.type = value;
            return this;
        }

        /**
         * Create instance of one command line option.
         *
         * @return instance of one command line option.
         * @throws CliException when the options are not valid.
         */
        public CliOption build() throws CliException {
            validate();
            return new CliOption(this);
        }

        /**
         * Validating fields of an option.
         *
         * @throws CliException when validation has failed.
         */
        private void validate() throws CliException {
            validateDescription();
            validateShortName();
            validateLongName();

            if (this.type == OptionType.BOOLEAN && this.bRepeatable) {
                throw new CliException(
                        CliMessage.BOOLEAN_OPTION_NOT_REPEATABLE.getMessage());
            }
        }

        /**
         * Validates the description.
         *
         * @throws CliException when description is not valid.
         */
        private void validateDescription() throws CliException {
            if (this.strDescription == null || this.strDescription.isEmpty()) {
                throw new CliException(
                        CliMessage.OPTION_DESCRIPTION_MISSING.getMessage());
            }

            if (this.strDescription.length() > MAX_DESCRIPTION_LENGTH) {
                throw new CliException(
                        CliMessage.OPTION_DESCRIPTION_TOO_LONG.getMessage());
            }
        }

        /**
         * Validates the short name.
         *
         * @throws CliException when short nameis not valid.
         */
        private void validateShortName() throws CliException {
            if (this.strShortName != null && !this.strShortName.isEmpty()) {
                if (this.strShortName.length() != 1) {
                    throw new CliException(
                            CliMessage.OPTION_SHORT_NAME_INVALID.getMessage());
                }

                final var cValue = this.strShortName.toLowerCase(Locale.getDefault()).charAt(
                        0);
                if (cValue < 'a' || cValue > 'z') {
                    throw new CliException(
                            CliMessage.OPTION_SHORT_NAME_INVALID.getMessage());
                }
            }
        }

        /**
         * Validates the long name.
         *
         * @throws CliException when long nameis not valid.
         */
        private void validateLongName() throws CliException {
            if (this.strLongName == null || this.strLongName.isEmpty()) {
                throw new CliException(
                        CliMessage.OPTION_LONG_NAME_MISSING.getMessage());
            }

            final var pattern = Pattern.compile("^[a-z][a-z0-9]{2,14}$");
            final var subNames = List.of(this.strLongName.split("-"));

            if (subNames.size() > MAX_SUBNAMES) {
                throw new CliException(
                        CliMessage.OPTION_TOO_MANY_SUBNAMES.getMessage());
            }

            for (final var subName : subNames) {
                final var matcher = pattern.matcher(subName);
                if (!matcher.find()) {
                    throw new CliException(
                            CliMessage.OPTION_LONG_SUBNAME_INVALID.getMessage());
                }
            }
        }
    }
}
