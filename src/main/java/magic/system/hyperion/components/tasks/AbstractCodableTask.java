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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

/**
 * Base class for codable tasks.
 *
 * @author Thomas Lehmann
 */
public abstract class AbstractCodableTask extends AbstractTask {
    /**
     * Path and name of file of script or inline script.
     */
    private String strCode;

    /**
     * Initialize task.
     *
     * @param strInitTitle - title of the task.
     * @param strInitCode - path and name of file of script or inline script.
     * @since 1.0.0
     */
    public AbstractCodableTask(final String strInitTitle, final String strInitCode) {
        super(strInitTitle);
        this.strCode = strInitCode;
    }

    /**
     * Provide Script code or path and filename to script.
     *
     * @return Path and name of file of script or inline script.
     * @since 1.0.0
     */
    public String getCode() {
        return this.strCode;
    }

    /**
     * Change code.
     *
     * @param strInitCode new value for code.
     * @since 1.0.0
     */
    public void setCode(final String strInitCode) {
        this.strCode = strInitCode;
    }

    /**
     * Checking for code to represent an existing path and filename.
     *
     * @return true when given code represents an existing path and filename.
     * @since 1.0.0
     */
    public boolean isRegularFile() {
        boolean success;
        try {
            success = Files.isRegularFile(Paths.get(this.strCode));
        } catch (InvalidPathException e) {
            success = false;
        }
        return success;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .appendSuper(super.hashCode())
                .append(this.strCode)
                .build();
    }

    @Override
    public boolean equals(final Object obj) {
        if (!super.equals(obj)) {
            return false;
        }

        final var other = (AbstractCodableTask) obj;
        return new EqualsBuilder()
                .append(this.strCode, other.getCode())
                .build();
    }
}
