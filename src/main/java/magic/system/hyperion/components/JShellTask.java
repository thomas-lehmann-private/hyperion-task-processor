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

import jdk.jshell.JShell;
import jdk.jshell.Snippet;
import magic.system.hyperion.interfaces.IVariable;
import magic.system.hyperion.tools.TemplateEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Provide JShell task. The JShell task is not considered for file usage.
 */
public class JShellTask extends AbstractTask {
    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(JShellTask.class);

    /**
     * Initialize JShell task.
     *
     * @param strInitTitle - title of the task.
     * @param strInitCode - Path and name of file of script or inline script.
     */
    public JShellTask(final String strInitTitle, final String strInitCode) {
        super(strInitTitle, strInitCode);
    }

    @Override
    public TaskResult run(final Map<String, IVariable> variables) {
        TaskResult taskResult = null;
        final var engine = new TemplateEngine();
        final var renderedText = engine.render(getCode(),
                Map.of("variables", variables));

        try {
            final var stream1 = new ByteArrayOutputStream();
            final var stream2 = new PrintStream(stream1, true, Charset.defaultCharset());

            final var shell = JShell.builder().out(stream2).build();
            final var events = shell.eval(renderedText);

            // checking for problems:
            for (final var event: events) {
                if (event.status().equals(Snippet.Status.REJECTED)) {
                    taskResult = new TaskResult(false, getVariable());
                    LOGGER.error(event.snippet().toString());
                    break;
                }
            }

            if (taskResult == null) {
                getVariable().setValue(
                        new String(stream1.toByteArray(), Charset.defaultCharset()));
                taskResult = new TaskResult(true, getVariable());
            }
        } catch (IllegalStateException e) {
            taskResult = new TaskResult(false, getVariable());
        }

        return taskResult;

    }
}
