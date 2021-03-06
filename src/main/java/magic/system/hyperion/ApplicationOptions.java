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
 * Application options (global options as well as options related to a command).
 *
 * @author Thomas Lehmann
 */
public enum ApplicationOptions {
    /**
     * Repeatable option to allow filtering for tasks.
     */
    TAG("t", "tag", "provide tag to filter tasks"),

    /**
     * Timeout on each task group.
     */
    TIMEOUT_TASKGROUP("", "timeout-taskgroup", "timeout for each taskgroup (minutes)"),

    /**
     * Option for displaying the help.
     */
    HELP("h", "help", "displaying this help"),

    /**
     * For the command "run" the option file to specify document to process tasks.
     */
    RUN_FILE("f", "file", "Document with tasks to be processed"),

    /**
     *  Option to define path where to place temporary files.
     */
    TEMPORARY_PATH("", "temporary-path", "Defining temporary path"),

    /**
     * Option to define port where to run server on.
     */
    PORT("p", "port", "port to run server on");

    /**
     * Option short name.
     */
    private final String strShortName;

    /**
     * Option long name.
     */
    private final String strLongName;

    /**
     * Description of option.
     */
    private final String strDescription;

    /**
     * Initialize option.
     *
     * @param strInitShortName short name of option.
     * @param strInitLongName long name of option.
     * @param strInitDescription description of option.
     * @since 1.0.0
     */
    ApplicationOptions(final String strInitShortName, final String strInitLongName,
                       final String strInitDescription) {
        this.strShortName = strInitShortName;
        this.strLongName = strInitLongName;
        this.strDescription = strInitDescription;
    }

    /**
     * Get short name of option.
     *
     * @return short name of option.
     * @since 1.0.0
     */
    public String getShortName() {
        return this.strShortName;
    }

    /**
     * Get long name of option.
     *
     * @return long name of option.
     * @since 1.0.0
     */
    public String getLongName() {
        return this.strLongName;
    }

    /**
     * Get description of option.
     *
     * @return description of option.
     * @since 1.0.0
     */
    public String getDescription() {
        return this.strDescription;
    }
}
