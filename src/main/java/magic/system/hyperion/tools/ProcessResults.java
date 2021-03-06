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

import java.util.Collections;
import java.util.List;

/**
 * Keeper for process resultsd.
 *
 * @author Thomas Lehmann
 */
public final class ProcessResults {

    /**
     * Line written to stdout by the process.
     */
    private final List<String> stdout;

    /**
     * Line written to stderr by the process.
     */
    private final List<String> stderr;

    /**
     * Exit code of the process.
     */
    private final int iExitCode;

    /**
     * Initialize with result of process.
     *
     * @param initStdout    - stdout of process.
     * @param initStderr    - stderr of process.
     * @param iInitExitCode - exit code of process.
     * @since 1.0.0
     */
    private ProcessResults(final List<String> initStdout, final List<String> initStderr,
                           final int iInitExitCode) {
        this.stdout = initStdout;
        this.stderr = initStderr;
        this.iExitCode = iInitExitCode;
    }

    /**
     * Readonly access to list of lines written to stdout.
     *
     * @return list of lines written to stdout.
     * @since 1.0.0
     */
    public List<String> getStdout() {
        return Collections.unmodifiableList(this.stdout);
    }

    /**
     * Readonly access to list of lines written to stderr.
     *
     * @return list of lines written to stderr.
     * @since 1.0.0
     */
    public List<String> getStderr() {
        return Collections.unmodifiableList(this.stderr);
    }

    /**
     * Error code written by execution of last process.
     *
     * @return last process exit code.
     * @since 1.0.0
     */
    public int getExitCode() {
        return this.iExitCode;
    }

    /**
     * Providing process results of last executed process given by parameter.
     *
     * @param process - last executed process.
     * @return lines written to stdout and stderr and the process exit code.
     * @throws InterruptedException when the process gets interrupted.
     */
    public static ProcessResults of(final Process process) throws InterruptedException {
        final var result = ProcessTools.captureOutput(process, true);
        process.waitFor();
        return new ProcessResults(result.getFirst(), result.getSecond(), process.exitValue());
    }

    /**
     * Providing process results of last executed process given by parameter.
     *
     * @param process  - last executed process.
     * @param bLogging - when true then logging lines of both streams (otherwise not).
     * @return lines written to stdout and stderr and the process exit code.
     * @throws InterruptedException when the process gets interrupted.
     */
    public static ProcessResults of(final Process process, final boolean bLogging)
            throws InterruptedException {
        final var result = ProcessTools.captureOutput(process, bLogging);
        process.waitFor();
        return new ProcessResults(result.getFirst(), result.getSecond(), process.exitValue());
    }
}
