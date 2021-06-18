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
package magic.system.hyperion.reader;

import magic.system.hyperion.exceptions.HyperionException;

/**
 * Field names.
 *
 * @author Thomas Lehmann
 */
public enum DocumentReaderFields {
    /**
     * Title for many components.
     */
    TITLE("title"),
    /**
     * Tasks for task groups.
     */
    TASKS("tasks"),
    /**
     * In a task group a field to adjust whether task should run in parallel.
     */
    PARALLEL("parallel"),
    /**
     * Mainly for tasks to indicate which one is meant.
     */
    TYPE("type"),
    /**
     * Code for a task.
     */
    CODE("code"),
    /**
     * a variable (usually optional for a task).
     */
    VARIABLE("variable"),
    /**
     * Any name (like the name of a variable).
     */
    NAME("name"),
    /**
     * Any Java regex (like the regex for a variable).
     */
    REGEX("regex"),
    /**
     * Any group (like the group for a variable).
     */
    GROUP("group"),
    /**
     * Each task may have a list of one or more tags.
     */
    TAGS("tags"),

    /**
     * Each task may have a list of one or more values and for each value the task is repeated.
     */
    WITH("with"),

    /**
     * Field which maintains the list of task groups.
     */
    TASKGROUPS("taskgroups"),

    /**
     * Main field for specifying one model inside the document.
     */
    MODEL("model"),

    /**
     * Main field for specifying the matrix.
     */
    MATRIX("matrix"),

    /**
     * Parameters of a matrix item.
     */
    PARAMETERS("parameters"),

    /**
     * Name of the Docker image.
     */
    IMAGE_NAME("image-name"),

    /**
     * Version of the Docker image.
     */
    IMAGE_VERSION("image-version"),

    /**
     * The platform used for containers by Docker (is Windows or Unix).
     */
    PLATFORM("platform"),

    /**
     * The repository:tag for the image (option -t of docker build).
     */
    REPOSITORY_TAG("repository-tag");

    /**
     * Name of the field.
     */
    private final String strFieldName;

    /**
     * Initialize constant with real field name.
     *
     * @param strInitFieldName real field name of field.
     */
    DocumentReaderFields(final String strInitFieldName) {
        this.strFieldName = strInitFieldName;
    }

    /**
     * Get real field name.
     *
     * @return read field name.
     */
    public String getFieldName() {
        return this.strFieldName;
    }

    /**
     * Trying to convert string into enum value.
     *
     * @param strName internal (field-)name of the enum value.
     * @return found enum value
     * @throws HyperionException when the name is not known.
     */
    public static DocumentReaderFields fromValue(final String strName)
            throws HyperionException {
        for (final var value: values()) {
            if (value.getFieldName().equals(strName)) {
                return value;
            }
        }

        throw new HyperionException(String.format("Unknown field '" + strName + "'!"));
    }
}
