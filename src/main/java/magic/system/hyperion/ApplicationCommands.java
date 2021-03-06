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
package magic.system.hyperion;

/**
 * Application commands.
 *
 * @author Thomas Lehmann
 */
public enum ApplicationCommands {
    /**
     * Command to process document with tasks.
     */
    RUN("run", "Running one document with tasks to be processed"),

    /**
     * Reporting the used third party libraries.
     */
    THIRD_PARTY("thirdparty", "Reporting the used third-party libraries"),

    /**
     * Command for reporting system capabilities.
     */
    CAPABILITIES("capabilities", "Reporting capabilities of system where Hyperion should run"),

    /**
     * Command for running task processor in server mode.
     */
    SERVE("serve", "Running Hyperion task processor in server mode");

    /**
     * Command name.
     */
    private final String strCommand;

    /**
     * Description of the commands.
     */
    private final String strDescription;

    /**
     * Initialize commands.
     *
     * @param strInitCommand name of the command.
     * @param strInitDescription description of the commands.
     * @since 1.0.0
     */
    ApplicationCommands(final String strInitCommand, final String strInitDescription) {
        this.strCommand = strInitCommand;
        this.strDescription = strInitDescription;
    }

    /**
     * Get name of the command.
     *
     * @return name of the command.
     * @since 1.0.0
     */
    public String getCommand() {
        return this.strCommand;
    }

    /**
     * Get description of the command.
     *
     * @return description of the command.
     * @since 1.0.0
     */
    public String getDescription() {
        return this.strDescription;
    }
}
