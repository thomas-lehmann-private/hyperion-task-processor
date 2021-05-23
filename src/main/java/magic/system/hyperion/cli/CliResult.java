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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Represents parsing result of {@link CliParser}.
 * The validation of the result will happen there.
 *
 * @author Thomas Lehmann
 */
public class CliResult {
    /**
     * Optional command name.
     */
    private final String strCommandName;

    /**
     * Map of global options. For those which can be repeated
     * the list can have more than one value.
     */
    private final Map<String, List<String>> globalOptionsMap;

    /**
     * Map of command options. For those which can be repeated
     * the list can have more than one value.
     */
    private final Map<String, List<String>> commandOptionsMap;

    /**
     * Initialize class with values from the value.
     *
     * @param builder builder with values.
     */
    private CliResult(final Builder builder) {
        this.strCommandName = builder.strCommandName;
        this.globalOptionsMap = new TreeMap<>(builder.globalOptionsMap);
        this.commandOptionsMap = new TreeMap<>(builder.commandOptionsMap);
    }

    /**
     * Get command name.
     *
     * @return name of command (when set) otherwise null.
     */
    public String getCommandName() {
        return this.strCommandName;
    }

    /**
     * Get global options.
     *
     * @return global options.
     */
    public Map<String, List<String>> getGlobalOptions() {
        return Collections.unmodifiableMap(this.globalOptionsMap);
    }

    /**
     * Get command options.
     *
     * @return command options.
     */
    public Map<String, List<String>> getCommandOptions() {
        return Collections.unmodifiableMap(this.commandOptionsMap);
    }

    /**
     * Create builder for this class.
     *
     * @return builder for this class.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for creating instance of {@link CliResult}.
     */
    public static class Builder {
        /**
         * Optional command name.
         */
        private String strCommandName;

        /**
         * Map of global options. For those which can be repeated
         * the list can have more than one value.
         */
        private final Map<String, List<String>> globalOptionsMap;

        /**
         * Map of command options. For those which can be repeated
         * the list can have more than one value.
         */
        private final Map<String, List<String>> commandOptionsMap;

        /**
         * Initialize Builder.
         */
        public Builder() {
            this.globalOptionsMap = new TreeMap<>();
            this.commandOptionsMap = new TreeMap<>();
        }

        /**
         * Evaluate whether global option with given name does exist.
         *
         * @param strName name of the option (long name).
         * @return true when global option does exist.
         * @version 1.0.0
         */
        public boolean hasGlobalOption(final String strName) {
            return this.globalOptionsMap.containsKey(strName);
        }

        /**
         * Get number of values for an option.
         *
         * @param strName name of the option (long name)
         * @return number of values
         * @version 1.0.0
         */
        public int countGlobalOption(final String strName) {
            int iCount = 0;
            final var values = this.globalOptionsMap.get(strName);
            if (values != null) {
                iCount = values.size();
            }
            return iCount;
        }

        /**
         * Changing command name.
         *
         * @param strValue new name for command.
         * @return builder itself to allow chaining.
         * @version 1.0.0
         */
        public Builder setCommandName(final String strValue) {
            this.strCommandName = strValue;
            return this;
        }

        /**
         * Adding parsed global option.
         *
         * @param strName  name of the global option.
         * @param strValue value of the global option.
         * @return builder itself to allow chaining.
         * @version 1.0.0
         */
        public Builder addGlobalOption(final String strName, final String strValue) {
            final var entry = this.globalOptionsMap.get(strName);
            if (entry == null) {
                this.globalOptionsMap.put(strName, Stream.of(strValue).collect(toList()));
            } else {
                entry.add(strValue);
            }
            return this;
        }

        /**
         * Adding parsed command option.
         *
         * @param strName  name of the global option.
         * @param strValue value of the global option.
         * @return builder itself to allow chaining.
         * @version 1.0.0
         */
        public Builder addCommandOption(final String strName, final String strValue) {
            final var entry = this.commandOptionsMap.get(strName);
            if (entry == null) {
                this.commandOptionsMap.put(strName, Stream.of(strValue).collect(toList()));
            } else {
                entry.add(strValue);
            }
            return this;
        }

        /**
         * Create instance of {@link CliResult}.
         *
         * @return instance of {@link CliResult}.
         * @throws CliException when validation has failed.
         * @version 1.0.0
         */
        public CliResult build() throws CliException {
            validate();
            return new CliResult(this);
        }

        /**
         * Validate the result from what is possible.
         *
         * @throws CliException when the validation has failed.
         * @version 1.0.0
         */
        private void validate() throws CliException {
            if ((this.strCommandName == null || strCommandName.isEmpty())
                    && !this.commandOptionsMap.isEmpty()) {
                throw new CliException(CliMessage.COMMAND_NAME_MISSING.getMessage());
            }
        }
    }
}
