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
package magic.system.hyperion.components;


/**
 * Provide document result.
 *
 * @author Thomas Lehmann
 */
public final class DocumentResult {
    /**
     * When true then document run has been successful.
     */
    private final boolean bSuccess;

    /**
     * Initialize with result.
     * @param bInitSuccess when true then document run has been successful.
     */
    private DocumentResult(final boolean bInitSuccess) {
        this.bSuccess = bInitSuccess;
    }

    /**
     * Get success of document run.
     *
     * @return true when document run has been successful.
     * @since 2.0.0
     */
    public boolean isSuccess() {
        return bSuccess;
    }

    /**
     * Creating instance of {@link DocumentResult}.
     *
     * @param bInitSuccess when true then document run has been successful.
     * @return Instance of {@link DocumentResult}.
     */
    public static DocumentResult of(final boolean bInitSuccess) {
        return new DocumentResult(bInitSuccess);
    }
}
