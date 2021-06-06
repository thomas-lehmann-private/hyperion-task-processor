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
 * Application options.
 *
 * @author Thomas Lehmann
 */
public enum ApplicationOptions {
    /**
     * Repeatable option to allow filtering for tasks.
     */
    TAG("t", "tag", "provide tag to filter tasks"),

    /**
     * Option for displaying the third party information.
     */
    THIRD_PARTY("", "third-party", "displaying used 3rd party libraries"),

    /**
     * Timeout on each taskgroup.
     */
    TIMEOUT_TASKGROUP("", "timeout-taskgroup", "timeout for each taskgroup (minutes)"),

    /**
     * Option for displaying the help.
     */
    HELP("h", "help", "displaying this help"),

    /**
     * FDor the command "run" the option file to specify document to process tasks.
     */
    RUN_FILE("f", "file", "Document with tasks to be processed");

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
     * @version 1.0.0
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
     * @version 1.0.0
     */
    public String getShortName() {
        return this.strShortName;
    }

    /**
     * Get long name of option.
     *
     * @return long name of option.
     * @version 1.0.0
     */
    public String getLongName() {
        return this.strLongName;
    }

    /**
     * Get description of option.
     *
     * @return description of option.
     * @version 1.0.0
     */
    public String getDescription() {
        return this.strDescription;
    }
}
