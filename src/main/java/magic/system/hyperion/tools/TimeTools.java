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

import magic.system.hyperion.interfaces.ICondition;

/**
 * Time tools.
 *
 * @author Thomas Lehmann
 */
public final class TimeTools {
    /**
     * Factor for converting minutes into milliseconds.
     */
    private static final int MINUTES_AS_MILLISECONDS = 60000;

    /**
     * Instantiation not allowed.
     */
    private TimeTools() {
        // nothing to do.
    }

    /**
     * Waiting for a condition getting true.
     *
     * @param condition any condition (functional interface possible).
     * @param iTimeout time in millisecond to wait for condition being true
     * @param iWait time in milliseconds before checking condition again.
     * @return true when condition has been true before timeout.
     * @throws InterruptedException when sleep has been interrupted.
     * @since 2.0.0
     */
    public static boolean wait(final ICondition condition, final long iTimeout, final long iWait)
            throws InterruptedException {
        assert iWait < iTimeout;

        final long iStart = System.currentTimeMillis();
        long iDuration = 0;

        while (iDuration < iTimeout) {
            if (condition.isTrue()) {
                break;
            }

            Thread.sleep(iWait);
            iDuration = System.currentTimeMillis() - iStart;
        }

        return iDuration < iTimeout;
    }

    /**
     * Get minutes in milliseconds.
     *
     * @param iValue minutes
     * @return milliseconds.
     * @since 2.0.0
     */
    public static int minutesAsMilliseconds(final int iValue) {
        return iValue * MINUTES_AS_MILLISECONDS;
    }
}
