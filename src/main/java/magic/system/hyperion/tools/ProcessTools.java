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

import magic.system.hyperion.generics.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * Process tools.
 *
 * @author Thomas Lehmann
 */
public final class ProcessTools {
    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessTools.class);

    /**
     * Provide stdout and stderr lines of the process and logging those outputs too.
     *
     * @param process the process to capture the output from.
     * @return lines of stdout and stderr.
     */
    public static Pair<List<String>, List<String>> captureOutput(final Process process) {
        final List<String> stdout = new Vector<>();
        final List<String> stderr = new Vector<>();

        final var stdoutCaptureThread = createCaptureThread(process.getInputStream(), stdout);
        final var stderrCaptureThread = createCaptureThread(process.getErrorStream(), stderr);

        stdoutCaptureThread.start();
        stderrCaptureThread.start();

        try {
            // waiting for the finishing of both threads.
            stdoutCaptureThread.join();
            stderrCaptureThread.join();
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }

        return Pair.of(Collections.unmodifiableList(stdout), Collections.unmodifiableList(stderr));
    }

    /**
     * The capture thread implementation capturing either stdout or stderr depending on the
     * the passes stream.
     *
     * @param stream either {@link Process#getInputStream()} or {@link Process#getErrorStream()}.
     * @param capturedLines container to add captured lines.
     * @return Thread to be started.
     */
    private static Thread createCaptureThread(final InputStream stream,
                                              final List<String> capturedLines) {
        return  new Thread(() -> {
            try (var reader = new BufferedReader(
                    new InputStreamReader(stream, Charset.defaultCharset()))) {

                String strLine;
                while ((strLine = reader.readLine()) != null) {
                    capturedLines.add(strLine);
                    LOGGER.info(strLine);
                }
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        });
    }
}
