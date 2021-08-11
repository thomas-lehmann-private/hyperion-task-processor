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

/**
 * HTTP status codes as number constants as well as string constants.
 *
 * @author Thomas Lehmann
 */
public enum HttpStatus {
    /**
     * Request has been successfully.
     */
    OK(Constants.OK),

    /**
     * Resource has not been found.
     */
    NOT_FOUND(Constants.NOT_FOUND);

    /**
     * String value of HTTP status.
     */
    private final String strStatus;

    HttpStatus(final String strInitStatus) {
        this.strStatus = strInitStatus;
    }

    /**
     * Get integer status.
     * @return integer status.
     */
    public int getStatus() {
        return Integer.parseInt(this.strStatus);
    }

    /**
     * HTTP Status as string values.
     */
    public static class Constants {
        /**
         * String value for status when request has been successfully.
         */
        public static final String OK = "200";

        /**
         * String value for status when resource has not been found.
         */
        public static final String NOT_FOUND = "404";
    }
}
