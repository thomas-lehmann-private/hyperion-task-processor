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
package magic.system.hyperion.components.tasks;

import magic.system.hyperion.exceptions.HyperionException;

/**
 * Type of task.
 *
 * @author Thomas Lehmann
 */
public enum TaskType {
    /**
     * Type for Powershell task.
     */
    POWERSHELL(Constants.POWERSHELL),

    /**
     * Type for Windows batch task.
     */
    BATCH(Constants.BATCH),

    /**
     * Type for Unix shell task.
     */
    SHELL(Constants.SHELL),

    /**
     * Type for Groovy task.
     */
    GROOVY(Constants.GROOVY),

    /**
     * Type for JShell task.
     */
    JSHELL(Constants.JSHELL),

    /**
     * Type for docker container task.
     */
    DOCKER_CONTAINER(Constants.DOCKER_CONTAINER),

    /**
     * Type for docker image task.
     */
    DOCKER_IMAGE(Constants.DOCKER_IMAGE),

    /**
     * Type for copy file task.
     */
    COPY_FILE(Constants.COPY_FILE),

    /**
     * Type for xsl transform task.
     */
    XSLT(Constants.XSLT),

    /**
     * Type for write file task.
     */
    WRITE_FILE(Constants.WRITE_FILE),

    /**
     * Type for download task.
     */
    DOWNLOAD(Constants.DOWNLOAD),

    /**
     * Type for Python task.
     */
    PYTHON(Constants.PYTHON);

    /**
     * Type of task (the concrete type name to be used in YAML).
     */
    private final String strTypeName;

    /**
     * Initialize type with name.
     *
     * @param strInitTypeName type name.
     */
    TaskType(final String strInitTypeName) {
        this.strTypeName = strInitTypeName;
    }

    /**
     * Get type name.
     *
     * @return type name.
     * @since 1.0.0
     */
    public String getTypeName() {
        return this.strTypeName;
    }

    /**
     * Trying to convert string into enum value.
     *
     * @param strName internal (field-)name of the enum value.
     * @return found enum value
     * @throws HyperionException when the name is not known.
     * @since 1.0.0
     */
    public static TaskType fromValue(final String strName)
            throws HyperionException {
        for (final var value : values()) {
            if (value.getTypeName().equals(strName)) {
                return value;
            }
        }

        throw new HyperionException("Unknown task type '" + strName + "'!");
    }

    /**
     * Constants for reuse (like annotations).
     *
     * @author Thomas Lehmann
     */
    public static class Constants {
        /**
         * Value for type for Powershell task.
         */
        public static final String POWERSHELL = "powershell";

        /**
         * Value for type for Windows batch task.
         */
        public static final String BATCH = "batch";

        /**
         * Value for type for Unix shell task.
         */
        public static final String SHELL = "shell";

        /**
         * Value for type for Groovy task.
         */
        public static final String GROOVY = "groovy";

        /**
         * Value for type for JShell task.
         */
        public static final String JSHELL = "jshell";

        /**
         * Value for type for docker container task.
         */
        public static final String DOCKER_CONTAINER = "docker-container";

        /**
         * Value for type for docker image task.
         */
        public static final String DOCKER_IMAGE = "docker-image";

        /**
         * Value for type for copy file task.
         */
        public static final String COPY_FILE = "copy-file";

        /**
         * Value for type for xsl transform task.
         */
        public static final String XSLT = "xsl-transform";

        /**
         * Value for type for write file task.
         */
        public static final String WRITE_FILE = "write-file";

        /**
         * Value for type for Download task.
         */
        public static final String DOWNLOAD = "download";

        /**
         * Value for type for Python task.
         */
        public static final String PYTHON = "python";
    }
}
