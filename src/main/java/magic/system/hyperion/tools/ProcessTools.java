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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
     * Provides lines written to stdout by the given process.
     *
     * @param process - the executed process.
     * @return lines written to stdout.
     */
    public static List<String> getStdout(final Process process) {
        final List<String> lines = new ArrayList<>();

        try (var reader = new BufferedReader(new InputStreamReader(
                process.getInputStream(), Charset.defaultCharset()))) {
            lines.addAll(reader.lines().collect(Collectors.toList()));
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }

        return lines;
    }

    /**
     * Provides lines written to stderr by the given process.
     *
     * @param process - the executed process.
     * @return lines written to stderr.
     */
    public static List<String> getStderr(final Process process) {
        final List<String> lines = new ArrayList<>();

        try (var reader = new BufferedReader(new InputStreamReader(
                process.getErrorStream(), Charset.defaultCharset()))) {
            lines.addAll(reader.lines().collect(Collectors.toList()));
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }

        return lines;
    }
}
