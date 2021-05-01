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
public class ProcessResults {

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
     * @param initStdout - stdout of process.
     * @param initStderr - stderr of process.
     * @param iInitExitCode - exit code of process.
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
     */
    public List<String> getStdout() {
        return Collections.unmodifiableList(this.stdout);
    }

    /**
     * Readonly access to list of lines written to stderr.
     *
     * @return list of lines written to stderr.
     */
    public List<String> getStderr() {
        return Collections.unmodifiableList(this.stderr);
    }

    /**
     * Error code written by execution of last process.
     *
     * @return last process exit code.
     */
    public int getExitCode() {
        return this.iExitCode;
    }

    /**
     * Provding process results of last executed process given by parameter.
     *
     * @param process - last executed process.
     * @return lines written to stdout and stderr and the process exit code.
     */
    public static ProcessResults of(final Process process) {
        return new ProcessResults(ProcessTools.getStdout(process),
                ProcessTools.getStderr(process), process.exitValue());
    }
}
