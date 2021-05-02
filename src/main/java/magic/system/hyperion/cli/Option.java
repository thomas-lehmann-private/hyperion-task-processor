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

import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Command line option class.
 *
 * @author Thomas Lehmann
 */
public class Option {

    /**
     * Maximum description length.
     */
    private static final int MAX_DESCRIPTION_LENGTH = 30;

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
     * When true then option is required.
     */
    private final boolean bRequired;

    /**
     * When true then option is repeatable.
     */
    private final boolean bRepeatable;

    /**
     * Initialize option instance.
     *
     * @param builder builder with defined values.
     */
    private Option(final Builder builder) {
        this.strLongName = builder.strLongName;
        this.strShortName = builder.strShortName;
        this.strDescription = builder.strDescription;
        this.bRequired = builder.bRequired;
        this.bRepeatable = builder.bRepeatable;
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
         * When true then option is required.
         */
        private boolean bRequired;

        /**
         * When true then option is repeatable.
         */
        private boolean bRepeatable;

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
         * Create instance of one command line option.
         *
         * @return instance of one command line option.
         * @throws CliException when the options are not valid.
         */
        public Option build() throws CliException {
            validate();
            return new Option(this);
        }

        /**
         * Validating fields of an option.
         *
         * @throws CliException when validation has failed.
         */
        //CHECKSTYLE.OFF: NPathComplexity - complexity is ok
        //CHECKSTYLE.OFF: CyclomaticComplexity - complexity is ok
        private void validate() throws CliException {
            if (this.strLongName == null || this.strLongName.isEmpty()) {
                throw new CliException("Long name is not set!");
            }

            if (this.strShortName == null || this.strShortName.isEmpty()) {
                throw new CliException("Short name is not set!");
            }

            if (this.strDescription == null || this.strDescription.isEmpty()) {
                throw new CliException("Description is not set!");
            }

            final var pattern = Pattern.compile(
                    "^([a-z]{2,15}$|[a-z]{2,10}-[a-z]{2,10}$)");
            final var matcher = pattern.matcher(this.strLongName);
            if (!matcher.find()) {
                throw new CliException("Long name is not set correctly!");
            }

            if (this.strShortName.isEmpty()
                    || !this.strShortName.toLowerCase(
                            Locale.getDefault()).equals(this.strShortName)) {
                throw new CliException("Short name is not set correctly!");
            }

            if (this.strDescription.length() > MAX_DESCRIPTION_LENGTH) {
                throw new CliException(
                        "Description too long (maximum: 30 characters)!");
            }
        }
        //CHECKSTYLE.ON: NPathComplexity
        //CHECKSTYLE.ON: CyclomaticComplexity
    }
}
