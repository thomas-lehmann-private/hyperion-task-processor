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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

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
     * Timestamp when document processing has started (in UTC).
     */
    private final ZonedDateTime started;

    /**
     * Timestamp when document processing has finished (in UTC).
     */
    private final ZonedDateTime finished;

    /**
     * Initialize with result.
     *
     * @param bInitSuccess When true then document run has been successful.
     * @param initStarted  Timestamp when document processing has started (in UTC).
     * @param initFinished Timestamp when document processing has finished (in UTC).
     */
    private DocumentResult(final boolean bInitSuccess, final ZonedDateTime initStarted,
                           final ZonedDateTime initFinished) {
        this.bSuccess = bInitSuccess;
        this.started = initStarted;
        this.finished = initFinished;
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
     * Get timestamp when document processing has started (in UTC).
     *
     * @return timestamp in UTC
     * @since 2.0.0
     */
    public ZonedDateTime getStarted() {
        return this.started;
    }

    /**
     * Get timestamp when document processing has finished (in UTC).
     *
     * @return timestamp in UTC
     * @since 2.0.0
     */
    public ZonedDateTime getFinished() {
        return this.finished;
    }

    /**
     * Creating instance of {@link DocumentResult}.
     *
     * @param bInitSuccess when true then document run has been successful.
     * @param initStarted  Timestamp when document processing has started (in UTC).
     * @param initFinished Timestamp when document processing has finished (in UTC).
     * @return Instance of {@link DocumentResult}.
     * @since 2.0.0
     */
    @JsonCreator
    public static DocumentResult of(@JsonProperty("success") final boolean bInitSuccess,
                                    @JsonProperty("started") final ZonedDateTime initStarted,
                                    @JsonProperty("finished") final ZonedDateTime initFinished) {
        return new DocumentResult(bInitSuccess, initStarted, initFinished);
    }

    /**
     * Provide negative result.
     *
     * @return Instance of {@link DocumentResult}.
     * @since 2.0.0
     */
    public static DocumentResult of() {
        // time in UTC
        final var timestamp = ZonedDateTime.now(ZoneId.of(ZoneOffset.UTC.toString()));
        return new DocumentResult(false, timestamp, timestamp);
    }
}
