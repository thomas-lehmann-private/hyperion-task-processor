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
    POWERSHELL("powershell"),

    /**
     * Type for Windows batch task.
     */
    BATCH("batch"),

    /**
     * Type for Unix shell task.
     */
    SHELL("shell"),

    /**
     * Type for Groovy task.
     */
    GROOVY("groovy"),

    /**
     * Type for JShell task.
     */
    JSHELL("jshell"),

    /**
     * Type for docker container task.
     */
    DOCKER_CONTAINER("docker-container"),

    /**
     * Type for docker image task.
     */
    DOCKER_IMAGE("docker-image");

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
     * @version 1.0.0
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
     */
    public static TaskType fromValue(final String strName)
            throws HyperionException {
        for (final var value: values()) {
            if (value.getTypeName().equals(strName)) {
                return value;
            }
        }

        throw new HyperionException(String.format("Unknown task type '" + strName + "'!"));
    }
}
