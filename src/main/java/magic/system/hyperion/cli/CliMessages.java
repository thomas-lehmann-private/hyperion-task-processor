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

/**
 * Error messages for the command line interface api.
 *
 * @author Thomas Lehmann
 */
public enum CliMessages {
    /**
     * Validation has failed because two options (boolean type and repeatable)
     * do not match.
     */
    BOOLEAN_OPTION_NOT_REPEATABLE("Boolean options are not repeatable!"),

    /**
     * Validation for description has failed (missing: null or empty).
     */
    OPTION_DESCRIPTION_MISSING("Description is not set!"),

    /**
     * Validation for description has failed (too long).
     */
    OPTION_DESCRIPTION_TOO_LONG("Description too long (maximum: 30 characters)!"),

    /**
     * Validation has failed because at least two option do have same description.
     */
    OPTION_DESCRIPTION_MORE_THAN_ONCE("Same description specified more than once!"),

    /**
     * Validation of the short name has failed.
     */
    OPTION_SHORT_NAME_INVALID("Short name is not set correctly!"),

    /**
     * Validation has failed because at least two options have the same short name.
     */
    OPTION_SHORT_NAME_MORE_THAN_ONCE("Same short name specified more than once"),

    /**
     * Validation of long name has failed (number of sub names).
     */
    OPTION_TOO_MANY_SUBNAMES("Long name can not have more than 3 sub names!"),

    /**
     * Validation of long (sub) name has failed (regex).
     */
    OPTION_LONG_SUBNAME_INVALID("Long (sub) name is not set correctly!"),

    /**
     * Validation of long name has failed (missing: null or empty).
     */
    OPTION_LONG_NAME_MISSING("Long name is not set!"),

    /**
     * Validation has failed because at least two options have the same long name.
     */
    OPTION_LONG_NAME_MORE_THAN_ONCE("Same long name specified more than once"),

    /**
     * Validation for command name has failed (missing: null or empty).
     */
    COMMAND_NAME_MISSING("Command name is not set!"),

    /**
     * Validation for command name has failed (regex).
     */
    COMMAND_NAME_INVALID("Name is not set correctly!");

    /**
     * Concrete message.
     */
    private final String strMessage;

    /**
     * Initialize enum with concrete message.
     *
     * @param strValue the message.
     */
    CliMessages(final String strValue) {
        this.strMessage = strValue;
    }

    /**
     * Get concrete message.
     *
     * @return concrete message.
     */
    public String getMessage() {
        return this.strMessage;
    }
}
