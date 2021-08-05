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
package magic.system.hyperion.server;

import magic.system.hyperion.exceptions.HyperionException;

/**
 * Path segements for REST calls.
 *
 * @author Thomas Lehmann
 */
public enum PathSegment {
    /**
     * Root path segment for document requests.
     * See {@link magic.system.hyperion.server.controller.DocumentsController}.
     */
    DOCUMENTS(Constants.DOCUMENTS);

    /**
     * Path segment name.
     */
    private final String strName;

    /**
     * Initialize path segment name.
     *
     * @param strInitSegmentName path segment name.
     * @since 2.0.0
     */
    PathSegment(final String strInitSegmentName) {
        this.strName = strInitSegmentName;
    }

    /**
     * Get path segment name.
     *
     * @return type name.
     * @since 2.0.0
     */
    public String getSegmentName() {
        return this.strName;
    }

    /**
     * Trying to convert string into enum value.
     *
     * @param strInitSegmentName path segment name.
     * @return found enum value
     * @throws HyperionException when the name is not known.
     * @since 2.0.0
     */
    public static PathSegment fromValue(final String strInitSegmentName)
            throws HyperionException {
        for (final var value: values()) {
            if (value.getSegmentName().equals(strInitSegmentName)) {
                return value;
            }
        }

        throw new HyperionException("Unknown path segment name '" + strInitSegmentName + "'!");
    }

    /**
     * Constants for reuse (like annotations).
     *
     * @author Thomas Lehmann
     */
    public static class Constants {
        /**
         * Value for root path segment of document requests.
         */
        public static final String DOCUMENTS = "documents";
    }
}
