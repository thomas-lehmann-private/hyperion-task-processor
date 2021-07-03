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
package magic.system.hyperion.tools;

/**
 * Used file extensions (without leading dot).
 */
public enum FileExtensions {
    /**
     * Powershell.
     */
    PS1("ps1"),

    /**
     * Windows batch.
     */
    BAT("bat"),

    /**
     * Windows command shell (same as Windows batch).
     */
    CMD("cmd"),

    /**
     * Unix shell.
     */
    SH("sh"),

    /**
     * Groovy file.
     */
    GROOVY("groovy");

    /**
     * File extension (without leading dot).
     */
    private final String strValue;

    /**
     * Initialize concrete file extension (without leading dot).
     *
     * @param strInitValue concrete file extension.
     * @since 1.0.0
     */
    FileExtensions(final String strInitValue) {
        this.strValue = strInitValue;
    }

    /**
     * Get value.
     *
     * @return value.
     * @since 1.0.0
     */
    public String getValue() {
        return this.strValue;
    }
}
